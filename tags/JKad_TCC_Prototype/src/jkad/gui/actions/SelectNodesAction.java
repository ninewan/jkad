package jkad.gui.actions;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JList;

import jkad.controller.JKadSystem;
import jkad.gui.JKadGUI;
import jkad.gui.KnowContactsListModel;
import jkad.gui.SystemsListModel;
import jkad.structures.kademlia.KadNode;

public class SelectNodesAction extends AbstractAction
{
    private JKadGUI gui;
    
    public SelectNodesAction(JKadGUI gui)
    {
        this.gui = gui;
    }
    
    public void actionPerformed(ActionEvent e)
    {
        JList nodeList = gui.getNodeList();
        KnowContactsListModel contactsModel = (KnowContactsListModel)gui.getKnowContactsListBox().getModel();
        SystemsListModel systemsModel = (SystemsListModel)gui.getNodeList().getModel();
        List<JKadSystem> systemsList = systemsModel.getSystemList();
        for(KadNode contact : contactsModel)
        {
            for(int i = 0; i < systemsList.size(); i++)
            {
                JKadSystem system = systemsList.get(i);
                if(contact.getNodeID().equals(system.getSystemID()))
                    nodeList.addSelectionInterval(i, i);
            }
        }
    }
}
