/***
 * JAVANAISE Implementation
 * JvnServerImpl class
 * Contact: 
 *
 * Authors: 
 */

package jvn;

import java.rmi.server.UnicastRemoteObject;
import java.io.*;
import static java.lang.System.exit;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;



public class JvnServerImpl extends UnicastRemoteObject implements JvnLocalServer, JvnRemoteServer{
    private static HashMap<Integer, JvnObject> jvnObjects;
    private static HashMap<Integer, String> jvnObjectNames;
    private static JvnRemoteCoord coord;
    
    // A JVN server is managed as a singleton 
    private static JvnServerImpl js = null;

    /**
    * Default constructor
    * @throws JvnException
    **/
    private JvnServerImpl() throws Exception {
            super();
            
            
            this.jvnObjects = new HashMap<>();
            this.jvnObjectNames = new HashMap<>();
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1333); 
            coord = (JvnRemoteCoord) registry.lookup("coord_service");
    }
	
    /**
    * Static method allowing an application to get a reference to 
    * a JVN server instance
    * @throws JvnException
    **/
    public static JvnServerImpl jvnGetServer() {
        if (js == null){
                try {
                        js = new JvnServerImpl();
                } catch (Exception e) {
                		System.out.println("Error getServer");
                        return null;
                }
        }
        return js;
    } 

    /* public Integer getJvnObject(Inte) {
        return jvnObjects;
    } */
	
    /**
    * The JVN service is not used anymore
    * @throws JvnException
    **/
    public void jvnTerminate() throws jvn.JvnException {
        try {
            // to be completed
            this.coord.jvnTerminate(this.js);
        } catch (RemoteException ex) {
            throw new JvnException("Error JvnServerImpl - jvnTerminate : " + ex);
        }
    } 
	
    /**
    * creation of a JVN object
    * @param o : the JVN object state
    * @throws JvnException
    **/
    public JvnObject jvnCreateObject(Serializable o) throws jvn.JvnException { 
        try {
            // to be completed
            return new JvnObjectImpl(this.coord.jvnGetObjectId(), o);
        } catch (RemoteException ex) {
            Logger.getLogger(JvnServerImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new JvnException("Error JvnServerImpl - jvnCreateObject");
        }
    }

    /**
    *  Associate a symbolic name with a JVN object
    * @param jon : the JVN object name
    * @param jo : the JVN object 
    * @throws JvnException
    **/
    public void jvnRegisterObject(String jon, JvnObject jo) throws jvn.JvnException {
        try {
            // to be completed
        	this.jvnObjects.put(jo.jvnGetObjectId(), jo);
            this.coord.jvnRegisterObject(jon, jo, this.js);
        } catch (RemoteException ex) {
            Logger.getLogger(JvnServerImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new JvnException("Error JvnServerImpl - jvnRegisterObject");
        }
    }
	
    /**
    * Provide the reference of a JVN object beeing given its symbolic name
    * @param jon : the JVN object name
    * @return the JVN object 
    * @throws JvnException
    **/
    public JvnObject jvnLookupObject(String jon) throws jvn.JvnException {
        try {
        	JvnObject jo = this.coord.jvnLookupObject(jon, this.js);
        	if (jo != null) this.jvnObjects.put(jo.jvnGetObjectId(), jo);
            return jo;
        } catch (RemoteException ex) {
            Logger.getLogger(JvnServerImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new JvnException("Error JvnServerImpl - jvnLookupObject");
        }
    }	
	
    /**
    * Get a Read lock on a JVN object 
    * @param joi : the JVN object identification
    * @return the current JVN object state
    * @throws  JvnException
    **/
    public Serializable jvnLockRead(int joi) throws JvnException {
        try {
            // to be completed
            return this.coord.jvnLockRead(joi, this.js);
            
        } catch (RemoteException ex) {
            System.out.println("Erreur JvnServeurImpl - jvnLockRead : " + ex);
            exit(1);
            return null;
        }
    }
    
    /**
    * Get a Write lock on a JVN object 
    * @param joi : the JVN object identification
    * @return the current JVN object state
    * @throws  JvnException
    **/
    public Serializable jvnLockWrite(int joi) throws JvnException {
           try {
            // to be completed
            
            return this.coord.jvnLockWrite(joi, this.js);
            
        } catch (RemoteException ex) {
            System.out.println("Erreur JvnServeurImpl - jvnLockWrite : " + ex);
            exit(1);
            return null;
        }
    }	

	
    /**
    * Invalidate the Read lock of the JVN object identified by id 
    * called by the JvnCoord
    * @param joi : the JVN object id
    * @return void
    * @throws java.rmi.RemoteException,JvnException
    **/
    public synchronized void jvnInvalidateReader(int joi) throws java.rmi.RemoteException,jvn.JvnException {
            // to be completed 

        this.jvnObjects.get(joi).jvnInvalidateReader();
            
    };
	    
    /**
    * Invalidate the Write lock of the JVN object identified by id 
    * @param joi : the JVN object id
    * @return the current JVN object state
    * @throws java.rmi.RemoteException,JvnException
    **/
    public synchronized Serializable jvnInvalidateWriter(int joi) throws java.rmi.RemoteException,jvn.JvnException { 
            // to be completed 
            return this.jvnObjects.get(joi).jvnInvalidateWriter();
    };
	
    /**
    * Reduce the Write lock of the JVN object identified by id 
    * @param joi : the JVN object id
    * @return the current JVN object state
    * @throws java.rmi.RemoteException,JvnException
    **/
    public synchronized Serializable jvnInvalidateWriterForReader(int joi) throws java.rmi.RemoteException,jvn.JvnException { 
           // to be completed 
           return this.jvnObjects.get(joi).jvnInvalidateWriterForReader();
    };
}

 
