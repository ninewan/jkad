package jKad.protocol.rpc.response;

import jKad.protocol.KadProtocol;
import jKad.protocol.KadProtocolException;
import jKad.protocol.rpc.RPC;

import java.math.BigInteger;

public class FindValueResponse extends RPC{
	
    /**
     * Estrutura da RPC a ser utilizada via reflection na 
     * constru��o do pacote UDP em baixo n�vel
     */
    public static final String[][] DATA_STRUCTURE = {
        {"piece", "PIECE_LENGTH"},
        {"pieceTotal","PIECE_LENGTH"},
        {"value", "VALUE_LENGTH"}
    };
    
    /**
     * �rea de identifica��o da parte da informa��o.
     * Trabalha em conjunto com total de partes da informa��o 
     */
    public static final int PIECE_AREA = KadProtocol.TOTAL_AREA_LENGTH;
    
    /**
     * �rea de identifica��o do total de partes da informa��o.
     * Trabalha em conjunto com o identificador de partes da informa��o 
     */
    public static final int PIECE_TOTAL_AREA = PIECE_AREA + PIECE_LENGTH;
    
    /**
     * �rea de identifica��o do valor a ser guardado.
     */
    public static final int VALUE_AREA = PIECE_TOTAL_AREA + PIECE_LENGTH;
    
    /**
     * Area total desta RPC
     */
    public static final int TOTAL_AREA_LENGTH = VALUE_AREA + VALUE_LENGTH;
    
    private BigInteger value;
    private byte piece;
    private byte pieceTotal;
    
	public BigInteger getValue() {
		return value;
	}

	public void setValue(BigInteger value) throws KadProtocolException {
        if(value == null) {
            throw new KadProtocolException("Cannot set value to null" );
        } else if(value.bitLength() > KadProtocol.VALUE_LENGTH * 8) {
            throw new KadProtocolException(
                "Value must have " + (KadProtocol.VALUE_LENGTH * 8) + " bits, " +
                "found " + value.bitLength() + " bits" 
            );
        } else {
            this.value = value;
        }
	}
    
    public byte getPiece() {
        return piece;
    }

    public void setPiece(byte piece) {
        this.piece = piece;
    }

    public byte getPieceTotal() {
        return pieceTotal;
    }

    public void setPieceTotal(byte pieceTotal) {
        this.pieceTotal = pieceTotal;
    }
    
    public String[][] getDataStructure(){
        return DATA_STRUCTURE;
    }
    
    public int getInfoLength(){
        return TOTAL_AREA_LENGTH;
    }
}
