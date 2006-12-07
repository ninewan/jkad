package jkad.gui.actions;

import java.awt.event.ActionEvent;
import java.util.List;

import jkad.controller.JKadSystem;
import jkad.facades.user.NetLocation;
import jkad.gui.JKadGUI;

public class AddSystemAction extends AbstractKeyboardAction
{
    private static final long serialVersionUID = 3931236608729875205L;

    private JKadGUI gui;
    
    public AddSystemAction(JKadGUI gui)
    {
        this.gui = gui;
    }

    public void actionPerformedImp(ActionEvent e)
    {
        JKadSystem system = gui.addSystem();
        try
        {
            Thread.sleep(200);
        } catch (InterruptedException e1) { }
        gui.populateSystems();
        if(System.getProperty("addAndLogin", "false").equals("true"))
        {
            List<JKadSystem> systems = gui.getSystems();
            if(systems.size() > 1)
            {
                JKadSystem bootSystem = systems.get(systems.size() - 2);
                system.login(new NetLocation(bootSystem.getIP(), bootSystem.getPort()));
            }
        }
    }
}