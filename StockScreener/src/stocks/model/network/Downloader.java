package stocks.model.network;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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
	URLConnection conn= null;
	try {
		conn = bhavUrl.openConnection();
		conn.setConnectTimeout(5000);
		conn.setReadTimeout(5000);
		conn.getInputStream();
	}
	catch (IOException ex) {
		try {
			int responseCode = ((HttpURLConnection)conn).getResponseCode();
			if(responseCode == 404) {
				returnValue = true;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
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

public static void main (String args[]) throws SQLException {
	Calendar currentDate = new GregorianCalendar();
	int lastYear = currentDate.get(Calendar.YEAR) -1;
	Calendar oldDate = new GregorianCalendar();
	oldDate.set(Calendar.YEAR, lastYear);
	Downloader downloadObj = new Downloader();
	while (oldDate.before(currentDate)) {
		try {
			String downloadedFile = downloadObj.downloadBhavCopy(new java.util.Date(oldDate.getTimeInMillis()),"BSE");
			if(downloadedFile != null) {
				System.out.println(downloadObj.unzipBhavCopy (downloadedFile,"BSE") +  " unzipped");
			}
			oldDate.add(Calendar.DAY_OF_YEAR, 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


}

