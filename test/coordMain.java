/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import jvn.*;

/**
 *
 * @author mariobap
 */
public class coordMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        JvnCoordImpl coord = new JvnCoordImpl();
        
        Registry registry = LocateRegistry.createRegistry(1333);
        registry.bind("coord_service", coord);
        
        System.out.println("Coordinator ready !");
    }
    
}
