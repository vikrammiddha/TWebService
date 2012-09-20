/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bean;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

/**
 *
 * @author Vikram Middha
 */
public class InvoiceNumber implements java.io.Serializable {
    
    private int InvoiceId;
    private String InvoiceNumber;
    private String Tier1;
    private Date GeneratedOn;
    private int GeneratedBy;

    public int getInvoiceId() {
        return InvoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.InvoiceId = invoiceId;
    }

    public String getInvoiceNumber() {
        return InvoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.InvoiceNumber = invoiceNumber;
    }

    public String getTier1() {
        return Tier1;
    }

    public void setTier1(String Tier1) {
        this.Tier1 = Tier1;
    }

    public Date getGeneratedOn() {
        return GeneratedOn;
    }

    public void setGeneratedOn(Date GeneratedOn) {
        this.GeneratedOn = GeneratedOn;
    }

    public int getGeneratedBy() {
        return GeneratedBy;
    }

    public void setGeneratedBy(int GeneratedBy) {
        this.GeneratedBy = GeneratedBy;
    }
    
}
