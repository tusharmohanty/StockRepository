package stocks.ui.components.tableModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import stocks.model.beans.PositionBean;
import stocks.ui.components.panels.OpenPositionPanel;

public class PositionTableModel extends AbstractTableModel{
	private String[] columnNames = {"Date","Buy price","Qty","Gain/Loss %"};
	
	public int getColumnCount() {
	      return columnNames.length;
	    }

	    public int getRowCount() {
	      return OpenPositionPanel.INSTANCE.openPositions.size();
	    }

	    public String getColumnName(int col) {
	      return columnNames[col];
	    }

	    public Object getValueAt(int row, int col) {
	    	
		    	DateFormat dateFormat = new SimpleDateFormat("dd/MMM/YYYY");
		    	String returnValue = "";
		    	PositionBean data = OpenPositionPanel.INSTANCE.openPositions.get(row);
		    	if(col==0) {
		    		returnValue = dateFormat.format(data.getTxnDate());
		    	}
		    	else if (col ==1) {
		    		returnValue = data.getPrice().toString();
		    	}
		    	else if (col ==2) {
		    		returnValue = data.getQty() +"";
		    	}
		    	else if (col ==3) {
		    		returnValue = "";
		    	}
		    	return returnValue;
	    }
	    public Class getColumnClass(int c) {
	        return String.class;
	    }
	    public boolean isCellEditable(int row, int col) {
	        return false;
	    }
}

