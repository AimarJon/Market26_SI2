/*import static org.junit.Assert.assertEquals;
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
	private  String regName;
	private  String regMail;
	private  double amount;
	private  MovementType type;

	@Before
	public  void defaultValues() {
		regMail="test@gmail.com";
		amount=100;
		regName="test1";
		type = MovementType.WITHDRAW;
	}
	@Test
	//sut.manageMoney: withdraw nahiko dirurik gabe
	public void test1() {
		testDA.open();
		testDA.addRegistered(regMail, regName, "123", 50);
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
	//sut.manageMoney: withdraw nahiko dirurekin
	public void test2() {
		testDA.open();
		testDA.addRegistered(regMail, regName, "123", 50);
		testDA.close();
		try {
			amount = 30;
			//invoke System Under Test (sut)  
			sut.open();
			reg = sut.manageMoney(regMail,amount,type);
			sut.close();

			assertEquals(20, reg.getBalance(), 0.0001); // 50-30 = 20

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
	// sut.manageMoney: deposit egin
	public void test3() {
		testDA.open();
		testDA.addRegistered(regMail, regName, "123", 50);
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
	//sut.manageMoney:  type = null denean
	public void test4() {
		testDA.open();
		testDA.addRegistered(regMail, regName, "123", 50);
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

}*/
