package jKad.protocol.rpc.response;

import jKad.protocol.KadProtocol;
import jKad.protocol.KadProtocolException;
import jKad.protocol.rpc.RPC;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public class FindNodeResponse extends RPC {
	
    /**
     * Estrutura da RPC a ser utilizada via reflection na 
     * construção do pacote UDP em baixo nível
     */
    public static final String[][] DATA_STRUCTURE = {
        {"foundNodeID", "NODE_ID_LENGTH"},
        {"ipAddress", "IP_ADDRESS_LENGTH"},
        {"port", "PORT_LENGTH"}
    };
    
    /**
     * Tamanho de um endereço IP em Bytes
     */
    public static final int IP_ADDRESS_LENGTH = Integer.SIZE / 8;
    
    /**
     * Tamanho de uma porta em Bytes
     */
    public static final int PORT_LENGTH = Integer.SIZE / 8;
    
    /**
     * Area de identificação do nó achado
     */
    public static final int FOUND_NODE_AREA = KadProtocol.TOTAL_AREA_LENGTH;
    
    /**
     * Area de identificação do ip do nó achado
     */
    public static final int IP_AREA = FOUND_NODE_AREA + NODE_ID_LENGTH;
    
    /**
     * Area de identificação da porta do nó achado
     */
    public static final int PORT_AREA = IP_AREA + IP_ADDRESS_LENGTH;
    
    /**
     * Area total desta RPC
     */
    public static final int TOTAL_AREA_LENGTH = PORT_AREA + PORT_LENGTH;
	
	private BigInteger foundNodeID;
	private BigInteger ipAddress;
    private BigInteger port;
    
	public BigInteger getFoundNodeID() {
		return foundNodeID;
	}
	
	public void setFoundNodeID(BigInteger foundNodeID) throws KadProtocolException {
		if(foundNodeID == null){
			throw new KadProtocolException("Cannot set foundNodeID to null");
        } else if(foundNodeID.bitLength() > KadProtocol.NODE_ID_LENGTH * 8) {
            throw new KadProtocolException(
                "foundNodeID must have " + (KadProtocol.NODE_ID_LENGTH * 8) + " bits, " +
                "found " + foundNodeID.bitLength() + " bits"
            );
        } else {
            this.foundNodeID = foundNodeID;
        }
	}
    
     public BigInteger getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(BigInteger ipAddress) throws KadProtocolException {
    	if(ipAddress == null){
			throw new KadProtocolException("Cannot set ipAddress to null");
        } else if(ipAddress.bitLength() > FindNodeResponse.IP_ADDRESS_LENGTH * 8) {
            throw new KadProtocolException(
                "ipAddress must have " + (FindNodeResponse.IP_ADDRESS_LENGTH * 8)+ " bits, " +
                "found " + ipAddress.bitLength() + " bits"
            );
        } else {
        	this.ipAddress = ipAddress;
        }
    }
    
    public BigInteger getPort() {
        return port;
    }

    public void setPort(BigInteger port) throws KadProtocolException {
    	if(port == null){
			throw new KadProtocolException("Cannot set port to null");
        } else if(port.bitLength() > FindNodeResponse.PORT_LENGTH * 8) {
            throw new KadProtocolException(
                "port must have " + (FindNodeResponse.PORT_LENGTH * 8)+ " bits, " +
                "found " + port.bitLength() + " bits"
            );
        } else {
        	this.port = port;
        }
    }
    
    public String[][] getDataStructure(){
        return DATA_STRUCTURE;
    }
    
    public int getInfoLength(){
        return TOTAL_AREA_LENGTH;
    }
}
