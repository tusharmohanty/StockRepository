package stocks.ui.components.tableModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import stocks.model.beans.PortfolioBean;
import stocks.ui.components.panels.PortfolioPanel;

public class PortfolioTableModel extends AbstractTableModel{
    private String[] columnNames = {"Stock","AvgPrice","Current Price","Qty","Invst","PL","%PL","Weight %","S"};

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return PortfolioPanel.INSTANCE.openPositions.size();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {

        DateFormat dateFormat = new SimpleDateFormat("dd/MMM/YYYY");
        String returnValue = "";
        PortfolioBean data = PortfolioPanel.INSTANCE.openPositions.get(row);
        if(col==0) {
            returnValue = data.getStockCode();
        }
        else if (col ==1) {
            returnValue=data.getPrice() + "";
        }
        else if (col ==2) {
            returnValue = data.getCurrentPrice()+ "";
        }
        else if (col ==3) {
            returnValue = data.getQty() + "";
        }
        else if (col ==4) {
            returnValue = data.getTotalInvestment() + "";
        }
        else if (col ==5) {
            returnValue = data.getPlActual() + "";
        }
        else if (col ==6) {
            returnValue = data.getPL() + "";
        }
        else if (col ==7) {
            returnValue = data.getWeightage() + "";
        }
        else if (col ==8) {
            returnValue = "S";
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

