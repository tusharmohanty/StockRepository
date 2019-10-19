package stocks.ui.components.panels;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

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
}

private void initializeComponents() throws SQLException {
	switchView = new JComboBox(new String[] {"Invst","Trade"});
	switchView.addActionListener(this);
}

public void actionPerformed(ActionEvent e) {
    if(e.getSource() instanceof JComboBox ) {
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


}
