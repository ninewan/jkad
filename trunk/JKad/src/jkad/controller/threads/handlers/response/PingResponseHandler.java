/* SVN Info:
 * $HeadURL: https://jkad.googlecode.com/svn/trunk/JKad/src/jkad/controller/threads/handlers/FindNodeHandler.java $
 * $LastChangedRevision: 26 $
 * $LastChangedBy: polaco $                             
 * $LastChangedDate: 2006-09-04 22:41:08 -0300 (seg, 04 set 2006) $  
 */
package jkad.controller.threads.handlers.response;

import jkad.controller.threads.Controller;
import jkad.controller.threads.handlers.HandlerThread;
import jkad.protocol.KadProtocolException;
import jkad.protocol.rpc.request.PingRPC;
import jkad.protocol.rpc.response.PingResponse;
import jkad.structures.RPCInfo;
import jkad.structures.buffers.RPCBuffer;

import org.apache.log4j.Logger;

public class PingResponseHandler extends HandlerThread
{
    private static Logger logger = Logger.getLogger(PingResponseHandler.class);
    
    private RPCInfo<PingRPC> rpcInfo;
    private Status actualStatus;
    
    public PingResponseHandler(RPCInfo<PingRPC> rpcInfo)
    {
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
            
            RPCBuffer.getSentBuffer().add(responseInfo);
            
            actualStatus = Status.KILLED;
        } catch (KadProtocolException e)
        {
            logger.warn(e);
        }
    }
}
