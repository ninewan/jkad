/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jKad.structures.kademlia;

import jKad.structures.tree.SimpleTreeNode;

public class KadTreeNode implements SimpleTreeNode<Bucket>
{
    private Bucket parent;

    public Bucket getParent()
    {
        return parent;
    }

    public boolean isRoot()
    {
        return (getParent() == null);
    }

    public void setParent(Bucket node)
    {
        this.parent = node;
    }
}