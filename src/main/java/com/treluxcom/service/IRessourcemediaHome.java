/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.treluxcom.service;

import com.treluxcom.metier.Ressourcemedia;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


public interface IRessourcemediaHome extends Remote {

    public List<Ressourcemedia> ressourceMediaList() throws RemoteException;

    public boolean persist(Ressourcemedia ressourceMedia) throws RemoteException;

    public void deleteByObject(Ressourcemedia ressourceMedia) throws RemoteException;

    public void deleteById(String coderessource) throws RemoteException;

    public void update(String toUpdate, String toUpdateValue, String reference, String referenceValue) throws RemoteException;

    public Ressourcemedia findById(String coderessource) throws RemoteException;

}
