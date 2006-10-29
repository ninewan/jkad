/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.controller.handlers.response;

import java.math.BigInteger;
import java.util.List;

import jkad.controller.handlers.Controller;
import jkad.controller.handlers.HandlerThread;
import jkad.controller.handlers.request.FindNodeHandler;
import jkad.protocol.KadProtocolException;
import jkad.protocol.rpc.request.FindNodeRPC;
import jkad.protocol.rpc.response.FindNodeResponse;
import jkad.structures.buffers.RPCBuffer;
import jkad.structures.kademlia.KadNode;
import jkad.structures.kademlia.KnowContacts;
import jkad.structures.kademlia.RPCInfo;
import jkad.tools.ToolBox;

import org.apache.log4j.Logger;

public class FindNodeResponseHandler extends HandlerThread
{
private static Logger logger = Logger.getLogger(FindNodeHandler.class);
    
    private Status actualStatus;
    private RPCInfo<FindNodeRPC> rpcInfo;
    private KnowContacts contacts;
    
    public FindNodeResponseHandler()
    {
        super(ToolBox.getReflectionTools().generateThreadName(FindNodeHandler.class));
        this.actualStatus = Status.NOT_STARTED;
    }
    
    public RPCInfo<FindNodeRPC> getRpcInfo()
    {
        return rpcInfo;
    }

    public void setRpcInfo(RPCInfo<FindNodeRPC> rpcInfo)
    {
        this.rpcInfo = rpcInfo;
    }
    
    public KnowContacts getContacts()
    {
        return contacts;
    }

    public void setContacts(KnowContacts contacts)
    {
        this.contacts = contacts;
    }

    public void run()
    {
        actualStatus = Status.PROCESSING;
        try
        {
            FindNodeRPC rpc = rpcInfo.getRPC();
            String searched = rpc.getSearchedNodeID().toString(16);
            logger.info("Processing FindNode request for node " + searched + " from " + rpcInfo.getIPAndPort());
            int amount = Integer.parseInt(System.getProperty("jkad.contacts.findamount"));
            List<KadNode> nodes = contacts.findClosestContacts(rpc.getSearchedNodeID(), amount);
            logger.debug("Found " + nodes.size() + " closest nodes to " + searched );
            BigInteger myID = Controller.getMyID();
            RPCBuffer buffer = RPCBuffer.getSentBuffer();
            logger.info("Sending " + nodes.size() + " nodes to " + rpcInfo.getIPAndPort());
            for(KadNode node : nodes)
            {
                FindNodeResponse response = new FindNodeResponse();
                response.setSenderNodeID(myID);
                response.setDestinationNodeID(rpc.getRPCID());
                response.setFoundNodeID(node.getNodeID());
                response.setIpAddress(node.getIpAddress());
                response.setPort(node.getPort());
                RPCInfo<FindNodeResponse> responseInfo = new RPCInfo<FindNodeResponse>(
                    response, 
                    rpcInfo.getIP(), 
                    rpcInfo.getPort()
                );
                logger.debug("Sending findNode response (node: " + node + ")  to " + rpcInfo.getIPAndPort());
                buffer.add(responseInfo);
            } 
        } catch (KadProtocolException e)
        {
            logger.warn(e);
        }
        actualStatus = Status.ENDED;
    }

    public Status getStatus()
    {
        return actualStatus;
    }

    @Override
    public void clear()
    {
        this.actualStatus = Status.NOT_STARTED;
    }
}
