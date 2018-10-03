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


public class JvnCoordImpl extends UnicastRemoteObject implements JvnRemoteCoord{
    HashMap<String, Integer> jvnObjectIds;
    HashMap<Integer, JvnObject> jvnObjects;
    HashMap<Integer, JvnRemoteServer> objectIdsLastVersionOwner;
    
    //HashMap<Integer, HashMap<JvnRemoteServer, State>> locks;
    
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
            
            this.counterObjectId = 0;
	}

    /**
    *  Allocate a NEW JVN object id (usually allocated to a 
    *  newly created JVN object)
    * @throws java.rmi.RemoteException,JvnException
    **/
    public int jvnGetObjectId()
    throws java.rmi.RemoteException,jvn.JvnException {
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
    public void jvnRegisterObject(String jon, JvnObject jo, JvnRemoteServer js)
    throws java.rmi.RemoteException,jvn.JvnException{
        int id = this.jvnGetObjectId();
        this.jvnObjectIds.put(jon, id);
        this.jvnObjects.put(id, jo);
        this.objectIdsLastVersionOwner.put(id, js);
    }
  
  /**
  * Get the reference of a JVN object managed by a given JVN server 
  * @param jon : the JVN object name
  * @param js : the remote reference of the JVNServer
  * @throws java.rmi.RemoteException,JvnException
  **/
  public JvnObject jvnLookupObject(String jon, JvnRemoteServer js)
  throws java.rmi.RemoteException,jvn.JvnException{          
    return null;
  }
  
  /**
  * Get a Read lock on a JVN object managed by a given JVN server 
  * @param joi : the JVN object identification
  * @param js  : the remote reference of the server
  * @return the current JVN object state
  * @throws java.rmi.RemoteException, JvnException
  **/
   public Serializable jvnLockRead(int joi, JvnRemoteServer js)
   throws java.rmi.RemoteException, JvnException{
    // to be completed
    return null;
   }

  /**
  * Get a Write lock on a JVN object managed by a given JVN server 
  * @param joi : the JVN object identification
  * @param js  : the remote reference of the server
  * @return the current JVN object state
  * @throws java.rmi.RemoteException, JvnException
  **/
   public Serializable jvnLockWrite(int joi, JvnRemoteServer js)
   throws java.rmi.RemoteException, JvnException{
    // to be completed
    return null;
   }

	/**
	* A JVN server terminates
	* @param js  : the remote reference of the server
	* @throws java.rmi.RemoteException, JvnException
	**/
    public void jvnTerminate(JvnRemoteServer js)
	 throws java.rmi.RemoteException, JvnException {
	 // to be completed
    }
}

 
