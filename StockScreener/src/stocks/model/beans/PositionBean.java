package stocks.model.beans;

import java.util.Date;

public class PositionBean {
private String stockCode,txnType;
public String getTxnType() {
	return txnType;
}
public void setTxnType(String txnType) {
	this.txnType = txnType;
}
private Date   txnDate;
private Double price;
private int qty;
public int getQty() {
	return qty;
}
public void setQty(int qty) {
	this.qty = qty;
}
public String getStockCode() {
	return stockCode;
}
public void setStockCode(String stockCode) {
	this.stockCode = stockCode;
}
public Date getTxnDate() {
	return txnDate;
}
public void setTxnDate(Date positionDate) {
	this.txnDate = positionDate;
}
public Double getPrice() {
	return price;
}
public void setPrice(Double price) {
	this.price = price;
}
}
