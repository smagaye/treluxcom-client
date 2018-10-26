package com.treluxcom.metier;
// Generated 9 juin 2018 12:35:35 by Hibernate Tools 4.3.1


import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * LignecommandeId generated by hbm2java
 */
@Embeddable
public class LignecommandeId  implements java.io.Serializable {


     private String codecommande;
     private String codefamille;

    public LignecommandeId() {
    }

    public LignecommandeId(String codecommande, String codefamille) {
       this.codecommande = codecommande;
       this.codefamille = codefamille;
    }
   


    @Column(name="codecommande", nullable=false, length=50)
    public String getCodecommande() {
        return this.codecommande;
    }
    
    public void setCodecommande(String codecommande) {
        this.codecommande = codecommande;
    }


    @Column(name="codefamille", nullable=false, length=254)
    public String getCodefamille() {
        return this.codefamille;
    }
    
    public void setCodefamille(String codefamille) {
        this.codefamille = codefamille;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof LignecommandeId) ) return false;
		 LignecommandeId castOther = ( LignecommandeId ) other; 
         
		 return ( (this.getCodecommande()==castOther.getCodecommande()) || ( this.getCodecommande()!=null && castOther.getCodecommande()!=null && this.getCodecommande().equals(castOther.getCodecommande()) ) )
 && ( (this.getCodefamille()==castOther.getCodefamille()) || ( this.getCodefamille()!=null && castOther.getCodefamille()!=null && this.getCodefamille().equals(castOther.getCodefamille()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getCodecommande() == null ? 0 : this.getCodecommande().hashCode() );
         result = 37 * result + ( getCodefamille() == null ? 0 : this.getCodefamille().hashCode() );
         return result;
   }   


}

