package Common.src.com.Config;

/* 
########################################################################### 
# File..................: AppConfig.java
# Version...............: 1.0
# Created by............: Vikram Middha
# Created Date..........: 27-Jul-2012
# Last Modified by......: 
# Last Modified Date....: 
# Description...........: This is a bean class for Resilient.properties file.
*                        For each entry in properties file, there should be a 
*                       field in this class and getter/setter for the same.
# Change Request History: 				   							 
########################################################################### 
*/

public final class AppConfig {
	
    // SFDC
    private String sfdcEndpoint;
    private String sfdcUsername;
    private String sfdcPassword;
    private String sfdcBillToItemQueryFields;
    private String reportsDirectory;    
    private String userName;
    private String password;
    private String uri;
    private String errorSuccessEmailAddress;
    private String errorSubject;
    private String successSubject;
    private String pdfCreateDirectory;
    private String pdfGetResource;
    private String pdfArchiveDir;
    private String pdfErrorDir;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String smtphostname;
    private String adminUserId;
    private String adminPassword;
    private String fromAlias;
    private String replytoaddress;
    private String smtpport;
    private String toAddressList;
    private String mailSubject;
    private String mailBody;
    private String attachmentPath;

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    public String getSmtphostname() {
        return smtphostname;
    }

    public void setSmtphostname(String smtphostname) {
        this.smtphostname = smtphostname;
    }

    public String getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(String adminUserId) {
        this.adminUserId = adminUserId;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getFromAlias() {
        return fromAlias;
    }

    public void setFromAlias(String fromAlias) {
        this.fromAlias = fromAlias;
    }

    public String getReplytoaddress() {
        return replytoaddress;
    }

    public void setReplytoaddress(String replytoaddress) {
        this.replytoaddress = replytoaddress;
    }

    public String getSmtpport() {
        return smtpport;
    }

    public void setSmtpport(String smtpport) {
        this.smtpport = smtpport;
    }

    public String getToAddressList() {
        return toAddressList;
    }

    public void setToAddressList(String toAddressList) {
        this.toAddressList = toAddressList;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public String getMailBody() {
        return mailBody;
    }

    public void setMailBody(String mailBody) {
        this.mailBody = mailBody;
    }

    public String getAttachmentPath() {
        return attachmentPath;
    }

    public void setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
    }  

    public String getSuccessSubject() {
        return successSubject;
    }

    public void setSuccessSubject(String successSubject) {
        this.successSubject = successSubject;
    }

    public String getErrorSubject() {
        return errorSubject;
    }

    public void setErrorSubject(String errorSubject) {
        this.errorSubject = errorSubject;
    }
   

    public String getErrorSuccessEmailAddress() {
        return errorSuccessEmailAddress;
    }

    public void setErrorSuccessEmailAddress(String errorSuccessEmailAddress) {
        this.errorSuccessEmailAddress = errorSuccessEmailAddress;
    }
    
    public String getSfdcEndpoint() {
        return sfdcEndpoint;
    }
    public void setSfdcEndpoint(String sfdcEndpoint) {
        this.sfdcEndpoint = sfdcEndpoint;
    }
    
    public String getSfdcUsername() {
        return sfdcUsername;
    }
    public void setSfdcUsername(String sfdcUsername) {
        this.sfdcUsername = sfdcUsername;
    }
    
    public String getSfdcPassword() {
        return sfdcPassword;
    }
    public void setSfdcPassword(String sfdcPassword) {
        this.sfdcPassword = sfdcPassword;
    }
   
    public String getSfdcBillToItemQueryFields() {
            return sfdcBillToItemQueryFields;
    }
    public void setSfdcBillToItemQueryFields(String sfdcBillToItemQueryFields) {
            this.sfdcBillToItemQueryFields = sfdcBillToItemQueryFields;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
        
    public String getReportsDirectory() {
        return reportsDirectory;
    }
    public void setReportsDirectory(String reportsDirectory) {
        this.reportsDirectory = reportsDirectory;
    }  
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getPdfCreateDirectory() {
        return pdfCreateDirectory;
    }

    public void setPdfCreateDirectory(String pdfCreateDirectory) {
        this.pdfCreateDirectory = pdfCreateDirectory;
    }
    
    public String getPdfGetResource() {
        return pdfGetResource;
    }

    public void setPdfGetResource(String pdfGetResource) {
        this.pdfGetResource = pdfGetResource;
    }
	public String getPdfArchiveDir() {
        return pdfArchiveDir;
    }

    public void setPdfArchiveDir(String pdfArchiveDir) {
        this.pdfArchiveDir = pdfArchiveDir;
    }
    
    public String getPdfErrorDir() {
        return pdfErrorDir;
    }

    public void setPdfErrorDir(String pdfErrorDir) {
        this.pdfErrorDir = pdfErrorDir;
    }
}