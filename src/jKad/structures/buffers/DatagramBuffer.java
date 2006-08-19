package jKad.structures.buffers;

import jKad.controller.ThreadGroupLocal;

import java.net.DatagramPacket;
import java.util.concurrent.ArrayBlockingQueue;

public class DatagramBuffer extends ArrayBlockingQueue<DatagramPacket>
{
    private static ThreadGroupLocal<DatagramBuffer> receivedBuffer;
    private static ThreadGroupLocal<DatagramBuffer> sentBuffer;
    
    public static DatagramBuffer getSentBuffer()
    {
        if (sentBuffer == null)
        {
            sentBuffer = new ThreadGroupLocal<DatagramBuffer>()
            {
                public DatagramBuffer initialValue()
                {
                    Integer size = Integer.parseInt(System.getProperty("jkad.datagrambuffer.output.size"));
                    return new DatagramBuffer(size);
                }
            };
        }
        return sentBuffer.get();
    }

    public static DatagramBuffer getReceivedBuffer()
    {
        if (receivedBuffer == null)
        {
            receivedBuffer = new ThreadGroupLocal<DatagramBuffer>()
            {
                public DatagramBuffer initialValue()
                {
                    Integer size = Integer.parseInt(System.getProperty("jkad.datagrambuffer.input.size"));
                    return new DatagramBuffer(size);
                }
            };
        }
        return receivedBuffer.get();
    }

    protected DatagramBuffer(int capacity)
    {
        super(capacity, true);
    }
}
