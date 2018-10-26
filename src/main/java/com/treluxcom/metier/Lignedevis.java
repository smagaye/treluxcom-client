package com.treluxcom.metier;
// Generated 9 juin 2018 12:35:35 by Hibernate Tools 4.3.1


import java.math.BigDecimal;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Lignedevis generated by hbm2java
 */
@Entity
@Table(name="lignedevis"
    ,schema="public"
)
public class Lignedevis  implements java.io.Serializable {


     private LignedevisId id;
     private Devis devis;
     private Famille famille;
     private BigDecimal conditionnement;
     private Integer quantite;
     private BigDecimal prixunitaire;
     private BigDecimal prixtotal;
     private Boolean tva;

    public Lignedevis() {
    }

	
    public Lignedevis(LignedevisId id, Devis devis, Famille famille) {
        this.id = id;
        this.devis = devis;
        this.famille = famille;
    }
    public Lignedevis(LignedevisId id, Devis devis, Famille famille, BigDecimal conditionnement, Integer quantite, BigDecimal prixunitaire, BigDecimal prixtotal, Boolean tva) {
       this.id = id;
       this.devis = devis;
       this.famille = famille;
       this.conditionnement = conditionnement;
       this.quantite = quantite;
       this.prixunitaire = prixunitaire;
       this.prixtotal = prixtotal;
       this.tva = tva;
    }
   
     @EmbeddedId

    
    @AttributeOverrides( {
        @AttributeOverride(name="codedevis", column=@Column(name="codedevis", nullable=false, length=50) ), 
        @AttributeOverride(name="codefamille", column=@Column(name="codefamille", nullable=false, length=254) ) } )
    public LignedevisId getId() {
        return this.id;
    }
    
    public void setId(LignedevisId id) {
        this.id = id;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="codedevis", nullable=false, insertable=false, updatable=false)
    public Devis getDevis() {
        return this.devis;
    }
    
    public void setDevis(Devis devis) {
        this.devis = devis;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="codefamille", nullable=false, insertable=false, updatable=false)
    public Famille getFamille() {
        return this.famille;
    }
    
    public void setFamille(Famille famille) {
        this.famille = famille;
    }

    
    @Column(name="conditionnement", precision=12)
    public BigDecimal getConditionnement() {
        return this.conditionnement;
    }
    
    public void setConditionnement(BigDecimal conditionnement) {
        this.conditionnement = conditionnement;
    }

    
    @Column(name="quantite")
    public Integer getQuantite() {
        return this.quantite;
    }
    
    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    
    @Column(name="prixunitaire", precision=12)
    public BigDecimal getPrixunitaire() {
        return this.prixunitaire;
    }
    
    public void setPrixunitaire(BigDecimal prixunitaire) {
        this.prixunitaire = prixunitaire;
    }

    
    @Column(name="prixtotal", precision=12)
    public BigDecimal getPrixtotal() {
        return this.prixtotal;
    }
    
    public void setPrixtotal(BigDecimal prixtotal) {
        this.prixtotal = prixtotal;
    }

    
    @Column(name="tva")
    public Boolean getTva() {
        return this.tva;
    }
    
    public void setTva(Boolean tva) {
        this.tva = tva;
    }




}


