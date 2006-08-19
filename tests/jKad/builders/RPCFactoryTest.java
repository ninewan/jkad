/*
 * Created on 20/03/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package jKad.builders;

import java.nio.ByteBuffer;

import jKad.builders.implementation.RPCFactoryImp;
import jKad.protocol.KadProtocol;
import jKad.protocol.KadProtocolException;
import jKad.protocol.rpc.RPC;
import jKad.protocol.rpc.request.*;
import jKad.protocol.rpc.response.*;
import junit.framework.ExtendedTestCase;

public class RPCFactoryTest extends ExtendedTestCase {

    private RPCFactory factory;
    
    public void setUp(){
        RPCFactory.setRPCFactoryClass(RPCFactoryImp.class);
        factory = RPCFactory.newInstance();
    }
    
	/*
	 * Test method for 'jKad.builders.RPCFactory.buildRPC(byte[])'
	 */
	public void testBuildStoreRPC() {
		RPC rpc;
		ByteBuffer data;
		
		data = ByteBuffer.allocate(StoreRPC.TOTAL_AREA_LENGTH);
		data.put(KadProtocol.STORE);
		data.put((byte)0);
		
		byte[] rpcID = this.buildData(KadProtocol.RPC_ID_LENGTH / 4, Integer.MAX_VALUE - 10);
		byte[] destinationID = this.buildData(KadProtocol.NODE_ID_LENGTH / 4, Integer.MAX_VALUE - 20);
		byte[] senderID = this.buildData(KadProtocol.NODE_ID_LENGTH / 4, Integer.MAX_VALUE - 30);
		byte piece = Byte.MAX_VALUE - 40;
		byte pieceTotal = Byte.MAX_VALUE - 50;
		byte[] key = this.buildData(KadProtocol.KEY_LENGTH / 4, Integer.MAX_VALUE - 60);
		byte[] value = this.buildData(KadProtocol.VALUE_LENGTH / 4, Integer.MAX_VALUE - 70);
		this.buildBasicInfo(data, rpcID, senderID, destinationID);
		data.position(StoreRPC.PIECE_AREA);
		data.put(piece);
		data.position(StoreRPC.PIECE_TOTAL_AREA);
		data.put(pieceTotal);
		data.position(StoreRPC.KEY_AREA);
		data.put(key);
		data.position(StoreRPC.VALUE_AREA);
		data.put(value);
		try {
			StoreRPC storeRPC = (StoreRPC)factory.buildRPC(data.array());
			assertEquals(storeRPC.getClass().getName(), StoreRPC.class.getName());
			assertEquals(rpcID, storeRPC.getRPCID().toByteArray());
			assertEquals(destinationID, storeRPC.getDestinationNodeID().toByteArray());
			assertEquals(senderID, storeRPC.getSenderNodeID().toByteArray());
			assertEquals(piece, storeRPC.getPiece());
			assertEquals(pieceTotal, storeRPC.getPieceTotal());
			assertEquals(key, storeRPC.getKey().toByteArray());
			assertEquals(value, storeRPC.getValue().toByteArray());
		} catch (KadProtocolException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/*
	 * Test method for 'jKad.builders.RPCFactory.buildRPC(byte[])'
	 */
	public void testBuildFindNodeRPC() {
		RPC rpc;
		ByteBuffer data;
		
		data = ByteBuffer.allocate(FindNodeRPC.TOTAL_AREA_LENGTH);
		data.put(KadProtocol.FIND_NODE);
		data.put((byte)0);
		
		byte[] rpcID = this.buildData(KadProtocol.RPC_ID_LENGTH / 4, Integer.MAX_VALUE - 10);
		byte[] destinationID = this.buildData(KadProtocol.NODE_ID_LENGTH / 4, Integer.MAX_VALUE - 20);
		byte[] senderID = this.buildData(KadProtocol.NODE_ID_LENGTH / 4, Integer.MAX_VALUE - 30);
		byte[] searchedID = this.buildData(KadProtocol.NODE_ID_LENGTH / 4, Integer.MAX_VALUE - 40);
		this.buildBasicInfo(data, rpcID, senderID, destinationID);
		data.position(FindNodeRPC.SEARCHED_NODE_AREA);
		data.put(searchedID);
		try {
			FindNodeRPC findNodeRPC = (FindNodeRPC)factory.buildRPC(data.array());
			assertEquals(findNodeRPC.getClass().getName(), FindNodeRPC.class.getName());
			assertEquals(rpcID, findNodeRPC.getRPCID().toByteArray());
			assertEquals(destinationID, findNodeRPC.getDestinationNodeID().toByteArray());
			assertEquals(senderID, findNodeRPC.getSenderNodeID().toByteArray());
			assertEquals(searchedID, findNodeRPC.getSearchedNodeID().toByteArray());
		} catch (KadProtocolException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/*
	 * Test method for 'jKad.builders.RPCFactory.buildRPC(byte[])'
	 */
	public void testBuildFindValueRPC() {
		RPC rpc;
		ByteBuffer data;
		
		data = ByteBuffer.allocate(FindValueRPC.TOTAL_AREA_LENGTH);
		data.put(KadProtocol.FIND_VALUE);
		data.put((byte)0);
		
		byte[] rpcID = this.buildData(KadProtocol.RPC_ID_LENGTH / 4, Integer.MAX_VALUE - 10);
		byte[] destinationID = this.buildData(KadProtocol.NODE_ID_LENGTH / 4, Integer.MAX_VALUE - 20);
		byte[] senderID = this.buildData(KadProtocol.NODE_ID_LENGTH / 4, Integer.MAX_VALUE - 30);
		byte[] searchedKey = this.buildData(KadProtocol.KEY_LENGTH / 4, Integer.MAX_VALUE - 40);
		this.buildBasicInfo(data, rpcID, senderID, destinationID);
		data.position(FindValueRPC.KEY_AREA);
		data.put(searchedKey);
		try {
			FindValueRPC findValueRPC = (FindValueRPC)factory.buildRPC(data.array());
			assertEquals(findValueRPC.getClass().getName(), FindValueRPC.class.getName());
			assertEquals(rpcID, findValueRPC.getRPCID().toByteArray());
			assertEquals(destinationID, findValueRPC.getDestinationNodeID().toByteArray());
			assertEquals(senderID, findValueRPC.getSenderNodeID().toByteArray());
			assertEquals(searchedKey, findValueRPC.getKey().toByteArray());
		} catch (KadProtocolException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/*
	 * Test method for 'jKad.builders.RPCFactory.buildRPC(byte[])'
	 */
	public void testBuildPingRPC() {
		RPC rpc;
		ByteBuffer data;
		
		data = ByteBuffer.allocate(PingRPC.TOTAL_AREA_LENGTH);
		data.put(KadProtocol.PING);
		data.put((byte)0);
		
		byte[] rpcID = this.buildData(KadProtocol.RPC_ID_LENGTH / 4, Integer.MAX_VALUE - 10);
		byte[] destinationID = this.buildData(KadProtocol.NODE_ID_LENGTH / 4, Integer.MAX_VALUE - 20);
		byte[] senderID = this.buildData(KadProtocol.NODE_ID_LENGTH / 4, Integer.MAX_VALUE - 30);
		byte[] searchedKey = this.buildData(KadProtocol.KEY_LENGTH / 4, Integer.MAX_VALUE - 40);
		this.buildBasicInfo(data, rpcID, senderID, destinationID);
		try {
			PingRPC pingRPC = (PingRPC)factory.buildRPC(data.array());
			assertEquals(pingRPC.getClass().getName(), PingRPC.class.getName());
			assertEquals(rpcID, pingRPC.getRPCID().toByteArray());
			assertEquals(destinationID, pingRPC.getDestinationNodeID().toByteArray());
			assertEquals(senderID, pingRPC.getSenderNodeID().toByteArray());
		} catch (KadProtocolException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/*
	 * Test method for 'jKad.builders.RPCFactory.buildRPC(byte[])'
	 */
	public void testBuildStoreResponse() {
		RPC rpc;
		ByteBuffer data;
		
		data = ByteBuffer.allocate(StoreResponse.TOTAL_AREA_LENGTH);
		data.put(KadProtocol.STORE);
		data.put((byte)1);
		
		byte[] rpcID = this.buildData(KadProtocol.RPC_ID_LENGTH / 4, Integer.MAX_VALUE - 10);
		byte[] destinationID = this.buildData(KadProtocol.NODE_ID_LENGTH / 4, Integer.MAX_VALUE - 20);
		byte[] senderID = this.buildData(KadProtocol.NODE_ID_LENGTH / 4, Integer.MAX_VALUE - 30);
		this.buildBasicInfo(data, rpcID, senderID, destinationID);
		try {
			StoreResponse storeResponse = (StoreResponse)factory.buildRPC(data.array());
			assertEquals(storeResponse.getClass().getName(), StoreResponse.class.getName());
			assertEquals(rpcID, storeResponse.getRPCID().toByteArray());
			assertEquals(destinationID, storeResponse.getDestinationNodeID().toByteArray());
			assertEquals(senderID, storeResponse.getSenderNodeID().toByteArray());
		} catch (KadProtocolException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/*
	 * Test method for 'jKad.builders.RPCFactory.buildRPC(byte[])'
	 */
	public void testBuildFindNodeResponse() {
		RPC rpc;
		ByteBuffer data;
		
		data = ByteBuffer.allocate(FindNodeResponse.TOTAL_AREA_LENGTH);
		data.put(KadProtocol.FIND_NODE);
		data.put((byte)1);
		
		byte[] rpcID = this.buildData(KadProtocol.RPC_ID_LENGTH / 4, Integer.MAX_VALUE - 10);
		byte[] destinationID = this.buildData(KadProtocol.NODE_ID_LENGTH / 4, Integer.MAX_VALUE - 20);
		byte[] senderID = this.buildData(KadProtocol.NODE_ID_LENGTH / 4, Integer.MAX_VALUE - 30);
		byte[] ip = this.buildData(FindNodeResponse.IP_ADDRESS_LENGTH / 4, Integer.MAX_VALUE - 40);
		byte[] port = this.buildData(FindNodeResponse.PORT_LENGTH / 4, Integer.MAX_VALUE - 50);
		this.buildBasicInfo(data, rpcID, senderID, destinationID);
		data.position(FindNodeResponse.IP_AREA);
		data.put(ip);
		data.position(FindNodeResponse.PORT_AREA);
		data.put(port);
		try {
			FindNodeResponse findNodeResponse = (FindNodeResponse)factory.buildRPC(data.array());
			assertEquals(findNodeResponse.getClass().getName(), FindNodeResponse.class.getName());
			assertEquals(rpcID, findNodeResponse.getRPCID().toByteArray());
			assertEquals(destinationID, findNodeResponse.getDestinationNodeID().toByteArray());
			assertEquals(senderID, findNodeResponse.getSenderNodeID().toByteArray());
			assertEquals(ip, findNodeResponse.getIpAddress().toByteArray());
			assertEquals(port, findNodeResponse.getPort().toByteArray());
		} catch (KadProtocolException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/*
	 * Test method for 'jKad.builders.RPCFactory.buildRPC(byte[])'
	 */
	public void testBuildFindValueResponse() {
		RPC rpc;
		ByteBuffer data;
		
		data = ByteBuffer.allocate(FindValueResponse.TOTAL_AREA_LENGTH);
		data.put(KadProtocol.FIND_VALUE);
		data.put((byte)1);
		
		byte[] rpcID = this.buildData(KadProtocol.RPC_ID_LENGTH / 4, Integer.MAX_VALUE - 10);
		byte[] destinationID = this.buildData(KadProtocol.NODE_ID_LENGTH / 4, Integer.MAX_VALUE - 20);
		byte[] senderID = this.buildData(KadProtocol.NODE_ID_LENGTH / 4, Integer.MAX_VALUE - 30);
		byte piece = Byte.MAX_VALUE - 40;
		byte pieceTotal = Byte.MAX_VALUE - 50;
		byte[] value = this.buildData(KadProtocol.VALUE_LENGTH / 4, Integer.MAX_VALUE - 70);
		this.buildBasicInfo(data, rpcID, senderID, destinationID);
		data.position(FindValueResponse.PIECE_AREA);
		data.put(piece);
		data.position(FindValueResponse.PIECE_TOTAL_AREA);
		data.put(pieceTotal);
		data.position(FindValueResponse.VALUE_AREA);
		data.put(value);
		try {
			FindValueResponse findValueResponse = (FindValueResponse)factory.buildRPC(data.array());
			assertEquals(findValueResponse.getClass().getName(), FindValueResponse.class.getName());
			assertEquals(rpcID, findValueResponse.getRPCID().toByteArray());
			assertEquals(destinationID, findValueResponse.getDestinationNodeID().toByteArray());
			assertEquals(senderID, findValueResponse.getSenderNodeID().toByteArray());
			assertEquals(piece, findValueResponse.getPiece());
			assertEquals(pieceTotal, findValueResponse.getPieceTotal());
			assertEquals(value, findValueResponse.getValue().toByteArray());
		} catch (KadProtocolException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/*
	 * Test method for 'jKad.builders.RPCFactory.buildRPC(byte[])'
	 */
	public void testBuildPingResponse() {
		RPC rpc;
		ByteBuffer data;
		
		data = ByteBuffer.allocate(PingResponse.TOTAL_AREA_LENGTH);
		data.put(KadProtocol.PING);
		data.put((byte)1);
		
		byte[] rpcID = this.buildData(KadProtocol.RPC_ID_LENGTH / 4, Integer.MAX_VALUE - 10);
		byte[] destinationID = this.buildData(KadProtocol.NODE_ID_LENGTH / 4, Integer.MAX_VALUE - 20);
		byte[] senderID = this.buildData(KadProtocol.NODE_ID_LENGTH / 4, Integer.MAX_VALUE - 30);
		this.buildBasicInfo(data, rpcID, senderID, destinationID);
		try {
			PingResponse pingResponse = (PingResponse)factory.buildRPC(data.array());
			assertEquals(pingResponse.getClass().getName(), PingResponse.class.getName());
			assertEquals(rpcID, pingResponse.getRPCID().toByteArray());
			assertEquals(destinationID, pingResponse.getDestinationNodeID().toByteArray());
			assertEquals(senderID, pingResponse.getSenderNodeID().toByteArray());
		} catch (KadProtocolException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	private byte[] buildData(int times, int value){
		ByteBuffer result = ByteBuffer.allocate(times*4);
		for(int i = 0; i < times; i++) result.putInt(value);
		return result.array();
	}
	
	private void buildBasicInfo(ByteBuffer data, byte[] rpcID, byte[] senderID, byte[] destinationID){
		data.position(KadProtocol.RPC_ID_AREA);
		data.put(rpcID);
		data.position(KadProtocol.DESTINATION_ID_AREA);
		data.put(destinationID);
		data.position(KadProtocol.SENDER_ID_AREA);
		data.put(senderID);
	}
}
