package jKad.structures.tree;

public interface SimpleTreeNode<E> {
	public boolean isRoot();
	
	public E getParent();
	public void setParent(E node);
}
