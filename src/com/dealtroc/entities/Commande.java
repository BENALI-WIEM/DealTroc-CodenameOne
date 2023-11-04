package com.dealtroc.entities;

import java.util.Date;

public class Commande {

    private int id;
    private Utilisateur utilisateur;
    private Produit produit;
    private Date date;
    private String role;
    private int status;
    private Livreur livreur;
    private Date dateLivraison;
    private Date dateConfirmation;

    public Commande() {
    }

    public Commande(int id, Utilisateur utilisateur, Produit produit, Date date, String role, int status, Livreur livreur, Date dateLivraison, Date dateConfirmation) {
        this.id = id;
        this.utilisateur = utilisateur;
        this.produit = produit;
        this.date = date;
        this.role = role;
        this.status = status;
        this.livreur = livreur;
        this.dateLivraison = dateLivraison;
        this.dateConfirmation = dateConfirmation;
    }

    public Commande(Utilisateur utilisateur, Produit produit, Date date, String role, int status, Livreur livreur, Date dateLivraison, Date dateConfirmation) {
        this.utilisateur = utilisateur;
        this.produit = produit;
        this.date = date;
        this.role = role;
        this.status = status;
        this.livreur = livreur;
        this.dateLivraison = dateLivraison;
        this.dateConfirmation = dateConfirmation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Livreur getLivreur() {
        return livreur;
    }

    public void setLivreur(Livreur livreur) {
        this.livreur = livreur;
    }

    public Date getDateLivraison() {
        return dateLivraison;
    }

    public void setDateLivraison(Date dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public Date getDateConfirmation() {
        return dateConfirmation;
    }

    public void setDateConfirmation(Date dateConfirmation) {
        this.dateConfirmation = dateConfirmation;
    }


    @Override
    public String toString() {
        return "Id commande : " + id;
    }
}