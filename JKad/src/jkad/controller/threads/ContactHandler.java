/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.controller.threads;

import jkad.controller.ThreadGroupLocal;

public class ContactHandler
{
    private static ThreadGroupLocal<ContactHandler> instance;
    
    public static ContactHandler getInstance()
    {
        if(instance == null)
        {
            instance = new ThreadGroupLocal<ContactHandler>()
            {
                public ContactHandler initialValue()
                {
                    return new ContactHandler();
                }
            };
        }
        return instance.get();
    }
    
    public addContact();
}
