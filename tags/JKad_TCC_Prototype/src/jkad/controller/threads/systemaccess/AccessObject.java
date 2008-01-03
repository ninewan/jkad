/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.controller.threads.systemaccess;

import jkad.controller.handlers.Controller;
import jkad.facades.user.NetLocation;
import jkad.facades.user.UserFacade;

public class AccessObject implements UserFacade
{
    protected Controller controller;
    protected ThreadGroup threadGroup;
    
    public AccessObject(ThreadGroup threadGroup, Controller controller)
    {
        this.controller = controller;
        this.threadGroup = threadGroup;
    }

    public void login(NetLocation anotherNode)
    {
        LoginThread thread = new LoginThread(threadGroup, controller, anotherNode);
        execSync(thread);
    }
    
    public String findValue(String key)
    {
        byte[] result = this.findValue(key.getBytes());
        return result != null ? new String(result) : null;
    }

    public byte[] findValue(byte[] key)
    {
        FindThread thread = new FindThread(threadGroup, controller, key);
        execSync(thread);
        return thread.getValue();
    }

    public void store(String key, String data)
    {
        this.store(key.getBytes(), data.getBytes());
    }

    public void store(byte[] key, byte[] data)
    {
        StoreThread thread = new StoreThread(threadGroup, controller, key, data);
        execSync(thread);
    }
    
    private void execSync(Thread thread)
    {
        thread.start();
        while(thread.isAlive())
        {
            try
            {
                thread.join();
            } catch (InterruptedException e) { }
        }
    }
}
