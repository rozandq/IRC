/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jvn;

import java.io.Serializable;

/**
 *
 * @author rozandq
 */
public class JvnObjectImpl implements JvnObject {
    Serializable state;
    Lock lock;
    int id;

    public JvnObjectImpl(int id) {
        this.lock = Lock.NL;
        this.id = id;
    }

    @Override
    public void jvnLockRead() throws JvnException {
        JvnServerImpl serv = JvnServerImpl.jvnGetServer();
        switch(this.lock){
            case NL:
                this.state = serv.jvnLockRead(this.jvnGetObjectId());
                break;
            case RC:
                this.state = serv.jvnLockRead(this.jvnGetObjectId());
                this.lock = Lock.R;
                break;
            case WC:
                this.state = serv.jvnLockRead(this.jvnGetObjectId());
                this.lock = Lock.RWC;
                break;
            case R:
                throw new JvnException("You already have a read lock!");
            case W:
                throw new JvnException("You already have a write lock!");
            case RWC:
                throw new JvnException("You already have a read-write-cached lock!");
        }
    }

    @Override
    public void jvnLockWrite() throws JvnException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void jvnUnLock() throws JvnException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int jvnGetObjectId() throws JvnException {
        return -1;
    }

    @Override
    public Serializable jvnGetObjectState() throws JvnException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void jvnInvalidateReader() throws JvnException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Serializable jvnInvalidateWriter() throws JvnException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Serializable jvnInvalidateWriterForReader() throws JvnException {
        this.lock = Lock.RC;
        return this.state;
    }
    
}
