package gui;

import businessLogic.BLFacade;
import configuration.UtilDate;
import domain.Registered;
import domain.Sale;
import domain.SaleContainer;
import enums.MovementType;
import enums.QueryFilterType;
import enums.SaleStatusType;
import enums.SaleType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import javax.swing.table.DefaultTableModel;


public class QuerySaleGUI extends JFrame {

	private String currentMail;
	private SaleType saleType;

	private static final long serialVersionUID = 1L;
	private final JLabel jLabelProducts = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("QuerySalesGUI.initialize")); 

	private JButton jButtonSearch = new JButton(ResourceBundle.getBundle("Etiquetas").getString("QuerySalesGUI.Search")); 
	private JButton jButtonClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));

	private JScrollPane scrollPanelProducts = new JScrollPane();

	private JTable tableProducts= new JTable();
	private DefaultTableModel tableModelProducts;

	private JFrame thisFrame; 

	private String[] columnNamesProducts = new String[] {
			ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.Title"), 
			ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.Price"),
			ResourceBundle.getBundle("Etiquetas").getString("CreateSaleGUI.PublicationDate"),

	};

	private JTextField jTextFieldSearch;

	private String QueryMessagge = "";
	private String emptyQueryMessagge = "";
	
	private ArrayList<Sale> basket = new ArrayList<Sale>();
	private String sellerEmail = "";


	
	public QuerySaleGUI(String currentUserMail, SaleType saleType) {
		this.currentMail = currentUserMail;
		this.saleType = saleType;

		BLFacade facade = MainGUInonReg.getBusinessLogic();
		tableProducts.setEnabled(false);
		thisFrame=this;
		this.getContentPane().setLayout(null);
		this.setSize(new Dimension(700, 500));
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("QuerySalesGUI.FindProducts"));
		
		jLabelProducts.setBounds(52, 108, 427, 16);
		this.getContentPane().add(jLabelProducts);

		jButtonClose.setBounds(new Rectangle(220, 379, 130, 30));
		jButtonClose.setBackground(new Color(231, 76, 60));
        jButtonClose.setForeground(Color.WHITE);
        jButtonClose.setFont(new Font("Tahoma", Font.BOLD, 12));
		jButtonClose.addActionListener(e -> thisFrame.setVisible(false));
		this.getContentPane().add(jButtonClose, null);

		scrollPanelProducts.setBounds(new Rectangle(52, 137, 459, 150));
		scrollPanelProducts.setViewportView(tableProducts);
		
		tableModelProducts = new DefaultTableModel(null, columnNamesProducts);
		tableProducts.setModel(tableModelProducts);

		tableModelProducts.setDataVector(null, columnNamesProducts);
		tableModelProducts.setColumnCount(4); // another column added to allocate ride objects

		tableProducts.getColumnModel().getColumn(0).setPreferredWidth(200);
		tableProducts.getColumnModel().getColumn(1).setPreferredWidth(20);
		tableProducts.getColumnModel().getColumn(2).setPreferredWidth(70);


		tableProducts.getColumnModel().removeColumn(tableProducts.getColumnModel().getColumn(3)); // not shown in JTable

		this.getContentPane().add(scrollPanelProducts, null);

		jTextFieldSearch = new JTextField();
		jTextFieldSearch.setBounds(52, 56, 357, 26);
		getContentPane().add(jTextFieldSearch);
		jTextFieldSearch.setColumns(10);


		jButtonSearch.setBounds(427, 56, 117, 29);
		getContentPane().add(jButtonSearch);
		
		JButton jButtonShBasket = new JButton(ResourceBundle.getBundle("Etiquetas").getString("QuerySalesGUI.ShBasket"));
		jButtonShBasket.setVisible(saleType.equals(SaleType.PUBLISHED_SALES)&&!currentUserMail.equals(""));
		jButtonShBasket.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ShowBasketGUI a = new ShowBasketGUI(currentUserMail,basket,thisFrame);
				a.setVisible(true);
			}
		});
		jButtonShBasket.setBounds(new Rectangle(220, 379, 130, 30));
		jButtonShBasket.setBounds(384, 379, 130, 30);
		getContentPane().add(jButtonShBasket);



		switch (saleType) {
		case ON_SALES:
			QueryMessagge = ResourceBundle.getBundle("Etiquetas").getString("QuerySalesGUI.OnSale");
			emptyQueryMessagge = ResourceBundle.getBundle("Etiquetas").getString("QuerySalesGUI.NoOnSale");
			break;
		case PUBLISHED_SALES:
			QueryMessagge = ResourceBundle.getBundle("Etiquetas").getString("QuerySalesGUI.Products");
			emptyQueryMessagge = ResourceBundle.getBundle("Etiquetas").getString("QuerySalesGUI.NoProducts");

			break;
		case PURCHASED:
			QueryMessagge = ResourceBundle.getBundle("Etiquetas").getString("QuerySalesGUI.Purchased");
			emptyQueryMessagge = ResourceBundle.getBundle("Etiquetas").getString("QuerySalesGUI.NoPurchased");

			break;
		case WISHLIST:
			QueryMessagge = ResourceBundle.getBundle("Etiquetas").getString("QuerySalesGUI.WhishList");
			emptyQueryMessagge = ResourceBundle.getBundle("Etiquetas").getString("QuerySalesGUI.NoWhishList");

			break;
		}

		jButtonSearch.addActionListener(e -> refreshQuery());
		refreshQuery();


		tableProducts.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent mouseEvent) {
				if(mouseEvent.getClickCount() == 2)
				{
					JTable table =(JTable) mouseEvent.getSource();
					Point point = mouseEvent.getPoint();
					int row = table.rowAtPoint(point);
					Sale s=(Sale) tableModelProducts.getValueAt(row, 3);
					System.out.println(currentUserMail);
					if(currentUserMail.equals("")) {
						new ShowSaleGUInonReg(s);
					}else {
						switch(saleType){
						case ON_SALES:
							new ShowSaleGUInonReg(s);
							break;
						case PUBLISHED_SALES:
							new ShowSaleGUIReg(currentUserMail,s,thisFrame);
							break;
						case PURCHASED:
							Boolean hasOffer = facade.hasReviewed(currentUserMail,s);
							new ShowSaleGUIBought(currentUserMail,s,hasOffer,thisFrame);
							break;
						case WISHLIST:
							new ShowSaleGUIReg(currentUserMail,s,thisFrame);
							break;

						}
					}
				}
			}
		});



	}

	public void refreshQuery() {
		try {
			System.out.println("QSale-Basket: " + basket);
			tableModelProducts.setDataVector(null, columnNamesProducts);
			tableModelProducts.setColumnCount(4); // another column added to allocate product object

			BLFacade facade = MainGUInonReg.getBusinessLogic();
			Date today = UtilDate.trim(new Date());

			//Query deia
			List<Sale> sales = facade.getQuery(jTextFieldSearch.getText(),today,saleType,currentMail, sellerEmail, basket);

			if (sales.isEmpty()) 	jLabelProducts.setText(emptyQueryMessagge);
			else 					jLabelProducts.setText(QueryMessagge);

			for (Sale sale : sales){
				Vector<Object> row = new Vector<Object>();
				row.add(sale.getTitle());
				row.add(sale.getPrice());
				row.add(new SimpleDateFormat("dd-MM-yyyy").format(sale.getPublicationDate()));
				row.add(sale); // product object added in order to obtain it with tableModelProducts.getValueAt(i,2)
				tableModelProducts.addRow(row);		
			}
		} catch (Exception e1) {

			e1.printStackTrace();
		}
		tableProducts.getColumnModel().getColumn(0).setPreferredWidth(200);
		tableProducts.getColumnModel().getColumn(1).setPreferredWidth(20);
		tableProducts.getColumnModel().getColumn(2).setPreferredWidth(70);
		tableProducts.getColumnModel().removeColumn(tableProducts.getColumnModel().getColumn(3)); // not shown in JTable
	}
	
	public boolean addTotheBasket(Sale s, String sellerEmail) {
		this.sellerEmail = sellerEmail;
		return basket.add(s);		
	}
	
	public void removeFromBasket(Sale sale) {
		System.out.println("QUERYGUI remove sale");
		//System.out.println(basket);
		basket.remove(sale);
		if(basket.isEmpty())  sellerEmail="";
		
	}
	
}
