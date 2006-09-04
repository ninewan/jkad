/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package rpctester;

import jkad.protocol.rpc.RPC;

public class RPCOption
{
    private String name;
    private RPC rpc;

    public RPCOption(String name, RPC rpc)
    {
        this.name = name;
        this.rpc = rpc;
    }
    
    public RPC getRPC()
    {
        return rpc;
    }
    
    public String toString()
    {
        return name;
    }
}
