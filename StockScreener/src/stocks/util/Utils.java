package stocks.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
	
	
}
