package com.dealtroc.entities;

public class Livreur {

    private int id;
    private String nom;
    private String num;

    public Livreur() {
    }

    public Livreur(int id, String nom, String num) {
        this.id = id;
        this.nom = nom;
        this.num = num;
    }

    public Livreur(String nom, String num) {
        this.nom = nom;
        this.num = num;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }


    @Override
    public String toString() {
        return nom;
    }
}