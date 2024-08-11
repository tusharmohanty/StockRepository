package stocks.model.stats;

public interface StatsSql {
public static final String SMA_SQL = 
          "select stock_code,"
		+ "txn_date, "
		+ "round((sum12/(decode(sign(count-12),-1,count,12))),2)  as sma16, "
		+ "round((sum26/(decode(sign(count-26),-1,count,26))),2) as sma26   "
		+ "from (select s.*, "  
		+ "               (select sum(close) from " 
		+ "               (select close from stock_data hist1 where trunc(hist1.txn_date) <= s.txn_date and s.stock_code = hist1.stock_code  order by hist1.txn_date desc) where rownum <= 12) as sum12, "  
		+ "               (select sum(close) from "  
		+ "               (select close from stock_data hist1 where trunc(hist1.txn_date) <= s.txn_date and s.stock_code = hist1.stock_code  order by hist1.txn_date desc) where rownum <= 26) as sum26, "  
		+ "               (select decode(count(*),0,1,count(*)+1) from stock_data hist where hist.txn_date < s.txn_date and hist.stock_code = s.stock_code)as count " 
		+ "               from stock_data s where s.stock_code = ? order by s.TXN_DATE asc) " 
		+ " where stock_code = ? and trunc(txn_date) = trunc(?)";




public static final String INSERT_SMA = "INSERT INTO STATS COLUMNS(STOCK_CODE,TXN_DATE,SMA_16,SMA_26) VALUES(?,?,?,?)";
public static final String UPDATE_EMA = "UPDATE STATS set EMA_16 =?, EMA_26 =? , MACD = ? where stock_code = ? and txn_date = ?";
public static final String EMA_SQL =
        "select sts.stock_code, "
	  + "sts.txn_date,st.close, "
	  + "sts.previous_ema_16, "
	  + "round(((st.close*2/17) + (sts.previous_ema_16 * (1-(2/17)))),2) as calculated_ema_16 , "
	  + "round(((st.close*2/27) + (sts.previous_ema_26 * (1-(2/27)))),2) as calculated_ema_26 from (" 
	  + " select s.* ," 
	  + "        nvl((select s1.ema_16 from stats s1 where s1.stock_code = s.stock_code and trunc(s1.txn_date)= trunc((select max(s2.txn_date) from stats s2 where s2.stock_code = s.stock_code and trunc(s2.txn_date) < trunc (s.txn_date)))),s.sma_16) as previous_ema_16, " 
	  + "        nvl((select s1.ema_26 from stats s1 where s1.stock_code = s.stock_code and trunc(s1.txn_date)= trunc((select max(s2.txn_date) from stats s2 where s2.stock_code = s.stock_code and trunc(s2.txn_date) < trunc (s.txn_date)))),s.sma_26) as previous_ema_26, " 
	  + "        (select decode(count(*),0,1,count(*)+1) from stock_data hist where hist.txn_date < s.txn_date and hist.stock_code = s.stock_code)as count " 
	  + " from stats s where s.stock_code = ? order by s.txn_date) sts, stock_data st " 
	  + " where st.stock_code = sts.stock_code " 
	  + " and trunc(st.txn_date) = trunc(sts.txn_date) "
	  + " and st.stock_code= ? "
	  + " and trunc(st.txn_date) = trunc(?)";



public static final String MACD_SIGNAL = "update stats s3 set s3.macd_9 = (select round((macd*0.2 +  previous_macd *0.8 ),2) as macd_9 from (select s.* , " + 
		" nvl((select s1.macd from stats s1 where s1.stock_code = s.stock_code and trunc(s1.txn_date)= trunc((select max(s2.txn_date) from stats s2 where s2.stock_code = s.stock_code and trunc(s2.txn_date) < trunc (s.txn_date)))),s.macd) as previous_macd " + 
		" from stats s where s.STOCK_CODE = ? and trunc(s.txn_date) = trunc(?)) ) where s3.stock_code = ? and trunc (s3.txn_date) = trunc(?)"   ;          
}
