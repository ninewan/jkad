/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jKad.controller;

public interface Statistical
{
    public long countReceivedPackets();

    public long countSentPackets();

    public long countReceivedRPCs();

    public long countReceivedRPCs(byte type);

    public long countSentRPCs();

    public long countSentRPCs(byte type);
}
