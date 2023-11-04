package com.dealtroc.gui.back.commande;


import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.dealtroc.entities.Commande;
import com.dealtroc.entities.Livreur;
import com.dealtroc.entities.Produit;
import com.dealtroc.entities.Utilisateur;
import com.dealtroc.services.CommandeService;
import com.dealtroc.services.LivreurService;
import com.dealtroc.services.ProduitService;
import com.dealtroc.services.UtilisateurService;

import java.util.ArrayList;

public class ModifierCommande extends Form {


    Commande currentCommande;

    TextField roleTF;
    Label roleLabel;
    PickerComponent dateTF;
    PickerComponent dateLivraisonTF;
    PickerComponent dateConfirmationTF;

    ArrayList<Utilisateur> listUtilisateurs;
    PickerComponent utilisateurPC;
    Utilisateur selectedUtilisateur = null;
    ArrayList<Produit> listProduits;
    PickerComponent produitPC;
    Produit selectedProduit = null;
    ArrayList<Livreur> listLivreurs;
    PickerComponent livreurPC;
    Livreur selectedLivreur = null;


    Button manageButton;

    Form previous;

    public ModifierCommande(Form previous) {
        super("Modifier", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        currentCommande = AfficherToutCommande.currentCommande;

        addGUIs();
        addActions();


        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    PickerComponent statusPC;

    private void addGUIs() {
        statusPC = PickerComponent.createStrings("").label("Status");
        String[] strings = new String[2];
        strings[0] = "Confirmé";
        strings[1] = "Non confirmé";
        statusPC.getPicker().setStrings(strings);


        String[] utilisateurStrings;
        int utilisateurIndex;
        utilisateurPC = PickerComponent.createStrings("").label("Utilisateur");
        listUtilisateurs = UtilisateurService.getInstance().getAll();
        utilisateurStrings = new String[listUtilisateurs.size()];
        utilisateurIndex = 0;
        for (Utilisateur utilisateur : listUtilisateurs) {
            utilisateurStrings[utilisateurIndex] = utilisateur.getEmail();
            utilisateurIndex++;
        }
        if (listUtilisateurs.size() > 0) {
            utilisateurPC.getPicker().setStrings(utilisateurStrings);
            utilisateurPC.getPicker().addActionListener(l -> selectedUtilisateur = listUtilisateurs.get(utilisateurPC.getPicker().getSelectedStringIndex()));
        } else {
            utilisateurPC.getPicker().setStrings("");
        }

        String[] produitStrings;
        int produitIndex;
        produitPC = PickerComponent.createStrings("").label("Produit");
        listProduits = ProduitService.getInstance().getAll();
        produitStrings = new String[listProduits.size()];
        produitIndex = 0;
        for (Produit produit : listProduits) {
            produitStrings[produitIndex] = produit.getDescription();
            produitIndex++;
        }
        if (listProduits.size() > 0) {
            produitPC.getPicker().setStrings(produitStrings);
            produitPC.getPicker().addActionListener(l -> selectedProduit = listProduits.get(produitPC.getPicker().getSelectedStringIndex()));
        } else {
            produitPC.getPicker().setStrings("");
        }

        String[] livreurStrings;
        int livreurIndex;
        livreurPC = PickerComponent.createStrings("").label("Livreur");
        listLivreurs = LivreurService.getInstance().getAll();
        livreurStrings = new String[listLivreurs.size()];
        livreurIndex = 0;
        for (Livreur livreur : listLivreurs) {
            livreurStrings[livreurIndex] = livreur.getNom();
            livreurIndex++;
        }
        if (listLivreurs.size() > 0) {
            livreurPC.getPicker().setStrings(livreurStrings);
            livreurPC.getPicker().addActionListener(l -> selectedLivreur = listLivreurs.get(livreurPC.getPicker().getSelectedStringIndex()));
        } else {
            livreurPC.getPicker().setStrings("");
        }


        dateTF = PickerComponent.createDate(null).label("Date");


        roleLabel = new Label("Role : ");
        roleLabel.setUIID("labelDefault");
        roleTF = new TextField();
        roleTF.setHint("Tapez le role");




        dateLivraisonTF = PickerComponent.createDate(null).label("DateLivraison");


        dateConfirmationTF = PickerComponent.createDate(null).label("DateConfirmation");


        dateTF.getPicker().setDate(currentCommande.getDate());
        roleTF.setText(currentCommande.getRole());

        dateLivraisonTF.getPicker().setDate(currentCommande.getDateLivraison());
        dateConfirmationTF.getPicker().setDate(currentCommande.getDateConfirmation());

        utilisateurPC.getPicker().setSelectedString(currentCommande.getUtilisateur().getEmail());
        selectedUtilisateur = currentCommande.getUtilisateur();
        produitPC.getPicker().setSelectedString(currentCommande.getProduit().getDescription());
        selectedProduit = currentCommande.getProduit();
        livreurPC.getPicker().setSelectedString(currentCommande.getLivreur().getNom());
        selectedLivreur = currentCommande.getLivreur();
        statusPC.getPicker().setSelectedString(currentCommande.getStatus() == 1 ? "Confirmé" : "Non confirmé");

        manageButton = new Button("Modifier");


        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(


                dateTF,
                roleLabel, roleTF,
                statusPC,

                dateLivraisonTF,
                dateConfirmationTF,
                utilisateurPC, produitPC, livreurPC,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {

        manageButton.addActionListener(action -> {
            if (controleDeSaisie()) {
                int responseCode = CommandeService.getInstance().edit(
                        new Commande(
                                currentCommande.getId(),
                                selectedUtilisateur,
                                selectedProduit,
                                dateTF.getPicker().getDate(),
                                roleTF.getText(),
                                statusPC.getPicker().getSelectedString().equalsIgnoreCase("Confirmé") ? 1 : 0,
                                selectedLivreur,
                                dateLivraisonTF.getPicker().getDate(),
                                dateConfirmationTF.getPicker().getDate()

                        )
                );
                if (responseCode == 200) {
                    Dialog.show("Succés", "Commande modifié avec succes", new Command("Ok"));
                    showBackAndRefresh();
                } else {
                    Dialog.show("Erreur", "Erreur de modification de commande. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            }
        });
    }

    private void showBackAndRefresh() {
        ((AfficherToutCommande) previous).refresh();
        previous.showBack();
    }

    private boolean controleDeSaisie() {


        if (dateTF.getPicker().getDate() == null) {
            Dialog.show("Avertissement", "Veuillez saisir la date", new Command("Ok"));
            return false;
        }


        if (roleTF.getText().equals("")) {
            Dialog.show("Avertissement", "Role vide", new Command("Ok"));
            return false;
        }


        if (dateLivraisonTF.getPicker().getDate() == null) {
            Dialog.show("Avertissement", "Veuillez saisir la dateLivraison", new Command("Ok"));
            return false;
        }


        if (dateConfirmationTF.getPicker().getDate() == null) {
            Dialog.show("Avertissement", "Veuillez saisir la dateConfirmation", new Command("Ok"));
            return false;
        }


        if (selectedUtilisateur == null) {
            Dialog.show("Avertissement", "Veuillez choisir un utilisateur", new Command("Ok"));
            return false;
        }

        if (selectedProduit == null) {
            Dialog.show("Avertissement", "Veuillez choisir un produit", new Command("Ok"));
            return false;
        }

        if (selectedLivreur == null) {
            Dialog.show("Avertissement", "Veuillez choisir un livreur", new Command("Ok"));
            return false;
        }


        return true;
    }
}