/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.WS;

import com.WS_client.GenerateReportWS;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

/**
 * REST Web Service
 *
 * @author Vikram Middha
 */
@Path("generatereportwsport")
public class GenerateReportWSPort {
    private GenerateReportWS port;

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GenerateReportWSPort
     */
    public GenerateReportWSPort() {
        port = getPort();
    }

    /**
     * Invokes the SOAP method generateReports
     * @param accountNumber resource URI parameter
     * @param billRunId resource URI parameter
     * @param billId resource URI parameter
     * @return an instance of javax.xml.bind.JAXBElement<java.lang.Integer>
     */
    @GET
    @Produces("application/xml")
    @Consumes("text/plain")
    @Path("generatereports/")
    public JAXBElement<Integer> getGenerateReports(@QueryParam("accountNumber") String accountNumber, @QueryParam("billRunId") String billRunId, @QueryParam("billId") String billId) {
        try {
            // Call Web Service Operation
            if (port != null) {
                java.lang.Integer result = port.generateReports(accountNumber, billRunId, billId);
                return new JAXBElement<java.lang.Integer>(new QName("http//lang.java/", "integer"), java.lang.Integer.class, result);
            }
        } catch (Exception ex) {
            // TODO handle custom exceptions here
        }
        return null;
    }

    /**
     *
     */
    private GenerateReportWS getPort() {
        try {
            // Call Web Service Operation
            com.WS_client.GenerateReportWS_Service service = new com.WS_client.GenerateReportWS_Service();
            com.WS_client.GenerateReportWS p = service.getGenerateReportWSPort();
            return p;
        } catch (Exception ex) {
            // TODO handle custom exceptions here
        }
        return null;
    }
}
