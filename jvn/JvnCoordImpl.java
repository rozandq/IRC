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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class JvnCoordImpl extends UnicastRemoteObject implements JvnRemoteCoord{
    private HashMap<String, Integer> jvnObjectIds;
    private HashMap<Integer, JvnObject> jvnObjects;
    private HashMap<Integer, JvnRemoteServer> objectIdsLastVersionOwner;
    private HashMap<Integer, ArrayList<JvnRemoteServer>> readLocks;
    
    private int counterObjectId;

    /**
    * Default constructor
    * @throws JvnException
    **/
	public JvnCoordImpl() throws Exception {
		// to be completed
            this.jvnObjectIds = new HashMap<>();
            this.jvnObjects = new HashMap<>();
            this.objectIdsLastVersionOwner = new HashMap<>();
            this.readLocks = new HashMap<>();
            
            this.counterObjectId = 0;
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
  **/
    public synchronized Serializable jvnLockRead(int joi, JvnRemoteServer js) throws java.rmi.RemoteException, JvnException{
        // to be completed
        Serializable state = null;
        if(this.objectIdsLastVersionOwner.get(joi) == null){
            state = this.jvnObjects.get(joi).jvnGetObjectState();

        } else {
            state = this.objectIdsLastVersionOwner.get(joi).jvnInvalidateWriterForReader(joi);
            this.objectIdsLastVersionOwner.put(joi, null);
            ((JvnObjectImpl)this.jvnObjects.get(joi)).setState(state);
        }

        this.readLocks.get(joi).add(js);
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
    // to be completed
       Serializable state = null;
       
       if(this.objectIdsLastVersionOwner.get(joi) == null){
           state = this.jvnObjects.get(joi).jvnGetObjectState();
       } else {
           state = this.objectIdsLastVersionOwner.get(joi).jvnInvalidateWriter(joi);
           
           
       }
       this.objectIdsLastVersionOwner.put(joi, js);
       for(JvnRemoteServer jvnServer : this.readLocks.get(joi)){
               jvnServer.jvnInvalidateReader(joi);
           }
       this.readLocks.get(joi).clear();
       
    return state;
   }

	/**
	* A JVN server terminates
	* @param js  : the remote reference of the server
	* @throws java.rmi.RemoteException, JvnException
	**/
    public synchronized void jvnTerminate(JvnRemoteServer js) throws java.rmi.RemoteException, JvnException {
	 // to be completed
         Serializable state;
         for(Map.Entry mapentry : this.objectIdsLastVersionOwner.entrySet()){
             if( (mapentry.getValue() != null) && (((JvnRemoteServer)mapentry.getValue()).equals(js)) ){
                state = ((JvnRemoteServer)mapentry.getValue()).jvnInvalidateWriter((Integer) mapentry.getKey());
                ((JvnObjectImpl)this.jvnObjects.get((Integer) mapentry.getKey())).setState(state);
                this.objectIdsLastVersionOwner.put((Integer) mapentry.getKey(), js);
             }
         }
    }
}

 
