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
    
    private JvnProxy(Object obj, Serializable state) throws JvnException{
        this.jvnObject = JvnServerImpl.jvnGetServer().jvnCreateObject(state);
        this.jvnObject.jvnUnLock();
        JvnServerImpl.jvnGetServer().jvnRegisterObject("IRC", this.jvnObject);
    } 
    
    public static Object newInstance(Serializable state) throws JvnException { 
        return java.lang.reflect.Proxy.newProxyInstance(state.getClass().getClassLoader(), state.getClass().getInterfaces(), new JvnProxy(state, JvnServerImpl.jvnGetServer().jvnCreateObject(state))); 
    } 
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) 
      throws Throwable {
        
//        System.out.println("Invoked method: " + method.getName());
//        
//        JvnAnnotation methodType = method.getAnnotation(JvnAnnotation.class);
//        System.out.println("Method type: " + methodType.type());
// 
//        return "test";
        
        JvnAnnotation methodType = method.getAnnotation(JvnAnnotation.class);
        if(methodType.type() == JvnAnnotation.JvnAnnotationType.READ){
            this.jvnObject.jvnLockRead();
        }
        else {
            this.jvnObject.jvnLockWrite();
        }
        
        Object obj = method.invoke(proxy, args);
        
        this.jvnObject.jvnUnLock();
        
        return obj;
    }
}
