package stocks.ui.components.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import stocks.model.beans.StockDataBean;
import stocks.model.beans.StocksBean;
import stocks.model.data.DataAccess;
import stocks.ui.components.Dashboard;
import stocks.ui.components.charts.MainPriceChart;

public class BasicInfoPanel extends JPanel implements ActionListener{
public static final BasicInfoPanel INSTANCE = new BasicInfoPanel();
private String[] columnNames = {"Data","Value"};
public Object[][] data = {
        {"Brokerage", DataAccess.INSTANCE.latestValue*0.002},
        {"Brek even", DataAccess.INSTANCE.latestValue*1.002}
        };

JTable table = null;
JScrollPane scrollPane = null;

private BasicInfoPanel() {
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
	table = new JTable(data, columnNames);
    table.setFillsViewportHeight(true);
	scrollPane = new JScrollPane(table);
}

public void actionPerformed(ActionEvent e) {
}


}
