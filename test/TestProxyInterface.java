/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import jvn.JvnAnnotation;
import jvn.JvnAnnotation.JvnAnnotationType;

/**
 *
 * @author mariobap
 */
public interface TestProxyInterface {
    
    @JvnAnnotation(type = JvnAnnotationType.READ)
    public String getText();
    @JvnAnnotation(type = JvnAnnotationType.WRITE)
    public void setText(String text); 
}
