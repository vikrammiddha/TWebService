package com.bean;
// Generated Jul 26, 2012 11:20:49 AM by Hibernate Tools 3.2.1.GA


import java.util.Date;

/**
 * RatedCdr generated by hbm2java
 */
public class RatedCdr  implements java.io.Serializable {


     private int id;
     private String cdrGuid;
     private String type;
     private String service;
     private String direction;
     private String network;
     private String remoteNetwork;
     private String remoteSwitch;
     private String portingPrefix;
     private Date startTimestamp;
     private String user;
     private String destination;
     private String caller;
     private double duration;
     private double retailPrice;
     private double wholesalePrice;
     private double localPolo;
     private double localRolo;
     private double localTransit;
     private double remotePolo;
     private double remoteRolo;
     private double remoteTransit;
     private String reseller;
     private String financialCode;
     private String billId;
     private String aggregationId;
     private String billStatus;
     private int state;
     private String callRef;
     private String numberType;
     private String resellerCode;
     private String wholesaleDescription;
     private String retailDescription;
     private String localcostDescription;
     private String remotecostDescription;
     private String localtransitDescription;
     private String remotetransitDescription;
     private String platform;
     private String timeBand;
     private String msn;
     private String zoneDestination;
     private String billRunId;

    public RatedCdr() {
    }

	
    public RatedCdr(int id, String type, String service, String direction, String network, String remoteNetwork, String remoteSwitch, String portingPrefix, Date startTimestamp, String user, String destination, String caller, double duration, double retailPrice, double wholesalePrice, double localPolo, double localRolo, double localTransit, double remotePolo, double remoteRolo, double remoteTransit, String financialCode, String aggregationId, String billStatus, int state, String callRef) {
        this.id = id;
        this.type = type;
        this.service = service;
        this.direction = direction;
        this.network = network;
        this.remoteNetwork = remoteNetwork;
        this.remoteSwitch = remoteSwitch;
        this.portingPrefix = portingPrefix;
        this.startTimestamp = startTimestamp;
        this.user = user;
        this.destination = destination;
        this.caller = caller;
        this.duration = duration;
        this.retailPrice = retailPrice;
        this.wholesalePrice = wholesalePrice;
        this.localPolo = localPolo;
        this.localRolo = localRolo;
        this.localTransit = localTransit;
        this.remotePolo = remotePolo;
        this.remoteRolo = remoteRolo;
        this.remoteTransit = remoteTransit;
        this.financialCode = financialCode;
        this.aggregationId = aggregationId;
        this.billStatus = billStatus;
        this.state = state;
        this.callRef = callRef;
    }
    public RatedCdr(int id, String cdrGuid, String type, String service, String direction, String network, String remoteNetwork, String remoteSwitch, String portingPrefix, Date startTimestamp, String user, String destination, String caller, double duration, double retailPrice, double wholesalePrice, double localPolo, double localRolo, double localTransit, double remotePolo, double remoteRolo, double remoteTransit, String reseller, String financialCode, String billId, String aggregationId, String billStatus, int state, String callRef, String numberType, String resellerCode, String wholesaleDescription, String retailDescription, String localcostDescription, String remotecostDescription, String localtransitDescription, String remotetransitDescription, String platform, String timeBand, String msn, String zoneDestination, String billRunId) {
       this.id = id;
       this.cdrGuid = cdrGuid;
       this.type = type;
       this.service = service;
       this.direction = direction;
       this.network = network;
       this.remoteNetwork = remoteNetwork;
       this.remoteSwitch = remoteSwitch;
       this.portingPrefix = portingPrefix;
       this.startTimestamp = startTimestamp;
       this.user = user;
       this.destination = destination;
       this.caller = caller;
       this.duration = duration;
       this.retailPrice = retailPrice;
       this.wholesalePrice = wholesalePrice;
       this.localPolo = localPolo;
       this.localRolo = localRolo;
       this.localTransit = localTransit;
       this.remotePolo = remotePolo;
       this.remoteRolo = remoteRolo;
       this.remoteTransit = remoteTransit;
       this.reseller = reseller;
       this.financialCode = financialCode;
       this.billId = billId;
       this.aggregationId = aggregationId;
       this.billStatus = billStatus;
       this.state = state;
       this.callRef = callRef;
       this.numberType = numberType;
       this.resellerCode = resellerCode;
       this.wholesaleDescription = wholesaleDescription;
       this.retailDescription = retailDescription;
       this.localcostDescription = localcostDescription;
       this.remotecostDescription = remotecostDescription;
       this.localtransitDescription = localtransitDescription;
       this.remotetransitDescription = remotetransitDescription;
       this.platform = platform;
       this.timeBand = timeBand;
       this.msn = msn;
       this.zoneDestination = zoneDestination;
       this.billRunId = billRunId;
    }
   
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    public String getCdrGuid() {
        return this.cdrGuid;
    }
    
    public void setCdrGuid(String cdrGuid) {
        this.cdrGuid = cdrGuid;
    }
    public String getType() {
        return this.type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    public String getService() {
        return this.service;
    }
    
    public void setService(String service) {
        this.service = service;
    }
    public String getDirection() {
        return this.direction;
    }
    
    public void setDirection(String direction) {
        this.direction = direction;
    }
    public String getNetwork() {
        return this.network;
    }
    
    public void setNetwork(String network) {
        this.network = network;
    }
    public String getRemoteNetwork() {
        return this.remoteNetwork;
    }
    
    public void setRemoteNetwork(String remoteNetwork) {
        this.remoteNetwork = remoteNetwork;
    }
    public String getRemoteSwitch() {
        return this.remoteSwitch;
    }
    
    public void setRemoteSwitch(String remoteSwitch) {
        this.remoteSwitch = remoteSwitch;
    }
    public String getPortingPrefix() {
        return this.portingPrefix;
    }
    
    public void setPortingPrefix(String portingPrefix) {
        this.portingPrefix = portingPrefix;
    }
    public Date getStartTimestamp() {
        return this.startTimestamp;
    }
    
    public void setStartTimestamp(Date startTimestamp) {
        this.startTimestamp = startTimestamp;
    }
    public String getUser() {
        return this.user;
    }
    
    public void setUser(String user) {
        this.user = user;
    }
    public String getDestination() {
        return this.destination;
    }
    
    public void setDestination(String destination) {
        this.destination = destination;
    }
    public String getCaller() {
        return this.caller;
    }
    
    public void setCaller(String caller) {
        this.caller = caller;
    }
    public double getDuration() {
        return this.duration;
    }
    
    public void setDuration(double duration) {
        this.duration = duration;
    }
    public double getRetailPrice() {
        return this.retailPrice;
    }
    
    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }
    public double getWholesalePrice() {
        return this.wholesalePrice;
    }
    
    public void setWholesalePrice(double wholesalePrice) {
        this.wholesalePrice = wholesalePrice;
    }
    public double getLocalPolo() {
        return this.localPolo;
    }
    
    public void setLocalPolo(double localPolo) {
        this.localPolo = localPolo;
    }
    public double getLocalRolo() {
        return this.localRolo;
    }
    
    public void setLocalRolo(double localRolo) {
        this.localRolo = localRolo;
    }
    public double getLocalTransit() {
        return this.localTransit;
    }
    
    public void setLocalTransit(double localTransit) {
        this.localTransit = localTransit;
    }
    public double getRemotePolo() {
        return this.remotePolo;
    }
    
    public void setRemotePolo(double remotePolo) {
        this.remotePolo = remotePolo;
    }
    public double getRemoteRolo() {
        return this.remoteRolo;
    }
    
    public void setRemoteRolo(double remoteRolo) {
        this.remoteRolo = remoteRolo;
    }
    public double getRemoteTransit() {
        return this.remoteTransit;
    }
    
    public void setRemoteTransit(double remoteTransit) {
        this.remoteTransit = remoteTransit;
    }
    public String getReseller() {
        return this.reseller;
    }
    
    public void setReseller(String reseller) {
        this.reseller = reseller;
    }
    public String getFinancialCode() {
        return this.financialCode;
    }
    
    public void setFinancialCode(String financialCode) {
        this.financialCode = financialCode;
    }
    public String getBillId() {
        return this.billId;
    }
    
    public void setBillId(String billId) {
        this.billId = billId;
    }
    public String getAggregationId() {
        return this.aggregationId;
    }
    
    public void setAggregationId(String aggregationId) {
        this.aggregationId = aggregationId;
    }
    public String getBillStatus() {
        return this.billStatus;
    }
    
    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }
    public int getState() {
        return this.state;
    }
    
    public void setState(int state) {
        this.state = state;
    }
    public String getCallRef() {
        return this.callRef;
    }
    
    public void setCallRef(String callRef) {
        this.callRef = callRef;
    }
    public String getNumberType() {
        return this.numberType;
    }
    
    public void setNumberType(String numberType) {
        this.numberType = numberType;
    }
    public String getResellerCode() {
        return this.resellerCode;
    }
    
    public void setResellerCode(String resellerCode) {
        this.resellerCode = resellerCode;
    }
    public String getWholesaleDescription() {
        return this.wholesaleDescription;
    }
    
    public void setWholesaleDescription(String wholesaleDescription) {
        this.wholesaleDescription = wholesaleDescription;
    }
    public String getRetailDescription() {
        return this.retailDescription;
    }
    
    public void setRetailDescription(String retailDescription) {
        this.retailDescription = retailDescription;
    }
    public String getLocalcostDescription() {
        return this.localcostDescription;
    }
    
    public void setLocalcostDescription(String localcostDescription) {
        this.localcostDescription = localcostDescription;
    }
    public String getRemotecostDescription() {
        return this.remotecostDescription;
    }
    
    public void setRemotecostDescription(String remotecostDescription) {
        this.remotecostDescription = remotecostDescription;
    }
    public String getLocaltransitDescription() {
        return this.localtransitDescription;
    }
    
    public void setLocaltransitDescription(String localtransitDescription) {
        this.localtransitDescription = localtransitDescription;
    }
    public String getRemotetransitDescription() {
        return this.remotetransitDescription;
    }
    
    public void setRemotetransitDescription(String remotetransitDescription) {
        this.remotetransitDescription = remotetransitDescription;
    }
    public String getPlatform() {
        return this.platform;
    }
    
    public void setPlatform(String platform) {
        this.platform = platform;
    }
    public String getTimeBand() {
        return this.timeBand;
    }
    
    public void setTimeBand(String timeBand) {
        this.timeBand = timeBand;
    }
    public String getMsn() {
        return this.msn;
    }
    
    public void setMsn(String msn) {
        this.msn = msn;
    }
    public String getZoneDestination() {
        return this.zoneDestination;
    }
    
    public void setZoneDestination(String zoneDestination) {
        this.zoneDestination = zoneDestination;
    }
    public String getBillRunId() {
        return this.billRunId;
    }
    
    public void setBillRunId(String billRunId) {
        this.billRunId = billRunId;
    }




}

