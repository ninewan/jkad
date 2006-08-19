package jKad.protocol;

/**
 * Classe representando uma viola��o ao protocolo Kademlia, sendo disparada tanto 
 * na constru��o de pacotes como de transa��es.
 * @author Bruno C. A. Penteado
 */
public class KadProtocolException extends Exception {
	
	/**
	 * Atributo para facilitar a serializa��o.
	 */
	public static final long serialVersionUID = -1184632623072714752L;
	
	/**
	 * Constroi nova Excess�o do protocolo Kademlia.
	 * @param message mensagem da Excess�o.
	 * @param cause causa da Excess�o.
	 */
	public KadProtocolException(String message, Throwable cause){
		super(message, cause);
	}
	
	/**
	 * Constroi nova Excess�o do protocolo Kademlia.
	 * @param message mensagem da Excess�o.
	 */
	public KadProtocolException(String message){
		super(message);
	}
	
	/**
	 * Constroi nova Excess�o do protocolo Kademlia.
	 * @param cause causa da Excess�o.
	 */
	public KadProtocolException(Throwable cause){
		super(cause);
	}
}
