/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.treluxcom.service;

import com.treluxcom.metier.Gerant;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author ADA-MALICK
 */
public interface IGerantHome extends Remote{

    void deleteById(String codepersonne) throws RemoteException;

    void deleteByObject(Gerant gerant) throws RemoteException;

    Gerant findById(String codepersonne) throws RemoteException;

    List<Gerant> gerantList() throws RemoteException;

    boolean persist(Gerant gerant) throws RemoteException;

    void update(String toUpdate, String toUpdateValue, String reference, String referenceValue) throws RemoteException;
    
}
