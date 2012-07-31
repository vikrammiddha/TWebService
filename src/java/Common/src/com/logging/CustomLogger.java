package Common.src.com.logging;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Layout;

import Common.src.com.Config.AppConfig;
import Common.src.com.Config.Configurator;

/* 
########################################################################### 
# File..................: CustomLogger.java
# Version...............: 1.0
# Created by............: Vikram Middha
# Created Date..........: 27-Jul-2012
# Last Modified by......: 
# Last Modified Date....: 
# Description...........: Custom Logger class.
# Change Request History: 				   							 
########################################################################### 
*/

public class CustomLogger extends org.apache.log4j.FileAppender
{
	
	public CustomLogger() {
    }

    public CustomLogger(Layout pLayout, String filename) throws IOException {
            super(pLayout, filename);
    }

    public CustomLogger(Layout pLayout, String filename, boolean append) throws IOException {
            super(pLayout, filename, append);
    }

    public CustomLogger(Layout pLayout, String filename, boolean append, boolean pBufferedIO,
                                                              int pBufferSize) throws IOException {
            super(pLayout, filename, append, pBufferedIO, pBufferSize);
    }

     
    public void activateOptions() {
    	 
    	BasicConfigurator.configure();
    	AppConfig	appConfig = Configurator.getAppConfig();
    	String logFileName=getFile();
    	SimpleDateFormat dateFormater=new SimpleDateFormat();
    	//dateFormater.applyLocalizedPattern(appConfig.getMerkleDataFeedFileDateFormat());

    	String todayDate = dateFormater.format(Calendar.getInstance().getTime());
    	logFileName=logFileName.replaceFirst(".log","_"+todayDate +".log");
	 
	try {
		setFile(logFileName, false, bufferedIO, bufferSize);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
 
	}
	 
}
