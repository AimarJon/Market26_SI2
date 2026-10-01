package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.ResourceBundle;

import businessLogic.BLFacade;
import domain.Admin;
import domain.Registered;
import domain.User;

import java.awt.event.ActionListener;

public class LoginGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldUsername;
	private JPasswordField textFieldPassword;


	public LoginGUI() {

		ResourceBundle bundle = ResourceBundle.getBundle("Etiquetas");

		setTitle(bundle.getString("MainGUI.Login"));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(500, 300);
		setResizable(false);


		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(40, 60, 40, 60));
		contentPane.setLayout(new BorderLayout(10, 20));
		setContentPane(contentPane);



		JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 15));


		// USERNAME
		JLabel lblUsername = new JLabel(bundle.getString("LoginGUI.Email"));
		lblUsername.setHorizontalAlignment(SwingConstants.RIGHT);
		textFieldUsername = new JTextField();
		textFieldUsername.setText("seller22@gmail.com");

		// PASSWORD
		JLabel lblPassword = new JLabel(bundle.getString("LoginGUI.Password"));
		lblPassword.setHorizontalAlignment(SwingConstants.RIGHT);
		textFieldPassword = new JPasswordField();
		textFieldPassword.setText("123");

		formPanel.add(lblUsername);
		formPanel.add(textFieldUsername);
		formPanel.add(lblPassword);
		formPanel.add(textFieldPassword);

		contentPane.add(formPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));

		JButton btnLogin = new JButton(bundle.getString("LoginGUI.LoginButton"));
		btnLogin.setPreferredSize(new Dimension(140, 28));

		JButton btnClose = new JButton(bundle.getString("LoginGUI.CloseButton"));
		btnClose.setBackground(new Color(231, 76, 60));
		btnClose.setForeground(Color.WHITE);
		btnClose.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				MainGUInonReg a = new MainGUInonReg(null);
				a.setVisible(true);
			}
		});
		btnClose.setPreferredSize(new Dimension(140, 28));

		buttonPanel.add(btnLogin);
		buttonPanel.add(btnClose);

		contentPane.add(buttonPanel, BorderLayout.SOUTH);

		JLabel errorLabel = new JLabel(" "); 
		errorLabel.setForeground(Color.red);
		errorLabel.setAlignmentX(CENTER_ALIGNMENT);
		contentPane.add(errorLabel, BorderLayout.NORTH);


		btnLogin.addActionListener((ActionEvent e) -> {

			BLFacade facade = MainGUInonReg.getBusinessLogic();
			String email = textFieldUsername.getText();
			String password = new String(textFieldPassword.getPassword());

			User u = facade.isLogin(email, password);
			if(u == null) errorLabel.setText(bundle.getString("LoginGUI.error"));

			else if(u instanceof Admin){
				new MainGUIAdmin((Admin) u).setVisible(true);
				dispose();
				
				
			}else {
					new MainGUIReg((Registered) u).setVisible(true);
					dispose();
				}

			


		});

	}

}
