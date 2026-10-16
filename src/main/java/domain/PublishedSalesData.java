package domain;

import java.util.ArrayList;
import java.util.Date;

public class PublishedSalesData {

	private String desc;
	private Date pubDate;
	private String buyerEmail;
	private String sellerMail;
	private ArrayList<Sale> basket;

	public PublishedSalesData(String desc, Date pubDate, String buyerEmail, String sellerMail, ArrayList<Sale> basket) {
		this.desc = desc;
		this.pubDate = pubDate;
		this.buyerEmail = buyerEmail;
		this.sellerMail = sellerMail;
		this.basket = basket;
	}

	public String getDesc() { 
		return desc; 
	}
	public Date getPubDate() { 
		return pubDate; 
	}
	public String getBuyerEmail() { 
		return buyerEmail; 
	}
	public String getSellerMail() { 
		return sellerMail; 
	}
	public ArrayList<Sale> getBasket() { 
		return basket; 
	}
}
