package com.treluxcom.metier;
// Generated 9 juin 2018 12:35:35 by Hibernate Tools 4.3.1


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Message generated by hbm2java
 */
@Entity
@Table(name="message"
    ,schema="public"
)
public class Message  implements java.io.Serializable {


     private long codemessage;
     private Groupe groupe;
     private Personne personneByCodedestinataire;
     private Personne personneByCodeenvoyeur;
     private String objet;
     private String contenu;

    public Message() {
    }

	
    public Message(long codemessage, Personne personneByCodeenvoyeur) {
        this.codemessage = codemessage;
        this.personneByCodeenvoyeur = personneByCodeenvoyeur;
    }
    public Message(long codemessage, Groupe groupe, Personne personneByCodedestinataire, Personne personneByCodeenvoyeur, String objet, String contenu) {
       this.codemessage = codemessage;
       this.groupe = groupe;
       this.personneByCodedestinataire = personneByCodedestinataire;
       this.personneByCodeenvoyeur = personneByCodeenvoyeur;
       this.objet = objet;
       this.contenu = contenu;
    }
   
     @Id 

    
    @Column(name="codemessage", nullable=false)
    public long getCodemessage() {
        return this.codemessage;
    }
    
    public void setCodemessage(long codemessage) {
        this.codemessage = codemessage;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="codegroupe")
    public Groupe getGroupe() {
        return this.groupe;
    }
    
    public void setGroupe(Groupe groupe) {
        this.groupe = groupe;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="codedestinataire")
    public Personne getPersonneByCodedestinataire() {
        return this.personneByCodedestinataire;
    }
    
    public void setPersonneByCodedestinataire(Personne personneByCodedestinataire) {
        this.personneByCodedestinataire = personneByCodedestinataire;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="codeenvoyeur", nullable=false)
    public Personne getPersonneByCodeenvoyeur() {
        return this.personneByCodeenvoyeur;
    }
    
    public void setPersonneByCodeenvoyeur(Personne personneByCodeenvoyeur) {
        this.personneByCodeenvoyeur = personneByCodeenvoyeur;
    }

    
    @Column(name="objet", length=50)
    public String getObjet() {
        return this.objet;
    }
    
    public void setObjet(String objet) {
        this.objet = objet;
    }

    
    @Column(name="contenu")
    public String getContenu() {
        return this.contenu;
    }
    
    public void setContenu(String contenu) {
        this.contenu = contenu;
    }




}

