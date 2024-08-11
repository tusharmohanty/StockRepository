package stocks.model.network;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import stocks.model.StockConstants;

public class NetworkUtils {
public static String getBhavUrl(java.util.Date dateObj, String exchange) {
	String returnString ="";
	if(exchange.equals(StockConstants.NSE_EXCHANGE)) {
		returnString = "https://www.nseindia.com/api/reports?archives=[{\"name\":\"CM-UDiFF Common Bhavcopy Final (zip)\",\"type\":\"daily-reports\",\"category\":\"capital-market\",\"section\":\"equities\"}]&date=";
		returnString +=getNSEDateFormat(dateObj) + "&type=equities&mode=single";
		//returnString = StockConstants.NSE_BHAV_COPY_URL+ getNetworkPrefix(dateObj, exchange) + getBhavCopyFile(dateObj, exchange);
	}
	else if (exchange.equals(StockConstants.BSE_EXCHANGE)) {
		returnString = StockConstants.BSE_BHAV_COPY_URL + getBhavCopyFile(dateObj, exchange);
	}
	
	return returnString;
}

private static String getNSEDateFormat(java.util.Date dateObj){
	String pattern ="dd-MMM-YYYY";
	DateFormat df = new SimpleDateFormat(pattern);
	String dateString = df.format(dateObj);
	return dateString;
}

private static String getNetworkPrefix (java.util.Date dateObj, String exchange) {
	DateFormat yearFormat = new SimpleDateFormat("YYYY");
	DateFormat monthFormat = new SimpleDateFormat("MMM");
	DateFormat dateFormat = new SimpleDateFormat("dd");
	String year = yearFormat.format(dateObj);
	String month = monthFormat.format(dateObj).toUpperCase();
	String strDate = dateFormat.format(dateObj);
	return year + StockConstants.URLSEPERATOR +month+ StockConstants.URLSEPERATOR;
}
public static String getBhavCopyFile (java.util.Date dateObj, String exchange) {
	String returnValue = "";
	DateFormat dateFormat = new SimpleDateFormat("dd");
	
	String strDate = dateFormat.format(dateObj);
	if(exchange.equals(StockConstants.NSE_EXCHANGE)) {
		DateFormat yearFormat = new SimpleDateFormat("YYYY");
		DateFormat monthFormat = new SimpleDateFormat("MMM");
		String year = yearFormat.format(dateObj);
		String month = monthFormat.format(dateObj).toUpperCase();
		returnValue =   "cm" + strDate  + month + year+	"bhav.csv.zip";
	}
	else if(exchange.equals(StockConstants.BSE_EXCHANGE)) {
		returnValue = "EQ" + strDate + "_CSV.ZIP";
	}
    return returnValue;
}

public static String getUnzippedBhavCopy(java.util.Date dateObj, String exchange) {
	String returnValue = "";
	DateFormat dateFormat = new SimpleDateFormat("dd");
	String strDate = dateFormat.format(dateObj);
	if(exchange.equals(StockConstants.NSE_EXCHANGE)) {
		DateFormat yearFormat = new SimpleDateFormat("YYYY");
		DateFormat monthFormat = new SimpleDateFormat("MMM");
		String year = yearFormat.format(dateObj);
		String month = monthFormat.format(dateObj).toUpperCase();
		returnValue = "cm" + strDate  + month + year+	"bhav.csv";
	}
	else if(exchange.equals(StockConstants.BSE_EXCHANGE)) {
		returnValue = "EQ" + strDate + ".CSV";
	}
    
    return returnValue;
}

public static void main (String args[]) {
	System.out.println(NetworkUtils.getBhavUrl(new java.util.Date(), "NSE"));
	
	
}
}
