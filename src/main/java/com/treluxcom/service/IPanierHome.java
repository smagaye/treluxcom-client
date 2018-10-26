/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.treluxcom.service;

import com.treluxcom.metier.Panier;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author smag
 */
public interface IPanierHome extends Remote {
    List<Panier> panierList() throws RemoteException ;

    boolean persist(Panier panier) throws RemoteException;

    void deleteByObject(Panier panier) throws RemoteException;

    void deleteById(String codepanier) throws RemoteException;

    void update(String toUpdate, String toUpdateValue, String reference, String referenceValue) throws RemoteException;

    Panier findById(String codepanier) throws RemoteException;
}
