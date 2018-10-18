/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jvn;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 *
 * @author mariobap
 */
public class JvnProxy implements InvocationHandler {
    private JvnObject jvnObject;
    
    private JvnProxy(Serializable state) throws JvnException{
        this.jvnObject = JvnServerImpl.jvnGetServer().jvnCreateObject(state);
        this.jvnObject.jvnUnLock();
        JvnServerImpl.jvnGetServer().jvnRegisterObject("IRC", this.jvnObject);
    }
    
    private JvnProxy(JvnObject jvnObj) throws JvnException{
        this.jvnObject = jvnObj;
    } 
    
    public static Object newInstance(Serializable state) throws JvnException {
        return java.lang.reflect.Proxy.newProxyInstance(state.getClass().getClassLoader(), state.getClass().getInterfaces(), new JvnProxy(state)); 
    } 
    
    public static Object newInstance(String name) throws JvnException{
        JvnObject jvnObj = JvnServerImpl.jvnGetServer().jvnLookupObject(name);
        if(jvnObj == null) return null;
        return java.lang.reflect.Proxy.newProxyInstance(jvnObj.jvnGetObjectState().getClass().getClassLoader(), jvnObj.jvnGetObjectState().getClass().getInterfaces(), new JvnProxy(jvnObj));
    }
    
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) 
      throws Throwable {
        
        System.out.println("Methode : " + method.getName());
        
        
        
        JvnAnnotation methodType = method.getAnnotation(JvnAnnotation.class);
        if(methodType.type() == JvnAnnotation.JvnAnnotationType.READ){
            this.jvnObject.jvnLockRead();
        }
        else if (methodType.type() == JvnAnnotation.JvnAnnotationType.WRITE) {
            this.jvnObject.jvnLockWrite();
        }
        
        Object obj = method.invoke(this.jvnObject.jvnGetObjectState(), args);
                
        this.jvnObject.jvnUnLock();
        
        return obj;
    }
}
