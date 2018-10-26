package com.treluxcom.service;

import com.treluxcom.metier.Paniercaissier;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


public interface IPaniercaissierHome extends Remote {
    List<Paniercaissier> paniercaissierList() throws RemoteException ;

    boolean persist(Paniercaissier paniercaissier) throws RemoteException;

    void deleteByObject(Paniercaissier paniercaissier) throws RemoteException;

    void deleteById(String codepanier) throws RemoteException;

    void update(String toUpdate, String toUpdateValue, String reference, String referenceValue) throws RemoteException;

    Paniercaissier findById(String codepanier) throws RemoteException;
}
