package telran.cars.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import telran.cars.dto.Car;
import telran.cars.dto.State;

@Document(collection = "cars")
public class CarCrud {
	@Id
	String regNumber;
	String color;
	State state;
	String modelName;
	boolean inUse;
	boolean flRemoved;

	public CarCrud() {
	}

	public CarCrud(Car car) {
		regNumber = car.getRegNumber();
		color = car.getColor();
		state = car.getState();
		modelName = car.getModelName();
		inUse = car.isInUse();
		flRemoved = car.isFlRemoved();
	}

	public Car getCar() {
		return new Car(regNumber, color, modelName);
	}
	
	public String getRegNumber() {
		return regNumber;
	}

	public String getColor() {
		return color;
	}

	public State getState() {
		return state;
	}

	public String getModelName() {
		return modelName;
	}

	public boolean isInUse() {
		return inUse;
	}

	public boolean isFlRemoved() {
		return flRemoved;
	}

	public void setState(State state) {
		this.state = state;
	}

	public void setInUse(boolean inUse) {
		this.inUse = inUse;
	}

	public void setFlRemoved(boolean flRemoved) {
		this.flRemoved = flRemoved;
	}
	
	
	
}
