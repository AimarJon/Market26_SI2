package domain;

public class OfferData {
	public double price;
	public int status;
	public String description;

	public OfferData(double price, int status, String description) {
		this.price = price;
		this.status = status;
		this.description = description;
	}
}