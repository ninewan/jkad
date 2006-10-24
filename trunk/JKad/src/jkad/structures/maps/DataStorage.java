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
    
    private HashMap<BigInteger, Object> map;
    
    public DataStorage()
    {
        this.map = new HashMap<BigInteger, Object>();
    }
    
    public Object get(BigInteger key)
    {
        return map.get(key);
    }

    public Object put(BigInteger key, Object value)
    {
        return map.put(key, value);
    }

    public Object remove(BigInteger key)
    {
        return map.remove(key);
    }
    
    public int getSize()
    {
        return map.size();
    }
    
    public List<Entry<BigInteger, Object>> getClosestValues(BigInteger key, BigInteger proximity)
    {
    	List<Entry<BigInteger, Object>> result = new ArrayList<Entry<BigInteger,Object>>();
        for(Entry<BigInteger, Object> entry : map.entrySet())
        {
            BigInteger delta = entry.getKey().subtract(key).abs();
            if(delta.compareTo(proximity) <= 0)
            {
                result.add(entry);
            }
        }
        return result;
    }
}
