package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import domain.Registered;
import domain.RegisteredAdapter;

public class RegisteredTable extends JFrame{
	private Registered reg;
	private JTable	tabla;
	public RegisteredTable(Registered reg){
		super(reg.getName()+"’s	sales ");
		this.setBounds(100,	100, 700, 200);
		this.reg = reg;
		RegisteredAdapter adapt	= new RegisteredAdapter(reg);
		tabla =	new JTable(adapt);
		tabla.setPreferredScrollableViewportSize(new Dimension(500,	70));

		JScrollPane	scrollPane = new JScrollPane(tabla);

		getContentPane().add(scrollPane, BorderLayout.CENTER);
	}
}