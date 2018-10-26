/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.treluxcom.service;

import com.treluxcom.metier.Commande;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author ADA-MALICK
 */
public interface ICommandeHome extends Remote {

    List<Commande> commandeList(String codepersonne) throws RemoteException;

    List<Commande> commandeAnnulerList() throws RemoteException;

    List<Commande> commandeEncoursList() throws RemoteException;

    List<Commande> commandeList() throws RemoteException;

    List<Commande> commandeValideList() throws RemoteException;

    void deleteById(String codecommande) throws RemoteException;

    void deleteByObject(Commande commande) throws RemoteException;

    Commande findById(String codecommande) throws RemoteException;

    boolean persist(Commande commande) throws RemoteException;

    void update(String toUpdate, String toUpdateValue, String reference, String referenceValue) throws RemoteException;

    void updateEtat(Commande com, int etat) throws RemoteException;

    public List<Commande> commandeAttenteConfirmationList() throws RemoteException;

}
