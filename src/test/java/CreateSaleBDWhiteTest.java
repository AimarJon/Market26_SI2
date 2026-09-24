import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import dataAccess.DataAccess;
import domain.Sale;
import domain.Seller;
import exceptions.MustBeLaterThanTodayException;
import exceptions.ParamNullException;
import exceptions.SaleAlreadyExistException;
import testOperations.TestDataAccess;

public class CreateSaleBDWhiteTest {

	 //sut:system under test
	 static DataAccess sut=new DataAccess();
	 
	 //additional operations needed to execute the test 
	 static TestDataAccess testDA=new TestDataAccess();

	@SuppressWarnings("unused")
	private  Seller seller; 
	private  String sellerMail;
	private  String sellerName;
	private  String title;
	private  String description;
	private  int status;
	private  float price;
	private  Date pubDate;
	
	@Before
    public  void defaultValues() {
	    sellerMail="sellerTest@ehu.eus";
		sellerName="Seller Test";
		title="futbol baloia";
		description="Used one hour";
		status=0;
		price=10;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		pubDate=null;
		try {
			pubDate = sdf.parse("05/10/2026");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
    }
	@Test
	//sut.createSale:  Some of the parameters are null
	public void test1() {
		description= null;
		try {
			//invoke System Under Test (sut)  
			sut.open();
			sut.createSale(title, description, status, price, pubDate, sellerMail, null);
			sut.close();			
			fail("The sale must not be created");
			
			} catch (ParamNullException e ) { 
			// if the program goes to this point true  
				assertTrue(true);
			} catch ( SaleAlreadyExistException  | MustBeLaterThanTodayException e ) { 
		// if the program goes to this point fail  
			e.printStackTrace();
		    System.out.println("Error: " + e.getMessage());
			fail();

		}catch (Exception e) {
				fail();
			} 
	}
	@Test
	//sut.createSale:  The seller must be in the DB (try captures null)
	public void test2() {
		sellerMail="sellerFake";
		try {
			//invoke System Under Test (sut)  
			sut.open();
			System.out.println(title+ " "+description+" "+status+ " "+ price + " "+ pubDate +" "+sellerMail);
			Sale s=sut.createSale(title, description, status, price, pubDate, sellerMail, null);
			sut.close();
			//sale is not created
			assertNull(s);
			
			//sale is not in DB
			testDA.open();
			boolean exist=testDA.existSale(sellerMail,description);
			assertTrue(!exist);
			testDA.close();
			
			} catch (ParamNullException | SaleAlreadyExistException  | MustBeLaterThanTodayException e ) { 
		// if the program goes to this point fail  
			e.printStackTrace();
		    System.out.println("Error: " + e.getMessage());
			fail();

		}catch (Exception e) {
				fail();
			} 
	}
	
	@Test
	//sut.createSale:  pubDate must be later that today 
	public void test3() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		pubDate=null;
		try {
			pubDate = sdf.parse("05/10/2020");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		try {
			//invoke System Under Test (sut)  
			sut.open();
			sut.createSale(title, description, status, price, pubDate, sellerMail, null);
			sut.close();
			
			} catch ( MustBeLaterThanTodayException e ) { 
		    // if the program goes to this point true  
			assertTrue(true);

		} catch (ParamNullException | SaleAlreadyExistException   e ) { 
		// if the program goes to this point fail  
			e.printStackTrace();
		    System.out.println("Error: " + e.getMessage());
			fail();

		}catch (Exception e) {
				fail();
			} 
	}
	
	@Test
	//sut.createSale:  The Seller("sellerTest@ehu.eus","Seller Test") HAS one sale with that title and the same "title". sale is created. 

	public void test4() {
		
		
		testDA.open();
		testDA.addSellerWithSale( sellerMail, sellerName, title, description, status, price, pubDate, null);
		testDA.close();
		try {	
			//verify the results
			sut.open();
			sut.createSale(title, description, status, price, pubDate, sellerMail, null);
			sut.close();
			fail();
			
			} catch (SaleAlreadyExistException e ) { 
			// if the program goes to this point true  
				assertTrue(true);


			} catch (ParamNullException  | MustBeLaterThanTodayException e ) { 
			// if the program goes to this point fail  
				e.printStackTrace();
			    System.out.println("Error: " + e.getMessage());
				fail();


			}catch (Exception e) {
				fail();
			} finally {   
				testDA.open();
				testDA.removeSeller(sellerMail);
				testDA.close();
		    }
	}


	@Test
	//sut.createSale:  The Seller(seller1@ehu.eus) HAS NOT one sale with "title" 
	// and the sale must be created in DB
	//The test supposes that the "Seller Test" does not exist in the DB before the test

	public void test5() {		
		
		testDA.open();
		testDA.createSeller(sellerMail,sellerName);
		testDA.close();
		try {
			//invoke System Under Test (sut)  
			sut.open();
			Sale sale=sut.createSale(title, description, status, price, pubDate, sellerMail, null);
			sut.close();			
			
			//verify the results
			assertNotNull(sale);
			
			//sale is in DB
			testDA.open();
			boolean exist=testDA.existSale(sellerMail,title);
			assertTrue(exist);
			testDA.close();
			
			} catch (ParamNullException | SaleAlreadyExistException  | MustBeLaterThanTodayException e ) { 
			// if the program goes to this point fail  
				e.printStackTrace();
			    System.out.println("Error: " + e.getMessage());
				fail();


			} catch (Exception e) {
				fail();
			} finally {   
				testDA.open();
				testDA.removeSeller(sellerMail);
				testDA.close();
		    }
	} 
	
}
