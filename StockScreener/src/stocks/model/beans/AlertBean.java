package stocks.model.beans;

import java.util.Date;

public class AlertBean {
private String stockCode,alertType,comments;
private double alertPrice,threshold;
private Date alertDate;
public String getStockCode() {
	return stockCode;
}
public void setStockCode(String stockCode) {
	this.stockCode = stockCode;
}
public String getAlertType() {
	return alertType;
}
public void setAlertType(String alertType) {
	this.alertType = alertType;
}
public String getComments() {
	return comments;
}
public void setComments(String comments) {
	this.comments = comments;
}
public double getAlertPrice() {
	return alertPrice;
}
public void setAlertPrice(double alertPrice) {
	this.alertPrice = alertPrice;
}
public double getThreshold() {
	return threshold;
}
public void setThreshold(double threshold) {
	this.threshold = threshold;
}
public Date getAlertDate() {
	return alertDate;
}
public void setAlertDate(Date alertDate) {
	this.alertDate = alertDate;
}


}
