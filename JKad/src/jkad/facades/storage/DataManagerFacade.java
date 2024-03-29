/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */

package jkad.facades.storage;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import jkad.controller.ThreadGroupLocal;
import jkad.structures.maps.DataStorage;

public abstract class DataManagerFacade<Value> implements DataStorageFacade<Value>
{
    private static ThreadGroupLocal<DataManagerFacade> dataStorage;
    
    public static DataManagerFacade getDataManager()
    {
        if(dataStorage == null)
        {
            dataStorage = new ThreadGroupLocal<DataManagerFacade>()
            {
                public DataManagerFacade initialValue()
                {
                    return new DataStorage();
                }
            };
        }
        return dataStorage.get();
    }
	
    public abstract List<Entry<BigInteger, Value>> getClosestValues(BigInteger key, BigInteger proximity); 
    
    public abstract Iterator<Entry<BigInteger, Value>> getIterator();
}
