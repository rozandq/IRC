/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

/**
 *
 * @author mariobap
 */
public interface TestProxyInterface {
    @MethodType (name = "get")
    public String getText(); 
    @MethodType (name = "set")
    public void setText(String text); 
}
