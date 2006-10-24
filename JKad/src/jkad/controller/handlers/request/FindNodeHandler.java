/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.controller.handlers.request;

import jkad.controller.handlers.HandlerThread;
import jkad.tools.ToolBox;

public class FindNodeHandler extends HandlerThread
{
    public FindNodeHandler()
    {
    	super(ToolBox.getReflectionTools().generateThreadName(FindNodeHandler.class));
    }
	
	public void run()
    {
        // TODO Auto-generated method stub
    }

    public Status getStatus()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
