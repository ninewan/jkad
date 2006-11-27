package jkad.gui.actions;

import java.awt.event.ActionEvent;

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
        gui.addSystem();
        try
        {
            Thread.sleep(200);
        } catch (InterruptedException e1) { }
        gui.populateSystems();
    }
}