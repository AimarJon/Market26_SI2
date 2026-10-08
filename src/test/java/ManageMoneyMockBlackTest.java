import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


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

import enums.MovementType;

import exceptions.NotEnoughMoneyException;

public class ManageMoneyMockBlackTest {

	static DataAccess sut;

	protected MockedStatic<Persistence> persistenceMock;

	@Mock
	protected  EntityManagerFactory entityManagerFactory;
	@Mock
	protected  EntityManager db;
	@Mock
	protected  EntityTransaction  et;

	private Registered reg;
	private String rMail;
	private String regName;
	private double initialBalance;


	@Before
	public  void init() {
		MockitoAnnotations.openMocks(this);
		persistenceMock = Mockito.mockStatic(Persistence.class);
		persistenceMock.when(() -> Persistence.createEntityManagerFactory(Mockito.any())).thenReturn(entityManagerFactory);

		Mockito.doReturn(db).when(entityManagerFactory).createEntityManager();
		Mockito.doReturn(et).when(db).getTransaction();
		sut=new DataAccess(db);

		rMail = "test@gmail.com";
		regName = "test1";
		initialBalance = 50;
		reg = new Registered(rMail,regName,"123");
		reg.setBalance(initialBalance);


		Mockito.when(db.find(Registered.class, rMail)).thenReturn(reg);
	}
	@After
	public  void tearDown() {
		persistenceMock.close();
	}


	@Test
	// withdraw nahiko dirurekin
	public void test1() {
		try {
			double amount = 25;
			MovementType type = MovementType.WITHDRAW;
			//invoke System Under Test (sut)  
			sut.open();
			Registered emaitzaReg = sut.manageMoney(rMail, amount, type);
			sut.close();			
			//verify the results
			assertNotNull(rMail);
			assertTrue(amount > 0);
			assertNotNull(type);
			assertEquals(MovementType.WITHDRAW,type);
			assertTrue(reg.getBalance() - amount >= 0);
			//emaitza begiratu
			assertEquals(25,emaitzaReg.getBalance(),0.0001);//50-25=25

		} catch (Exception e) {
			fail();
		} 
	}

	@Test
	// deposit egin
	public void test2() {
		try {
			double amount = 25;
			MovementType type = MovementType.DEPOSIT;
			//invoke System Under Test (sut)  
			sut.open();
			Registered emaitzaReg = sut.manageMoney(rMail, amount, type);
			sut.close();			
			//verify the results
			assertNotNull(rMail);
			assertTrue(amount > 0);
			assertNotNull(type);
			assertEquals(MovementType.DEPOSIT,type);
			//emaitza begiratu
			assertEquals(75,emaitzaReg.getBalance(),0.0001);//50+25=75

		} catch (Exception e) {
			fail();
		} 
	}

	@Test
	// withdraw nahiko dirurik gabe
	public void test3() {
		try {
			double amount = 75;
			MovementType type = MovementType.WITHDRAW;
			//invoke System Under Test (sut)  
			sut.open();
			sut.manageMoney(rMail, amount, type);
			sut.close();			
			//verify the results
			assertNotNull(rMail);
			assertTrue(amount > 0);
			assertNotNull(type);
			assertEquals(MovementType.WITHDRAW,type);
			assertTrue(reg.getBalance() - amount < 0);
			fail("NotEnoughMoneyException altxatu beharko luke");
		}catch (NotEnoughMoneyException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail();
		} 
	}

	@Test
	//rMail == null denean
	public void test4() {
		try {
			double amount = 25;
			MovementType type = MovementType.WITHDRAW;
			String nullMail = null;
			//invoke System Under Test (sut)  
			sut.open();
			sut.manageMoney(nullMail, amount, type);
			sut.close();			
			//verify the results
			fail("IllegalArgumentException  altxatu beharko luke");
		}catch (IllegalArgumentException | NullPointerException e) {
			assertTrue(true);
		} catch (Exception e) {
			fail();
		} 
	}

	@Test
	//amount < 0 denean
	public void test5() {
		try {
			double amount = -10;
			MovementType type = MovementType.DEPOSIT;
			//invoke System Under Test (sut)  
			sut.open();
			sut.manageMoney(rMail, amount, type);
			sut.close();			
			//verify the results
			assertTrue(amount < 0);
			fail("Exception altxa beharko luke.");
		} catch (Exception e) {
			assertTrue(true);
		} 
	}

	@Test
	//type == null denean
	public void test6() {
		try {
			double amount = 25;
			MovementType type = null;
			//invoke System Under Test (sut)  
			sut.open();
			Registered emaitzaReg = sut.manageMoney(rMail, amount, type);
			sut.close();			
			//verify the results
			assertNull(type);
			assertEquals(50,emaitzaReg.getBalance(),0.0001); //50+0=50
		} catch (Exception e) {
			fail();
		} 
	}
}
