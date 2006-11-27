package jkad.gui;

import java.util.Comparator;

import jkad.structures.kademlia.KadNode;

public class NodeListSystemIDComparator implements Comparator<KadNode>
{
    public int compare(KadNode node1, KadNode node2)
    {
        return node1.getNodeID().compareTo(node2.getNodeID());
    }
}

