package stocks.model.network;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import stocks.model.StockConstants;
import stocks.model.data.SyncData;

public class Downloader {
    private NetworkUtils netUtilObj = null;
	public Downloader() {
		netUtilObj = new NetworkUtils();
	}
public String downloadBhavCopy(java.util.Date dateObject, String exchange) throws IOException, SQLException {
	String downloadedFile = null;
	URL bhavUrl = new URL (netUtilObj.getBhavUrl(dateObject, exchange));
	String bhavCopyFile = netUtilObj.getBhavCopyFile(dateObject,exchange);
	String absFilePath = "";
	 if(exchange.equals(StockConstants.NSE_EXCHANGE)) {
		 absFilePath = StockConstants.STOCK_SCREENER_HOME  + File.separator + StockConstants.BHAV_COPY_DOWNLOAD_FOLDER + File.separator+bhavCopyFile;
	 }
	 else {
		 absFilePath = StockConstants.STOCK_SCREENER_HOME +  File.separator + exchange +  File.separator + StockConstants.BHAV_COPY_DOWNLOAD_FOLDER + File.separator+bhavCopyFile;
	 }
	FileOutputStream fileOutputStream =null;
	File downloadFile = new File(absFilePath);
	SyncData dataObj = new SyncData();
	try {
		if(!downloadFile.exists()) {     // if the file doesnt exist
			// check if its a registered holiday
			if(!dataObj.isHoliday(new java.sql.Date(dateObject.getTime()))) {         // if its not a registered holiday
				boolean isHoliday = register404(bhavUrl);                             // check over network if file exists 
				if(isHoliday) {                                                       // if file doesnt exist
					dataObj.registerHoliday(new java.sql.Date(dateObject.getTime())); // register the holiday
				}
				else {                                                                // if its not a holiday download the file
					ReadableByteChannel readableByteChannel = null;
					System.out.println(bhavUrl);
					readableByteChannel= Channels.newChannel(bhavUrl.openStream());
					fileOutputStream = new FileOutputStream(absFilePath);
					FileChannel fileChannel = fileOutputStream.getChannel();
					fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
					downloadedFile = absFilePath;
				}
			}
		}
		else {                                                                       // if the file exists return the file path
				downloadedFile = absFilePath;
		} 
	}catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	finally {
        if(fileOutputStream != null) {
        		fileOutputStream.close();
        }
	}
	return downloadedFile;
}

private boolean register404(URL bhavUrl) {
	boolean returnValue = false;
	HttpURLConnection conn= null;
	try {
		conn = (HttpURLConnection) bhavUrl.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("User-Agent","Mozilla/5.0");
		int responseCode = conn.getResponseCode();
	}
	catch (IOException ex) {
		try {
			int responseCode = ((HttpURLConnection)conn).getResponseCode();
			if(responseCode == 404) {
				returnValue = true;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(bhavUrl.toString());
			e.printStackTrace();
			returnValue = true;
		}
	}
	return returnValue;
}

public String unzipBhavCopy(String zippedBhavCopyFile, String exchange) throws SQLException {
	byte[] buffer = new byte[1024];
	FileInputStream fis;
	String unzippedFileName = null;
	
    try {
    	if(zippedBhavCopyFile != null) {
        fis = new FileInputStream(zippedBhavCopyFile);
        ZipInputStream zis = new ZipInputStream(fis);
        ZipEntry ze = zis.getNextEntry();
        String opDir = null;
        if(exchange.equals(StockConstants.NSE_EXCHANGE)) {
        		opDir = StockConstants.STOCK_SCREENER_HOME + File.separator+ StockConstants.BHAV_COPY_FOLDER+  File.separator;
        }
        else if(exchange.equals(StockConstants.BSE_EXCHANGE)) {
        	    opDir = StockConstants.STOCK_SCREENER_HOME + File.separator + exchange + File.separator + StockConstants.BHAV_COPY_FOLDER+  File.separator;
        }
        while(ze != null){
            String fileName = ze.getName();
            unzippedFileName = opDir+ fileName;
            File newFile = new File( unzippedFileName);
            
            if(!newFile.exists()) {
            		FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                		fos.write(buffer, 0, len);
                }
                fos.close();
                SyncData dataObj = new SyncData();
				dataObj.registerUnzipEvent(zippedBhavCopyFile);
               
            }
            else {
            	   System.out.println("Unzipped file:" +opDir + fileName + "  exists ");
            }
            zis.closeEntry();
            ze = zis.getNextEntry();
            //close this ZipEntry
            	}
        zis.closeEntry();
        zis.close();
        fis.close();
    	}
    } catch (IOException e) {
        e.printStackTrace();
    }
    return unzippedFileName;
    
}

public static void main (String args[]) throws SQLException, IOException {
//	Calendar currentDate = new GregorianCalendar();
//	int lastYear = currentDate.get(Calendar.YEAR) -1;
//	Calendar oldDate = new GregorianCalendar();
//	oldDate.set(Calendar.YEAR, lastYear);
//	Downloader downloadObj = new Downloader();
//	while (oldDate.before(currentDate)) {
//		try {
//			String downloadedFile = downloadObj.downloadBhavCopy(new java.util.Date(oldDate.getTimeInMillis()),"BSE");
//			if(downloadedFile != null) {
//				System.out.println(downloadObj.unzipBhavCopy (downloadedFile,"BSE") +  " unzipped");
//			}
//			oldDate.add(Calendar.DAY_OF_YEAR, 1);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	ReadableByteChannel readableByteChannel = null;
	URL bhavUrl = null ;
	try {
		bhavUrl = new URL("https://www.nseindia.com/api/reports?archives=[{\"name\":\"CM-UDiFF Common Bhavcopy Final (zip)\",\"type\":\"daily-reports\",\"category\":\"capital-market\",\"section\":\"equities\"}]&date=03-Jul-2024&type=equities&mode=single");
	} catch (MalformedURLException e) {
		throw new RuntimeException(e);
	}


	HttpURLConnection con = (HttpURLConnection) bhavUrl.openConnection();
	con.addRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:127.0) Gecko/20100101 Firefox/127.0");
	con.setRequestMethod("GET");
	//con.setDoOutput(true);
	con.addRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8");
	con.addRequestProperty("Accept-Encoding","gzip, deflate, br, zstd");
	con.addRequestProperty("Host","www.nseindia.com");
	con.addRequestProperty("Referer","https://www.nseindia.com/all-reports");
	con.addRequestProperty("Sec-Fetch-Dest", "document");
	con.addRequestProperty("Sec-Fetch-Mode", "navigate");
	con.addRequestProperty("Sec-Fetch-Site", "same-origin");
	con.addRequestProperty("Sec-Fetch-User", "?1");
	con.addRequestProperty("Upgrade-Insecure-Requests","1");
	con.addRequestProperty("Connection","keep-alive");
	con.addRequestProperty("Accept-Language","en-US,en;q=0.5");
	con.addRequestProperty("Cookie","_ga_87M7PJ3R97=GS1.1.1720708999.21.1.1720709627.21.0.0; _ga=GA1.1.647735472.1703309215; _abck=8227ACBC4E6005943CBC761649238578~0~YAAQhzYauCzdiIuQAQAAbqc/ogzcPSO6rchXPpvDanonx9c20r2B9ZgZmEFNy/3SsuifZtbk2gUWF0mDMAqxldQhRIDPTJKMGkFXrcsNqoRmPhSmW45wDQFiEzGw1SK7wQ0Be8LiIEvGC+ZcFI7kwBDPyAmOIKEsibnoBxggyqy6MeHSWM4IH1bCrzylZL1GwAuDm3Q4573Q352auNr19V4X2y/O+Hq4AmDvymgZg99m9QB8Mdd49c8ifEdsGN/b5Y70gfCFa3QPEBAFMCcx/keGlo3ywnNmsn8EX5KIajjmbto+ryyJIfe9fZEs1glEOBEZ0TNFsXlzHsM+Gi0RmbNxV1KB/5coAz/zqZlmqYf4boO6Zh0OshUD+2VrUEm…m2B983Pw==~3753525~3688003; bm_sv=3FCC1CD5B6A539E7FA9229AC9D2D0812~YAAQhzYauO4EiYuQAQAALBpJohjIFFYal6s9SY+msv1xEBD8XG6yAfKZxNaF6+M8E5M6xtbJQYs4wKN+vMk+xwCg+gbo0PzI/crZ8ZDCn+CgTG3DIwZ29ACaHtwtUuK9bbD/2ixcfyvhtWHJGqf4sI3BwR0iTD+juhBFVB5sFmJoEfzufki3GU5uIbsd4C2RJLDvLCHv08Sj3MPqKqQha32O6MptF4CLCLlLE5s+gYcCklmNMHvXZIGiSyz/1+DM/gyV~1; nseappid=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhcGkubnNlIiwiYXVkIjoiYXBpLm5zZSIsImlhdCI6MTcyMDcwOTQ5NSwiZXhwIjoxNzIwNzE2Njk1fQ.kXE7VMvN8PeP4b6vjSxOPhDm1HwZVXguHlZSJw45-V8");


	//DataOutputStream out = new DataOutputStream(con.getOutputStream());

	int status = con.getResponseCode();
	FileOutputStream fileOutputStream =null;
	String absFilePath = "/Users/tusharmohanty/";
	readableByteChannel= Channels.newChannel(bhavUrl.openStream());
	fileOutputStream = new FileOutputStream(absFilePath);
	FileChannel fileChannel = fileOutputStream.getChannel();
	fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);



}


}

