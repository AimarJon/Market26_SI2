package gui;

import java.awt.Color;
import java.net.URL;
import java.util.Locale;

import javax.swing.UIManager;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import configuration.ConfigXML;
import dataAccess.DataAccess;
import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;
import businessLogic.BLFactory;

public class ApplicationLauncher { 
	
	
	
	public static void main(String[] args) {

		ConfigXML c=ConfigXML.getInstance();		
		Locale.setDefault(new Locale(c.getLocale()));
		MainGUInonReg a=new MainGUInonReg("seller3@gmail.com");
		a.setVisible(true);


		 try {
	            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");

	            BLFactory.Type type;
	            
	            if(c.isBusinessLogicLocal()) {
	            	type = BLFactory.Type.LOCAL;
	            }else {
	            	type = BLFactory.Type.REMOTE;
	            }
	            

	            BLFacade appFacadeInterface = BLFactory.createBLFacade(type);

	            MainGUInonReg.setBussinessLogic(appFacadeInterface);

	        } catch (Exception e) {
	            System.out.println("Error in ApplicationLauncher: " + e.toString());
	        }


	}

}
