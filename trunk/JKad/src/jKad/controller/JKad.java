package jKad.controller;

import jKad.exceptions.PropertiesNotFoundException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class JKad implements Runnable
{
    private static Logger logger;
    
    public static final String DEFAULT_PROPERTY_FILE = "jkad.properties";
    
    public static final String[] usedProperties = new String[]
    {
        "jkad.systems",
        "jkad.socket.startPort",
        "jkad.datagrambuffer.output.size",
        "jkad.datagrambuffer.input.size"
    };
    
    private File propFile;
    private JKadSystem[] systems;
    private boolean started;
    
    public JKad(String propertiesFileName)
    {
        propFile = new File(propertiesFileName);
        started = false;
    }
    
    public void run()
    {
        try
        {
            this.initialize();
            
            this.startSystem();
            
            Thread.sleep(5000);
            
            this.stopSystem();
            
            this.joinSystem();
            
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
        logger.info("-===================================================-");
        logger.info("   JKad                                              ");
        logger.info("     @author Bruno Colameo de Arruda Penteado        ");
        logger.info("     @email  polaco@gmail.com                        ");
        logger.info("     @url    http://sourceforge.net/projects/jkad    ");
        logger.info("-===================================================-");
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
            if(properties.getProperty(usedProperties[i]) == null)
                failedProperties.add(usedProperties[i]);
        }
        if(failedProperties.size() > 0)
            throw new PropertiesNotFoundException(failedProperties);
    }
    
    private void startSystem()
    {
        try
        {
            Integer systemsAmount = Integer.parseInt(System.getProperty("jkad.systems"));
            systems = new JKadSystem[systemsAmount];
            logger.info("Starting JKad with " + systemsAmount + " nodes");
            for (int i = 0; i < systems.length; i++)
            {
                String nodeName = "Node" + i;
                JKadSystem system = new JKadSystem(nodeName);
                systems[i] = system;
                logger.debug("Launching " + nodeName);
                system.start();
                try { Thread.sleep(10); } catch (InterruptedException e) {}
            }
            started = true;
            logger.info("JKad sucessfully started");
        } catch (NumberFormatException e) {
            logger.fatal(e);
            throw e;
        }
    }
    
    public void stopSystem()
    {
        if(started)
        {
            logger.info("Stop command received, JKad going down!");
            for (JKadSystem system : systems)
            {
                logger.debug("Stopping " + system.getName());
                system.stopThread();
                try { Thread.sleep(10); } catch (InterruptedException e) {}
            }
        } else
            logger.warn("Cannot stop JKad now: system not fully started");
    }
    
    private void joinSystem() throws InterruptedException
    {
        for (int i = 0; i < systems.length; i++)
        {
            systems[i].join();
            logger.debug(systems[i].getName() + " joined");
        }
        logger.info("JKad Sucessfully shutdown! cya ;)");
    }
    
    public static void main(String[] args) throws IOException
    {
        JKad jkad = new JKad(args.length > 0 ? args[0] : DEFAULT_PROPERTY_FILE);
        jkad.run();
    }

}
