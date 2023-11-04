package com.dealtroc.gui.front.produit;


import com.codename1.capture.Capture;
import com.codename1.components.ImageViewer;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.dealtroc.entities.Produit;
import com.dealtroc.entities.Utilisateur;
import com.dealtroc.gui.front.produit.AfficherToutProduit;
import com.dealtroc.services.ProduitService;
import com.dealtroc.services.UtilisateurService;
import com.dealtroc.utils.Statics;

import java.io.IOException;
import java.util.ArrayList;

public class ModifierProduit extends Form {


    Resources theme = UIManager.initFirstTheme("/theme");
    String selectedImage;
    boolean imageEdited = false;


    Produit currentProduit;

    TextField imageTF;
    TextField descriptionTF;
    TextField titreTF;
    TextField categorieTF;
    TextField prixTF;
    Label imageLabel;
    Label descriptionLabel;
    Label titreLabel;
    Label categorieLabel;
    Label prixLabel;


    ArrayList<Utilisateur> listUtilisateurs;
    PickerComponent utilisateurPC;
    Utilisateur selectedUtilisateur = null;


    ImageViewer imageIV;
    Button selectImageButton;

    Button manageButton;

    Form previous;

    public ModifierProduit(Form previous) {
        super("Modifier", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        currentProduit = com.dealtroc.gui.front.produit.AfficherToutProduit.currentProduit;

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


        descriptionLabel = new Label("Description : ");
        descriptionLabel.setUIID("labelDefault");
        descriptionTF = new TextField();
        descriptionTF.setHint("Tapez le description");


        titreLabel = new Label("Titre : ");
        titreLabel.setUIID("labelDefault");
        titreTF = new TextField();
        titreTF.setHint("Tapez le titre");


        categorieLabel = new Label("Categorie : ");
        categorieLabel.setUIID("labelDefault");
        categorieTF = new TextField();
        categorieTF.setHint("Tapez le categorie");


        prixLabel = new Label("Prix : ");
        prixLabel.setUIID("labelDefault");
        prixTF = new TextField();
        prixTF.setHint("Tapez le prix");


        imageLabel = new Label("Image : ");
        imageLabel.setUIID("labelDefault");
        selectImageButton = new Button("Ajouter une image");


        descriptionTF.setText(currentProduit.getDescription());
        titreTF.setText(currentProduit.getTitre());
        categorieTF.setText(currentProduit.getCategorie());
        prixTF.setText(currentProduit.getPrix());


        utilisateurPC.getPicker().setSelectedString(currentProduit.getUtilisateur().getEmail());
        selectedUtilisateur = currentProduit.getUtilisateur();


        if (currentProduit.getImage() != null) {
            selectedImage = currentProduit.getImage();
            String url = Statics.PRODUIT_IMAGE_URL + currentProduit.getImage();
            Image image = URLImage.createToStorage(
                    EncodedImage.createFromImage(theme.getImage("profile-pic.jpg").fill(1100, 500), false),
                    url,
                    url,
                    URLImage.RESIZE_SCALE
            );
            imageIV = new ImageViewer(image);
        } else {
            imageIV = new ImageViewer(theme.getImage("profile-pic.jpg").fill(1100, 500));
        }
        imageIV.setFocusable(false);


        manageButton = new Button("Modifier");


        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                imageLabel, imageIV,
                selectImageButton,

                descriptionLabel, descriptionTF,
                titreLabel, titreTF,
                categorieLabel, categorieTF,
                prixLabel, prixTF,

                utilisateurPC,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {

        selectImageButton.addActionListener(a -> {
            selectedImage = Capture.capturePhoto(900, -1);
            try {
                imageEdited = true;
                imageIV.setImage(Image.createImage(selectedImage));
            } catch (IOException e) {
                e.printStackTrace();
            }
            selectImageButton.setText("Modifier l'image");
        });

        manageButton.addActionListener(action -> {
            if (controleDeSaisie()) {
                int responseCode = ProduitService.getInstance().edit(
                        new Produit(
                                currentProduit.getId(),


                                selectedImage,
                                descriptionTF.getText(),
                                titreTF.getText(),
                                categorieTF.getText(),
                                prixTF.getText(),
                                selectedUtilisateur

                        ), imageEdited
                );
                if (responseCode == 200) {
                    Dialog.show("Succés", "Produit modifié avec succes", new Command("Ok"));
                    showBackAndRefresh();
                } else {
                    Dialog.show("Erreur", "Erreur de modification de produit. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            }
        });
    }

    private void showBackAndRefresh() {
        ((AfficherToutProduit) previous).refresh();
        previous.showBack();
    }

    private boolean controleDeSaisie() {


        if (descriptionTF.getText().equals("")) {
            Dialog.show("Avertissement", "Description vide", new Command("Ok"));
            return false;
        }


        if (titreTF.getText().equals("")) {
            Dialog.show("Avertissement", "Titre vide", new Command("Ok"));
            return false;
        }


        if (categorieTF.getText().equals("")) {
            Dialog.show("Avertissement", "Categorie vide", new Command("Ok"));
            return false;
        }


        if (prixTF.getText().equals("")) {
            Dialog.show("Avertissement", "Prix vide", new Command("Ok"));
            return false;
        }


        if (selectedUtilisateur == null) {
            Dialog.show("Avertissement", "Veuillez choisir un utilisateur", new Command("Ok"));
            return false;
        }


        if (selectedImage == null) {
            Dialog.show("Avertissement", "Veuillez choisir une image", new Command("Ok"));
            return false;
        }


        return true;
    }
}