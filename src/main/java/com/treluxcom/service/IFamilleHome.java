/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.treluxcom.service;

import com.treluxcom.metier.Famille;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author ADA-MALICK
 */
public interface IFamilleHome extends Remote {

    void deleteById(String codefamille) throws RemoteException;

    void deleteByObject(Famille famille) throws RemoteException;

    List<Famille> familleList() throws RemoteException;

    Famille findById(String codefamille) throws RemoteException;

    boolean persist(Famille famille) throws RemoteException;

    void update(String toUpdate, String toUpdateValue, String reference, String referenceValue) throws RemoteException;
    
}
