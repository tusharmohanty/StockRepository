package stocks.ui.components;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import layout.TableLayout;
import javax.swing.JFrame;

import stocks.ui.components.panels.MainChartPanel;
import stocks.ui.components.panels.StockPanel;

public class Dashboard extends JFrame{
	StockPanel stockPanel = StockPanel.INSTANCE;
	MainChartPanel chartPanel = MainChartPanel.INSTANCE;
    public Dashboard() {
    	   this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    	   this.setTitle("Stock Screener 1.0");
    	   this.setPreferredSize(new Dimension(3000,12000));
    	   this.setLocationRelativeTo(null);
    	   
    }
    
    private void addMainComponentsToPane(Container pane) {
    	double size[][] =
            {{0.12,0.88},
             {0.05,0.24,0.1,0.1,0.1,0.1,0.1,0.1,0.08,0.03}};

    this.setLayout(new TableLayout(size));
    this.add(stockPanel,"0,0,0,0");
    this.add(chartPanel,"1,0,1,9");
    }
    
    public void initializeComponents(){
    	addMainComponentsToPane(this.getContentPane());
    }
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        Dashboard uiObj = new Dashboard();
        uiObj.initializeComponents();
        uiObj.pack();
        uiObj.setVisible(true);
	}

}
