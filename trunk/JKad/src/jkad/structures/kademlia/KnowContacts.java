package jkad.structures.kademlia;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class KnowContacts 
{
	private List<KadNode> lastSeemList;
	private List<KadNode> contactList;
	private int maxSize;
	
	public KnowContacts()
	{
		this(Integer.parseInt(System.getProperty("jkad.contacts.size")));
	}
	
	public KnowContacts(int maxSize)
	{
		this.maxSize = maxSize;
		this.lastSeemList = new ArrayList<KadNode>(maxSize);
		this.contactList = new ArrayList<KadNode>(maxSize);
	}
	
	public boolean addContact(KadNode node)
	{
		if(contactList.size() < maxSize)
		{
			lastSeemList.add(node);
			contactList.add(node);
		}
	}
	
	private void addLastSeemContact(KadNode node)
	{
		boolean added = false;
		for (int i = 0; i < lastSeemList.size() && !added; i++) 
		{
			if(added = (node.getLastAccess() > lastSeemList.get(i).getLastAccess()))
				lastSeemList.add(i, node);
		}
		if(!added)
			lastSeemList.add(node);
		//todo terminar isso
	}
	
	private void addNodeIDContact(KadNode node)
	{
		
	}
	
	public boolean removeContact(KadNode node)
	{
		return contactSet.remove(node);
	}
	
	public boolean removeLastSeemContact()
	{
		return contactSet.remove(contactSet.last());
	}
	
	public KadNode findContact(BigInteger nodeID)
	{
		KadNode foundNode = null;
		boolean found = false;
		for(Iterator<KadNode> it = contactSet.iterator(); it.hasNext() && !found;)
		{
			KadNode node = it.next(); 
			if(found = (node.getNodeID().equals(nodeID)))
				foundNode = node;
		}
		return foundNode;
	}
}

class KadNodeComparator implements Comparator<KadNode>
{
	public int compare(KadNode node1, KadNode node2) 
	{
		long node1LastAccess = node1.getLastAccess();
		long node2LastAccess = node2.getLastAccess();
		if(node1LastAccess < node2LastAccess)
			return -1;
		else if(node1LastAccess == node2LastAccess)
			return 0;
		else 
			return 1;
	}
	
}