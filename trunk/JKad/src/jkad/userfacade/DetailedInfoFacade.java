/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.userfacade;

import java.math.BigInteger;

public interface DetailedInfoFacade
{
    public long countReceivedPackets();

    public long countSentPackets();

    public long countReceivedRPCs();

    public long countReceivedRPCs(byte type);

    public long countSentRPCs();

    public long countSentRPCs(byte type);
    
    public BigInteger getSystemID();
}
