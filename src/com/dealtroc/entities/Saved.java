package com.dealtroc.entities;


public class Saved {

    private int id;
    private String image;
    private String description;
    private String titre;
    private String categorie;
    private String prix;

    public Saved() {
    }

    public Saved(int id, String image, String description, String titre, String categorie, String prix) {
        this.id = id;
        this.image = image;
        this.description = description;
        this.titre = titre;
        this.categorie = categorie;
        this.prix = prix;
    }

    public Saved(String image, String description, String titre, String categorie, String prix) {
        this.image = image;
        this.description = description;
        this.titre = titre;
        this.categorie = categorie;
        this.prix = prix;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }
}