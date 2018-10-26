package com.treluxcom.service;

import com.treluxcom.metier.Lignecommande;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author ADA-MALICK
 */
public interface ILignecommandeHome extends Remote {

    void deleteById(String codelignecommande) throws RemoteException;

    boolean persistLigneCommande(Lignecommande lignecommande) throws RemoteException;

    Lignecommande findById(String codelignecommande) throws RemoteException;

    List<Lignecommande> lignecommandeList() throws RemoteException;

    boolean persist(Lignecommande lignecommande) throws RemoteException;

    void update(String toUpdate, String toUpdateValue, String reference, String referenceValue) throws RemoteException;

}
