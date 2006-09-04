/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jKad.controller.threads;

public interface Stoppable
{
    public void stopThread();

    public boolean isStopped();
}
