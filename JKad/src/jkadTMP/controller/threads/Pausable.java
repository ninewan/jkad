/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jKad.controller.threads;

public interface Pausable
{
    public void pauseThread();

    public void playThread();

    public boolean isPaused();
}
