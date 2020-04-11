package stocks.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import stocks.model.beans.StockDataBean;

public class Utils {

	
	public static StockCalendar getLastYearDate() {
		StockCalendar currentDate = new StockCalendar();
		int lastYear = currentDate.get(Calendar.YEAR) -1;
		StockCalendar oldDate = new StockCalendar();
		oldDate.set(Calendar.YEAR, lastYear);
		return oldDate;
	}
	public static String calendarToString(Calendar calendarObj) {
		
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		return   format1.format(calendarObj.getTime());
	}
	
	public static Date getxAxisDateValue(Double xCoordinate) {
		return new java.util.Date(xCoordinate.longValue());
	}
	public static Calendar getxAxisCalendarValue(Double xCoordinate) {
		Calendar calObj = new GregorianCalendar();
		calObj.setTime(getxAxisDateValue(xCoordinate));
		return calObj;
	}
	public static String getStringfromDate(Date dateObj) {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		return   format1.format(dateObj);
	}
	
	public static StockDataBean getStockDataAsOfDate(List<StockDataBean> stockData, Calendar calObj, String stockCode) {
		StockDataBean returnObj = null;
		HashSet<StockDataBean> set = new HashSet<StockDataBean>(stockData);
		for(StockDataBean pr:stockData){
			boolean isSameDay = (pr.getDateObj().get(Calendar.DAY_OF_YEAR)   == calObj.get(Calendar.DAY_OF_YEAR) +1) &&
                    (pr.getDateObj().get(Calendar.YEAR) == calObj.get(Calendar.YEAR));
            if(pr.getStockCode().equals(stockCode) && isSameDay) {
            	   returnObj= pr;
            }
        }
		return returnObj;
	}
}
