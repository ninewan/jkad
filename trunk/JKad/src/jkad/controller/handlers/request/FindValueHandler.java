/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.controller.handlers.request;

import java.math.BigInteger;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jkad.controller.handlers.Controller;
import jkad.controller.handlers.RequestHandler;
import jkad.facades.storage.DataManagerFacade;
import jkad.protocol.KadProtocolException;
import jkad.protocol.rpc.RPC;
import jkad.protocol.rpc.request.FindValueRPC;
import jkad.protocol.rpc.response.FindNodeResponse;
import jkad.protocol.rpc.response.FindValueResponse;
import jkad.structures.buffers.RPCBuffer;
import jkad.structures.kademlia.KadNode;
import jkad.structures.kademlia.KnowContacts;
import jkad.structures.kademlia.RPCInfo;

import org.apache.log4j.Logger;

public class FindValueHandler extends RequestHandler<FindValueResponse>
{
    private static Logger logger = Logger.getLogger(FindValueHandler.class);

    private Status actualStatus;
    private BigInteger rpcID;
    
    private BigInteger valueKey;
    private String valueKeyString;
    
    private BigInteger result;
    
    private BigInteger distanceFromClosest;
    
    private DataManagerFacade storage;
    private KnowContacts contacts;
    
    private RPCBuffer outputBuffer;
    
    private Set<BigInteger> queriedNodes;
    private int maxQueries;
    
    public FindValueHandler()
    {
        actualStatus = Status.NOT_STARTED;
        valueKey = null;
        storage = null;
        contacts = null;
        outputBuffer = RPCBuffer.getSentBuffer();
        result = null;
        distanceFromClosest = MAX_RANGE;
        queriedNodes = new HashSet<BigInteger>();
        maxQueries = Integer.parseInt(System.getProperty("jkad.findvalue.maxqueries"));
    }
    
    public BigInteger getValueKey()
    {
        return valueKey;
    }

    public void setValueKey(BigInteger valueKey)
    {
        this.valueKey = valueKey;
        this.valueKeyString = valueKey.toString(16);
    }
    
	public DataManagerFacade getStorage()
    {
        return storage;
    }

    public void setStorage(DataManagerFacade storage)
    {
        this.storage = storage;
    }
    
    public KnowContacts getContacts()
    {
        return contacts;
    }

    public void setContacts(KnowContacts contacts)
    {
        this.contacts = contacts;
    }
    
    public BigInteger getRpcID()
    {
        return rpcID;
    }

    public void setRpcID(BigInteger rpcID)
    {
        this.rpcID = rpcID;
    }

    public void run()
    {
        actualStatus = Status.PROCESSING;
        logger.info("Looking for value with key " + valueKeyString);
        Object result = storage.get(valueKey);
        
        // caso nao tenha achado o valor localmente, procure na rede
        if (result == null)
        {
            logger.debug("Value with key " + valueKeyString + " not found localy, looking for it on the network");
            int amount = Integer.parseInt(System.getProperty("jkad.contacts.findamount"));
            List<KadNode> closestNodes = contacts.findClosestContacts(valueKey, amount);
            if (closestNodes.size() > 0)
            {
                for (KadNode node : closestNodes)
                    requestNode(rpcID, node);
            }
            actualStatus = Status.WAITING;
        } else
        {
            logger.info("Value with key " + valueKeyString + " found locally");
            actualStatus = Status.ENDED;
        }
    }

    private void requestNode(BigInteger rpcID, KadNode node)
    {
        this.requestNode(rpcID, node.getNodeID(), node.getIpAddress().getHostAddress(), node.getPort());
    }
    
    private void requestNode(BigInteger rpcID, BigInteger nodeID, String ip, int port)
    {
        FindValueRPC rpc = new FindValueRPC();
        try
        {
            logger.debug("Querying node " + nodeID.toString(16) + " on " + ip + ":" + port + " for the value with key " + valueKeyString);
            rpc.setRPCID(rpcID);
            rpc.setDestinationNodeID(nodeID);
            rpc.setSenderNodeID(Controller.getMyID());
            rpc.setKey(valueKey);
            RPCInfo<FindValueRPC> rpcInfo = new RPCInfo<FindValueRPC>(rpc, ip, port);
            outputBuffer.add(rpcInfo);
            queriedNodes.add(nodeID);
        } catch (KadProtocolException e)
        {
            logger.warn(e);
        }
    }
    
    public void addResult(RPCInfo rpcInfo)
    {
        RPC response = rpcInfo.getRPC();
        if (response instanceof FindNodeResponse)
        {
            try
            {
                FindNodeResponse findNodeResponse = (FindNodeResponse) response;
                BigInteger foundNode = findNodeResponse.getFoundNodeID();
                String foundNodeString = foundNode.toString(16);
                if(queriedNodes.contains(foundNode) )
                {
                    logger.debug("Found node " + foundNodeString + " instead of value. Node already queried");
                } else if (queriedNodes.size() < maxQueries)
                {
                    logger.debug("Found node " + foundNodeString + " instead of value. Querying node");
                    KadNode node = new KadNode(foundNode, findNodeResponse.getIpAddressINet(), findNodeResponse.getPortInteger());
                    BigInteger delta = foundNode.subtract(valueKey).abs();
                    if (delta.compareTo(distanceFromClosest) < 0)
                    {
                        distanceFromClosest = delta;
                        requestNode(rpcID, node);
                    }
                } else
                {
                    logger.debug("Max queries reached, aborting find value");
                    actualStatus = Status.ENDED;
                }
            } catch (UnknownHostException e) 
            {
                logger.warn(e);
            }
        } else if (response instanceof FindValueResponse)
        {
            FindValueResponse findValueResponse = (FindValueResponse) response;
            logger.info("Value with key " + valueKeyString + " found on node " + response.getSenderNodeID().toString(16) + " on " + rpcInfo.getIPAndPort());
            result = findValueResponse.getValue();
            actualStatus = Status.ENDED;
        }
    }
    
    public synchronized BigInteger getResult()
    {
        return result;
    }
    
    public Status getStatus()
    {
        synchronized (actualStatus)
        {
            return actualStatus;
        }
    }
    
    @Override
    public void clear()
    {
        actualStatus = Status.NOT_STARTED;
        valueKey = null;
        storage = null;
        contacts = null;
        outputBuffer = null;
        result = null;
        distanceFromClosest = MAX_RANGE;
        queriedNodes.clear();
    }
}
