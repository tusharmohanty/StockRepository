package stocks.ui.components.panels;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import stocks.model.beans.StockDataBean;
import stocks.model.beans.StocksBean;
import stocks.model.data.DataAccess;
import stocks.ui.components.Dashboard;
import stocks.ui.components.charts.MainPriceChart;
import stocks.ui.components.charts.TradeChart;

public class OptionsPanel extends JPanel implements ActionListener{
public static final OptionsPanel INSTANCE = new OptionsPanel();
JComboBox switchView = null;
JComboBox portfolio = null;
private OptionsPanel() {
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
    this.add(switchView);
    this.add(portfolio);
}

private void initializeComponents() throws SQLException {
	switchView = new JComboBox(new String[] {"Invst","Trade"});
	switchView.addActionListener(this);
	switchView.setActionCommand("chartView");
	portfolio = new JComboBox(new String[] {"Open","Watchlist"});
	portfolio.addActionListener(this);
	portfolio.setActionCommand("portfolio");
}

public void actionPerformed(ActionEvent e) {
    if(e.getSource() instanceof JComboBox  && e.getActionCommand().equals("chartView")) {
        JComboBox cb = (JComboBox)e.getSource();
        String view = (String) cb.getSelectedItem();
        if(view != null){
        		   if(view.equals("Invst")){
        			   CardLayout cl = (CardLayout)(MainChartPanel.INSTANCE.getLayout());
        			   cl.show(MainChartPanel.INSTANCE, "PriceChart");
        			   //Dashboard.INSTANCE.repaint();
        		   }
        		   else if(view.equals("Trade")) {
        			   CardLayout cl = (CardLayout)(MainChartPanel.INSTANCE.getLayout());
        			   cl.show(MainChartPanel.INSTANCE, "TradeChart");
        			   //Dashboard.INSTANCE.repaint();
        		   }
        }
    }
    else if (e.getSource() instanceof JComboBox  && e.getActionCommand().equals("portfolio")) {
            JComboBox cb = (JComboBox)e.getSource();
            String view = (String) cb.getSelectedItem();
            if(view != null){
            		   if(view.equals("Open")){
            			   try {
							StockPanel.INSTANCE.scripComboBox.setModel(new DefaultComboBoxModel(DataAccess.INSTANCE.getPortfolioStockList().toArray()));
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
            		   }
            		   else if(view.equals("Watchlist")) {
            			   try {
							StockPanel.INSTANCE.scripComboBox.setModel(new DefaultComboBoxModel(DataAccess.INSTANCE.getWatchListStockList().toArray()));
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
            		   }
            }
       
    }
}


}
