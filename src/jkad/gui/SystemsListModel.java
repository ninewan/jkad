package jkad.gui;

import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractListModel;

import jkad.controller.JKadSystem;

public class SystemsListModel extends AbstractListModel implements Iterable<JKadSystem>
{
    private static final long serialVersionUID = -6982774312738913001L;
    
    private List<JKadSystem> systems;

    public SystemsListModel(List<JKadSystem> systems)
    {
        this.systems = systems;
    }
    
    public List<JKadSystem> getSystemList()
    {
        return this.systems;
    }
    
    public Object getElementAt(int index)
    {
        JKadSystem system = systems.get(index);
        ListNode result = null;
        if(system != null)
            result = new ListNode(system);
        return result;
    }

    public int getSize()
    {
        return systems.size();
    }

    public Iterator<JKadSystem> iterator()
    {
        return systems.iterator();
    }

}