/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.structures.kademlia;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

public class KnowContacts implements Iterable<KadNode>
{
    private static final Logger logger = Logger.getLogger(KnowContacts.class);
    
    private static final BigInteger MAX = new BigInteger("2").pow(160);
    
	private List<KadNode> contactList;
	private int maxSize;
    private BigInteger myID;
    private String myIDString;
    
    public enum AddResult
    {
        ADDED,
        ALREADY_ADDED,
        CONTACTS_FULL,
        REPLACED_FARTEST
    }
	
	public KnowContacts(BigInteger myID)
	{
		this(Integer.parseInt(System.getProperty("jkad.contacts.size")), myID);
	}
	
    public KnowContacts(int maxSize, BigInteger myID)
	{
        this.maxSize = maxSize;
		this.contactList = new ArrayList<KadNode>();
        this.myID = myID;
        this.myIDString = myID.toString(16);
    }
	
	public synchronized AddResult addContact(KadNode node)
	{
	    logger.debug("Trying to add contact " + node);
        if(node.getNodeID().equals(myID))
        {
            logger.warn("Cannot add myself to know contacts!");
            return AddResult.ALREADY_ADDED;
        } if(contactList.size() < maxSize)
        {
            KadNode alreadyAdded = findContact(node.getNodeID());
            if(alreadyAdded == null)
            {
                this.addContactOnList(node);
                logger.debug("Contact " + node + " sucessfully added");
                return AddResult.ADDED;
            } else
            {
                logger.debug("Contact " + node + " already added!");
                return AddResult.ALREADY_ADDED;
            }
        } else
        {
            logger.debug("Contact list is full, looking for fartest contact");
            KadNode fartest = getFartestContact();
            logger.debug("Fartest contact is " + fartest);
            int fartestCompare = fartest.getNodeID().compareTo(myID);
            int nodeCompare = fartest.getNodeID().compareTo(node.getNodeID());
            if(nodeCompare * fartestCompare > 0)
            {
                logger.debug("Contact added: " + node + " is closer to " + myIDString + " than " + fartest);
                this.removeContact(fartest);
                this.addContactOnList(node);
                return AddResult.REPLACED_FARTEST;
            } else
            {
                logger.debug("Contact not added: " + node + " is farter to " + myIDString + " than " + fartest);
                return AddResult.CONTACTS_FULL;
            }
        }
	}
    
    private void addContactOnList(KadNode node)
    {
        BigInteger id = node.getNodeID();
        int position = 0;
        while(position < contactList.size())
        {
            if(contactList.get(position).getNodeID().compareTo(id) >= 0)
                break;
            else
                position++;
        }
        contactList.add(position, node);
    }
	
	public synchronized boolean removeContact(KadNode node)
	{
		return contactList.remove(node);
	}
    
    public synchronized boolean removeContact(BigInteger nodeID)
    {
        return contactList.remove(new KadNode(nodeID));
    }
	
	public KadNode findContact(BigInteger nodeID)
	{
		for(Iterator<KadNode> it = contactList.iterator(); it.hasNext();)
		{
			KadNode nextNode = it.next();
            BigInteger nextNodeID = nextNode.getNodeID(); 
            int compare = nextNodeID.compareTo(nodeID);
			if(compare == 0)
				return nextNode;
            else if (compare > 0)
                break;
		}
		return null;
	}
    
    public List<KadNode> findClosestContacts(BigInteger nodeID, int amount)
    {
        List<KadNode> result = new ArrayList<KadNode>();
        Integer position = this.getClosestContactPosition(nodeID);
        if(position != null)
        {
            int left = position; 
            int right = position;
            int compare = nodeID.compareTo(contactList.get(position).getNodeID());
            if(compare > 0)
                right++;
            else
                left--;
            
            int i = 0;
            while(i < amount && left >= 0 && right < contactList.size())
            {
                KadNode leftNode = contactList.get(left);
                KadNode rightNode = contactList.get(right);
                BigInteger leftRange = nodeID.subtract(leftNode.getNodeID());
                BigInteger rightRange = rightNode.getNodeID().subtract(nodeID);
                if(leftRange.compareTo(rightRange) < 0)
                {
                    result.add(leftNode);
                    left--;
                } else
                {
                    result.add(rightNode);
                    right++;
                }
                i++;
            }
            if(i < amount)
            {
                while(i < amount && left >= 0)
                {
                    result.add(contactList.get(left));
                    left--;
                    i++;
                }
                while(i < amount && right < contactList.size())
                {
                    result.add(contactList.get(right));
                    right++;
                    i++;
                }
            }
        } 
        return result;
    }
    
    public KadNode findClosestContact(BigInteger nodeID)
    {
        Integer position = getClosestContactPosition(nodeID);
        return position != null ? contactList.get(position) : null;
    }
    
    private KadNode getFartestContact()
    {
        KadNode node = null;
        if(contactList.size() > 0)
        {
            KadNode firstNode = contactList.get(0);
            if(contactList.size() == 1)
            {
                node = firstNode;
            } else
            {
                KadNode lastNode = contactList.get(contactList.size() - 1);
                BigInteger firstNodeDelta = firstNode.getNodeID().subtract(myID).abs();
                BigInteger lastNodeDelta = lastNode.getNodeID().subtract(myID).abs();
                node = firstNodeDelta.compareTo(lastNodeDelta) >= 0 ? firstNode : lastNode;
            }
        }
        return node;
    }
    
    private Integer getClosestContactPosition(BigInteger nodeID)
    {
        Integer position = null;
        if(contactList.size() > 0)
        {
            boolean converging = true;
            BigInteger range = MAX;
            for(int i = 0; i < contactList.size() && converging; i++)
            {
                KadNode node = contactList.get(i);
                BigInteger newRange = nodeID.subtract(node.getNodeID()).abs();
                converging = newRange.compareTo(range) < 0;
                range = newRange;
                position = i;
            }
            if(!converging)
                position--;
        }
        return position;
    }
    
    public int getMaxSize()
    {
        return this.maxSize;
    }
    
    public int getSize()
    {
        return this.contactList.size();
    }
    
    public Iterator<KadNode> iterator()
    {
        return this.contactList.iterator();
    }
}

class KadNodeIDComparator implements Comparator<KadNode>
{
    public int compare(KadNode node1, KadNode node2) 
	{
        return node1.getNodeID().compareTo(node2.getNodeID());
	}
}