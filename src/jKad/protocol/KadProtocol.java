package jKad.protocol;
/**
 * Interface que guarda as constantes do protocolo Kademlia utilizadas principalmente
 * na construção do pacote, definindo sua estrutura e tamanho.
 * @author Bruno C. A. Penteado
 */
public interface KadProtocol {
	/**
	 * Identificador de um RPC do tipo PING
	 */
	public static final byte PING = 1;
	/**
	 * Identificador de um RPC do tipo STORE
	 */
	public static final byte STORE = 2;
	/**
	 * Identificador de um RPC do tipo FIND_NODE
	 */
	public static final byte FIND_NODE = 4;
	/**
	 * Identificador de um RPC do tipo FIND_VALUE
	 */
	public static final byte FIND_VALUE = 8;
	
	/**
	 * Tamanho de um identificador de um nó em Bytes
	 */
	public static final int NODE_ID_LENGTH = 20;
    /**
     * Tamanho de uma chave identificadora de valor
     */
    public static final int KEY_LENGTH = 20;
	/**
	 * Tamanho de um identificador de uma RPC em Bytes
	 */
	public static final int RPC_ID_LENGTH = 20;
	/**
	 * Tamanho da area de tipo em Bytes
	 */
	public static final int TYPE_AREA_LENGTH = 1;
	/**
	 * Tamanho da area de resposta em Bytes
	 */
	public static final int RESPONSE_AREA_LENGTH = 1;
	/**
	 * Tamanho da area de valor de uma chave em Bytes
	 */
	public static final int VALUE_LENGTH = 160;
    
    /**
     * Tamanho de um identificador de pedaço de uma informação.<br>
     * Utilizado principalmente na quebra de valores de chaves em diversos pacotes.
     */
    public static final int PIECE_LENGTH = 1;
	
	/*
	 * Constantes para construção dos dados encapsulados no pacote que trafega na rede
	 */
	
	/**
	 * Área de identificação do RPC, podendo ser do tipos PING, STORE, FIND_NODE e FIND_VALUE.
	 */
	public static final int TYPE_AREA = 0;
	
	/**
	 * Área de identificação de encaminhamento do pacote, que pode ser uma requisiçao (Valor 0)
	 * ou então uma resposta (Valor > 0), sendo este número um identificador numérico da resposta
	 * pois uma requisição pode gerar mais de uma resposta.
	 */
	public static final int RESPONSE_AREA = TYPE_AREA + TYPE_AREA_LENGTH;
	
	/**
	 * Área de identificação do RPC. Todo RPC possui um identificador aleatório que é
	 * compartilhado entre o destinatário e as respostas do remetente
	 */
	public static final int RPC_ID_AREA = RESPONSE_AREA + RESPONSE_AREA_LENGTH;
    
    /**
     * Área de identificação do remetente do RPC. 
     * Todo RPC possui o idnetificador do remetente imbutido.
     */
    public static final int SENDER_ID_AREA = RPC_ID_AREA + RPC_ID_LENGTH;
    
    /**
     * Área de identificação do destinatário do RPC. 
     * Todo RPC possui o idnetificador do destinatário embutido para minimizar forjamento
     * de endereços e ataques DoS.
     */
    public static final int DESTINATION_ID_AREA = SENDER_ID_AREA + NODE_ID_LENGTH;
    
    /**
     * Area total da estrutura simples presente em todos os RPCs.
     */
    public static final int TOTAL_AREA_LENGTH = DESTINATION_ID_AREA + NODE_ID_LENGTH;
    
    /**
     * Constante de tamanho dos buckets
     */
    public static final int BUCKET_SIZE = 20;
    
    public String[][] getDataStructure();
    
    public int getInfoLength();
}
