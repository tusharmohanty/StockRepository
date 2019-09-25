package stocks.model.network;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import stocks.model.StockConstants;

public class NetworkUtils {
public String getBhavUrl(java.util.Date dateObj) {
    return StockConstants.BHAV_COPY_URL+ getBhavCopyFile(dateObj);
}

public static String getBhavCopyFile (java.util.Date dateObj) {
	String returnValue = "";
	DateFormat dateFormat = new SimpleDateFormat("ddMMyy");
	String strDate = dateFormat.format(dateObj);
    returnValue = "PR" + strDate  +".zip";
    return returnValue;
}

public static String getUnzippedBhavCopy(java.util.Date dateObj) {
	String returnValue = "";
	DateFormat dateFormat = new SimpleDateFormat("ddMMyy");
	String strDate = dateFormat.format(dateObj);
    returnValue = "Pd" + strDate  +".csv";
    return returnValue;
}
}
