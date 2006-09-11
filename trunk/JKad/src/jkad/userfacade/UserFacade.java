/* SVN Info:
 * $HeadURL: https://jkad.googlecode.com/svn/trunk/JKad/src/jkad/controller/DetailedInfoFacade.java $
 * $LastChangedRevision: 24 $
 * $LastChangedBy: polaco $                             
 * $LastChangedDate: 2006-09-04 02:32:35 -0300 (seg, 04 set 2006) $  
 */
package jkad.userfacade;

import java.util.List;

public interface UserFacade
{
    public void store(String key, String data);
    
    public void store(byte[] key, byte[] data);
    
    public String findValue(String key);
    
    public byte[] findValue(byte[] data);
    
    public List<NetLocation> listNodesWithValue(String key);
    
}