package com.src;

import Common.src.com.Config.AppConfig;
import Common.src.com.Config.Configurator;
import Common.src.com.Exception.ResilientException;
import com.bean.BillItem;
import com.bean.CallReport;
import com.bean.InvoiceNumber;
import com.bean.Itemisation;
import com.bean.RatedCdr;
import ewsconnect.EWSConnection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

/* 
########################################################################### 
# File..................: ReportUtils.java
# Version...............: 1.0
# Created by............: Vikram Middha
# Created Date..........: 27-Jul-2012
# Last Modified by......: 
# Last Modified Date....: 
# Description...........: This class contains Utility functions .
# Change Request History: 				   							 
########################################################################### 
 */
public class ReportUtils {

    private static Logger LOGGER = Logger.getLogger(ReportUtils.class);
    private static AppConfig appConfig = null;
    private static String[] explodedValues;
    private static int IDX_ACCOUNT_NUMBER = 0;
    private static int IDX_REQUIRE_SERV = 1;
    private static int IDX_REQUIRE_TEL = 2;
    private static int IDX_ACCOUNT_NAME = 3;
    /*Key Field values*/
    private String AccountNumber;
    private String AccountName;
    private String RequireTelephony;
    private String RequireService;
    /*RatedCdr Records*/
    ArrayList<RatedCdr> ratedCdrs = new ArrayList<RatedCdr>();
    /*Service Itemisation Records*/
    ArrayList<Object> callReportObject = new ArrayList<Object>();
    ArrayList<CallReport> callReport = new ArrayList<CallReport>();
    /*RatedCdr Pack*/
    HashMap<String, ArrayList<RatedCdr>> rCdrPack = new HashMap<String, ArrayList<RatedCdr>>();
    /*Service Itemisation Pack*/
    HashMap<String, ArrayList<CallReport>> servicePack = new HashMap<String, ArrayList<CallReport>>();
    /*RatedCdr Records*/
    RatedCdrHelper rch = new RatedCdrHelper();
    
    Set<String> accountNumbers = new HashSet<String>();
    /*Call Report Mapping*/
    private static int IDX_DESTINATION = 0;
    private static int IDX_COST = 1;
    private static int IDX_COUNT = 2;
    private Itemisation itemisation = new Itemisation();
    PDFCreator pdfCreate ;
    private String invoiceNumber = "";
    
    public ReportUtils() throws Exception {
        try {
            appConfig = Configurator.getAppConfig();;
            pdfCreate = new PDFCreator();
        } catch (ResilientException ex) {
            java.util.logging.Logger.getLogger(ReportUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /*prepares the select query by reading the fields from Resilient.properties file.*/

    public String getQuery(AppConfig appConfig) {

        return "SELECT " + appConfig.getSfdcBillToItemQueryFields();

    }

    /*Makes sure that the field is not blank.*/
    public Boolean isNotBlank(String val) {
        LOGGER.info("Entered in isNotBlank . Val : " + val);
        if (null != val && !"".equals(val.trim())) {
            return true;
        }
        return false;
    }

    /*Makes sure that the field is blank.*/
    public Boolean isBlank(String val) {

        if (null == val || "".equals(val.trim())) {
            return true;
        }
        return false;
    }

    /*Adds the where clause in the query.*/
    public String addWhereClause(String key, String value, String query) {

        if (query.toUpperCase().contains("WHERE")) {
            query += " AND " + key + "='" + value + "'";
        } else {
            query += " WHERE " + key + "='" + value + "'";
        }

        return query;
    }

    /*Adds the where clause in the query.*/
    public String addWhereClause(String key, Integer value, String query) {

        LOGGER.info("In Method addWhereClause. Key :" + key + ",value : " + value + ", query " + query);

        if (query.toUpperCase().contains("WHERE")) {
            query += " AND " + key + "=" + value;
        } else {
            query += " WHERE " + key + "=" + value;
        }

        return query;
    }

    /*Adds the where clause in the query.*/
    public String addWhereClause(String key, Boolean value, String query) {

        LOGGER.info("In Method addWhereClause. Key :" + key + ",value : " + value + ", query " + query);

        if (query.toUpperCase().contains("WHERE")) {
            query += " AND " + key + "=" + value;
        } else {
            query += " WHERE " + key + "=" + value;
        }

        return query;
    }

    /*The below method get the latest Bill Id for the given Account number from SFDC.*/
    public String getLatestBillId(String accountNumber, QuerySFDC querySFDC) throws ResilientException {

        String query = "Select Name from Espresso_Bill__Bill__c ";
        query = addWhereClause("ESPRESSO_BILL__ACCOUNT__R.ESPRESSO_PC__ACCOUNT_NUMBER__C", accountNumber, query);
        query += " ORDER BY NAME DESC LIMIT 1";

        HashMap<String, Object>[] resultMap = querySFDC.executeQuery(query);

        if (resultMap.length != 1) {
            return null;
        }

        return (String) resultMap[0].get("NAME");

    }

    /*populate the data queried from SFDC into List of BillItem objects.*/
    public HashMap<String, ArrayList<BillItem>> populateBillItemBeans(String query, QuerySFDC querySFDC) throws ResilientException {

        ArrayList<BillItem> retBIList = new ArrayList<BillItem>();
        
        HashMap<String, ArrayList<BillItem>> retBIMap = new HashMap<String, ArrayList<BillItem>>();
        try {
            HashMap<String, Object>[] resultMap = querySFDC.executeQuery(query);

            for (int i = 0; i < resultMap.length; i++) {

                HashMap<String, Object> hm = resultMap[i];
                BillItem biObj = new BillItem();               
                
                biObj.setAssetName((String) hm.get("ESPRESSO_BILL__ASSET__R.NAME"));
                biObj.setBillDate((String) hm.get("ESPRESSO_BILL__BILL__R.ESPRESSO_BILL__BILL_DATE__C"));
                //biObj.setBillPeriod((String)hm.get("Espresso_Bill__Asset__R.Name"));
                biObj.setDateFrom(getFormatedDate((String) hm.get("ESPRESSO_BILL__DATE_FROM__C")));
                biObj.setDateTo(getFormatedDate((String) hm.get("ESPRESSO_BILL__DATE_TO__C")));
                biObj.setRetalGross((String) hm.get("ESPRESSO_BILL__GROSS_AMOUNT_1__C"));
                biObj.setAccountNumber((String) hm.get("ESPRESSO_BILL__BILL__R.ESPRESSO_BILL__ACCOUNT__R.ESPRESSO_PC__ACCOUNT_NUMBER__C"));
                biObj.setAccountName((String) hm.get("ESPRESSO_BILL__BILL__R.ESPRESSO_BILL__ACCOUNT__R.NAME"));
                biObj.setIdentifier((String) hm.get("ESPRESSO_BILL__ACCOUNT_SERVICE_LINE__R.ESPRESSO_PC__BILLING_IDENTIFIER__C"));
                biObj.setRequireServItemisation((((String) hm.get("ESPRESSO_BILL__BILL__R.ESPRESSO_BILL__ACCOUNT__R.SUMMARY_ITEMISATION_REQUIRED__C")).equalsIgnoreCase("true")) ? true : false);
                biObj.setRequireTelItemisation((((String) hm.get("ESPRESSO_BILL__BILL__R.ESPRESSO_BILL__ACCOUNT__R.TEL_ITEMISATION_REQUIRED__C")).equalsIgnoreCase("true")) ? true : false);
                biObj.setOfferName((String) hm.get("ESPRESSO_BILL__ASSET__R.ESPRESSO_PC__OFFER__R.NAME"));
                biObj.setOfferSFDCId((String) hm.get("ESPRESSO_BILL__ASSET_R.ESPRESSO_PC__OFFER_C"));            
                retBIList.add(biObj);
                
                //retBIList.add(groupedBIMap.values());
            }
            for (BillItem billItem : retBIList) {
                if (retBIMap.containsKey(billItem.getAccountNumber() + "," + billItem.getRequireServItemisation() + "," + billItem.getRequireTelItemisation() + "," + billItem.getAccountName())) {
                    retBIMap.get(billItem.getAccountNumber() + "," + billItem.getRequireServItemisation() + "," + billItem.getRequireTelItemisation() + "," + billItem.getAccountName()).add(billItem);
                } else {
                    retBIMap.put(billItem.getAccountNumber() + "," + billItem.getRequireServItemisation() + "," + billItem.getRequireTelItemisation() + "," + billItem.getAccountName(), new ArrayList<BillItem>());
                    retBIMap.get(billItem.getAccountNumber() + "," + billItem.getRequireServItemisation() + "," + billItem.getRequireTelItemisation() + "," + billItem.getAccountName()).add(billItem);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error occured while query SFDC for BillItems. Cause : " + e.getStackTrace());
            throw new ResilientException(e.getMessage());
        }

        return retBIMap;
    }

    /*Creation of PDF can be handled here*/
    public void createPDF(Itemisation itemisation, String runId, String invoiceNumber) throws Exception {
        pdfCreate.createPdf(itemisation, runId, invoiceNumber);
    }

    /*Below function returns the map of Accountnumber and authorized contact email address.*/
    public HashMap<String, String> getAccountAuthorizedEmailMap(Set<String> accountNumberSet, QuerySFDC querySFDC) throws ResilientException {

        HashMap<String, String> retMap = new HashMap<String, String>();

        String accountNumberExp = getCommaSeparatedString(accountNumberSet);

        if (accountNumberExp.length() <= 0) {
            return retMap;
        }

        String query = "Select EMAIL,Account.Espresso_PC__Account_Number__c from Contact where Authorised_Contact__c = true and "
                + "Account.Espresso_PC__Account_Number__c IN (" + accountNumberExp + ")";
        LOGGER.info("Query String to get Email Addresses : " + query);
        try {
            HashMap<String, Object>[] resultMap = querySFDC.executeQuery(query);

            if (resultMap.length > 0) {
                for (int i = 0; i < resultMap.length; i++) {
                    HashMap<String, Object> hm = resultMap[i];
                    retMap.put((String) hm.get("ACCOUNT.ESPRESSO_PC__ACCOUNT_NUMBER__C"), (String) hm.get("EMAIL"));
                }
            }

            LOGGER.info("Total number of email adddresses retrieved : " + retMap.size());
        } catch (Exception e) {
            LOGGER.error("Error occured while query SFDC for Authorized contact email address. Cause : " + e.getMessage());
            throw new ResilientException(e.getMessage());
        }

        return retMap;
    }
    
    private HashMap<String, ArrayList<BillItemSummary>> getBillItemSummaryMap(ArrayList<BillItem> biList){
        
        HashMap<String,BillItemSummary> groupedBIMap = new HashMap<String,BillItemSummary>();
        HashMap<String, ArrayList<BillItemSummary>> retBIMap = new HashMap<String, ArrayList<BillItemSummary>>();
        
        for(BillItem bi : biList){
            
            if(groupedBIMap.get(bi.getOfferSFDCId()) == null){                    
                BillItemSummary biSummaryObj = new BillItemSummary();
                biSummaryObj.setOfferName(bi.getOfferName());
                biSummaryObj.setOfferSFDCId(bi.getOfferSFDCId());
                biSummaryObj.setRetalGross(bi.getRetalGross());
                biSummaryObj.setCount(1);
                biSummaryObj.setBillItem(bi);
                groupedBIMap.put(biSummaryObj.getOfferSFDCId(),biSummaryObj);

        }else{
                BillItemSummary existingBISummaryObj = groupedBIMap.get(bi.getOfferSFDCId());
                Double  newRetGrossAmount = Double.valueOf(existingBISummaryObj.getRetalGross()) + Double.valueOf(bi.getRetalGross());
                existingBISummaryObj.setRetalGross(String.valueOf(newRetGrossAmount));
                existingBISummaryObj.setCount(existingBISummaryObj.getCount()+1);
                groupedBIMap.put(bi.getOfferSFDCId(),existingBISummaryObj);

            }
            
        }
        
        for (BillItemSummary billItemSummary : groupedBIMap.values()) {
            if (retBIMap.containsKey(billItemSummary.getBillItem().getAccountNumber() + "," + billItemSummary.getBillItem().getRequireServItemisation() + "," + billItemSummary.getBillItem().getRequireTelItemisation() + "," + billItemSummary.getBillItem().getAccountName())) {
                retBIMap.get(billItemSummary.getBillItem().getAccountNumber() + "," + billItemSummary.getBillItem().getRequireServItemisation() + "," + billItemSummary.getBillItem().getRequireTelItemisation() + "," + billItemSummary.getBillItem().getAccountName()).add(billItemSummary);
            } else {
                retBIMap.put(billItemSummary.getBillItem().getAccountNumber() + "," + billItemSummary.getBillItem().getRequireServItemisation() + "," + billItemSummary.getBillItem().getRequireTelItemisation() + "," + billItemSummary.getBillItem().getAccountName(), new ArrayList<BillItemSummary>());
                retBIMap.get(billItemSummary.getBillItem().getAccountNumber() + "," + billItemSummary.getBillItem().getRequireServItemisation() + "," + billItemSummary.getBillItem().getRequireTelItemisation() + "," + billItemSummary.getBillItem().getAccountName()).add(billItemSummary);
            }
        }
        
        
        return retBIMap;
    }
    
    public void buildItemisation(Set<String> bItemKeys, String billDate,HashMap<String, String> accountAuthorizedEmailMap, HashMap<String, ArrayList<BillItem>> biItemMap, String runId, HashMap<String,String> invoiceMap) throws Exception {
        
        HashMap<String, ArrayList<BillItemSummary>> biSummaryItemMap = new HashMap<String, ArrayList<BillItemSummary>>();
        
        ArrayList<BillItem> biList = new  ArrayList<BillItem>();
        
        for(ArrayList<BillItem> bil : biItemMap.values()){
            biList.addAll(bil);
        }
        
        biSummaryItemMap = getBillItemSummaryMap(biList);
        
        for (String keySet : bItemKeys) {
            explodedValues = keySet.split(",");
            AccountNumber = explodedValues[IDX_ACCOUNT_NUMBER];
            RequireTelephony = explodedValues[IDX_REQUIRE_TEL];
            RequireService = explodedValues[IDX_REQUIRE_SERV];
            AccountName = explodedValues[IDX_ACCOUNT_NAME];
            invoiceNumber = invoiceMap.get(AccountNumber);
            ratedCdrs = rch.getEvents(AccountNumber, Date.valueOf(billDate));
            LOGGER.info("ratedCdrs queried");
            callReportObject = rch.getCallReport(AccountNumber, Date.valueOf(billDate));
            for (Object eachReport : callReportObject) {
                Object[] row = (Object[]) eachReport;
                callReport.add(new CallReport(Integer.valueOf(row[IDX_COUNT].toString()), row[IDX_DESTINATION].toString(), Double.valueOf(row[IDX_COST].toString())));
            }
            LOGGER.info("Total number of Rated records queried for AccountNumber " + keySet + " is : " + ratedCdrs.size());
            rCdrPack.put(AccountNumber, ratedCdrs);
            servicePack.put(AccountNumber, callReport);
            itemisation.setItemisation(AccountNumber, AccountName, RequireTelephony, RequireService, rCdrPack.get(AccountNumber), servicePack.get(AccountNumber), biItemMap.get(keySet));
            itemisation.setEmailAddress(accountAuthorizedEmailMap.get(AccountNumber));
            itemisation.setBiSummary(biSummaryItemMap.get(keySet));
            createPDF(itemisation, runId, invoiceNumber);

        }
    }

    private String getCommaSeparatedString(Set<String> accountNumberSet) {

        String retString = "";

        for (String s : accountNumberSet) {
            retString += "'" + s + "',";
        }

        if (retString.endsWith(",")) {
            retString = retString.substring(0, retString.lastIndexOf(","));
        }

        return retString;
    }

    Set<String> getAccountNumbers(Set<String> bItemKeys) {
        
        for (String keySet : bItemKeys) {
                explodedValues = keySet.split(",");
                accountNumbers.add(explodedValues[IDX_ACCOUNT_NUMBER]);
            }
        return accountNumbers;
    }

    void closeConnections() throws SQLException {
        LOGGER.info("Closing Hibernate connection");
        rch.closeSession();
    }
    
    public static String getBillPeriod() throws ParseException{
        
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        
        java.util.Date d = (java.util.Date)formatter.parse(ReportsHelper.billDateVal);
        
        DateTime dt = new DateTime(d);
        
        MutableDateTime mdt = new MutableDateTime(dt);
        mdt.addMonths(-1);
        mdt.setDayOfMonth(mdt.dayOfMonth().getMaximumValue());
        mdt.setMillisOfDay(mdt.millisOfDay().getMaximumValue());
        
        return  mdt.getDayOfMonth() + "-" + mdt.getMonthOfYear() + "-" + mdt.getYear();  
        
    }
    
     public static String getBillDate() throws ParseException{
        
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        
        java.util.Date d = (java.util.Date)formatter.parse(ReportsHelper.billDateVal);
        
        DateTime dt = new DateTime(d);
        
        MutableDateTime mdt = new MutableDateTime(dt);
        //mdt.addMonths(-1);
        //mdt.setDayOfMonth(mdt.dayOfMonth().getMaximumValue());
        //mdt.setMillisOfDay(mdt.millisOfDay().getMaximumValue());
        
        return  mdt.getDayOfMonth() + "-" + mdt.getMonthOfYear() + "-" + mdt.getYear();  
        
    }
    
    
    private String getFormatedDate(String inpDate) throws ParseException{
        
        if(!isBlank(inpDate)){
            SimpleDateFormat dt = new SimpleDateFormat("yyyyy-mm-dd"); 
            java.util.Date date = dt.parse(inpDate); 
            SimpleDateFormat dt1 = new SimpleDateFormat("dd/mm/yyyy");
            return dt1.format(date);
        }
        
        return "";
    }
    
    public HashMap<String,String> generateInvoiceNumbers(Set<String> accNumbers) throws ParseException{
        
        HashMap<String,String> retMap = new HashMap<String,String>();
        ArrayList<InvoiceNumber> invoiceList = rch.getInvoiceNumbers(accNumbers);
        
        for(InvoiceNumber in : invoiceList){
            if(isValidInvoiceMonth(in.getGeneratedOn())){
                retMap.put(in.getTier1().toString(), in.getInvoiceNumber());
            }
        }
        
        return retMap;
        
    }
    
    private Boolean isValidInvoiceMonth(java.util.Date invoiceGeneratedDate) throws ParseException{
        
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");        
        java.util.Date d = (java.util.Date)formatter.parse(ReportsHelper.billDateVal);
        
        DateTime dt = new DateTime(d);                  
        DateTime dtInv = new DateTime(invoiceGeneratedDate);        
               
        if(dt.getMonthOfYear() == dtInv.getMonthOfYear() && dt.getYear() == dtInv.getYear()){
            return true;
        }
        return false;
    }
    
    public static String getCurrencyValue(Double val){
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.UK);
        return nf.format(val);
    }
    
    public static String getDateFromString(String inputDate){
               
        try{
            String dateStr = inputDate.substring(0,10);
            String[] dateStrArr = dateStr.split("-");
            return dateStrArr[2] + "/" + dateStrArr[1] + "/" + dateStrArr[0];
        }catch(Exception e){
            return inputDate;
        }
    }
    
     public static String getTimeFromString(String inputDate){
        
        try{
            String dateStr = inputDate.substring(10,19);
            return dateStr;
        }catch(Exception e){
            return inputDate;
        }
    }
}
