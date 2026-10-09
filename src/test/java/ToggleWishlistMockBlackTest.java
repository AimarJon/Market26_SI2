import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

public class ToggleWishlistMockBlackTest {
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
	private int saleNumber;
	private Registered reg;
	private Sale sale;
	
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
		saleNumber = 1;
		
		reg = new Registered(mail, name, password);
		sale = reg.addSale("futbol baloia", "ordu bete erabilita", 0, 10.5f, new Date(), null);

		
		Mockito.when(db.find(Registered.class, mail)).thenReturn(reg);
		Mockito.when(db.find(Sale.class, saleNumber)).thenReturn(sale);
	    
    }
	@After
    public  void tearDown() {
		persistenceMock.close();
    }
	
	@Test
	public void test1() {

		reg.addToWishList(sale); // aurretik wishlistean sartu


		sut.open();
		boolean result = sut.toggleWishList(mail, saleNumber);
		sut.close();

		assertTrue(result);
		assertFalse(reg.getWishList().contains(sale)); // orain ez dago
		
		
	}
	
	@Test
	public void test2() {		

		sut.open();
		boolean result = sut.toggleWishList(mail, saleNumber);
		sut.close();

		assertTrue(result);
		assertTrue(reg.getWishList().contains(sale)); // orain badago dago
		
	}
/*	
	@Test
	public void test3() {
		int saleNumber = 1;

		String nullMail = null;
		
		sut.open();
		boolean result = sut.toggleWishList(nullMail, saleNumber);
		sut.close();

		assertFalse(result);
	}
	
	@Test
	public void test4() {
		int saleNumber = 1;

		String errMail = "errmail@gmail.com";
		
		sut.open();
		boolean result = sut.toggleWishList(errMail, saleNumber);
		sut.close();

		assertFalse(result);
	}
*/	
	@Test
	public void test5() {
		int saleNumberErr = 9999;
		
		sut.open();
		boolean result = sut.toggleWishList(mail, saleNumberErr);
		sut.close();
		
		assertFalse(result);
	}

}
