/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.controller.io;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import jkad.controller.ThreadGroupLocal;

public class SingletonSocket
{
    private static Integer actualPort = null;

    private static ThreadGroupLocal<JKadDatagramSocket> tlSocket = new ThreadGroupLocal<JKadDatagramSocket>()
    {
        public JKadDatagramSocket initialValue()
        {
            JKadDatagramSocket socket = null;
            try
            {
                if(actualPort == null)
                {
                    String startPort = System.getProperty("jkad.socket.startPort");
                    actualPort = Integer.parseInt(startPort);
                }
                socket = new JKadDatagramSocket(actualPort, InetAddress.getLocalHost());
            } catch (UnknownHostException e)
            {
                e.printStackTrace();
            } catch (SocketException e)
            {
                e.printStackTrace();
            }
            actualPort++;
            return socket;
        }
    };

    protected SingletonSocket()
    {
    }

    public static JKadDatagramSocket getInstance()
    {
        return tlSocket.get();
    }
}