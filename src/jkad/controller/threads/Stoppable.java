/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.controller.threads;

public interface Stoppable
{
    public void stopThread();

    public boolean isStopped();
}
