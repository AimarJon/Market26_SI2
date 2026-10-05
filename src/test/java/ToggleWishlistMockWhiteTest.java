/* import static org.junit.Assert.*;

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
import domain.Registered;
import domain.Sale;

public class ToggleWishlistMockWhiteTest {

	static DataAccess sut;

	protected MockedStatic<Persistence> persistenceMock;

	@Mock
	protected  EntityManagerFactory entityManagerFactory;
	@Mock
	protected  EntityManager db;
	@Mock
	protected  EntityTransaction  et;



	private String mail;
	private String name;
	private String password;
	
	@Before
    public  void init() {
		MockitoAnnotations.openMocks(this);
        persistenceMock = Mockito.mockStatic(Persistence.class);
		persistenceMock.when(() -> Persistence.createEntityManagerFactory(Mockito.any()))
        .thenReturn(entityManagerFactory);
        
        Mockito.doReturn(db).when(entityManagerFactory).createEntityManager();
		Mockito.doReturn(et).when(db).getTransaction();
	    sut=new DataAccess(db);
	    
		mail = "user1@gmail.com";
		name = "user1";
		password = "123";
	    
    }
	@After
    public  void tearDown() {
		persistenceMock.close();
    }
	
	@Test
	public void test1() {
		int saleNumber = 1;

		Mockito.when(db.find(Registered.class, mail)).thenReturn(null);

		sut.open();
		boolean result = sut.toggleWishList(mail, saleNumber);
		sut.close();
		
		assertFalse(result);
	}
	
	@Test
	public void test2() {
		int saleNumber = 9999;
		Registered reg = new Registered(mail, name, password);

		Mockito.when(db.find(Registered.class, mail)).thenReturn(reg);
		Mockito.when(db.find(Sale.class, saleNumber)).thenReturn(null);

		sut.open();
		boolean result = sut.toggleWishList(mail, saleNumber);
		sut.close();

		assertFalse(result);
	}
	
	@Test
	public void test3() {
		Registered reg = new Registered(mail, name, password);
		Sale sale = reg.addSale("futbol baloia", "ordu bete erabilita", 0, 10, new Date(), null);
		
		int saleNumber = 1;

		Mockito.when(db.find(Registered.class, mail)).thenReturn(reg);
		Mockito.when(db.find(Sale.class, saleNumber)).thenReturn(sale);

		sut.open();
		boolean result = sut.toggleWishList(mail, saleNumber);
		sut.close();

		assertTrue(result);
		assertTrue(reg.getWishList().contains(sale));
	}
	
	@Test
	public void test4() {
		int saleNumber = 2;

		Registered reg = new Registered(mail, name, password);
		Sale sale = reg.addSale("futbol baloia", "ordu bete erabilita", 0, 10, new Date(), null);
		reg.addToWishList(sale);

		Mockito.when(db.find(Registered.class, mail)).thenReturn(reg);
		Mockito.when(db.find(Sale.class, saleNumber)).thenReturn(sale);

		sut.open();
		boolean result = sut.toggleWishList(mail, saleNumber);
		sut.close();

		assertTrue(result);
		assertFalse(reg.getWishList().contains(sale));
	}




}*/