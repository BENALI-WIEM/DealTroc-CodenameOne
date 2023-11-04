package com.dealtroc.gui.front.produit;


import com.codename1.components.ImageViewer;
import com.codename1.components.InteractionDialog;
import com.codename1.components.ShareButton;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.dealtroc.MainApp;
import com.dealtroc.entities.Produit;
import com.dealtroc.entities.Saved;
import com.dealtroc.gui.front.commande.AjouterCommande;
import com.dealtroc.gui.front.saved.AfficherToutSaved;
import com.dealtroc.gui.uikit.BaseForm;
import com.dealtroc.services.ProduitService;
import com.dealtroc.services.SavedService;
import com.dealtroc.utils.Statics;

import java.util.ArrayList;
import java.util.Collections;

public class AfficherToutProduit extends BaseForm {

    Form previous;

    Resources theme = UIManager.initFirstTheme("/theme");

    public static Produit currentProduit = null;
    Button addBtn;

    PickerComponent sortPicker;
    ArrayList<Component> componentModels;

    public AfficherToutProduit(Resources res) {
        super(new BoxLayout(BoxLayout.Y_AXIS));

        Toolbar tb = new Toolbar(true);
        tb.setUIID("CustomToolbar");
        setToolbar(tb);
        getTitleArea().setUIID("Container");
        setTitle("Produit");
        getContentPane().setScrollVisible(false);

        super.addSideMenu(res);

        addGUIs();
        addActions();


    }

    public void refresh() {
        this.removeAll();
        addGUIs();
        addActions();
        this.refreshTheme();
    }

    private void addGUIs() {

        Container container = new Container();
        container.setPreferredH(250);
        this.add(container);

        addBtn = new Button("Ajouter");
        this.add(addBtn);

        ArrayList<Produit> listProduits = ProduitService.getInstance().getAll();

        componentModels = new ArrayList<>();

        sortPicker = PickerComponent.createStrings("Image", "Description", "Titre", "Categorie", "Prix", "Utilisateur").label("Trier par");
        sortPicker.getPicker().setSelectedString("");
        sortPicker.getPicker().addActionListener((l) -> {
            if (componentModels.size() > 0) {
                for (Component componentModel : componentModels) {
                    this.removeComponent(componentModel);
                }
            }
            componentModels = new ArrayList<>();
            Statics.compareVar = sortPicker.getPicker().getSelectedString();
            Collections.sort(listProduits);
            for (Produit produit : listProduits) {
                Component model = makeProduitModel(produit);
                this.add(model);
                componentModels.add(model);
            }
            this.revalidate();
        });
        this.add(sortPicker);

        if (listProduits.size() > 0) {
            for (Produit produit : listProduits) {
                Component model = makeProduitModel(produit);
                this.add(model);
                componentModels.add(model);
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {
        addBtn.addActionListener(action -> {
            currentProduit = null;
            new AjouterProduit(this).show();
        });
    }

    Label imageLabel, descriptionLabel, titreLabel, categorieLabel, prixLabel, utilisateurLabel;

    ImageViewer imageIV;


    private Container makeModelWithoutButtons(Produit produit) {
        Container produitModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        produitModel.setUIID("containerRounded");


        imageLabel = new Label("Image : " + produit.getImage());
        imageLabel.setUIID("labelDefault");

        descriptionLabel = new Label("Description : " + produit.getDescription());
        descriptionLabel.setUIID("labelDefault");

        titreLabel = new Label("Titre : " + produit.getTitre());
        titreLabel.setUIID("labelDefault");

        categorieLabel = new Label("Categorie : " + produit.getCategorie());
        categorieLabel.setUIID("labelDefault");

        prixLabel = new Label("Prix : " + produit.getPrix());
        prixLabel.setUIID("labelDefault");

        utilisateurLabel = new Label("Utilisateur : " + produit.getUtilisateur());
        utilisateurLabel.setUIID("labelDefault");

        utilisateurLabel = new Label("Utilisateur : " + produit.getUtilisateur().getEmail());
        utilisateurLabel.setUIID("labelDefault");

        if (produit.getImage() != null) {
            String url = Statics.PRODUIT_IMAGE_URL + produit.getImage();
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

        produitModel.addAll(
                imageIV,
                descriptionLabel, titreLabel, categorieLabel, prixLabel, utilisateurLabel
        );

        return produitModel;
    }

    Container btnsContainer2;
    Button editBtn, deleteBtn;
    Container btnsContainer;


    private Component makeProduitModel(Produit produit) {

        Container produitModel = makeModelWithoutButtons(produit);

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");


        Button btnSave = new Button("Save");
        btnSave.addActionListener(listener -> {
            float prix;
            try {
                prix = Float.parseFloat(produit.getPrix());
            } catch (NumberFormatException ignored) {
                prix = 0;
            }
            int statusCode = SavedService.getInstance().add(
                    new Saved(
                            produit.getImage(),
                            produit.getDescription(),
                            produit.getTitre(),
                            produit.getCategorie(),
                            String.valueOf(prix)
                    )
            );
            System.out.println(statusCode);
            new AfficherToutSaved(MainApp.res).show();
        });

        ShareButton btnPartager = new ShareButton();
        btnPartager.setText("Partager ");
        btnPartager.setTextPosition(LEFT);
        btnPartager.setTextToShare(produit.toStringForShare());


        Button btnCommander = new Button("Commander");
        btnCommander.addActionListener(actionEvent -> new AjouterCommande(this, produit).show());

        btnsContainer.add(BorderLayout.EAST, btnSave);
        btnsContainer.add(BorderLayout.CENTER, btnCommander);
        btnsContainer.add(BorderLayout.WEST, btnPartager);

        produitModel.add(btnsContainer);


        btnsContainer2 = new Container(new BorderLayout());
        btnsContainer2.setUIID("containerButtons");

        editBtn = new Button("Modifier");

        editBtn.addActionListener(action -> {
            currentProduit = produit;
            new ModifierProduit(this).show();
        });

        deleteBtn = new Button("Supprimer");

        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce produit ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = ProduitService.getInstance().delete(produit.getId());

                if (responseCode == 200) {
                    currentProduit = null;
                    dlg.dispose();
                    produitModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du produit. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer2.add(BorderLayout.WEST, editBtn);
        btnsContainer2.add(BorderLayout.EAST, deleteBtn);


        produitModel.add(btnsContainer2);

        return produitModel;
    }


}