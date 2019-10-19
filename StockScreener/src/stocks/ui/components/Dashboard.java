package stocks.ui.components;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import layout.TableLayout;
import javax.swing.JFrame;

import stocks.ui.components.panels.BasicInfoPanel;
import stocks.ui.components.panels.InvestmentChartPanel;
import stocks.ui.components.panels.MainChartPanel;
import stocks.ui.components.panels.StockPanel;

public class Dashboard extends JFrame{
	public static final StockPanel stockPanel = StockPanel.INSTANCE;
	public static final MainChartPanel chartPanel = MainChartPanel.INSTANCE;
	public static final InvestmentChartPanel invstchartPanel = InvestmentChartPanel.INSTANCE;
	public static final BasicInfoPanel basicInfoPanel = BasicInfoPanel.INSTANCE;
	public static final Dashboard INSTANCE = new Dashboard(); 
    private  Dashboard() {
    	   this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    	   this.setTitle("Stock Screener 1.0");
    	   this.setPreferredSize(new Dimension(3000,12000));
    	   this.setLocationRelativeTo(null);
    	   
    }
    
    private void addMainComponentsToPane(Container pane) {
    	double size[][] =
            {{0.15,0.85},
             {0.05,0.24,0.1,0.1,0.1,0.1,0.1,0.1,0.08,0.03}};

    this.setLayout(new TableLayout(size));
    this.add(stockPanel,    "0,0,0,0");
    this.add(basicInfoPanel,"0,1,0,4");
    this.add(invstchartPanel,    "1,0,1,9");
    }
    
    public void initializeComponents(){
    	addMainComponentsToPane(this.getContentPane());
    	this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        Dashboard uiObj = Dashboard.INSTANCE;
        uiObj.initializeComponents();
        uiObj.pack();
        uiObj.setVisible(true);
	}

}
