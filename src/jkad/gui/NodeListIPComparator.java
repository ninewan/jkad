package jkad.gui;

import java.util.Comparator;

import jkad.structures.kademlia.KadNode;

public class NodeListIPComparator implements Comparator<KadNode>
{
    public int compare(KadNode node1, KadNode node2)
    {
        int compare = 0;
        byte[] node1Address = node1.getIpAddress().getAddress();
        byte[] node2Address = node2.getIpAddress().getAddress();
        for(int i = 0; i < 4 && compare == 0; i++)
            compare = ((Byte)node1Address[i]).compareTo(node2Address[i]);
        if(compare == 0)
            compare = ((Integer)node1.getPort()).compareTo(node2.getPort());
        return compare;
    }
}

