package jKad.builders.implementation.reflection;

import jKad.builders.OutputBuilder;
import jKad.protocol.KadProtocol;
import jKad.protocol.rpc.RPC;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class ReflectionOutputBuilderImp extends OutputBuilder {

    public ByteBuffer buildData(RPC rpc) {
        return null;
    }

    public DatagramPacket buildPacket(RPC rpc, String ip, int port) throws UnknownHostException {
        return this.buildPacket(rpc, InetAddress.getByName(ip), port);
    }
    
    public DatagramPacket buildPacket(RPC rpc, InetAddress ip, int port) {
        ByteBuffer data = this.buildData(rpc);
        return new DatagramPacket(data.array(), data.array().length, ip, port);
    }
    
    private void buildbasicInfo(ByteBuffer data, RPC rpc, byte type, byte response){
//        //preenche com o tipo
//        data.position(KadProtocol.TYPE_AREA);
//        data.put(type);
//        //indica se é uma requisição ou resposta
//        data.position(KadProtocol.RESPONSE_AREA);
//        data.put(response);
//        //preenche com o identificador da RPC
//        data.position(KadProtocol.RPC_ID_AREA);
//        data.put(rpc.getRPCID());
//        //preenche com o ientificador do remetente
//        data.position(KadProtocol.SENDER_ID_AREA);
//        data.put(rpc.getSenderNodeID());
//        //preenche com o identificador do destinatario
//        data.position(KadProtocol.DESTINATION_ID_AREA);
//        data.put(rpc.getDestinationNodeID());
    }
}
