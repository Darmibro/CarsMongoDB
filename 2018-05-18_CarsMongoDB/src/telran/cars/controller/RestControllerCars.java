package telran.cars.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import telran.cars.dto.Car;
import telran.cars.dto.CarsApiConstants;
import telran.cars.dto.CarsReturnCode;
import telran.cars.dto.DateDays;
import telran.cars.dto.Driver;
import telran.cars.dto.Model;
import telran.cars.dto.RentRecord;
import telran.cars.model.dao.IRentCompany;
import telran.cars.model.dao.RentCompanyMongoDB;

@ComponentScan(basePackages="telran.cars.model.dao")
@EnableMongoRepositories("telran.cars.mongo.repo")
@RestController
public class RestControllerCars {
	@Autowired
	IRentCompany company;
	
	@PostMapping(value=CarsApiConstants.ADD_CAR_MODEL)
	CarsReturnCode addCarModel(@RequestBody Model carModel) {
		return company.addModel(carModel);
	}
	@PostMapping(value=CarsApiConstants.ADD_DRIVER)
	CarsReturnCode addDriver(@RequestBody Driver driver){
		return company.addDriver(driver);
	}
	@PostMapping(value=CarsApiConstants.ADD_CAR)
	CarsReturnCode addCar(@RequestBody Car car) {
		return company.addCar(car);
	}
	
	@PostMapping(value=CarsApiConstants.CLEAR_CARS)
	List<Car> clear(@RequestBody DateDays dateDays){
		return company.clear(dateDays.currentDate, dateDays.days);
	}
	@PostMapping(value=CarsApiConstants.REMOVE_CAR)
	CarsReturnCode removeCar(@RequestBody String carNumber) {
		return company.removeCar(carNumber);
	}
	
	@PostMapping(value=CarsApiConstants.RENT_CAR)
	CarsReturnCode rentCar(@RequestBody RentRecord record) {
		return company.rentCar(record.getCarNumber(), record.getLicenseId(), record.getRentDate(), record.getRentDays());
	}
	
	@PostMapping(value=CarsApiConstants.RETURN_CAR)
	CarsReturnCode returnCar(String carNumber, long licenseId, LocalDate returnDate, int gasTankPercent,
			int damages) {
		return company.returnCar(carNumber, licenseId, returnDate, gasTankPercent, damages);
	}
	
	@PostMapping(value=CarsApiConstants.SAVE)
	void save() {
		company.save();
	}
	
	@RequestMapping(value =CarsApiConstants.GET_MODEL)
	Model getModel(String modelName) {
		return company.getModel(modelName);
	}
	@GetMapping(value=CarsApiConstants.GET_DRIVER)
	 Driver getDriver(long licenseId) {
		 return company.getDriver(licenseId);
	 }
	
	@RequestMapping(value=CarsApiConstants.GET_ALL_CARS)
	List<Car> getAllCars(){
		return company.getAllCars().collect(Collectors.toList());
	}
	
	@RequestMapping(value=CarsApiConstants.GET_ALL_DRIVERS)
	List<Driver> getAllDrives(){
		return company.getAllDrivers().collect(Collectors.toList());
	}
	
	@RequestMapping(value=CarsApiConstants.GET_ALL_MODELS)
	List<String> getAllModels(){
		return company.getAllModels();
	}
	
	@RequestMapping(value=CarsApiConstants.GET_ALL_RECORDS)
	List<RentRecord> getAllRecords(){
		return company.getAllRecords().collect(Collectors.toList());
	}
	
	@GetMapping(value=CarsApiConstants.GET_CAR)
	Car getCar(String carNumber){
		return company.getCar(carNumber);
	}
	
	@GetMapping(value=CarsApiConstants.GET_CAR_DRIVERS)
	List<Driver> getCarDriver(String carNumber){
		return company.getCarDrivers(carNumber);
	}
	
	@GetMapping(value =CarsApiConstants.GET_DRIVER_CARS)
	List<Car> getDriverCars(long licenseId){
		return company.getDriverCars(licenseId);
	}
	
}
