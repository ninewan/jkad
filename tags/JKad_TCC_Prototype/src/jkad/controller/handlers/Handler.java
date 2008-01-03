/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.controller.handlers;

import jkad.protocol.rpc.RPC;
import jkad.structures.kademlia.RPCInfo;

public abstract class Handler<HandledRPC extends RPC> implements Runnable
{
    private RPCInfo<HandledRPC> rpcInfo;
    
    public enum Status
    {
        NOT_STARTED,
        WAITING,
        PROCESSING,
        ENDED
    }
    
    public RPCInfo<HandledRPC> getRPCInfo()
    {
        return rpcInfo;
    }

    public void setRPCInfo(RPCInfo<HandledRPC> rpcInfo)
    {
        this.rpcInfo = rpcInfo;
    }
    
    public abstract void clear();
    
    public abstract void run();

    public abstract Status getStatus();
    
}
