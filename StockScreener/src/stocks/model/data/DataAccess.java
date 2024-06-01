package stocks.model.data;

import java.io.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.*;
import java.util.*;

import stocks.model.StockConstants;
import stocks.model.beans.*;

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
		if(rs!= null) {
			rs.close();
		}
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


public List <StocksBean> getBuyStockList() throws SQLException{
	List <StocksBean> stockList = new ArrayList<StocksBean>();
	Connection conn= DBTxn.INSTANCE.DS.getConnection();
	Statement stmt = null;
	ResultSet rs = null;
	try {
		stmt = conn.createStatement();
		rs = stmt.executeQuery("select s.stock_code,s.stock_name, s.exchange ,a.stock_code,d.close,a.alert_price*(1-a.threshold/100) as lower_limit , a.alert_price*(1+a.threshold/100) as upper_limit, (d.close-(a.alert_price*(1+a.threshold/100)))/d.close as gap from stocks s, stock_alerts a, stock_data d where s.stock_code = a.stock_code and a.ALERT_TYPE = 'BUY' and a.STOCK_CODE = d.STOCK_CODE and trunc(d.txn_date) = (select max(trunc(txn_date)) from stock_data where stock_code = a.stock_code) order by gap ");
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

public List <AlertBean> getAlertList(String stockCode) throws SQLException{
	List <AlertBean> alertList = new ArrayList<AlertBean>();
	Connection conn= DBTxn.INSTANCE.DS.getConnection();
	PreparedStatement stmt = null;
	ResultSet rs = null;
	try {
		stmt = conn.prepareStatement("select a.STOCK_CODE, a.alert_type, a.threshold, a.comments,a.alert_price,a.alert_date  from stock_alerts a where a.STOCK_CODE = ?");
		stmt.setString(1, stockCode);
		rs = stmt.executeQuery();
		while(rs.next()) {
			AlertBean beanObj = new AlertBean();
			beanObj.setStockCode(rs.getString("stock_Code"));
			beanObj.setAlertType(rs.getString("alert_type"));
			beanObj.setComments(rs.getString("comments"));
			beanObj.setThreshold(rs.getDouble("threshold"));
			beanObj.setAlertPrice(rs.getDouble("alert_price"));
			beanObj.setAlertDate(rs.getDate("alert_date"));
			alertList.add(beanObj);
		}
	}
	finally {
		if(rs != null) {
			rs.close();
		}
		if(stmt!= null) {
			stmt.close();
		}
		conn.close();
	}
	return alertList;
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

public List <PositionBean> getPositionData(String stockCode, String status) throws SQLException{
	List <PositionBean> positionDataList = new ArrayList<PositionBean>();
	Connection conn= DBTxn.INSTANCE.DS.getConnection();
	PreparedStatement stmt = null;
	ResultSet rs = null;
	boolean latestValue = true;
	try {
		stmt = conn.prepareStatement("Select p.stock_code, p.txn_date, p.price ,p.qty,p.txn_type from positions p, stock_data s where s.stock_code = p.stock_code "
				                   + "and s.stock_code = ? and trunc(s.TXN_DATE) =(select trunc(max(txn_date)) from stock_data where stock_code = ?) and p.status = ? order by p.txn_date desc");
		stmt.setString(1, stockCode);
		stmt.setString(2, stockCode);
		stmt.setString(3, status);
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
		if(rs != null) {
			rs.close();
		}
		if(stmt != null) {
			stmt.close();
		}
		conn.close();
	}
	return positionDataList;
}


	public List <PortfolioBean> getPortfolioData() throws SQLException{
		List <PortfolioBean> portfolioDataList = new ArrayList<PortfolioBean>();
		Connection conn= DBTxn.INSTANCE.DS.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean latestValue = true;
		try {
			stmt = conn.prepareStatement("Select p.portfolio_id, p.stock_code, p.qty ,p.price,p.txn_date, s.close " +
					"from portfolio p, stock_data s " +
					"where p.stock_code= s.stock_code " +
					"and s.txn_date =  (select max(txn_date) from stock_data where stock_code =p.stock_code)");
			rs = stmt.executeQuery();
			while(rs.next()) {
				PortfolioBean beanObj = new PortfolioBean();
				beanObj.setPortfolioId(rs.getLong("portfolio_id"));
				beanObj.setStockCode(rs.getString("stock_code"));
				beanObj.setQty(rs.getLong("qty"));
				beanObj.setPrice(rs.getDouble("price"));
				beanObj.setTxnDate(rs.getDate("txn_date"));
				beanObj.setCurrentPrice(rs.getDouble("close"));
				beanObj.setPL();
				portfolioDataList.add(beanObj);
			}
		}
		finally {
			if(rs != null) {
				rs.close();
			}
			if(stmt != null) {
				stmt.close();
			}
			conn.close();
		}
		portfolioDataList = normalizePortfolioData(portfolioDataList);
		return portfolioDataList;
	}

	private List<PortfolioBean> normalizePortfolioData (List<PortfolioBean> portfolioBeanList){
	List <PortfolioBean> returnList = new ArrayList<>();

	List <String> portfolioStocks = new ArrayList<>();
		//for each portfolio entry , iterate through the portfolio Bean List and prepare a List of all stock codes
	for(int tempCount =0; tempCount < portfolioBeanList.size(); tempCount ++) {
		PortfolioBean beanObj = portfolioBeanList.get(tempCount);
		String stockCode = beanObj.getStockCode();
		portfolioStocks.add(stockCode);
	}
	HashSet<String> stockSet = new HashSet<>(portfolioStocks);
	// Iterate through each hashset and find the data where stock code matches
		for (String stockCode : stockSet) {
			long qty = 0L;
			BigDecimal totalInvestment = new BigDecimal(0d);
            double currentPrice =0d;
			for(int tempCount1 =0;tempCount1 < portfolioBeanList.size();tempCount1++ ){
				PortfolioBean innerBeanObj = portfolioBeanList.get(tempCount1);
				if(stockCode.equals(innerBeanObj.getStockCode())){
					qty = qty + innerBeanObj.getQty();
					totalInvestment = totalInvestment.add(new BigDecimal(innerBeanObj.getPrice() * innerBeanObj.getQty()))  ;
					currentPrice = innerBeanObj.getCurrentPrice();
				}
			}
		    PortfolioBean beanObj = new PortfolioBean();
			beanObj.setStockCode(stockCode);
			beanObj.setCurrentPrice(currentPrice);
			beanObj.setQty(qty);
			beanObj.setPrice(totalInvestment.divide( new BigDecimal(qty)).doubleValue());
			beanObj.setTotalInvestment(totalInvestment.doubleValue());
			BigDecimal actualPL =new BigDecimal(currentPrice);
			actualPL.setScale(2,RoundingMode.HALF_UP);
			actualPL = actualPL.multiply(new BigDecimal(qty),new MathContext(2)).subtract(totalInvestment);
			beanObj.setPlActual(actualPL.doubleValue());
			beanObj.setPL();
			beanObj.setTotalPortfolioValue(totalInvestment.doubleValue());
			returnList.add(beanObj);
			}

		// calculate weightage
        BigDecimal totalPortfolioValue = new BigDecimal(0d);
		for(int tempCount=0;tempCount < returnList.size();tempCount ++){
			totalPortfolioValue= totalPortfolioValue.add(new BigDecimal(returnList.get(tempCount).getTotalPortfolioValue()));
		}
		List<PortfolioBean> finalReturnList = new ArrayList<>();
		for(int tempCount=0;tempCount < returnList.size();tempCount ++){
			PortfolioBean beanObj = returnList.get(tempCount);
			BigDecimal weightage = new BigDecimal(beanObj.getTotalPortfolioValue()).divide(totalPortfolioValue,2,RoundingMode.HALF_UP);
			weightage = weightage.multiply(new BigDecimal(100));
			weightage.setScale(2,RoundingMode.HALF_UP);
			beanObj.setWeightage(weightage.doubleValue());
			finalReturnList.add(beanObj);
		}
	return finalReturnList;
	}

	public Map<String,Map<String,List<String>>> getCommentsDataTest(String stockCode){
		Map<String,List<String>> fundamentalComments = new HashMap<>();
		List<String> positiveComments = new ArrayList<>();
		positiveComments.add("20th May 2024, Q4 2023, Very good Q4,Good commentry from management");
		positiveComments.add("Endorsed by Rohit Chauhan");
		positiveComments.add("Endorsed by Sajal Kapur");
		fundamentalComments.put(StockConstants.POSITIVE,positiveComments);

		List<String> negativeComments = new ArrayList<>();
		negativeComments.add("Run up recently, valuations above avg : price to book, marketcap/sales ,ev/ebidta");

		List<String> neutralComments = new ArrayList<>();
		neutralComments.add("Run up recently, valuations above avg : price to book, marketcap/sales ,ev/ebidta");
        positiveComments = new ArrayList<>();
		positiveComments.add("Since there is a growth in margin EV/EBIDTA is perhaps important , still the stock is not very far from this mean");

		Map<String,List<String>> technicalComments = new HashMap<>();
		technicalComments.put(StockConstants.NEGATIVE,negativeComments);
		technicalComments.put(StockConstants.POSITIVE,positiveComments);
		technicalComments.put(StockConstants.NEUTRAL,neutralComments);

		Map<String,Map<String,List<String>>> commentsData = new HashMap<>();
		commentsData.put(StockConstants.FUNDAMENTAL,fundamentalComments);
		commentsData.put(StockConstants.TECHNICAL,technicalComments);
		return commentsData;
	}

	public List <CommentsBean> getCommentsData(String stockCode) throws SQLException{
		List <CommentsBean> commentDataList = new ArrayList<CommentsBean>();
		Connection conn= DBTxn.INSTANCE.DS.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement("Select c.comment_id, c.stock_code, c.comment_date ,c.comment_basis,c.comment_type, c.comment_text from comments c where c.stock_code = ?");
			stmt.setString(1, stockCode);
			rs = stmt.executeQuery();
			while(rs.next()) {
				CommentsBean beanObj = new CommentsBean();
				beanObj.setCommentId(rs.getLong("comment_id"));
				beanObj.setStockCode(rs.getString("stock_code"));
				beanObj.setCommentDate(rs.getDate("comment_date"));
				beanObj.setCommentBasis(rs.getString("comment_basis"));
				beanObj.setCommentType(rs.getString("comment_type"));
				beanObj.setCommentText(rs.getString("comment_text"));
				commentDataList.add(beanObj);
			}
		}
		finally {
			if(rs != null) {
				rs.close();
			}
			if(stmt != null) {
				stmt.close();
			}
			conn.close();
		}
		return commentDataList;
	}

	public void saveEarningsNotes(int year, String quarter,String filePath, String stockCode) throws SQLException, FileNotFoundException {
		Connection conn= DBTxn.INSTANCE.DS.getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("insert into earnings_call_notes (call_id,quarter,earning_year,earnings_notes,stock_code)( select key_seq.nextval, v.quarter ,v.year , ? ,? from (select ? as quarter,? as year from dual) v)");
			stmt.setString(2, stockCode);
			stmt.setString(3, quarter);
			stmt.setInt(4, year);
			File f = new File(filePath);
			FileInputStream fis = new FileInputStream(f);
			stmt.setBlob(1,fis);
			stmt.executeUpdate();
		}
		finally {
			stmt.close();
			conn.close();
		}
	}

	public String downloadAndSave(String stockCode,
								   int year,
								   String quarter, String fileLocation) throws SQLException, IOException {
		Connection conn= DBTxn.INSTANCE.DS.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement("Select earnings_notes from earnings_call_notes where stock_code = ? and earning_year =? and quarter = ?");
			stmt.setString(1, stockCode);
			stmt.setInt(2,year);
			stmt.setString(3,quarter);
			rs = stmt.executeQuery();
			while(rs.next()) {
				Blob blob = rs.getBlob("earnings_notes");
				InputStream in = blob.getBinaryStream();
				OutputStream out = new FileOutputStream(fileLocation);
				byte[] buff = new byte[4096];  // how much of the blob to read/write at a time
				int len = 0;
				while ((len = in.read(buff)) != -1) {
					out.write(buff, 0, len);
				}
			}
		}
		finally {
			if(rs != null) {
				rs.close();
			}
			if(stmt != null) {
				stmt.close();
			}
			conn.close();
		}
		return fileLocation;
	}

	public ConsolidatedPortfolioBean getConsolidatedPortfolio() throws SQLException {
		ConsolidatedPortfolioBean portfolioBean = new ConsolidatedPortfolioBean();
		Connection conn= DBTxn.INSTANCE.DS.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement("Select ti , tp , pl, pl_percent  from portfolio_cons");
			rs = stmt.executeQuery();
			while(rs.next()) {
				portfolioBean.setTotalInvestments(rs.getDouble("ti"));
				portfolioBean.setTotalPortfolio(rs.getDouble("tp"));
				portfolioBean.setProfitLoss(rs.getDouble("pl"));
				portfolioBean.setProfitLossPercentage(rs.getDouble("pl_percent"));
			}
		}
		finally {
			if(rs != null) {
				rs.close();
			}
			if(stmt != null) {
				stmt.close();
			}
			conn.close();
		}
		return portfolioBean;
	}
}
