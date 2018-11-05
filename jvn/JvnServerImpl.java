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
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.UnmarshalException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.Keymap;
import test.coordMain;



public class JvnServerImpl extends UnicastRemoteObject implements JvnLocalServer, JvnRemoteServer{
    private static HashMap<Integer, JvnObject> jvnObjects;
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
            
            this.connectToCoord();
            
            if(coordMain.printDebug) System.out.println("Server : " + this);
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
            for(Map.Entry entry : this.jvnObjects.entrySet()){
                JvnObjectImpl obj = (JvnObjectImpl) entry.getValue();
                if(obj.getLock() == Lock.R || obj.getLock() == Lock.RWC || obj.getLock() == Lock.W){
                    obj.jvnUnLock();
                    
                }
            }
//            this.getCoord().jvnTerminate(this.js);
            this.coord.jvnTerminate(js);
        } catch (UnmarshalException|ConnectException ex) {
            this.connectToCoord();
            this.jvnTerminate();
        } catch (Exception ex) {
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
//            return new JvnObjectImpl(this.getCoord().jvnGetObjectId(), o);
            return new JvnObjectImpl(this.coord.jvnGetObjectId(), o);
        } catch (UnmarshalException|ConnectException ex) {
            this.connectToCoord();
            return this.jvnCreateObject(o);
        } catch (Exception ex) {
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
//            this.getCoord().jvnRegisterObject(jon, jo, this.js);
            this.coord.jvnRegisterObject(jon, jo, this.js);
        } catch (UnmarshalException|ConnectException ex) {
            this.connectToCoord();
            this.jvnRegisterObject(jon, jo);
        } catch (Exception ex) {
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
//            JvnObject jo = this.getCoord().jvnLookupObject(jon, this.js);
            JvnObject jo = this.coord.jvnLookupObject(jon, this.js);
            if (jo != null) this.jvnObjects.put(jo.jvnGetObjectId(), jo);
            return jo;
        } catch (UnmarshalException|ConnectException ex) {
            this.connectToCoord();
            return this.jvnLookupObject(jon);
        } catch (Exception ex) {
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
            if(coordMain.printDebug) System.out.println("jvn.JvnServerImpl.jvnLockRead()");
            // to be completed
//            Serializable s = this.getCoord().jvnLockRead(joi, this.js);
            Serializable s = this.coord.jvnLockRead(joi, this.js);
            if(coordMain.printDebug) System.out.println("jvn.JvnServerImpl.jvnLockRead() Done");
            return s;
            
            
            
        } catch (UnmarshalException|ConnectException ex) {
            this.connectToCoord();
            return this.jvnLockRead(joi);
        } catch (Exception ex) {
            System.out.println("Erreur JvnServeurImpl - jvnLockRead : " + ex);
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
        if(coordMain.printDebug) System.out.println("Server - LockWrite");
           try {
            // to be completed
//            Serializable state = this.getCoord().jvnLockWrite(joi, this.js);
            Serializable state = this.coord.jvnLockWrite(joi, this.js);
            if(coordMain.printDebug) System.out.println("Server - LockWrite - Done");
            
            return state;
            
        } catch (UnmarshalException|ConnectException ex) {
            this.connectToCoord();
            return this.jvnLockWrite(joi);
        } catch (Exception ex) {
            System.out.println("Erreur JvnServeurImpl - jvnLockWrite : " + ex);
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
        if(coordMain.printDebug) System.out.println("jvn.JvnServerImpl.jvnInvalidateReader()");
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
            if(coordMain.printDebug) System.out.println("jvn.JvnServerImpl.jvnInvalidateWriter()");
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
           if(coordMain.printDebug) System.out.println("jvn.JvnServerImpl.jvnInvalidateWriterForReader()");
           return this.jvnObjects.get(joi).jvnInvalidateWriterForReader();
    };
    
    
    private void connectToCoord(){
        if(coordMain.printDebug) System.out.println("jvn.JvnServerImpl.connectToCoord()");
        boolean connected = false;
        boolean printed = false;
        while(!connected){
            try {
                Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1333); 
                coord = (JvnRemoteCoord) registry.lookup("coord_service");
                connected = true;
                if(coordMain.printDebug) System.out.println("jvn.JvnServerImpl.connectToCoord() ok ");
            } catch (ConnectException ex) {
                if(!printed){
                    System.out.println("Connexion en cours...");
                    printed = true;
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex1) {
                    ex1.printStackTrace();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        if(coordMain.printDebug) System.out.println("jvn.JvnServerImpl.connectToCoord() done");
    }
}

 
