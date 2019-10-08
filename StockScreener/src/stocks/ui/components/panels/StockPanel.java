package stocks.ui.components.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import stocks.model.beans.StockDataBean;
import stocks.model.beans.StocksBean;
import stocks.model.data.DataAccess;
import stocks.ui.components.Dashboard;
import stocks.ui.components.charts.MainPriceChart;

public class StockPanel extends JPanel implements ActionListener{
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
	scripComboBox.addActionListener(this);
}

public void actionPerformed(ActionEvent e) {
    if(e.getSource() instanceof JComboBox ) {
        JComboBox cb = (JComboBox)e.getSource();
        StocksBean selectedStock = (StocksBean)cb.getSelectedItem();
        if(selectedStock != null){
        	   String scripCode = selectedStock.getStockCode();
                //setModel(selectedStock.getStockSymbol());
        	   List <StockDataBean> stockData = null;
        	   try {
        		   stockData = DataAccess.INSTANCE.getStockData(scripCode);
        		   MainPriceChart.INSTANCE.stockData = stockData;
        		   MainPriceChart.INSTANCE.refreshDataSet();;
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
           //DashBoard.getInstance().stockDetailPanelObj.stockDetailTable.setModel(new DashboardQuoteDetailsTableModel(selectedStock.getStockSymbol(),new Date(),100));

        }
    }
}


}
