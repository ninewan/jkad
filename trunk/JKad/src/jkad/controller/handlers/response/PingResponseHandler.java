/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.controller.handlers.response;

import jkad.controller.handlers.Controller;
import jkad.controller.handlers.HandlerThread;
import jkad.protocol.KadProtocolException;
import jkad.protocol.rpc.request.PingRPC;
import jkad.protocol.rpc.response.PingResponse;
import jkad.structures.buffers.RPCBuffer;
import jkad.structures.kademlia.RPCInfo;
import jkad.tools.ToolBox;

import org.apache.log4j.Logger;

public class PingResponseHandler extends HandlerThread
{
    private static Logger logger = Logger.getLogger(PingResponseHandler.class);
    
    private RPCInfo<PingRPC> rpcInfo;
    private Status actualStatus;
    
    public PingResponseHandler(RPCInfo<PingRPC> rpcInfo)
    {
        super(ToolBox.getReflectionTools().generateThreadName(PingResponseHandler.class));
    	this.rpcInfo = rpcInfo;
        this.actualStatus = Status.NOT_STARTED;
    }
    
    public synchronized Status getStatus()
    {
        return actualStatus;
    }

    public void run()
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
            
            actualStatus = Status.KILLED;
        } catch (KadProtocolException e)
        {
            logger.warn(e);
        }
    }
}
