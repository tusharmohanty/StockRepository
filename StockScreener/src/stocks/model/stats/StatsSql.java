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
}
