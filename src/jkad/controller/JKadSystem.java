/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.controller;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import jkad.controller.handlers.Controller;
import jkad.controller.io.SingletonSocket;
import jkad.controller.io.UDPReceiver;
import jkad.controller.io.UDPSender;
import jkad.controller.processors.RPCInputProcessor;
import jkad.controller.processors.RPCOutputProcessor;
import jkad.controller.threads.Pausable;
import jkad.controller.threads.Stoppable;
import jkad.controller.threads.systemaccess.AccessObject;
import jkad.facades.user.DetailedInfoFacade;
import jkad.facades.user.NetLocation;
import jkad.facades.user.UserFacade;
import jkad.structures.kademlia.KadNode;
import jkad.structures.kademlia.KnowContacts;

import org.apache.log4j.Logger;

public class JKadSystem extends Thread implements Pausable, Stoppable, DetailedInfoFacade, UserFacade
{
    private static Logger logger = Logger.getLogger(JKadSystem.class);

    private UDPReceiver receiver;
    private UDPSender sender;
    private RPCInputProcessor inputProcessor;
    private RPCOutputProcessor outputProcessor;
    private Controller controller;
    private AccessObject accessObject;
    private boolean paused;
    private boolean running;
    
    private BigInteger systemID;
    private InetAddress ip;
    private int port;

    public JKadSystem(String name)
    {
        super(new ThreadGroup(name), name);
        this.paused = false;
        this.running = false;
    }
    
    public void login(NetLocation anotherNode)
    {
        this.accessObject.login(anotherNode);
    }
    
    public String findValue(String key)
    {
        return this.accessObject.findValue(key);
    }

    public byte[] findValue(byte[] data)
    {
        return this.accessObject.findValue(data);
    }

    public void store(String key, String data)
    {
        this.accessObject.store(key, data);
    }

    public void store(byte[] key, byte[] data)
    {
        this.accessObject.store(key, data);
    }

    public void run()
    {
        try
        {
            logger.info("Initializing " + this.getThreadGroup().getName());
            running = true;

            receiver = new UDPReceiver();
            sender = new UDPSender();
            receiver.start();
            sender.start();

            inputProcessor = new RPCInputProcessor();
            outputProcessor = new RPCOutputProcessor();
            inputProcessor.start();
            outputProcessor.start();
            
            this.controller = new Controller();
            this.accessObject = new AccessObject(this.getThreadGroup(), controller);
            controller.start();
            
            systemID = Controller.getMyID();
            ip = SingletonSocket.getInstance().getLocalAddress();
            port = SingletonSocket.getInstance().getLocalPort();
            
            synchronized (this)
            {
                while (running)
                {
                    try
                    {
                        this.wait();
                    } catch (InterruptedException e)
                    {
                    }
                    if (isPaused())
                    {
                    	if (!controller.isPaused())
                    		controller.pauseThread();
                    	if (!inputProcessor.isPaused())
                            inputProcessor.pauseThread();
                        if (!outputProcessor.isPaused())
                            outputProcessor.pauseThread();
                        if (!receiver.isPaused())
                            receiver.pauseThread();
                        if (!sender.isPaused())
                            sender.pauseThread();
                    } else
                    {
                    	if (receiver.isPaused())
                            receiver.playThread();
                        if (sender.isPaused())
                            sender.playThread();
                    	if (inputProcessor.isPaused())
                            inputProcessor.playThread();
                        if (outputProcessor.isPaused())
                            outputProcessor.playThread();
                        if (controller.isPaused())
                        	controller.playThread();
                    }
                }
            }

            logger.info("Stopping " + this.getThreadGroup().getName());

            controller.stopThread();
            
            inputProcessor.stopThread();
            outputProcessor.stopThread();

            receiver.stopThread();
            sender.stopThread();

            try
            {
            	logger.debug("Joining with " + controller.getName());
                controller.join();
            	logger.debug("Joining with " + inputProcessor.getName());
                inputProcessor.join();
                logger.debug("Joining with " + outputProcessor.getName());
                outputProcessor.join();
                logger.debug("Joining with " + sender.getName());
                sender.join();
                logger.debug("Joining with " + receiver.getName());
                receiver.join();
            } catch (InterruptedException e)
            {
                logger.warn(e);
            }

        } catch (SocketException e)
        {
            logger.error(e);
        }
    }

    protected void finalize() throws Throwable
    {
        if (controller != null)
        	controller.stopThread();
    	if (inputProcessor != null)
            inputProcessor.stopThread();
        if (outputProcessor != null)
            outputProcessor.stopThread();
        if (receiver != null)
            receiver.stopThread();
        if (sender != null)
            sender.stopThread();
        super.finalize();
    }

    public boolean isPaused()
    {
        return paused;
    }

    public void pauseThread()
    {
        this.paused = true;
        synchronized (this)
        {
            notifyAll();
        }
    }

    public void playThread()
    {
        this.paused = false;
        synchronized (this)
        {
            notifyAll();
        }
    }

    public boolean isStopped()
    {
        return !running;
    }

    public void stopThread()
    {
        this.running = false;
        synchronized (this)
        {
            notifyAll();
        }
    }

    public long countReceivedPackets()
    {
        return receiver != null ? receiver.getHandledPacketAmount() : 0;
    }

    public long countReceivedRPCs()
    {
        return inputProcessor != null ? inputProcessor.countReceivedRPCs() : 0;
    }

    public long countReceivedRPCs(byte type)
    {
        return inputProcessor != null ? inputProcessor.countReceivedRPCs(type) : 0;
    }

    public long countSentPackets()
    {
        return sender != null ? sender.getHandledPacketAmount() : 0;
    }

    public long countSentRPCs()
    {
        return outputProcessor != null ? outputProcessor.countSentRPCs() : 0;
    }

    public long countSentRPCs(byte type)
    {
        return outputProcessor != null ? outputProcessor.countSentRPCs(type) : 0;
    }

    public BigInteger getSystemID()
    {
        return systemID;
    }
    
    public InetAddress getIP()
    {
        return ip;
    }

    public int getPort()
    {
        return port;
    }
    
    public List<KadNode> listKnowContacts()
    {
        List<KadNode> list = new ArrayList<KadNode>();
        KnowContacts contacts = controller.getKnowContacts();
        synchronized (contacts)
        {
            for(KadNode node : contacts)
                list.add(node);
        }
        return list;
    }
    
    public String toString()
    {
        return this.getName();
    }
}