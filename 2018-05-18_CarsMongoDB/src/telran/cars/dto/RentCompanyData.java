package telran.cars.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class RentCompanyData implements Serializable{
private int finePercent;
private int gasPrice;

public RentCompanyData(){
	finePercent=15;
	gasPrice=10;
}
public RentCompanyData(int finePercent, int gasPrice) {
	super();
	this.finePercent = finePercent;
	this.gasPrice = gasPrice;
}
public int getFinePercent() {
	return finePercent;
}
public void setFinePercent(int finePercent) {
	this.finePercent = finePercent;
}
public int getGasPrice() {
	return gasPrice;
}
public void setGasPrice(int gasPrice) {
	this.gasPrice = gasPrice;
}
@Override
public String toString() {
	return "RentCompanyData [finePercent=" + finePercent + ", gasPrice=" + gasPrice + "]";
}

}
