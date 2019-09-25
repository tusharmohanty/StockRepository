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
import stocks.model.beans.DailyStockData;
import stocks.model.network.Downloader;
import stocks.model.network.NetworkUtils;

public class BhavCopyParser {

	
	public void parseBhavCopy(List<String> stockList, Calendar dateObj) throws IOException {
		String unzippedBhavCopyFile = StockConstants.STOCK_SCREENER_HOME + StockConstants.BHAV_COPY_FOLDER + File.separator +  NetworkUtils.getUnzippedBhavCopy(new java.util.Date(dateObj.getTimeInMillis()));
		BufferedReader csvReader = new BufferedReader(new FileReader(unzippedBhavCopyFile));
		String row;
		while ((row = csvReader.readLine()) != null) {
		    String[] data = row.split(",");
		    if(data[0].equals("N") && data[1].equals("EQ")) {  // first csv value is N and second EQ
		    	   // System.out.println(data[2]);
		    	    if(stockList.contains(data[2])) {
		    	    	   DailyStockData beanObj = new DailyStockData();
		    	    	   beanObj.setStockCode(data[2]);
		    	    	   beanObj.setDateObj(dateObj);
		    	    	   beanObj.setOpen(Double.parseDouble(data[5]));
		    	       beanObj.setHigh(Double.parseDouble(data[6]));
		    	       beanObj.setLow(Double.parseDouble(data[7]));
		    	    	   beanObj.setClose(Double.parseDouble(data[8]));
		    	    	   beanObj.setVolume(Long.parseLong(data[10]));
		    	       System.out.println(beanObj);
		    	    }
		    }
		    // do something with the data
		}
		csvReader.close();
	}
	
	public static void main (String args[]) throws IOException {
		Calendar currentDate = new GregorianCalendar();
		int lastYear = currentDate.get(Calendar.YEAR) -1;
		Calendar oldDate = new GregorianCalendar();
		oldDate.set(Calendar.YEAR, lastYear);
		BhavCopyParser parserObj = new BhavCopyParser();
		parserObj.parseBhavCopy(new ArrayList<String>(), oldDate);
	}
}
