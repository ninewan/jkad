/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.controller.processors;

import java.net.DatagramPacket;
import java.net.UnknownHostException;

import jkad.builders.OutputBuilder;
import jkad.controller.threads.CyclicThread;
import jkad.protocol.rpc.RPC;
import jkad.structures.buffers.DatagramBuffer;
import jkad.structures.buffers.RPCBuffer;
import jkad.structures.kademlia.RPCInfo;
import jkad.tools.ToolBox;

import org.apache.log4j.Logger;

public class RPCOutputProcessor extends CyclicThread
{
    private static Logger logger = Logger.getLogger(RPCOutputProcessor.class);
    
    private OutputBuilder packetBuilder;
    private RPCBuffer inputBuffer;
    private DatagramBuffer outputBuffer;
    private Long[] sentRPCsArray;
    
    public RPCOutputProcessor()
    {
        super(ToolBox.getReflectionTools().generateThreadName(RPCOutputProcessor.class));
        this.packetBuilder = OutputBuilder.getInstance();
        this.inputBuffer = RPCBuffer.getSentBuffer();
        this.outputBuffer = DatagramBuffer.getSentBuffer();
        this.sentRPCsArray = new Long[9];
        for (int i = 0; i < sentRPCsArray.length; i++)
            sentRPCsArray[i] = 0L;
        this.setRoundWait(50);
    }
    
    protected void cycleOperation() throws InterruptedException
    {
        while(!inputBuffer.isEmpty())
        {
            RPCInfo removed = inputBuffer.remove();
            RPC rpc = removed.getRPC();
            String ip = removed.getIP();
            Integer port = removed.getPort();
            logger.debug("Building packet for rpc type " + rpc.getClass().getSimpleName());
            try
            {
                DatagramPacket packet = packetBuilder.buildPacket(rpc, ip, port);
                outputBuffer.add(packet);
                sentRPCsArray[rpc.getType()]++;
                logger.debug("Built packet for " + ip + ":" + port);
            } catch (UnknownHostException e)
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
    
    public Long countSentRPCs()
    {
        Long result = 0L;
        for (int i = 0; i < sentRPCsArray.length; i++)
            result += sentRPCsArray[i];
        return result;
    }
    
    public Long countSentRPCs(byte type)
    {
        return sentRPCsArray[type];
    }
}
