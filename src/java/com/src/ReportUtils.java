package com.src;

import Common.src.com.Config.AppConfig;
import Common.src.com.Config.Configurator;
import Common.src.com.Exception.ResilientException;
import com.bean.BillItem;
import com.bean.CallReport;
import com.bean.Itemisation;
import com.bean.RatedCdr;
import ewsconnect.EWSConnection;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import org.apache.log4j.Logger;

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
        
        public ReportUtils(){
        try {
            appConfig = Configurator.getAppConfig();;
        } catch (ResilientException ex) {
            java.util.logging.Logger.getLogger(ReportUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
        /*prepares the select query by reading the fields from Resilient.properties file.*/
	public String getQuery( AppConfig appConfig){
		
            return "SELECT " + appConfig.getSfdcBillToItemQueryFields() ;
		
	}
	
        /*Makes sure that the field is not blank.*/
	public Boolean isNotBlank(String val){
            LOGGER.info("Entered in isNotBlank . Val : " + val );
            if(null != val && !"".equals(val.trim())){
                    return true;
            }
            return false;
	}
	
        /*Makes sure that the field is blank.*/
	public Boolean isBlank(String val){
		
            if(null == val || "".equals(val.trim())){
                    return true;
            }
            return false;
	}
	
        /*Adds the where clause in the query.*/
	public String addWhereClause(String key, String value, String query){
		
            if(query.toUpperCase().contains("WHERE")){
                query += " AND " + key + "='" + value + "'";  
            }else{
                query += " WHERE " + key + "='" + value + "'";
            }
		
            return query;
	}
        
         /*Adds the where clause in the query.*/
        public String addWhereClause(String key, Integer value, String query){
		
            LOGGER.info("In Method addWhereClause. Key :" + key + ",value : " + value + ", query " + query);

            if(query.toUpperCase().contains("WHERE")){
                query += " AND " + key + "=" + value;  
            }else{
                query += " WHERE " + key + "=" + value;  
            }

            return query;
	}
        
        /*Adds the where clause in the query.*/
        public String addWhereClause(String key, Boolean value, String query){
		
            LOGGER.info("In Method addWhereClause. Key :" + key + ",value : " + value + ", query " + query);

            if(query.toUpperCase().contains("WHERE")){
                query += " AND " + key + "=" + value;  
            }else{
                query += " WHERE " + key + "=" + value;  
            }

            return query;
	}
        
        /*The below method get the latest Bill Id for the given Account number from SFDC.*/
        
        public String getLatestBillId(String accountNumber, QuerySFDC querySFDC) throws ResilientException{
            
            String query = "Select Name from Espresso_Bill__Bill__c ";
            query = addWhereClause("ESPRESSO_BILL__ACCOUNT__R.ESPRESSO_PC__ACCOUNT_NUMBER__C", accountNumber, query);
            query += " ORDER BY NAME DESC LIMIT 1";
            
            HashMap<String,Object>[] resultMap = querySFDC.executeQuery(query);
            
            if(resultMap.length != 1)
                return null;
            
            return (String)resultMap[0].get("NAME");
            
        }
	
	/*populate the data queried from SFDC into List of BillItem objects.*/
	public HashMap<String,ArrayList<BillItem>> populateBillItemBeans(String query, QuerySFDC querySFDC) throws ResilientException{
		
            ArrayList<BillItem> retBIList = new ArrayList<BillItem>();
            HashMap<String,ArrayList<BillItem>> retBIMap = new HashMap<String,ArrayList<BillItem>>();
            try{
                HashMap<String,Object>[] resultMap = querySFDC.executeQuery(query);

                for(int i=0; i<resultMap.length; i++){

                    HashMap<String,Object> hm = resultMap[i];
                    BillItem biObj = new BillItem();

                    biObj.setAssetName((String)hm.get("ESPRESSO_BILL__ASSET__R.NAME"));
                    biObj.setBillDate((String)hm.get("ESPRESSO_BILL__BILL__R.ESPRESSO_BILL__BILL_DATE__C"));
                    //biObj.setBillPeriod((String)hm.get("Espresso_Bill__Asset__R.Name"));
                    biObj.setDateFrom((String)hm.get("ESPRESSO_BILL__DATE_FROM__C"));
                    biObj.setDateTo((String)hm.get("ESPRESSO_BILL__BILL_TO__C"));
                    biObj.setRetalGross((String)hm.get("ESPRESSO_BILL__GROSS_AMOUNT_1__C"));
                    biObj.setAccountNumber((String)hm.get("ESPRESSO_BILL__BILL__R.ESPRESSO_BILL__ACCOUNT__R.ESPRESSO_PC__ACCOUNT_NUMBER__C"));
                    biObj.setRequireServItemisation((((String)hm.get("ESPRESSO_BILL__BILL__R.ESPRESSO_BILL__ACCOUNT__R.SUMMARY_ITEMISATION_REQUIRED__C")).equalsIgnoreCase("true")) ? true : false);
                    biObj.setRequireTelItemisation((((String)hm.get("ESPRESSO_BILL__BILL__R.ESPRESSO_BILL__ACCOUNT__R.TEL_ITEMISATION_REQUIRED__C")).equalsIgnoreCase("true")) ? true : false);

                    retBIList.add(biObj);
                }
                for(BillItem billItem : retBIList){
                    if(retBIMap.containsKey(billItem.getAccountNumber()+","+billItem.getRequireServItemisation()+","+billItem.getRequireTelItemisation())){
                        retBIMap.get(billItem.getAccountNumber()+","+billItem.getRequireServItemisation()+","+billItem.getRequireTelItemisation()).add(billItem);
                    }else{
                        retBIMap.put(billItem.getAccountNumber()+","+billItem.getRequireServItemisation()+","+billItem.getRequireTelItemisation(), new ArrayList<BillItem>());
                        retBIMap.get(billItem.getAccountNumber()+","+billItem.getRequireServItemisation()+","+billItem.getRequireTelItemisation()).add(billItem);
                    }
                }
            }catch(Exception e){
                LOGGER.error("Error occured while query SFDC for BillItems. Cause : " + e.getStackTrace());
                throw new ResilientException(e.getMessage());
            }
            
            return retBIMap;
	}
        
        /*Creation of PDF can be handled here*/
        public boolean createPDF(ArrayList <Itemisation> itemisations, String runId) throws Exception{
            EWSConnection ewsObj = new EWSConnection();
            PDFCreator pdfCreate = new PDFCreator(itemisations,runId);
            //File file = new File(appConfig.getPdfCreateDirectory());
            //if(pdfCreate.valid){
            //    ewsObj.processFolder(file);
            //}
            return true;
        }
        
        /*Below function returns the map of Accountnumber and authorized contact email address.*/
       
        public HashMap<String,String> getAccountAuthorizedEmailMap(Set<String> accountNumberSet, QuerySFDC querySFDC) throws ResilientException{
            
            HashMap<String,String> retMap = new HashMap<String, String>();
            
            String accountNumberExp = getCommaSeparatedString(accountNumberSet);
            
            if(accountNumberExp.length() <=0){
                return retMap;
            }                
            
            String query = "Select EMAIL,Account.Espresso_PC__Account_Number__c from Contact where Authorised_Contact__c = true and "
                    + "Account.Espresso_PC__Account_Number__c IN (" + accountNumberExp +")";
            LOGGER.info("Query String to get Email Addresses : " + query);          
            try{
                 HashMap<String,Object>[] resultMap = querySFDC.executeQuery(query);
                 
                 if(resultMap.length > 0){
                    for(int i=0; i<resultMap.length; i++){
                        HashMap<String,Object> hm = resultMap[i];
                        retMap.put((String)hm.get("ACCOUNT.ESPRESSO_PC__ACCOUNT_NUMBER__C"), (String)hm.get("EMAIL"));
                     }
                 }
                 
            LOGGER.info("Total number of email adddresses retrieved : " + retMap.size());     
            }catch(Exception e){
                LOGGER.error("Error occured while query SFDC for Authorized contact email address. Cause : " + e.getMessage());
                throw new ResilientException(e.getMessage());
            }
            
            return retMap;
        }
        
        private String getCommaSeparatedString(Set<String> accountNumberSet){
            
            String retString = "";
            
            for(String s : accountNumberSet ){
                retString += "'" + s + "',";
            }
            
            if(retString.endsWith(",")){
                retString = retString.substring(0,retString.lastIndexOf(","));
            }
            
            return retString;
        }

}
