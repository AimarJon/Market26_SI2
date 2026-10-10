import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.*;

import dataAccess.DataAccess;
import domain.Sale;
import testOperations.TestDataAccess;

public class ToggleWishlistBDBlackTest {
	static DataAccess sut = new DataAccess();
	static TestDataAccess testDA = new TestDataAccess();


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
		testDA.open();
		testDA.addRegistered(mail, name, password, 0);
		Sale sale = testDA.addSaleToRegistered(mail, "futbol baloia", "ordu bete erabilita", 0, 10, new Date(), null);
		testDA.close();

		int salenumber = sale.getSaleNumber();

		try {
			sut.open();
			boolean result = sut.toggleWishList(mail, salenumber);
			sut.close();

			assertTrue(result);
			
			testDA.open();
			boolean orain = testDA.isInWishList(mail, salenumber);
			testDA.close();

			assertTrue(orain);

		}finally {
			testDA.open();
			testDA.removeSale(salenumber);
			testDA.removeRegistered(mail);
			testDA.close();
		}
	}

	@Test
	public void test2() {
		testDA.open();
		testDA.addRegistered(mail, name, password, 0);
		Sale sale = testDA.addSaleToRegistered(mail, "futbol baloia", "ordu bete erabilita", 0, 10, new Date(), null);
		testDA.close();

		int saleNumber = sale.getSaleNumber();

		testDA.open();
		testDA.addToWishList(mail, saleNumber);
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

	@Test
	public void test3() {
		String nullMail = null;
		int saleNumber = 2;

		try {
		sut.open();
		sut.toggleWishList(nullMail, saleNumber);
		sut.close();
		fail();
		}catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void test4() {
		String noExistingMail = "err@gmail.com";
		int saleNumber = 2;

		sut.open();
		boolean result = sut.toggleWishList(noExistingMail, saleNumber);
		sut.close();

		assertFalse(result);
	}

	@Test
	public void test5() {
		testDA.open();
		testDA.addRegistered(mail, name, password, 0);
		testDA.close();
		
		int  noExistingSaleNumber = 9999;
		
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
}
