/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.controller.handlers.request;

import java.math.BigInteger;

import org.apache.log4j.Logger;

import jkad.controller.handlers.Controller;
import jkad.controller.handlers.RequestHandler;
import jkad.protocol.KadProtocolException;
import jkad.protocol.rpc.request.StoreRPC;
import jkad.protocol.rpc.response.StoreResponse;
import jkad.structures.buffers.RPCBuffer;
import jkad.structures.kademlia.KadNode;
import jkad.structures.kademlia.RPCInfo;

public class StoreHandler extends RequestHandler<StoreResponse>
{
	private static Logger logger = Logger.getLogger(StoreHandler.class);
	
    private KadNode node;
    private BigInteger key;
    private BigInteger rpcID;
    private String value;
    private Status actualStatus;
    private RPCBuffer outputBuffer;
	
	public StoreHandler()
    {
		this.node = null;
		this.key = null;
		this.value = null;
		this.rpcID = null;
		this.outputBuffer = RPCBuffer.getSentBuffer();
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
	
	public BigInteger getRpcID()
	{
		return rpcID;
	}

	public void setRpcID(BigInteger rpcID) 
	{
		this.rpcID = rpcID;
	}

	public KadNode getNode()
	{
		return node;
	}

	public void setNode(KadNode node) 
	{
		this.node = node;
	}

	public String getValue() 
	{
		return value;
	}

	public void setValue(String value) 
	{
		this.value = value;
	}
	
    public Status getStatus()
    {
        return actualStatus;
    }

	public void run()
    {
		actualStatus = Status.PROCESSING;
		String keyString = this.key.toString(16);
		String nodeID = this.node.getNodeID().toString(16);
		String ipAndPort = this.node.getIpAndPort();
		logger.info("Saving value " + value + " with key " + keyString + " on node " + nodeID + " (" + ipAndPort + ")");
		StoreRPC rpc = new StoreRPC();
		try 
		{
			rpc.setDestinationNodeID(node.getNodeID());
			rpc.setSenderNodeID(Controller.getMyID());
			rpc.setRPCID(rpcID);
			rpc.setKey(this.key);
			rpc.setValue(new BigInteger(value.getBytes()));
			rpc.setPiece((byte)1);
			rpc.setPieceTotal((byte)1);
			RPCInfo<StoreRPC> rpcInfo = new RPCInfo<StoreRPC>(rpc, node.getIpAddress().getHostAddress(), node.getPort());
			outputBuffer.add(rpcInfo);
		} catch (KadProtocolException e) 
		{
			logger.warn(e);
		}
		actualStatus = Status.ENDED;
    }

    public void addResult(RPCInfo<StoreResponse> rpcInfo)
    {
    	String rpcID = rpcInfo.getRPC().getRPCID().toString(16);
        logger.warn("Response to a store commmand not expected (response from " + rpcInfo.getIPAndPort() + " with rpcID " + rpcID + ")");
    }
    
    public void clear()
    {
    	this.node = null;
		this.key = null;
		this.value = null;
		this.rpcID = null;
		this.actualStatus = Status.NOT_STARTED;
    }
}
