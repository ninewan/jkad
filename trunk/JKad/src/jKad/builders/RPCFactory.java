package jKad.builders;

import jKad.builders.implementation.RPCFactoryImp;
import jKad.protocol.KadProtocolException;
import jKad.protocol.rpc.RPC;

public abstract class RPCFactory {
    private static Class<? extends RPCFactory> instanceClass = RPCFactoryImp.class;

    public static RPCFactory newInstance() {
        RPCFactory instance = null;
        try {
            instance = instanceClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return instance;
    }

    public static final void setRPCFactoryClass(String className) throws ClassNotFoundException{
        RPCFactory.setRPCFactoryClass((Class<? extends RPCFactory>)Class.forName(className));
    }    
    
    public static final void setRPCFactoryClass(Class<? extends RPCFactory> clazz) {
        instanceClass = clazz;
    }

    public abstract RPC buildRPC(byte[] data) throws KadProtocolException;
}
