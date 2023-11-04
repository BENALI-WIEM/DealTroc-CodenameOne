package com.dealtroc.gui.front.commande;


import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.dealtroc.MainApp;
import com.dealtroc.entities.Commande;
import com.dealtroc.entities.Livreur;
import com.dealtroc.entities.Produit;
import com.dealtroc.entities.Utilisateur;
import com.dealtroc.gui.front.produit.AfficherToutProduit;
import com.dealtroc.services.CommandeService;
import com.dealtroc.services.LivreurService;
import com.dealtroc.services.ProduitService;
import com.dealtroc.services.UtilisateurService;

import java.util.ArrayList;
import java.util.Date;

public class AjouterCommande extends Form {


    TextField roleTF;
    Label roleLabel;
    ArrayList<Utilisateur> listUtilisateurs;
    PickerComponent utilisateurPC;
    PickerComponent dateLivraisonTF;
    Utilisateur selectedUtilisateur = null;
    Produit selectedProduit = null;


    Button manageButton;

    Form previous;

    public AjouterCommande(Form previous, Produit produit) {
        super("Ajouter", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;
        this.selectedProduit = produit;

        addGUIs();
        addActions();


        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {

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

        roleLabel = new Label("Role : ");
        roleLabel.setUIID("labelDefault");
        roleTF = new TextField();
        roleTF.setHint("Tapez le role");

        manageButton = new Button("Ajouter");

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        dateLivraisonTF = PickerComponent.createDate(null).label("DateLivraison");

        
        container.addAll(
                roleLabel, roleTF,dateLivraisonTF,
                utilisateurPC,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {

        manageButton.addActionListener(action -> {
            if (controleDeSaisie()) {
                int responseCode = CommandeService.getInstance().add(
                        new Commande(
                                selectedUtilisateur,
                                selectedProduit,
                                new Date(),
                                roleTF.getText(),
                                0,
                                null,
                                dateLivraisonTF.getPicker().getDate(),
                                new Date()
                        )
                );
                if (responseCode == 200) {
                    Dialog.show("Succés", "Commande ajouté avec succes", new Command("Ok"));
                    showBackAndRefresh();
                } else {
                    Dialog.show("Erreur", "Erreur d'ajout de commande. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            }
        });
    }

    private void showBackAndRefresh() {
        ((AfficherToutProduit) previous).refresh();
        new AfficherToutCommande(MainApp.res).show();
        //previous.showBack();
    }

    private boolean controleDeSaisie() {

        if (roleTF.getText().equals("")) {
            Dialog.show("Avertissement", "Role vide", new Command("Ok"));
            return false;
        }
        
         if (dateLivraisonTF.getPicker().getDate() == null) {
            Dialog.show("Avertissement", "Veuillez saisir la date", new Command("Ok"));
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

        return true;
    }
}