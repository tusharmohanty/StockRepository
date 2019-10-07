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

import stocks.model.beans.StockDataBean;
import stocks.model.beans.StocksBean;

public class DataAccess {
public static final DataAccess INSTANCE = new DataAccess();
private DataAccess () {
	
}
public List <StocksBean> getStockList() throws SQLException{
	List <StocksBean> stockList = new ArrayList<StocksBean>();
	Connection conn= DBTxn.INSTANCE.DS.getConnection();
	Statement stmt = null;
	ResultSet rs = null;
	try {
		stmt = conn.createStatement();
		rs = stmt.executeQuery("Select stock_code, stock_name from stocks");
		while(rs.next()) {
			StocksBean beanObj = new StocksBean();
			beanObj.setStockCode(rs.getString("stock_Code"));
			beanObj.setStockName(rs.getString("stock_name"));
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
	try {
		stmt = conn.prepareStatement("Select stock_code, txn_date, open , close, high, low , volume  from stock_data where stock_code = ?");
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
			beanObj.setClose(rs.getDouble("close"));
			beanObj.setVolume(rs.getLong("volume"));
			stockDataList.add(beanObj);
		}
	}
	finally {
		rs.close();
		stmt.close();
		conn.close();
	}
	return stockDataList;
}
}
