package stocks.util;

import java.util.GregorianCalendar;
import org.apache.commons.lang3.time.DateUtils;
public class StockCalendar extends GregorianCalendar{
public boolean equals(Object a) {
	if(a == null) {
		return false;
	}
	 if (!StockCalendar.class.isAssignableFrom(a.getClass())) {
         return false;
     }
	 final StockCalendar other = (StockCalendar) a;
	 
     return DateUtils.isSameDay(this, other);
}
}
