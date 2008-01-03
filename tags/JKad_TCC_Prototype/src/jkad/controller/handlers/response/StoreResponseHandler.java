/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.controller.handlers.response;

import java.math.BigInteger;

import jkad.controller.handlers.Handler;
import jkad.facades.storage.DataManagerFacade;
import jkad.protocol.rpc.request.StoreRPC;

import org.apache.log4j.Logger;

public class StoreResponseHandler extends Handler<StoreRPC>
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
        StoreRPC rpc = getRPCInfo().getRPC();
        BigInteger key = rpc.getKey();
        String value = new String(rpc.getValue().toByteArray());
        if(key != null)
        {
            this.actualStatus = Status.PROCESSING;
            DataManagerFacade<String> storage = DataManagerFacade.getDataManager();
            logger.info("Saving value " + value + " with key " + key.toString(16));
            String oldValue = storage.put(key, value);
            if(oldValue != null)
            	logger.debug("previous value overwritten: " + oldValue);
            this.actualStatus = Status.ENDED;
        } else 
        {
            throw new NullPointerException("Cannot process a StoreRpc with a null key");
        }
    }

    public void clear()
    {
        this.actualStatus = Status.NOT_STARTED;
        setRPCInfo(null);
    }
}
