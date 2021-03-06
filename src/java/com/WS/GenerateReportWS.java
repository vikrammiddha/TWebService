/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.WS;

import com.src.ReportsHelper;
import ewsconnect.EWSConnect;
import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import org.apache.log4j.Logger;

/* 
########################################################################### 
# File..................: CustomLogger.java
# Version...............: 1.0
# Created by............: Vikram Middha
# Created Date..........: 27-Jul-2012
# Last Modified by......: 
# Last Modified Date....: 
# Description...........: Web service class.
# Change Request History: 				   							 
########################################################################### 
*/
@WebService(serviceName = "GenerateReportWS")
public class GenerateReportWS {
    
     /*Initializing Logger.*/
    private static Logger LOGGER = Logger.getLogger(GenerateReportWS.class);

    /**
     * Web service operation
     */
    @WebMethod(operationName = "generateReports")
    public Integer generateReports(@WebParam(name = "accountNumber") String accountNumber, @WebParam(name = "billRunId") String billRunId, @WebParam(name = "billId") String billId, @WebParam(name = "runId") String runId, @WebParam(name = "billDate") String billDate) {
        
        try{
            ReportsHelper obj = new ReportsHelper();
            return obj.generateReports(accountNumber, billRunId, billId, runId, billDate); 
            //System.out.println("ret List " + retList.get(0).getAccountNumber());
            //return retList;
        }catch(Exception e){
            LOGGER.error("Exception occured in the web serice call generateReports. Cause : " + e.getMessage() );
        }
       
        return null;
        
    }
    
    @WebMethod(operationName = "SendEmail")
    @Oneway
    public void SendEmail() {
        try{
            
            EWSConnect ewsObj = new EWSConnect();
            ewsObj.emailReports();
            
        }catch(Exception e){
            LOGGER.error("Exception occured while emailing reports. Cause : " + e.getMessage());
        }
    }
}
