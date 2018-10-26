/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.treluxcom.service;

import com.treluxcom.metier.Fournisseur;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author ADA-MALICK
 */
public interface IFournisseurHome extends Remote {

    void deleteById(String codepersonne) throws RemoteException;

    void deleteByObject(Fournisseur fournisseur) throws RemoteException;

    Fournisseur findById(String codepersonne) throws RemoteException;

    List<Fournisseur> fournisseurList() throws RemoteException;

    boolean persist(Fournisseur fournisseur) throws RemoteException;

    void update(String toUpdate, String toUpdateValue, String reference, String referenceValue) throws RemoteException;
    
}
