package jKad.protocol;

/**
 * Classe representando uma violação ao protocolo Kademlia, sendo disparada tanto 
 * na construção de pacotes como de transações.
 * @author Bruno C. A. Penteado
 */
public class KadProtocolException extends Exception {
	
	/**
	 * Atributo para facilitar a serialização.
	 */
	public static final long serialVersionUID = -1184632623072714752L;
	
	/**
	 * Constroi nova Excessão do protocolo Kademlia.
	 * @param message mensagem da Excessão.
	 * @param cause causa da Excessão.
	 */
	public KadProtocolException(String message, Throwable cause){
		super(message, cause);
	}
	
	/**
	 * Constroi nova Excessão do protocolo Kademlia.
	 * @param message mensagem da Excessão.
	 */
	public KadProtocolException(String message){
		super(message);
	}
	
	/**
	 * Constroi nova Excessão do protocolo Kademlia.
	 * @param cause causa da Excessão.
	 */
	public KadProtocolException(Throwable cause){
		super(cause);
	}
}
