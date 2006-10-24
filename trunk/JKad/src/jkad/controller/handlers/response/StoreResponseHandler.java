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
    private DataManagerFacade<String> storage;
    private BigInteger key;
    private String value;
    
    public StoreResponseHandler(RPCInfo<StoreRPC> rpcInfo)
    {
    	super(ToolBox.getReflectionTools().generateThreadName(StoreResponseHandler.class));
    	this.actualStatus = Status.NOT_STARTED;
        this.storage = DataManagerFacade.getDataManager();
        this.key = rpcInfo.getRPC().getKey();
        this.value = new String(rpcInfo.getRPC().getValue().toByteArray());
    }
    
    public void setKeyAndValue(BigInteger key, String value)
    {
    	this.setKey(key);
    	this.setValue(value);
    }
    
    public void setKey(BigInteger key)
    {
    	this.key = key;
    }
    
    public void setValue(String value)
    {
    	this.value = value;
    }
    
    public synchronized Status getStatus()
    {
        return this.actualStatus;
    }

    public void run()
    {
        this.actualStatus = Status.PROCESSING;
        logger.info("Saving value " + value + " with key " + key.toString(16));
        String oldValue = storage.put(key, value);
        if(oldValue != null)
        	logger.debug("previous value overwritten: " + oldValue);
        this.actualStatus = Status.KILLED;
    }
}
