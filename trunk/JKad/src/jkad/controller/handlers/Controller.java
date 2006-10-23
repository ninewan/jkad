/* SVN Info:
 * $HeadURL: https://jkad.googlecode.com/svn/trunk/JKad/src/jkad/controller/managers/NetManager.java $
 * $LastChangedRevision: 34 $
 * $LastChangedBy: polaco $                             
 * $LastChangedDate: 2006-09-17 16:06:00 -0300 (dom, 17 set 2006) $  
 */
package jkad.controller.handlers;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;

import jkad.builders.SHA1Digester;
import jkad.controller.ThreadGroupLocal;
import jkad.controller.handlers.request.StoreHandler;
import jkad.controller.handlers.response.PingResponseHandler;
import jkad.controller.io.JKadDatagramSocket;
import jkad.controller.io.SingletonSocket;
import jkad.controller.threads.CyclicThread;
import jkad.facades.user.NetLocation;
import jkad.facades.user.UserFacade;
import jkad.protocol.KadProtocol;
import jkad.protocol.rpc.RPC;
import jkad.structures.buffers.RPCBuffer;
import jkad.structures.kademlia.KadNode;
import jkad.structures.kademlia.KnowContacts;
import jkad.structures.kademlia.RPCInfo;
import jkad.structures.kademlia.KnowContacts.AddResult;
import jkad.tools.ToolBox;

import org.apache.log4j.Logger;

public class Controller extends CyclicThread implements UserFacade
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
                    BigInteger id = SHA1Digester.hash(idString);
                    logger.debug("Generated ID " + id.toString(16));
                    return id;
                }
            };
        }
        return myID.get();
    }
    
    public static BigInteger generateRPCID()
    {
        Long currentTime = System.currentTimeMillis();
        String myID = Controller.getMyID().toString(16);
        String rpcID = myID + currentTime;
        return SHA1Digester.hash(rpcID);
    }
    
    private KnowContacts knowContacts;
	private RPCBuffer inputBuffer;
	private RPCBuffer outputBuffer;
	
	private HashMap<BigInteger, HandlerThread> rpcIDMap; 
	
	public Controller()
	{
		super(ToolBox.getReflectionTools().generateThreadName(Controller.class));
		knowContacts = new KnowContacts();
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
            try
            {
                KadNode senderNode = new KadNode(rpc.getSenderNodeID(), ip, port);
    			AddResult addResult = knowContacts.addContact(senderNode);
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
            } catch (UnknownHostException e)
            {
                logger.warn("Received packet from a invalid ip address: " + ip + ", discarting request");
            }
		}
	}

	public void store(String key, String data) 
	{
		// TODO Auto-generated method stub
		
	}

	public void store(byte[] key, byte[] data) 
	{
		// TODO Auto-generated method stub
		
	}

	public String findValue(String key) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	public byte[] findValue(byte[] data) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	public List<NetLocation> listNodesWithValue(String key) 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	protected Logger getLogger() 
	{
		return logger;
	}
	
	protected void finalize() 
	{
		
	}
}
