/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.controller.handlers.request;

import java.math.BigInteger;
import java.net.UnknownHostException;
import java.util.List;

import jkad.controller.handlers.Controller;
import jkad.controller.handlers.RequestHandler;
import jkad.protocol.KadProtocolException;
import jkad.protocol.rpc.request.FindNodeRPC;
import jkad.protocol.rpc.response.FindNodeResponse;
import jkad.structures.buffers.RPCBuffer;
import jkad.structures.kademlia.KadNode;
import jkad.structures.kademlia.KnowContacts;
import jkad.structures.kademlia.RPCInfo;

import org.apache.log4j.Logger;

public class FindNodeHandler extends RequestHandler<FindNodeResponse>
{
    private static Logger logger = Logger.getLogger(FindNodeHandler.class);

    private BigInteger searchedNode;
    private String searchedNodeString;
    
    private BigInteger rpcID;

    private KadNode closestNode;
    private BigInteger distanceFromClosest;

    private Status actualStatus;
    private RPCBuffer outputBuffer;

    private KnowContacts contacts;

    public FindNodeHandler()
    {
        actualStatus = Status.NOT_STARTED;
        distanceFromClosest = MAX_RANGE;
        searchedNode = null;
        searchedNodeString = null;
        rpcID = null;
        outputBuffer = RPCBuffer.getSentBuffer();
    }

    public BigInteger getSearchedNode()
    {
        return searchedNode;
    }

    public void setSearchedNode(BigInteger searchedNode)
    {
        this.searchedNode = searchedNode;
        this.searchedNodeString = searchedNode != null ? searchedNode.toString(16) : null;
    }

    public KnowContacts getContacts()
    {
        return contacts;
    }

    public void setContacts(KnowContacts contacts)
    {
        this.contacts = contacts;
    }

    public Status getStatus()
    {
        return actualStatus;
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
        logger.info("Looking for contact with nodeID " + searchedNodeString);
        closestNode = contacts.findContact(searchedNode);
        // caso nao tenha achado o resultado localmente, procure na rede
        if (closestNode == null)
        {
            logger.debug("Contact " + searchedNodeString + " not found localy, looking for closest nodes");
            int amount = Integer.parseInt(System.getProperty("jkad.contacts.findamount"));
            List<KadNode> closestNodes = contacts.findClosestContacts(searchedNode, amount);
            if (closestNodes.size() > 0)
            {
                for (KadNode node : closestNodes)
                    requestNode(rpcID, node);
            }
            actualStatus = Status.WAITING;
        } else
        {
            logger.info("Node " + searchedNodeString + " found on location " + closestNode.getIpAddress().getHostAddress() + ":" + closestNode.getPort());
            actualStatus = Status.ENDED;
        }
    }

    public void addResult(RPCInfo<FindNodeResponse> rpcInfo)
    {
        FindNodeResponse response = rpcInfo.getRPC();
        BigInteger found = response.getFoundNodeID();
        try
        {
            if(found.equals(searchedNode))
            {
                closestNode = new KadNode(found, rpcInfo.getIP(), rpcInfo.getPort());
                distanceFromClosest = BigInteger.ZERO;
                actualStatus = Status.ENDED;
                logger.info("Node " + searchedNodeString + " found on location " + closestNode.getIpAddress().getHostAddress() + ":" + closestNode.getPort());
            } else
            {
                BigInteger delta = found.subtract(searchedNode).abs();
                if (delta.compareTo(distanceFromClosest) < 0)
                {
                    logger.debug("Found closest node " + found.toString(16) + " to " + searchedNodeString + " (delta: " + delta.toString(16) + ")");
                    closestNode = new KadNode(found, rpcInfo.getIP(), rpcInfo.getPort());
                    distanceFromClosest = delta;
                    requestNode(rpcID, closestNode);
                } else
                {
                    logger.debug("Node " + found.toString(16) + " not closest to " + searchedNodeString + " discarting response (delta " + delta.toString(16) + " greater than " + distanceFromClosest.toString(16) + ")");
                }
            }
        } catch (UnknownHostException e)
        {
            logger.warn(e);
        }
    }

    private void requestNode(BigInteger rpcID, KadNode node)
    {
        this.requestNode(rpcID, node.getNodeID(), node.getIpAddress().getHostAddress(), node.getPort());
    }
    
    private void requestNode(BigInteger rpcID, BigInteger nodeID, String ip, int port)
    {
        FindNodeRPC rpc = new FindNodeRPC();
        try
        {
            logger.debug("Querying " + ip + ":" + port + " for the node " + searchedNodeString);
            rpc.setRPCID(rpcID);
            rpc.setDestinationNodeID(nodeID);
            rpc.setSearchedNodeID(searchedNode);
            rpc.setSenderNodeID(Controller.getMyID());
            RPCInfo<FindNodeRPC> rpcInfo = new RPCInfo<FindNodeRPC>(rpc, ip, port);
            outputBuffer.add(rpcInfo);
        } catch (KadProtocolException e)
        {
            logger.warn(e);
        }
    }

    public KadNode getResult()
    {
        return closestNode;
    }
    
    public void clear()
    {
        actualStatus = Status.NOT_STARTED;
        searchedNode = null;
        searchedNodeString = null;
        closestNode = null;
        rpcID = null;
        distanceFromClosest = MAX_RANGE;
    }
}
