/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.controller.threads;

import java.math.BigInteger;

import junit.framework.TestCase;

import org.junit.Test;

public class ControllerTest extends TestCase
{
    public void testGetMyID()
    {
        BigInteger id = Controller.getMyID();
        assertNotNull(id);
    }

    @Test
    public void testGenerateRPCID()
    {
        fail("Not yet implemented");
    }

}
