package Common.src.com.util;

import Common.src.com.Config.AppConfig;
import Common.src.com.Exception.ResilientException;
import Common.src.com.SFDC.EnterpriseSession;
import org.apache.log4j.Logger;

/* 
########################################################################### 
# File..................: CustomLogger.java
# Version...............: 1.0
# Created by............: Vikram Middha
# Created Date..........: 27-Jul-2012
# Last Modified by......: 
# Last Modified Date....: 
# Description...........: Utility class for SFDC connectivity.
# Change Request History: 				   							 
########################################################################### 
*/

public final class SalesforceUtils {
	
	private static Logger LOGGER = Logger.getLogger(SalesforceUtils.class);
	
    /**
     * Private constructor.
     */
    private SalesforceUtils() {
        throw new UnsupportedOperationException("Class is not instantiable.");
    }
    
    /**
     * Connects to Master SFDC.
     * 
     * @param appConfig AppConfig instance
     * @return a populated PartnerSession for the Master session.
     * @throws ResilientException 
     */
    public static EnterpriseSession initMasterSession(String sessionId, String serverURL) throws ResilientException {
    	LOGGER.info("SalesforceUtils: initMasterSession(): Connecting to SFDC...........");
    	EnterpriseSession session = new EnterpriseSession();
        boolean connected = session.connect(sessionId, serverURL);
        if(connected) {
        	return session;
        }
        return null;
    }
    
}
