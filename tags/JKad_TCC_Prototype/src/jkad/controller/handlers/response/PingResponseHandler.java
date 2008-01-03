/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.controller.handlers.response;

import jkad.controller.handlers.Controller;
import jkad.controller.handlers.Handler;
import jkad.protocol.KadProtocolException;
import jkad.protocol.rpc.request.PingRPC;
import jkad.protocol.rpc.response.PingResponse;
import jkad.structures.buffers.RPCBuffer;
import jkad.structures.kademlia.RPCInfo;

import org.apache.log4j.Logger;

public class PingResponseHandler extends Handler<PingRPC>
{
    private static Logger logger = Logger.getLogger(PingResponseHandler.class);
    
    private Status actualStatus;
    
    public PingResponseHandler()
    {
        this.actualStatus = Status.NOT_STARTED;
    }
    
    public synchronized Status getStatus()
    {
        return actualStatus;
    }

    public void run()
    {
        RPCInfo<PingRPC> rpcInfo = getRPCInfo();
        if(getRPCInfo() != null)
        {
            try
            {
                actualStatus = Status.PROCESSING;
                logger.info("Processing Ping request from " + rpcInfo.getIPAndPort());
                PingRPC rpc = rpcInfo.getRPC();
                PingResponse response = new PingResponse();
                
                response.setDestinationNodeID(rpc.getDestinationNodeID());
                response.setRPCID(rpc.getRPCID());
                response.setSenderNodeID(Controller.getMyID());
                
                RPCInfo<PingResponse> responseInfo = new RPCInfo<PingResponse>(
                    response, 
                    rpcInfo.getIP(), 
                    rpcInfo.getPort()
                );
                
                logger.info("Sending PingResponse to " + rpcInfo.getIPAndPort());
                RPCBuffer.getSentBuffer().add(responseInfo);
                
                actualStatus = Status.ENDED;
            } catch (KadProtocolException e)
            {
                logger.warn(e);
            }
        } else
            throw new NullPointerException("Cannot proccess a ping request for a null rpcInfo");
    }

    public void clear()
    {
        this.actualStatus = Status.NOT_STARTED;
        setRPCInfo(null);
    }
}
