package jkad.controller.threads;

import java.math.BigInteger;
import java.util.HashMap;

import jkad.controller.threads.handlers.HandlerThread;
import jkad.protocol.KadProtocol;
import jkad.protocol.rpc.RPC;
import jkad.structures.RPCTriple;
import jkad.structures.buffers.RPCBuffer;
import jkad.tools.ToolBox;

import org.apache.log4j.Logger;

public class Controller extends CyclicThread
{
	private static Logger logger = Logger.getLogger(Controller.class);
	
	private RPCBuffer inputBuffer;
	private RPCBuffer outputBuffer;
	
	private HashMap<BigInteger, HandlerThread> rpcIDMap; 
	
	public Controller()
	{
		super(ToolBox.getReflectionTools().generateThreadName(Controller.class));
		inputBuffer = RPCBuffer.getReceivedBuffer();
		outputBuffer = RPCBuffer.getSentBuffer();
		rpcIDMap = new HashMap<BigInteger, HandlerThread>();
		super.setRoundWait(50);
	}
	
	protected void cycleOperation() throws InterruptedException 
	{
		while(!inputBuffer.isEmpty())
		{
			RPCTriple rpcInfo = inputBuffer.remove();
			RPC rpc = rpcInfo.getRPC();
			String ip = rpcInfo.getIP();
			Integer port = rpcInfo.getPort();
			logger.debug("Processing RPC of type " + rpc.getClass().getSimpleName());
			
			HandlerThread handler = rpcIDMap.get(rpc.getRPCID());
			if(handler == null)
			{
				logger.debug("RPC is a new request");
				//TODO criacao de handler
			}
			
			switch(rpc.getType())
			{
				case KadProtocol.PING:
					//TODO Ping Handler
				case KadProtocol.STORE:
					//TODO Store Handler
				case KadProtocol.FIND_NODE:
					//TODO Find Node Handler
				case KadProtocol.FIND_VALUE:
					//TODO Find Value Handler
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
	
}
