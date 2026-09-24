package testOperations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.mockito.Mockito;

import businessLogic.BLFacade;
import configuration.UtilDate;
import domain.Sale;
import domain.Seller;
import exceptions.MustBeLaterThanTodayException;
import exceptions.ParamNullException;
import exceptions.SaleAlreadyExistException;
import gui.MainGUI;

public class SalesMockTest {
	static BLFacade appFacadeMock = Mockito.mock(BLFacade.class);
	public static void main(String args[]) {
		configureMockQuerySales();
		configureMockCreateSale();
		MainGUI sut = new MainGUI("seller1@gmail.com");
		MainGUI.setBussinessLogic(appFacadeMock);

		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		sut.setVisible(true);		
	}
	
	public static void configureMockQuerySales() {
		Seller seller1=new Seller("seller1@gmail.com","Aitor Fernandez");
		Date today = UtilDate.trim(new Date());
		List<Sale> sales=new Vector<Sale>();
		sales.add(new Sale("futbol baloia", "oso polita, gutxi erabilita", 2, 10, today, null, seller1));
		sales.add(new Sale("salomon mendiko botak", "44 zenbakia, 3 ateraldi",2, 20, today, null, seller1));
		sales.add(new Sale("samsung 42\" telebista", "berria, erabili gabe", 1, 175, today, null, seller1));
		Mockito.when(appFacadeMock.getPublishedSales(anyString(), any(Date.class))).thenReturn(sales);
	}
	
	public static void configureMockCreateSale() {
		String sellerEmail="seller1@gmail.com";
		Date today = UtilDate.trim(new Date());
		@SuppressWarnings("removal")
		float f10=new Float(10).floatValue();
		try {
			Mockito.when(appFacadeMock.createSale("futbol baloia", "oso polita, gutxi erabilita", 2, f10, today, sellerEmail, null)).thenThrow(new SaleAlreadyExistException("Ya existe"));
		} catch (ParamNullException | MustBeLaterThanTodayException | SaleAlreadyExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
