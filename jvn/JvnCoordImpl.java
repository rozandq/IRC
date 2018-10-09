/***
 * JAVANAISE Implementation
 * JvnServerImpl class
 * Contact: 
 *
 * Authors: 
 */

package jvn;

import java.rmi.server.UnicastRemoteObject;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class JvnCoordImpl extends UnicastRemoteObject implements JvnRemoteCoord{
    HashMap<Integer, String> jvnObjectIds;
    HashMap<Integer, JvnObject> jvnObjects;
    HashMap<Integer, JvnRemoteServer> objectIdsLastVersionOwner;
    HashMap<Integer, HashMap<JvnRemoteServer, Lock>> locks;
    HashMap<Integer, LinkedList<ServerWaiting>> waitingQueue;
    
    private int counterObjectId;

    /**
    * Default constructor
    * @throws JvnException
    **/
	private JvnCoordImpl() throws Exception {
		// to be completed
            this.jvnObjectIds = new HashMap<>();
            this.jvnObjects = new HashMap<>();
            this.objectIdsLastVersionOwner = new HashMap<>();
            this.locks = new HashMap<>();
            this.waitingQueue = new HashMap<>();
            
            this.counterObjectId = 0;
	}

    /**
    *  Allocate a NEW JVN object id (usually allocated to a 
    *  newly created JVN object)
    * @throws java.rmi.RemoteException,JvnException
    **/
    public int jvnGetObjectId() throws java.rmi.RemoteException,jvn.JvnException {
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
    public void jvnRegisterObject(String jon, JvnObject jo, JvnRemoteServer js) throws java.rmi.RemoteException,jvn.JvnException{
        int id = jo.jvnGetObjectId();
        this.jvnObjectIds.put(id, jon);
        this.jvnObjects.put(id, jo);
        this.objectIdsLastVersionOwner.put(id, js);
        this.locks.put(id, new HashMap<>());
        this.waitingQueue.put(id, new LinkedList<>());
    }
  
  /**
  * Get the reference of a JVN object managed by a given JVN server 
  * @param jon : the JVN object name
  * @param js : the remote reference of the JVNServer
  * @throws java.rmi.RemoteException,JvnException
  **/
  public JvnObject jvnLookupObject(String jon, JvnRemoteServer js) throws java.rmi.RemoteException,jvn.JvnException{          
    return null;
  }
  
  /**
  * Get a Read lock on a JVN object managed by a given JVN server 
  * @param joi : the JVN object identification
  * @param js  : the remote reference of the server
  * @return the current JVN object state
  * @throws java.rmi.RemoteException, JvnException
  **/
   public Serializable jvnLockRead(int joi, JvnRemoteServer js) throws java.rmi.RemoteException, JvnException{
    // to be completed
    Serializable state = null;
    for(Map.Entry mapentry : locks.get(joi).entrySet()){
        switch((Lock)mapentry.getValue()){
            case W:
                this.waitingQueue.get(joi).add(new ServerWaiting(js, Type.READ));
                try {
                    js.wait();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                break;
                
            case WC:
                mapentry.setValue(Lock.RC);
                state = ((JvnRemoteServer)mapentry.getKey()).jvnInvalidateWriterForReader(joi);
                break;
                
            case RWC:
                mapentry.setValue(Lock.R);
                state = ((JvnRemoteServer)mapentry.getKey()).jvnInvalidateWriterForReader(joi);
                break;
            
            default:
                break;
            
        }
    }
    
    this.locks.get(joi).put(js, Lock.R);
    return state;
   }

  /**
  * Get a Write lock on a JVN object managed by a given JVN server 
  * @param joi : the JVN object identification
  * @param js  : the remote reference of the server
  * @return the current JVN object state
  * @throws java.rmi.RemoteException, JvnException
  **/
   public Serializable jvnLockWrite(int joi, JvnRemoteServer js) throws java.rmi.RemoteException, JvnException{
    // to be completed
    return null;
   }

	/**
	* A JVN server terminates
	* @param js  : the remote reference of the server
	* @throws java.rmi.RemoteException, JvnException
	**/
    public void jvnTerminate(JvnRemoteServer js) throws java.rmi.RemoteException, JvnException {
	 // to be completed
    }
}

 
