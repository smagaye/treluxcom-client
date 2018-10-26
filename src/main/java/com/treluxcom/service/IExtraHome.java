package com.treluxcom.service;

import com.treluxcom.metier.Boutique;
import com.treluxcom.metier.Personne;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

public interface IExtraHome extends Remote {

    public List<Personne> EmployeList(String codeboutique) throws RemoteException ;

    Set<Boutique> boutiqueList(Personne personne) throws RemoteException;
    
}
