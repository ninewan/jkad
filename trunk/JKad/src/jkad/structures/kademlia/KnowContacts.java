package jkad.structures.kademlia;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class KnowContacts 
{
	private SortedSet<KadNode> contactSet;
	private int maxSize;
	
	public KnowContacts()
	{
		this(Integer.parseInt(System.getProperty("jkad.contacts.size")));
	}
	
	public KnowContacts(int maxSize)
	{
		this.maxSize = maxSize;
		this.contactSet = new TreeSet<KadNode>(new KadNodeComparator());
	}
	
	public KadNode addContact(KadNode node)
	{
		if(contactSet.size() < maxSize)
			return contactSet.add(node);
		else
			return false;
	}
	
	public boolean removeContact(KadNode node)
	{
		return contactSet.remove(node);
	}
	
	public boolean removeNewestContact()
	{
		return contactSet.remove(contactSet.first());
	}
}

class KadNodeComparator implements Comparator<KadNode>
{
	public int compare(KadNode node1, KadNode node2) 
	{
		return node1.getCreateTime().compareTo(node2.getCreateTime());
	}
	
}