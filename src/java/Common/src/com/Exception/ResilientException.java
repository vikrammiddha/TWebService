
package Common.src.com.Exception;

/* 
########################################################################### 
# File..................: ResilientException.java
# Version...............: 1.0
# Created by............: Vikram Middha
# Created Date..........: 27-Jul-2012
# Last Modified by......: 
# Last Modified Date....: 
# Description...........: Custom Exception class.
# Change Request History: 				   							 
########################################################################### 
*/

public class ResilientException extends Exception{
    /**
     * <p>
     * Thrown when the cause of this exception is not another exception.
     * </p>
     * 
     * @param sourceClass
     *            The class which threw the exception.
     * @param message
     *            The reason for the exception.
     */
    public ResilientException(String msg){ 
          super(msg); 
        } 

    /**
     * <p>
     * Thrown when the cause of this exception is another exception.
     * </p>
     * 
     * @param sourceClass
     *            The class which threw the exception.
     * @param message
     *            The reason for the exception.
     * @param cause
     *            The causing exception.
     */
    public ResilientException(String msg, Throwable t){ 
      super(msg,t); 
    } 
}
