package stocks.ui.components.panels;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import stocks.model.beans.StockDataBean;
import stocks.model.beans.StocksBean;
import stocks.model.data.DataAccess;
import stocks.model.data.SyncData;
import stocks.ui.components.Dashboard;
import stocks.ui.components.charts.MainPriceChart;
import stocks.ui.components.charts.TradeChart;

public class OptionsPanel extends JPanel implements ActionListener{
public static final OptionsPanel INSTANCE = new OptionsPanel();
JComboBox switchView = null;
JComboBox portfolio = null;
JButton sync= null;
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
    this.add(sync);
	this.add(switchView);
    this.add(portfolio);
    
}

private void initializeComponents() throws SQLException {
	switchView = new JComboBox(new String[] {"Invst","Trade"});
	switchView.addActionListener(this);
	switchView.setActionCommand("chartView");
	portfolio = new JComboBox(new String[] {"Open","Watchlist","Buy List"});
	portfolio.addActionListener(this);
	portfolio.setActionCommand("portfolio");
	sync = new JButton("Sync");
	sync.setActionCommand("sync");
	sync.addActionListener(this);
}

public void actionPerformed(ActionEvent e) {
    if(e.getSource() instanceof JComboBox  && e.getActionCommand().equals("chartView")) {
    		manageChartView(e);
    }
    else if (e.getSource() instanceof JComboBox  && e.getActionCommand().equals("portfolio")) {
        managePortfolio(e);
    }
    else if (e.getSource() instanceof JButton && e.getActionCommand().equals("sync")){
    	new Thread(new Runnable() {
    	    public void run() {
    	    	SyncData syncObj = new SyncData();
        	    try {
    				syncObj.syncData();
    			} catch (SQLException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			} catch (IOException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			}
    	    }
    	}).start();
    	
    }
}

private void managePortfolio(ActionEvent e) {
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
    		   else if(view.equals("Buy List")) {
    			   try {
					StockPanel.INSTANCE.scripComboBox.setModel(new DefaultComboBoxModel(DataAccess.INSTANCE.getBuyStockList().toArray()));
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
    		   }
    }
	
}

private void manageChartView(ActionEvent e) {
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

}
