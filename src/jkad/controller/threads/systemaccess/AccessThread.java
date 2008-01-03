/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.controller.threads.systemaccess;

import jkad.controller.handlers.Controller;

abstract class AccessThread extends Thread
{
    protected Controller controller;
    protected ThreadGroup group;
    
    protected AccessThread(ThreadGroup group, Controller controller)
    {
        super(group, "AccessThread");
        this.controller = controller;
        this.group = group;
    }
    
    public abstract void run();
    
}
