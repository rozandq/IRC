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
public enum Lock implements Serializable {
    NL,
    RC,
    WC,
    R,
    W,
    RWC
}
