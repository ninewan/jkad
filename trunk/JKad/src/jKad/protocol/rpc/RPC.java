package jKad.protocol.rpc;

import java.math.BigInteger;

import jKad.protocol.KadProtocol;
import jKad.protocol.KadProtocolException;

/**
 * Classe Abstrata RPC.
 * Esta classe representa uma RPC (Remote Procedure Call), sendo esta a forma de troca
 * de mensagens entre hosts e assim funcionando como um objeto transação.<br>
 * Nela estão presentes todos os dados da resposta ou requisição de um host.
 * @see jKad.protocol.KadProtocol
 * @author Bruno C. A. Penteado
 */
public abstract class RPC implements KadProtocol{
	private BigInteger senderNodeID;
    private BigInteger destinationNodeID;
	private BigInteger RPCID;
	
	/**
	 * Retorna identicador do nó remetente deste RPC.<br>
	 * RPCs geradas localmente, ou seja, requisições efetuadas por este middleware 
	 * (response = 0) sempre terão o mesmo valor de nodeID. 
	 * @return identificador do nó remetente desta RPC.
	 */
	public BigInteger getSenderNodeID(){
		return this.senderNodeID;
	}
	
	public void setSenderNodeID(BigInteger node) throws KadProtocolException{
		senderNodeID = node;
	}
	
    public BigInteger getDestinationNodeID(){
        return this.destinationNodeID;
    }
    
    public void setDestinationNodeID(BigInteger node) throws KadProtocolException{
        destinationNodeID = node;
    }
    
	public BigInteger getRPCID() {
		return this.RPCID;
	}
	
	public void setRPCID(BigInteger RPCID) throws KadProtocolException{
		if(RPCID != null && RPCID.bitLength() > KadProtocol.RPC_ID_LENGTH * 8)
			throw new KadProtocolException(
				"RPC ID must have " + (KadProtocol.RPC_ID_LENGTH * 8)+ " bits, " +
				"found " + RPCID.bitLength() + " bits"
			);
		else 
			this.RPCID = RPCID;
	} 
    
    public abstract String[][] getDataStructure();
    
    public abstract int getInfoLength();
}
