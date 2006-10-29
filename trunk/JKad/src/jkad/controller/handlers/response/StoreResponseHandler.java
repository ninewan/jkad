/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.controller.handlers.response;

import java.math.BigInteger;

import jkad.controller.handlers.HandlerThread;
import jkad.facades.storage.DataManagerFacade;
import jkad.protocol.rpc.request.StoreRPC;
import jkad.structures.kademlia.RPCInfo;
import jkad.tools.ToolBox;

import org.apache.log4j.Logger;

public class StoreResponseHandler extends HandlerThread
{
    private static Logger logger = Logger.getLogger(StoreResponseHandler.class);
    
    private Status actualStatus;
    private BigInteger key;
    private String value;
    
    public StoreResponseHandler()
    {
    	super(ToolBox.getReflectionTools().generateThreadName(StoreResponseHandler.class));
    	this.actualStatus = Status.NOT_STARTED;
    }
    
    public BigInteger getKey()
    {
        return key;
    }

    public void setKey(BigInteger key)
    {
        this.key = key;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
    
    public void setKeyAndValue(BigInteger key, String value)
    {
        this.setKey(key);
        this.setValue(value);
    }
    
    public void setRPCInfo(RPCInfo<StoreRPC> rpcInfo)
    {
        this.key = rpcInfo.getRPC().getKey();
        this.value = new String(rpcInfo.getRPC().getValue().toByteArray());
    }

    public synchronized Status getStatus()
    {
        return this.actualStatus;
    }

    public void run()
    {
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
        this.key = null;
        this.value = null;
    }
}
