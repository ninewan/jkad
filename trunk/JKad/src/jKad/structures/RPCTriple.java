package jKad.structures;

import jKad.protocol.rpc.RPC;

public class RPCTriple
{
    private RPC rpc;
    private String ip;
    private int port;
    
    public RPCTriple(RPC rpc, String ip, int port)
    {
        this.rpc = rpc;
        this.ip = ip;
        this.port = port;
    }

    public RPC getRPC()
    {
        return rpc;
    }
    
    public String getIP()
    {
        return ip;
    }

    public int getPort()
    {
        return port;
    }
}
