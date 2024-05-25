package stocks.ui.components;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import layout.TableLayout;
import javax.swing.JFrame;

import stocks.ui.components.panels.BasicInfoPanel;
import stocks.ui.components.panels.MainChartPanel;
import stocks.ui.components.panels.OpenPositionPanel;
import stocks.ui.components.panels.OptionsPanel;
import stocks.ui.components.panels.StockPanel;

public class Dashboard extends JFrame{
	public  final StockPanel stockPanel = StockPanel.INSTANCE;
	public  final MainChartPanel chartPanel = MainChartPanel.INSTANCE;
	public  final BasicInfoPanel basicInfoPanel = BasicInfoPanel.INSTANCE;
    public  final OpenPositionPanel openPositionPanel = OpenPositionPanel.INSTANCE;
	
	public  final OptionsPanel optionsPanel = OptionsPanel.INSTANCE;
	public static final Dashboard INSTANCE = new Dashboard(); 
    private  Dashboard() {
    	   this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    	   this.setTitle("Stock Screener 1.0");
    	   this.setPreferredSize(new Dimension(3000,10000));
    	   this.setLocationRelativeTo(null);
    	   
    }
    
    private void addMainComponentsToPane(Container pane) {
    	double size[][] =
            {{0.15,0.85},
             {0.05,
            	  0.1,
            	  0.18,
            	  0.1,
            	  0.1,
            	  0.1,
            	  0.1,
            	  0.1,
            	  0.08,
            	  0.03}};

    this.setLayout(new TableLayout(size));
    this.add(stockPanel,    "0,0,0,0");
    this.add(basicInfoPanel,"0,1,0,1");
    this.add(openPositionPanel,"0,2,0,1");
    this.add(chartPanel,    "1,0,1,8");
    this.add(optionsPanel,"0,9,1,9");
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
