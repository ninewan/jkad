package jKad.builders.implementation.reflection;

import jKad.builders.RPCFactory;
import jKad.protocol.KadProtocol;
import jKad.protocol.KadProtocolException;
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
import jKad.tools.ReflectionTools;
import jKad.tools.ToolBox;

import java.nio.ByteBuffer;

public class ReflectionRPCFactoryImp extends RPCFactory {

    public RPC buildRPC(byte[] data) throws KadProtocolException {
        DataTools dataTools = ToolBox.getDataTools();
        ReflectionTools reflTools = ToolBox.getReflectionTools();
        RPC rpc = null;
        
        /* Verifica se a mensagem tem tamanho correto */
        if(data.length < KadProtocol.TOTAL_AREA_LENGTH){
            throw new KadProtocolException("Expected at least " + KadProtocol.TOTAL_AREA_LENGTH + " bytes, found " + data.length);
        }
        
        /* Descobre qual o tipo de RPC */
        byte type = data[KadProtocol.TYPE_AREA];
        
        /* Descobre qual o response deste RPC. */
        byte response = data[KadProtocol.RESPONSE_AREA];
        
        if(response < 0){
            throw new KadProtocolException("Invalid response code: " + response);
        } else if (response == 0){
            switch (type) {
                case KadProtocol.FIND_VALUE: rpc = new FindValueRPC(); break; 
                case KadProtocol.FIND_NODE: rpc = new FindNodeRPC(); break;
                case KadProtocol.STORE: rpc = new StoreRPC(); break;
                case KadProtocol.PING: rpc = new PingRPC(); break;
                default: throw new KadProtocolException("Unknown RPC type: " + type);
            }
        } else {
            switch (type) {
                case KadProtocol.FIND_VALUE: rpc = new FindValueResponse(); break;    
                case KadProtocol.FIND_NODE: rpc = new FindNodeResponse(); break;
                case KadProtocol.STORE: rpc = new StoreResponse(); break;
                case KadProtocol.PING: rpc = new PingResponse(); break;
                default: throw new KadProtocolException("Unknown RPC type: " + type);
            }
        }
        
        /* Verifica se a mensagem tem tamanho correto */
        if(data.length != rpc.getInfoLength()){
            throw new KadProtocolException("Constructing " + rpc.getClass().getName() + ": Expected " + rpc.getInfoLength() + " bytes, found " + data.length);
        }
        
        /* Constroi infos básicas presentes em todos as RPCS */
        buildBasicInfo(data, rpc);
        /* resgata matriz com mapeamento dos campos as áreas do pacote*/
        String[][] structure = rpc.getDataStructure();
       
        try {
            /* Através da estrutura mapeada na RPC, a RPC é construida via reflection
             * invocando metodos setters para cada campo da RPC */
            for(int i = 0, position = KadProtocol.TOTAL_AREA_LENGTH; i < structure.length; i++){
                String fieldName = structure[i][0];
                int value = reflTools.getFieldValue(structure[i][1], rpc);
                /* Extrai dados que deverão ir neste campo */
                byte[] extracted = dataTools.copyByteArray(data, position, value);
                /* Invoca metodo setter do campo.
                 * Caso o vetor possua apenas 1 posição, invoque para 1 Byte */
                if(extracted.length > 1){
                    reflTools.invokeSetter(fieldName, extracted, rpc);
                } else {
                    reflTools.invokeSetter(fieldName, extracted[0], rpc);
                }
            }
        } catch (NoSuchFieldException e){
            e.printStackTrace();
            return null;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
        return rpc;
    }

    private void buildBasicInfo(byte[] data, RPC rpc) throws KadProtocolException {
        DataTools dataTools = ToolBox.getDataTools();
        byte[] senderNodeID = dataTools.copyByteArray(
            data, 
            KadProtocol.SENDER_ID_AREA,
            KadProtocol.NODE_ID_LENGTH
        ); 
        byte[] rpcID = dataTools.copyByteArray(
            data, 
            KadProtocol.RPC_ID_AREA,
            KadProtocol.RPC_ID_LENGTH
        );
        byte[] destinationNodeID = dataTools.copyByteArray(
            data, 
            KadProtocol.DESTINATION_ID_AREA,
            KadProtocol.NODE_ID_LENGTH
        );
              
//        rpc.setRPCID(rpcID);
//        rpc.setSenderNodeID(senderNodeID);
//        rpc.setDestinationNodeID(destinationNodeID);
    }
}
