/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.controller.io;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;

import jkad.structures.buffers.DatagramBuffer;
import jkad.tools.ToolBox;

import org.apache.log4j.Logger;

public class UDPSender extends UDPHandler
{
    private static Logger logger = Logger.getLogger(UDPSender.class);

    private JKadDatagramSocket socket;
    private DatagramBuffer buffer;
    private long sentPackets;
    
    public UDPSender() throws SocketException
    {
        this(SingletonSocket.getInstance(), DatagramBuffer.getSentBuffer());
    }
    
    public UDPSender(JKadDatagramSocket socket, DatagramBuffer buffer) throws SocketException
    {
        super(ToolBox.getReflectionTools().generateThreadName(UDPSender.class));
        if(socket == null)
            throw new NullPointerException("socket is null!");
        if(buffer == null)
            throw new NullPointerException("buffer is null!");
        this.socket = socket;
        this.buffer = buffer;
        this.sentPackets = 0L;
        
        this.socket.addObserver(this);
    }

    protected void cycleOperation()
    {
        if (!socket.isClosed())
        {
            try
            {
                while (!buffer.isEmpty())
                {
                    DatagramPacket packet = buffer.remove();
                    socket.send(packet);
                    sentPackets++;
                    logger.debug("Sent packet to " + packet.getSocketAddress());
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    protected JKadDatagramSocket getSocket()
    {
        return socket;
    }
    
    protected void finalize()
    {
        if (socket != null)
            socket.close();
        logger.debug("Thread finalized");
    }
    
    public long getHandledPacketAmount()
    {
        return this.sentPackets;
    }

    protected Logger getLogger()
    {
        return logger;
    }
}
