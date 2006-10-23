/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.facades.user;

import java.util.List;

public interface UserFacade
{
    public void store(String key, String data);
    
    public void store(byte[] key, byte[] data);
    
    public String findValue(String key);
    
    public byte[] findValue(byte[] data);
    
    public List<NetLocation> listNodesWithValue(String key);
    
}