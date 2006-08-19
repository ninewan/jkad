package jKad.builders;

import jKad.builders.implementation.InputBuilderImp;
import jKad.protocol.KadProtocolException;
import jKad.protocol.rpc.RPC;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;

public abstract class InputBuilder {

    private static Class<? extends InputBuilder> instanceClass = InputBuilderImp.class;
    
    public static InputBuilder newInstance() {
        InputBuilder instance = null;
        try {
            instance = instanceClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return instance;
    }

    public static final void setInputBuilderClass(String className) throws ClassNotFoundException {
        InputBuilder.setInputBuilderClass((Class<? extends InputBuilder>) Class.forName(className));
    }

    public static final void setInputBuilderClass(Class<? extends InputBuilder> clazz) {
        instanceClass = clazz;
    }

    public static final Class<? extends InputBuilder> getOutputBuilderClass() {
        return instanceClass;
    }
    
    public abstract RPC buildRPC(Byte[] data) throws KadProtocolException;

    public abstract RPC buildRPC(byte[] data) throws KadProtocolException;

    public abstract RPC buildRPC(ByteBuffer data) throws KadProtocolException;

    public abstract RPC buildRPC(DatagramPacket packet) throws KadProtocolException;
}
