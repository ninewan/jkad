/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.controller.threads.systemaccess;

import jkad.controller.handlers.Controller;

public class FindThread extends AccessThread
{
    private byte[] key;
    private byte[] result;
    
    protected FindThread(ThreadGroup group, Controller controller, byte[] key)
    {
        super(group, controller);
        this.key = key;
        this.result = null;
    }

    public void run()
    {
        result = this.controller.findValue(key);
    }
    
    public byte[] getValue()
    {
        return this.result;
    }

}
