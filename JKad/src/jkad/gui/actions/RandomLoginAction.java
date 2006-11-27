package jkad.gui.actions;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JProgressBar;

import org.apache.log4j.Logger;

import jkad.controller.JKadSystem;
import jkad.facades.user.NetLocation;
import jkad.gui.JKadGUI;

public class RandomLoginAction extends AbstractAction
{
    private static final long serialVersionUID = 9188570705606119909L;
    
    private JKadGUI gui;

    public RandomLoginAction(JKadGUI gui)
    {
        this.gui = gui;
    }
    
    public void actionPerformed(ActionEvent e)
    {
        Thread worker = new WorkerThread(gui);
        worker.start();
    }
}

class WorkerThread extends Thread
{
    private static final Logger logger = Logger.getLogger(RandomLoginAction.class);
    
    private JKadGUI gui;

    public WorkerThread(JKadGUI gui)
    {
        this.gui = gui;
    }
    
    public void run()
    {
        List<JKadSystem> systems = gui.getSystems();
        String intervalString = gui.getRandomLoginIntervalField().getText();
        Integer interval = intervalString != null && intervalString.length() > 0 ? Integer.parseInt(intervalString) : 0;
        if(systems != null && systems.size() > 1)
        {
            JProgressBar progressBar = gui.getRandomLoginProgressBar();
            JButton randomLoginButton = gui.getRandomLoginButton();
            randomLoginButton.setVisible(false);
            progressBar.setValue(progressBar.getMinimum());
            progressBar.setVisible(true);
            try
            {
                Integer[] ports = new Integer[systems.size()];
                for(int i = 0; i < systems.size(); i++)
                    ports[i] = systems.get(i).getPort();
                
                int progressUnit = (progressBar.getMaximum() - progressBar.getMinimum()) / systems.size();
                for(JKadSystem system : systems)
                {
                    int randomPort = system.getPort();
                    while(randomPort == system.getPort())
                        randomPort = ports[(int)(Math.random() * ports.length)];
                    progressBar.setString(system.getName() + " on " + system.getIP().toString() + ":" + randomPort);
                    logger.info("Bootstrapping " + system.getName() + " on " + system.getIP().toString() + ":" + randomPort );
                    system.login(new NetLocation(system.getIP(), randomPort));
                    long currTime = System.currentTimeMillis();
                    while(System.currentTimeMillis() < currTime + interval)
                    {
                        try { Thread.sleep(interval); } catch (InterruptedException e1) { }
                    }
                    progressBar.setValue(progressBar.getValue() + progressUnit);
                }
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
            progressBar.setVisible(false);
            randomLoginButton.setVisible(true);
        }
    }
}