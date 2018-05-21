package telran.cars.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import telran.cars.dto.Model;

@Document(collection = "models")
public class ModelCrud {
	@Id
	String modelName;
	int gasTank;
	String company;
	String country;
	int priceDay;

	public ModelCrud() {
	}

	public ModelCrud(Model model) {
		modelName = model.getModelName();
		gasTank = model.getGasTank();
		company = model.getCompany();
		country = model.getCountry();
		priceDay = model.getPriceDay();
	}

	public Model getModel() {
		return new Model(modelName, gasTank, company, country, priceDay);
	}

	public String getModelName() {
		return modelName;
	}

	public int getGasTank() {
		return gasTank;
	}

	public String getCompany() {
		return company;
	}

	public String getCountry() {
		return country;
	}

	public int getPriceDay() {
		return priceDay;
	}

	public void setPriceDay(int priceDay) {
		this.priceDay = priceDay;
	}

}
