/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.treluxcom.service;

import com.treluxcom.metier.Employe;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

/**
 *
 * @author ADA-MALICK
 */
public interface IEmployeHome extends Remote{

    void deleteById(String codepersonne) throws RemoteException;

    void deleteByObject(Employe employe) throws RemoteException;

    List<Employe> employeList() throws RemoteException;

    Employe findById(String codepersonne) throws RemoteException;

    boolean persist(Employe employe) throws RemoteException;
           ;

    void update(String toUpdate, String toUpdateValue, String reference, String referenceValue) throws RemoteException;
    
      public List<Employe> ListEmployes(Set boutiques) throws RemoteException;
}
