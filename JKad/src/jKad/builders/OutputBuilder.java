package jKad.builders;

import jKad.builders.implementation.OutputBuilderImp;
import jKad.protocol.rpc.RPC;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public abstract class OutputBuilder
{
    private static OutputBuilder instance;
    
    public static OutputBuilder getInstance()
    {
        if(instance == null)
            instance = new OutputBuilderImp();
        return instance;
    }
    
    public abstract ByteBuffer buildData(RPC rpc);

    public abstract DatagramPacket buildPacket(RPC rpc, String ip, int port) throws UnknownHostException;

    public abstract DatagramPacket buildPacket(RPC rpc, InetAddress ip, int port);

}
