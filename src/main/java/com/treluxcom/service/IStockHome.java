/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.treluxcom.service;

import com.treluxcom.metier.Famille;
import com.treluxcom.metier.Stock;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author apple
 */
public interface IStockHome extends Remote {

    void deleteById(String codestock) throws RemoteException;

    void deleteByObject(Stock stock) throws RemoteException;

    Stock findById(String codestock) throws RemoteException;

    boolean persist(Stock stock) throws RemoteException;

    List<Stock> stockList() throws RemoteException;

    void update(String toUpdate, String toUpdateValue, String reference, String referenceValue) throws RemoteException;

    List<Stock> stockPublieList() throws RemoteException;

    List<Stock> stockPublieNonVideList() throws RemoteException;

    boolean isEmpty(Stock stock) throws RemoteException;
    
    public int quantiteDispoEnStock(Stock stock, Famille famille) throws RemoteException;
    
    public HashMap<String, Integer> famillePublie(List<Stock> stocks) throws RemoteException ;
    
    public List<Famille> familleList(Stock stock) throws RemoteException ;
    
    public List<Stock> stockNonPublieList() throws RemoteException ;
}
