package testOperations;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import configuration.ConfigXML;
import domain.Registered;
import domain.Sale;


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

	
	public Registered addRegistered(String email, String name, String password, double initialBalance) {
		System.out.println(">> TestDataAccess: addRegistered");
		Registered reg = null;
		db.getTransaction().begin();
		try {
			reg = db.find(Registered.class, email);
			if (reg == null) {
				reg = new Registered(email, name, password);
				reg.setBalance(initialBalance);
				db.persist(reg);
			} else {
				reg.setBalance(initialBalance);
			}
			db.getTransaction().commit();
			return reg;
		} catch (Exception e) {
			e.printStackTrace();
			if (db.getTransaction().isActive()) {
				db.getTransaction().rollback();
			}
		}
		return null;
	}

	public boolean removeRegistered(String email) {
		System.out.println(">> TestDataAccess: removeRegistered");
		Registered reg = db.find(Registered.class, email);
		if (reg != null) {
			db.getTransaction().begin();
			db.remove(reg);
			db.getTransaction().commit();
			return true;
		}
		return false;
	}
	
	public boolean removeSale(int saleNumber) {
		System.out.println(">> TestDataAccess: removeSale");
		Sale sale = db.find(Sale.class, saleNumber);
		if (sale != null) {
			db.getTransaction().begin();
			db.remove(sale);
			db.getTransaction().commit();
			return true;
		}
		return false;
	}

	public boolean existRegistered(String email) {
		System.out.println(">> TestDataAccess: existRegistered");
		return db.find(Registered.class, email) != null;
	}
	
	public Sale addSaleToRegistered(String email, String title, String description, int status, float price, Date pubDate, File file) {
		System.out.println(">> TestDataAccess: addSaleToRegistered");
		Sale sale = null;
		db.getTransaction().begin();
		try {
			Registered reg = db.find(Registered.class, email);
			if (reg != null) {
				sale = reg.addSale(title, description, status, price, pubDate, file);
			}
			db.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (db.getTransaction().isActive()) db.getTransaction().rollback();
		}
		return sale;
	}
	
	public void addToWishList(String email, int saleNumber) {
		System.out.println(">> TestDataAccess: addToWishList");
		db.getTransaction().begin();
		try {
			Registered reg = db.find(Registered.class, email);
			Sale sale = db.find(Sale.class, saleNumber);
			if (reg != null && sale != null) {
				reg.addToWishList(sale);
			}
			db.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (db.getTransaction().isActive()) db.getTransaction().rollback();
		}
	}

	public boolean isInWishList(String email, int saleNumber) {
		System.out.println(">> TestDataAccess: isInWishList");
		Registered reg = db.find(Registered.class, email);
		Sale sale = db.find(Sale.class, saleNumber);
		if (reg == null || sale == null) return false;
		return reg.getWishList().contains(sale);
	}
	

}