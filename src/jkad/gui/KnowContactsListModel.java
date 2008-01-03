package jkad.gui;

import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractListModel;

import jkad.structures.kademlia.KadNode;

public class KnowContactsListModel extends AbstractListModel implements Iterable<KadNode>
{
    private static final long serialVersionUID = -6982774312738913001L;
    
    private List<KadNode> nodes;

    public KnowContactsListModel(List<KadNode> nodes)
    {
        this.nodes = nodes;
    }
    
    public Object getElementAt(int index)
    {
        return nodes.get(index);
    }

    public int getSize()
    {
        return nodes.size();
    }

    public Iterator<KadNode> iterator()
    {
        return nodes.iterator();
    }
}