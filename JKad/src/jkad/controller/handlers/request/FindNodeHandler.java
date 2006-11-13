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
import jkad.controller.handlers.Handler.Status;
import jkad.protocol.KadProtocolException;
import jkad.protocol.rpc.request.FindNodeRPC;
import jkad.protocol.rpc.response.FindNodeResponse;
import jkad.structures.buffers.RPCBuffer;
import jkad.structures.kademlia.KadNode;
import jkad.structures.kademlia.KnowContacts;
import jkad.structures.kademlia.RPCInfo;
import jkad.structures.lists.ClosestNodes;

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
    
    private ClosestNodes results;
    
    private Set<BigInteger> queriedNodes;
    private int maxQueries;

    public FindNodeHandler()
    {
        actualStatus = Status.NOT_STARTED;
        distanceFromClosest = MAX_RANGE;
        searchedNode = null;
        searchedNodeString = null;
        rpcID = null;
        outputBuffer = RPCBuffer.getSentBuffer();
        queriedNodes = new HashSet<BigInteger>();
        maxQueries = Integer.parseInt(System.getProperty("jkad.findnode.maxqueries"));
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
        synchronized (actualStatus)
        {
            return actualStatus;
        }
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
        results = new ClosestNodes(Integer.parseInt(System.getProperty("jkad.findnode.maxnodes")), searchedNode);
        logger.info("Looking for closest nodes to " + searchedNodeString);
        closestNode = contacts.findContact(searchedNode);
        if(closestNode != null)
            results.add(closestNode);
        int amount = Integer.parseInt(System.getProperty("jkad.contacts.findamount"));
        List<KadNode> closestNodes = contacts.findClosestContacts(searchedNode, amount);
        if (closestNodes.size() > 0)
        {
            for (KadNode node : closestNodes)
                requestNode(rpcID, node);
        }
        actualStatus = Status.WAITING;
    }

    public void addResult(RPCInfo<FindNodeResponse> rpcInfo)
    {
        FindNodeResponse response = rpcInfo.getRPC();
        BigInteger found = response.getFoundNodeID();
        String foundNodeString = found.toString(16);
        try
        {
            if(queriedNodes.contains(found))
            {
                logger.debug("Node " + foundNodeString + " already queried");
            } else if (queriedNodes.size() < maxQueries)
            {
                logger.debug("Found node " + foundNodeString);
                closestNode = new KadNode(found, response.getIpAddressINet(), response.getPortInteger());
                results.add(closestNode);
                BigInteger delta = found.subtract(searchedNode).abs();
                if (delta.compareTo(distanceFromClosest) < 0)
                {
                    distanceFromClosest = delta;
                    requestNode(rpcID, closestNode);
                }
            } else
            {
                logger.debug("Max queries reached, terminating find node");
                actualStatus = Status.ENDED;
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
            queriedNodes.add(nodeID);
        } catch (KadProtocolException e)
        {
            logger.warn(e);
        }
    }

    public synchronized List<KadNode> getResults()
    {
        return results.toList();
    }
    
    public void clear()
    {
        actualStatus = Status.NOT_STARTED;
        searchedNode = null;
        searchedNodeString = null;
        closestNode = null;
        rpcID = null;
        distanceFromClosest = MAX_RANGE;
        results = null;
        queriedNodes.clear();
    }
}
