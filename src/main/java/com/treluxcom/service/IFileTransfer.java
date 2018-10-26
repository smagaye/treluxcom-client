/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.treluxcom.service;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author apple
 */
public interface IFileTransfer extends Remote {

    byte[] downloadFile(String fileName) throws RemoteException ;

    public boolean copierServer(File source, File dest) throws RemoteException ;
    
}
