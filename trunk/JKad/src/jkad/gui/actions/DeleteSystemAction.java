package jkad.gui.actions;

import java.awt.event.ActionEvent;

import jkad.gui.JKadGUI;

public class DeleteSystemAction extends AbstractKeyboardAction
{
    private static final long serialVersionUID = -1767574029083218196L;
   
    private JKadGUI gui;
    
    public DeleteSystemAction(JKadGUI gui)
    {
        this.gui = gui;
    }
    
    public void actionPerformedImp(ActionEvent e)
    {
        int selected = gui.getSystemsComboBox().getSelectedIndex();
        if(selected != -1)
        {
            gui.removeAndStopSystem(selected);
            try
            {
                Thread.sleep(200);
            } catch (InterruptedException e1) { }
            gui.populateSystems();
        }
    }
}



