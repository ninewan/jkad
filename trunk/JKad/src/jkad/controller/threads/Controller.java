package jkad.controller.threads;

import java.math.BigInteger;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import jkad.controller.ThreadGroupLocal;
import jkad.controller.io.SingletonSocket;
import jkad.controller.threads.handlers.HandlerThread;
import jkad.controller.threads.handlers.response.PingResponseHandler;
import jkad.protocol.KadProtocol;
import jkad.protocol.rpc.RPC;
import jkad.structures.JKadDatagramSocket;
import jkad.structures.RPCInfo;
import jkad.structures.buffers.RPCBuffer;
import jkad.tools.ToolBox;

import org.apache.log4j.Logger;

public class Controller extends CyclicThread
{
	private static Logger logger = Logger.getLogger(Controller.class);
	
    private static ThreadGroupLocal<BigInteger> myID;
    
    public static BigInteger getMyID()
    {
        if(myID == null)
        {
            myID = new ThreadGroupLocal<BigInteger>()
            {
                public BigInteger initialValue()
                {
                    JKadDatagramSocket socket = SingletonSocket.getInstance();
                    InetAddress ip = socket.getInetAddress() != null ? socket.getInetAddress() : socket.getLocalAddress();
                    Integer port = socket.getPort() != -1 ? socket.getPort() : socket.getLocalPort();
                    String idString = ip.getHostAddress() + ":" + port;
                    logger.debug("Generating ID for " + Thread.currentThread().getThreadGroup().getName() + " with address " + idString);
                    try
                    {
                        MessageDigest digester = MessageDigest.getInstance("SHA-1");
                        byte[] hash = digester.digest(idString.getBytes());
                        BigInteger id = new BigInteger(hash);
                        logger.debug("Generated ID " + id.toString(16));
                        return id;
                    } catch (NoSuchAlgorithmException e)
                    {
                        logger.fatal(e);
                        throw new RuntimeException(e);
                    }
                }
            };
        }
        return myID.get();
    }
    
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
			RPCInfo rpcInfo = inputBuffer.remove();
			RPC rpc = rpcInfo.getRPC();
			String ip = rpcInfo.getIP();
			Integer port = rpcInfo.getPort();
			logger.debug("Processing RPC of type " + rpc.getClass().getSimpleName());
			
            switch(rpc.getType())
            {
                case KadProtocol.PING:
                    PingResponseHandler pingHandler = new PingResponseHandler(rpcInfo);
                    pingHandler.start();
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
