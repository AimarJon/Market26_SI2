
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import dataAccess.DataAccess;
import domain.Registered;
import domain.Sale;

import enums.MovementType;
import exceptions.MustBeLaterThanTodayException;

import exceptions.SaleAlreadyExistException;
import testOperations.TestDataAccess;

public class ManageMoneyBDBlackTest {

	//sut:system under test
	static DataAccess sut=new DataAccess();

	//additional operations needed to execute the test 
	static TestDataAccess testDA=new TestDataAccess();

	@SuppressWarnings("unused")
	private  Registered reg; 
	private  String regMail;
	private  double amount;
	private  MovementType type;

	@Before
	public  void defaultValues() {
		regMail="seller1@gmail.com";
		amount=25;
		type = MovementType.WITHDRAW;
	}

	@Test
	public void test1() {

		testDA.open();
		testDA.addRegistered(regMail, "user1", "123", 50);
		testDA.close();
		try {
			//invoke System Under Test (sut)  
			sut.open();
			reg=sut.manageMoney(regMail,amount,type);
			sut.close();			
			//verify the results
			assertNotNull(reg);
			assertEquals(25, reg.getBalance(), 0.0001); // 50-25=25

		} catch (Exception e) {
			fail();
		} finally {   
			testDA.open();
			testDA.removeRegistered(regMail);
			testDA.close();
		}
	}

	@Test
	public void test2() {

		testDA.open();
		testDA.addRegistered(regMail, "user1", "123", 50);
		testDA.close();
		try {
			type = MovementType.DEPOSIT;
			//invoke System Under Test (sut)  
			sut.open();
			reg=sut.manageMoney(regMail,amount,type);
			sut.close();			
			//verify the results
			assertNotNull(reg);
			assertEquals(75, reg.getBalance(), 0.0001); // 50+25=75

		} catch (Exception e) {
			fail();
		} finally {   
			testDA.open();
			testDA.removeRegistered(regMail);
			testDA.close();
		}
	}

	@Test
	public void test3() {

		testDA.open();
		testDA.addRegistered(regMail, "user1", "123", 50);
		testDA.close();
		try {
			//invoke System Under Test (sut)  
			sut.open();
			reg=sut.manageMoney(null,amount,type);
			sut.close();			
			//verify the results
			fail("IllegalArgumentException altxatu beharko luke");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail();
		} finally {   
			testDA.open();
			testDA.removeRegistered(regMail);
			testDA.close();
		}
	}

	@Test
	public void test4() {

		testDA.open();
		testDA.addRegistered(regMail, "user1", "123", 50);
		testDA.close();
		try {
			type = MovementType.DEPOSIT;
			amount = -10;
			//invoke System Under Test (sut)  
			sut.open();
			reg=sut.manageMoney(regMail,amount,type);
			sut.close();			
			//verify the results
			fail("IllegalArgumentException altxatu beharko luke");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail();
		} finally {   
			testDA.open();
			testDA.removeRegistered(regMail);
			testDA.close();
		}
	}
	
	@Test
	public void test5() {

		testDA.open();
		testDA.addRegistered(regMail, "user1", "123", 50);
		testDA.close();
		try {
			type = null;
			//invoke System Under Test (sut)  
			sut.open();
			reg=sut.manageMoney(regMail,amount,type);
			sut.close();			
			//verify the results
			assertEquals(50, reg.getBalance(), 0.0001); //0+-50=50 ez du ez deposit ez withdraw egiten
		} catch (Exception e) {
			fail();
		} finally {   
			testDA.open();
			testDA.removeRegistered(regMail);
			testDA.close();
		}
	}


	/*
	@Test
	//sut.createSale:  The title parameter is null
	public void test2() {
		title= null;
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
	//sut.createSale:  The title parameter is ""
	public void test3() {
		title= "";
		testDA.open();
		testDA.createSeller(sellerMail,sellerName);
		testDA.close();
		try {
			//invoke System Under Test (sut)  
			sut.open();
			Sale s=sut.createSale(title, description, status, price, pubDate, sellerMail, null);
			sut.close();
			//sale is not created
			assertTrue(s==null);

			//sale is not in DB
			testDA.open();
			boolean exist=testDA.existSale(sellerMail,title);
			assertTrue(!exist);
			testDA.close();

			} catch (ParamNullException | SaleAlreadyExistException  | MustBeLaterThanTodayException e ) { 
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
	//sut.createSale:  The title parameter has not letters
	public void test4() {
		title= "1234";
		testDA.open();
		testDA.createSeller(sellerMail,sellerName);
		testDA.close();
		try {
			//invoke System Under Test (sut)  
			sut.open();
			Sale s=sut.createSale(title, description, status, price, pubDate, sellerMail, null);
			sut.close();
			//sale is not created
			assertTrue(s==null);

			//sale is not in DB
			testDA.open();
			boolean exist=testDA.existSale(sellerMail,title);
			assertTrue(!exist);
			testDA.close();

			} catch (ParamNullException | SaleAlreadyExistException  | MustBeLaterThanTodayException e ) { 
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
	//sut.createSale:  The description parameter is null
	public void test5() {
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
	//sut.createSale:  The description parameter is ""
	public void test6() {
		description= "";
		testDA.open();
		testDA.createSeller(sellerMail,sellerName);
		testDA.close();

		try {
			//invoke System Under Test (sut)  
			sut.open();
			Sale s=sut.createSale(title, description, status, price, pubDate, sellerMail, null);
			sut.close();
			//sale is not created
			assertTrue(s==null);

			//sale is not in DB
			testDA.open();
			boolean exist=testDA.existSale(sellerMail,"");
			assertTrue(!exist);
			testDA.close();

			} catch (ParamNullException | SaleAlreadyExistException  | MustBeLaterThanTodayException e ) { 
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
	//sut.createSale:  The status > 3 
	public void test7() {
		status= 7;
		testDA.open();
		testDA.createSeller(sellerMail,sellerName);
		testDA.close();
		try {
			//invoke System Under Test (sut)  
			sut.open();
			Sale s=sut.createSale(title, description, status, price, pubDate, sellerMail, null);
			sut.close();
			//sale is not created
			assertTrue(s==null);

			//sale is not in DB
			testDA.open();
			boolean exist=testDA.existSale(sellerMail,"");
			assertTrue(!exist);
			testDA.close();

			} catch (ParamNullException | SaleAlreadyExistException  | MustBeLaterThanTodayException e ) { 
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
	//sut.createSale:  The price > 0 
	public void test8() {
		price= -20;
		testDA.open();
		testDA.createSeller(sellerMail,sellerName);
		testDA.close();

		try {
			//invoke System Under Test (sut)  
			sut.open();
			Sale s=sut.createSale(title, description, status, price, pubDate, sellerMail, null);
			sut.close();
			//sale is not created
			assertTrue(s==null);

			//sale is not in DB
			testDA.open();
			boolean exist=testDA.existSale(sellerMail,"");
			assertTrue(!exist);
			testDA.close();

			} catch (ParamNullException | SaleAlreadyExistException  | MustBeLaterThanTodayException e ) { 
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
	//sut.createSale:  pubDate must be later that today 
	public void test9() {
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
	//sut.createSale:  sellerMail must not be null 
	public void test10() {
		sellerMail=null;
		try {
			//invoke System Under Test (sut)  
			sut.open();
			sut.createSale(title, description, status, price, pubDate, sellerMail, null);
			sut.close();

			} catch (ParamNullException  e ) { 
		    // if the program goes to this point true  
			assertTrue(true);

		} catch (MustBeLaterThanTodayException | SaleAlreadyExistException   e ) { 
		// if the program goes to this point fail  
			e.printStackTrace();
		    System.out.println("Error: " + e.getMessage());
			fail();

		}catch (Exception e) {
				fail();
			} 
	}
	@Test
	//sut.createSale:  The seller must be in the DB 
	public void test11() {
		sellerMail="sellerFake";
		try {
			//invoke System Under Test (sut)  
			sut.open();
			Sale s=sut.createSale(title, description, status, price, pubDate, sellerMail, null);
			sut.close();
			//sale is not created
			assertTrue(s==null);

			//sale is not in DB
			testDA.open();
			boolean exist=testDA.existSale(sellerMail,"");
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
	//sut.createSale:  The Seller("sellerTest@ehu.eus","Seller Test") HAS one sale with that title and the same "title" sale is created. 

	public void test12() {


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
	 */
}

