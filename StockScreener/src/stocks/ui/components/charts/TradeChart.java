package stocks.ui.components.charts;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.sql.SQLException;
import java.text.DecimalFormat;
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
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.RectangleAnchor;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.general.DatasetUtils;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.data.time.ohlc.OHLCItem;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.PlotOrientation;

import stocks.model.beans.StockDataBean;
import stocks.model.data.DataAccess;

public class TradeChart implements ChartMouseListener{
JFreeChart mainChart= null;
public ChartPanel chartPanel = null;

private static final int SERIES_COUNT = 2;

private Crosshair xCrosshair;
private Crosshair[] yCrosshairs;
public List<StockDataBean> stockData = null;
private OHLCSeries ohlcSeries;
private TimeSeries volumeSeries;

public static final TradeChart INSTANCE = new TradeChart();

private TradeChart() {
	// Get all teh data first
	try {
		stockData = DataAccess.INSTANCE.getStockData(null);
    } catch (SQLException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
   }
	// get the main chart sorted 
	mainChart = createCandlStickChart();
	chartPanel = new ChartPanel(mainChart);
	chartPanel.setPreferredSize(new java.awt.Dimension(2200, 1200));
	//chartPanel.setMouseZoomable(true, false);
	
}

private JFreeChart createCandlStickChart() {
	OHLCSeriesCollection candlestickDataset = new OHLCSeriesCollection();
	ohlcSeries = new OHLCSeries("Price");
	candlestickDataset.addSeries(ohlcSeries);
	NumberAxis priceAxis = new NumberAxis("Price");
	priceAxis.setAutoRangeIncludesZero(false);
	// Create candlestick chart renderer
	CandlestickRenderer candlestickRenderer = new CandlestickRenderer(CandlestickRenderer.WIDTHMETHOD_AVERAGE);
	// Create candlestickSubplot
	XYPlot candlestickSubplot = new XYPlot(candlestickDataset, null, priceAxis, candlestickRenderer);
	candlestickSubplot.setBackgroundPaint(Color.white);
    
	/**
	 * Creating volume subplot
	 */
	// creates TimeSeriesCollection as a volume dataset for volume chart
	TimeSeriesCollection volumeDataset = new TimeSeriesCollection();
	volumeSeries = new TimeSeries("Volume");
	volumeDataset.addSeries(volumeSeries);
	// Create volume chart volumeAxis
	NumberAxis volumeAxis = new NumberAxis("Volume");
	volumeAxis.setAutoRangeIncludesZero(false);
	// Set to no decimal
	volumeAxis.setNumberFormatOverride(new DecimalFormat("0"));
	// Create volume chart renderer
	XYBarRenderer timeRenderer = new XYBarRenderer();
	timeRenderer.setShadowVisible(false);
	//timeRenderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator("Volume--> Time={1} Size={2}",
	//		new SimpleDateFormat("kk:mm"), new DecimalFormat("0")));
	// Create volumeSubplot
	XYPlot volumeSubplot = new XYPlot(volumeDataset, null, volumeAxis, timeRenderer);
	volumeSubplot.setBackgroundPaint(Color.white);
	/**
	 * Create chart main plot with two subplots (candlestickSubplot,
	 * volumeSubplot) and one common dateAxis
	 */
	// Creating charts common dateAxis
	DateAxis dateAxis = new DateAxis("Time");
	dateAxis.setDateFormatOverride(new SimpleDateFormat("dd-MM-yyyy"));
	// reduce the default left/right margin from 0.05 to 0.02
	dateAxis.setLowerMargin(0.02);
	dateAxis.setUpperMargin(0.02);
	// Create mainPlot
	CombinedDomainXYPlot mainPlot = new CombinedDomainXYPlot(dateAxis);
	mainPlot.setGap(10.0);
	mainPlot.add(candlestickSubplot, 3);
	mainPlot.add(volumeSubplot, 1);
	mainPlot.setOrientation(PlotOrientation.VERTICAL);

	JFreeChart chart = new JFreeChart(null, JFreeChart.DEFAULT_TITLE_FONT, mainPlot, true);
	chart.removeLegend();
	return chart;

}

public void refreshDataSet() {
	XYPlot plot = (XYPlot) mainChart.getPlot();
	ohlcSeries.clear();
	volumeSeries.clear();
	getMainDataSet();
	//plot.setDataset(1,getEarningsDataSet());
}

private void getMainDataSet() {
	
	for(int tempCount=0; tempCount < 100 && stockData.size() >0;tempCount++) {
		StockDataBean beanObj = stockData.get(tempCount);
		Day dayObj = new Day(beanObj.getDateObj().get(Calendar.DATE),
		          beanObj.getDateObj().get(Calendar.MONTH) + 1, 
		          beanObj.getDateObj().get(Calendar.YEAR));
		ohlcSeries.add(new OHLCItem(dayObj, beanObj.getOpen(),beanObj.getHigh(), beanObj.getLow(),beanObj.getClose()));
		volumeSeries.add(new TimeSeriesDataItem(dayObj,beanObj.getVolume()));
	}
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
