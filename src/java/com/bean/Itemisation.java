/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bean;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Nimil
 */
public class Itemisation {
    String AccountNumber;
    Boolean RequireTelephony;
    Boolean RequireService;
    private HashMap<String,ArrayList<RatedCdr>> rCdrs;
    private ArrayList<BillItem> bItems;
    HashMap<String, ArrayList<Object>> summary;
    
    public Itemisation(String AccountNumber, String RequireTelephony, String RequireService,HashMap<String,ArrayList<RatedCdr>> rCdrs,HashMap<String, ArrayList<Object>> summary, ArrayList<BillItem> bItems){
        this.AccountNumber = AccountNumber;
        this.rCdrs = rCdrs;
        this.bItems = bItems;
        this.RequireTelephony = ((RequireTelephony.equalsIgnoreCase("true")) ? true : false);
        this.RequireService = ((RequireService.equalsIgnoreCase("true")) ? true : false);
        this.summary = summary;
    }
    
    
    public HashMap<String,ArrayList<RatedCdr>> getRatedCdrs(){
        return this.rCdrs;
    }
    
    public ArrayList<BillItem> getBillItems(){
        return this.bItems;
    }
    
    public String getAccountNumber(){
        return this.AccountNumber;
    }
    
    public Boolean getRequireTelephony(){
        return this.RequireTelephony;
    }
    
    
    public Boolean getRequireService(){
        return this.RequireService;
    }
    
    public HashMap<String, ArrayList<Object>> getSummary(){
        return this.summary;
    }
    
}
