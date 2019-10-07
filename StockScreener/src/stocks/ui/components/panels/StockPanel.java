package stocks.ui.components.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.sql.SQLException;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import stocks.model.data.DataAccess;

public class StockPanel extends JPanel{
public static final StockPanel INSTANCE = new StockPanel();
JLabel scripCode = null;
JComboBox scripComboBox = null;
private StockPanel() {
	try {
		initializeComponents();
	}
	catch(Exception E) {
		E.printStackTrace();
	}
	this.setLayout(new GridBagLayout());
	addComponentsToPanel();
}
private void addComponentsToPanel() {
    this.add(scripCode);
    this.add(scripComboBox);
}

private void initializeComponents() throws SQLException {
	scripCode = new JLabel("Scrip Code");
	scripComboBox = new JComboBox(DataAccess.INSTANCE.getStockList().toArray());
}
}
