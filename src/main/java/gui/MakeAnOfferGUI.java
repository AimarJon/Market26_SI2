package gui;

import businessLogic.BLFacade;
import domain.Request;
import domain.RequestContainer;
import domain.Sale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ResourceBundle;

public class MakeAnOfferGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JLabel errorLabel;

	JComboBox<String> jComboBoxStatus = new JComboBox<String>();
	DefaultComboBoxModel<String> statusOptions = new DefaultComboBoxModel<String>();
	List<String> status;
	private ShowRequestGUI parent;

	public MakeAnOfferGUI(String offererMail, RequestContainer request, JFrame p) {
		this.parent = (ShowRequestGUI) p;
		ResourceBundle bundle = ResourceBundle.getBundle("Etiquetas");

		setTitle(bundle.getString("MakeOfferGUI.Title"));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(400, 375);
		setResizable(false);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(20, 30, 20, 30));
		contentPane.setLayout(new BorderLayout(10, 15));
		setContentPane(contentPane);


		status=Utils.getStatus();
		if(status != null) {
			for(String s:status) statusOptions.addElement(s);
		}

		jComboBoxStatus.setModel(statusOptions);
		jComboBoxStatus.setBounds(90, 183, 114, 27);


		errorLabel = new JLabel(" ");
		errorLabel.setForeground(Color.RED);
		contentPane.add(errorLabel, BorderLayout.NORTH);

		JPanel centerPanel = new JPanel(new GridLayout(5, 2, 10, 15));

		JLabel lblPrice = new JLabel(bundle.getString("MakeOfferGUI.Price"));
		lblPrice.setHorizontalAlignment(SwingConstants.RIGHT);
		JTextField priceField = new JTextField();

		JLabel lblDescription = new JLabel(bundle.getString("MakeOfferGUI.Description"));
		lblDescription.setHorizontalAlignment(SwingConstants.RIGHT);
		JTextField descriptionField = new JTextField();

		centerPanel.add(lblPrice);
		centerPanel.add(priceField);
		centerPanel.add(lblDescription);
		centerPanel.add(descriptionField);
		centerPanel.add(jComboBoxStatus);

		contentPane.add(centerPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
		JButton btnSend = new JButton(bundle.getString("MakeOfferGUI.SendButton"));
		JButton btnClose = new JButton(bundle.getString("Close"));
		btnSend.setPreferredSize(new Dimension(140, 28));
		btnClose.setPreferredSize(new Dimension(140, 28));
		buttonPanel.add(btnSend);
		buttonPanel.add(btnClose);
		btnClose.setBackground(new Color(231, 76, 60));
		btnClose.setForeground(Color.WHITE);
		btnClose.setFont(new Font("Tahoma", Font.BOLD, 12));
		contentPane.add(buttonPanel, BorderLayout.SOUTH);


		btnClose.addActionListener(e -> dispose());

		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String priceText = priceField.getText();
				String description = descriptionField.getText();
				String s=(String)jComboBoxStatus.getSelectedItem();
				int numStatus=status.indexOf(s);

				if (priceText.isEmpty() || description.isEmpty()) {
					errorLabel.setText(bundle.getString("MakeOfferGUI.FieldsEmpty"));
					return;
				}

				Double price;
				try {
					price = Double.parseDouble(priceText.replace(',', '.'));
				} catch (NumberFormatException ex) {
					errorLabel.setText(bundle.getString("MakeOfferGUI.InvalidPrice"));
					return;
				}

				if (price <= 0) {
					errorLabel.setText(bundle.getString("MakeOfferGUI.PriceMustBePositive"));
					return;
				}

				BLFacade facade = MainGUInonReg.getBusinessLogic();
				facade.makeOffer(offererMail, request.getRequest(), price, numStatus, description);
				parent.lehioaItxi(); 
				dispose();

			}
		});
	}


}