/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.structures.kademlia;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;

import jkad.protocol.KadProtocol;
import jkad.protocol.KadProtocolException;

public class KadNode extends KadTreeNode
{
    private BigInteger nodeID;
    private InetAddress ipAddress;
    private int port;
    private long createTime;
    private long lastAccess;
    
	public KadNode()
    {
    	this.createTime = System.currentTimeMillis();
    }

	public KadNode(BigInteger nodeID) throws KadProtocolException
    {
        this.setNodeID(nodeID);
        this.createTime = System.currentTimeMillis();
        this.lastAccess = createTime;
    }

    public KadNode(BigInteger nodeID, String ip, int port) throws KadProtocolException, UnknownHostException
    {
        this(nodeID, InetAddress.getByName(ip), port);
    }

    public KadNode(BigInteger nodeID, InetAddress ip, int port) throws KadProtocolException
    {
        this.setNodeID(nodeID);
        this.setIpAddress(ip);
        this.setPort(port);
        this.createTime = System.currentTimeMillis();
    }

    public void setNodeID(BigInteger nodeID) throws KadProtocolException
    {
        if (nodeID != null && nodeID.bitCount() > KadProtocol.NODE_ID_LENGTH * 8)
        {
            throw new KadProtocolException("NodeID must have " + (KadProtocol.NODE_ID_LENGTH * 8) + " bits, found" + nodeID.bitCount() + " bits");
        }
        this.nodeID = nodeID;
    }

    public InetAddress getIpAddress()
    {
        return ipAddress;
    }

    public void setIpAddress(InetAddress ipAddress)
    {
        this.ipAddress = ipAddress;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port) throws KadProtocolException
    {
        int max = 0xFFFF;
        if (port <= 0 || port >= max)
        {
            throw new KadProtocolException("Cannot set port to " + port + ". Port must be on 0 < range < " + max);
        }
        this.port = port;
    }

    public BigInteger getNodeID()
    {
        return nodeID;
    }
    
    public Long getCreateTime() 
    {
		return createTime;
	}
    
    public long getLastAccess() 
    {
		return lastAccess;
	}

	public void setLastAccess(long lastAccess) 
	{
		this.lastAccess = lastAccess;
	}

}
