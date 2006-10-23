/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.structures.maps;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import jkad.controller.ThreadGroupLocal;
import jkad.facades.storage.DataManagerFacade;
import jkad.protocol.KadProtocol;

public class DataStorage implements DataManagerFacade
{
    public static final int KEY_LENGTH = KadProtocol.KEY_LENGTH;
    
    private static ThreadGroupLocal<DataStorage> dataStorage;
    
    public static DataStorage getDataStorage()
    {
        if(dataStorage == null)
        {
            dataStorage = new ThreadGroupLocal<DataStorage>()
            {
                public DataStorage initialValue()
                {
                    return new DataStorage();
                }
            };
        }
        return dataStorage.get();
    }
    
    private HashMap<byte[], Object> map;
    
    public DataStorage()
    {
        this.map = new HashMap<byte[], Object>();
    }
    
    public Object get(byte[] key)
    {
        return map.get(this.normalizeArray(key));
    }

    public Object get(BigInteger key)
    {
        return this.get(key.toByteArray());
    }

    public Object put(byte[] key, Object value)
    {
        return map.put(this.normalizeArray(key), value);
    }

    public Object put(BigInteger key, Object value)
    {
        return this.put(key.toByteArray(), value);
    }

    public Object remove(byte[] key)
    {
        return map.remove(normalizeArray(key));
    }

    public Object remove(BigInteger key)
    {
        return this.remove(key.toByteArray());
    }
    
    public int getSize()
    {
        return map.size();
    }
    
    public List<Entry<byte[], Object>> getClosestValues(byte[] key, BigInteger proximity)
    {
        BigInteger bigIntKey = new BigInteger(key);
        List<Entry<byte[], Object>> result = new ArrayList<Entry<byte[],Object>>();
        for(Entry<byte[], Object> entry : map.entrySet())
        {
            BigInteger delta = new BigInteger(entry.getKey()).subtract(bigIntKey).abs();
            if(delta.compareTo(proximity) <= 0)
                result.add(entry);
        }
        return result;
    }
    
    public List<Entry<byte[], Object>> getClosestValues(BigInteger key, BigInteger proximity)
    {
        return getClosestValues(key.toByteArray(), proximity);
    }
    
    private byte[] normalizeArray(byte[] array)
    {
        if(array.length < KEY_LENGTH)
        {
            byte[] normalized = new byte[KEY_LENGTH];
            System.arraycopy(array, 0, normalized, KEY_LENGTH - array.length, array.length);
            return normalized;
        } else
            return array;
    }
}
