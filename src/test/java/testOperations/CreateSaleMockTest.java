package testOperations;

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
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import dataAccess.DataAccess;
import domain.Seller;
import exceptions.MustBeLaterThanTodayException;
import exceptions.ParamNullException;
import exceptions.SaleAlreadyExistException;

public class CreateSaleMockTest {

	static DataAccess sut;

	protected MockedStatic<Persistence> persistenceMock;

	@Mock
	protected EntityManagerFactory entityManagerFactory;

	@Mock
	protected EntityManager db;

	@Mock
	protected EntityTransaction et;

	@Before
	public void init() {
		MockitoAnnotations.openMocks(this);
		persistenceMock = Mockito.mockStatic(Persistence.class);
		persistenceMock.when(() ->
		Persistence.createEntityManagerFactory(Mockito.any())).thenReturn(entityManagerFactory);
		Mockito.doReturn(db).when(entityManagerFactory).createEntityManager();
		Mockito.doReturn(et).when(db).getTransaction();
		sut=new DataAccess(db);
	}

	@After
	public void tearDown() {
		persistenceMock.close();
	}

	public void test4() {
		String title="futbol baloia";
		String description="Used one hour";
		int status=0;
		float price=10;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date pubDate=null;
		try {
			pubDate = sdf.parse("05/10/2026");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String sellerMail="sellerTest1@ehu.eus";
		String sellerName="Seller Test 1";
		Seller s1=new Seller(sellerMail, sellerName);
		s1.addSale(title, description, status, price, pubDate, null);
		Mockito.when(db.find(Seller.class,sellerMail)).thenReturn(s1);
		try {
			sut.open();
			sut.createSale(title, description, status, price, pubDate, sellerMail, null);
			sut.close();
			fail();
		} catch (SaleAlreadyExistException e ) {
			// if the program goes to this point true
			assertTrue(true);
		} catch (ParamNullException | MustBeLaterThanTodayException e ) {
			// if the program goes to this point fail
			e.printStackTrace();
			System.out.println("Error: " + e.getMessage());
			fail();
		} catch (Exception e) {
			fail();
		}
	}
}
