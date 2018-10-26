
package com.treluxcom.service;

import com.treluxcom.metier.Boutique;
import com.treluxcom.metier.Commande;
import com.treluxcom.metier.Produit;
import com.treluxcom.metier.Stock;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

public interface Statistiques extends Remote{
    public HashMap<String, List<Produit>> stock(Stock stock) throws RemoteException;
    public HashMap<String, List<Commande>> commande(Boutique boutique) throws RemoteException ;
    public HashMap<String, Integer> statProduit(Commande commande) throws RemoteException;
    public HashMap<String, Integer> getDashBoard() throws RemoteException;
}
