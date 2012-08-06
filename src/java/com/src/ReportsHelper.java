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
import java.sql.Date;
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
    private static String[] explodedValues;
    private static int IDX_ACCOUNT_NUMBER = 0;
    private static int IDX_REQUIRE_SERV = 1;
    private static int IDX_REQUIRE_TEL = 2;
    /*Key Field values*/
    private String AccountNumber;
    private String RequireTelephony;
    private String RequireService;
    /*Call Report Mapping*/
    private static int IDX_DESTINATION = 0;
    private static int IDX_COST = 1;
    private static int IDX_COUNT = 2;
    /*ReportUtils contains helper mehtods.*/
    private ReportUtils utils = null;
    /*RatedCdr Records*/
    RatedCdrHelper rch = new RatedCdrHelper();
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
    ArrayList<Itemisation> itemisations = new ArrayList<Itemisation>();
    /*Set of account Numbers*/
    Set<String> accountNumbers = new HashSet<String>();
    /*Email address map for each account Number*/
    HashMap<String, String> accountAuthorizedEmailMap = null;
    /*Email body*/
    private StringBuffer emailBody = new StringBuffer();

    /*Constructor. This initiates the SFDC Connection and other variables.*/
    public ReportsHelper() throws ResilientException {

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
        mObj.generateReports("SS79926808", "", "", "GR-10", "");

    }

    /*This method is called from web service. This queries SFDC and populates the BilItem data in BillItem beans ArrayList.*/
    public Integer generateReports(String accountNumber, String billRunId, String billId, String runId, String billDate) throws Exception {

        LOGGER.info("Method Started : generateReports");

        LOGGER.info("Input values . account Number :" + accountNumber + ", billRunId :" + billRunId + ", billId :" + billId);

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

            LOGGER.info("Query prepared : " + query);

            //query += " limit 1";

            /*Populate the data returned from SFDC in Arraylist of BillItem bean.*/
            biItemMap = utils.populateBillItemBeans(query, querySfdc);

            LOGGER.info("Total number of Bill Item records queried: " + biItemMap.size());
            Set<String> bItemKeys = biItemMap.keySet();
            // This is the place where Nimil's code will start with bitemList as Input;
            for (String keySet : bItemKeys) {
                explodedValues = keySet.split(",");
                AccountNumber = explodedValues[IDX_ACCOUNT_NUMBER];
                RequireTelephony = explodedValues[IDX_REQUIRE_TEL];
                RequireService = explodedValues[IDX_REQUIRE_SERV];
                ratedCdrs = rch.getEvents(AccountNumber, Date.valueOf(billDate));
                callReportObject = rch.getCallReport(AccountNumber, Date.valueOf(billDate));
                for (Object eachReport : callReportObject) {
                    Object[] row = (Object[]) eachReport;
                    callReport.add(new CallReport(Integer.valueOf(row[IDX_COUNT].toString()), row[IDX_DESTINATION].toString(), Double.valueOf(row[IDX_COST].toString())));
                }
                LOGGER.info("Total number of Rated records queried for AccountNumber " + keySet + " is : " + ratedCdrs.size());
                rCdrPack.put(AccountNumber, ratedCdrs);
                servicePack.put(AccountNumber, callReport);
                itemisations.add(new Itemisation(AccountNumber, RequireTelephony, RequireService, rCdrPack, servicePack, biItemMap.get(keySet)));
            }

            for (Itemisation itemisation : itemisations) {
                accountNumbers.add(itemisation.getAccountNumber());
            }

            accountAuthorizedEmailMap = utils.getAccountAuthorizedEmailMap(accountNumbers, querySfdc);

            for (Itemisation itemisation : itemisations) {
                itemisation.setEmailAddress(accountAuthorizedEmailMap.get(itemisation.getAccountNumber()));
            }

            /*Create the pdfs*/
            utils.createPDF(itemisations,runId);
        } catch (Exception e) {
            LOGGER.error("Exception occured while preparing data for Bill Item. Cause : " + e.getMessage());
            emailBody.append("Reports could not be generated for run Id :").append(runId).append(". Cause :").append(e.getMessage()).append("\n");
            ewsObj.sendEmail(appConfig.getErrorSubject() + ". RunId :" + runId, emailBody.toString());
            return -1;
        }

        //LOGGER.info("returning the list" + biItemList);
        emailBody.append("Successfully generated the Reports for Run Id :").append(runId).append("\n");
        ewsObj.sendEmail(appConfig.getSuccessSubject() + ". RunId :" + runId, emailBody.toString());
        return biItemMap.size();
    }
}
