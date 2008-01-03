/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.controller.threads.systemaccess;

import jkad.controller.handlers.Controller;

public class StoreThread extends AccessThread
{
    private byte[] key;
    private byte[] data;
    
    protected StoreThread(ThreadGroup group, Controller controller, byte[] key, byte[] data)
    {
        super(group, controller);
        this.key = key;
        this.data = data;
    }
    
    public void run()
    {
        this.controller.store(key, data);
    }
}
