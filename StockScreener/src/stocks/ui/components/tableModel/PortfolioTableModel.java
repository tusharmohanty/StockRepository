package stocks.ui.components.tableModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import stocks.model.beans.PortfolioBean;
import stocks.ui.components.panels.PortfolioPanel;

public class PortfolioTableModel extends AbstractTableModel{
    private String[] columnNames = {"Stock","AvgPrice","Current Price","Qty","Invst","PL","%PL","W%","S","Delta","Move"};

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return PortfolioPanel.INSTANCE.portfolioData.size();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {

        DateFormat dateFormat = new SimpleDateFormat("dd/MMM/YYYY");
        Object returnValue = "";
        PortfolioBean data = PortfolioPanel.INSTANCE.portfolioData.get(row);
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
            returnValue = Double.parseDouble(data.getTotalInvestment() + "");
        }
        else if (col ==5) {
            returnValue = Double.parseDouble(data.getPlActual() + "");
        }
        else if (col ==6) {
            returnValue = Double.parseDouble(data.getPL() + "");
        }
        else if (col ==7) {
            returnValue = Double.parseDouble(data.getWeightage() + "");
        }
        else if (col ==8) {
            returnValue = "S";
        }
        else if (col ==9) {
            returnValue = Double.parseDouble(data.getDelta() + "");;
        }
        else if (col ==10) {
            returnValue = Double.parseDouble(data.getMove() + "");;
        }
        return returnValue;
    }
    public Class getColumnClass(int c) {
        if(c == 7 || c== 6 || c== 5 || c ==4 || c==9 || c==10){
            return Double.class;
        }
        else {
            return String.class;
        }
    }
    public boolean isCellEditable(int row, int col) {
        return false;
    }
}

