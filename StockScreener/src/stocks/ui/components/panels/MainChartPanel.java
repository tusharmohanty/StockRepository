package stocks.ui.components.panels;

import java.awt.CardLayout;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;

import stocks.ui.components.charts.MainPriceChart;
import stocks.ui.components.charts.TradeChart;

public class MainChartPanel extends JPanel{
public static final MainChartPanel INSTANCE = new MainChartPanel();

JPanel priceChart;
JPanel tradeChart;
private MainChartPanel() {
	this.setLayout(new CardLayout());
	addComponentToPane();
}


private void addComponentToPane() {
	priceChart = new JPanel();
	priceChart.add(MainPriceChart.INSTANCE.chartPanel);
	tradeChart = new JPanel();
	tradeChart.add(TradeChart.INSTANCE.chartPanel);
	this.add(priceChart,"PriceChart");
	this.add(tradeChart,"TradeChart");
	CardLayout cl = (CardLayout)(this.getLayout());
    cl.show(this, "PriceChart");
	
	
}
}
