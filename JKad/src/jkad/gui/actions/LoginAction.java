package jkad.gui.actions;

import java.awt.event.ActionEvent;

import jkad.controller.JKadSystem;
import jkad.facades.user.NetLocation;
import jkad.gui.JKadGUI;

public class LoginAction extends AbstractKeyboardAction
{
    private static final long serialVersionUID = 441617059553599799L;

    private JKadGUI gui;
    
    public LoginAction(JKadGUI gui)
    {
        this.gui = gui;
    }
    
    public void actionPerformedImp(ActionEvent e)
    {
        JKadSystem system = gui.getSelectedSystem();
        try
        {
            system.login(new NetLocation(gui.getIpField().getText(), Integer.parseInt(gui.getPortField().getText())));
        } catch (Exception error)
        {
            System.err.println(error.getMessage());
        }
    }
}

