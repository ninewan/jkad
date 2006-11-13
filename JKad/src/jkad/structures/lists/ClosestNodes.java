package jkad.structures.lists;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import jkad.structures.kademlia.KadNode;

public class ClosestNodes
{
    private static Logger logger = Logger.getLogger(ClosestNodes.class);
    
    private TreeSet<KadNode> backSet;
    private int maxSize;
    private BigInteger closestNode;
    
    public ClosestNodes(int maxSize, BigInteger closestNode)
    {
        backSet = new TreeSet<KadNode>(new Comparator<KadNode>()
        {
            public int compare(KadNode o1, KadNode o2)
            {
                return o1.getNodeID().compareTo(o2.getNodeID());
            }
        });
        this.maxSize = maxSize;
        this.closestNode = closestNode;
    }
    
    public boolean add(KadNode node)
    {
        boolean result = true;
        if(backSet.size() < maxSize)
        {
            backSet.add(node);
            logger.debug("Added node " + node);
        } else
        {
            BigInteger add = node.getNodeID();
            BigInteger addDelta = add.subtract(closestNode).abs();
            KadNode first = backSet.first();
            BigInteger firstDelta = first.getNodeID().subtract(closestNode).abs();
            if(addDelta.compareTo(firstDelta) < 0)
            {
                backSet.remove(first);
                logger.debug("Removed node " + first);
                backSet.add(node);
                logger.debug("Added node " + node);
            } else 
            {
                KadNode last = backSet.last();
                BigInteger lastDelta = last.getNodeID().subtract(closestNode).abs();
                if(addDelta.compareTo(lastDelta) < 0)
                {
                    backSet.remove(last);
                    logger.debug("Removed node " + last);
                    backSet.add(node);
                    logger.debug("Added node " + node);
                } else
                {
                    logger.debug("Node " + node + " not added");
                    result =  false;
                }
            }
        }
        return result;
    }
    
    public int getSize()
    {
        return backSet.size();
    }
    
    public int getMaxSize()
    {
        return maxSize;
    }
    
    public BigInteger getClosestNode()
    {
        return closestNode;
    }
    
    public List<KadNode> toList()
    {
        List<KadNode> list = new ArrayList<KadNode>();
        for(KadNode node : backSet)
            list.add(node);
        return list;
    }
}