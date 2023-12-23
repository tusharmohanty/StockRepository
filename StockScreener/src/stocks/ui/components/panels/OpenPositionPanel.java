package stocks.ui.components.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import stocks.model.beans.PositionBean;
import stocks.model.beans.StockDataBean;
import stocks.model.beans.StocksBean;
import stocks.model.data.DataAccess;
import stocks.ui.components.Dashboard;
import stocks.ui.components.charts.MainPriceChart;
import stocks.ui.components.tableModel.PositionTableModel;

public class OpenPositionPanel extends JPanel implements ActionListener{
public static final OpenPositionPanel INSTANCE = new OpenPositionPanel();
private String[] columnNames = {"Date","Buy price","Gain/Loss %"};
public List<PositionBean> openPositions = new ArrayList();

JTable table = null;
JScrollPane scrollPane = null;

private OpenPositionPanel() {
	this.setLayout(new BorderLayout());
	try {
		initializeComponents();
	}
	catch(Exception E) {
		E.printStackTrace();
	}
	addComponentsToPanel();
}
private void addComponentsToPanel() {
	
    this.add(scrollPane);
}

private void initializeComponents() throws SQLException {
	//DataAccess.INSTANCE.getPositionData(StockPanel.INSTANCE.scripComboBox.getSelectedItem().toString(),"A");
	//table = new JTable(new PositionTableModel());
    //table.setFillsViewportHeight(true);
	//scrollPane = new JScrollPane(table);
}

public void actionPerformed(ActionEvent e) {
}


}
