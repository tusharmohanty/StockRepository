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
	PreparedStatement stmt = null,stmt1 = null;
	ResultSet rs = null; 
    double sma16, sma26;
	try {
		stmt = conn.prepareStatement(StatsSql.SMA_SQL);
		System.out.println(StatsSql.SMA_SQL);
		stmt.setString(1, stockCode);
		stmt.setString(2, stockCode);
		stmt.setDate(3, new Date(dateObj.getTimeInMillis()));
		rs = stmt.executeQuery();
		while(rs.next()) {
			sma16 = rs.getDouble("sma16");
			sma26= rs.getDouble("sma26");
			stmt1 = conn.prepareStatement(StatsSql.INSERT_SMA);
			stmt1.setString(1, stockCode);
			stmt1.setDate(2, new Date(dateObj.getTimeInMillis()));
			stmt1.setDouble(3, sma16);
			stmt1.setDouble(4, sma26);
			stmt1.executeUpdate();
		}
		
	}
	finally {
		if(rs != null) {
			rs.close();
		}
		if(stmt != null) {
			stmt.close();
		}
		if(stmt1 != null) {
			stmt1.close();
		}
		conn.close();
	}
}
public static void main (String args[]) throws SQLException {
	MA statsObj = new MA();
	statsObj.generateMA("TCS",new GregorianCalendar(2019,03,18));
}
}
