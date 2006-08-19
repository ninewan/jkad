package jKad.tools;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionTools {
    protected ReflectionTools(){}
    
    public int getFieldValue(String fieldName, Object object) throws NoSuchFieldException{
        try {
            Class clazz = object.getClass();
            Field field = clazz.getField(fieldName);
            return field.getInt(field);
        } catch (SecurityException e){
            e.printStackTrace();
        } catch (IllegalAccessException e){
            e.printStackTrace();
        }
        return -1;
    }
    
    public void invokeSetter(String fieldName, Object value, Object object) throws NoSuchMethodException{
        try {
            this.invokeMethod("set", fieldName, new Object[]{value}, object);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    
    public Object invokeGetter(String fieldName, Object value, Object object) throws NoSuchMethodException{
        try {
            return this.invokeMethod("get", fieldName, new Object[]{value}, object);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Object invokeMethod(String prefix, String fieldName, Object[] parameters, Object object) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
        Class clazz = object.getClass();
        String capital = fieldName.substring(0,1).toUpperCase();
        String termination = fieldName.substring(1, fieldName.length());
        StringBuffer methodName = new StringBuffer(prefix);
        methodName.append(capital);
        methodName.append(termination);
        Class[] parametersClasses = new Class[parameters.length];
        for(int i = 0; i < parametersClasses.length; i++){
            parametersClasses[i] = parameters[i].getClass();
        }
        Method method = clazz.getMethod(methodName.toString(), parametersClasses);
        return method.invoke(object, parameters);
    }
}
