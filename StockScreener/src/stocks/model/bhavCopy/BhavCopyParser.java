package stocks.model.bhavCopy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import stocks.model.StockConstants;
import stocks.model.beans.StockDataBean;
import stocks.model.network.Downloader;
import stocks.model.network.NetworkUtils;

public class BhavCopyParser {

	
	public List<StockDataBean> parseBhavCopy(List<String> stockList, Calendar dateObj, String exchange) throws IOException {
		String unzippedBhavCopyFile ="";
		if (exchange.equals(StockConstants.NSE_EXCHANGE)) {
			unzippedBhavCopyFile = StockConstants.STOCK_SCREENER_HOME +  File.separator +StockConstants.BHAV_COPY_FOLDER + File.separator +  NetworkUtils.getUnzippedBhavCopy(new java.util.Date(dateObj.getTimeInMillis()), exchange);
		}
		else if (exchange.equals(StockConstants.BSE_EXCHANGE)){
			unzippedBhavCopyFile = StockConstants.STOCK_SCREENER_HOME + File.separator + exchange + File.separator + StockConstants.BHAV_COPY_FOLDER + File.separator +  NetworkUtils.getUnzippedBhavCopy(new java.util.Date(dateObj.getTimeInMillis()),exchange);	
		}
		BufferedReader csvReader = new BufferedReader(new FileReader(unzippedBhavCopyFile));
		String row;
		List<StockDataBean> returnCollection = new ArrayList<StockDataBean>();
		//while parsing skip first row
		boolean firstRow = true;
		StockDataBean beanObj= null;
		while ((row = csvReader.readLine()) != null) {
		    String[] data = row.split(",");
		    if (exchange.equals(StockConstants.NSE_EXCHANGE)) {
		    		beanObj = parseNseData(stockList,data,dateObj);
		    		if(beanObj != null) {
		    			returnCollection.add(beanObj);
		    		}
		    }
		    else if (exchange.equals(StockConstants.BSE_EXCHANGE)){
		    		if(firstRow) {
		    			firstRow = false;
		    			continue;
		    		}
		    		beanObj = parseBseData(stockList,data,dateObj);
		    		if(beanObj != null) {
		    			returnCollection.add(beanObj);
		    		}
		    }
		    
		}
		csvReader.close();
		return returnCollection;
	}
	
	private StockDataBean parseNseData(List<String> stockList, String[] data,Calendar dateObj) {
		  StockDataBean beanObj= null;
		if(data[1].equals("EQ")) {  // first csv value is EQ 
	    	   // System.out.println(data[2]);
	    	    if(stockList.contains(data[0])) {
	    	    	   beanObj = new StockDataBean();
	    	    	   beanObj.setStockCode(data[0]);
	    	    	   beanObj.setDateObj(dateObj);
	    	    	   beanObj.setOpen(Double.parseDouble(data[2]));
	    	       beanObj.setHigh(Double.parseDouble(data[3]));
	    	       beanObj.setLow(Double.parseDouble(data[4]));
	    	    	   beanObj.setClose(Double.parseDouble(data[5]));
	    	    	   beanObj.setVolume(Long.parseLong(data[11]));
	    	    }
	    }
	return beanObj;
	}
	private StockDataBean parseBseData(List<String> stockList, String[] data,Calendar dateObj) {
		  StockDataBean beanObj = null;
		  
	    	    if(stockList.contains(data[0])) {
	    	    	   beanObj = new StockDataBean();
	    	    	   beanObj.setStockCode(data[0]);
	    	    	   beanObj.setDateObj(dateObj);
	    	    	   beanObj.setOpen(Double.parseDouble(data[4]));
	    	       beanObj.setHigh(Double.parseDouble(data[5]));
	    	       beanObj.setLow(Double.parseDouble(data[6]));
	    	    	   beanObj.setClose(Double.parseDouble(data[7]));
	    	    	   beanObj.setVolume(Long.parseLong(data[10]));
	    	    }
	return beanObj;
	}
	
	public static void main (String args[]) throws IOException {
		Calendar currentDate = new GregorianCalendar();
		int lastYear = currentDate.get(Calendar.YEAR) -1;
		Calendar oldDate = new GregorianCalendar();
		oldDate.set(Calendar.YEAR, lastYear);
		BhavCopyParser parserObj = new BhavCopyParser();
		parserObj.parseBhavCopy(new ArrayList<String>(), oldDate,"BSE");
	}
}
