package testOperations;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import dataAccess.DataAccess;
import exceptions.MustBeLaterThanTodayException;
import exceptions.ParamNullException;
import exceptions.SaleAlreadyExistException;

public class CreateSaleBDTest {
	static DataAccess sut=new DataAccess();
	static TestDataAccess test = new TestDataAccess();
@Test
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
		test.open();
		test.addSellerWithSale(sellerMail, sellerName, title, description, status,
				price, pubDate, null);
		test.close();
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
		} finally {
			test.open();
			test.removeSeller(sellerMail);
			test.close();
		}
	}
}
