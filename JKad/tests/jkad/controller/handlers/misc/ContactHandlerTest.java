package jkad.controller.handlers.misc;

import java.math.BigInteger;

import jkad.structures.kademlia.KadNode;
import junit.framework.TestCase;

import org.apache.log4j.BasicConfigurator;

public class ContactHandlerTest extends TestCase
{
    public void setUp()
    {
        BasicConfigurator.configure();
    }
    
    public void testRun()
    {
        try
        {
            ContactHandler handler = new ContactHandler(500, 5000, 12000);
            handler.addContact(new KadNode(BigInteger.ZERO, "192.168.0.1", 5000));
            handler.run();
            Thread.sleep(10000);
            assertEquals(1, handler.getSize());
            handler.addContact(new KadNode(BigInteger.ONE, "192.168.0.1", 5001));
            assertEquals(2, handler.getSize());
            Thread.sleep(20000);
            assertEquals(0, handler.getSize());
        } catch (Exception e)
        {
            fail(e.getMessage());
        }
    }

}
