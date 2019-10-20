package stocks.model.beans;

public class StocksBean {
String stockCode,stockName, exchange;

public String getExchange() {
	return exchange;
}

public void setExchange(String exchange) {
	this.exchange = exchange;
}

public String getStockCode() {
	return stockCode;
}

public void setStockCode(String stockCode) {
	this.stockCode = stockCode;
}

public String getStockName() {
	return stockName;
}

public void setStockName(String stockName) {
	this.stockName = stockName;
}

public String toString() {
	return this.stockName;
}
}
