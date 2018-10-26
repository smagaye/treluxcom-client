package com.treluxcom.service;

import com.treluxcom.metier.Personne;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IPersonneHome extends Remote {

    public List<Personne> personneList() throws RemoteException;

    public Personne connection(String login, String motpasse)throws RemoteException;

    void delete(Personne persistentInstance) throws RemoteException;

    public Personne findById(String codepersonne) throws RemoteException;
    
    public Personne findByEmail(String email) throws RemoteException;
    
    public Personne findByLogin(String login) throws RemoteException;

    boolean inserer(Personne personne) throws RemoteException;

    void update(String toUpdate, String toUpdateValue, String reference, String referenceValue) throws RemoteException;
    
}
