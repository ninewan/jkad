/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.structures.kademlia;

import jkad.protocol.rpc.RPC;

public class RPCInfo<T extends RPC>
{
    private T rpc;

    private String ip;

    private int port;

    public RPCInfo(T rpc, String ip, int port)
    {
        this.rpc = rpc;
        this.ip = ip;
        this.port = port;
    }

    public String getIP()
    {
        return ip;
    }

    public int getPort()
    {
        return port;
    }

    public T getRPC()
    {
        return rpc;
    }
    
    public String getIPAndPort()
    {
        return ip + ":" + port;
    }
}