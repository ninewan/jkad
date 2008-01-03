package jkad.gui.actions;

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.text.JTextComponent;

public abstract class AbstractKeyboardAction extends AbstractAction
{
    public void actionPerformed(ActionEvent e)
    {
        Component focused = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
        if (!(focused instanceof JTextComponent))
            actionPerformedImp(e);
    }
    
    public abstract void actionPerformedImp(ActionEvent e);

}
