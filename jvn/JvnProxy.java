/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jvn;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import test.MethodType;

/**
 *
 * @author mariobap
 */
public class JvnProxy implements InvocationHandler {
    private Object obj;
    private JvnObject jvnObject;
    
    private JvnProxy(Object obj, JvnObject jo){
        this.obj= obj;
        this.jvnObject = jo;
    } 
    
    public static Object newInstance(Object obj) { 
        return java.lang.reflect.Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), new JvnProxy(obj, JvnServerImpl.jvnGetServer().jvnCreateObject(obj))); 
    } 
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) 
      throws Throwable {
        System.out.println("Invoked method: " + method.getName());
        
        MethodType methodType = method.getAnnotation(MethodType.class);
        System.out.println("Method type: " + methodType.name());
 
        return "test";
    }
}
