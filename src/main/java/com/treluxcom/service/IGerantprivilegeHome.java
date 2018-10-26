/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.treluxcom.service;

import com.treluxcom.metier.Gerantprivilege;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author ADA-MALICK
 */
public interface IGerantprivilegeHome extends Remote {

    Gerantprivilege findById(String codegerantprivilege) throws RemoteException;

    List<Gerantprivilege> gerantprivilegeList() throws RemoteException;

    boolean persist(Gerantprivilege gerantprivilege) throws RemoteException;

    /*   public void deleteByObject(Gerantprivilege gerantprivilege) {
    try {
    Session session = HibernateUtil.getSession();
    Transaction tr = session.beginTransaction();
    session.createQuery("delete from Gerantprivilege where codegerantprivilege = :codegerantprivilege")
    .setParameter("codegerantprivilege", gerantprivilege.getCodegerantprivilege())
    .executeUpdate();
    tr.commit();
    } catch (RuntimeException re) {
    throw re;
    }
    }
    public void deleteById(String codegerantprivilege) {
    try {
    Session session = HibernateUtil.getSession();
    Transaction tr = session.beginTransaction();
    session.createQuery("delete from Gerantprivilege where codegerantprivilege = :codegerantprivilege")
    .setParameter("codegerantprivilege", codegerantprivilege)
    .executeUpdate();
    tr.commit();
    } catch (RuntimeException re) {
    throw re;
    }
    }*/
    void update(String toUpdate, String toUpdateValue, String reference, String referenceValue) throws RemoteException;
    
}
