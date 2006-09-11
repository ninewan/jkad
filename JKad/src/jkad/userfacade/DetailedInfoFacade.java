/* SVN Info:
 * $HeadURL: https://jkad.googlecode.com/svn/trunk/JKad/src/jkad/controller/DetailedInfoFacade.java $
 * $LastChangedRevision: 24 $
 * $LastChangedBy: polaco $                             
 * $LastChangedDate: 2006-09-04 02:32:35 -0300 (seg, 04 set 2006) $  
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
