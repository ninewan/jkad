package jKad.builders.implementation;

import jKad.builders.OutputBuilder;
import jKad.protocol.KadProtocol;
import jKad.protocol.rpc.RPC;
import jKad.protocol.rpc.request.FindNodeRPC;
import jKad.protocol.rpc.request.FindValueRPC;
import jKad.protocol.rpc.request.PingRPC;
import jKad.protocol.rpc.request.StoreRPC;
import jKad.protocol.rpc.response.FindNodeResponse;
import jKad.protocol.rpc.response.FindValueResponse;
import jKad.protocol.rpc.response.PingResponse;
import jKad.protocol.rpc.response.StoreResponse;
import jKad.tools.DataTools;
import jKad.tools.ToolBox;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class OutputBuilderImp extends OutputBuilder {

	public ByteBuffer buildData(RPC rpc) {
		DataTools tool = ToolBox.getDataTools();
        ByteBuffer data;
        if(rpc instanceof FindNodeRPC){
            FindNodeRPC castedRPC = (FindNodeRPC) rpc;
            data = ByteBuffer.allocate(FindNodeRPC.TOTAL_AREA_LENGTH);
            /* constroi as 5 infos basicas de todas RPCs:
             * TYPE, RESPONSE, RPC ID, SENDER ID e NODE ID */
            this.buildbasicInfo(data, rpc, KadProtocol.FIND_NODE, (byte)0);
            /* posiciona no final do basico de toda rpc e 
             * preenche com informação especifica da rpc de FindNode*/
            data.position(FindNodeRPC.SEARCHED_NODE_AREA);
            byte[] array = castedRPC.getSearchedNodeID().toByteArray();
            data.put(tool.formatByteArray(array, KadProtocol.NODE_ID_LENGTH));
        
        } else if(rpc instanceof FindValueRPC) {
            FindValueRPC castedRPC = (FindValueRPC) rpc;
            data = ByteBuffer.allocate(FindValueRPC.TOTAL_AREA_LENGTH);
            /* constroi as 5 infos basicas de todas RPCs:
             * TYPE, RESPONSE, RPC ID, SENDER ID e NODE ID */
            this.buildbasicInfo(data, rpc, KadProtocol.FIND_VALUE, (byte)0);
            /* posiciona no final do basico de toda rpc e 
             * preenche com informação especifica da rpc de FindValue*/
            data.position(FindValueRPC.KEY_AREA);
            byte[] array = castedRPC.getKey().toByteArray();
            data.put(tool.formatByteArray(array, KadProtocol.KEY_LENGTH));
        
        } else if(rpc instanceof PingRPC) {
            data = ByteBuffer.allocate(PingRPC.TOTAL_AREA_LENGTH);
            /* constroi as 5 infos basicas de todas RPCs:
             * TYPE, RESPONSE, RPC ID, SENDER ID e NODE ID */
            this.buildbasicInfo(data, rpc, KadProtocol.PING, (byte)0);
            /* ping RPC nao possui informação especifica portanto 
             * basta informações basicas */
            
        } else if(rpc instanceof StoreRPC) {
            StoreRPC castedRPC = (StoreRPC) rpc;
            byte[] array;
            data = ByteBuffer.allocate(StoreRPC.TOTAL_AREA_LENGTH);
            /* constroi as 5 infos basicas de todas RPCs:
             * TYPE, RESPONSE, RPC ID, SENDER ID e NODE ID */
            this.buildbasicInfo(data, rpc, KadProtocol.STORE, (byte)0);
            
            /* posiciona no final do basico de toda rpc e 
             * preenche com informação especifica da rpc Store*/
            data.position(StoreRPC.PIECE_AREA);
            data.put(castedRPC.getPiece());
            
            data.position(StoreRPC.PIECE_TOTAL_AREA);
            data.put(castedRPC.getPieceTotal());
           
            data.position(StoreRPC.KEY_AREA);
            array = castedRPC.getKey().toByteArray();
            data.put(tool.formatByteArray(array, KadProtocol.KEY_LENGTH));
            
            data.position(StoreRPC.VALUE_AREA);
            array = castedRPC.getValue().toByteArray();
            data.put(tool.formatByteArray(array, KadProtocol.VALUE_LENGTH));
            
        } else if(rpc instanceof FindNodeResponse) {
            FindNodeResponse castedRPC = (FindNodeResponse) rpc;
            byte[] array;
            data = ByteBuffer.allocate(FindNodeResponse.TOTAL_AREA_LENGTH);
            /* constroi as 5 infos basicas de todas RPCs:
             * TYPE, RESPONSE, RPC ID, SENDER ID e NODE ID */
            this.buildbasicInfo(data, rpc, KadProtocol.FIND_NODE, (byte)1);
            
            /* posiciona no final do basico de toda rpc e 
             * preenche com informação especifica da rpc FindNodeResponse*/
            data.position(FindNodeResponse.FOUND_NODE_AREA);
            array = castedRPC.getFoundNodeID().toByteArray();
            data.put(tool.formatByteArray(array, KadProtocol.NODE_ID_LENGTH));
            
            data.position(FindNodeResponse.IP_AREA);
            array = castedRPC.getIpAddress().toByteArray();
            data.put(tool.formatByteArray(array, FindNodeResponse.IP_ADDRESS_LENGTH));
            
            data.position(FindNodeResponse.PORT_AREA);
            array = castedRPC.getPort().toByteArray();
            data.put(tool.formatByteArray(array, FindNodeResponse.PORT_LENGTH));
            
        } else if(rpc instanceof FindValueResponse) {
            FindValueResponse castedRPC = (FindValueResponse) rpc;
            data = ByteBuffer.allocate(FindValueResponse.TOTAL_AREA_LENGTH);
            /* constroi as 5 infos basicas de todas RPCs:
             * TYPE, RESPONSE, RPC ID, SENDER ID e NODE ID */
            this.buildbasicInfo(data, rpc, KadProtocol.FIND_VALUE, (byte)1);
            /* posiciona no final do basico de toda rpc e 
             * preenche com informação especifica da rpc FindNodeResponse*/
            data.position(FindValueResponse.PIECE_AREA);
            data.put(castedRPC.getPiece());
            
            data.position(FindValueResponse.PIECE_TOTAL_AREA);
            data.put(castedRPC.getPieceTotal());
            
            data.position(FindValueResponse.VALUE_AREA);
            byte[] array = castedRPC.getValue().toByteArray();
            data.put(tool.formatByteArray(array, KadProtocol.VALUE_LENGTH));
            
        } else if(rpc instanceof PingResponse) {
            data = ByteBuffer.allocate(PingResponse.TOTAL_AREA_LENGTH);
            /* constroi as 5 infos basicas de todas RPCs:
             * TYPE, RESPONSE, RPC ID, SENDER ID e NODE ID */
            this.buildbasicInfo(data, rpc, KadProtocol.PING, (byte)1);
            /* ping response nao possui informação especifica portanto 
             * basta informações basicas */
            
        } else if(rpc instanceof StoreResponse) {
            data = ByteBuffer.allocate(StoreResponse.TOTAL_AREA_LENGTH);
            /* constroi as 5 infos basicas de todas RPCs:
             * TYPE, RESPONSE, RPC ID, SENDER ID e NODE ID */
            this.buildbasicInfo(data, rpc, KadProtocol.STORE, (byte)1);
            /* Store response nao possui informação especifica portanto 
             * basta informações basicas */
            
        } else {
            data = null;
        }
        return data;
	}
    
    public DatagramPacket buildPacket(RPC rpc, String ip, int port) throws UnknownHostException {
        return this.buildPacket(rpc, InetAddress.getByName(ip), port);
    }
    
	public DatagramPacket buildPacket(RPC rpc, InetAddress ip, int port) {
        ByteBuffer data = this.buildData(rpc);
        return new DatagramPacket(data.array(), data.array().length, ip, port);
	}
    
    private void buildbasicInfo(ByteBuffer data, RPC rpc, byte type, byte response){
        DataTools tool = ToolBox.getDataTools();
        byte[] array;
        //preenche com o tipo
        data.position(KadProtocol.TYPE_AREA);
        data.put(type);
        
        //indica se é uma requisição ou resposta
        data.position(KadProtocol.RESPONSE_AREA);
        data.put(response);
        
        //preenche com o identificador da RPC
        data.position(KadProtocol.RPC_ID_AREA);
        array = rpc.getRPCID().toByteArray();
        data.put(tool.formatByteArray(array, KadProtocol.RPC_ID_LENGTH));
        
        //preenche com o ientificador do remetente
        data.position(KadProtocol.SENDER_ID_AREA);
        array = rpc.getSenderNodeID().toByteArray();
        data.put(tool.formatByteArray(array, KadProtocol.NODE_ID_LENGTH));
        
        //preenche com o identificador do destinatario
        data.position(KadProtocol.DESTINATION_ID_AREA);
        array = rpc.getDestinationNodeID().toByteArray();
        data.put(tool.formatByteArray(array, KadProtocol.NODE_ID_LENGTH));
    }
    
}
