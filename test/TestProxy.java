/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import jvn.JvnProxy;

/**
 *
 * @author mariobap
 */
public class TestProxy implements TestProxyInterface {
    String text;
    
    public TestProxy(String text){
        this.text = text;
    }

    @Override
    public String getText() {
        return this.text;
    }
    
    @Override
    public void setText(String text) {
        this.text = text;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TestProxyInterface test = (TestProxyInterface) JvnProxy.newInstance(new TestProxy("Hello World!"));
        
        test.getText();
        test.setText("coucou");
    }
}
