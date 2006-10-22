package jkad.structures.kademlia;

import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import jkad.structures.kademlia.KnowContacts.AddResult;

import junit.framework.TestCase;

public class KnowContactsTest extends TestCase
{
    private static final Integer MAX_SIZE = 20;
    
    private InetAddress defaultIP;
    private int defaultPort;
    
    protected void setUp() throws Exception
    {
        super.setUp();
        System.setProperty("jkad.contacts.size", MAX_SIZE.toString());
        defaultIP = Inet4Address.getByName("10.0.0.1");
        defaultPort = 1234;
    }
    
    public void testKnowContacts()
    {
        KnowContacts contacts = new KnowContacts();
        assertEquals(contacts.getMaxSize(), 20);
    }

    public void testKnowContactsInt()
    {
        KnowContacts contacts = new KnowContacts(50);
        assertEquals(contacts.getMaxSize(), 50);
    }

    public void testAddContact()
    {
        KnowContacts contacts = new KnowContacts();
        assertEquals(contacts.addContact(createNode(0 + "")), AddResult.ADDED);
        assertEquals(contacts.addContact(createNode(0 + "")), AddResult.ALREADY_ADDED);
        for(int i = 1; i < MAX_SIZE; i++)
            assertEquals(contacts.addContact(createNode(i + "")), AddResult.ADDED);
        assertEquals(contacts.addContact(createNode(999 + "")), AddResult.CONTACTS_FULL);
    }

//    public void testRemoveContact()
//    {
//        KnowContacts contacts = getFilledContacts();
//        contacts.removeContact(node);
//    }

    public void testFindContact()
    {
        KnowContacts contacts = getFilledContacts();
        KadNode node = contacts.findContact(new BigInteger("20"));
        assertEquals(node.getNodeID(), new BigInteger("20"));
        
        node = contacts.findContact(new BigInteger("25"));
        assertNull(node);
    }

    public void testFindClosestContacts()
    {
        KnowContacts contacts = getFilledContacts();
        List<KadNode> nodes;
        String[] closestIds;
        Comparator comp = new Comparator<KadNode>()
        {
            public int compare(KadNode o1, KadNode o2)
            {
                return o1.getNodeID().compareTo(o2.getNodeID());
            }
        };
        
        nodes = contacts.findClosestContacts(new BigInteger("20"), 5);
        Collections.sort(nodes, comp);
        closestIds = new String[]{"0", "10", "20", "30", "40"};
        for(int i = 0; i < nodes.size(); i++)
            assertEquals(nodes.get(i).getNodeID(), new BigInteger(closestIds[i]));
        
        nodes = contacts.findClosestContacts(new BigInteger("23"), 5);
        Collections.sort(nodes, comp);
        closestIds = new String[]{"0", "10", "20", "30", "40"};
        for(int i = 0; i < nodes.size(); i++)
            assertEquals(nodes.get(i).getNodeID(), new BigInteger(closestIds[i]));
        
        nodes = contacts.findClosestContacts(new BigInteger("88"), 5);
        Collections.sort(nodes, comp);
        closestIds = new String[]{"70" ,"80", "90", "100", "110"};
        for(int i = 0; i < nodes.size(); i++)
            assertEquals(nodes.get(i).getNodeID(), new BigInteger(closestIds[i]));
        
        nodes = contacts.findClosestContacts(new BigInteger("-10"), 5);
        Collections.sort(nodes, comp);
        closestIds = new String[]{"0", "10", "20", "30", "40"};
        for(int i = 0; i < nodes.size(); i++)
            assertEquals(nodes.get(i).getNodeID(), new BigInteger(closestIds[i]));
        
        nodes = contacts.findClosestContacts(new BigInteger("250"), 5);
        Collections.sort(nodes, comp);
        closestIds = new String[]{"150", "160", "170", "180", "190"};
        for(int i = 0; i < nodes.size(); i++)
            assertEquals(nodes.get(i).getNodeID(), new BigInteger(closestIds[i]));
        
        nodes = contacts.findClosestContacts(new BigInteger("120"), 500);
        Collections.sort(nodes, comp);
        assertEquals(nodes.size(), MAX_SIZE.intValue());
        for(int i = 0; i < nodes.size(); i++)
            assertEquals(nodes.get(i).getNodeID(), new BigInteger((i * 10) + ""));
        
        List<KadNode> randomList = new ArrayList<KadNode>();
        contacts = getRandomFilledContacts(randomList);
        nodes = contacts.findClosestContacts(new BigInteger("50"), 10);
        Collections.sort(nodes, comp);
        Collections.sort(randomList, comp);
        
        int i = 0;
        KadNode node = randomList.get(0);
        while(!randomList.get(i).getNodeID().equals(nodes.get(0).getNodeID()))
            i++;
        for(int j = 0; j < nodes.size(); j++, i++)
            assertEquals(nodes.get(j).getNodeID(), randomList.get(i).getNodeID());
        
        System.out.println();
    }

    public void testFindClosestContact()
    {
        KnowContacts contacts = getFilledContacts();
        KadNode node = contacts.findClosestContact(new BigInteger("23"));
        assertEquals(node.getNodeID(), new BigInteger("20"));
        node = contacts.findClosestContact(new BigInteger("28"));
        assertEquals(node.getNodeID(), new BigInteger("30"));
    }
    
    private KadNode createNode(String nodeID)
    {
        KadNode node = new KadNode();
        node.setIpAddress(defaultIP);
        node.setPort(defaultPort);
        node.setLastAccess(System.currentTimeMillis());
        node.setNodeID(new BigInteger(nodeID));
        return node;
    }
   
    private KnowContacts getFilledContacts()
    {
        KnowContacts contacts = new KnowContacts();
        for(int i = 0; i < MAX_SIZE; i++)
            contacts.addContact(createNode((i * 10 ) + ""));
        return contacts;
    }
    
    private KnowContacts getRandomFilledContacts(List<KadNode> contactList)
    {
        List<KadNode> randomList = new ArrayList<KadNode>();
        for(int i = 0; i < 100; i++)
            randomList.add(createNode(i + ""));
        Collections.shuffle(randomList);
        
        KnowContacts contacts = new KnowContacts();
        for(int i = 0; i < MAX_SIZE; i++)
        {
            KadNode node = randomList.get(i);
            contacts.addContact(node);
            contactList.add(node);
        }
        return contacts;
    }
}
