/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.treluxcom.service;

import com.treluxcom.metier.Famille;
import com.treluxcom.metier.Produit;
import java.math.BigDecimal;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author apple
 */
public interface IProduitHome  extends Remote{

    void deleteById(String codeproduit) throws RemoteException;

    void deleteByObject(Produit produit) throws RemoteException;

    Produit findById(String codeproduit) throws RemoteException;

    boolean persist(Produit produit) throws RemoteException;

    List<Produit> produitAnnulerList() throws RemoteException;

    List<Produit> produitAttenteConfirmationList() throws RemoteException;

    List<Produit> produitEncoursList() throws RemoteException;

    List<Produit> produitList() throws RemoteException;

    List<Produit> produitList(String codepersonne) throws RemoteException;

    List<Produit> produitValideList() throws RemoteException;

    void update(String toUpdate, String toUpdateValue, String reference, String referenceValue) throws RemoteException;

    void updateEtat(Produit com, int etat) throws RemoteException;
    
    public List<Produit> produitDispoList(Famille famille) throws RemoteException;
    
    public List<Produit> retirerProduit(Famille famille, int nombre) throws RemoteException;
    
     public BigDecimal getPrice(Famille famille) throws RemoteException;
}
