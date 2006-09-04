/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jKad.structures.tree;

import java.util.List;

public interface MultiTreeNode<P, N> extends SimpleTreeNode<P>
{
    public List<? extends N> getChildren();

    public void setChildren(List<N> nodes);

    public N getChild(int position);

    public boolean addChild(N node);

    public boolean addChild(int position, N node);

    public boolean addChildren(List<N> nodes);

    public N removeChild(int position);

    public boolean removeChild(N node);

    public List<? extends N> removeChildren();
}
