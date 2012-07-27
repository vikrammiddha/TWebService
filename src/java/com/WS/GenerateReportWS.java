/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.WS;

import com.bean.BillItem;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

import com.src.MainClass;
import java.util.ArrayList;

/**
 *
 * @author Vikram Middha
 */
@WebService(serviceName = "GenerateReportWS")
public class GenerateReportWS {

    /**
     * Web service operation
     */
    @WebMethod(operationName = "generateReports")
    public Integer generateReports(@WebParam(name = "accountNumber") String accountNumber, @WebParam(name = "billRunId") String billRunId, @WebParam(name = "billId") String billId) {
        
        try{
        
            MainClass obj = new MainClass();
            return obj.generateReports(accountNumber, billRunId, billId);
            
        }catch(Exception e){
            
        }
       
        return null;
        
    }
}
