/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.treluxcom.utilitaire;

import com.treluxcom.metier.Boutique;
import javafx.scene.control.ListCell;

/**
 *
 * @author ADA-MALICK
 */
public class SimpleBoutiqueCell extends ListCell<Boutique> {
    @Override
    protected void updateItem(Boutique item, boolean empty){
        super.updateItem(item, empty);
        setText(null);
        if(!empty && item !=null){
            final String text= String.format("%s %s",item.getNom(),item.getAdresse());
            setText(text);
        }
    }   

}
