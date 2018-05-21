package telran.cars.model.dao;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import telran.cars.dto.Car;
import telran.cars.dto.CarsReturnCode;
import telran.cars.dto.Driver;
import telran.cars.dto.Model;
import telran.cars.dto.RentCompanyData;
import telran.cars.dto.RentRecord;
import telran.cars.mongo.CarCrud;
import telran.cars.mongo.DriverCrud;
import telran.cars.mongo.ModelCrud;
import telran.cars.mongo.RentRecordCrud;
import telran.cars.mongo.repo.CarRepository;
import telran.cars.mongo.repo.DriversRepository;
import telran.cars.mongo.repo.ModelRepository;
import telran.cars.mongo.repo.RentRecordsRepositor;

@Service
public class RentCompanyMongoDB implements IRentCompany {
	protected RentCompanyData companyData=new RentCompanyData(); 

	@Autowired
	CarRepository cars;
	@Autowired
	DriversRepository drivers;
	@Autowired
	ModelRepository models;
	@Autowired
	RentRecordsRepositor records;

	@Override
	public CarsReturnCode addModel(Model model) {
		if (models.existsById(model.getModelName())) {
			return CarsReturnCode.CAR_EXISTS;
		}
		models.save(new ModelCrud(model));
		return CarsReturnCode.OK;
	}

	@Override
	public CarsReturnCode addCar(Car car) {
		if (models.existsById(car.getModelName()) == false) {
			return CarsReturnCode.NO_MODEL;
		}
		if (cars.existsById(car.getRegNumber())) {
			return CarsReturnCode.CAR_EXISTS;
		}
		cars.save(new CarCrud(car));
		return CarsReturnCode.OK;
	}

	@Override
	public CarsReturnCode addDriver(Driver driver) {
		if (drivers.existsById(driver.getLicenseId())) {
			return CarsReturnCode.DRIVER_EXISTS;
		}
		drivers.save(new DriverCrud(driver));
		return CarsReturnCode.OK;
	}

	@Override
	public Model getModel(String modelName) {
		ModelCrud model = models.findById(modelName).orElse(null);
		return model != null ? model.getModel() : null;
	}

	@Override
	public Car getCar(String carNumber) {
		CarCrud car = cars.findById(carNumber).orElse(null);
		return car != null ? car.getCar() : null;
	}

	@Override
	public Driver getDriver(long licenseId) {
		DriverCrud driver = drivers.findById(licenseId).orElse(null);
		return driver != null ? driver.getDriver() : null;
	}

	@Override
	public CarsReturnCode rentCar(String regNumber, long licenseId, LocalDate rentDate, int rentDays) {
		CarCrud carCrud = cars.findById(regNumber).orElse(null);

		if (carCrud == null || carCrud.isFlRemoved())
			return CarsReturnCode.NO_CAR;
		if (carCrud.isInUse())
			return CarsReturnCode.CAR_IN_USE;
		if (getDriver(licenseId) == null)
			return CarsReturnCode.NO_DRIVER;
		RentRecord record = new RentRecord(licenseId, regNumber, rentDate, rentDays);
		records.save(new RentRecordCrud(record));
		carCrud.setInUse(true);
		return CarsReturnCode.OK;
	}

	@Override
	public CarsReturnCode returnCar(String carNumber, long licenseId, LocalDate returnDate, int gasTankPercent,
			int damages) {
		CarCrud carCrud = cars.findById(carNumber).orElse(null);
		DriverCrud driverCrud = drivers.findById(licenseId).orElse(null);
		if (carCrud == null || carCrud.isInUse())
			return CarsReturnCode.CAR_NOT_RETED;
		if (driverCrud == null)
			return CarsReturnCode.NO_DRIVER;

		List<RentRecordCrud> rentRecordsCrud = records.findByCarNumber(carNumber);

		RentRecordCrud rentRecordCrud = rentRecordsCrud.stream().filter(x -> x.getReturnDate() == null).findFirst()
				.get();

		if (rentRecordCrud == null)
			throw new IllegalArgumentException("record in use doesn't exists");
		if (returnDate.isBefore(rentRecordCrud.getRentDate()))
			return CarsReturnCode.RETURN_DATE_WRONG;
		rentRecordCrud.setDamages(damages);
		rentRecordCrud.setGasTankPercent(gasTankPercent);
		rentRecordCrud.setReturnDate(returnDate);
		setCost(rentRecordCrud, carCrud);
		return null;
	}

	private void setCost(RentRecordCrud rentRecordCrud, CarCrud carCrud) {
		long period = ChronoUnit.DAYS.between(rentRecordCrud.getRentDate(), rentRecordCrud.getReturnDate());
		float costPeriod = 0;
		ModelCrud modelCrud = models.findById(carCrud.getModelName()).orElse(null);
		float costGas = 0;
		costPeriod = getCostPeriod(rentRecordCrud, period, modelCrud);
		costGas = getCostGas(rentRecordCrud, modelCrud);
		rentRecordCrud.setCost(costPeriod + costGas);

	}

	private float getCostGas(RentRecordCrud rentRecordCrud, ModelCrud modelCrud) {
		float costGas;
		int gasTank = modelCrud.getGasTank();
		float litersCost = (float) (100 - rentRecordCrud.getGasTankPercent()) * gasTank / 100;
		costGas = litersCost * companyData.getGasPrice();
		return costGas;
	}

	private float getCostPeriod(RentRecordCrud rentRecordCrud, long period, ModelCrud modelCrud) {
		float costPeriod;
		long delta = period - rentRecordCrud.getRentDays();
		float additionalPeriodCost = 0;

		if (modelCrud == null)
			throw new IllegalArgumentException("Car contains wrong model");
		int pricePerDay = modelCrud.getPriceDay();
		int rentDays = rentRecordCrud.getRentDays();
		if (delta > 0) {
			additionalPeriodCost = getAdditionalPeriodCost(pricePerDay, delta);
		}
		costPeriod = rentDays * pricePerDay + additionalPeriodCost;
		return costPeriod;
	}

	private float getAdditionalPeriodCost(int pricePerDay, long delta) {
		float fineCostPerDay = pricePerDay * companyData.getFinePercent() / 100;
		return (pricePerDay + fineCostPerDay) * delta;
	}

	@Override
	public CarsReturnCode removeCar(String carNumber) {
		CarCrud carCrud = cars.findById(carNumber).orElse(null);
		if (carCrud == null)
			return CarsReturnCode.NO_CAR;
		if (carCrud.isInUse())
			return CarsReturnCode.CAR_IN_USE;
		cars.delete(carCrud);
		return CarsReturnCode.OK;
	}
	@Override
	public List<Car> clear(LocalDate currentDate, int days) {
		LocalDate returnedDateDelet = currentDate.minusDays(days);
		/*List<RentRecordCrud> recordDelete = getRecordsForDelete(returnedDateDelet);
		records.deleteAll(recordDelete);*/

		List<CarCrud> carsCrud = cars.findAll().stream().filter(x -> !x.isInUse()).collect(Collectors.toList());
		cars.deleteAll(carsCrud);

		return carsCrud.stream().map(x -> x.getCar()).collect(Collectors.toList());
	}

/*	private List<RentRecordCrud> getRecordsForDelete(LocalDate returnedDateDelet) {
		LocalDate localDateStart = LocalDate.of(1971, 01, 01);
		return records.findByRentDataBetween(localDateStart, returnedDateDelet).stream()
				.filter(x -> getCar(x.getCarNumber()).isFlRemoved()).collect(Collectors.toList());

	}*/

	@Override
	public List<Driver> getCarDrivers(String carNumber) {
		return drivers.findAll().stream().map(DriverCrud::getDriver).collect(Collectors.toList());
	}

	@Override
	public List<Car> getDriverCars(long licenseId) {
		return cars.findAll().stream().map(CarCrud::getCar).collect(Collectors.toList());
	}

	@Override
	public Stream<Car> getAllCars() {
		return cars.findAll().stream().map(CarCrud::getCar);
	}

	@Override
	public Stream<Driver> getAllDrivers() {
		return drivers.findAll().stream().map(DriverCrud::getDriver);
	}

	@Override
	public Stream<RentRecord> getAllRecords() {

		return records.findAll().stream().map(RentRecordCrud::getRentRecord);
	}

	@Override
	public List<String> getAllModels() {

		return models.findAll().stream().map(ModelCrud::getModelName).collect(Collectors.toList());
	}

	@Override
	public void save() {

	}

	@Override
	public void setCompanyData(RentCompanyData companyData) {
		// TODO Auto-generated method stub
		
	}

}
