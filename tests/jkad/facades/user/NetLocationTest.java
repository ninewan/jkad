/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.facades.user;

import java.net.InetAddress;
import java.net.UnknownHostException;

import junit.framework.Assert;
import junit.framework.TestCase;

public class NetLocationTest extends TestCase
{
    public void testNetLocationStringInteger()
    {
        try
        {
            new NetLocation("192.168.1.2", 5000);
        } catch (UnknownHostException e)
        {
           Assert.fail();
        }
        try
        {
            new NetLocation("asdasd$$", 5000);
            Assert.fail();
        } catch (UnknownHostException e)
        {
        }
    }

    public void testGetStringIP()
    {
        try
        {
            String ip = "192.168.1.2";
            NetLocation netLocation = new NetLocation(ip, 5000);
            assertEquals(ip, netLocation.getStringIP());
            
            InetAddress inetIP = InetAddress.getByName(ip);
            netLocation = new NetLocation(inetIP, 5000);
            assertEquals(ip, netLocation.getStringIP());
            
            ip = "127.0.0.1";
            String hostname = "localhost";
            netLocation = new NetLocation(hostname, 5000);
            assertEquals(hostname + "/" + ip, netLocation.getStringIP());
            
            inetIP = InetAddress.getByName(hostname);
            netLocation = new NetLocation(inetIP, 5000);
            assertEquals(hostname + "/" + ip, netLocation.getStringIP());
        } catch (UnknownHostException e)
        {
            fail();
        }
    }

    public void testGetPort()
    {
        try
        {
            Integer port = 5000;
            NetLocation netLocation = new NetLocation("192.168.1.2", port);
            assertEquals(port, netLocation.getPort());
            
            port = null;
            netLocation = new NetLocation("192.168.1.2", port);
            assertEquals(port, netLocation.getPort());
        } catch (UnknownHostException e)
        {
            fail();
        }
    }
}