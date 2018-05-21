package telran.cars.controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import telran.cars.dto.Car;
import telran.cars.dto.Driver;
import telran.cars.dto.Model;
import telran.cars.dto.RentRecord;
import telran.cars.model.dao.IRentCompany;

public class RandomRentCompanyController {
private static final String MODEL = "model";
private static final String COMPANY = "company";
private static final String COUNTRY = "country";
private static final String NAME = "name";
int nCarModels=20;
int nDrivers=300;
int nCars=500;
int minGasTank=40;
int maxGasTank=70;
int minPriceDay=100;
int maxPriceDay=400;
int minBirthYear=1940;
int maxBirthYear=2000;
int probRent=40;
int probReturn=80;
int maxRentDays=30;
int probDamages=20;
int probSmallDamages=70;
int probMiddleDamages=25;
int probFullGasTank=80;
IRentCompany rentCompany;
private int maxCarCompanies=10;
private int maxCountries=5;
public RandomRentCompanyController(IRentCompany rentCompany) {
	super();
	this.rentCompany = rentCompany;
}
public void setnCarModels(int nCarModels) {
	this.nCarModels = nCarModels;
}
public void setnDrivers(int nDrivers) {
	this.nDrivers = nDrivers;
}
public void setnCars(int nCars) {
	this.nCars = nCars;
}
public void setMinGasTank(int minGasTank) {
	this.minGasTank = minGasTank;
}
public void setMaxGasTank(int maxGasTank) {
	this.maxGasTank = maxGasTank;
}
public void setMinPriceDay(int minPriceDay) {
	this.minPriceDay = minPriceDay;
}
public void setMaxPriceDay(int maxPriceDay) {
	this.maxPriceDay = maxPriceDay;
}
public void setMinBirthYear(int minBirthYear) {
	this.minBirthYear = minBirthYear;
}
public void setMaxBirthYear(int maxBirthYear) {
	this.maxBirthYear = maxBirthYear;
}
public void setProbRent(int probRent) {
	this.probRent = probRent;
}
public void setProbReturn(int probReturn) {
	this.probReturn = probReturn;
}
public void setMaxRentDays(int maxRentDays) {
	this.maxRentDays = maxRentDays;
}
public void setProbDamages(int probDamages) {
	this.probDamages = probDamages;
}
public void setProbSmallDamages(int probSmallDamages) {
	this.probSmallDamages = probSmallDamages;
}
public void setProbMiddleDamages(int probMiddleDamages) {
	this.probMiddleDamages = probMiddleDamages;
}
public void setProbFullGasTank(int probFullGasTank) {
	this.probFullGasTank = probFullGasTank;
}
public void setRentCompany(IRentCompany rentCompany) {
	this.rentCompany = rentCompany;
}
public void createRentCompany(int years){
	createRandomModels();
	createRandomDrivers();
	createRandomCars();
	imitateRentCompany(years);
}
private void imitateRentCompany(int years) {
	LocalDate current=LocalDate.now();
	LocalDate finishDate=current.plusYears(years);
	while(current.isBefore(finishDate)){
		oneDayImitation(current);
		current=current.plusDays(1);
	}
	
}
LocalDate current;
private void oneDayImitation(LocalDate current) {
	this.current=current;
	rentCars();
	returnCars();
	
}
private void returnCars() {
	rentCompany.getAllRecords()
	.filter(x->x.getReturnDate()==null&&
	ChronoUnit.DAYS.between
	(x.getRentDate(), current)>=x.getRentDays())
	.forEach(this::possibleReturn);
	
}
private void possibleReturn(RentRecord record){
	if(byChance(probReturn))
		returnCar(record);
}
private void returnCar(RentRecord record) {
	String carNumber=record.getCarNumber();
	long licenseId=record.getLicenseId();
	LocalDate returnDate=current;
	int gasTankPercent=getGasTankPercent();
	int damages=getDamages();
	rentCompany.returnCar
	(carNumber, licenseId, returnDate, gasTankPercent, damages);
	
}
private int getDamages() {
	if(byChance(probDamages)){
		if(byChance(probSmallDamages))
			return 5;
		if(byChance(probMiddleDamages))
			return 15;
		return 40;
	}
	return 0;
}
private int getGasTankPercent() {
	if(byChance(probFullGasTank))
		return 100;
	
	return getRandomNumber(1,99);
}
private void rentCars() {
	rentCompany.getAllCars().filter(x->!x.isInUse())
	.forEach(this::possibleRent);
	
}
private void possibleRent(Car car){
	if(byChance(probRent))
		rentCar(car);
}
private void rentCar(Car car) {
	String carNumber=car.getRegNumber();
	long licenseId=getRandomNumber(1,nDrivers);
	LocalDate rentDate=current;
	int rentDays=getRandomNumber(1, maxRentDays);
	rentCompany.rentCar(carNumber, licenseId, rentDate, rentDays);
	
}
private boolean byChance(int probability){
	return getRandomNumber(1,100)<=probability;
}
private void createRandomCars() {
	for(int i=1;i<=nCars;i++)
		rentCompany.addCar(getRandomCar(i));
	
}
private Car getRandomCar(int i) {
	
	String regNumber="IL"+Integer.toString(i);
	String color=getRandomColor();
	String modelName=MODEL+getRandomNumber(1,nCarModels);
	return new Car(regNumber, color, modelName);
}
private String getRandomColor() {
	String[]colors={"red","pink","green","white","black","blue",
			"yellow"};
	int ind=getRandomNumber(0, colors.length-1);
	return colors[ind];
}
private void createRandomDrivers() {
	for(int i=1;i<=nDrivers;i++){
		Driver driver=getRandomDriver(i);
		rentCompany.addDriver(driver);
	}
	
}
private Driver getRandomDriver(int i) {
	long licenseId=i;
	String name=NAME+i;
	int birthYear=getRandomNumber(minBirthYear, maxBirthYear);
	String phone=getRandomPhone();
	return new Driver(licenseId, name, birthYear, phone);
}
private String getRandomPhone() {
	
	return "+972"+"-"+getRandomNumber(50,58)+"-"
	+getRandomNumber(1000000,9999999);
}
private void createRandomModels() {
	for(int i=1;i<=nCarModels;i++){
		rentCompany.addModel(getRandomModel(i));
	}
	
}
private Model getRandomModel(int i) {
	String modelName=MODEL+i;
	int gasTank=getRandomNumber(minGasTank,maxGasTank);
	String company=COMPANY+getRandomNumber(1,maxCarCompanies);
	String country=COUNTRY+getRandomNumber(1,maxCountries);
	int priceDay=getRandomNumber(minPriceDay,maxPriceDay);
	return new Model
			(modelName, gasTank, company, country, priceDay);
}
private int getRandomNumber(int min, int max) {
	return (int) (min+Math.random()*(max-min+1));
}

}
