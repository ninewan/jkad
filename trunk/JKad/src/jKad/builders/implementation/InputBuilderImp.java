package jKad.builders.implementation;

import jKad.builders.InputBuilder;
import jKad.builders.RPCFactory;
import jKad.protocol.KadProtocolException;
import jKad.protocol.rpc.RPC;
import jKad.tools.ToolBox;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;

public class InputBuilderImp extends InputBuilder {

    private RPCFactory factory;
    
    public InputBuilderImp() {
        factory = RPCFactory.newInstance();
    }

    public RPC buildRPC(Byte[] data) throws KadProtocolException {
        return this.buildRPC(ToolBox.getDataTools().convertArray(data));
    }

    public RPC buildRPC(byte[] data) throws KadProtocolException {
        return this.buildRPC(ByteBuffer.wrap(data));
    }

    public RPC buildRPC(ByteBuffer data) throws KadProtocolException {
        return factory.buildRPC(data.array());
    }

    public RPC buildRPC(DatagramPacket packet) throws KadProtocolException {
        return this.buildRPC(packet.getData());
    }
}
