package jKad.controller.threads.processors;

import jKad.builders.InputBuilder;
import jKad.controller.threads.CyclicThread;
import jKad.protocol.KadProtocolException;
import jKad.protocol.rpc.RPC;
import jKad.structures.RPCTriple;
import jKad.structures.buffers.DatagramBuffer;
import jKad.structures.buffers.RPCBuffer;
import jKad.tools.ToolBox;

import java.net.DatagramPacket;

import org.apache.log4j.Logger;

public class RPCInputProcessor extends CyclicThread
{
    private static Logger logger = Logger.getLogger(RPCInputProcessor.class);

    private InputBuilder rpcBuilder;
    private DatagramBuffer inputBuffer;
    private RPCBuffer outputBuffer;
    private Long[] receivedRPCsArray;
    
    public RPCInputProcessor()
    {
        super(ToolBox.getReflectionTools().generateThreadName(RPCInputProcessor.class));
        this.rpcBuilder = InputBuilder.getInstance();
        this.inputBuffer = DatagramBuffer.getReceivedBuffer();
        this.outputBuffer = RPCBuffer.getReceivedBuffer();
        this.receivedRPCsArray = new Long[8];
        for (int i = 0; i < receivedRPCsArray.length; i++)
            receivedRPCsArray[i] = 0L;
        this.setRoundWait(50);
    }

    protected void cycleOperation() throws InterruptedException
    {
        while(!inputBuffer.isEmpty())
        {
            DatagramPacket packet = inputBuffer.remove();
            String ip = packet.getAddress().getHostAddress();
            Integer port = packet.getPort();
            logger.debug("Building RPC from " + ip + ":" + port);
            try
            {
                RPC rpc = rpcBuilder.buildRPC(packet);
                logger.debug("Built rpc of type " + rpc.getClass().getSimpleName());
                receivedRPCsArray[rpc.getType()]++;
                outputBuffer.add(new RPCTriple(rpc, ip, port));
            } catch (KadProtocolException e)
            {
                logger.warn(e);
            }
        }
    }

    protected void finalize()
    {
        
    }

    protected Logger getLogger()
    {
        return logger;
    }
    
    public Long countReceivedRPCs()
    {
        Long result = 0L;
        for (int i = 0; i < receivedRPCsArray.length; i++)
            result += receivedRPCsArray[i];
        return result;
    }
    
    public Long countReceivedRPCs(byte type)
    {
        return receivedRPCsArray[type];
    }
}
