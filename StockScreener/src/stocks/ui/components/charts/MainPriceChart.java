package stocks.ui.components.charts;

import java.awt.Color;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

import stocks.model.beans.StockDataBean;
import stocks.model.data.DataAccess;

public class MainPriceChart {
JFreeChart mainChart= null;
public ChartPanel chartPanel = null;

public static final MainPriceChart INSTANCE = new MainPriceChart();

private MainPriceChart() {
	initializeChart("TCS");
	chartPanel = new ChartPanel(mainChart);
	chartPanel.setPreferredSize(new java.awt.Dimension(2000, 1200));
	chartPanel.setMouseZoomable(true, false);
	
}
private void initializeChart(String scripCode) {
	
	mainChart = ChartFactory.createTimeSeriesChart("Price", "Date", "Price", getMainDataSet(scripCode), false, false,false);
	//mainChart.setBackgroundPaint(Color.black);
	XYPlot plot = (XYPlot) mainChart.getPlot();
    plot.setBackgroundPaint(Color.lightGray);
    plot.setDomainGridlinePaint(Color.white);
    plot.setRangeGridlinePaint(Color.white);
   //plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
    plot.setDomainCrosshairVisible(true);
    plot.setRangeCrosshairVisible(true);
//    XYItemRenderer r = plot.getRenderer();
//    if (r instanceof XYLineAndShapeRenderer) {
//        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
//        renderer.setBaseShapesVisible(true);
//        renderer.setBaseShapesFilled(true);
//    }
    DateAxis axis = (DateAxis) plot.getDomainAxis();
    axis.setDateFormatOverride(new SimpleDateFormat("dd-MM-yyyy"));
}

private XYDataset getMainDataSet(String scripCode) {
	TimeSeries price = new TimeSeries("Price", org.jfree.data.time.Day.class);
	List<StockDataBean> stockData = null;
	TimeSeriesCollection dataSet = new TimeSeriesCollection();
	try {
		stockData = DataAccess.INSTANCE.getStockData(scripCode);
		for(int tempCount=0; tempCount < stockData.size();tempCount++) {
			StockDataBean beanObj = stockData.get(tempCount);
			price.add(new Day(beanObj.getDateObj().get(Calendar.DATE),
					          beanObj.getDateObj().get(Calendar.MONTH) + 1, 
					          beanObj.getDateObj().get(Calendar.YEAR)),
					  beanObj.getClose());
			
		}
		dataSet.addSeries(price);
		
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return dataSet;
}
}
