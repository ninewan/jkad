/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.controller.handlers.misc;

import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import jkad.structures.kademlia.KadNode;
import jkad.structures.kademlia.KnowContacts;

import org.apache.log4j.Logger;

public class ContactHandler extends KnowContacts implements Runnable
{
    public static final long DEFAULT_REFRESH = 10000;
    public static final long DEFAULT_EXPIRE = 1000 * 60 * 10; //10 minutos
    
    private static final Logger logger = Logger.getLogger(ContactHandler.class);

    private Timer timer;
    private RefreshTask refreshTask;
    private long refreshPeriod;
    
    public ContactHandler()
    {
        super();
        this.init();
    }

    public ContactHandler(int maxSize)
    {
        super(maxSize);
        this.init();
    }
    
    public ContactHandler(int refreshPeriod, int nodeExpire)
    {
        super();
        this.init(refreshPeriod, nodeExpire);
    }
    
    public ContactHandler(int maxSize, int refreshPeriod, int nodeExpire)
    {
        super(maxSize);
        this.init(refreshPeriod, nodeExpire);
    }
    
    private void init()
    {
        String refreshPeriod = System.getProperty("jkad.contacts.refreshPeriod");
        String nodeExpire = System.getProperty("jkad.contacts.expire");
        long intRefreshPeriod = refreshPeriod != null ? Long.parseLong(refreshPeriod) : DEFAULT_REFRESH;
        long intNodeExpire = nodeExpire != null ? Long.parseLong(nodeExpire) : DEFAULT_EXPIRE;
        this.init(intRefreshPeriod, intNodeExpire);
    }
    
    private void init(long refreshPeriod, long nodeExpire)
    {
        this.refreshTask = new RefreshTask(this, logger, nodeExpire);
        this.refreshPeriod = refreshPeriod;
        this.timer = new Timer(true);
    }

    public void run()
    {
        this.timer.schedule(refreshTask, refreshPeriod, refreshPeriod);
    }
    
    public void setNodeExpire(long expire)
    {
        this.refreshTask.setNodeExpire(expire);
    }
}

class RefreshTask extends TimerTask
{
    private KnowContacts knowContacts;
    private Logger logger;
    private long nodeExpire;
    
    protected RefreshTask(KnowContacts knowContacts, Logger logger, long nodeExpire)
    {
        this.knowContacts = knowContacts;
        this.logger = logger;
        this.nodeExpire = nodeExpire;
    }
    
    protected void setNodeExpire(long nodeExpire)
    {
        this.nodeExpire = nodeExpire;
    }
    
    public void run()
    {
        logger.info("Refreshing contact list");
        long now = System.currentTimeMillis();
        if(knowContacts.getSize() > 0)
        {
            for(Iterator<KadNode> it = knowContacts.getIterator(); it.hasNext();)
            {
                KadNode next = it.next();
                long delta = now - next.getLastAccess();
                if(delta >= nodeExpire)
                {
                    logger.debug("Removing KadNode " + next.toString() + ", last accessed " + (delta / 1000) + " seconds ago");
                    it.remove();
                }
            }
        }
    }
}