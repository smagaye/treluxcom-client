/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.treluxcom.service;

import com.treluxcom.metier.Calendrierservice;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author ADA-MALICK
 */
public interface ICalendrierservicehome extends Remote {

    List<Calendrierservice> calendrierserviceList() throws RemoteException;

    void deleteById(String codecalendrierservice) throws RemoteException;

    Calendrierservice findById(String codecalendrierservice) throws RemoteException;

    boolean persist(Calendrierservice calendrierservice) throws RemoteException;

    void update(String toUpdate, String toUpdateValue, String reference, String referenceValue) throws RemoteException;
    
}
