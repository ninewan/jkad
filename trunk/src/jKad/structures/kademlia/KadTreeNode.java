/*
 * Created on 28/03/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package jKad.structures.kademlia;

import jKad.structures.tree.SimpleTreeNode;

public class KadTreeNode implements SimpleTreeNode<Bucket>{
	private Bucket parent;

	public Bucket getParent() {
		return parent;
	}

	public boolean isRoot() {
		return (getParent() == null);
	}

	public void setParent(Bucket node) {
		this.parent = node;		
	}
}