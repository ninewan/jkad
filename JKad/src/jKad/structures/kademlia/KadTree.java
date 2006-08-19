/*
 * Created on 03/04/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package jKad.structures.kademlia;

import java.util.List;

public interface KadTree {
	public KadTreeNode getRoot();
	public int countBuckets();
	public int countNodes();
	public List<Bucket> getBuckets();
}
