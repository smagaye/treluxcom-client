/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.treluxcom.service;

import com.treluxcom.metier.Devis;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


public interface IDevisHome extends Remote {

    void deleteById(String codedevis)throws RemoteException;

    void deleteByObject(Devis devis)throws RemoteException;

    List<Devis> devisAnnulerList() throws RemoteException;

    List<Devis> devisEncoursList() throws RemoteException;

    List<Devis> devisList() throws RemoteException;

    List<Devis> devisList(String codepersonne) throws RemoteException;

    List<Devis> devisValideList() throws RemoteException;

    Devis findById(String codedevis) throws RemoteException;

    boolean persist(Devis devis) throws RemoteException;

    void update(String toUpdate, String toUpdateValue, String reference, String referenceValue) throws RemoteException;
    
}
