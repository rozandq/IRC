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
    State state;

    public JvnObjectImpl() {
        this.state = State.NL;
    }

    @Override
    public void jvnLockRead() throws JvnException {
        switch(this.state){
            case NL:
                JvnServerImpl serv = JvnServerImpl.jvnGetServer();
                //serv.jvnLockRead(joi);
                break;
            case RC:
                this.state = State.R;
                break;
            case WC:
                this.state = State.RWC;
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
