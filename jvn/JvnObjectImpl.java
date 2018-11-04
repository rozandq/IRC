/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jvn;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import test.coordMain;

/**
 *
 * @author rozandq
 */
public class JvnObjectImpl implements JvnObject {
    private Serializable state;
    private Lock lock;
    private int id;

    public JvnObjectImpl(int id, Serializable s) {
        this.lock = Lock.W;
        this.state = s;
        this.id = id;
    }

    @Override
    public void jvnLockRead() throws JvnException {
        if(coordMain.printDebug) System.out.println("Object - LockRead");
        
        JvnServerImpl serv = JvnServerImpl.jvnGetServer();
        switch(this.lock){
            case NL:
                this.state = serv.jvnLockRead(this.jvnGetObjectId());
                this.lock = Lock.R;
                break;
            case RC:
                this.lock = Lock.R;
                break;
            case WC:
                this.lock = Lock.RWC;
                break;
            default:
                if(coordMain.printDebug) System.out.println("You already have a lock!");
        }
        
//        if(this.lock == Lock.NL) this.state = serv.jvnLockRead(this.jvnGetObjectId());
//        
//        if(this.lock == Lock.NL || this.lock == Lock.RC) this.lock = Lock.R;
//        else if(this.lock == Lock.WC)  this.lock = Lock.RWC;
        
        if(coordMain.printDebug) System.out.println("jvn.JvnObjectImpl.jvnLockRead() Done");
    }

    @Override
    public void jvnLockWrite() throws JvnException {
        if(coordMain.printDebug) System.out.println("Object - LockWrite");
        JvnServerImpl serv = JvnServerImpl.jvnGetServer();
        switch(this.lock){
            case NL:
                this.state = serv.jvnLockWrite(this.jvnGetObjectId());
                this.lock = Lock.W;
                break;
            case RC:
                this.state = serv.jvnLockWrite(this.jvnGetObjectId());
                this.lock = Lock.W;
                break;
            case WC:
                this.lock = Lock.W;
                break;
            default:
                if(coordMain.printDebug) System.out.println("You already have a lock!");
        }
        if(coordMain.printDebug) System.out.println("Object - LockWrite - Done");
    }

    @Override
    public synchronized void jvnUnLock() throws JvnException {
        if(coordMain.printDebug) System.out.println("Unlock");
        switch(this.lock){
            case R:
                this.lock = Lock.RC;
                break;
            
            case RWC:
            case W:
                this.lock = Lock.WC;
                break;
            default:
                System.err.println("JvnObjectImpl - jvnUnLock : " + this.lock);
                
        }
        if(coordMain.printDebug) System.out.println("Notify");
        notify();          
        
    }

    @Override
    public int jvnGetObjectId() throws JvnException {
        return this.id;
    }

    @Override
    public synchronized Serializable jvnGetObjectState() throws JvnException {
        return this.state;
    }
    
    public synchronized Lock getLock(){
        return this.lock;
    }

    @Override
    public synchronized void jvnInvalidateReader() throws JvnException {
    	if(coordMain.printDebug) System.out.println("jvn.JvnObjectImpl.jvnInvalidateReader()");
        switch(this.lock){
            case RC:
                this.lock = Lock.NL;
                break;
            case R:
                try {
                    if(coordMain.printDebug) System.out.println("(R)Waiting..");
                    wait();
                } catch (InterruptedException ex) {
                    System.err.println("JvnObjectImpl - jvnInvalidateReader wait");
                }
                this.lock = Lock.NL;
                break;
            default:
                System.err.println("JvnObjectImpl - jvnInvalidateReader : " + this.lock); 
        }
        if(coordMain.printDebug) System.out.println("jvn.JvnObjectImpl.jvnInvalidateReader() Done");
    }

    @Override
    public synchronized Serializable jvnInvalidateWriter() throws JvnException {
        if(coordMain.printDebug) System.out.println("jvn.JvnObjectImpl.jvnInvalidateWriter()");
        switch(this.lock){
            case WC:
                this.lock = Lock.NL;
                break;
            // RWC || W
            case RWC:
            case W:
                try {
                    if(coordMain.printDebug) System.out.println("(W)Waiting..");
                    wait();
                } catch (InterruptedException ex) {
                    System.err.println("JvnObjectImpl - jvnInvalidateReader wait");
                }
                this.lock = Lock.NL;
                break;
            case NL:
                break;
            default:
                System.err.println("JvnObjectImpl - jvnInvalidateWriter : " + this.lock);
                
        }
        
        if(coordMain.printDebug) System.out.println("jvn.JvnObjectImpl.jvnInvalidateWriter() Done");
        return this.state;
    }

    @Override
    public synchronized Serializable jvnInvalidateWriterForReader() throws JvnException {
        if(coordMain.printDebug) System.out.println("jvn.JvnObjectImpl.jvnInvalidateWriterForReader()");
        switch(this.lock){
            case WC:
                this.lock = Lock.RC;
                break;
            case W:
                try {
                    if(coordMain.printDebug) System.out.println("(WFR)Waiting..");
                    wait();
                    this.lock = Lock.RC;
                    break;
                } catch (InterruptedException ex) {
                    System.err.println("Error JvnObjectImpl - jvnInvalidateWriterForReader");
                }
            case RWC:
                this.lock = Lock.R;
                break;
            case NL:
                break;
            default:
                break;
        }
        if(coordMain.printDebug) System.out.println("jvn.JvnObjectImpl.jvnInvalidateWriterForReader() Done");
        return this.state;
    }

    public void setState(Serializable state) {
        this.state = state;
    }
    
    public void setLock(Lock lock) {
    	this.lock = lock;
    }
}
