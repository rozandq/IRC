/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jvn;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rozandq
 */
public class JvnObjectImpl implements JvnObject {
    private Serializable state;
    private Lock lock;
    int id;

    public JvnObjectImpl(int id, Serializable s) {
        this.lock = Lock.W;
        this.state = s;
        this.id = id;
    }

    @Override
    public synchronized void jvnLockRead() throws JvnException {
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
            case R:
                throw new JvnException("You already have a read lock!");
            case W:
                throw new JvnException("You already have a write lock!");
            case RWC:
                throw new JvnException("You already have a read lock!");
        }
    }

    @Override
    public synchronized void jvnLockWrite() throws JvnException {
        JvnServerImpl serv = JvnServerImpl.jvnGetServer();
        switch(this.lock){
            case NL:
                this.state = serv.jvnLockWrite(this.jvnGetObjectId());
                this.lock = Lock.W;
            case RC:
                this.state = serv.jvnLockWrite(this.jvnGetObjectId());
                this.lock = Lock.W;
                break;
            case WC:
                this.lock = Lock.W;
                break;
            case R:
                throw new JvnException("You already have a read lock!");
            case W:
                throw new JvnException("You already have a write lock!");
            case RWC:
                throw new JvnException("You already have a read lock!");
        }
    }

    @Override
    public synchronized void jvnUnLock() throws JvnException {
        switch(this.lock){
            case R:
                this.lock = Lock.RC;
                break;
            
            case RWC:
            case W:
                this.lock = Lock.WC;
                break;
            default:
                throw new JvnException("JvnObjectImpl - jvnUnLock : " + this.lock);
                
        }
        System.out.println("Notify");
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

    @Override
    public synchronized void jvnInvalidateReader() throws JvnException {
    	System.out.println("invR");
        switch(this.lock){
            case RC:
                this.lock = Lock.NL;
                break;
            case R:
//                try {
                    System.out.println("(R)Waiting..");
                    System.out.flush();
//                    wait();
//                } catch (InterruptedException ex) {
//                    throw new JvnException("JvnObjectImpl - jvnInvalidateReader wait");
//                }
                this.lock = Lock.NL;
                break;
            default:
                throw new JvnException("JvnObjectImpl - jvnInvalidateReader : " + this.lock);
                
        }
    }

    @Override
    public synchronized Serializable jvnInvalidateWriter() throws JvnException {
    	System.out.println("invW");
        switch(this.lock){
            case WC:
                this.lock = Lock.NL;
                break;
            // RWC || W
            case RWC:
            case W:
//                try {
                    System.out.println("(W)Waiting..");
                    System.out.flush();
//                    wait();
//                } catch (InterruptedException ex) {
//                    throw new JvnException("JvnObjectImpl - jvnInvalidateReader wait");
//                }
                this.lock = Lock.NL;
                break;
            default:
                throw new JvnException("JvnObjectImpl - jvnInvalidateReader : " + this.lock);
                
        }
        return this.state;
    }

    @Override
    public synchronized Serializable jvnInvalidateWriterForReader() throws JvnException {
    	System.out.println("invWFR");
        switch(this.lock){
            case WC:
                this.lock = Lock.RC;
                break;
            case W:
//                try {
                    System.out.println("(WFR)Waiting..");
//                    wait();
                    this.lock = Lock.RC;
                    break;
//                } catch (InterruptedException ex) {
//                    throw new JvnException("Error JvnObjectImpl - jvnInvalidateWriterForReader");
//                }
            case RWC:
                this.lock = Lock.R;
                break;
            default:
                break;
        }
        
        return this.state;
    }

    public void setState(Serializable state) {
        this.state = state;
    }
    
    public void setLock(Lock lock) {
    	this.lock = lock;
    }
}
