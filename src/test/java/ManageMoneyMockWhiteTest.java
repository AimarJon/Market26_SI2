/*import static org.junit.Assert.assertEquals;

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

public class ManageMoneyMockWhiteTest {

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
	//sut.createSale:  withdraw nahiko dirurik gabe
	public void test1() {
		double amount = 100;
		MovementType type = MovementType.WITHDRAW;
		try {
			//invoke System Under Test (sut)  
			sut.open();
			sut.manageMoney(rMail,amount,type);
			sut.close();			
			fail("NotEnoughMoneyException altxatu beharko luke.");

		} catch (NotEnoughMoneyException e ) { 
			// if the program goes to this point true  
			assertTrue(true);
		}catch (Exception e) {
			fail();
		} 
	}

	@Test
	//sut.manageMoney:  withdraw egin nahiko dirurekin
	public void test2() {
		double amount = 30;
		MovementType type = MovementType.WITHDRAW;
		try {
			//invoke System Under Test (sut)  
			sut.open();
			Registered emaitzaReg = sut.manageMoney(rMail,amount,type);
			sut.close();			
			assertEquals(20, emaitzaReg.getBalance(), 0.0001); //50-30=20

		}catch (Exception e) {
			fail();
		} 
	}

	@Test
	//sut.manageMoney:  deposit egin
	public void test3() {
		double amount = 100;
		MovementType type = MovementType.DEPOSIT;
		try {
			//invoke System Under Test (sut) 
			sut.open();
			Registered emaitzaReg = sut.manageMoney(rMail,amount,type);
			sut.close();			
			assertEquals(150, emaitzaReg.getBalance(), 0.0001); //50+100=150

		}catch (Exception e) {
			fail();
		} 
	}

	@Test
	//sut.manageMoney:  type = null denean
	public void test4() {
		double amount = 100;
		MovementType type = null;
		try {
			//invoke System Under Test (sut) 
			sut.open();
			Registered emaitzaReg = sut.manageMoney(rMail,amount,type);
			sut.close();			
			assertEquals(50, emaitzaReg.getBalance(), 0.0001); //50+0=50

		}catch (Exception e) {
			fail();
		} 
	} 

}*/