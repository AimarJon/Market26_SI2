import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import dataAccess.DataAccess;
import domain.Registered;
import enums.MovementType;
import exceptions.NotEnoughMoneyException;
import testOperations.TestDataAccess;

public class ManageMoneyBDWhiteTest {

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
		regMail="test@gmail.com";
		amount=100;
		type = MovementType.WITHDRAW;
	}
	@Test
	//sut.createSale:  Some of the parameters are null
	public void test1() {
		testDA.open();
		testDA.addRegistered(regMail, "user1", "123", 50);
		testDA.close();
		try {
			//invoke System Under Test (sut)  
			sut.open();
			sut.manageMoney(regMail,amount,type);
			sut.close();		
			fail("NotEnoughMoneyException altxatu beharko luke.");

		} catch (NotEnoughMoneyException e ) { 
			// if the program goes to this point true  
			assertTrue(true);
		}catch (Exception e) {
			fail();
		}finally {
			testDA.open();
			testDA.removeRegistered(regMail);
			testDA.close();
		}
	}

	@Test
	public void test2() {
		testDA.open();
		testDA.addRegistered(regMail, "user1", "123", 150);
		testDA.close();
		try {
			amount = 30;
			//invoke System Under Test (sut)  
			sut.open();
			reg =sut.manageMoney(regMail,amount,type);
			sut.close();

			assertEquals(120, reg.getBalance(), 0.0001); // 150-30 = 120

		} catch (NotEnoughMoneyException e ) { 
			// if the program goes to this point true  
			fail("Ez luke NotEnoughMoneyException altxatu beharko");
		}catch (Exception e) {
			fail();
		}finally {
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
			type = MovementType.DEPOSIT;
			//invoke System Under Test (sut)  
			sut.open();
			reg = sut.manageMoney(regMail,amount,type);
			sut.close();

			assertEquals(150, reg.getBalance(), 0.0001); // 50+100 = 150

		}catch (Exception e) {
			fail();
		}finally {
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
			type = null;
			//invoke System Under Test (sut)  
			sut.open();
			reg = sut.manageMoney(regMail,amount,type);
			sut.close();

			assertEquals(50, reg.getBalance(), 0.0001); // ez da ezer egiten

		}catch (Exception e) {
			fail();
		}finally {
			testDA.open();
			testDA.removeRegistered(regMail);
			testDA.close();
		}
	}	

}
