package jKad.builders.implementation;

import java.math.BigInteger;

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
import jKad.tools.ToolBox;

public class RPCFactoryImp extends RPCFactory {

    public RPC buildRPC(byte[] data) throws KadProtocolException {
        RPC rpc = null;
        // pega priemiro byte que representa o tipo de rpc
        byte type = data[KadProtocol.TYPE_AREA];
        // pega o segundo byte que representa se é resposta ou não
        byte response = data[KadProtocol.RESPONSE_AREA];
        if (response == 0) {
            switch (type) {
            case KadProtocol.PING:
                rpc = this.buildPingRPC(data);
                break;
            case KadProtocol.STORE:
                rpc = this.buildStoreRPC(data);
                break;
            case KadProtocol.FIND_NODE:
                rpc = this.buildFindNodeRPC(data);
                break;
            case KadProtocol.FIND_VALUE:
                rpc = this.buildFindValueRPC(data);
                break;
            default:
                throw new KadProtocolException("Unknown RPC type: " + type);
            }
        } else if (response > 0) {
            switch (type) {
            case KadProtocol.PING:
                rpc = this.buildPingResponse(data);
                break;
            case KadProtocol.STORE:
                rpc = this.buildStoreResponse(data);
                break;
            case KadProtocol.FIND_NODE:
                rpc = this.buildFindNodeResponse(data);
                break;
            case KadProtocol.FIND_VALUE:
                rpc = this.buildFindValueResponse(data);
                break;
            default:
                throw new KadProtocolException("Unknown RPC type: " + type);
            }
        } else {
            throw new KadProtocolException("Invalid response code: " + response);
        }

        return rpc;
    }

    private PingRPC buildPingRPC(byte[] data) throws KadProtocolException {
        PingRPC rpc = new PingRPC();
        buildBasicInfo(data, rpc);
        return rpc;
    }

    private StoreRPC buildStoreRPC(byte[] data) throws KadProtocolException {
        DataTools tools = ToolBox.getDataTools();
        StoreRPC rpc = new StoreRPC();
        buildBasicInfo(data, rpc);
        rpc.setPiece(data[StoreRPC.PIECE_AREA]);
        rpc.setPieceTotal(data[StoreRPC.PIECE_TOTAL_AREA]);
        rpc.setKey(new BigInteger(1, tools.copyByteArray(data, StoreRPC.KEY_AREA, StoreRPC.KEY_LENGTH)));
        rpc.setValue(new BigInteger(1, tools.copyByteArray(data, StoreRPC.VALUE_AREA, StoreRPC.VALUE_LENGTH)));
        return rpc;
    }

    private FindNodeRPC buildFindNodeRPC(byte[] data) throws KadProtocolException {
        DataTools tools = ToolBox.getDataTools();
        FindNodeRPC rpc = new FindNodeRPC();
        buildBasicInfo(data, rpc);
        rpc.setSearchedNodeID(new BigInteger(1, tools.copyByteArray(data, FindNodeRPC.SEARCHED_NODE_AREA, StoreRPC.NODE_ID_LENGTH)));
        return rpc;
    }

    private FindValueRPC buildFindValueRPC(byte[] data) throws KadProtocolException {
        DataTools tools = ToolBox.getDataTools();
        FindValueRPC rpc = new FindValueRPC();
        buildBasicInfo(data, rpc);
        rpc.setKey(new BigInteger(1, tools.copyByteArray(data, FindValueRPC.KEY_AREA, FindValueRPC.KEY_LENGTH)));
        return rpc;
    }

    private PingResponse buildPingResponse(byte[] data) throws KadProtocolException {
        PingResponse rpc = new PingResponse();
        buildBasicInfo(data, rpc);
        return rpc;
    }

    private StoreResponse buildStoreResponse(byte[] data) throws KadProtocolException {
        StoreResponse rpc = new StoreResponse();
        buildBasicInfo(data, rpc);
        return rpc;
    }

    private FindNodeResponse buildFindNodeResponse(byte[] data) throws KadProtocolException {
        DataTools tools = ToolBox.getDataTools();
        FindNodeResponse rpc = new FindNodeResponse();
        buildBasicInfo(data, rpc);
        rpc.setFoundNodeID(new BigInteger(1, tools.copyByteArray(data, FindNodeResponse.FOUND_NODE_AREA, FindNodeResponse.NODE_ID_LENGTH)));
        rpc.setIpAddress(new BigInteger(1, tools.copyByteArray(data, FindNodeResponse.IP_AREA, FindNodeResponse.IP_ADDRESS_LENGTH)));
        rpc.setPort(new BigInteger(1, tools.copyByteArray(data, FindNodeResponse.PORT_AREA, FindNodeResponse.PORT_LENGTH)));
        return rpc;
    }

    private FindValueResponse buildFindValueResponse(byte[] data) throws KadProtocolException {
        DataTools tools = ToolBox.getDataTools();
        FindValueResponse rpc = new FindValueResponse();
        buildBasicInfo(data, rpc);
        rpc.setPiece(data[FindValueResponse.PIECE_AREA]);
        rpc.setPieceTotal(data[FindValueResponse.PIECE_TOTAL_AREA]);
        rpc.setValue(new BigInteger(1, tools.copyByteArray(data, FindValueResponse.VALUE_AREA, FindValueResponse.VALUE_LENGTH)));
        return rpc;
    }

    private void buildBasicInfo(byte[] data, RPC rpc) throws KadProtocolException {
        DataTools tools = ToolBox.getDataTools();
        byte[] senderNodeID = tools.copyByteArray(
            data, 
            KadProtocol.SENDER_ID_AREA,
            KadProtocol.NODE_ID_LENGTH
        ); 
        byte[] rpcID = tools.copyByteArray(
            data, 
            KadProtocol.RPC_ID_AREA,
            KadProtocol.RPC_ID_LENGTH
        );
        byte[] destinationID = tools.copyByteArray(
            data, 
            KadProtocol.DESTINATION_ID_AREA,
            KadProtocol.NODE_ID_LENGTH
        );
        
        rpc.setRPCID(new BigInteger(1, rpcID));
        rpc.setSenderNodeID(new BigInteger(1, senderNodeID));
        rpc.setDestinationNodeID(new BigInteger(1, destinationID));
    }
}

