package com.dealtroc.entities;

import com.dealtroc.utils.Statics;

public class Produit implements Comparable<Produit> {

    private int id;
    private String image;
    private String description;
    private String titre;
    private String categorie;
    private String prix;
    private Utilisateur utilisateur;

    public Produit() {
    }

    public Produit(int id, String image, String description, String titre, String categorie, String prix, Utilisateur utilisateur) {
        this.id = id;
        this.image = image;
        this.description = description;
        this.titre = titre;
        this.categorie = categorie;
        this.prix = prix;
        this.utilisateur = utilisateur;
    }

    public Produit(String image, String description, String titre, String categorie, String prix, Utilisateur utilisateur) {
        this.image = image;
        this.description = description;
        this.titre = titre;
        this.categorie = categorie;
        this.prix = prix;
        this.utilisateur = utilisateur;
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

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    @Override
    public int compareTo(Produit produit) {
        switch (Statics.compareVar) {
            case "Image":
                return this.getImage().compareTo(produit.getImage());
            case "Description":
                return this.getDescription().compareTo(produit.getDescription());
            case "Titre":
                return this.getTitre().compareTo(produit.getTitre());
            case "Categorie":
                return this.getCategorie().compareTo(produit.getCategorie());
            case "Prix":
                return this.getPrix().compareTo(produit.getPrix());
            case "Utilisateur":
                return this.getUtilisateur().getEmail().compareTo(produit.getUtilisateur().getEmail());

            default:
                return 0;
        }
    }

    @Override
    public String toString() {
        return titre == null ? "ID produit : " + id : "Titre : " + titre;
    }

    public String toStringForShare() {
        return "Produit{" +
                "\nid : " + id +
                "\nimage : '" + image +
                "\ndescription : '" + description +
                "\ntitre : '" + titre +
                "\ncategorie : '" + categorie +
                "\nprix : '" + prix +
                "\nutilisateur : " + utilisateur +
                '}';
    }

}