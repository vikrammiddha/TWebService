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
    private HashMap<String,ArrayList<RatedCdr>> rCdrs;
    private ArrayList<BillItem> bItems;
    private ArrayList<Summary> summary;
    
    public Itemisation(HashMap<String,ArrayList<RatedCdr>> rCdrs, ArrayList<BillItem> bItems, ArrayList<Summary> summary){
        this.rCdrs = rCdrs;
        this.bItems = bItems;
        this.summary = summary;
    }
    
    
    public HashMap<String,ArrayList<RatedCdr>> getRatedCdrs(){
        return this.rCdrs;
    }
    
    public ArrayList<BillItem> getBillItems(){
        return this.bItems;
    }
    
    public ArrayList<Summary> getSummary(){
        return this.summary;
    }
    
}
