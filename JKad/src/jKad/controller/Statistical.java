package jKad.controller;

public interface Statistical
{
    public long countReceivedPackets();
    
    public long countSentPackets();
    
    public long countReceivedRPCs();
    
    public long countReceivedRPCs(int type);
    
    public long countSentRPCs();
    
    public long countSentRPCs(int type);
}
