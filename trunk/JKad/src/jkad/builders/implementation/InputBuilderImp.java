/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.builders.implementation;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;

import jkad.builders.InputBuilder;
import jkad.builders.RPCFactory;
import jkad.protocol.KadProtocolException;
import jkad.protocol.rpc.RPC;
import jkad.tools.ToolBox;

public class InputBuilderImp extends InputBuilder
{

    private RPCFactory factory;

    public InputBuilderImp()
    {
        factory = RPCFactory.newInstance();
    }

    public RPC buildRPC(Byte[] data) throws KadProtocolException
    {
        return this.buildRPC(ToolBox.getDataTools().convertArray(data));
    }

    public RPC buildRPC(byte[] data) throws KadProtocolException
    {
        return this.buildRPC(ByteBuffer.wrap(data));
    }

    public RPC buildRPC(ByteBuffer data) throws KadProtocolException
    {
        return factory.buildRPC(data.array());
    }

    public RPC buildRPC(DatagramPacket packet) throws KadProtocolException
    {
        return this.buildRPC(packet.getData());
    }
}
