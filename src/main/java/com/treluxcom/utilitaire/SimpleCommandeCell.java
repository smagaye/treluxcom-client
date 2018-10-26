package com.treluxcom.utilitaire;
import com.treluxcom.metier.Commande;
import javafx.scene.control.ListCell;

/**
 *
 * @author ADA-MALICK
 */
public class SimpleCommandeCell extends ListCell<Commande> {
    @Override
    protected void updateItem(Commande item, boolean empty){
        super.updateItem(item, empty);
        setText(null);
        if(!empty && item !=null){
            final String text= String.format("%s",item.getCodecommande());
            setText(text);
        }
    }   

}
