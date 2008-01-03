package jkad.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.JComboBox;

import jkad.gui.JKadGUI;

public class ComboScrollAction extends AbstractKeyboardAction
{
    private static final long serialVersionUID = -8972331039059193499L;

    private JKadGUI gui;
    
    public ComboScrollAction(JKadGUI gui)
    {
        this.gui = gui;
    }

    public void actionPerformedImp(ActionEvent e)
    {
        JComboBox comboBox = gui.getSystemsComboBox();
        int itemCount = comboBox.getItemCount();
        if(itemCount > 1)
        {
            if(e.getActionCommand().equals("i"))
                comboBox.setSelectedIndex((comboBox.getSelectedIndex() + itemCount + 1) % itemCount);
            else if(e.getActionCommand().equals("k")) 
                comboBox.setSelectedIndex((comboBox.getSelectedIndex() + itemCount - 1) % itemCount);
        }
    }
}