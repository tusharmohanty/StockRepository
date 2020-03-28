package stocks.model.stats;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import stocks.model.beans.StockDataBean;
import stocks.model.data.DBTxn;
import stocks.util.StockCalendar;
import stocks.util.Utils;

public class MA {
public void generateMA(String stockCode, Calendar dateObj) throws SQLException {
	Connection conn= DBTxn.INSTANCE.DS.getConnection();
	PreparedStatement stmt = null,stmt1 = null, stmt3 = null, stmt4= null, stmt5= null;
	ResultSet rs = null, rs2= null; 
    double sma16 =0d, sma26=0d,ema16=0d,ema26=0d;
	try {
		stmt = conn.prepareStatement(StatsSql.SMA_SQL);
		//System.out.println(StatsSql.SMA_SQL);
		stmt.setString(1, stockCode);
		stmt.setString(2, stockCode);
		stmt.setDate(3, new Date(dateObj.getTimeInMillis()));
		rs = stmt.executeQuery();
		while(rs.next()) {
			sma16 = rs.getDouble("sma16");
			sma26= rs.getDouble("sma26");
		}
		stmt1 = conn.prepareStatement(StatsSql.INSERT_SMA);
		stmt1.setString(1, stockCode);
		stmt1.setDate(2, new Date(dateObj.getTimeInMillis()));
		stmt1.setDouble(3, sma16);
		stmt1.setDouble(4, sma26);
		stmt1.executeUpdate();
		// Now calculate EMAs
		stmt3 = conn.prepareStatement(StatsSql.EMA_SQL);
		System.out.println(StatsSql.EMA_SQL);
		stmt3.setString(1, stockCode);
		stmt3.setString(2, stockCode);
		stmt3.setDate(3, new Date(dateObj.getTimeInMillis()));
		rs2 = stmt3.executeQuery();
		while(rs2.next()) {
			ema16 = rs2.getDouble("calculated_ema_16");
			ema26= rs2.getDouble("calculated_ema_26");
			System.out.println(ema16 + "=========" + ema26);
		}
		stmt4 = conn.prepareStatement(StatsSql.UPDATE_EMA);
		stmt4.setDouble(1, ema16);
		stmt4.setDouble(2, ema26);
		stmt4.setDouble(3, ema16-ema26);
		stmt4.setString(4, stockCode);
		stmt4.setDate(5, new Date(dateObj.getTimeInMillis()));
		stmt4.executeUpdate();
		
		stmt5 = conn.prepareStatement(StatsSql.MACD_SIGNAL);
		stmt5.setString(1, stockCode);
		stmt5.setDate(2, new Date(dateObj.getTimeInMillis()));
		stmt5.setString(3, stockCode);
		stmt5.setDate(4, new Date(dateObj.getTimeInMillis()));
		stmt5.executeUpdate();
		
	
	}
	finally {
		if(rs != null) {
			rs.close();
		}
		if(rs2 != null) {
			rs2.close();
		}
		if(stmt != null) {
			stmt.close();
		}
		if(stmt1 != null) {
			stmt1.close();
		}
		if(stmt3 != null) {
			stmt3.close();
		}
		if(stmt4 != null) {
			stmt4.close();
		}
		if(stmt5 != null) {
			stmt5.close();
		}
		conn.close();
	}
}
public static void main (String args[]) throws SQLException {
	MA statsObj = new MA();
	statsObj.generateMA("TCS",new GregorianCalendar(2019,03,18));
}
}
