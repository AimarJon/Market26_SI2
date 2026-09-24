package testOperations;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import configuration.ConfigXML;
import domain.Sale;
import domain.Seller;


public class TestDataAccess {
	protected  EntityManager  db;
	protected  EntityManagerFactory emf;

	ConfigXML  c=ConfigXML.getInstance();


	public TestDataAccess()  {
		
		System.out.println("TestDataAccess created");

		//open();
		
	}

	
	public void open(){
		

		String fileName=c.getDbFilename();
		
		if (c.isDatabaseLocal()) {
			  emf = Persistence.createEntityManagerFactory("objectdb:"+fileName);
			  db = emf.createEntityManager();
		} else {
			Map<String, String> properties = new HashMap<String, String>();
			  properties.put("javax.persistence.jdbc.user", c.getUser());
			  properties.put("javax.persistence.jdbc.password", c.getPassword());

			  emf = Persistence.createEntityManagerFactory("objectdb://"+c.getDatabaseNode()+":"+c.getDatabasePort()+"/"+fileName, properties);

			  db = emf.createEntityManager();
    	   }
		System.out.println("TestDataAccess opened");

		
	}
	public void close(){
		db.close();
		System.out.println("TestDataAccess closed");
	}

	public boolean removeSeller(String email) {
		System.out.println(">> TestDataAccess: removeSeller");
		Seller d = db.find(Seller.class, email);
		if (d!=null) {
			db.getTransaction().begin();
			db.remove(d);
			db.getTransaction().commit();
			return true;
		} else 
		return false;
    }
	public Seller createSeller(String email, String name) {
		System.out.println(">> TestDataAccess: addSeller");
		Seller seller=null;
			db.getTransaction().begin();
			try {
			    seller=new Seller(email,name);
				db.persist(seller);
				db.getTransaction().commit();
			}
			catch (Exception e){
				e.printStackTrace();
			}
			return seller;
    }
	public boolean existSeller(String email) {
		 return  db.find(Seller.class, email)!=null;
		 

	}
		
		public Seller addSellerWithSale(String email, String sellerName, String title, String description, int status, float price,  Date pubDate, File file) {
			System.out.println(">> TestDataAccess: addSellerWithSale");
				Seller seller=null;
				db.getTransaction().begin();
				try {
					seller = db.find(Seller.class, email);
					if (seller==null) {
						seller=new Seller(email, sellerName);
				    	db.persist(seller);
					}
					seller.addSale(title, description, status, price, pubDate, file);
					db.getTransaction().commit();
					System.out.println("Seller created "+seller);
					
					return seller;
					
				}
				catch (Exception e){
					e.printStackTrace();
				}
				return null;
	    }
		
	public boolean existSale(String sellerEmail, String title) {
			System.out.println(">> TestDataAccess: existSale");
			Seller s = db.find(Seller.class, sellerEmail);
			if (s!=null) {
				return s.doesSaleExists(title);
			} else 
			return false;
		}
	
		public Sale removeSale(String sellerEmail, String name, String description, Date date ) {
			System.out.println(">> TestDataAccess: removeRide");
			Seller s = db.find(Seller.class, sellerEmail);
			if (s!=null) {
				db.getTransaction().begin();
				Sale sale= s.removeSale(name, description);
				db.getTransaction().commit();
				return sale;

			} else 
			return null;

		}

		
}