package domain;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class RegisteredAdapter extends AbstractTableModel{
	protected Registered reg;
	protected String[] columnNames = new String[] {"Title", "Description", "Price" };
	protected Vector<Sale> sales;

	
	public RegisteredAdapter (Registered reg){
		this.reg = reg;
		this.sales = new Vector<Sale>(reg.getSales());
	}

	public int getColumnCount() {
		return 3;
	}

	public String getColumnName(int i) {
		return columnNames[i];
	}

	public int getRowCount() {
		return reg.getSales().size();
	}

	public Object getValueAt(int row, int col) {
		
		sales = new Vector<>(reg.getSales());
		
		if(col == 0) {
			return sales.get(row).getTitle();
		} else if (col == 1) {
			return sales.get(row).getDescription();
		}else if (col == 2) {
			return sales.get(row).getPrice();
		}
		return null;
	}

}
