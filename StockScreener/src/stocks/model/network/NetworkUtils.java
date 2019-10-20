package stocks.model.network;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import stocks.model.StockConstants;

public class NetworkUtils {
public String getBhavUrl(java.util.Date dateObj, String exchange) {
	String returnString ="";
	if(exchange.equals(StockConstants.NSE_EXCHANGE)) {
		returnString = StockConstants.NSE_BHAV_COPY_URL+ getBhavCopyFile(dateObj, exchange);
	}
	else if (exchange.equals(StockConstants.BSE_EXCHANGE)) {
		returnString = StockConstants.BSE_BHAV_COPY_URL + getBhavCopyFile(dateObj, exchange);
	}
	return returnString;
}

public static String getBhavCopyFile (java.util.Date dateObj, String exchange) {
	String returnValue = "";
	DateFormat dateFormat = new SimpleDateFormat("ddMMyy");
	String strDate = dateFormat.format(dateObj);
	if(exchange.equals(StockConstants.NSE_EXCHANGE)) {
		returnValue = "PR" + strDate  +".zip";
	}
	else if(exchange.equals(StockConstants.BSE_EXCHANGE)) {
		returnValue = "EQ" + strDate + "_CSV.ZIP";
	}
    return returnValue;
}

public static String getUnzippedBhavCopy(java.util.Date dateObj, String exchange) {
	String returnValue = "";
	DateFormat dateFormat = new SimpleDateFormat("ddMMyy");
	String strDate = dateFormat.format(dateObj);
	if(exchange.equals(StockConstants.NSE_EXCHANGE)) {
		returnValue = "Pd" + strDate  +".csv";
	}
	else if(exchange.equals(StockConstants.BSE_EXCHANGE)) {
		returnValue = "EQ" + strDate + ".CSV";
	}
    
    return returnValue;
}
}
