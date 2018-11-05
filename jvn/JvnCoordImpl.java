/***
 * JAVANAISE Implementation
 * JvnServerImpl class
 * Contact: 
 *
 * Authors: 
 */

package jvn;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.server.UnicastRemoteObject;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.rmi.UnmarshalException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import test.coordMain;


public class JvnCoordImpl extends UnicastRemoteObject implements JvnRemoteCoord, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private HashMap<String, Integer> jvnObjectIds;
    private HashMap<Integer, JvnObject> jvnObjects;
    private HashMap<Integer, JvnRemoteServer> objectIdsLastVersionOwner;
    private HashMap<Integer, ArrayList<JvnRemoteServer>> readLocks;
    
    private int counterObjectId;

    /**
    * Default constructor
    * @throws JvnException
    **/
    public JvnCoordImpl() throws RemoteException, IOException{

        FileInputStream fin = null;
        ObjectInputStream ois = null;

        try {

        fin = new FileInputStream("coord.ser");
        ois = new ObjectInputStream(fin);
        JvnCoordImpl coord = (JvnCoordImpl) ois.readObject();
        
        this.jvnObjectIds = coord.jvnObjectIds;
        this.jvnObjects = coord.jvnObjects;
        this.objectIdsLastVersionOwner = coord.objectIdsLastVersionOwner;
        this.readLocks = coord.readLocks;
        this.counterObjectId = coord.counterObjectId;
        
        if(coordMain.printDebug) System.out.println("Coordinateur chargé");
        
        } catch(FileNotFoundException e){            
            this.jvnObjectIds = new HashMap<>();
            this.jvnObjects = new HashMap<>();
            this.objectIdsLastVersionOwner = new HashMap<>();
            this.readLocks = new HashMap<>();

            this.counterObjectId = 0;            
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            if(fin != null) fin.close();
            if(ois != null) ois.close();
        }
    }
    
    
    /**
    *  Allocate a NEW JVN object id (usually allocated to a 
    *  newly created JVN object)
    * @throws java.rmi.RemoteException,JvnException
    **/

    public synchronized int jvnGetObjectId() throws java.rmi.RemoteException, JvnException {
      return this.counterObjectId++;
    }
  
    /**
    * Associate a symbolic name with a JVN object
    * @param jon : the JVN object name
    * @param jo  : the JVN object 
    * @param joi : the JVN object identification
    * @param js  : the remote reference of the JVNServer
    * @throws java.rmi.RemoteException,JvnException
    **/
    public synchronized void jvnRegisterObject(String jon, JvnObject jo, JvnRemoteServer js) throws java.rmi.RemoteException, JvnException{
        int id = jo.jvnGetObjectId();
        this.jvnObjectIds.put(jon, id);
        ((JvnObjectImpl) jo).setLock(Lock.NL);
        this.jvnObjects.put(id, jo);
        this.objectIdsLastVersionOwner.put(id, js);
        this.readLocks.put(id, new ArrayList<>());
        this.readLocks.get(id).add(js);
        
        this.saveCoord();
    }
  
  /**
  * Get the reference of a JVN object managed by a given JVN server 
  * @param jon : the JVN object name
  * @param js : the remote reference of the JVNServer
  * @throws java.rmi.RemoteException,JvnException
  **/

    public synchronized JvnObject jvnLookupObject(String jon, JvnRemoteServer js) throws java.rmi.RemoteException, JvnException{   
        if(!this.jvnObjectIds.containsKey(jon)) return null;
        return this.jvnObjects.get(this.jvnObjectIds.get(jon));
    }
  
  /**
  * Get a Read lock on a JVN object managed by a given JVN server 
  * @param joi : the JVN object identification
  * @param js  : the remote reference of the server
  * @return the current JVN object state
  * @throws java.rmi.RemoteException, JvnException
  * @throws jvn.JvnException
  **/
    public synchronized Serializable jvnLockRead(int joi, JvnRemoteServer js) throws java.rmi.RemoteException, JvnException{
        // to be completed
        if(coordMain.printDebug) System.out.println("jvn.JvnCoordImpl.jvnLockRead()");
        Serializable state = null;
        try {
            if(this.objectIdsLastVersionOwner.get(joi) == null){
                state = this.jvnObjects.get(joi).jvnGetObjectState();

            } else {
                state = this.objectIdsLastVersionOwner.get(joi).jvnInvalidateWriterForReader(joi);
                this.readLocks.get(joi).add(this.objectIdsLastVersionOwner.get(joi));
                this.objectIdsLastVersionOwner.put(joi, null);
                ((JvnObjectImpl)this.jvnObjects.get(joi)).setState(state);
            }

            this.readLocks.get(joi).add(js);
        } catch (RemoteException ex){
            System.out.println("Hôte perdu");
            this.objectIdsLastVersionOwner.put(joi, null);
            this.readLocks.get(joi).add(js);
            state = this.jvnObjects.get(joi).jvnGetObjectState();
        }
        
        this.saveCoord();
        if(coordMain.printDebug) System.out.println("jvn.JvnCoordImpl.jvnLockRead() Done");
        return state;
    }

  /**
  * Get a Write lock on a JVN object managed by a given JVN server 
  * @param joi : the JVN object identification
  * @param js  : the remote reference of the server
  * @return the current JVN object state
  * @throws java.rmi.RemoteException, JvnException
  **/
   public synchronized Serializable jvnLockWrite(int joi, JvnRemoteServer js) throws java.rmi.RemoteException, JvnException{
        if(coordMain.printDebug) System.out.println("Coord - LockWrite");
    // to be completed
        Serializable state = null;
        
            if(this.objectIdsLastVersionOwner.get(joi) == null){
                state = this.jvnObjects.get(joi).jvnGetObjectState();
            } else {
                try{
                    state = this.objectIdsLastVersionOwner.get(joi).jvnInvalidateWriter(joi);
                    ((JvnObjectImpl)this.jvnObjects.get(joi)).setState(state);                    
                } catch (RemoteException ex){
                    state = this.jvnObjects.get(joi).jvnGetObjectState();
                }


            }
            this.objectIdsLastVersionOwner.put(joi, js);
            if(coordMain.printDebug) System.out.println("InvalidateWriter done");
            
            for(JvnRemoteServer jvnServer : this.readLocks.get(joi)){
                if(! jvnServer.equals(js)){
                    try{
                        if(coordMain.printDebug) System.out.println("InvalidateReader : " + jvnServer.toString());
                        jvnServer.jvnInvalidateReader(joi);
                        if(coordMain.printDebug) System.out.println("InvalidateReader Done");
                    } catch (RemoteException ex){}
                }
            }
            this.readLocks.get(joi).clear();
            if(coordMain.printDebug) System.out.println("Coord - LockWrite - Done");
        
        this.saveCoord();
        return state;
   }

	/**
	* A JVN server terminates
	* @param js  : the remote reference of the server
	* @throws java.rmi.RemoteException, JvnException
	**/
    public synchronized void jvnTerminate(JvnRemoteServer js) throws java.rmi.RemoteException, JvnException {
	 // to be completed
        if(coordMain.printDebug) System.out.println("Coor - terminate");
        Serializable state;
        for(Map.Entry mapentry : this.objectIdsLastVersionOwner.entrySet()){
            if( (mapentry.getValue() != null) && (((JvnRemoteServer)mapentry.getValue()).equals(js)) ){
               state = ((JvnRemoteServer)mapentry.getValue()).jvnInvalidateWriter((Integer) mapentry.getKey());
               ((JvnObjectImpl)this.jvnObjects.get((Integer) mapentry.getKey())).setState(state);
               this.objectIdsLastVersionOwner.put((Integer) mapentry.getKey(), null);
            }
        }
        
        this.saveCoord();
    }
    
    private synchronized void saveCoord(){
        FileOutputStream fout = null;
	ObjectOutputStream oos = null;
        
        try {
            fout = new FileOutputStream("coord.ser");
            oos = new ObjectOutputStream(fout);
            oos.writeObject(this);
        } catch (Exception ex) {
            ex.printStackTrace();

	} finally {

            if (fout != null) {
                try {
                    fout.close();
		} catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            if (oos != null) {
		try {
                    oos.close();
		} catch (IOException ex) {
                    ex.printStackTrace();
		}
            }
        }
            
    } 
    
    
    
}

 
