package Common.src.com.Config;

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
    public static AppConfig getAppConfig() {

        //LOGGER.info("Configurator:getAppConfig(): Configuring the Application credentials .........................");

        Properties props = new Properties();
        AppConfig appConfig = new AppConfig();

        try {
                props.load(ClassLoader.getSystemResourceAsStream("Resilient.properties"));
            //props.load(new FileInputStream("C:/Vikram Data/Java Workspace/GenerateReportsWS/Resilient.properties"));
               // props.load(getServletContext().getResourceAsStream("/WEB-INF/filename.properties"));
                // SFDC
                appConfig.setSfdcEndpoint(props.getProperty("sfdc.sfdcEndpoint"));
                appConfig.setSfdcUsername(props.getProperty("sfdc.sfdcUsername"));
                appConfig.setSfdcPassword(props.getProperty("sfdc.sfdcPassword"));

                appConfig.setSfdcBillToItemQueryFields(props.getProperty("resilient.launchReport.sfdcBillToItemQueryFields"));

        } catch (IOException e) {
            LOGGER.error("Exception while configuring the Application credentials ..." + e);
        }
        return appConfig;
    }

}
