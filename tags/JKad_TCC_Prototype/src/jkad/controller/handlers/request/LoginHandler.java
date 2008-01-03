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

import jkad.controller.handlers.Controller;
import jkad.controller.handlers.RequestHandler;
import jkad.facades.user.NetLocation;
import jkad.protocol.KadProtocolException;
import jkad.protocol.rpc.request.FindNodeRPC;
import jkad.protocol.rpc.response.FindNodeResponse;
import jkad.structures.buffers.RPCBuffer;
import jkad.structures.kademlia.KadNode;
import jkad.structures.kademlia.RPCInfo;

import org.apache.log4j.Logger;

public class LoginHandler extends RequestHandler<FindNodeResponse>
{
    private static Logger logger = Logger.getLogger(LoginHandler.class);
    
    private Status actualStatus;
    
    private NetLocation bootstrapNode;
    
    private BigInteger rpcID;
    
    private RPCBuffer outputBuffer;
    
    private HashSet<BigInteger> queriedNodes;
    
    public LoginHandler()
    {
        actualStatus = Status.NOT_STARTED;
        outputBuffer = RPCBuffer.getSentBuffer();
        bootstrapNode = null;
        rpcID = null;
        queriedNodes = new HashSet<BigInteger>();
    }
    
    public NetLocation getBootstrapNode()
    {
        return bootstrapNode;
    }

    public void setBootstrapNode(NetLocation bootstrapNode)
    {
        this.bootstrapNode = bootstrapNode;
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
        logger.info("Starting login process, bootstraping on " + bootstrapNode);
        requestNode(rpcID, BigInteger.ZERO, bootstrapNode.getIP().getHostAddress(), bootstrapNode.getPort());
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
            BigInteger myID = Controller.getMyID();
            logger.debug("Querying " + ip + ":" + port + " for myself (" + myID.toString(16) + ")");
            rpc.setRPCID(rpcID);
            rpc.setDestinationNodeID(nodeID);
            rpc.setSearchedNodeID(myID);
            rpc.setSenderNodeID(myID);
            RPCInfo<FindNodeRPC> rpcInfo = new RPCInfo<FindNodeRPC>(rpc, ip, port);
            queriedNodes.add(nodeID);
            outputBuffer.add(rpcInfo);
        } catch (KadProtocolException e)
        {
            logger.warn(e);
        }
    }
    
    public void addResult(RPCInfo<FindNodeResponse> rpcInfo)
    {
        FindNodeResponse response = rpcInfo.getRPC();
        BigInteger found = response.getFoundNodeID();
        String foundNodeString = found.toString(16);
        try
        {
            if(!found.equals(Controller.getMyID()))
            {
                if(queriedNodes.contains(found))
                {
                    logger.debug("Node " + foundNodeString + " already queried");
                    logger.debug("Querying node " + foundNodeString);
                } else
                {
                    logger.debug("Querying node " + foundNodeString);
                    KadNode node = new KadNode(found, response.getIpAddressINet(), response.getPortInteger());
                    requestNode(rpcID, node);
                }
            }
        } catch (UnknownHostException e)
        {
            logger.warn(e);
        }
    }

    public Status getStatus()
    {
        return actualStatus;
    }
    
    public void clear()
    {
        actualStatus = Status.NOT_STARTED;
        outputBuffer = RPCBuffer.getSentBuffer();
        bootstrapNode = null;
        rpcID = null;
        queriedNodes.clear();
    }
}
