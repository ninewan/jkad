package jkad.controller;

import org.apache.log4j.Logger;

public class JKadShutdownHook extends Thread
{
    private static Logger logger = Logger.getLogger(JKad.class);

    private Thread jkadThread;

    private JKad jkad;

    public JKadShutdownHook(Thread jkadThread, JKad jkad)
    {
        this.jkadThread = jkadThread;
        this.jkad = jkad;
    }

    public void run()
    {
        logger.debug("Shutdown hook called");
        if (jkadThread.isAlive())
        {
            if (!jkad.isStarted())
            {
                logger.warn("System not started, interrupting");
                jkadThread.interrupt();
            } else
            {
                logger.info("Stopping JKad");
                jkad.stopThread();
            }
            while (jkadThread.isAlive())
                try
                {
                    jkadThread.join();
                } catch (InterruptedException e)
                {
                }
        }
        logger.debug("Finished Shutdown hook");
    }
}
