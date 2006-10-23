package jkad.structures.maps;

import java.math.BigInteger;

import junit.framework.TestCase;

public class DataStorageTest extends TestCase
{
    private static final int SIZE = 500;

    public void testGetByteArray()
    {
        DataStorage storage = getFilledStorage();
        assertEquals("value: 0", storage.get(new BigInteger("0")));
    }

//    public void testGetBigInteger()
//    {
//        fail("Not yet implemented");
//    }
//
//    public void testPutByteArrayObject()
//    {
//        fail("Not yet implemented");
//    }
//
//    public void testPutBigIntegerObject()
//    {
//        fail("Not yet implemented");
//    }
//
//    public void testRemoveByteArray()
//    {
//        fail("Not yet implemented");
//    }
//
//    public void testRemoveBigInteger()
//    {
//        fail("Not yet implemented");
//    }
//
//    public void testGetSize()
//    {
//        fail("Not yet implemented");
//    }
//
//    public void testGetClosestValuesByteArrayBigInteger()
//    {
//        fail("Not yet implemented");
//    }
//
//    public void testGetClosestValuesBigIntegerBigInteger()
//    {
//        fail("Not yet implemented");
//    }

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
}
