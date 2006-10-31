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

	public KadNode(BigInteger nodeID)
    {
        this.setNodeID(nodeID);
        this.createTime = System.currentTimeMillis();
        this.lastAccess = createTime;
    }

    public KadNode(BigInteger nodeID, String ip, int port) throws UnknownHostException
    {
        this(nodeID, InetAddress.getByName(ip), port);
    }

    public KadNode(BigInteger nodeID, InetAddress ip, int port)
    {
        this.setNodeID(nodeID);
        this.setIpAddress(ip);
        this.setPort(port);
        this.createTime = System.currentTimeMillis();
        this.lastAccess = createTime;
    }

    public void setNodeID(BigInteger nodeID) 
    {
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

    public void setPort(int port) 
    {
        this.port = port;
    }

    public String getIpAndPort()
    {
    	return ipAddress.getHostAddress() + ":" + port;
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

    public boolean equals(Object obj)
    {
        boolean result = false;
        if (obj instanceof KadNode)
        {
            KadNode node = (KadNode) obj;
            if(node.getNodeID() != null && this.getNodeID() != null)
                result = node.getNodeID().equals(this.getNodeID());
            else
                result = node.getNodeID() == null && this.getNodeID() != null;
        }
        return result;
    }

    public int hashCode()
    {
        return this.getNodeID().hashCode();
    }
    
    public String toString()
    {
        return "id: " + this.getNodeID().toString(16) + ", address: " + this.getIpAddress().toString() + ":" + this.getPort();
    }
}
