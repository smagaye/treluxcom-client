package com.treluxcom.service;

import com.treluxcom.metier.Administrateur;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IAdministrateurHome extends Remote{

    List<Administrateur> administrateurList() throws RemoteException;

    void deleteById(String codeadministrateur) throws RemoteException;

    void deleteByObject(Administrateur administrateur) throws RemoteException;

    Administrateur findById(String codeadministrateur) throws RemoteException;

    boolean persist(Administrateur administrateur) throws RemoteException;

    void update(String toUpdate, String toUpdateValue, String reference, String referenceValue) throws RemoteException;
    
}
