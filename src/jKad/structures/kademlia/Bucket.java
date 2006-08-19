/*
 * Created on 28/03/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package jKad.structures.kademlia;

import jKad.protocol.KadProtocol;
import jKad.structures.tree.MultiTreeNode;
import jKad.tools.DataTools;
import jKad.tools.ToolBox;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Bucket extends KadTreeNode implements MultiTreeNode<Bucket, KadTreeNode>{
	private BigInteger prefix;
    private int size;
	private int capacity;
	private boolean structural;
    private KadNode ownNode;
    
    private List<KadTreeNode> children;
    
    private static Class<? extends List> childrenListClass = java.util.ArrayList.class;
    
    public static void setChildrenListClass(Class<? extends List> listClass){
        childrenListClass = listClass;
    }
    
    public static Class<? extends List> getChildrenListClass(){
        return childrenListClass;
    }

	public Bucket(KadNode ownNode){
		this(ownNode, null, KadProtocol.BUCKET_SIZE);
	}
	
	public Bucket(KadNode ownNode, BigInteger prefix){
		this(ownNode, prefix, KadProtocol.BUCKET_SIZE);
	}
	
	public Bucket(KadNode ownNode, BigInteger prefix, int capacity)
	{
	    this.ownNode = ownNode;
        this.prefix = prefix;
        this.size = 0;
		this.capacity = capacity;
	}

	public int getCapacity() {
		return capacity;
	}

	public int getSize() {
		return size;
	}

	public boolean isFull() {
		return size == capacity;
	}
	
	public boolean isEmpty(){
		return size == 0;
	}
    
    public boolean isLeaf() {
        return (children == null || children.size() == 0); 
    }
    
	public BigInteger getPrefix() {
        return prefix;
    }

    public void setPrefix(BigInteger prefix) {
        this.prefix = prefix;
    }
    
    public boolean isStructure() {
        return structural;
    }

    public void setStructure(boolean structure) {
        this.structural = structure;
    }

    public boolean addChild(int position, KadTreeNode node) {
        if(!isFull() && node != null){
          node.setParent(this);
          getChildrenList().add(position, node);
          size++;
          return true;
        } else {
          return false;
        }
    }

	public boolean addChild(KadTreeNode node) {
		if(!isFull() && node != null){
			node.setParent(this);
			getChildrenList().add(node);
			size++;
			return true;
		} else {
			return false;
		}
	}

	public boolean addChildren(List<KadTreeNode> nodes) {
		if(nodes != null && nodes.size() <= capacity - size){
            adjustChildren(this, nodes);
            getChildrenList().addAll(nodes);
            size += nodes.size();
            return true;
		} else {
		    return false;
        }
	}

	public KadTreeNode removeChild(int position) {
        KadTreeNode removed = getChildrenList().remove(position);
		if(removed != null){
            removed.setParent(null);
            size--;
        }
		return removed;
	}

	public boolean removeChild(KadTreeNode node) {
		boolean removed = getChildrenList().remove(node);
		if(removed) {
            node.setParent(null);
            size--;
        }
		return removed;
	}

	public List<KadTreeNode> removeChildren() {
		List<KadTreeNode> removedList = children;
        adjustChildren(null, removedList);
        children = null;
        size = 0;
		return removedList;
	}
	
	public KadTreeNode getChild(int position) {
		return getChildrenList().get(position);
	}

	public List<KadTreeNode> getChildren() {
		return getChildrenList();
	}
    
    public void setChildren(List<KadTreeNode> nodes) {
        if(nodes != null) {
            if(nodes.size() <= capacity) {
                children = nodes;
                size = nodes.size();
                adjustChildren();
            }
        } else {
            children = nodes;
        }
    }
    
    public void split() {
        if(!isStructure()) {
            Bucket left = new Bucket(getOwnNode());
            Bucket right = new Bucket(getOwnNode());
            this.setStructure(true); 
            int prefixPos;
            
            //caso seja bucket root
            if(this.getPrefix() == null) {
                prefixPos = 1;
            	left.setPrefix(BigInteger.ONE);
                right.setPrefix(BigInteger.ZERO);
            } else {
            	prefixPos = this.getPrefix().bitLength();
            	left.setPrefix((this.getPrefix().shiftLeft(1)).setBit(0));
            	right.setPrefix((this.getPrefix().shiftLeft(1)).clearBit(0));
            }
            
            for(int i = this.getChildren().size(); i > 0 ; i--) {
            	KadNode removed = (KadNode)this.removeChild(0);
            	BigInteger nodeID = removed.getNodeID();
//            	if(nodeID.testBit((KadProtocol.NODE_ID_LENGTH * 8) - prefixPos)) {
            	if(nodeID.testBit(4 - prefixPos)) {
            		left.addChild(removed);
            	} else {
            		right.addChild(removed);
            	}
            		
            }
            this.addChild(left);
            this.addChild(right);
            
//            // Busca prefixos Comuns
//            BigInteger maxNumber = BigInteger.ZERO;
//            //procura pelo maior numero
//            for(int i = 0; i < this.getChildren().size(); i++)
//            {
//                KadNode node = (KadNode)this.getChildren().get(i);
//                maxNumber = maxNumber.max(node.getNodeID());
//            }
//            //realiza XOR entre o maior numero e todos os filhos
//            BigInteger maxXor = BigInteger.ZERO;
//            for(int i = 0; i < this.getChildren().size(); i++)
//            {
//                KadNode node = (KadNode)this.getChildren().get(i);
//                BigInteger xor = node.getNodeID().xor(maxNumber);
//                maxXor = maxXor.max(xor);
//            }
//            int commonPrefixSize = KadProtocol.NODE_ID_LENGTH - maxXor.bitLength();
//            byte[] result = new byte[commonPrefixSize];
//            System.arraycopy(maxNumber, 0, result, 0, commonPrefixSize);
        }
    }

    @SuppressWarnings("unchecked")
	protected List<KadTreeNode> getChildrenList(){
		if(this.children == null){
			try {
				this.children = (List<KadTreeNode>)Bucket.childrenListClass.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return this.children;
	}
	
    protected void adjustChildren(){
        this.adjustChildren(this, children);
    }
    
	protected void adjustChildren(Bucket bucket, List<KadTreeNode> children){
		if(children != null) {
			for(int i = 0; i < children.size(); i++){
				children.get(i).setParent(bucket);
			}
		}
	}

    protected KadNode getOwnNode() {
        return ownNode;
    }

    protected void setOwnNode(KadNode ownNode) {
        this.ownNode = ownNode;
    }
    
    protected void divideContents(Bucket left, Bucket right, int prefixPosition) {
        this.divideContents(left, right, prefixPosition, this.getChildren());
    }
    
    protected void divideContents(Bucket left, Bucket right, int prefixPosition, List<KadTreeNode> list) {
        for(Iterator<KadTreeNode> it = list.iterator(); it.hasNext();) {
            KadNode node = (KadNode)it.next();
            BigInteger nodeId = node.getNodeID();
            if(nodeId.testBit(KadProtocol.NODE_ID_LENGTH - prefixPosition)) {
                left.addChild(node);
            } else {
                right.addChild(node);
            }
        }
    }
}
