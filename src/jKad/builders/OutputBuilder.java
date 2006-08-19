package jKad.builders;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import jKad.builders.implementation.OutputBuilderImp;
import jKad.protocol.rpc.RPC;

public abstract class OutputBuilder {
	
    private static Class<? extends OutputBuilder> instanceClass = OutputBuilderImp.class;
    
    public static OutputBuilder newInstance() {
        OutputBuilder instance = null;
        try {
            instance = instanceClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return instance;
    }
    
    public static final void setOutputBuilderClass(String className) throws ClassNotFoundException{
        OutputBuilder.setOutputBuilderClass((Class<? extends OutputBuilder>)Class.forName(className));
    }
    
    public static final void setOutputBuilderClass(Class<? extends OutputBuilder> clazz){
        instanceClass = clazz; 
    }
    
    public static final Class<? extends OutputBuilder> getOutputBuilderClass(){
        return instanceClass;
    }
    
    public abstract ByteBuffer buildData(RPC rpc);
    
    public abstract DatagramPacket buildPacket(RPC rpc, String ip, int port) throws UnknownHostException;
    
    public abstract DatagramPacket buildPacket(RPC rpc, InetAddress ip, int port);
    
}
