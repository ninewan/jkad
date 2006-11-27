/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.facades.user;

import java.math.BigInteger;
import java.net.InetAddress;
import java.util.List;

import jkad.structures.kademlia.KadNode;

public interface DetailedInfoFacade
{
    public long countReceivedPackets();

    public long countSentPackets();

    public long countReceivedRPCs();

    public long countReceivedRPCs(byte type);

    public long countSentRPCs();

    public long countSentRPCs(byte type);
    
    public BigInteger getSystemID();
    
    public List<KadNode> listKnowContacts();
    
    public InetAddress getIP();
    
    public int getPort();
}
