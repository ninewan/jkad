package jkad.gui.actions;

import java.awt.event.ActionEvent;

import jkad.controller.JKadSystem;
import jkad.gui.JKadGUI;

public class PauseAction extends AbstractKeyboardAction
{
    private static final long serialVersionUID = 6952790623531161229L;
   
    private JKadGUI gui;
    
    public PauseAction(JKadGUI gui)
    {
        this.gui = gui;
    }
    
    public void actionPerformedImp(ActionEvent e)
    {
        JKadSystem system = gui.getSelectedSystem();
        if(system != null)
            gui.pauseSystem(system);
    }
}
