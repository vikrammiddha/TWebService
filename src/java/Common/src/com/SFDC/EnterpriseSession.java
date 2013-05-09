package Common.src.com.SFDC;

import Common.src.com.Config.AppConfig;
import Common.src.com.Exception.ResilientException;
import com.sforce.soap.partner.*;
import com.sforce.soap.partner.fault.*;
import com.sforce.soap.partner.sobject.*;
import java.net.URL;
import java.rmi.RemoteException;
import org.apache.log4j.Logger;
;

/* 
########################################################################### 
# File..................: EnterpriseSession.java
# Version...............: 1.0
# Created by............: Vikram Middha
# Created Date..........: 27-Jul-2012
# Last Modified by......: 
# Last Modified Date....: 
# Description...........: This class is used to initialize SFDC Client Stub SoapBindingStub.
*                           And this class is used as an entry point to SFDC communication.
# Change Request History: 				   							 
########################################################################### 
*/

/**
 * 
 * 
 * @author jkannan
 *
 */

public class EnterpriseSession {

    private static Logger LOGGER = Logger.getLogger(EnterpriseSession.class);

    private SoapBindingStub binding;
    private LoginResult loginResult = null;
    AppConfig appConfig = null;
    public String sessionId = "";
    public String serverURL = "";

    public EnterpriseSession() {
    }

    /**
     * Initialize the Appconfig and call to login.
     * 
     * @param appConfig
     * @return
     * @throws ResilientException 
     */
    public boolean connect(String sessionId, String serverURL) throws ResilientException {
            this.sessionId = sessionId;
            this.serverURL = serverURL;
            return login();
    }

    /**
     * used to Login to SFDC and initialize SFDC Client Stub SoapBindingStub.
     * 
     * @return
     * @throws ResilientException 
     */
    private boolean login() throws ResilientException {

        // Provide feed back while we create the web service binding
        LOGGER.info("Creating the binding to the web service...");

        /*
         * URL wsdlURL; try { wsdlURL = new File("enterprise.wsdl").toURL();
         * }catch (IOException e) { throw new
         * RuntimeException("Can't find WSDL.", e); }
         */
        try {
            SforceServiceLocator svc = new SforceServiceLocator();
            binding = new SoapBindingStub(svc);
        }catch (Exception ex) {
            LOGGER.error("creating binding to soap service, error was: \n" + ex.getMessage());
            throw new ResilientException("Error while creating binding soap service " + ex.getMessage(), ex);
        }

        // Time out after a minute
        binding.setTimeout(60000);

        // Attempt the login giving the user feedback
        LOGGER.info("Login into Salesforce.com now....");
        
        // set the session header for subsequent call authentication
        binding._setProperty("javax.xml.rpc.service.endpoint.address", serverURL);
        SessionHeader sh = new SessionHeader();
        sh.setSessionId(sessionId);
        binding.setHeader(new SforceServiceLocator().getServiceName()
                        .getNamespaceURI(), "SessionHeader", sh);             
        return true;
    }

    /**
     * Submit query to SFDC.
     * 
     * @param query
     *            SOQL query
     * @return QueryResult
     * @throws ResilientException 
     */
    public QueryResult query(final String query) throws ResilientException {

        try {
            return binding.query(query);
        } 
        catch (InvalidSObjectFault e) {
            LOGGER.error("Exception in EnterpriseSession: query: InvalidSObjectFault:-" + e.getExceptionMessage());
            throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
            } 
        catch (MalformedQueryFault e) {
            LOGGER.error("Exception in EnterpriseSession: query: MalformedQueryFault:-" + e.getExceptionMessage());
            throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
        }
        catch (InvalidFieldFault e) {
            LOGGER.error("Exception in EnterpriseSession: query: InvalidFieldFault :-" + e.getExceptionMessage());
            throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
        } catch (InvalidIdFault e) {
            LOGGER.error("Exception in EnterpriseSession: query: InvalidIdFault:-" + e.getExceptionMessage());
            throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
        } catch (UnexpectedErrorFault e) {
            LOGGER.error("Exception in EnterpriseSession: query: UnexpectedErrorFault:-" + e.getExceptionMessage());
            throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
        } catch (InvalidQueryLocatorFault e) {
            LOGGER.error("Exception in EnterpriseSession: query: InvalidQueryLocatorFault:-" + e.getExceptionMessage());
            throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
        } catch (RemoteException e) {
            LOGGER.error("Exception in EnterpriseSession: query: RemoteException:-" + e.getMessage());
            throw new ResilientException("Error while creating binding soap service"+e.getMessage(),e);		
        } 

    }

    /**
     * Submit queryMore() call to SFDC.
     * 
     * @param queryLocator
     *            from previous query
     * @return QueryResult object
     * @throws ResilientException 
     */
    public QueryResult queryMore(final String queryLocator) throws ResilientException {

        try {
            return binding.queryMore(queryLocator);
        } catch (InvalidSObjectFault e) {
            LOGGER.error("Exception in EnterpriseSession: query: InvalidSObjectFault:-" + e.getExceptionMessage());
            throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
        } 
        catch (MalformedQueryFault e) {
            LOGGER.error("Exception in EnterpriseSession: query: MalformedQueryFault:-" + e.getExceptionMessage());
            throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
        } catch (InvalidFieldFault e) {
            LOGGER.error("Exception in EnterpriseSession: query: InvalidFieldFault :-" + e.getExceptionMessage());
            throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
        } catch (InvalidIdFault e) {
            LOGGER.error("Exception in EnterpriseSession: query: InvalidIdFault:-" + e.getExceptionMessage());
            throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
        } catch (UnexpectedErrorFault e) {
            LOGGER.error("Exception in EnterpriseSession: query: UnexpectedErrorFault:-" + e.getExceptionMessage());
            throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
        } catch (InvalidQueryLocatorFault e) {
            LOGGER.error("Exception in EnterpriseSession: query: InvalidQueryLocatorFault:-" + e.getExceptionMessage());
            throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
        } catch (RemoteException e) {
            LOGGER.error("Exception in EnterpriseSession: query: RemoteException:-" + e.getMessage());
            throw new ResilientException("Error while creating binding soap service"+e.getMessage(),e);		
        }catch (Exception e) {
            throw new ResilientException("Error while creating binding soap service"+e.getMessage(),e);		
        }

    }

    /**
     * Submit create() call to SFDC.
     * 
     * @param sobjects
     *            Objects to create
     * @return List of SaveResult objects
     * @throws ResilientException 
     */
    public SaveResult[] create(final SObject[] sobjects) throws ResilientException {

        try {
            SaveResult[] sr = binding.create(sobjects);
            return sr;
        } 
        catch (InvalidSObjectFault e) {
            LOGGER.error("Exception in EnterpriseSession: query: InvalidSObjectFault:-" + e.getExceptionMessage());
            throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
        } 
        catch (MalformedQueryFault e) {
                    LOGGER.error("Exception in EnterpriseSession: query: MalformedQueryFault:-" + e.getExceptionMessage());
                    throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
        } catch (InvalidFieldFault e) {
                LOGGER.error("Exception in EnterpriseSession: query: InvalidFieldFault :-" + e.getExceptionMessage());
                throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
        } catch (InvalidIdFault e) {
                LOGGER.error("Exception in EnterpriseSession: query: InvalidIdFault:-" + e.getExceptionMessage());
                throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
        } catch (UnexpectedErrorFault e) {
                LOGGER.error("Exception in EnterpriseSession: query: UnexpectedErrorFault:-" + e.getExceptionMessage());
                throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
        } catch (InvalidQueryLocatorFault e) {
                LOGGER.error("Exception in EnterpriseSession: query: InvalidQueryLocatorFault:-" + e.getExceptionMessage());
                throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
        } catch (RemoteException e) {
                LOGGER.error("Exception in EnterpriseSession: query: RemoteException:-" + e.getMessage());
                throw new ResilientException("Error while creating binding soap service"+e.getMessage(),e);		
        }catch (Exception e) {
                throw new ResilientException("Error while creating binding soap service"+e.getMessage(),e);		
        }

    }

    /**
     * Submit update() call to SFDC.
     * 
     * @param sobjects
     *            Objects to update
     * @return List of SaveResult objects
     * @throws ResilientException 
     */
    public SaveResult[] update(final SObject[] sobjects) throws ResilientException {
        try {
                return binding.update(sobjects);
        } catch (InvalidSObjectFault e) {
                LOGGER.error("Exception in EnterpriseSession: query: InvalidSObjectFault:-" + e.getExceptionMessage());
                throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
                } catch (MalformedQueryFault e) {
                        LOGGER.error("Exception in EnterpriseSession: query: MalformedQueryFault:-" + e.getExceptionMessage());
                        throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
        } catch (InvalidFieldFault e) {
                LOGGER.error("Exception in EnterpriseSession: query: InvalidFieldFault :-" + e.getExceptionMessage());
                throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
        } catch (InvalidIdFault e) {
                LOGGER.error("Exception in EnterpriseSession: query: InvalidIdFault:-" + e.getExceptionMessage());
                throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
        } catch (UnexpectedErrorFault e) {
                LOGGER.error("Exception in EnterpriseSession: query: UnexpectedErrorFault:-" + e.getExceptionMessage());
                throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
        } catch (InvalidQueryLocatorFault e) {
                LOGGER.error("Exception in EnterpriseSession: query: InvalidQueryLocatorFault:-" + e.getExceptionMessage());
                throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
        } catch (RemoteException e) {
                LOGGER.error("Exception in EnterpriseSession: query: RemoteException:-" + e.getMessage());
                throw new ResilientException("Error while creating binding soap service"+e.getMessage(),e);		
        }catch (Exception e) {
                throw new ResilientException("Error while creating binding soap service"+e.getMessage(),e);		
        }
    }

    /**
     * Submit delete() call to SFDC.
     * 
     * @param sobjects
     *            Objects to create
     * @return List of SaveResult objects
     * @throws ResilientException 
     */
    public DeleteResult[] delete(final String[] ids) throws ResilientException {
        try {
                return binding.delete(ids);
        } catch (InvalidSObjectFault e) {
                LOGGER.error("Exception in EnterpriseSession: query: InvalidSObjectFault:-" + e.getExceptionMessage());
                throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
                } catch (MalformedQueryFault e) {
                        LOGGER.error("Exception in EnterpriseSession: query: MalformedQueryFault:-" + e.getExceptionMessage());
                        throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
        } catch (InvalidFieldFault e) {
                LOGGER.error("Exception in EnterpriseSession: query: InvalidFieldFault :-" + e.getExceptionMessage());
                throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
        } catch (InvalidIdFault e) {
                LOGGER.error("Exception in EnterpriseSession: query: InvalidIdFault:-" + e.getExceptionMessage());
                throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
        } catch (UnexpectedErrorFault e) {
                LOGGER.error("Exception in EnterpriseSession: query: UnexpectedErrorFault:-" + e.getExceptionMessage());
                throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
        } catch (InvalidQueryLocatorFault e) {
                LOGGER.error("Exception in EnterpriseSession: query: InvalidQueryLocatorFault:-" + e.getExceptionMessage());
                throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
        } catch (RemoteException e) {
                LOGGER.error("Exception in EnterpriseSession: query: RemoteException:-" + e.getMessage());
                throw new ResilientException("Error while creating binding soap service"+e.getMessage(),e);		
        }
    }
	
    /**
     * Create batches when the number of records to update exceeds the API limit and submit update() call recursively to SFDC.
     * 
     * @param sobjects
     *            Objects to update
     * @param totalRecords         
     *            Number of records to update
     * @return List of SaveResult objects
     * @throws ResilientException 
     */
    public SaveResult[] updateMore(final SObject[] sobjects, int totalRecords) throws ResilientException {
        try {	
                // Define the maximum API limit
                final int MAX_BATCH_SIZE = 200;

                int totalCount = totalRecords;
                int posCounter = 0;
                int resultsCounter =0;

                // List of SaveResult to hold the consolidated results
                SaveResult[] saveResults = new SaveResult[totalRecords];

                // Repeat this loop until complete list of SObjects is updated
                while (totalCount > 0)
            {
                        // Get the number of SObject records that need to be updated				
                        int batchCount = totalCount;

                  // Check if the number is larger than the maximum API limit
                        if (totalCount > MAX_BATCH_SIZE) {
                                //Split into batches of permitted size
                                batchCount = MAX_BATCH_SIZE;
                        }
                  // Define a new list of SObject to hold a subset of the list of SObjects that need to be updated
                        SObject[] updateSObj = new SObject[batchCount];
                        int thisBatchCounter = 0;

                  // Create the current batch from the list of SObject that need to be updated 
                        for (int i = posCounter; i < posCounter + batchCount; i++) {
                              updateSObj[thisBatchCounter] = sobjects[i];
                              thisBatchCounter++;
                        }
                  // List of SaveResult to hold the current batch update results      
                        SaveResult[] saveTempResults = null;
                  // Update the current batch
                        saveTempResults = binding.update(updateSObj);

                  // Save the results of the current batch update into the consolidated results      
                        for (int j= 0; (saveTempResults!=null && j<saveTempResults.length); j++) {
                                saveResults[resultsCounter]=saveTempResults[j];
                                resultsCounter++;	                                                        	                
                        }
                  // Adjust position to the next SObject in the list      
                        posCounter = posCounter + batchCount;
                  // Calculate the number of SObject records yet to be updated.      
                        totalCount = totalCount - batchCount;
            }			
                return saveResults;
        } catch (InvalidSObjectFault e) {
                LOGGER.error("Exception in EnterpriseSession: query: InvalidSObjectFault:-" + e.getExceptionMessage());
                throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
                } catch (MalformedQueryFault e) {
                        LOGGER.error("Exception in EnterpriseSession: query: MalformedQueryFault:-" + e.getExceptionMessage());
                        throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
        } catch (InvalidFieldFault e) {
                LOGGER.error("Exception in EnterpriseSession: query: InvalidFieldFault :-" + e.getExceptionMessage());
                throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
        } catch (InvalidIdFault e) {
                LOGGER.error("Exception in EnterpriseSession: query: InvalidIdFault:-" + e.getExceptionMessage());
                throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
        } catch (UnexpectedErrorFault e) {
                LOGGER.error("Exception in EnterpriseSession: query: UnexpectedErrorFault:-" + e.getExceptionMessage());
                throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
        } catch (InvalidQueryLocatorFault e) {
                LOGGER.error("Exception in EnterpriseSession: query: InvalidQueryLocatorFault:-" + e.getExceptionMessage());
                throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
        } catch (RemoteException e) {
                LOGGER.error("Exception in EnterpriseSession: query: RemoteException:-" + e.getMessage());
                throw new ResilientException("Error while creating binding soap service"+e.getMessage(),e);		
        }catch (Exception e) {
                LOGGER.error("Exception in EnterpriseSession: query: RemoteException:-" + e.getMessage());
                throw new ResilientException("Error while creating binding soap service"+e.getMessage(),e);		
        }
    }
	
    /**
     * Create batches when the number of records to insert exceeds the API limit and submit update() call recursively to SFDC.
     * 
     * @param sobjects
     *            Objects to update
     * @param totalRecords         
     *            Number of records to update
     * @return List of SaveResult objects
     * @throws ResilientException 
     */
    public SaveResult[] createMore(final SObject[] sobjects, int totalRecords) throws ResilientException {
        try {	
                // Define the maximum API limit
                final int MAX_BATCH_SIZE = 200;

                int totalCount = totalRecords;
                int posCounter = 0;
                int resultsCounter =0;

                // List of SaveResult to hold the consolidated results
                SaveResult[] saveResults = new SaveResult[totalRecords];

                // Repeat this loop until complete list of SObjects is inserted
                while (totalCount > 0)
            {
                        // Get the number of SObject records that need to be inserted				
                        int batchCount = totalCount;

                  // Check if the number is larger than the maximum API limit
                        if (totalCount > MAX_BATCH_SIZE) {
                                //Split into batches of permitted size
                                batchCount = MAX_BATCH_SIZE;
                        }
                  // Define a new list of SObject to hold a subset of the list of SObjects that need to be inserted
                        SObject[] insertSObj = new SObject[batchCount];
                        int thisBatchCounter = 0;

                  // Create the current batch from the list of SObject that need to be inserted 
                        for (int i = posCounter; i < posCounter + batchCount; i++) {
                              insertSObj[thisBatchCounter] = sobjects[i];
                              thisBatchCounter++;
                        }
                  // List of SaveResult to hold the current batch update results      
                        SaveResult[] saveTempResults = null;
                  // Update the current batch
                        saveTempResults = binding.create(insertSObj);

                  // Save the results of the current batch update into the consolidated results      
                        for (int j= 0; (saveTempResults!=null && j<saveTempResults.length); j++) {
                                saveResults[resultsCounter]=saveTempResults[j];
                                resultsCounter++;	                                                        	                
                        }
                  // Adjust position to the next SObject in the list      
                        posCounter = posCounter + batchCount;
                  // Calculate the number of SObject records yet to be inserted.      
                        totalCount = totalCount - batchCount;
            }			
                return saveResults;
        } catch (InvalidSObjectFault e) {
                LOGGER.error("Exception in EnterpriseSession: query: InvalidSObjectFault:-" + e.getExceptionMessage());
                throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
                } catch (MalformedQueryFault e) {
                        LOGGER.error("Exception in EnterpriseSession: query: MalformedQueryFault:-" + e.getExceptionMessage());
                        throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
        } catch (InvalidFieldFault e) {
                LOGGER.error("Exception in EnterpriseSession: query: InvalidFieldFault :-" + e.getExceptionMessage());
                throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
        } catch (InvalidIdFault e) {
                LOGGER.error("Exception in EnterpriseSession: query: InvalidIdFault:-" + e.getExceptionMessage());
                throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
        } catch (UnexpectedErrorFault e) {
                LOGGER.error("Exception in EnterpriseSession: query: UnexpectedErrorFault:-" + e.getExceptionMessage());
                throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
        } catch (InvalidQueryLocatorFault e) {
                LOGGER.error("Exception in EnterpriseSession: query: InvalidQueryLocatorFault:-" + e.getExceptionMessage());
                throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
        } catch (RemoteException e) {
                LOGGER.error("Exception in EnterpriseSession: query: RemoteException:-" + e.getMessage());
                throw new ResilientException("Error while creating binding soap service"+e.getMessage(),e);		
        }
    }
    /**
     * Create batches when the number of records to deleted exceeds the API limit and submit update() call recursively to SFDC.
     * 
     * @param sobjects
     *            Objects to update
     * @param totalRecords         
     *            Number of records to update
     * @return List of SaveResult objects
     * @throws ResilientException 
     */
    public DeleteResult[] deleteMore(final String[] id, int totalRecords) throws ResilientException {
            try {	
                    // Define the maximum API limit
                    final int MAX_BATCH_SIZE = 200;

                    int totalCount = totalRecords;
                    int posCounter = 0;
                    int resultsCounter =0;

                    // List of DeleteResult to hold the consolidated results
                    DeleteResult[] delResults = new DeleteResult[totalRecords];

                    // Repeat this loop until complete list of SObjects Id is deleted
                    while (totalCount > 0)
                {
                            // Get the number of SObject records that need to be deleted				
                            int batchCount = totalCount;

                      // Check if the number is larger than the maximum API limit
                            if (totalCount > MAX_BATCH_SIZE) {
                                    //Split into batches of permitted size
                                    batchCount = MAX_BATCH_SIZE;
                            }
                      // Define a new list of SObject to hold a subset of the list of SObjects that need to be deleted
                            String[] insertSObj = new String[batchCount];
                            int thisBatchCounter = 0;

                      // Create the current batch from the list of SObject that need to be deleted 
                            for (int i = posCounter; i < posCounter + batchCount; i++) {
                                  insertSObj[thisBatchCounter] = id[i];
                                  thisBatchCounter++;
                            }
                      // List of DeleteResult to hold the current batch deleted results      
                            DeleteResult[] delTempResults = null;
                      // Update the current batch
                            delTempResults = binding.delete(insertSObj);

                      // Save the results of the current batch deleted into the consolidated results      
                            for (int j= 0; (delTempResults!=null && j<delTempResults.length); j++) {
                                    delResults[resultsCounter]=delTempResults[j];
                                    resultsCounter++;	                                                        	                
                            }
                      // Adjust position to the next SObject in the list      
                            posCounter = posCounter + batchCount;
                      // Calculate the number of SObject records yet to be deleted.      
                            totalCount = totalCount - batchCount;
                }			
                    return delResults;
            } catch (InvalidSObjectFault e) {
                    LOGGER.error("Exception in EnterpriseSession: query: InvalidSObjectFault:-" + e.getExceptionMessage());
                    throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
                    } catch (MalformedQueryFault e) {
                            LOGGER.error("Exception in EnterpriseSession: query: MalformedQueryFault:-" + e.getExceptionMessage());
                            throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
            } catch (InvalidFieldFault e) {
                    LOGGER.error("Exception in EnterpriseSession: query: InvalidFieldFault :-" + e.getExceptionMessage());
                    throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
            } catch (InvalidIdFault e) {
                    LOGGER.error("Exception in EnterpriseSession: query: InvalidIdFault:-" + e.getExceptionMessage());
                    throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
            } catch (UnexpectedErrorFault e) {
                    LOGGER.error("Exception in EnterpriseSession: query: UnexpectedErrorFault:-" + e.getExceptionMessage());
                    throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
            } catch (InvalidQueryLocatorFault e) {
                    LOGGER.error("Exception in EnterpriseSession: query: InvalidQueryLocatorFault:-" + e.getExceptionMessage());
                    throw new ResilientException("Error while creating binding soap service"+e.getExceptionMessage(),e);		
            } catch (RemoteException e) {
                    LOGGER.error("Exception in EnterpriseSession: query: RemoteException:-" + e.getMessage());
                    throw new ResilientException("Error while creating binding soap service"+e.getMessage(),e);		
            }
    }


}
