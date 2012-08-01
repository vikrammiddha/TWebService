/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ewsconnect;

import Common.src.com.Config.AppConfig;
import Common.src.com.Config.Configurator;
import Common.src.com.Exception.ResilientException;
import java.io.File;
import java.net.URISyntaxException;
import microsoft.exchange.webservices.data.ServiceLocalException;
import org.apache.log4j.Logger;

/**
 *
 * @author Nimil
 */
public class EWSConnect {
    
    /*Initialize and load the Resilient.Propoerties file */
    private static AppConfig appConfig = null;    
    
    /*Initializing Logger.*/
    private static Logger LOGGER = Logger.getLogger(EWSConnect.class);
    /**
     * @param args the command line arguments
     */
    
    /*Constructor for initializing variables.*/
    public EWSConnect() throws ResilientException{
        
        appConfig = Configurator.getAppConfig();
        
    }
    
    
    public static void main(String[] args) throws URISyntaxException, ServiceLocalException, Exception {
        // TODO code application logic here
        
        /*ItemView view = new ItemView(100);    //Read maximum of 100 emails
        view.getOrderBy().add(ItemSchema.DateTimeReceived, SortDirection.Ascending);    // Read emails by received date order by Ascending
        FindItemsResults< Item> results = service.findItems(WellKnownFolderName.Inbox, new SearchFilter.IsEqualTo(EmailMessageSchema.IsRead, false), view);    //Read only unread emails in inbox
        Iterator<Item> itr = results.iterator();
        System.out.println("Total Unread Emails=" + results.getTotalCount());
        while (itr.hasNext()) {
            Item item = itr.next();
            ItemId itemId = item.getId();
            EmailMessage email = EmailMessage.bind(service, itemId);
            System.out.println("Sender= " + email.getSender());
            System.out.println("Subject= " + email.getSubject());
            System.out.println("Body= " + email.getBody());
            email.setIsRead(true);        //Set the email to read.
            email.update(ConflictResolutionMode.AlwaysOverwrite);
        }
        */
        EWSConnect obj = new EWSConnect();
        obj.emailReports();   
    }
    
    public void emailReports() throws Exception{
        LOGGER.info("Started the Main method in EWSConnect. ");
        File file = new File(appConfig.getReportsDirectory());
        EWSConnection ewsObj = new EWSConnection();
        ewsObj.processFolder(file);
        LOGGER.info("Completed the Main method in EWSConnect. ");
    }
}