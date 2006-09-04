/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jKad.controller;

import jKad.controller.threads.Pausable;
import jKad.controller.threads.Stoppable;
import jKad.exceptions.PropertiesNotFoundException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class JKad implements Runnable, Pausable, Stoppable
{
    private static Logger logger;

    public static final String DEFAULT_PROPERTY_FILE = "jkad.properties";

    public static final String[] usedProperties = new String[]
    {
    "jkad.systems", "jkad.socket.startPort", "jkad.datagrambuffer.output.size", "jkad.datagrambuffer.input.size", "jkad.rpcbuffer.output.size", "jkad.rpcbuffer.input.size"
    };

    private File propFile;

    private List<JKadSystem> systems;

    private boolean started;

    private boolean paused;

    private boolean dead;

    public JKad()
    {
        this(DEFAULT_PROPERTY_FILE);
    }

    public JKad(String propertiesFileName)
    {
        systems = new ArrayList<JKadSystem>();
        propFile = new File(propertiesFileName);
        started = false;
        paused = false;
        dead = false;
    }

    public List<JKadSystem> getSystems()
    {
        return systems;
    }

    public void run()
    {
        try
        {
            try
            {
                this.initialize();

                this.startSystems();

                while (!isStopped())
                {
                    synchronized (this)
                    {
                        this.wait();
                    }
                    if (isPaused())
                        this.pauseSystems();
                    else
                        this.playSystems();
                }
            } catch (InterruptedException e)
            {
                logger.info("Thread Interrupted");
            }

            this.stopSystems();

            this.joinSystems();

        } catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void initialize() throws IOException, PropertiesNotFoundException
    {
        this.initializeLogger();
        this.printWelcomeMessage();
        this.setProperties();
    }

    private void initializeLogger()
    {
        PropertyConfigurator.configure("log4j.properties");
        logger = Logger.getLogger(this.getClass());
    }

    private void printWelcomeMessage()
    {
        System.out.println("-=====================================================================================-");
        System.out.println("   JKad                                                                                ");
        System.out.println("      @author Bruno Colameo de Arruda Penteado                                         ");
        System.out.println("      @email  polaco@gmail.com                                                         ");
        System.out.println("      @url    http://code.google.com/p/jkad/                                           ");
        System.out.println("              http://sourceforge.net/projects/jkad                                     ");
        System.out.println("-=====================================================================================-");
    }

    private void setProperties() throws IOException, PropertiesNotFoundException
    {
        logger.debug("Reading properties from file " + propFile.getAbsolutePath());
        Properties properties = new Properties();
        properties.load(new FileInputStream(propFile));
        validateProperties(properties);
        logger.debug("All necessary properties found");
        System.setProperties(properties);
    }

    private void validateProperties(Properties properties) throws PropertiesNotFoundException
    {
        List<String> failedProperties = new ArrayList<String>();
        for (int i = 0; i < usedProperties.length; i++)
        {
            if (properties.getProperty(usedProperties[i]) == null)
                failedProperties.add(usedProperties[i]);
        }
        if (failedProperties.size() > 0)
            throw new PropertiesNotFoundException(failedProperties);
    }

    private void startSystems()
    {
        try
        {
            Integer systemsAmount = Integer.parseInt(System.getProperty("jkad.systems"));
            logger.info("Starting JKad with " + systemsAmount + " nodes");
            for (int i = 0; i < systemsAmount; i++)
            {
                String nodeName = "Node" + i;
                JKadSystem system = new JKadSystem(nodeName);
                systems.add(system);
                logger.debug("Launching " + nodeName);
                system.start();
                try
                {
                    Thread.sleep(10);
                } catch (InterruptedException e)
                {
                }
            }
            started = true;
            logger.info("JKad sucessfully started");
        } catch (NumberFormatException e)
        {
            logger.fatal(e);
            throw e;
        }
    }

    public void pauseSystems()
    {
        if (started)
        {
            logger.info("Pause command received, JKad is pausing");
            for (JKadSystem system : systems)
            {
                if (!system.isPaused())
                {
                    logger.debug("Pausing " + system.getName());
                    system.pauseThread();
                    try
                    {
                        Thread.sleep(10);
                    } catch (InterruptedException e)
                    {
                    }
                }
            }
        } else
            logger.warn("Cannot pause JKad now: system not fully started");
    }

    private void playSystems()
    {
        if (started)
        {
            logger.info("Play command received, JKad is playing");
            for (JKadSystem system : systems)
            {
                if (system.isPaused())
                {
                    logger.debug("Playing " + system.getName());
                    system.playThread();
                    try
                    {
                        Thread.sleep(10);
                    } catch (InterruptedException e)
                    {
                    }
                }
            }
        } else
            logger.warn("Cannot play JKad now: system not fully started");
    }

    public void stopSystems()
    {
        if (started)
        {
            logger.info("Stop command received, JKad going down!");
            for (JKadSystem system : systems)
            {
                logger.debug("Stopping " + system.getName());
                system.stopThread();
                try
                {
                    Thread.sleep(10);
                } catch (InterruptedException e)
                {
                }
            }
        } else
            logger.warn("Cannot stop JKad now: system not fully started");
    }

    public JKadSystem removeAndStopSystem(int index)
    {
        JKadSystem system = systems.remove(index);
        system.stopThread();
        return system;
    }

    private void joinSystems() throws InterruptedException
    {
        for (JKadSystem system : systems)
        {
            system.join();
            logger.debug(system.getName() + " joined");
        }
        logger.info("JKad Sucessfully shutdown! cya ;)");
    }

    public boolean isStarted()
    {
        return this.started;
    }

    public boolean isPaused()
    {
        return this.paused;
    }

    public void pauseThread()
    {
        this.paused = true;
        synchronized (this)
        {
            this.notifyAll();
        }
    }

    public void playThread()
    {
        this.paused = false;
        synchronized (this)
        {
            this.notifyAll();
        }
    }

    public boolean isStopped()
    {
        return this.dead;
    }

    public void stopThread()
    {
        this.dead = true;
        synchronized (this)
        {
            this.notifyAll();
        }
    }

    public static void main(String[] args) throws Exception
    {
        JKad jkad = new JKad(args.length > 0 ? args[0] : DEFAULT_PROPERTY_FILE);
        Thread thread = new Thread(jkad);
        thread.start();
        Runtime.getRuntime().addShutdownHook(new JKadShutdownHook(thread, jkad));
    }
}

class JKadShutdownHook extends Thread
{
    private static Logger logger = Logger.getLogger(JKad.class);

    private Thread jkadThread;

    private JKad jkad;

    protected JKadShutdownHook(Thread jkadThread, JKad jkad)
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
