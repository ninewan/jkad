package jKad.protocol.rpc.request;

import java.math.BigInteger;

import jKad.protocol.KadProtocol;
import jKad.protocol.KadProtocolException;
import jKad.protocol.rpc.RPC;

/**
 * Classe FindNodeRPC.<br>
 * Esta classe representa uma RPC de busca por um determinado n� no sistema.
 * @author Bruno C. A. Penteado
 */
public class FindNodeRPC extends RPC{
	
	/**
	 * Estrutura da RPC a ser utilizada via reflection na 
     * constru��o do pacote UDP em baixo n�vel
	 */
	public static final String[][] DATA_STRUCTURE = {
        {"searchedNodeID", "NODE_ID_LENGTH"}
	};
    
    /**
     * �rea de identifica��o do n� a ser buscado.
     */
    public static final int SEARCHED_NODE_AREA = KadProtocol.TOTAL_AREA_LENGTH;
    
    /**
     * Area total desta RPC
     */
    public static final int TOTAL_AREA_LENGTH = SEARCHED_NODE_AREA + NODE_ID_LENGTH;
    
	private BigInteger searchedNodeID;                                                     
	
	/**
	 * @return n� a ser buscado por esta RPC
	 */
	public BigInteger getSearchedNodeID() {
		return searchedNodeID;
	}
	
	/**
	 * Ajusta o n� a ser buscado por esta RPC
	 * @param searchedNodeID n� a ser buscado por esta RPC
	 * @throws KadProtocolException
	 */
	public void setSearchedNodeID(BigInteger searchedNodeID) throws KadProtocolException {
		if(searchedNodeID == null ){
			throw new KadProtocolException("Cannot set Searched Node ID to null");
		} else if (searchedNodeID.bitLength() > NODE_ID_LENGTH * 8) {
            throw new KadProtocolException(
                "SearchedNodeID must have " + KadProtocol.NODE_ID_LENGTH * 8 + " bits, " +
                "found " + searchedNodeID.bitLength() + " bits" 
            );
        } else {
            this.searchedNodeID = searchedNodeID;
        }
	}

	public String[][] getDataStructure(){
		return DATA_STRUCTURE;
	}
    
    public int getInfoLength(){
        return TOTAL_AREA_LENGTH;
    }
}
