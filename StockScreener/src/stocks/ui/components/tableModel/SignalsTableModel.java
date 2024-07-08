package stocks.ui.components.tableModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import stocks.model.beans.PositionBean;
import stocks.model.beans.SignalsBean;
import stocks.ui.components.panels.OpenPositionPanel;
import stocks.ui.components.panels.SignalsPanel;

public class SignalsTableModel extends AbstractTableModel{
    private String[] columnNames = {"Stock","Signals","Shortlist"};

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {

        return SignalsPanel.INSTANCE.signals.size();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {

        String returnValue = "";
        SignalsBean data = SignalsPanel.INSTANCE.signals.get(row);
        if(col==0) {
            returnValue = data.getStockCode();
        }
        else if (col ==1) {
            returnValue = data.getSignalType();
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

