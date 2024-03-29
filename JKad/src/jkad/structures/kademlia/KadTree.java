/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.structures.kademlia;

import java.util.List;

public interface KadTree
{
    public KadTreeNode getRoot();

    public int countBuckets();

    public int countNodes();

    public List<Bucket> getBuckets();
}
