import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

import dataAccess.DataAccess;
import domain.Registered;
import enums.MovementType;
import exceptions.NotEnoughMoneyException;
import testOperations.TestDataAccess;


public class ManageMoneyBDBlackTest {

	//sut:system under test
	static DataAccess sut=new DataAccess();

	//additional operations needed to execute the test 
	static TestDataAccess testDA=new TestDataAccess();

	@SuppressWarnings("unused")
	private  Registered reg; 
	private  String regMail;
	private  String regName;
	private  double amount;
	private  MovementType type;

	@Before
	public  void defaultValues() {
		regMail="test@gmail.com";
		regName="test1";
		amount=25;
		type = MovementType.WITHDRAW;
	}

	@Test
	// withdraw nahiko dirurekin
	public void test1() {
		testDA.open();
		testDA.addRegistered(regMail, regName, "123", 50);
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
	// deposit egin
	public void test2() {
		testDA.open();
		testDA.addRegistered(regMail, regName, "123", 50);
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
/*	
	@Test
	// withdraw nahiko dirurik gabe
	public void test3() {
		testDA.open();
		testDA.addRegistered(regMail, regName, "123", 50);
		testDA.close();
		amount = 75;
		try {
			//invoke System Under Test (sut)  
			sut.open();
			reg=sut.manageMoney(regMail,amount,type);
			sut.close();			
			//verify the results
			fail("NotEnoughMoneyException altxatu beharko luke");
		} catch (NotEnoughMoneyException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail();
		} finally {   
			testDA.open();
			testDA.removeRegistered(regMail);
			testDA.close();
		}
	}
*/
	@Test
	//rMail == null denean
	public void test4() {
		testDA.open();
		testDA.addRegistered(regMail, regName, "123", 50);
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
	//amount < 0 denean
	public void test5() {
		testDA.open();
		testDA.addRegistered(regMail, regName, "123", 50);
		testDA.close();
		try {
			type = MovementType.DEPOSIT;
			amount = -10;
			//invoke System Under Test (sut)  
			sut.open();
			reg=sut.manageMoney(regMail,amount,type);
			sut.close();			
			fail("Exception altxa beharko luke.");
		} catch (Exception e) {
			assertTrue(true);
		} finally {   
			testDA.open();
			testDA.removeRegistered(regMail);
			testDA.close();
		}
	}

	@Test
	//type == null denean
	public void test6() {
		testDA.open();
		testDA.addRegistered(regMail, regName, "123", 50);
		testDA.close();
		try {
			type = null;
			//invoke System Under Test (sut)  
			sut.open();
			reg=sut.manageMoney(regMail,amount,type);
			sut.close();			
			//verify the results
			assertEquals(50, reg.getBalance(), 0.0001); //50+0=50 ez du ez deposit ez withdraw egiten
		} catch (Exception e) {
			fail();
		} finally {   
			testDA.open();
			testDA.removeRegistered(regMail);
			testDA.close();
		}
	}
}