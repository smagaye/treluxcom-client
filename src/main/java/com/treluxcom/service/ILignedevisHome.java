package com.treluxcom.service;

import com.treluxcom.metier.Lignedevis;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author ADA-MALICK
 */
public interface ILignedevisHome extends Remote {

    void deleteById(String codelignedevis) throws RemoteException;

    boolean persistLigneCommande(Lignedevis lignedevis) throws RemoteException;

    Lignedevis findById(String codelignedevis) throws RemoteException;

    List<Lignedevis> lignedevisList() throws RemoteException;

    boolean persist(Lignedevis lignedevis) throws RemoteException;

    void update(String toUpdate, String toUpdateValue, String reference, String referenceValue) throws RemoteException;

}
