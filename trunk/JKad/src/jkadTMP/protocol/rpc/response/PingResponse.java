/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jKad.protocol.rpc.response;

import jKad.protocol.rpc.RPC;

public class PingResponse extends RPC
{
    /**
     * Estrutura da RPC a ser utilizada via reflection na constru��o do pacote
     * UDP em baixo n�vel
     */
    public static final String[][] DATA_STRUCTURE = {};

    public String[][] getDataStructure()
    {
        return DATA_STRUCTURE;
    }

    public int getInfoLength()
    {
        return TOTAL_AREA_LENGTH;
    }

    public byte getType()
    {
        return PING;
    }
}
