package stocks.model.beans;

import java.util.Calendar;

public class StockDataBean {
   private String stockCode;
   private Calendar dateObj;
   private double  open, close,high,low,pe;
   public double getPE() {
	return pe;
}
public void setPE(double pe) {
	this.pe = pe;
}

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

public String toString() {
	return "Code  :" + this.getStockCode() + "\n" +
           "Open  :" +  this .getOpen()    + "\n" +
		   "Close :" +  this.getClose()   +  "\n" +
           "High  :" +  this.getHigh()    +  "\n" +
		   "Low   :" +  this.getLow()     +  "\n" +
           "Volume:" +  this.getVolume()  +  "\n" ;
}
   
}
