package stocks.model.network;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import stocks.model.StockConstants;

public class Downloader {
    private NetworkUtils netUtilObj = null;
	public Downloader() {
		netUtilObj = new NetworkUtils();
	}
public String downloadBhavCopy(java.util.Date dateObject) throws IOException {
	String downloadedFile = null;
	URL bhavUrl = new URL (netUtilObj.getBhavUrl(dateObject));
	String bhavCopyFile = netUtilObj.getBhavCopyFile(dateObject);
	String absFilePath = StockConstants.STOCK_SCREENER_HOME + StockConstants.BHAV_COPY_DOWNLOAD_FOLDER + File.separator+bhavCopyFile;
	FileOutputStream fileOutputStream =null;
	File downloadFile = new File(absFilePath);
	try {
		if(!downloadFile.exists()) {     // if the file already exists dont download it
			ReadableByteChannel readableByteChannel = Channels.newChannel(bhavUrl.openStream());
			fileOutputStream = new FileOutputStream(absFilePath);
			FileChannel fileChannel = fileOutputStream.getChannel();
			fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
			downloadedFile = absFilePath;
		}
		else {
			downloadedFile = absFilePath;
		}
	} catch (IOException e) {
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

public String unzipBhavCopy(String zippedBhavCopyFile) {
	byte[] buffer = new byte[1024];
	FileInputStream fis;
	String unzippedFileName = null;
    try {
        fis = new FileInputStream(zippedBhavCopyFile);
        ZipInputStream zis = new ZipInputStream(fis);
        ZipEntry ze = zis.getNextEntry();
        String opDir = StockConstants.STOCK_SCREENER_HOME + StockConstants.BHAV_COPY_FOLDER+  File.separator;
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
    } catch (IOException e) {
        e.printStackTrace();
    }
    return unzippedFileName;
    
}

public static void main (String args[]) {
	Calendar currentDate = new GregorianCalendar();
	int lastYear = currentDate.get(Calendar.YEAR) -1;
	Calendar oldDate = new GregorianCalendar();
	oldDate.set(Calendar.YEAR, lastYear);
	Downloader downloadObj = new Downloader();
	while (oldDate.before(currentDate)) {
		try {
			String downloadedFile = downloadObj.downloadBhavCopy(new java.util.Date(oldDate.getTimeInMillis()));
			if(downloadedFile != null) {
				System.out.println(downloadObj.unzipBhavCopy (downloadedFile) +  " unzipped");
			}
			oldDate.add(Calendar.DAY_OF_YEAR, 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
}

