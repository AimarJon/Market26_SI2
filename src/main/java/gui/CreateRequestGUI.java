package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.ResourceBundle;

import javax.swing.*;
import businessLogic.BLFacade;

public class CreateRequestGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final String RESOURCE_BUNDLE_NAME = "Etiquetas";


	private String mail;
	private JFrame thisFrame;

	private JTextField fieldTitle = new JTextField();
	private JTextField fieldDescription = new JTextField();
	private JTextField fieldPrice = new JTextField();

	private JLabel jLabelTitle = new JLabel(ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME).getString("CreateRequestGUI.Title"));
	private JLabel jLabelDescription = new JLabel(ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME).getString("CreateRequestGUI.Description"));
	private JLabel jLabelPrice = new JLabel(ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME).getString("CreateRequestGUI.Price"));
	private JLabel jLabelMsg = new JLabel();

	private JButton jButtonCreate = new JButton(ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME).getString("CreateRequestGUI.CreateRequest"));
	private JButton jButtonClose = new JButton(ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME).getString("Close"));
	

	
	public CreateRequestGUI(String mail) {
		thisFrame = this;
		this.mail = mail;

		this.setTitle(ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME).getString("CreateRequestGUI.CreateRequest"));
		this.setSize(new Dimension(470, 320));
		this.setLocationRelativeTo(null);
		this.getContentPane().setLayout(null);

		// Title
		jLabelTitle.setBounds(6, 20, 110, 20);
		fieldTitle.setBounds(120, 17, 280, 26);
		fieldTitle.setColumns(10);
		getContentPane().add(jLabelTitle);
		getContentPane().add(fieldTitle);

		// Description
		jLabelDescription.setBounds(6, 60, 110, 20);
		fieldDescription.setBounds(120, 57, 280, 73);
		fieldDescription.setColumns(10);
		getContentPane().add(jLabelDescription);
		getContentPane().add(fieldDescription);

		// Price
		jLabelPrice.setBounds(6, 150, 110, 20);
		fieldPrice.setBounds(120, 147, 100, 26);
		getContentPane().add(jLabelPrice);
		getContentPane().add(fieldPrice);

		// Message label
		jLabelMsg.setBounds(8, 190, 420, 20);
		jLabelMsg.setForeground(Color.red);
		getContentPane().add(jLabelMsg);

		// Buttons
		jButtonCreate.setBounds(80, 225, 150, 30);
		jButtonCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				jLabelMsg.setText(" ");
				String error=check_fields_Errors();
				if (error!=null) 
					jLabelMsg.setText(error);
				else {
					BLFacade facade = MainGUInonReg.getBusinessLogic();
					String title = fieldTitle.getText();
					String description = fieldDescription.getText();
					Double price = Double.parseDouble(fieldPrice.getText().replace(',', '.'));
					facade.createRequest(mail,title,description,price);
					dispose();
				}
			}
		});
		jButtonClose.setBounds(265, 225, 130, 30);
		jButtonClose.setBackground(new Color(231, 76, 60));
		jButtonClose.setForeground(Color.WHITE);
		jButtonClose.setFont(new Font("Tahoma", Font.BOLD, 12));
		jButtonClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		getContentPane().add(jButtonCreate);
		getContentPane().add(jButtonClose);


	}
	
	private String check_fields_Errors() {
        if (fieldTitle.getText().trim().isEmpty() ||
            fieldDescription.getText().trim().isEmpty() ||
            fieldPrice.getText().trim().isEmpty()) {
            return ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME).getString("CreateRequestGUI.ErrorQuery");
        }
        try {
            double price = Double.parseDouble(fieldPrice.getText().replace(',', '.'));
            if (price <= 0)
                return ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME).getString("CreateRequestGUI.PriceMustBeGreaterThan0");
        } catch (NumberFormatException e) {
            return ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME).getString("CreateRequestGUI.ErrorNumber");
        }
        return null;
    }
	
}