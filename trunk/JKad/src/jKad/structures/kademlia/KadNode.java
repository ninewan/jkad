package jKad.structures.kademlia;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;

import jKad.protocol.KadProtocol;
import jKad.protocol.KadProtocolException;

public class KadNode extends KadTreeNode{
	private BigInteger nodeID;
	private InetAddress ipAddress;
	private int port;
		
	public KadNode(){}
	
	public KadNode(BigInteger nodeID) throws KadProtocolException{
		this.setNodeID(nodeID);
	}
	
	public KadNode(BigInteger nodeID, String ip, int port) throws KadProtocolException, UnknownHostException {
		this(nodeID, InetAddress.getByName(ip), port);
	}
	
	public KadNode(BigInteger nodeID, InetAddress ip, int port) throws KadProtocolException{
		this.setNodeID(nodeID);
		this.setIpAddress(ip);
		this.setPort(port);
	}
	
	public void setNodeID(BigInteger nodeID) throws KadProtocolException {
		if(nodeID != null && nodeID.bitCount() > KadProtocol.NODE_ID_LENGTH * 8){
			throw new KadProtocolException("NodeID must have " + (KadProtocol.NODE_ID_LENGTH * 8) + " bits, found" + nodeID.bitCount() + " bits");
		}
		this.nodeID = nodeID;
	}
	
//	public String toString(){
//		String result = "Node " + this + ":";
//		if(nodeID != null){
//			for(int i = 0; i < nodeID.length; i++){
//				result = result + " " + nodeID[i];
//			}
//		} else {
//			result = result + " empty nodeID";
//		}
//		return result;
//	}
	
	public InetAddress getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(InetAddress ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	public int getPort(){
		return port;
	}
	
	public void setPort(int port) throws KadProtocolException{
		int max = 0xFFFF;
		if (port <= 0 || port >= max){
			throw new KadProtocolException("Cannot set port to " + port + ". Port must be on 0 < range < " + max);
		}
		this.port = port;
	}

    public BigInteger getNodeID() {
        return nodeID;
    }
}
