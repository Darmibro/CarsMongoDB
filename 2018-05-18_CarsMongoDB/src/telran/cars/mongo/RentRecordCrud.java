package telran.cars.mongo;

import java.time.LocalDate;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import telran.cars.dto.RentRecord;

@Document(collection="records")
public class RentRecordCrud {
	@Id
	long id;
	long licenseId;
	String carNumber;
	LocalDate rentDate;
	LocalDate returnDate;
	int gasTankPercent;
	int rentDays;
	float cost;
	int damages;

	public RentRecordCrud() {
	}

	public RentRecordCrud(RentRecord record) {
		id = System.nanoTime();
		licenseId = record.getLicenseId();
		carNumber = record.getCarNumber();
		rentDate = record.getRentDate();
		returnDate = record.getReturnDate();
		gasTankPercent = record.getGasTankPercent();
		rentDays = record.getRentDays();
		cost = record.getCost();
		damages = record.getDamages();
	}

	
	public RentRecord getRentRecord() {
		return new RentRecord(licenseId, carNumber, rentDate, rentDays);
	}

	public long getId() {
		return id;
	}

	public long getLicenseId() {
		return licenseId;
	}

	public String getCarNumber() {
		return carNumber;
	}

	public LocalDate getRentDate() {
		return rentDate;
	}

	public LocalDate getReturnDate() {
		return returnDate;
	}

	public int getGasTankPercent() {
		return gasTankPercent;
	}

	public int getRentDays() {
		return rentDays;
	}

	public float getCost() {
		return cost;
	}

	public int getDamages() {
		return damages;
	}

	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}

	public void setGasTankPercent(int gasTankPercent) {
		this.gasTankPercent = gasTankPercent;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public void setDamages(int damages) {
		this.damages = damages;
	}
	
	
}
