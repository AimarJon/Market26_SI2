package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;
import configuration.UtilDate;
import domain.Registered;
import exceptions.EmailNotValidException;
import exceptions.PasswordNotValidException;

import java.util.Date;
import java.util.ResourceBundle;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RegisterGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTextField textFieldUsername;
	private JPasswordField passwordField;
	private JPasswordField repeatPasswordField;
	private JTextField textFieldEmail;
	private JLabel errorLabel;
	private static final String EMAIL_AE ="^[A-Za-z0-9+_.-]+@[A-Za-z0-9-]+\\.[A-Za-z]{2,}$";
	private static final String PASS_VALIDATION="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{5,}$";

	public RegisterGUI() {

		ResourceBundle bundle = ResourceBundle.getBundle("Etiquetas");

		setTitle(bundle.getString("MainGUI.Register"));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(600, 350);
		setResizable(false);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(40, 60, 40, 60));
		contentPane.setLayout(new BorderLayout(10, 20));
		setContentPane(contentPane);

		JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 15));

		// USERNAME
		JLabel lblUsername = new JLabel(bundle.getString("RegisterGUI.Username"));
		lblUsername.setHorizontalAlignment(SwingConstants.RIGHT);
		textFieldUsername = new JTextField();

		// PASSWORD
		JLabel lblPassword = new JLabel(bundle.getString("RegisterGUI.Password"));
		lblPassword.setHorizontalAlignment(SwingConstants.RIGHT);
		passwordField = new JPasswordField();

		// REPEAT PASSWORD
		JLabel lblRepeat = new JLabel(bundle.getString("RegisterGUI.RepeatPassword"));
		lblRepeat.setHorizontalAlignment(SwingConstants.RIGHT);
		repeatPasswordField = new JPasswordField();

		formPanel.add(lblUsername);
		formPanel.add(textFieldUsername);

		//EMAIL
		JLabel lblEmail = new JLabel(bundle.getString("RegisterGUI.Email"));
		lblEmail.setHorizontalAlignment(SwingConstants.RIGHT);
		formPanel.add(lblEmail);



		textFieldEmail = new JTextField();
		formPanel.add(textFieldEmail);
		formPanel.add(lblPassword);
		formPanel.add(passwordField);
		formPanel.add(lblRepeat);
		formPanel.add(repeatPasswordField);

		contentPane.add(formPanel, BorderLayout.CENTER);



		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
		JButton btnRegister = new JButton(bundle.getString("MainGUI.Register"));
		JButton btnClose = new JButton(bundle.getString("RegisterGUI.CloseButton"));

		btnRegister.setPreferredSize(new Dimension(140, 28));
		btnClose.setPreferredSize(new Dimension(140, 28));
		buttonPanel.add(btnRegister);
		buttonPanel.add(btnClose);
		btnClose.setBackground(new Color(231, 76, 60));
		btnClose.setForeground(Color.WHITE);
		btnClose.setFont(new Font("Tahoma", Font.BOLD, 12));
		contentPane.add(buttonPanel, BorderLayout.SOUTH);

		btnClose.addActionListener(e -> toMainNonRegGUI());


		//Error Label
		errorLabel = new JLabel(" ");
		errorLabel.setForeground(Color.red);
		errorLabel.setAlignmentX(CENTER_ALIGNMENT);
		contentPane.add(errorLabel, BorderLayout.NORTH);

		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String password = new String(passwordField.getPassword());
				String password2 = new String(repeatPasswordField.getPassword());
				BLFacade facade = MainGUInonReg.getBusinessLogic();

				String email = textFieldEmail.getText().trim();
				String username = textFieldUsername.getText().trim();

				try {
					System.out.println(email.matches(EMAIL_AE));

					isValidEmail(email);
					isValidPassWord(password);
					if(username.length() > 0) {
						if(password.equals(password2)) {
							if(!facade.isRegistered(email)) {
								facade.register(new Registered(textFieldEmail.getText(),username,password));
								toMainNonRegGUI();
							}else {
								errorLabel.setText(bundle.getString("RegisterGUI.UserAlreadyExists"));

							}

						}else {
							errorLabel.setText(bundle.getString("RegisterGUI.PasswordMismatch"));
						}

					}else {
						errorLabel.setText(bundle.getString("RegisterGUI.UsernameEmpty"));
					}



				}catch(EmailNotValidException e){
					errorLabel.setText(bundle.getString("RegisterGUI.notValidMail"));

				}catch(PasswordNotValidException p) {
					errorLabel.setText(bundle.getString("RegisterGUI.notValidPassword"));

				}
				
			}

		});


	}


	private  boolean isValidEmail(String email) throws EmailNotValidException {
		if (email == null || email.length()== 0 || !email.matches(EMAIL_AE) || email.contains("@admin")){
			throw new EmailNotValidException();
		}
		return true;

	}
	private  boolean isValidPassWord(String pass) throws PasswordNotValidException{
		if (pass == null || pass.length()== 0 || !pass.matches(PASS_VALIDATION)){
			throw new PasswordNotValidException();	
		}
		return true;
	}
	public void toMainNonRegGUI() {
		dispose();
		MainGUInonReg a = new MainGUInonReg(null);
		a.setVisible(true);	
	}

}
