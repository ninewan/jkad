/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.structures.buffers;

import java.util.concurrent.ArrayBlockingQueue;

import jkad.controller.ThreadGroupLocal;
import jkad.structures.RPCInfo;

public class RPCBuffer extends ArrayBlockingQueue<RPCInfo>
{
    private static final long serialVersionUID = -325461731321155725L;

    private static ThreadGroupLocal<RPCBuffer> receivedBuffer;

    private static ThreadGroupLocal<RPCBuffer> sentBuffer;

    public static RPCBuffer getSentBuffer()
    {
        if (sentBuffer == null)
        {
            sentBuffer = new ThreadGroupLocal<RPCBuffer>()
            {
                public RPCBuffer initialValue()
                {
                    Integer size = Integer.parseInt(System.getProperty("jkad.datagrambuffer.output.size"));
                    return new RPCBuffer(size);
                }
            };
        }
        return sentBuffer.get();
    }

    public static RPCBuffer getReceivedBuffer()
    {
        if (receivedBuffer == null)
        {
            receivedBuffer = new ThreadGroupLocal<RPCBuffer>()
            {
                public RPCBuffer initialValue()
                {
                    Integer size = Integer.parseInt(System.getProperty("jkad.datagrambuffer.input.size"));
                    return new RPCBuffer(size);
                }
            };
        }
        return receivedBuffer.get();
    }

    protected RPCBuffer(int capacity)
    {
        super(capacity, true);
    }
}
