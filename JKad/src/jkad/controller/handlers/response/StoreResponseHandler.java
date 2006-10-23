/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.controller.handlers.response;

import jkad.controller.handlers.HandlerThread;

import org.apache.log4j.Logger;

public class StoreResponseHandler extends HandlerThread
{
    private static Logger logger = Logger.getLogger(StoreResponseHandler.class);
    
    private Status actualStatus;
    
    public StoreResponseHandler()
    {
        this.actualStatus = Status.NOT_STARTED;
    }
    
    public synchronized Status getStatus()
    {
        return this.actualStatus;
    }

    public void run()
    {
        this.actualStatus = Status.PROCESSING;
    }
}
