/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irc;

import jvn.JvnAnnotation;

/**
 *
 * @author Baptiste
 */
public interface SentenceInterface {
    
    @JvnAnnotation(type = JvnAnnotation.JvnAnnotationType.WRITE)
    public void write(String text);
    
    @JvnAnnotation(type = JvnAnnotation.JvnAnnotationType.READ)
    public String read();
}
