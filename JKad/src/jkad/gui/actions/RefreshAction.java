package jkad.gui.actions;

import java.awt.event.ActionEvent;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.swing.border.TitledBorder;

import jkad.controller.JKadSystem;
import jkad.gui.JKadGUI;
import jkad.gui.KnowContactsListModel;
import jkad.gui.NodeListIPComparator;
import jkad.gui.NodeListSystemIDComparator;
import jkad.gui.SystemIDComparator;
import jkad.gui.SystemIPComparator;
import jkad.gui.SystemsListModel;
import jkad.structures.kademlia.KadNode;

public class RefreshAction extends AbstractKeyboardAction
{
    private static final long serialVersionUID = 3933710118907136229L;
   
    private JKadGUI gui;
    private HashMap<String, Comparator<KadNode>> kadNodeCompMap;
    private HashMap<String, Comparator<JKadSystem>> systemCompMap;
    
    public RefreshAction(JKadGUI gui)
    {
        this.gui = gui;
        kadNodeCompMap = new HashMap<String, Comparator<KadNode>>();
        kadNodeCompMap.put("IP", new NodeListIPComparator());
        kadNodeCompMap.put("System ID", new NodeListSystemIDComparator());
        systemCompMap = new HashMap<String, Comparator<JKadSystem>>();
        systemCompMap.put("IP", new SystemIPComparator());
        systemCompMap.put("System ID", new SystemIDComparator());
    }
    
    public void actionPerformedImp(ActionEvent e)
    {
        JKadSystem system = gui.getSelectedSystem();
        if(system != null)
        {
            gui.getReceivedPacketsField().setText("" + system.countReceivedPackets());
            gui.getReceveidRPCsField().setText("" + system.countReceivedRPCs());
            gui.getSentPacketsField().setText("" + system.countSentPackets());
            gui.getSentRPCsField().setText("" + system.countSentRPCs());
            gui.getSystemID().setText(idToString(system.getSystemID()));
            
            List<KadNode> knowContacts = system.listKnowContacts();
            String orderMode = (String)gui.getOrderBox().getSelectedItem();
            Collections.sort(knowContacts, kadNodeCompMap.get(orderMode));
            gui.getKnowContactsListBox().setModel(new KnowContactsListModel(knowContacts));
            TitledBorder border = (TitledBorder)gui.getKnowContactsPanel().getBorder();
            border.setTitle("Know Contacts (" + knowContacts.size() + ")");
            
            List<JKadSystem> systems = gui.getSystems();
            synchronized (systems)
            {
                orderMode = (String)gui.getNodesListOrderByCombo().getSelectedItem();
                List<JKadSystem> clone = new ArrayList<JKadSystem>(systems.size());
                clone.addAll(systems);
                Collections.sort(clone, systemCompMap.get(orderMode));
                gui.getNodeList().setModel(new SystemsListModel(clone));
            }

            gui.getKnowContactsPanel().repaint();
            gui.getNodeListContentPane().repaint();
        }
    }
    
    private String idToString(BigInteger id)
    {
        StringBuffer idString = new StringBuffer(id.toString(16).toUpperCase());
        idString.ensureCapacity(21);
        if(id.compareTo(BigInteger.ZERO) >= 0)
            idString.insert(0, "+");
        for(int i = idString.length(); i <= 40; i++)
            idString.insert(1, "0");
        return idString.toString();
    }
}