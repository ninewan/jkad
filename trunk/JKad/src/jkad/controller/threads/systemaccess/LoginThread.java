/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.controller.threads.systemaccess;

import jkad.controller.handlers.Controller;
import jkad.facades.user.NetLocation;

class LoginThread extends AccessThread
{
    private NetLocation anotherNode;
    
    protected LoginThread(ThreadGroup group, Controller controller, NetLocation anotherNode)
    {
        super(group, controller);
        this.anotherNode = anotherNode;
    }
    
    public void run()
    {
        controller.login(anotherNode);
    }
}
