package stocks.model.data;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import stocks.model.data.DBTxn;
import stocks.model.network.Downloader;
import stocks.util.StockCalendar;
import stocks.util.Utils;
import stocks.model.beans.StockDataBean;
import stocks.model.beans.StocksBean;
import stocks.model.bhavCopy.BhavCopyParser;
public class SyncData {



/***
 * Method return the list of dates on which data exists for a given stock code
 * */


private List<StockCalendar> dataExists(String stockCode) throws SQLException{
	Connection conn= DBTxn.INSTANCE.DS.getConnection();
	PreparedStatement stmt = null;
	ResultSet rs = null;
	List<StockCalendar> returnObj= new ArrayList<StockCalendar>();
	try {
		stmt = conn.prepareStatement("Select txn_date from stock_data where stock_code = ? "
				+ "  UNION select holiday from holiday_list");
		stmt.setString(1, stockCode);
		rs = stmt.executeQuery();
		while(rs.next()) {
			Date dateObj =rs.getDate(1);
			StockCalendar calObj = new StockCalendar();
			calObj.setTime(dateObj);
			returnObj.add(calObj);
		}
	}
	finally {
		rs.close();
		stmt.close();
		conn.close();
	}
	return returnObj;
}


/**
 * Method returns a Hashmap containing a calendarObj as a key and List of StockBeans as values 
 * This is the master data that needs to be synced
 * @throws SQLException 
 * **/
//for a given stock find the dates on which data needs to be synced
//  and then return a Hashmap that contains a date Obj as key and List of StockBean as values 
// ignore saturday and Sunday from the list



private Map<StockCalendar,List<String>> getTargetSyncData(List <StocksBean> stockList) throws SQLException {
	Map<StockCalendar,List<String>> returnMap = new Hashtable<StockCalendar,List<String>>();
	StockCalendar oldDate = Utils.getLastYearDate();                   //initialize date to 1 year back
	StockCalendar currentDate = new StockCalendar();
	for(int tempCount =0; tempCount < stockList.size(); tempCount++) {   // iterate over each stock list
		String stockCode = stockList.get(tempCount).getStockCode();
		List<StockCalendar> dataPresent = dataExists(stockCode);              // get the list of dates when data is not present
		while (oldDate.before(currentDate)) {                            // start counting from 1 year back
			if(!dataPresent.contains(oldDate)) {                         // if data is not present for the given date
				if(!returnMap.containsKey(oldDate)) {                    // if this is a new entry , create a new list and add current stock to it 
					List<String> valueObj =  new ArrayList<String>();
					valueObj.add(stockCode);
					returnMap.put((StockCalendar)oldDate.clone(),valueObj);
				}
				else {                                                    // if an entry exists , get the entry and add current stock to it
					List<String> valueObj = returnMap.get(oldDate);
					valueObj.add(stockCode);
					returnMap.put((StockCalendar)oldDate.clone(),valueObj);
				}
			}
			oldDate.add(Calendar.DATE, 1);
		}
		oldDate = Utils.getLastYearDate();   
	}
	return returnMap;
}


private void persistData(List<StockDataBean> dataObj) throws SQLException {
	Connection conn= DBTxn.INSTANCE.DS.getConnection();
	PreparedStatement stmt = null;
	try {
		for(int tempCount =0; tempCount < dataObj.size();tempCount++) {
			StockDataBean data = dataObj.get(tempCount);
			stmt = conn.prepareStatement("insert into  stock_data(txn_date,stock_code,open,close,high,low,volume) values (?,?,?,?,?,?,?)");
			stmt.setDate(1, new Date(data.getDateObj().getTimeInMillis()));
			stmt.setString(2, data.getStockCode());
			stmt.setDouble(3, data.getOpen());
			stmt.setDouble(4, data.getClose());
			stmt.setDouble(5, data.getHigh());
			stmt.setDouble(6, data.getLow());
			stmt.setLong(7, data.getVolume());
			stmt.executeUpdate();
		}
		//conn.commit();
		
	}
	finally {
		stmt.close();
		conn.close();
	}
}

public void syncData() throws SQLException, IOException {
	List <StocksBean> stockList = DataAccess.INSTANCE.getStockList();
	Map<StockCalendar,List<String>> masterSyncList =  getTargetSyncData(stockList);
	Downloader downloaderObj = new Downloader();
	BhavCopyParser parserObj = new BhavCopyParser();
	for(Map.Entry<StockCalendar,List<String>> entry :masterSyncList.entrySet()) {
		StockCalendar dateObj = entry.getKey();
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String formatted = format1.format(dateObj.getTime());
		List<String> stockStringlist = entry.getValue();
		boolean unzipResult = downLoadAndUnzipBhavCopy(new java.util.Date(dateObj.getTimeInMillis()));
		
		if(unzipResult ) {
			List<StockDataBean> dataObj= parserObj.parseBhavCopy(stockStringlist, dateObj);
			persistData(dataObj);
		}
	}
}
// prepare list of missing stockList and Date collection
//for each date call parser
//persist parser data

private boolean downLoadAndUnzipBhavCopy(java.util.Date dateObj) throws IOException, SQLException {
	boolean returnValue = true;
	Downloader downloaderObj = new Downloader();
	String downloadedFile = downloaderObj.downloadBhavCopy(dateObj);
	if(downloadedFile != null) {
		if(!isAlreadyUnzipped(downloadedFile)) {
			String unzipResult = downloaderObj.unzipBhavCopy (downloadedFile);
		}
	}
	else {
		returnValue = false;
	}
	return returnValue;
}

private boolean isAlreadyUnzipped(String bhavCopy) throws SQLException {
	boolean returnValue = false;
	Connection conn= DBTxn.INSTANCE.DS.getConnection();
	PreparedStatement stmt = null;
	try {
		stmt = conn.prepareStatement("select 1 from  unzip_bhavcopy where bhav_copy= ? ");
		stmt.setString(1, bhavCopy);
		ResultSet rs =stmt.executeQuery();
		while(rs.next()) {
			returnValue = true;
		}
	return returnValue;
	}
	finally {
		stmt.close();
		conn.close();
	}
}


public boolean isHoliday(java.sql.Date dateObject) throws SQLException {
	boolean returnValue = false;
	Connection conn= DBTxn.INSTANCE.DS.getConnection();
	PreparedStatement stmt = null;
	try {
		stmt = conn.prepareStatement("select 1 from  holiday_list where holiday= ? ");
		stmt.setDate(1, dateObject);
		ResultSet rs =stmt.executeQuery();
		while(rs.next()) {
			returnValue = true;
		}
	return returnValue;
	
}
finally {
	stmt.close();
	conn.close();
}
}

public void registerHoliday(java.sql.Date dateObject) throws SQLException {
	Connection conn= DBTxn.INSTANCE.DS.getConnection();
	PreparedStatement stmt = null;
	try {
			stmt = conn.prepareStatement("merge  into  holiday_list a using (select holiday from holiday_list ) b on (a.holiday= b.holiday) when not matched then insert (holiday) values (?) ");
			stmt.setDate(1, dateObject);
						stmt.executeUpdate();
		   // conn.commit();
		
	}
	finally {
		stmt.close();
		conn.close();
	}
}

public void registerUnzipEvent(String zippedBhavCopyFilePath) throws SQLException {
	Connection conn= DBTxn.INSTANCE.DS.getConnection();
	PreparedStatement stmt = null;
	try {
			stmt = conn.prepareStatement("merge  into  unzip_bhavcopy a using (select bhavcopy from unzip_bhavcopy ) b on (a.bhavcopy= b.bhavcopy) when not matched then insert (bhavcopy) values (?) ");
			stmt.setString(1,zippedBhavCopyFilePath);
			stmt.executeUpdate();
		   // conn.commit();
		
	}
	finally {
		stmt.close();
		conn.close();
	}
}

public static void main(String args[]) throws SQLException, IOException {
	SyncData dataObj = new SyncData();
	dataObj.syncData();
	
}
}
