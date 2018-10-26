package com.treluxcom.utilitaire;
import com.treluxcom.metier.Famille;
import javafx.scene.control.ListCell;

/**
 *
 * @author ADA-MALICK
 */
public class SimpleFamilleCell extends ListCell<Famille> {
    @Override
    protected void updateItem(Famille item, boolean empty){
        super.updateItem(item, empty);
        setText(null);
        if(!empty && item !=null){
            final String text= String.format("%s",item.getNom());
            setText(text);
        }
    }   

}
