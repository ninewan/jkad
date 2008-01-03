/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.structures.tree;

public interface SimpleTreeNode<E>
{
    public boolean isRoot();

    public E getParent();

    public void setParent(E node);
}
