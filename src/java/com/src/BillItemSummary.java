/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.src;

import com.bean.BillItem;

/**
 *
 * @author Vikram Middha
 */
public class BillItemSummary {
    
    private String offerName = null;
    private String offerSFDCId = null;
    private Integer count = 1;
    private String retalGross = "";
    private BillItem billItem = null;

    public BillItem getBillItem() {
        return billItem;
    }

    public void setBillItem(BillItem billItem) {
        this.billItem = billItem;
    }
    
    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
    

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getOfferSFDCId() {
        return offerSFDCId;
    }

    public void setOfferSFDCId(String offerSFDCId) {
        this.offerSFDCId = offerSFDCId;
    }    
    
    public String getRetalGross() {
        return retalGross;
    }
    public void setRetalGross(String retalGross) {
        this.retalGross = retalGross;
    }
}
