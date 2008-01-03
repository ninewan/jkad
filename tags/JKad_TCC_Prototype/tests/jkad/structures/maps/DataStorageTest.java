package jkad.structures.maps;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

import junit.framework.TestCase;

public class DataStorageTest extends TestCase 
{
	private static final int SIZE = 500;
	
	public void testGet() 
	{
		DataStorage storage = getFilledStorage();
		assertEquals("value: 50", storage.get(new BigInteger("50")).toString());
	}

	public void testPut() 
	{
		DataStorage storage = new DataStorage();
		assertEquals(0, storage.getSize());
		assertNull(storage.put(new BigInteger("0"), "0"));
		assertEquals(1, storage.getSize());
		assertEquals("0", storage.get(new BigInteger("0")).toString());
		assertNull(storage.get(new BigInteger("666")));
		assertEquals("0", storage.put(new BigInteger("0"), "a").toString());
		assertEquals(1, storage.getSize());
		assertEquals("a", storage.get(new BigInteger("0")).toString());
		assertNull(storage.put(new BigInteger("2"), "2"));
		assertEquals(2, storage.getSize());
		assertEquals("2", storage.get(new BigInteger("2")).toString());
	}

	public void testRemove() 
	{
		DataStorage storage = getFilledStorage();
		assertEquals(SIZE, storage.getSize());
		assertEquals("value: 0", storage.remove(new BigInteger("0")));
		assertEquals(SIZE - 1, storage.getSize());
		assertNull(storage.remove(new BigInteger("666")));
		assertEquals(SIZE - 1, storage.getSize());
	}

	public void testGetClosestValues() 
	{
		EntryComparator comp = new EntryComparator();
		DataStorage storage = getFilledStorage();
		List<Entry<BigInteger, Object>> result;
		String[] keys;
		
		result = storage.getClosestValues(new BigInteger("0"), new BigInteger("30"));
		keys = new String[]{"0", "10", "20", "30"};
		assertClosestValuesResult(keys, result);
		
		result = storage.getClosestValues(new BigInteger("25"), new BigInteger("20"));
		keys = new String[]{"10", "20", "30", "40"};
		assertClosestValuesResult(keys, result);

		result = storage.getClosestValues(new BigInteger("51"), new BigInteger("19"));
		keys = new String[]{"40", "50", "60", "70"};
		assertClosestValuesResult(keys, result);
	}
	
    private DataStorage getFilledStorage()
    {
        DataStorage storage = new DataStorage();
        for(int i = 0; i < SIZE; i++)
        {
            String value = "" + (i * 10);
            storage.put(new BigInteger(value), "value: " + value);
        }
        return storage;
    }

    private void assertClosestValuesResult(String[] keys, List<Entry<BigInteger, Object>> result)
    {
    	Collections.sort(result, new EntryComparator());
    	assertEquals(keys.length, result.size());
		for(int i = 0; i < keys.length; i++)
		{
			assertEquals(keys[i], result.get(i).getKey().toString());
			assertEquals("value: " + keys[i], result.get(i).getValue());
		}
    }
}

class EntryComparator implements Comparator<Entry<BigInteger, Object>>
{
	public int compare(Entry<BigInteger, Object> o1, Entry<BigInteger, Object> o2) 
	{
		return o1.getKey().compareTo(o2.getKey());
	}
}