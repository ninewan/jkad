package jKad.builders.implementation.reflection;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;

import jKad.builders.InputBuilder;
import jKad.builders.RPCFactory;
import jKad.protocol.KadProtocolException;
import jKad.protocol.rpc.RPC;
import jKad.tools.ToolBox;

public class ReflectionInputBuilderImp extends InputBuilder {

    public RPC buildRPC(Byte[] data) {
        return this.buildRPC(ToolBox.getDataTools().convertArray(data));
    }
    
    public RPC buildRPC(ByteBuffer data){
//        RPCFactory factory = ReflectionRPCFactoryImp.getFactory();
        RPC rpc = null;
//        try {
//            rpc = factory.buildRPC(data.array());
//        } catch (KadProtocolException e) {
//            e.printStackTrace();
//        }
        return rpc;
    }
    
    public RPC buildRPC(byte[] data){
        return this.buildRPC(ToolBox.getDataTools().convertArray(data));
    }

    public RPC buildRPC(DatagramPacket packet) {
        return this.buildRPC(packet.getData());
    }

}
