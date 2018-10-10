/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import static java.lang.System.exit;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
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
        
//        JvnRemoteCoord remoteCoord = (JvnRemoteCoord) UnicastRemoteObject.exportObject(coord, 0);
        
        Registry registry = LocateRegistry.getRegistry();
        registry.rebind("coord_service", coord);
        
        
    }
    
}
