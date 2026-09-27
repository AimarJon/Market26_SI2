import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.*;

import dataAccess.DataAccess;
import domain.Registered;
import domain.Sale;
import testOperations.TestDataAccess;

public class ToggleWishlistBDWhiteTest {
	//sut:system under test
	static DataAccess sut=new DataAccess();

	//additional operations needed to execute the test 
	static TestDataAccess testDA=new TestDataAccess();	
	
	private String mail;
	private String name;
	private String password;
	
	@Before
	public void defaultValues() {
		mail = "user1@gmail.com";
		name = "user1";
		password = "123";
	}
	
	@Test
	public void test1() {
		String noExistingMail = "err@gmail.com";
		int saleNumber = 1;

		sut.open();
		boolean result = sut.toggleWishList(noExistingMail, saleNumber);
		sut.close();

		assertFalse(result); // DB egoera: ez da aldatzen
	}
	
	@Test
	public void test2() {
		testDA.open();
		testDA.addRegistered(mail, name, password, 100);
		testDA.close();
		
		int noExistingSaleNumber = 9999;
		
		try {
			sut.open();
			boolean result = sut.toggleWishList(mail, noExistingSaleNumber);
			sut.close();
			
			assertFalse(result);
		}finally {
			testDA.open();
			testDA.removeRegistered(mail);
			testDA.close();
		}
		
	}
	
	@Test
	public void test3() {
		testDA.open();
		testDA.addRegistered(mail, name, password, 100);
		Sale sale = testDA.addSaleToRegistered(mail, "futbol baloia", "ordu bete erabilita", 0, 10, new Date(), null);
		testDA.close();
		
		int saleNumber = sale.getSaleNumber();
		
		try {
			sut.open();
			boolean result = sut.toggleWishList(mail, saleNumber);
			sut.close();
			
			assertTrue(result);
			
			testDA.open();
			boolean orain = testDA.isInWishList(mail, saleNumber);
			testDA.close();
			
			assertTrue(orain);
			
		}finally {
			testDA.open();
			testDA.removeSale(saleNumber);
			testDA.removeRegistered(mail);
			testDA.close();
		}
		
	}
	
	
	@Test
	public void test4() {
		testDA.open();
		testDA.addRegistered(mail, name, password, 0);
		Sale sale = testDA.addSaleToRegistered(mail, "futbol baloia", "ordu bete erabilita", 0, 10, new Date(), null);
		testDA.close();

		int saleNumber = sale.getSaleNumber();
		Registered reg = sale.getSeller();
		
		testDA.open();
		testDA.addToWishList(mail,saleNumber);
		testDA.close();
		
		try {
			sut.open();
			boolean result = sut.toggleWishList(mail, saleNumber);
			sut.close();
			
			assertTrue(result);
			
			testDA.open();
			boolean orain = testDA.isInWishList(mail, saleNumber);
			testDA.close();
			
			assertFalse(orain);
			
		}finally {
			testDA.open();
			testDA.removeSale(saleNumber);
			testDA.removeRegistered(mail);
			testDA.close();
		}
	}

}
