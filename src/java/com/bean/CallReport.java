/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bean;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

/**
 *
 * @author Nimil
 */
public class CallReport {

    private int count;
    private String zoneDestination;
    private Double retailPrice;
    private NumberFormat formatter;
    public CallReport(int count, String zoneDestination, Double retailPrice) {
        this.count = count;
        this.zoneDestination = zoneDestination;
        this.formatter = new DecimalFormat("#0.00");
        this.retailPrice = Double.valueOf(formatter.format(retailPrice));
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getZoneDestination() {
        return this.zoneDestination;
    }

    public void setZoneDestination(String zoneDestination) {
        this.zoneDestination = zoneDestination;
    }

    public Double getRetailPrice() {
        return this.retailPrice;
    }

    public void setRetailPrice(Double retailPrice) {
        this.retailPrice = retailPrice;
    }
}
