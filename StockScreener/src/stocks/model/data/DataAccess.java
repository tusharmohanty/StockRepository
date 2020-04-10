package stocks.model.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import stocks.model.beans.PositionBean;
import stocks.model.beans.StockDataBean;
import stocks.model.beans.StocksBean;

public class DataAccess {
public static final DataAccess INSTANCE = new DataAccess();
public double latestValue =0;
private DataAccess () {
	
}
public List <StocksBean> getStockList() throws SQLException{
	List <StocksBean> stockList = new ArrayList<StocksBean>();
	Connection conn= DBTxn.INSTANCE.DS.getConnection();
	Statement stmt = null;
	ResultSet rs = null;
	try {
		stmt = conn.createStatement();
		rs = stmt.executeQuery("Select stock_code, stock_name, exchange from stocks");
		while(rs.next()) {
			StocksBean beanObj = new StocksBean();
			beanObj.setStockCode(rs.getString("stock_Code"));
			beanObj.setStockName(rs.getString("stock_name"));
			beanObj.setExchange(rs.getString("exchange"));
			stockList.add(beanObj);
		}
	}
	finally {
		rs.close();
		stmt.close();
		conn.close();
	}
	return stockList;
}

public List <StocksBean> getPortfolioStockList() throws SQLException{
	List <StocksBean> stockList = new ArrayList<StocksBean>();
	Connection conn= DBTxn.INSTANCE.DS.getConnection();
	Statement stmt = null;
	ResultSet rs = null;
	try {
		stmt = conn.createStatement();
		rs = stmt.executeQuery("select s.STOCK_CODE, s.stock_name, s.exchange,SUM((p.price * p.qty)) as value from stocks s , positions p where s.STOCK_CODE= p.STOCK_CODE and p.status = 'A' group by s.stock_code, s.stock_name,s.exchange order by value desc");
		while(rs.next()) {
			StocksBean beanObj = new StocksBean();
			beanObj.setStockCode(rs.getString("stock_Code"));
			beanObj.setStockName(rs.getString("stock_name"));
			beanObj.setExchange(rs.getString("exchange"));
			stockList.add(beanObj);
		}
	}
	finally {
		rs.close();
		stmt.close();
		conn.close();
	}
	return stockList;
}
public List <StocksBean> getWatchListStockList() throws SQLException{
	List <StocksBean> stockList = new ArrayList<StocksBean>();
	Connection conn= DBTxn.INSTANCE.DS.getConnection();
	Statement stmt = null;
	ResultSet rs = null;
	try {
		stmt = conn.createStatement();
		rs = stmt.executeQuery("select s.STOCK_CODE, s.stock_name, s.exchange  from stocks s where s.STOCK_CODE not in (select stock_code from positions where status = 'A' )");
		while(rs.next()) {
			StocksBean beanObj = new StocksBean();
			beanObj.setStockCode(rs.getString("stock_Code"));
			beanObj.setStockName(rs.getString("stock_name"));
			beanObj.setExchange(rs.getString("exchange"));
			stockList.add(beanObj);
		}
	}
	finally {
		rs.close();
		stmt.close();
		conn.close();
	}
	return stockList;
}


public List <StockDataBean> getStockData(String stockCode) throws SQLException{
	List <StockDataBean> stockDataList = new ArrayList<StockDataBean>();
	Connection conn= DBTxn.INSTANCE.DS.getConnection();
	PreparedStatement stmt = null;
	ResultSet rs = null;
	boolean latestValue = true;
	try {
		stmt = conn.prepareStatement("Select s.stock_code, s.txn_date, s.open , s.close, s.high, s.low , s.volume, cast(round((s.close/e.eps)/4,2) as numeric (18,2)) as pe from stock_data s, earnings e where s.stock_code = ? "
				                   + "and s.stock_code = e.stock_code(+) and s.TXN_DATE "
				                   + "between nvl(e.EFFECTIVE_START_DATE,to_date('01/01/1950','DD/MM/YYYY')) and nvl(e.EFFECTIVE_END_DATE,to_date('31/12/4712','DD/MM/YYYY')) "
				                   + "order by txn_date desc");
		stmt.setString(1, stockCode);
		rs = stmt.executeQuery();
		while(rs.next()) {
			StockDataBean beanObj = new StockDataBean();
			beanObj.setStockCode(stockCode);
			Calendar calObj = new GregorianCalendar();
			calObj.setTime(rs.getDate("txn_Date"));
			beanObj.setDateObj(calObj);
			beanObj.setOpen(rs.getDouble("open"));
			beanObj.setClose(rs.getDouble("close"));
			beanObj.setHigh(rs.getDouble("high"));
			beanObj.setLow(rs.getDouble("low"));
			beanObj.setVolume(rs.getLong("volume"));
			beanObj.setPE(rs.getDouble("pe"));
			stockDataList.add(beanObj);
			if(latestValue) {
				this.latestValue=beanObj.getClose();
				latestValue= false;
			}
		}
	}
	finally {
		rs.close();
		stmt.close();
		conn.close();
	}
	return stockDataList;
}

public List <PositionBean> getPositionData(String stockCode) throws SQLException{
	List <PositionBean> positionDataList = new ArrayList<PositionBean>();
	Connection conn= DBTxn.INSTANCE.DS.getConnection();
	PreparedStatement stmt = null;
	ResultSet rs = null;
	boolean latestValue = true;
	try {
		stmt = conn.prepareStatement("Select p.stock_code, p.txn_date, p.price ,p.qty,p.txn_type from positions p, stock_data s where s.stock_code = p.stock_code "
				                   + "and s.stock_code = ? and trunc(s.TXN_DATE) =(select trunc(max(txn_date)) from stock_data where stock_code = ?)  order by p.txn_date desc");
		stmt.setString(1, stockCode);
		stmt.setString(2, stockCode);
		rs = stmt.executeQuery();
		while(rs.next()) {
			PositionBean beanObj = new PositionBean();
			beanObj.setStockCode(stockCode);
			Calendar calObj = new GregorianCalendar();
			beanObj.setTxnDate(rs.getDate("txn_Date"));
			beanObj.setPrice(rs.getDouble("price"));
			beanObj.setQty(rs.getInt("qty"));
			beanObj.setTxnType(rs.getString("txn_type"));
			
			positionDataList.add(beanObj);
		}
	}
	finally {
		rs.close();
		stmt.close();
		conn.close();
	}
	return positionDataList;
}
}
