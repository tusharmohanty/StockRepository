package stocks.ui.components.charts;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYRangeValueAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.CrosshairLabelGenerator;
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
import org.jfree.ui.TextAnchor;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;

import stocks.model.beans.AlertBean;
import stocks.model.beans.PositionBean;
import stocks.model.beans.StockDataBean;
import stocks.model.data.DataAccess;
import stocks.util.Utils;

public class MainPriceChart implements ChartMouseListener{
JFreeChart mainChart= null;
public ChartPanel chartPanel = null;

private static final int SERIES_COUNT = 2;

private Crosshair xCrosshair;
private Crosshair[] yCrosshairs;
public List<StockDataBean> stockData = null;
public List<PositionBean> positionData = null;
private TimeSeries volumeSeries;
public static final MainPriceChart INSTANCE = new MainPriceChart();
public String selectedStock;
TimeSeries priceSeries ;
TimeSeries buyPositionSeries;
TimeSeries sellPositionSeries;
XYTextAnnotation a;
XYPlot priceSubplot;
private MainPriceChart() {
	// Get all teh data first
	try {
		stockData = DataAccess.INSTANCE.getStockData(null);
        positionData = DataAccess.INSTANCE.getPositionData(null);
    } catch (SQLException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
   }
	// get the main chart sorted 
	mainChart = createMainChart();
	chartPanel = new ChartPanel(mainChart);
	chartPanel.setPreferredSize(new java.awt.Dimension(2200, 1200));
	chartPanel.addChartMouseListener(this);
	
	initializeMainChart();
	//chartPanel.setMouseZoomable(true, false);
	
}

public void refreshDataSet() {
	volumeSeries.clear();
	priceSeries.clear();
	sellPositionSeries.clear();
	List<XYAnnotation> allAnnotations = priceSubplot.getAnnotations();
	for (int tempCount =0;tempCount < allAnnotations.size();tempCount++) {
		priceSubplot.removeAnnotation(allAnnotations.get(tempCount));
	}
	for(int tempCount=0; tempCount < 100 && stockData.size() >0;tempCount++) {
		StockDataBean beanObj = stockData.get(tempCount);
		Day dayObj = new Day(beanObj.getDateObj().get(Calendar.DATE),
		          beanObj.getDateObj().get(Calendar.MONTH) + 1, 
		          beanObj.getDateObj().get(Calendar.YEAR));
		priceSeries.add(dayObj,beanObj.getClose());
		volumeSeries.add(new TimeSeriesDataItem(dayObj,beanObj.getVolume()));
		buyPositionSeries.clear();
	}
	for(int tempCount=0; tempCount < positionData.size();tempCount++) {
		PositionBean beanObj = positionData.get(tempCount);
		if(beanObj.getTxnType().equals("B")){
			Calendar calObj = Calendar.getInstance();
			calObj.setTime(beanObj.getTxnDate());
			Day dayObj = new Day(calObj.get(Calendar.DATE),
			                     calObj.get(Calendar.MONTH) + 1, 
			                     calObj.get(Calendar.YEAR));
			buyPositionSeries.addOrUpdate(dayObj,beanObj.getPrice());
		}
	}
	for(int tempCount=0; tempCount < positionData.size();tempCount++) {
		PositionBean beanObj = positionData.get(tempCount);
		if(beanObj.getTxnType().equals("S")){
			Calendar calObj = Calendar.getInstance();
			calObj.setTime(beanObj.getTxnDate());
			Day dayObj = new Day(calObj.get(Calendar.DATE),
			                     calObj.get(Calendar.MONTH) + 1, 
			                     calObj.get(Calendar.YEAR));
			sellPositionSeries.add(dayObj,beanObj.getPrice());
		}
	}
	try {
		List<AlertBean> alertList = DataAccess.INSTANCE.getAlertList(selectedStock);
		for(int tempCount=0; tempCount < alertList.size();tempCount++) {
			AlertBean beanObj = alertList.get(tempCount);
			XYRangeValueAnnotation rva = new XYRangeValueAnnotation();
	        rva.setValue(beanObj.getAlertPrice());
	        rva.setLabel("Anno Test");
	        Font f = new Font("Tahoma",1,12);
	        rva.setLabelFont(f);
	        rva.setStroke(new BasicStroke(3.0f));
	        if(beanObj.getAlertType().equals("BUY")) {
	        		rva.setPaint(Color.GREEN);
	        }
	        else if (beanObj.getAlertType().equals("SELL")) {
	        		rva.setPaint(Color.RED);
	        }
	        else if (beanObj.getAlertType().equals("NEUTRAL")) {
        			rva.setPaint(Color.YELLOW);
	        }
	        rva.setLabelTextAnchor(TextAnchor.CENTER_LEFT);
	        rva.setLabelAnchor(org.jfree.ui.RectangleAnchor.LEFT);
	        rva.setToolTipText(beanObj.getComments());
	        priceSubplot.addAnnotation(rva);

			
//			final Marker alertMarker = new ValueMarker(beanObj.getAlertPrice());
//			alertMarker.setPaint(Color.green);
//			
//			alertMarker.setLabel("Bid Start Price");
//			alertMarker.setLabelAnchor(RectangleAnchor.BOTTOM_RIGHT);
//			alertMarker.setLabelTextAnchor(TextAnchor.TOP_RIGHT);
//			priceSubplot.addRangeMarker(alertMarker);
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

private XYLineAndShapeRenderer getPricePlotRenderer() {
	XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
    renderer.setSeriesLinesVisible(0, true);     //price data is displayed as a line
    renderer.setSeriesLinesVisible(1, false);     // earnings data is displayed as a line 
    renderer.setSeriesLinesVisible(2, false);   // position data is displayed as a dot
    renderer.setSeriesShapesVisible(0, false);
    renderer.setSeriesShapesVisible(1, true);
    renderer.setSeriesShapesVisible(2, true);
    renderer.setSeriesFillPaint(1, ChartColor.LIGHT_GREEN,true);
    renderer.setSeriesFillPaint(2, ChartColor.LIGHT_GREEN,true);
    renderer.setSeriesPaint(1, ChartColor.LIGHT_GREEN, true);
    renderer.setSeriesPaint(2, ChartColor.LIGHT_MAGENTA, true);
    renderer.setSeriesShapesVisible(2, true);
    renderer.setDefaultToolTipGenerator(new PriceToolTipGenerator());
    renderer.setDefaultEntityRadius(6);
    return renderer;
}

private JFreeChart createMainChart() {
	NumberAxis priceAxis = new NumberAxis("Price");
	priceAxis.setAutoRangeIncludesZero(false);
	XYLineAndShapeRenderer renderer =getPricePlotRenderer();
	// Create price subplot
	TimeSeriesCollection priceDataSet = new TimeSeriesCollection();
	priceSeries = new TimeSeries("Price");
	buyPositionSeries = new TimeSeries ("Buy");
	sellPositionSeries = new TimeSeries ("Sell");
	priceDataSet.addSeries(priceSeries);
	priceDataSet.addSeries(buyPositionSeries);
	priceDataSet.addSeries(sellPositionSeries);
	priceSubplot = new XYPlot(priceDataSet , null, priceAxis, renderer);
	priceSubplot.setBackgroundPaint(Color.black);
	priceSubplot.setDomainPannable(true);
	priceSubplot.setRangePannable(true);
	priceSubplot.setInsets(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
	priceSubplot.setBackgroundPaint(Color.BLACK);
	priceSubplot.setDomainGridlinePaint(Color.white);
	priceSubplot.setRangeGridlinePaint(Color.white);
	priceSubplot.setDomainCrosshairVisible(true);
	priceSubplot.setRangeCrosshairVisible(true);
	priceSubplot.setRenderer(renderer);
	
	//AXIS 2
    NumberAxis axis2 = new NumberAxis("Earnings");
    axis2.setAutoRangeIncludesZero(false);
    priceSubplot.setRangeAxis(1, axis2);
    priceSubplot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_LEFT);
    
    priceSubplot.setDataset(1, getEarningsDataSet());
    priceSubplot.mapDatasetToRangeAxis(1, 1);
    XYItemRenderer renderer2 = new StandardXYItemRenderer();
    priceSubplot.setRenderer(1, renderer2);
 
    
    

	
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
	mainPlot.add(priceSubplot, 3);
	mainPlot.add(volumeSubplot, 1);
	mainPlot.setOrientation(PlotOrientation.VERTICAL);
	

	JFreeChart chart = new JFreeChart(null, JFreeChart.DEFAULT_TITLE_FONT, mainPlot, true);
	chart.removeLegend();
	return chart;

}

private  void initializeMainChart() {
    CrosshairOverlay crosshairOverlay = new CrosshairOverlay();
    this.xCrosshair = new Crosshair(Double.NaN, Color.WHITE, new BasicStroke(0.5f));
    this.xCrosshair.setLabelVisible(true);
    this.xCrosshair.setLabelBackgroundPaint(Color.WHITE);
    this.xCrosshair.setLabelGenerator(new CrosshairLabelGenerator () {
        @Override
        public String generateLabel(Crosshair crshr) {
            String converted = new SimpleDateFormat("dd-MMM-YYYY").format(crshr.getValue());   
            return converted;
        }
    
    });
    crosshairOverlay.addDomainCrosshair(xCrosshair);
    this.yCrosshairs = new Crosshair[1];
    
   
    for (int i = 0; i < 1; i++) {
        this.yCrosshairs[i] = new Crosshair(Double.NaN, Color.WHITE, new BasicStroke(0.5f));
        this.yCrosshairs[i].setLabelVisible(true);
        if (i % 2 != 0) {
        		this.yCrosshairs[i].setLabelAnchor(RectangleAnchor.TOP_RIGHT);
        }        
        crosshairOverlay.addRangeCrosshair(yCrosshairs[i]);
    }
    this.yCrosshairs[0].setLabelBackgroundPaint(Color.WHITE);
//    chartPanel.addOverlay(crosshairOverlay);
//    this.chartPanel.addChartMouseListener(this);
}
private  void initializeChart() {
	getMainDataSet();
	XYPlot plot = (XYPlot) mainChart.getPlot();
	plot.setDomainPannable(true);
    plot.setRangePannable(true);
	plot.setInsets(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
    plot.setBackgroundPaint(Color.BLACK);
    plot.setDomainGridlinePaint(Color.white);
    plot.setRangeGridlinePaint(Color.white);
    plot.setDomainCrosshairVisible(true);
    plot.setRangeCrosshairVisible(true);
    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
    renderer.setSeriesLinesVisible(0, true);     //price data is displayed as a line
    renderer.setSeriesLinesVisible(1, false);     // earnings data is displayed as a line 
    renderer.setSeriesLinesVisible(2, false);   // position data is displayed as a dot
    renderer.setSeriesShapesVisible(0, false);
    renderer.setSeriesShapesVisible(1, true);
    renderer.setSeriesShapesVisible(2, true);
    renderer.setSeriesFillPaint(1, ChartColor.LIGHT_GREEN,true);
    renderer.setSeriesFillPaint(2, ChartColor.LIGHT_GREEN,true);
    renderer.setSeriesPaint(1, ChartColor.LIGHT_GREEN, true);
    renderer.setSeriesPaint(2, ChartColor.LIGHT_MAGENTA, true);
    renderer.setSeriesShapesVisible(2, true);
    renderer.setDefaultToolTipGenerator(new StandardXYToolTipGenerator());
    renderer.setDefaultEntityRadius(6);
    plot.setRenderer(renderer);
//    XYItemRenderer r = plot.getRenderer();
//    if (r instanceof XYLineAndShapeRenderer) {
//        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
//        renderer.setDefaultShapesVisible(false);
//        renderer.setDefaultShapesFilled(false);
//    }
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
    this.xCrosshair = new Crosshair(Double.NaN, Color.WHITE, new BasicStroke(0.5f));
    this.xCrosshair.setLabelVisible(true);
    this.xCrosshair.setLabelBackgroundPaint(Color.WHITE);
    this.xCrosshair.setLabelGenerator(new CrosshairLabelGenerator () {
        @Override
        public String generateLabel(Crosshair crshr) {
            String converted = new SimpleDateFormat("dd-MMM-YYYY").format(crshr.getValue());   
            return converted;
        }
    
    });
    crosshairOverlay.addDomainCrosshair(xCrosshair);
    this.yCrosshairs = new Crosshair[SERIES_COUNT];
    
    
    for (int i = 0; i < SERIES_COUNT; i++) {
        this.yCrosshairs[i] = new Crosshair(Double.NaN, Color.WHITE, new BasicStroke(0.5f));
        this.yCrosshairs[i].setLabelVisible(true);
        if (i % 2 != 0) {
            this.yCrosshairs[i].setLabelAnchor(RectangleAnchor.TOP_RIGHT);
        }
        crosshairOverlay.addRangeCrosshair(yCrosshairs[i]);
    }
    this.yCrosshairs[0].setLabelBackgroundPaint(Color.WHITE);
    chartPanel.addOverlay(crosshairOverlay);
    this.chartPanel.addChartMouseListener(this);
}

private TimeSeries getBuyPositionSeries(){
	TimeSeries positionSeries = new TimeSeries("Positions Buy");
	for(int tempCount=0; tempCount < positionData.size();tempCount++) {
		PositionBean beanObj = positionData.get(tempCount);
		if(beanObj.getTxnType().equals("B")){
			Calendar calObj = Calendar.getInstance();
			calObj.setTime(beanObj.getTxnDate());
			Day dayObj = new Day(calObj.get(Calendar.DATE),
			                     calObj.get(Calendar.MONTH) + 1, 
			                     calObj.get(Calendar.YEAR));
			positionSeries.addOrUpdate(dayObj,beanObj.getPrice());
		}
		
	}
return positionSeries;
	
}

private TimeSeries getSellPositionSeries(){
	TimeSeries positionSeries = new TimeSeries("Positions Sell");
	for(int tempCount=0; tempCount < positionData.size();tempCount++) {
		PositionBean beanObj = positionData.get(tempCount);
		if(beanObj.getTxnType().equals("S")){
			Calendar calObj = Calendar.getInstance();
			calObj.setTime(beanObj.getTxnDate());
			Day dayObj = new Day(calObj.get(Calendar.DATE),
			                     calObj.get(Calendar.MONTH) + 1, 
			                     calObj.get(Calendar.YEAR));
			positionSeries.add(dayObj,beanObj.getPrice());
		}
	}
return positionSeries;
	
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
	dataSet.addSeries(getBuyPositionSeries());
	dataSet.addSeries(getSellPositionSeries());
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
    CombinedDomainXYPlot plot = (CombinedDomainXYPlot) chart.getPlot();
    ValueAxis xAxis = plot.getDomainAxis();
    XYPlot pricePlot = (XYPlot) plot.getSubplots().get(0);
    double x = xAxis.java2DToValue(event.getTrigger().getX(), dataArea, 
            RectangleEdge.BOTTOM);
    Calendar calObj = Utils.getxAxisCalendarValue(x);  
    
    double y = DatasetUtils.findYValue(pricePlot.getDataset(0), 0, x);
    StockDataBean beanObj = Utils.getStockDataAsOfDate(stockData, calObj, selectedStock);
    
    if(a!= null) {
    		pricePlot.removeAnnotation(a);
    }
    if(beanObj!= null) {
    		a = new XYTextAnnotation(beanObj.getClose()+"", x, beanObj.getClose());
    		a.setPaint(Color.WHITE);
    	    pricePlot.addAnnotation(a);
    }
    //a.setFont(a.getFont().deriveFont(24f));

    //this.yCrosshairs[1].setValue(DatasetUtils.findYValue(plot.getDataset(1), 0, x));
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
