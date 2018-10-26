package com.treluxcom.metier;
// Generated 9 juin 2018 12:35:35 by Hibernate Tools 4.3.1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Stock generated by hbm2java
 */
@Entity
@Table(name="stock"
    ,schema="public"
)
public class Stock  implements java.io.Serializable {


     private String codestock;
     private String categorie;
     private Integer quantitetotal;
     private Date datereception;
     private Boolean publier;
     private Set commandes = new HashSet(0);
     private Set produits = new HashSet(0);

    public Stock() {
    }

	
    public Stock(String codestock) {
        this.codestock = codestock;
    }
    public Stock(String codestock, String categorie, Integer quantitetotal, Date datereception, Boolean publier, Set commandes, Set produits) {
       this.codestock = codestock;
       this.categorie = categorie;
       this.quantitetotal = quantitetotal;
       this.datereception = datereception;
       this.publier = publier;
       this.commandes = commandes;
       this.produits = produits;
    }
   
     @Id 

    
    @Column(name="codestock", nullable=false, length=50)
    public String getCodestock() {
        return this.codestock;
    }
    
    public void setCodestock(String codestock) {
        this.codestock = codestock;
    }

    
    @Column(name="categorie", length=50)
    public String getCategorie() {
        return this.categorie;
    }
    
    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    
    @Column(name="quantitetotal")
    public Integer getQuantitetotal() {
        return this.quantitetotal;
    }
    
    public void setQuantitetotal(Integer quantitetotal) {
        this.quantitetotal = quantitetotal;
    }

    @Temporal(TemporalType.DATE)
    @Column(name="datereception", length=13)
    public Date getDatereception() {
        return this.datereception;
    }
    
    public void setDatereception(Date datereception) {
        this.datereception = datereception;
    }

    
    @Column(name="publier")
    public Boolean getPublier() {
        return this.publier;
    }
    
    public void setPublier(Boolean publier) {
        this.publier = publier;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="stock")
    public Set getCommandes() {
        return this.commandes;
    }
    
    public void setCommandes(Set commandes) {
        this.commandes = commandes;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="stock")
    public Set getProduits() {
        return this.produits;
    }
    
    public void setProduits(Set produits) {
        this.produits = produits;
    }




}


