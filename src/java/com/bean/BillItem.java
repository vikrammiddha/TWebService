package com.bean;

/* 
########################################################################### 
# File..................: CustomLogger.java
# Version...............: 1.0
# Created by............: Vikram Middha
# Created Date..........: 27-Jul-2012
# Last Modified by......: 
# Last Modified Date....: 
# Description...........: Bean class for storing data coming from SFDC.
# Change Request History: 				   							 
########################################################################### 
*/

public class BillItem {

    private String billPeriod = "";
    private String assetName = "";
    private String dateFrom = "";
    private String dateTo = "";
    private String retalGross = "";
    private String billDate = "";
    private String accountNumber = "";
    private Boolean requireTelItemisation = null;
    private Boolean requireServItemisation = null;

    public String getBillPeriod() {
        return billPeriod;
    }
    public void setBillPeriod(String billPeriod) {
        this.billPeriod = billPeriod;
    }
    public String getAssetName() {
        return assetName;
    }
    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }
    public String getDateFrom() {
        return dateFrom;
    }
    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }
    public String getDateTo() {
        return dateTo;
    }
    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }
    public String getRetalGross() {
        return retalGross;
    }
    public void setRetalGross(String retalGross) {
        this.retalGross = retalGross;
    }
    public String getBillDate() {
        return billDate;
    }
    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }
    public String getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    public Boolean getRequireTelItemisation() {
        return requireTelItemisation;
    }
    public void setRequireTelItemisation(Boolean requireTelItemisation) {
        this.requireTelItemisation = requireTelItemisation;
    }
    public Boolean getRequireServItemisation() {
        return requireServItemisation;
    }
    public void setRequireServItemisation(Boolean requireServItemisation) {
        this.requireServItemisation = requireServItemisation;
    }
	
}
