package stocks.model.beans;

import java.util.Calendar;

public class StockDataBean {
   private String stockCode;
   private Calendar dateObj;
   private double  open, close,high,low;

private long volume;
public String getStockCode() {
	return stockCode;
}
public void setStockCode(String stockCode) {
	this.stockCode = stockCode;
}
public Calendar getDateObj() {
	return dateObj;
}
public void setDateObj(Calendar dateObj) {
	this.dateObj = dateObj;
}
public double getOpen() {
	return open;
}
public void setOpen(double open) {
	this.open = open;
}
public double getClose() {
	return close;
}
public void setClose(double close) {
	this.close = close;
}
public double getHigh() {
	return high;
}
public void setHigh(double high) {
	this.high = high;
}
public double getLow() {
	return low;
}
public void setLow(double low) {
	this.low = low;
}
public long getVolume() {
	return volume;
}
public void setVolume(long volume) {
	this.volume = volume;
}

@Override
public boolean equals(Object obj) { 
if(this == obj) {
        return true; 
}
else if(obj == null || obj.getClass()!= this.getClass()) {
       return false; 
}  
StockDataBean stockData = (StockDataBean) obj; 
    boolean  isSameDay = (stockData.dateObj.get(Calendar.DAY_OF_YEAR)  == this.dateObj.get(Calendar.DAY_OF_YEAR) ) &&
                         (stockData.dateObj.get(Calendar.YEAR) == this.dateObj.get(Calendar.YEAR));
    return (stockData.stockCode == this.stockCode && isSameDay); 
} 
  
@Override
public int hashCode() { 
          return this.stockCode.hashCode() + new Integer(this.dateObj.get(Calendar.DAY_OF_YEAR)).hashCode() +  new Integer(this.dateObj.get(Calendar.YEAR)).hashCode(); 
} 

public String toString() {
	return "Code  :" + this.getStockCode() + "\n" +
           "Open  :" +  this .getOpen()    + "\n" +
		   "Close :" +  this.getClose()   +  "\n" +
           "High  :" +  this.getHigh()    +  "\n" +
		   "Low   :" +  this.getLow()     +  "\n" +
           "Volume:" +  this.getVolume()  +  "\n" ;
}
   
}
