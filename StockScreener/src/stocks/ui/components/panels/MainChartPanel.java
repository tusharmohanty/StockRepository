package stocks.ui.components.panels;

import javax.swing.JPanel;

import stocks.ui.components.charts.MainPriceChart;

public class MainChartPanel extends JPanel{
public static final MainChartPanel INSTANCE = new MainChartPanel();
private MainChartPanel() {
	this.add(MainPriceChart.INSTANCE.chartPanel);
}
}
