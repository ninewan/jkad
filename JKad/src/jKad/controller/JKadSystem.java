package jKad.controller;

import jKad.builders.RPCFactory;
import jKad.controller.io.UDPReceiver;
import jKad.controller.io.UDPSender;
import jKad.controller.threads.Pausable;
import jKad.controller.threads.Stoppable;

import java.net.SocketException;

public class JKadSystem extends Thread implements Pausable, Stoppable, Statistical
{
    private UDPReceiver receiver;
    private UDPSender sender;
    
    private RPCFactory rpcFactory;
    private 
    
    private boolean paused;
    private boolean running;

    public JKadSystem(String name)
    {
        super(new ThreadGroup(name), name);
        this.paused = false;
        this.running = false;
    }

    public void run()
    {
        try
        {
            running = true;
            
            receiver = new UDPReceiver();
            sender = new UDPSender();
            
            receiver.start();
            sender.start();
            
            synchronized (this)
            {
                while(running)
                {
                    try { this.wait(); } catch (InterruptedException e){}
                    if(isPaused())
                    {
                        if(!receiver.isPaused())
                            receiver.pauseThread();
                        if(!sender.isPaused())
                            sender.pauseThread();
                    } else
                    {
                        if(receiver.isPaused())
                            receiver.playThread();
                        if(sender.isPaused())
                            sender.playThread();
                    }
                }
            }
            
            receiver.stopThread();
            sender.stopThread();
            
            try
            {
                sender.join();
                receiver.join();
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            
        } catch (SocketException e)
        {
            e.printStackTrace();
        }
    }
    
    protected void finalize() throws Throwable
    {
        if(receiver != null)
            receiver.stopThread();
        if(sender != null)
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
        return receiver.getHandledPacketAmount();
    }

    public long countReceivedRPCs()
    {
        return 0;
    }

    public long countReceivedRPCs(int type)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public long countSentPackets()
    {
        return sender.getHandledPacketAmount();
    }

    public long countSentRPCs()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public long countSentRPCs(int type)
    {
        // TODO Auto-generated method stub
        return 0;
    }
}