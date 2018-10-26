/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.treluxcom.service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.persistence.Column;

/**
 *
 * @author ADA-MALICK
 */
public interface IGerantprivilegeId extends Remote {

    boolean equals(Object other);

    @Column(name = "codepersonne", nullable = false, length = 40)
    String getCodepersonne();

    @Column(name = "codeprivilege", nullable = false)
    int getCodeprivilege()  throws RemoteException;

    int hashCode();

    void setCodepersonne(String codepersonne)  throws RemoteException;

    void setCodeprivilege(int codeprivilege)  throws RemoteException;
    
}
