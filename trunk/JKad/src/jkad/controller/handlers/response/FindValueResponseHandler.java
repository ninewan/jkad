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
import jkad.controller.handlers.Handler;
import jkad.facades.storage.DataManagerFacade;
import jkad.protocol.KadProtocolException;
import jkad.protocol.rpc.request.FindValueRPC;
import jkad.protocol.rpc.response.FindNodeResponse;
import jkad.protocol.rpc.response.FindValueResponse;
import jkad.structures.buffers.RPCBuffer;
import jkad.structures.kademlia.KadNode;
import jkad.structures.kademlia.KnowContacts;
import jkad.structures.kademlia.RPCInfo;

import org.apache.log4j.Logger;

public class FindValueResponseHandler extends Handler<FindValueRPC>
{
	private static Logger logger = Logger.getLogger(FindValueResponseHandler.class);
    
    private Status actualStatus;
    private KnowContacts contacts;
    
    public FindValueResponseHandler()
	{
        actualStatus = Status.NOT_STARTED;
	}
	
    public Status getStatus()
    {
        return actualStatus;
    }
    
    public KnowContacts getContacts()
    {
        return contacts;
    }

    public void setContacts(KnowContacts contacts)
    {
        this.contacts = contacts;
    }

    @SuppressWarnings("unchecked")
    public void run()
    {
        try
        {
            actualStatus = Status.PROCESSING;
            
            RPCInfo<FindValueRPC> rpcInfo = getRPCInfo();
            FindValueRPC rpc = rpcInfo.getRPC();
            String key = rpc.getKey().toString(16);
            logger.info("Processing FindValue request for key " + key + " from " + rpcInfo.getIPAndPort());
            
            DataManagerFacade<String> storage = DataManagerFacade.getDataManager();
            String value = storage.get(rpc.getKey());
            if(value != null)
            {
                logger.debug("Found value " + value + " for key " + key);
                FindValueResponse response = new FindValueResponse();
                response.setSenderNodeID(Controller.getMyID());
                response.setDestinationNodeID(rpc.getSenderNodeID());
                response.setRPCID(rpc.getRPCID());
                response.setValue(new BigInteger(value.getBytes()));
                //TODO: MultiPart values
                response.setPiece((byte)1);
                response.setPieceTotal((byte)1);
                
                RPCInfo<FindValueResponse> responseInfo = new RPCInfo<FindValueResponse>(
                    response, 
                    rpcInfo.getIP(), 
                    rpcInfo.getPort()
                );
                logger.info("Sending FindValueResponse to " + responseInfo.getIPAndPort());
                RPCBuffer.getSentBuffer().add(responseInfo);
            } else
            {
                int amount = Integer.parseInt(System.getProperty("jkad.contacts.findamount"));
                logger.debug("Found no value for key " + key + ", searching for " + amount + " closest nodes");
                List<KadNode> nodes = contacts.findClosestContacts(rpc.getKey(), amount);
                logger.debug("Found " + nodes.size() + " closest nodes to " + key );
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
            }
            
            actualStatus = Status.ENDED;
        } catch (KadProtocolException e)
        {
            logger.warn(e);
        }
    }
    
    public void clear()
    {
        this.actualStatus = Status.NOT_STARTED;
        this.setRPCInfo(null);
    }
}
