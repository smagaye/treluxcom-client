package com.treluxcom.service;

import com.treluxcom.metier.Boutique;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


public interface IBoutiqueHome extends Remote {

    List<Boutique> boutiqueList() throws RemoteException;

    void deleteById(String codeboutique) throws RemoteException;

    void deleteByObject(Boutique boutique) throws RemoteException;

    Boutique findById(String codeboutique) throws RemoteException;

    boolean persist(Boutique boutique) throws RemoteException;

    void update(String toUpdate, String toUpdateValue, String reference, String referenceValue) throws RemoteException;
    
}
