package com.treluxcom.service;

import com.treluxcom.metier.Panierclient;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


public interface IPanierclientHome extends Remote {
    List<Panierclient> panierclientList() throws RemoteException ;

    boolean persist(Panierclient panierclient) throws RemoteException;

    void deleteByObject(Panierclient panierclient) throws RemoteException;

    void deleteById(String codepanier) throws RemoteException;

    void update(String toUpdate, String toUpdateValue, String reference, String referenceValue) throws RemoteException;

    Panierclient findById(String codepanier) throws RemoteException;
}
