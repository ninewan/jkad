/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */

package jkad.facades.storage;

import java.math.BigInteger;

public interface DataStorageFacade<Value>
{
    public Value get(byte[] key);
    
    public Value get(BigInteger key);
    
    public Value put(byte[] key, Value value);
    
    public Value put(BigInteger key, Value value);
    
    public Value remove(byte[] key);
    
    public Value remove(BigInteger key);
    
    public int getSize();
}
