package Common.src.com.Config;

import Common.src.com.Exception.ResilientException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;


/* 
########################################################################### 
# File..................: Configurator.java
# Version...............: 1.0
# Created by............: Vikram Middha
# Created Date..........: 27-Jul-2012
# Last Modified by......: 
# Last Modified Date....: 
# Description...........: This class reads the Resilient.properties file and 
*                         populates AppConfig object.
# Change Request History: 				   							 
########################################################################### 
*/
public class Configurator {

    private static Logger LOGGER = Logger.getLogger(Configurator.class);

    /**
     * Private constructor.
     */
    private Configurator() {
            throw new UnsupportedOperationException("Class is not instantiable.");
    }

    /**
     * initialize and get the Configuration
     * 
     * @return
     */
    public static AppConfig getAppConfig() throws ResilientException {

        LOGGER.info("Configurator:getAppConfig(): Configuring the Application credentials .........................");

        Properties props = new Properties();
        AppConfig appConfig = new AppConfig();

        try {
                File directory = new File (".");
                // props.load("Resilient.properties");
               // props.load(new FileInputStream(directory.getCanonicalPath() + "/Resilient.properties"));
                 props.load(new FileInputStream("C:/Resilient.properties"));
                //props.load(Configurator.class.getClassLoader().getResourceAsStream("Resilient.properties"));
                // SFDC
                LOGGER.info(" Resilient Properties loaded successfully ");
                appConfig.setSfdcEndpoint(props.getProperty("sfdc.sfdcEndpoint"));
                appConfig.setSfdcUsername(props.getProperty("sfdc.sfdcUsername"));
                appConfig.setSfdcPassword(props.getProperty("sfdc.sfdcPassword"));

                appConfig.setSfdcBillToItemQueryFields(props.getProperty("resilient.launchReport.sfdcBillToItemQueryFields"));
                
                appConfig.setReportsDirectory(props.getProperty("email.reports.directory"));
                appConfig.setUserName(props.getProperty("email.webCredentials.userName"));
                appConfig.setPassword(props.getProperty("email.webCredentials.password"));
                appConfig.setUri(props.getProperty("email.webservice.uri"));
                appConfig.setErrorSuccessEmailAddress(props.getProperty("email.errorSuccessEmailAddress"));
                appConfig.setErrorSubject(props.getProperty("email.confirmationEmail.Error.Subject"));
                appConfig.setSuccessSubject(props.getProperty("email.confirmationEmail.Success.Subject"));

        } catch (Exception e) {
            LOGGER.error("Exception while configuring the Application credentials ..." + e);
            throw new ResilientException(e.getMessage());
        } 
        return appConfig;
    }

}
