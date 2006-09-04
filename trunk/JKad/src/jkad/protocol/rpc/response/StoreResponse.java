/* SVN Info:
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$                             
 * $LastChangedDate$  
 */
package jkad.protocol.rpc.response;

import jkad.protocol.rpc.RPC;

public class StoreResponse extends RPC
{
    /**
     * Estrutura da RPC a ser utilizada via reflection na construção do pacote
     * UDP em baixo nível
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
        return STORE;
    }
}
