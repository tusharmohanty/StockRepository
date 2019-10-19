package stocks.ui.components.panels;

import javax.swing.JPanel;

import stocks.ui.components.charts.InvestmentChart;
import stocks.ui.components.charts.MainPriceChart;

public class InvestmentChartPanel extends JPanel{
public static final InvestmentChartPanel INSTANCE = new InvestmentChartPanel();
private InvestmentChartPanel() {
	this.add(InvestmentChart.INSTANCE.chartPanel);
}
}
