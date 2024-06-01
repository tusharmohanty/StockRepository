package stocks.ui.components.panels;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;

import stocks.model.beans.PositionBean;
import stocks.model.beans.StockDataBean;
import stocks.model.beans.StocksBean;
import stocks.model.data.DataAccess;
import stocks.ui.components.Dashboard;
import stocks.ui.components.charts.TradeChart;
import stocks.ui.components.charts.MainPriceChart;

import static stocks.util.Utils.openScreener;

public class StockPanel extends JPanel implements ActionListener{
public static final StockPanel INSTANCE = new StockPanel();
JComboBox scripComboBox = null;
JButton launchScreener = null;
String selectedScripCode = "";
List<StocksBean> stocksBeanList = null;
private StockPanel() {
	try {
		stocksBeanList =DataAccess.INSTANCE.getStockList();
		initializeComponents();
	}
	catch(Exception E) {
		E.printStackTrace();
	}
	this.setLayout(new GridBagLayout());
	addComponentsToPanel();
}
private void addComponentsToPanel() {
    this.add(scripComboBox);
	this.add(launchScreener);
}

private void initializeComponents() throws SQLException {
	scripComboBox = new JComboBox(stocksBeanList.toArray());
	selectedScripCode = DataAccess.INSTANCE.getStockList().get(0).getStockCode(); // on initialization the first value will be teh selected scrip
	scripComboBox.setFont(new Font("Aptos", Font.PLAIN,11));
	scripComboBox.addActionListener(this);
	launchScreener = new JButton("Screener");
	launchScreener.setFont(new Font("Aptos", Font.PLAIN,11));
	launchScreener.setMargin(new Insets(0,0,0,0));
	launchScreener.addActionListener(this);
}

public void actionPerformed(ActionEvent e) {
    if(e.getSource() instanceof JComboBox ) {
        JComboBox cb = (JComboBox)e.getSource();
        StocksBean selectedStock = (StocksBean)cb.getSelectedItem();
        if(selectedStock != null){
        	   selectedScripCode = selectedStock.getStockCode();
                //setModel(selectedStock.getStockSymbol());
        	   try {
        		   
        		   MainPriceChart.INSTANCE.stockData = DataAccess.INSTANCE.getStockData(selectedScripCode);
        		   MainPriceChart.INSTANCE.positionData= DataAccess.INSTANCE.getPositionData(selectedScripCode,"A");
        		   MainPriceChart.INSTANCE.closedPositionData=DataAccess.INSTANCE.getPositionData(selectedScripCode,"I");
        		   MainPriceChart.INSTANCE.selectedStock = selectedScripCode;
        		   MainPriceChart.INSTANCE.refreshDataSet();
        		   
        		   TradeChart.INSTANCE.stockData = MainPriceChart.INSTANCE.stockData;
        		   TradeChart.INSTANCE.refreshDataSet();
        		   BasicInfoPanel.INSTANCE.data[0][1]= new DecimalFormat("##.##").format(DataAccess.INSTANCE.latestValue*0.002);
        		   BasicInfoPanel.INSTANCE.data[1][1]= new DecimalFormat("##.##").format(DataAccess.INSTANCE.latestValue*1.002);
//        		   OpenPositionPanel.INSTANCE.openPositions = MainPriceChart.INSTANCE.positionData;
//        		   OpenPositionPanel.INSTANCE.table.repaint();
        		   BasicInfoPanel.INSTANCE.table.repaint();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
           //DashBoard.getInstance().stockDetailPanelObj.stockDetailTable.setModel(new DashboardQuoteDetailsTableModel(selectedStock.getStockSymbol(),new Date(),100));

        }
    }
	else if(e.getSource() instanceof JButton ) {
		openScreener(selectedScripCode);
	}
}
   public void setSelectedScripCode(String stockCode){
	this.selectedScripCode= stockCode;
	StocksBean selectedObj = null;
	for (int tempCount =0 ; tempCount < stocksBeanList.size();tempCount ++){
		if(stocksBeanList.get(tempCount).getStockCode().equals(stockCode)){
			selectedObj = stocksBeanList.get(tempCount);
			break;
		}
	}
	scripComboBox.setSelectedItem(selectedObj);
   }

}
