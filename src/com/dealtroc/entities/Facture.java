package com.dealtroc.entities;

import java.util.Date;

public class Facture {

    private int id;
    private Date dateFacturation;
    private float commission;
    private String statut;
    private Commande commande;

    public Facture() {
    }

    public Facture(int id, Date dateFacturation, float commission, String statut, Commande commande) {
        this.id = id;
        this.dateFacturation = dateFacturation;
        this.commission = commission;
        this.statut = statut;
        this.commande = commande;
    }

    public Facture(Date dateFacturation, float commission, String statut, Commande commande) {
        this.dateFacturation = dateFacturation;
        this.commission = commission;
        this.statut = statut;
        this.commande = commande;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateFacturation() {
        return dateFacturation;
    }

    public void setDateFacturation(Date dateFacturation) {
        this.dateFacturation = dateFacturation;
    }

    public float getCommission() {
        return commission;
    }

    public void setCommission(float commission) {
        this.commission = commission;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    @Override
    public String toString() {
        return "Id facture : " + id;
    }
}