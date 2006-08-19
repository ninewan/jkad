package jKad.protocol.rpc.request;

import java.math.BigInteger;

import jKad.protocol.KadProtocol;
import jKad.protocol.KadProtocolException;
import jKad.protocol.rpc.RPC;

public class FindValueRPC extends RPC {
		
    /**
     * Estrutura da RPC a ser utilizada via reflection na 
     * construção do pacote UDP em baixo nível
     */
	public static final String[][] DATA_STRUCTURE = {
        {"key", "NODE_ID_LENGTH"}
	};
    
    /**
     * Área de identificação da chave a ser buscada 
     */
    public static final int KEY_AREA = KadProtocol.TOTAL_AREA_LENGTH;
    
    /**
     * Area total desta RPC
     */
    public static final int TOTAL_AREA_LENGTH = KEY_AREA + NODE_ID_LENGTH;
	
	private BigInteger key;                                                     
	
	public BigInteger getKey() {
		return key;
	}
	
	public void setKey(BigInteger key) throws KadProtocolException {
		if(key == null){
			throw new KadProtocolException("Cannot set key to null");
		} else if(key.bitLength() > KadProtocol.NODE_ID_LENGTH * 8) {
			throw new KadProtocolException(
                "Key must have " + KadProtocol.NODE_ID_LENGTH * 8 + " bits, " +
                "found " + key.bitLength() + " bits" 
            );
		} else { 
			this.key = key;
		}
	}
	
	public String[][] getDataStructure(){
		return DATA_STRUCTURE;
	}
    
    public int getInfoLength(){
        return TOTAL_AREA_LENGTH;
    }
}
