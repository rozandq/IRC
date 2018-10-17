/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jvn;
import java.lang.annotation.*; 

// Is the annotation available at execution time 
@Retention(RetentionPolicy.RUNTIME)  
// Annotation associated with a type (Classe, interface)
@Target(ElementType.METHOD)  
public @interface JvnAnnotation { 
    public enum JvnAnnotationType {READ, WRITE}
    
    JvnAnnotationType type() default JvnAnnotationType.WRITE;  
}

