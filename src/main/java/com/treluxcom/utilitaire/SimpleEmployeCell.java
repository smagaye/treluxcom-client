/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.treluxcom.utilitaire;

import com.treluxcom.metier.Fournisseur;
import javafx.scene.control.ListCell;

/**
 *
 * @author ADA-MALICK
 */
public class SimpleEmployeCell extends ListCell<Fournisseur> {
    @Override
    protected void updateItem(Fournisseur item, boolean empty){
        super.updateItem(item, empty);
        setText(null);
        if(!empty && item !=null){
            final String text= String.format("%s %s",item.getPersonne().getPrenom(),item.getPersonne().getNom());
            setText(text);
        }
    }   

}
