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
import com.bean.CallReport;
import com.bean.Itemisation;
import com.bean.RatedCdr;
import ewsconnect.EWSConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
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
    /*Key fields from HashMap of BillItems that need to be assembled after Explode*/
    /*ReportUtils contains helper mehtods.*/
    private ReportUtils utils = null;
    /*RatedCdr Records*/
    ArrayList<RatedCdr> ratedCdrs = new ArrayList<RatedCdr>();
    /*Service Itemisation Records*/
    ArrayList<Object> callReportObject = new ArrayList<Object>();
    ArrayList<CallReport> callReport = new ArrayList<CallReport>();
    /*RatedCdr Pack*/
    HashMap<String, ArrayList<RatedCdr>> rCdrPack = new HashMap<String, ArrayList<RatedCdr>>();
    /*Service Itemisation Pack*/
    HashMap<String, ArrayList<CallReport>> servicePack = new HashMap<String, ArrayList<CallReport>>();
    /*Itemisation Records*/
    Itemisation itemisation = new Itemisation();
    /*Set of account Numbers*/
    Set<String> accountNumbers = new HashSet<String>();
    /*Email address map for each account Number*/
    HashMap<String, String> accountAuthorizedEmailMap = null;
    /*Email body*/
    private StringBuffer emailBody = new StringBuffer();
    
    public static String billDateVal = "";

    /*Constructor. This initiates the SFDC Connection and other variables.*/
    public ReportsHelper() throws ResilientException, Exception {

        appConfig = Configurator.getAppConfig();

        try {
            eSession = SalesforceUtils.initMasterSession(appConfig);
        } catch (Exception de) {
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
        mObj.generateReports("TH14225500-2", "", "", "", "2012-06-26");

    }

    /*This method is called from web service. This queries SFDC and populates the BilItem data in BillItem beans ArrayList.*/
    public Integer generateReports(String accountNumber, String billRunId, String billId, String runId, String billDate) throws Exception {

        LOGGER.info("Method Started : generateReports");

        LOGGER.info("Input values . account Number :" + accountNumber + ", billRunId :" + billRunId + ", billId :" + billId + ", RequestId :" + runId + ", BillDate :" + billDate);
        
        billDateVal = billDate;
                
        /*This ArrayList contains all the data queried from SFDC.*/
        HashMap<String, ArrayList<BillItem>> biItemMap = new HashMap<String, ArrayList<BillItem>>();

        EWSConnection ewsObj = new EWSConnection();
        /*If all the 3 input variables are null, return null.*/
        if (utils.isBlank(accountNumber) && utils.isBlank(billRunId) && utils.isBlank(billId)) {
            return null;
        }

        try {

            /*Prepare the query by getting the fields from Resilient.properties file.*/
            String query = utils.getQuery(appConfig);

            /*Add AccountNumber in the where clause if it is not null.*/
            if (utils.isNotBlank(accountNumber)) {
                query = utils.addWhereClause("Espresso_Bill__Bill__r.Espresso_Bill__Account__r.Espresso_PC__Account_Number__c", accountNumber, query);
            }

            /*Add billRunId in the where clause if it is not null.*/
            if (utils.isNotBlank(billRunId)) {
                query = utils.addWhereClause("Espresso_Bill__Bill__r.Espresso_Bill__Bill_Run_ID__c", Integer.valueOf(billRunId), query);
            }

            /*Add billId in the where clause if it is not null.*/
            if (utils.isNotBlank(billId)) {
                query = utils.addWhereClause("Espresso_Bill__Bill__r.Name", billId, query);
            }

            /*Add Where clause to only retrieve bills that require Itemisation*/
            query = utils.addWhereClause("Espresso_Bill__Bill__r.Espresso_Bill__Account__r.Itemisation_Required__c", true, query);

            /*Below code is to get the latest Bill in case only Account Number is mentioned.*/
            if (utils.isNotBlank(accountNumber) && utils.isBlank(billRunId) && utils.isBlank(billId)) {
                String latestBillId = utils.getLatestBillId(accountNumber, querySfdc);
                if (utils.isNotBlank(latestBillId)) {
                    query = utils.addWhereClause("Espresso_Bill__Bill__r.Name", latestBillId, query);
                    LOGGER.info("Queried Latest Bill id from SFDC. Bill ID : " + latestBillId);
                }
            }
            
            query += " AND Espresso_Bill__Total_Gross_Amount__c > 0";
            //query += " AND Espresso_Bill__Total_Gross_Amount__c > 0 AND Espresso_Bill__Type__c != 'Event'";
            //LOGGER.info("Query prepared : " + query);

            //query += " limit 1";

            /*Populate the data returned from SFDC in Arraylist of BillItem bean.*/
            biItemMap = utils.populateBillItemBeans(query, querySfdc);

            LOGGER.info("Total number Accounts returned: " + biItemMap.size());
            
            if(biItemMap.size() > 0){
                Set<String> bItemKeys = biItemMap.keySet();
            
                accountNumbers = utils.getAccountNumbers(bItemKeys);

                // This is the place where Nimil's code will start with bitemList as Input;

                accountAuthorizedEmailMap = utils.getAccountAuthorizedEmailMap(accountNumbers, querySfdc);

                /*Fetch the Invoice numbes from another database based on the account numbers.*/

                HashMap<String,String> accountNumInvoiceMap = new HashMap<String,String>();

                accountNumInvoiceMap = utils.generateInvoiceNumbers(accountNumbers);

                LOGGER.info("Fetched Invoice Numbers : " + accountNumInvoiceMap );

                /*Loop through each bill and create pdf. 
                 */
                utils.buildItemisation(bItemKeys, billDate, accountAuthorizedEmailMap, biItemMap,runId,accountNumInvoiceMap);


                utils.closeConnections();
            }
            
            
        } catch (Exception e) {
            LOGGER.error("Exception occured while preparing data for Bill Item. Cause : " + e.getCause().getMessage());
            emailBody.append("Reports could not be generated for run Id :").append(runId).append(". Cause :").append(e.getCause().getMessage()).append(System.getProperty("line.separator"));
            emailBody.append("Inputs for generating the reports : ").append("</br>").append(" Account Number :").append(accountNumber).append("</br>").append(" Bill Run Id :").append(billRunId).append("</br>").append("Bill Id :").append(billId).append("\n");
            ewsObj.sendEmail(appConfig.getErrorSubject() + ". RunId :" + runId, emailBody.toString());
            System.gc();
            return -1;
        }

        //LOGGER.info("returning the list" + biItemList);
        emailBody.append("Successfully generated the Reports for Run Id :").append(runId).append("</br>");
        emailBody.append("Inputs for generating the reports : ").append("</br>").append(" Account Number :").append(accountNumber).append("</br>").append(" Bill Run Id :").append(billRunId).append("</br>").append("Bill Id :").append(billId).append("\n");
        ewsObj.sendEmail(appConfig.getSuccessSubject() + ". RunId :" + runId, emailBody.toString());
        System.gc();
        return biItemMap.size();
    }
}
