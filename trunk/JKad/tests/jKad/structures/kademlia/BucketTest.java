package jKad.structures.kademlia;

import jKad.protocol.KadProtocol;
import jKad.protocol.KadProtocolException;
import jKad.tools.DataTools;
import jKad.tools.ToolBox;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

public class BucketTest extends TestCase {

	private KadNode ownNode;
	
	public void setUp() {
		try {
			ownNode = new KadNode(BigInteger.ONE);
		} catch (KadProtocolException e) {
			e.printStackTrace();
		}
	}
	
	/*
     * Test method for 'jKad.structures.kademlia.Bucket.getChildrenListClass()'
     */
    public void testGetChildrenListClass() {
        assertEquals(Bucket.getChildrenListClass(), java.util.ArrayList.class);
        Class<? extends List> listClass = new Bucket(ownNode).getChildren().getClass();
        assertEquals(listClass, java.util.ArrayList.class);
        
        Bucket.setChildrenListClass(java.util.LinkedList.class);
        assertEquals(Bucket.getChildrenListClass(), java.util.LinkedList.class);
        listClass = new Bucket(ownNode).getChildren().getClass();
        assertEquals(listClass, java.util.LinkedList.class);
    }

    /*
     * Test method for 'jKad.structures.kademlia.Bucket.getCapacity()'
     */
    public void testGetCapacity() {
        Bucket bucket = new Bucket(ownNode);
        assertEquals(bucket.getCapacity(), KadProtocol.BUCKET_SIZE);
    }

    /*
     * Test method for 'jKad.structures.kademlia.Bucket.getSize()'
     */
    public void testGetSize() {
        Bucket bucket = new Bucket(ownNode);
        assertEquals(bucket.getSize(), 0);
        bucket.addChild(new KadNode());
        assertEquals(bucket.getSize(), 1);
        bucket.removeChildren();
        assertEquals(bucket.getSize(), 0);
        List<KadTreeNode> nodes = new ArrayList<KadTreeNode>();
        for(int i = 0; i < KadProtocol.BUCKET_SIZE; i++) nodes.add(new KadNode());
        assertEquals(bucket.addChildren(nodes), true);
        assertEquals(bucket.getSize(), KadProtocol.BUCKET_SIZE);
        assertEquals(bucket.addChild(new KadNode()), false);
        assertEquals(bucket.getSize(), KadProtocol.BUCKET_SIZE);
    }

    /*
     * Test method for 'jKad.structures.kademlia.Bucket.isFull()'
     */
    public void testIsFull() {
        Bucket bucket = new Bucket(ownNode);
        assertEquals(bucket.isFull(), false);
        List<KadTreeNode> nodes = new ArrayList<KadTreeNode>();
        for(int i = 0; i < KadProtocol.BUCKET_SIZE / 2; i++) nodes.add(new KadNode());
        assertEquals(bucket.addChildren(nodes), true);
        assertEquals(bucket.isFull(), false);
        assertEquals(bucket.addChildren(nodes), true);
        assertEquals(bucket.isFull(), true);
    }

    /*
     * Test method for 'jKad.structures.kademlia.Bucket.isEmpty()'
     */
    public void testIsEmpty() {
        Bucket bucket = new Bucket(ownNode);
        assertEquals(bucket.isEmpty(), true);
        assertEquals(bucket.addChild(new KadNode()), true);
        assertEquals(bucket.isEmpty(), false);
    }

    /*
     * Test method for 'jKad.structures.kademlia.Bucket.isLeaf()'
     */
    public void testIsLeaf() {
        Bucket bucket = new Bucket(ownNode);
        assertEquals(bucket.isLeaf(), true);
        assertEquals(bucket.addChild(new KadNode()), true);
        assertEquals(bucket.isLeaf(), false);
    }

    /*
     * Test method for 'jKad.structures.kademlia.Bucket.addChild(int, KadTreeNode)'
     */
    public void testAddChildIntKadTreeNode() {
        Bucket bucket = new Bucket(ownNode);
        KadNode node1 = new KadNode();
        KadNode node2 = new KadNode();
        KadNode node3 = new KadNode();
        assertEquals(bucket.addChild(0, node1), true);
        assertEquals(bucket.addChild(1, node2), true);
        assertEquals(bucket.getSize(), 2);
        assertEquals(bucket.addChild(0, node3), true);
        assertEquals(bucket.getSize(), 3);
        List<KadTreeNode> nodes = bucket.getChildren();
        assertEquals(nodes.get(0), node3);
        assertEquals(nodes.get(1), node1);
        assertEquals(nodes.get(2), node2);
    }

    /*
     * Test method for 'jKad.structures.kademlia.Bucket.addChild(KadTreeNode)'
     */
    public void testAddChildKadTreeNode() {
        Bucket bucket = new Bucket(ownNode);
        KadNode node1 = new KadNode();
        KadNode node2 = new KadNode();
        assertEquals(bucket.addChild(node1), true);
        assertEquals(bucket.addChild(node2), true);
        assertEquals(bucket.getSize(), 2);
        List<KadTreeNode> nodes = bucket.getChildren();
        assertEquals(nodes.get(0), node1);
        assertEquals(nodes.get(1), node2);
    }

    /*
     * Test method for 'jKad.structures.kademlia.Bucket.addChildren(List<KadTreeNode>)'
     */
    public void testAddChildren() {
        Bucket bucket = new Bucket(ownNode);
        List<KadTreeNode> list = new ArrayList<KadTreeNode>();
        list.add(new KadNode());
        assertEquals(bucket.addChildren(list), true);
        assertEquals(bucket.getSize(), 1);
        list.add(new KadNode());
        assertEquals(bucket.addChildren(list), true);
        assertEquals(bucket.getSize(), 3);
        list.add(new KadNode());
        assertEquals(bucket.addChildren(list), true);
        assertEquals(bucket.getSize(), 6);
    }

    /*
     * Test method for 'jKad.structures.kademlia.Bucket.removeChild(int)'
     */
    public void testRemoveChildInt() {
        Bucket bucket = new Bucket(ownNode);
        KadNode node1 = new KadNode();
        KadNode node2 = new KadNode();
        KadNode node3 = new KadNode();
        assertEquals(bucket.addChild(node1), true);
        assertEquals(bucket.addChild(node2), true);
        assertEquals(bucket.addChild(node3), true);
        assertEquals(bucket.getSize(), 3);
        assertEquals(bucket.removeChild(1), node2);
        assertEquals(bucket.getSize(), 2);
        assertEquals(bucket.removeChild(0), node1);
        assertEquals(bucket.getSize(), 1);
        assertEquals(bucket.removeChild(0), node3);
        assertEquals(bucket.getSize(), 0);
    }

    /*
     * Test method for 'jKad.structures.kademlia.Bucket.removeChild(KadTreeNode)'
     */
    public void testRemoveChildKadTreeNode() {
        Bucket bucket = new Bucket(ownNode);
        KadNode node1 = new KadNode();
        KadNode node2 = new KadNode();
        KadNode node3 = new KadNode();
        assertEquals(bucket.addChild(node1), true);
        assertEquals(bucket.addChild(node2), true);
        assertEquals(bucket.addChild(node3), true);
        assertEquals(bucket.getSize(), 3);
        assertEquals(bucket.removeChild(node2), true);
        assertEquals(bucket.getSize(), 2);
        assertEquals(bucket.removeChild(node1), true);
        assertEquals(bucket.getSize(), 1);
        assertEquals(bucket.removeChild(node3), true);
        assertEquals(bucket.getSize(), 0);
        assertEquals(bucket.removeChild(node3), false);
        assertEquals(bucket.getSize(), 0);
    }

    /*
     * Test method for 'jKad.structures.kademlia.Bucket.removeChildren()'
     */
    public void testRemoveChildren() {
        Bucket bucket = new Bucket(ownNode);
        KadNode node1 = new KadNode();
        KadNode node2 = new KadNode();
        KadNode node3 = new KadNode();
        assertEquals(bucket.addChild(node1), true);
        assertEquals(bucket.addChild(node2), true);
        assertEquals(bucket.addChild(node3), true);
        assertEquals(bucket.getSize(), 3);
        List<KadTreeNode> removed = bucket.removeChildren();
        assertEquals(bucket.getSize(), 0);
        assertEquals(bucket.getChildren().size(), 0);
        assertEquals(bucket.isEmpty(), true);
        assertEquals(removed.size(), 3);
        Iterator<KadTreeNode> it = removed.iterator();
        assertEquals(it.next(), node1);
        assertEquals(it.next(), node2);
        assertEquals(it.next(), node3);
    }

    /*
     * Test method for 'jKad.structures.kademlia.Bucket.getChild(int)'
     */
    public void testGetChild() {

    }

    /*
     * Test method for 'jKad.structures.kademlia.Bucket.getChildren()'
     */
    public void testGetChildren() {

    }

    /*
     * Test method for 'jKad.structures.kademlia.Bucket.setChildren(List<KadTreeNode>)'
     */
    public void testSetChildren() {

    }

    /*
     * Test method for 'jKad.structures.kademlia.Bucket.getChildrenList()'
     */
    public void testGetChildrenList() {

    }

    /*
     * Test method for 'jKad.structures.kademlia.Bucket.adjustChildren()'
     */
    public void testAdjustChildren() {

    }

    /*
     * Test method for 'jKad.structures.kademlia.Bucket.adjustChildren(Bucket, List<KadTreeNode>)'
     */
    public void testAdjustChildrenBucketListOfKadTreeNode() {

    }

    /*
     * Test method for 'jKad.structures.kademlia.KadTreeNode.getParent()'
     */
    public void testGetParent() {

    }

    /*
     * Test method for 'jKad.structures.kademlia.KadTreeNode.isRoot()'
     */
    public void testIsRoot() {

    }

    /*
     * Test method for 'jKad.structures.kademlia.KadTreeNode.setParent(Bucket)'
     */
    public void testSetParent() {

    }

    /*
     * Test method for 'java.lang.Object.Object()'
     */
    public void testObject() {

    }

    /*
     * Test method for 'java.lang.Object.getClass()'
     */
    public void testGetClass() {

    }

    /*
     * Test method for 'java.lang.Object.hashCode()'
     */
    public void testHashCode() {

    }

    /*
     * Test method for 'java.lang.Object.equals(Object)'
     */
    public void testEquals() {

    }

    /*
     * Test method for 'java.lang.Object.clone()'
     */
    public void testClone() {

    }

    /*
     * Test method for 'java.lang.Object.toString()'
     */
    public void testToString() {

    }

    /*
     * Test method for 'java.lang.Object.notify()'
     */
    public void testNotify() {

    }

    /*
     * Test method for 'java.lang.Object.notifyAll()'
     */
    public void testNotifyAll() {

    }

    /*
     * Test method for 'java.lang.Object.wait(long)'
     */
    public void testWaitLong() {

    }

    /*
     * Test method for 'java.lang.Object.wait(long, int)'
     */
    public void testWaitLongInt() {

    }

    /*
     * Test method for 'java.lang.Object.wait()'
     */
    public void testWait() {

    }

    /*
     * Test method for 'java.lang.Object.finalize()'
     */
    public void testFinalize() {

    }
    
    public void testSplit() throws KadProtocolException {
    	Bucket.setChildrenListClass(java.util.ArrayList.class);
    	DataTools tool = ToolBox.getDataTools();
    	Bucket bucket = new Bucket(ownNode);
    	for(int i = 0; i < 5; i++) {
    		BigInteger data = BigInteger.ZERO;
    		data = data.setBit(i);
    		KadNode node = new KadNode(data);
    		System.out.println(tool.toBinaryString(data, 20));
    		bucket.addChild(node);
    	}
    	bucket.split();
    	System.out.println("\nleft");
    	for(Iterator<KadTreeNode> it = ((Bucket)bucket.getChild(0)).getChildren().iterator(); it.hasNext();) {
    		KadNode node = (KadNode)it.next();
    		System.out.println(tool.toBinaryString(node.getNodeID(), 20));
    	}
    	System.out.println("\nright");
    	for(Iterator<KadTreeNode> it = ((Bucket)bucket.getChild(1)).getChildren().iterator(); it.hasNext();) {
    		KadNode node = (KadNode)it.next();
    		System.out.println(tool.toBinaryString(node.getNodeID(), 20));
    	}
    }
}
