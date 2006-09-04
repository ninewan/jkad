/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.builders;

import jkad.builders.implementation.InputBuilderImp;
import jkad.protocol.KadProtocolException;
import jkad.protocol.rpc.RPC;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;

public abstract class InputBuilder
{
    private static InputBuilder instance;

    public static InputBuilder getInstance()
    {
        if (instance == null)
            instance = new InputBuilderImp();
        return instance;
    }

    public abstract RPC buildRPC(Byte[] data) throws KadProtocolException;

    public abstract RPC buildRPC(byte[] data) throws KadProtocolException;

    public abstract RPC buildRPC(ByteBuffer data) throws KadProtocolException;

    public abstract RPC buildRPC(DatagramPacket packet) throws KadProtocolException;
}
