/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.treluxcom.service;

import com.treluxcom.metier.Caissier;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author ADA-MALICK
 */
public interface ICaissierHome extends Remote {

    List<Caissier> caissierList() throws RemoteException;

    void deleteById(String codepersonne) throws RemoteException;

    void deleteByObject(Caissier caissier) throws RemoteException;

    Caissier findById(String codepersonne) throws RemoteException;

    boolean persist(Caissier caissier) throws RemoteException;

    void update(String toUpdate, String toUpdateValue, String reference, String referenceValue) throws RemoteException;
    
}
