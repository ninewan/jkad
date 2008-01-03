/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.controller.threads;

import java.math.BigInteger;

import jkad.controller.handlers.Controller;
import junit.framework.TestCase;

public class ControllerTest extends TestCase
{
    public void testGetMyID()
    {
        BigInteger id = Controller.getMyID();
        assertNotNull(id);
    }

    public void testGenerateRPCID()
    {
        fail("Not yet implemented");
    }

}
