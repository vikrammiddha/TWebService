package com.src;

import Common.src.com.Config.AppConfig;
import Common.src.com.Exception.ResilientException;
import com.bean.BillItem;
import com.bean.Itemisation;
import java.util.ArrayList;
import java.util.HashMap;
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
	public ArrayList<BillItem> populateBillItemBeans(String query, QuerySFDC querySFDC){
		
            ArrayList<BillItem> retBIList = new ArrayList<BillItem>();

            try{
                HashMap<String,Object>[] resultMap = querySFDC.executeQuery(query);

                for(int i=0; i<resultMap.length; i++){

                    HashMap<String,Object> hm = resultMap[i];
                    BillItem biObj = new BillItem();

                    biObj.setAssetName((String)hm.get("ESPRESSO_BILL__ASSET__R.NAME"));
                    biObj.setBillDate((String)hm.get("ESPRESSO_BILL__BILL__R.Espresso_Bill__Bill_Date__c"));
                    //biObj.setBillPeriod((String)hm.get("Espresso_Bill__Asset__R.Name"));
                    biObj.setDateFrom((String)hm.get("Espresso_Bill__Bill_From__c"));
                    biObj.setDateTo((String)hm.get("Espresso_Bill__Bill_To__c"));
                    biObj.setRetalGross((String)hm.get("ESPRESSO_BILL__GROSS_AMOUNT_1__C"));
                    biObj.setAccountNumber((String)hm.get("ESPRESSO_BILL__BILL__R.ESPRESSO_BILL__ACCOUNT__R.ESPRESSO_PC__ACCOUNT_NUMBER__C"));

                    retBIList.add(biObj);
                }

            }catch(Exception e){
                LOGGER.error("Error occured while query SFDC for BillItems. Cause : " + e.getStackTrace());
            }

            return retBIList;
	}
        
        /*Creation of PDF can be handled here*/
        public boolean createPDF(ArrayList <Itemisation> itemisations){
            return true;
        }
}
