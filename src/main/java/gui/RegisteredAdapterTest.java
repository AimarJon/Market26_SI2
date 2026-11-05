package gui;

import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;
import businessLogic.BLFactory;
import dataAccess.DataAccess;
import domain.Registered;

public class RegisteredAdapterTest {
	public static void main(String[]	args)	{
		DataAccess da = new DataAccess();
        BLFacade facade = new BLFacadeImplementation(da);
        
        da.open();
        Registered reg = da.getRegistered("seller1@gmail.com");
        
        RegisteredTable st = new RegisteredTable(reg);
        st.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        st.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                da.close();
            }
        });
        st.setVisible(true);
		
	}
}
