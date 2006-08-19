package jKad.controller.threads;

import org.apache.log4j.Logger;

public abstract class CyclicThread extends Thread implements Pausable, HardStoppable
{
    private boolean paused = false;
    private boolean dead = false;
    private long roundWait = 10;

    protected CyclicThread(String name)
    {
        super(name);
    }
    
    public void run()
    {
        getLogger().debug("Starting Thread");
        while (!isStopped())
        {
            try
            {
                if (isPaused())
                {
                    synchronized (this)
                    {
                        this.wait();
                    }
                } else
                {
                    this.cycleOperation();
                    synchronized (this)
                    {
                        this.wait(roundWait);
                    }
                }
            } catch (InterruptedException e)
            {
                getLogger().debug("Thread interrupted");
                break;
            }
        }
        this.finalize();
    }

    protected abstract Logger getLogger();
    
    protected abstract void cycleOperation() throws InterruptedException;

    protected abstract void finalize();

    public boolean isPaused()
    {
        return paused;
    }

    public void pauseThread()
    {
        getLogger().debug("Pausing Thread");
        paused = true;
    }

    public void playThread()
    {
        getLogger().debug("Starting Thread (returning from pause)");
        paused = false;
        synchronized (this)
        {
            this.notify();
        }
    }

    public boolean isStopped()
    {
        return dead;
    }

    public void stopThread()
    {
        getLogger().debug("Stopping Thread");
        dead = true;
    }

    public void hardStopThread()
    {
        this.stopThread();
        this.interrupt();
    }

    public long getRoundWait()
    {
        return roundWait;
    }

    public void setRoundWait(long roundWait)
    {
        if (roundWait >= 0)
            this.roundWait = roundWait;
    }
}
