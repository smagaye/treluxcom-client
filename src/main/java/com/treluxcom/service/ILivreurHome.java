/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.treluxcom.service;

import com.treluxcom.metier.Livreur;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author ADA-MALICK
 */
public interface ILivreurHome extends Remote {

    void deleteById(String codepersonne) throws RemoteException;

    void deleteByObject(Livreur livreur) throws RemoteException;

    Livreur findById(String codepersonne) throws RemoteException;

    List<Livreur> livreurList() throws RemoteException;

    boolean persist(Livreur livreur) throws RemoteException;

    void update(String toUpdate, String toUpdateValue, String reference, String referenceValue) throws RemoteException;
    
}
