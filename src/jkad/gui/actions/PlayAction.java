package jkad.gui.actions;

import java.awt.event.ActionEvent;

import jkad.controller.JKadSystem;
import jkad.gui.JKadGUI;

public class PlayAction extends AbstractKeyboardAction
{
    private static final long serialVersionUID = 4858393134703618084L;
    
    private JKadGUI gui;
    
    public PlayAction(JKadGUI gui)
    {
        this.gui = gui;
    }
    
    public void actionPerformedImp(ActionEvent e)
    {
        JKadSystem system = gui.getSelectedSystem();
        if(system != null)
            gui.playSystem(system);
    }
}
