/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.controller.handlers.request;

import jkad.controller.handlers.HandlerThread;
import jkad.tools.ToolBox;

public class PingHandler extends HandlerThread
{
    public PingHandler()
    {
    	super(ToolBox.getReflectionTools().generateThreadName(PingHandler.class));
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

    @Override
    public void clear()
    {
        // TODO Auto-generated method stub
        
    }
}
