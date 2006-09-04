/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.controller.io;

import jkad.structures.JKadDatagramSocket;
import jkad.structures.buffers.DatagramBuffer;
import jkad.tools.ToolBox;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;

import org.apache.log4j.Logger;

public class UDPReceiver extends UDPHandler
{
    private static Logger logger = Logger.getLogger(UDPReceiver.class);
    
    private JKadDatagramSocket socket;
    private DatagramBuffer buffer;
    private long receivedPackets;

    public UDPReceiver() throws SocketException
    {
        this(SingletonSocket.getInstance(), DatagramBuffer.getReceivedBuffer());
    }

    public UDPReceiver(JKadDatagramSocket socket, DatagramBuffer buffer) throws SocketException
    {
        super(ToolBox.getReflectionTools().generateThreadName(UDPReceiver.class));
        if (socket == null)
            throw new NullPointerException("socket is null");
        if (buffer == null)
            throw new NullPointerException("buffer is null");
        this.socket = socket;
        this.buffer = buffer;
        this.receivedPackets = 0L;
        
        this.socket.addObserver(this);
    }

    protected void cycleOperation()
    {
        DatagramPacket packet = new DatagramPacket(new byte[256], 256);
        try
        {
            socket.receive(packet);
            logger.debug("Received packet from " + packet.getSocketAddress());
            buffer.add(packet);
            receivedPackets++;
        } catch (SocketException e)
        {
            if (!socket.isClosed() || !this.isInterrupted())
                logger.error(e);
        } catch (IOException e)
        {
            logger.error(e);
        }
    }
    
    protected JKadDatagramSocket getSocket()
    {
        return this.socket;
    }

    protected void finalize()
    {
        if (socket != null)
            socket.close();
        logger.debug("Thread finalized");
    }

    public long getHandledPacketAmount()
    {
        return receivedPackets;
    }

    protected Logger getLogger()
    {
        return logger;
    }
}
