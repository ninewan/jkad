/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jKad.controller.io;

import jKad.controller.threads.CyclicThread;
import jKad.structures.JKadDatagramSocket;

import java.util.Observable;
import java.util.Observer;

public abstract class UDPHandler extends CyclicThread implements Observer
{
    public UDPHandler(String name)
    {
        super(name);
    }
    
    public void update(Observable o, Object arg)
    {
        JKadDatagramSocket socket = (JKadDatagramSocket) o;
        JKadDatagramSocket.Action action = (JKadDatagramSocket.Action) arg;
        if(socket == this.getSocket())
        {
            if(action == JKadDatagramSocket.Action.CLOSE_SOCKET)
                this.interrupt();
        }
    }
    
    public void stopThread()
    {
        this.interrupt();
        getSocket().notifyObservers(JKadDatagramSocket.Action.CLOSE_SOCKET);
        super.stopThread();
    }
    
    protected abstract JKadDatagramSocket getSocket();
    
    public abstract long getHandledPacketAmount();
}