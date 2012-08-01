package com.src;

/*########################################################################### 
# File..................: ReportsHelper.java
# Version...............: 1.0
# Created by............: Vikram Middha
# Created Date..........: 24-07-2012
# Last Modified by......: 
# Last Modified Date....: 
# Description...........: This is the helper class which is called from the web service
*                         method. This makes the SFDC connection and gets the data from 
*                         SFDC that needs to be displayed on the reports.
########################################################################### 
*/


import Common.src.com.Config.AppConfig;
import Common.src.com.Config.Configurator;
import Common.src.com.Exception.ResilientException;
import Common.src.com.SFDC.EnterpriseSession;
import Common.src.com.util.SalesforceUtils;
import com.bean.BillItem;
import ewsconnect.EWSConnection;
import java.util.ArrayList;
import org.apache.log4j.Logger;


public class ReportsHelper {
	
    /*session variable used to make SFDC connections.*/
    private EnterpriseSession eSession = null;

    /*Initialize and load the Resilient.Propoerties file */
    private static AppConfig appConfig = null;

    /*QuerySFDC contains all the methods for interacting with SFDC.*/
    private static QuerySFDC querySfdc = null;

    /*Initializing Logger.*/
    private static Logger LOGGER = Logger.getLogger(ReportsHelper.class);

    /*ReportUtils contains helper mehtods.*/
    private ReportUtils utils = null;
    
    private StringBuffer emailBody = new StringBuffer();
	
        
    /*Constructor. This initiates the SFDC Connection and other variables.*/
    public ReportsHelper() throws ResilientException{
        
        appConfig = Configurator.getAppConfig();
        
        try{
            eSession = SalesforceUtils.initMasterSession(appConfig);				
        }catch(Exception de) {
            LOGGER.error("Exception while initializing SFDC Login Session...: " + de);
            throw new ResilientException(de.getMessage());
        }
        querySfdc = new QuerySFDC(eSession);
        utils = new ReportUtils();				
    }
	
    /**
     * The main class. This is just for testing purpose. This needs to be removed and this is the helper class
     * for the web service method.
     * @throws Exception 
     * 
     */
    public static void main(String[] s) throws Exception {

            ReportsHelper mObj = new ReportsHelper();
            mObj.generateReports("SS79926808","","", "GR-10");

    }
	
    /*This method is called from web service. This queries SFDC and populates the BilItem data in BillItem beans ArrayList.*/
    public Integer generateReports(String accountNumber, String billRunId, String billId, String runId) throws Exception{

        LOGGER.info("Method Started : generateReports");

        LOGGER.info("Input values . account Number :" + accountNumber + ", billRunId :" + billRunId + ", billId :" + billId);

        /*This ArrayList contains all the data queried from SFDC.*/                
        ArrayList<BillItem> biItemList = new ArrayList<BillItem>();
        
        EWSConnection ewsObj = new EWSConnection();

        /*If all the 3 input variables are null, return null.*/
        if(utils.isBlank(accountNumber) && utils.isBlank(billRunId) && utils.isBlank(billId)){			
            return null;	
        }

        try{

            /*Prepare the query by getting the fields from Resilient.properties file.*/
            String query = utils.getQuery(appConfig);

            /*Add AccountNumber in the where clause if it is not null.*/
            if(utils.isNotBlank(accountNumber)){
                query = utils.addWhereClause("Espresso_Bill__Bill__r.Espresso_Bill__Account__r.Espresso_PC__Account_Number__c", accountNumber , query);
            }

            /*Add billRunId in the where clause if it is not null.*/  
            if(utils.isNotBlank(billRunId)){
               query = utils.addWhereClause("Espresso_Bill__Bill__r.Espresso_Bill__Bill_Run_ID__c", Integer.valueOf(billRunId) , query);
            }

            /*Add billId in the where clause if it is not null.*/  
            if(utils.isNotBlank(billId)){
                query = utils.addWhereClause("Espresso_Bill__Bill__r.Name", billId, query);
            }
            
            /*Below code is to get the latest Bill in case only Account Number is mentioned.*/
            if(utils.isNotBlank(accountNumber) && utils.isBlank(billRunId) && utils.isBlank(billId)){
                String latestBillId = utils.getLatestBillId(accountNumber, querySfdc);
                if(utils.isNotBlank(latestBillId)){
                    query = utils.addWhereClause("Espresso_Bill__Bill__r.Name", latestBillId , query);
                    LOGGER.info("Queried Latest Bill id from SFDC. Bill ID : " + latestBillId);
                }
            }

            LOGGER.info("Query prepared : " + query);

            //query += " limit 1";

            /*Populate the data returned from SFDC in Arraylist of BillItem bean.*/
            biItemList = utils.populateBillItemBeans(query, querySfdc);

            LOGGER.info("Total number of Bill Item records queried: " + biItemList.size());

            // This is the place where Nimil's code will start with bitemList as Input;

        }catch(Exception e){
            LOGGER.error("Exception occured while preparing data for Bill Item. Cause : " + e.getMessage());
            emailBody.append("Reports could not be generated for run Id :").append(runId).append(". Cause :").append(e.getMessage()).append("\n");
            ewsObj.sendEmail(appConfig.getErrorSubject() + ". RunId :" + runId, emailBody.toString());
            return -1;
        }

        //LOGGER.info("returning the list" + biItemList);
        emailBody.append("Successfully generated the Reports for Run Id :").append(runId).append("\n");        
        //ewsObj.sendEmail(appConfig.getSuccessSubject() + ". RunId :" + runId, emailBody.toString());
        return biItemList.size();
    }
	
	
}
