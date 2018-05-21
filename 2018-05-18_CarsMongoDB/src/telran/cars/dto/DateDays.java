package telran.cars.dto;

import java.time.LocalDate;

public class DateDays {
	public LocalDate currentDate;
	public int days;

	public DateDays() {
	}

	public DateDays(LocalDate currentDate, int days) {
		this.currentDate = currentDate;
		this.days = days;
	}

	public LocalDate getCurrentDate() {
		return currentDate;
	}

	public int getDays() {
		return days;
	}
}
