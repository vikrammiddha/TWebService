/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bean;

/**
 *
 * @author Nimil
 */
public class CallReport {

    private int count;
    private String zoneDestination;
    private Double retailPrice;

    public CallReport(int count, String zoneDestination, Double retailPrice) {
        this.count = count;
        this.zoneDestination = zoneDestination;
        this.retailPrice = retailPrice;
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
