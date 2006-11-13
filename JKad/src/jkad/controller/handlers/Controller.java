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
import java.util.Collection;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import jkad.builders.SHA1Digester;
import jkad.controller.ThreadGroupLocal;
import jkad.controller.handlers.Handler.Status;
import jkad.controller.handlers.misc.ContactHandler;
import jkad.controller.handlers.request.FindNodeHandler;
import jkad.controller.handlers.request.FindValueHandler;
import jkad.controller.handlers.request.LoginHandler;
import jkad.controller.handlers.request.StoreHandler;
import jkad.controller.handlers.response.FindNodeResponseHandler;
import jkad.controller.handlers.response.FindValueResponseHandler;
import jkad.controller.handlers.response.PingResponseHandler;
import jkad.controller.handlers.response.StoreResponseHandler;
import jkad.controller.io.JKadDatagramSocket;
import jkad.controller.io.SingletonSocket;
import jkad.controller.threads.CyclicThread;
import jkad.facades.storage.DataManagerFacade;
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
    
    private ContactHandler knowContacts;
	private RPCBuffer inputBuffer;
    
	private HashMap<BigInteger, RequestHandler> rpcIDMap; 
	
	public Controller()
	{
		super(ToolBox.getReflectionTools().generateThreadName(Controller.class));
		knowContacts = new ContactHandler();
		inputBuffer = RPCBuffer.getReceivedBuffer();
		rpcIDMap = new HashMap<BigInteger, RequestHandler>();
		super.setRoundWait(50);
	}
    
    public void run()
    {
        knowContacts.run();
        super.run();
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
    			KadNode senderNode = knowContacts.findContact(rpc.getSenderNodeID());
                if(senderNode == null)
                {
                    logger.debug("Contact " + rpc.getSenderNodeID().toString(16) + " unknown to this system, adding to contact list");
                    senderNode = new KadNode(rpc.getSenderNodeID(), ip, port);
                    AddResult result = knowContacts.addContact(senderNode);
                    if(result.equals(AddResult.CONTACTS_FULL))
                        logger.debug("Contact List Full!");
                } else
                {
                    logger.debug("Contact " + rpc.getSenderNodeID().toString(16) + "  already known to this system, refreshing last access");
                    senderNode.setLastAccess(System.currentTimeMillis());
                }
                logger.debug("Processing RPC of type " + rpc.getClass().getSimpleName());
    			if(rpc.isRequest())
                {
                    switch(rpc.getType())
                    {
                        case KadProtocol.PING:
                            PingResponseHandler pingHandler = new PingResponseHandler();
                            pingHandler.setRPCInfo(rpcInfo);
                            pingHandler.run();
                            break;
                        case KadProtocol.STORE:
                            StoreResponseHandler storeHandler = new StoreResponseHandler();
                            storeHandler.setRPCInfo(rpcInfo);
                            storeHandler.run();
                            break;
                        case KadProtocol.FIND_NODE:
                            FindNodeResponseHandler findNodeHandler = new FindNodeResponseHandler();
                            findNodeHandler.setRPCInfo(rpcInfo);
                            findNodeHandler.setContacts(knowContacts);
                            findNodeHandler.run();
                            break;
                        case KadProtocol.FIND_VALUE:
                            FindValueResponseHandler findValueHandler = new FindValueResponseHandler();
                            findValueHandler.setRPCInfo(rpcInfo);
                            findValueHandler.setContacts(knowContacts);
                            findValueHandler.run();
                            break;
                    }
                } else
                {
                    RequestHandler handler;
                    synchronized (rpcIDMap)
                    {
                        handler = rpcIDMap.get(rpc.getRPCID());
                    }
                    if(handler != null)
                        handler.addResult(rpcInfo);
                    else
                        logger.warn("Received response of a request not requested by this node! (request id: " + rpc.getRPCID().toString(16) + ")");
                }
            } catch (UnknownHostException e)
            {
                logger.warn("Received packet from a invalid ip address: " + ip + ", discarting request");
            }
		}
	}

    public void login(NetLocation anotherNode)
    {
        LoginHandler handler = new LoginHandler();
        BigInteger rpcID = generateRPCID();
        handler.setRpcID(rpcID);
        handler.setBootstrapNode(anotherNode);
        synchronized (rpcIDMap)
        {
           rpcIDMap.put(rpcID, handler);
        }
        LoginEnd task = new LoginEnd(handler, rpcIDMap, logger);
        Timer timer = new Timer(true);
        handler.run();
        timer.schedule(task, Long.parseLong(System.getProperty("jkad.login.time")));
    }
    
	public void store(String key, String data) 
	{
	    this.store(key.getBytes(), data.getBytes());
	}

	public void store(byte[] key, byte[] data) 
	{
        BigInteger bKey = new BigInteger(key);
        BigInteger bData = new BigInteger(data);
        
        FindNodeHandler handler = new FindNodeHandler();
        BigInteger rpcID = generateRPCID();
        handler.setRpcID(rpcID);
        handler.setContacts(knowContacts);
        handler.setSearchedNode(bKey);
        synchronized (rpcIDMap)
        {
           rpcIDMap.put(rpcID, handler);
        } 
        long maxWait = Long.parseLong(System.getProperty("jkad.findnode.maxwait"));
        long startTime = System.currentTimeMillis();
        handler.run();
        while(handler.getStatus() != Status.ENDED && System.currentTimeMillis() - startTime < maxWait)
        {
            try
            {
                Thread.sleep(100);
            } catch (InterruptedException e)
            {
                logger.warn(e);
            }
        }
        synchronized (rpcIDMap)
        {
            rpcIDMap.remove(rpcID);
        }
        
        storeValueOnNodes(handler.getResults(), bKey, bData);
    }

	public String findValue(String key) 
	{
        byte[] result = this.findValue(key.getBytes());
        return result != null ? new String(result) : null;
	}

	public byte[] findValue(byte[] data) 
	{
        FindValueHandler handler = new FindValueHandler();
        BigInteger rpcID = generateRPCID();
        handler.setRpcID(rpcID);
        handler.setStorage(DataManagerFacade.getDataManager());
        handler.setContacts(knowContacts);
        handler.setValueKey(new BigInteger(SHA1Digester.digest(data)));
        synchronized (rpcIDMap)
        {
           rpcIDMap.put(rpcID, handler);
        } 
        long maxWait = Long.parseLong(System.getProperty("jkad.findvalue.maxwait"));
        long startTime = System.currentTimeMillis();
        handler.run();
        while(handler.getStatus() != Status.ENDED && System.currentTimeMillis() - startTime < maxWait)
        {
            try
            {
                Thread.sleep(100);
            } catch (InterruptedException e)
            {
                logger.warn(e);
            }
        }
        synchronized (rpcIDMap)
        {
            rpcIDMap.remove(rpcID);
        }
        return handler.getResult() != null ? handler.getResult().toByteArray() : null;
	}

    private void storeValueOnNodes(Collection<KadNode> nodes, BigInteger key, BigInteger value)
    {
        StoreHandler storeHandler = new StoreHandler();
        for(KadNode node : nodes)
        {
            storeHandler.clear();
            storeHandler.setKey(key);
            storeHandler.setNode(node);
            storeHandler.setRpcID(generateRPCID());
            storeHandler.setValue(value);
            storeHandler.run();
        }
    }
    
    public KnowContacts getKnowContacts()
    {
        return knowContacts;
    }
    
	protected Logger getLogger() 
	{
		return logger;
	}
	
	protected void finalize() 
	{
		
	}
}

class LoginEnd extends TimerTask
{
    private LoginHandler handler;
    private HashMap<BigInteger, RequestHandler> rpcIDMap;
    private Logger controllerLogger;
    
    protected LoginEnd(LoginHandler handler, HashMap<BigInteger, RequestHandler> rpcIDMap, Logger controllerLogger)
    {
        this.handler = handler;
        this.rpcIDMap = rpcIDMap;
        this.controllerLogger = controllerLogger;
    }
    
    public void run()
    {
        synchronized (rpcIDMap)
        {
            controllerLogger.debug("Finalizing login process");
            rpcIDMap.remove(handler.getRpcID());
        }
    }
    
}