/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ewsconnect;

import Common.src.com.Config.AppConfig;
import Common.src.com.Config.Configurator;
import Common.src.com.Exception.ResilientException;
import Common.src.com.util.EmailUtils;
import com.itextpdf.text.pdf.PdfReader;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.logging.Level;
import microsoft.exchange.webservices.data.EmailMessage;
import microsoft.exchange.webservices.data.ExchangeCredentials;
import microsoft.exchange.webservices.data.ExchangeService;
import microsoft.exchange.webservices.data.MessageBody;
import microsoft.exchange.webservices.data.WebCredentials;
import org.apache.log4j.Logger;

/**
 *
 * @author Nimil
 */
public class EWSConnection {

    public static ExchangeService service = null;
    public static boolean processedFile = false;
    public static boolean movedFile = false;

    /*Initialize and load the Resilient.Propoerties file */
    private static AppConfig appConfig = null; 
    
    /*Initializing Logger.*/
    private static Logger LOGGER = Logger.getLogger(EWSConnection.class);
    
    public EWSConnection() throws URISyntaxException, ResilientException {
       appConfig = Configurator.getAppConfig();        
    }

    private boolean process(File file) {
        boolean processed = false;
        try {
            LOGGER.info("Processing Report : " + file);
            PdfReader ReadInputPDF = new PdfReader(file.getAbsolutePath());
            HashMap<String, String> metaDataInfo = ReadInputPDF.getInfo();
            LOGGER.info("PDF Metadata : " + metaDataInfo.get("Keywords"));
            //System.out.println(metaDataInfo.get("Keywords"));
            processed = emailReport(file.getAbsolutePath(),metaDataInfo.get("Keywords"));
            /* dumping metadata on the screen */
            return processed;
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(EWSConnection.class.getName()).log(Level.SEVERE, null, ex);
            return processed;
        } 
        
    }
 

    public boolean emailReport(String path,String emailToAddress) throws Exception {
        //Send Email 
        boolean sent = false;
        LOGGER.info("Emailing Report : " + path);
        try{
            EmailUtils email = new EmailUtils();
            appConfig.setAttachmentPath(path);
            EmailUtils _tmp = email;
            EmailUtils.sendEMail(appConfig, emailToAddress, "Itemisation Report", "");
            sent = true;
        }catch(Exception e){
            LOGGER.error("Could not send email for the report :" + path + ". Cause :" + e.getMessage());
            throw new Exception(e);
        }
        finally{
            return sent;
        }
    }
    
    public void sendEmail(String subject, String emailBody ) throws Exception {
        //Send Email 
        
        LOGGER.info("Sending Confirmation email.");
        try{
            EmailMessage msg = new EmailMessage(service);
            msg.setSubject(subject);
            msg.setBody(MessageBody.getMessageBodyFromText(emailBody));
           
            if(appConfig.getErrorSuccessEmailAddress().indexOf(";") > -1){
                String[] emailToAddresses = appConfig.getErrorSuccessEmailAddress().split(";");
                for(String emailAdd : emailToAddresses){
                    msg.getToRecipients().add(emailAdd);
                }
            }else{
                msg.getToRecipients().add(appConfig.getErrorSuccessEmailAddress());
            }
            msg.send();
        }catch(Exception e){
            LOGGER.error("Exception while sending Success/Error email. Cause :" + e.getMessage());
            throw new Exception(e);
        }
    }

    public void processFolder(File dir) throws IOException, Exception {
        
        LOGGER.info("Entered into Method : processFolder ");
        LOGGER.info("Total reports in the Reports Directory := " + dir.list().length);
        if (dir.isDirectory()) {
            String[] children = dir.list();
            File file = null;
            File archiveDir = new File(appConfig.getPdfArchiveDir());
            File errorDir = new File(appConfig.getPdfErrorDir());
            for (int i = 0; i < children.length; i++) {
                file = new File(dir.getAbsoluteFile()+"\\"+children[i]);
                processedFile = process(file);
                file = new File(dir.getAbsoluteFile()+"\\"+children[i]);
                if (processedFile){
                    movedFile = file.renameTo(new File(archiveDir, file.getName()));
                }else{
                    movedFile = file.renameTo(new File(errorDir, file.getName()));
                }

            }
        }
    }
}
