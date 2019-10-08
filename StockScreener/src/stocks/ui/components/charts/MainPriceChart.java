package stocks.ui.components.charts;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.RectangleAnchor;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.general.DatasetUtils;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;
import org.jfree.chart.plot.Crosshair;

import stocks.model.beans.StockDataBean;
import stocks.model.data.DataAccess;

public class MainPriceChart implements ChartMouseListener{
JFreeChart mainChart= null;
public ChartPanel chartPanel = null;

private static final int SERIES_COUNT = 2;

private Crosshair xCrosshair;
private Crosshair[] yCrosshairs;
public List<StockDataBean> stockData = null;

public static final MainPriceChart INSTANCE = new MainPriceChart();

private MainPriceChart() {
	// Get all teh data first
	try {
		stockData = DataAccess.INSTANCE.getStockData("TCS");
    } catch (SQLException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
   }
	// get the main chart sorted 
	mainChart = ChartFactory.createTimeSeriesChart(null, "Date", "Price", getMainDataSet(), false, false,false);
	chartPanel = new ChartPanel(mainChart);
	initializeChart();
	chartPanel.setPreferredSize(new java.awt.Dimension(2000, 1200));
	//chartPanel.setMouseZoomable(true, false);
	
}

public void refreshDataSet() {
	XYPlot plot = (XYPlot) mainChart.getPlot();
	plot.setDataset(0,getMainDataSet());
	plot.setDataset(1,getEarningsDataSet());
}

private  void initializeChart() {
	getMainDataSet();
	XYPlot plot = (XYPlot) mainChart.getPlot();
    plot.setBackgroundPaint(Color.lightGray);
    plot.setDomainGridlinePaint(Color.white);
    plot.setRangeGridlinePaint(Color.white);
    plot.setDomainCrosshairVisible(true);
    plot.setRangeCrosshairVisible(true);
    XYItemRenderer r = plot.getRenderer();
    if (r instanceof XYLineAndShapeRenderer) {
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
        renderer.setDefaultShapesVisible(false);
        renderer.setDefaultShapesFilled(false);
    }
    DateAxis axis = (DateAxis) plot.getDomainAxis();
    axis.setDateFormatOverride(new SimpleDateFormat("dd-MM-yyyy"));
    
   
    
 // AXIS 2
    NumberAxis axis2 = new NumberAxis("Earnings");
    axis2.setAutoRangeIncludesZero(false);
    plot.setRangeAxis(1, axis2);
    plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_LEFT);
    plot.setDataset(1, getEarningsDataSet());
    plot.mapDatasetToRangeAxis(1, 1);
    XYItemRenderer renderer2 = new StandardXYItemRenderer();
    plot.setRenderer(1, renderer2);
    
    CrosshairOverlay crosshairOverlay = new CrosshairOverlay();
    this.xCrosshair = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0.5f));
    this.xCrosshair.setLabelVisible(true);
    crosshairOverlay.addDomainCrosshair(xCrosshair);
    this.yCrosshairs = new Crosshair[SERIES_COUNT];
    for (int i = 0; i < SERIES_COUNT; i++) {
        this.yCrosshairs[i] = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0.5f));
        this.yCrosshairs[i].setLabelVisible(true);
        if (i % 2 != 0) {
            this.yCrosshairs[i].setLabelAnchor(RectangleAnchor.TOP_RIGHT);
        }
        crosshairOverlay.addRangeCrosshair(yCrosshairs[i]);
    }
    chartPanel.addOverlay(crosshairOverlay);
    this.chartPanel.addChartMouseListener(this);
}
private XYDataset getEarningsDataSet() {
	
    TimeSeries earnings = new TimeSeries("Earnings");
    TimeSeriesCollection earningsDataSet = new TimeSeriesCollection();
	for(int tempCount=0; tempCount < stockData.size();tempCount++) {
		StockDataBean beanObj = stockData.get(tempCount);
		Day dayObj = new Day(beanObj.getDateObj().get(Calendar.DATE),
		          beanObj.getDateObj().get(Calendar.MONTH) + 1, 
		          beanObj.getDateObj().get(Calendar.YEAR));
		earnings.add(dayObj,beanObj.getPE());
		
	}
	earningsDataSet.addSeries(earnings);
	return earningsDataSet;
	
}
private XYDataset getMainDataSet() {
	TimeSeries price = new TimeSeries("Price");
	
	TimeSeriesCollection dataSet = new TimeSeriesCollection();
	for(int tempCount=0; tempCount < stockData.size();tempCount++) {
		StockDataBean beanObj = stockData.get(tempCount);
		Day dayObj = new Day(beanObj.getDateObj().get(Calendar.DATE),
		          beanObj.getDateObj().get(Calendar.MONTH) + 1, 
		          beanObj.getDateObj().get(Calendar.YEAR));
		price.add(dayObj,beanObj.getClose());
	}
	dataSet.addSeries(price);
	return dataSet;
}

/**
 * Update the crosshairs.
 * 
 * @param event  the mouse event.
 */
@Override
public void chartMouseMoved(ChartMouseEvent event) {
    Rectangle2D dataArea = this.chartPanel.getScreenDataArea();
    JFreeChart chart = event.getChart();
    XYPlot plot = (XYPlot) chart.getPlot();
    ValueAxis xAxis = plot.getDomainAxis();
    double x = xAxis.java2DToValue(event.getTrigger().getX(), dataArea, 
            RectangleEdge.BOTTOM);
    this.xCrosshair.setValue(x);
    this.yCrosshairs[0].setValue(DatasetUtils.findYValue(plot.getDataset(), 0, x));
    this.yCrosshairs[1].setValue(DatasetUtils.findYValue(plot.getDataset(1), 0, x));
//    for (int i = 0; i < SERIES_COUNT; i++) {
//        double y = DatasetUtils.findYValue(plot.getDataset(), i, x);
//        this.yCrosshairs[i].setValue(y);
//    }
    //System.out.println(DatasetUtils.findYValue(plot.getDataset(1),0,x));
}

@Override
public void chartMouseClicked(ChartMouseEvent event) {
    // ignore
}
}
