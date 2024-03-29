/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import jkad.controller.threads.Pausable;
import jkad.controller.threads.Stoppable;
import jkad.exceptions.PropertiesNotFoundException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class JKad implements Runnable, Pausable, Stoppable
{
    private static Logger logger;

    public static final String DEFAULT_PROPERTY_FILE = "jkad.properties";

    public static final String[] usedProperties = new String[]
    {
    	"jkad.systems.amount", 
    	"jkad.socket.startPort", 
    	"jkad.datagrambuffer.output.size", 
    	"jkad.datagrambuffer.input.size", 
    	"jkad.rpcbuffer.output.size", 
    	"jkad.rpcbuffer.input.size",
    	"jkad.contacts.size",
        "jkad.contacts.refreshPeriod",
        "jkad.contacts.expire",
        "jkad.contacts.findamount",
        "jkad.findvalue.maxqueries",
        "jkad.findvalue.maxwait",
        "jkad.findnode.maxqueries",
        "jkad.findnode.maxwait",
        "jkad.findnode.maxnodes",
        "jkad.login.time"
    };

    private File propFile;

    private List<JKadSystem> systems;

    private boolean started;

    private boolean paused;

    private boolean dead;
    
    private int counter;

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
        counter = 0;
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
        System.getProperties().putAll(properties);
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
            Integer systemsAmount = Integer.parseInt(System.getProperty("jkad.systems.amount"));
            logger.info("Starting JKad with " + systemsAmount + " nodes");
            for (int i = 0; i < systemsAmount; i++)
            {
                this.addSystem();
                try
                {
                    Thread.sleep(100);
                } catch (InterruptedException e)
                {
                }
            }
            started = true;
            logger.info("JKad sucessfully started");
            
//            try
//            {
//                Thread.sleep(1000);
//                systems.get(0).login(new NetLocation("192.168.1.2", 4001));
//            } catch (Exception e)
//            {
//                e.printStackTrace();
//            }
        } catch (NumberFormatException e)
        {
            logger.fatal(e);
            throw e;
        }
    }
    
    public JKadSystem addSystem()
    {
        String nodeName = getBaseName() + (getStartNumber() + counter);
        JKadSystem system = new JKadSystem(nodeName);
        systems.add(system);
        logger.debug("Launching " + nodeName);
        system.start();
        counter++;
        return system;
    }
    
    public Integer getStartNumber()
    {
        return Integer.parseInt(System.getProperty("jkad.systems.start", "0"));
    }
    
    public String getBaseName()
    {
        return System.getProperty("jkad.systems.name", "Node");
    }
    
    public void pauseSystems()
    {
        if (started)
        {
            logger.info("Pause command received, JKad is pausing");
            for (JKadSystem system : systems)
                pauseSystem(system);
        } else
            logger.warn("Cannot pause JKad now: system not fully started");
    }
    
    public void pauseSystem(JKadSystem system)
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

    private void playSystems()
    {
        if (started)
        {
            logger.info("Play command received, JKad is playing");
            for (JKadSystem system : systems)
                this.playSystem(system);
        } else
            logger.warn("Cannot play JKad now: system not fully started");
    }
    
    public void playSystem(JKadSystem system)
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

    public void stopSystems()
    {
        if (started)
        {
            logger.info("Stop command received, JKad going down!");
            for (JKadSystem system : systems)
                stopSystem(system);
        } else
            logger.warn("Cannot stop JKad now: system not fully started");
    }
    
    protected void stopSystem(JKadSystem system)
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