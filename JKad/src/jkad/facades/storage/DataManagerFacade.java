/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */

package jkad.facades.storage;

import java.math.BigInteger;
import java.util.List;
import java.util.Map.Entry;

public interface DataManagerFacade<Value> extends DataStorageFacade<Value>
{
    public List<Entry<byte[], Value>> getClosestValues(byte[] key, BigInteger proximity); 
    
    public List<Entry<byte[], Value>> getClosestValues(BigInteger key, BigInteger proximity); 
    
}
