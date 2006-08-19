package jKad.controller.threads.handlers;

import jKad.structures.tree.SimpleTreeNode;

import java.util.Iterator;
import java.util.List;

public abstract class HandlerThread extends Thread implements SimpleTreeNode<HandlerThread>{
	public static final int STATUS_WAITING = 1;
	public static final int STATUS_PROCESSING = 2;
	public static final int STATUS_KILLED = 4;
	public static final int STATUS_ZOMBIE = 8;
	
	private HandlerThread parent;
	private List<HandlerThread> children;
	
	private static Class<? extends List> childrenListClass = java.util.ArrayList.class;
	
	public static void setChildrenListClass(Class<? extends List> listClass){
		HandlerThread.childrenListClass = listClass;
	}
	
	public static Class<? extends List> getChildrenListClass(){
		return HandlerThread.childrenListClass;
	}

	public boolean addChild(HandlerThread node) {
		if(node != null){
			node.setParent(this);
			getBuiltChildrenList().add(node);
			return true;
		} else {
			return false;
		}
	}

	public boolean addChild(int position, HandlerThread node) {
		if(node != null){
			node.setParent(this);
			getBuiltChildrenList().add(position, node);
			return true;
		} else {
			return false;
		}
	}

	public boolean addChildren(List<HandlerThread> nodes) {
		if(nodes != null && nodes.size() > 0){
			adjustChildren(this, nodes);
			getBuiltChildrenList().addAll(nodes);
			return true;
		} else {
			return false;
		}
	}
	
	public HandlerThread getChild(int position) {
		return this.children.get(position);
	}

	public List<HandlerThread> getChildren() {
		return this.children;
	}

	public HandlerThread getParent() {
		return parent;
	}

	public boolean isLeaf() {
		return (children == null || children.size() == 0); 
	}

	public boolean isRoot() {
		return (getParent() == null);
	}

	public boolean removeChild(HandlerThread node) {
		return children.remove(node);
	}

	public HandlerThread removeChild(int position) {
		return children.remove(position);
	}

	public List<HandlerThread> removeChildren() {
		adjustChildren(null, children);
		List<HandlerThread> result = children;
		children = null;
		return result;
	}

	public void setChildren(List<HandlerThread> nodes) {
		if(nodes != null) {
			getBuiltChildrenList().addAll(nodes);
			adjustChildren(this, children);
		} else {
			children = null;
		}
	}

	public void setParent(HandlerThread node) {
		this.parent = node;		
	}

	public abstract void run();
	
	public abstract int getStatus();
	
	@SuppressWarnings("unchecked")
	protected List<HandlerThread> getBuiltChildrenList(){
		if(this.children == null){
			try {
				this.children = (List<HandlerThread>)HandlerThread.childrenListClass.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return this.children;
	}
	
	protected void adjustChildren(HandlerThread node, List<HandlerThread> children){
		if(children != null) {
			for(int i = 0; i < children.size(); i++){
				children.get(i).setParent(node);
			}
		}
	}
}
