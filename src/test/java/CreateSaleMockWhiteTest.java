import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import dataAccess.DataAccess;
import domain.Sale;
import domain.Seller;
import exceptions.MustBeLaterThanTodayException;
import exceptions.ParamNullException;
import exceptions.SaleAlreadyExistException;

public class CreateSaleMockWhiteTest {
	
	static DataAccess sut;
	
	protected MockedStatic<Persistence> persistenceMock;

	@Mock
	protected  EntityManagerFactory entityManagerFactory;
	@Mock
	protected  EntityManager db;
	@Mock
    protected  EntityTransaction  et;
	
	private  Seller seller; 
	private  String sellerMail;
	private  String sellerName;
	private  String title;
	private  String description;
	private  int status;
	private  float price;
	private  Date pubDate;

	@Before
    public  void init() {
		MockitoAnnotations.openMocks(this);
        persistenceMock = Mockito.mockStatic(Persistence.class);
		persistenceMock.when(() -> Persistence.createEntityManagerFactory(Mockito.any()))
        .thenReturn(entityManagerFactory);
        
        Mockito.doReturn(db).when(entityManagerFactory).createEntityManager();
		Mockito.doReturn(et).when(db).getTransaction();
	    sut=new DataAccess(db);
	    
	    sellerMail="sellerTest@ehu.eus";
		sellerName="Seller Test";
		seller=new Seller(sellerMail,sellerName);
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
	    
        Mockito.when(db.find(Seller.class, seller.getEmail())).thenReturn(seller);
    }
	@After
    public  void tearDown() {
		persistenceMock.close();
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
	//sut.createSale:  The seller must be in the DB 
	public void test2() {
		sellerMail="sellerFake";
		try {
			//invoke System Under Test (sut)  
			sut.open();
			Sale s=sut.createSale(title, description, status, price, pubDate, sellerMail, null);
			sut.close();
			//sale is not created
			assertTrue(s==null);
			
			//sale is not in DB
			
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
	//sut.createSale:  The Seller("sellerTest1@ehu.eus","Seller Test 1") HAS one sale with that title and the same "title" sale is created. 

	public void test4() {
		sellerMail="sellerTest1@ehu.eus";
		Seller s1=new Seller(sellerMail,"Seller Test 1");
		s1.addSale(title, description, status, price, pubDate, null);
        Mockito.when(db.find(Seller.class, s1.getEmail())).thenReturn(s1);

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
			}  		
	}
	@Test
	//sut.createSale:  The Seller("sellerTest@ehu.eus","Seller Test") HAS  NOT one sale with that "title"" . 
	// and the Sale must be created in DB
	//The test supposes that the "Seller Test" does not exist in the DB

	public void test5() {
		
		
		try {
			//invoke System Under Test (sut)  
			sut.open();
			Sale sale=sut.createSale(title, description, status, price, pubDate, sellerMail, null);
			sut.close();			
			//verify the results
			assertNotNull(sale);
			assertNotNull(sale);
			assertEquals(sale.getTitle(),title);
			assertEquals(sale.getDescription(),description);
			assertEquals(sale.getStatus(),status);
			
			
			} catch (ParamNullException | SaleAlreadyExistException  | MustBeLaterThanTodayException e ) { 
			// if the program goes to this point fail  
				e.printStackTrace();
			    System.out.println("Error: " + e.getMessage());
				fail();


			} catch (Exception e) {
				fail();
			} 
	}
}