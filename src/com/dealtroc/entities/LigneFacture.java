package com.dealtroc.entities;

public class LigneFacture {

    private int id;
    private Facture facture;
    private float prixInitial;
    private float prixVente;
    private float prixLivraison;
    private float prixTotal;
    private float revenu;

    public LigneFacture() {
    }

    public LigneFacture(int id, Facture facture, float prixInitial, float prixVente, float prixLivraison, float prixTotal, float revenu) {
        this.id = id;
        this.facture = facture;
        this.prixInitial = prixInitial;
        this.prixVente = prixVente;
        this.prixLivraison = prixLivraison;
        this.prixTotal = prixTotal;
        this.revenu = revenu;
    }

    public LigneFacture(Facture facture, float prixInitial, float prixVente, float prixLivraison, float prixTotal, float revenu) {
        this.facture = facture;
        this.prixInitial = prixInitial;
        this.prixVente = prixVente;
        this.prixLivraison = prixLivraison;
        this.prixTotal = prixTotal;
        this.revenu = revenu;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Facture getFacture() {
        return facture;
    }

    public void setFacture(Facture facture) {
        this.facture = facture;
    }

    public float getPrixInitial() {
        return prixInitial;
    }

    public void setPrixInitial(float prixInitial) {
        this.prixInitial = prixInitial;
    }

    public float getPrixVente() {
        return prixVente;
    }

    public void setPrixVente(float prixVente) {
        this.prixVente = prixVente;
    }

    public float getPrixLivraison() {
        return prixLivraison;
    }

    public void setPrixLivraison(float prixLivraison) {
        this.prixLivraison = prixLivraison;
    }

    public float getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(float prixTotal) {
        this.prixTotal = prixTotal;
    }

    public float getRevenu() {
        return revenu;
    }

    public void setRevenu(float revenu) {
        this.revenu = revenu;
    }

    @Override
    public String toString() {
        return "Id ligne facture : " + id;
    }
}