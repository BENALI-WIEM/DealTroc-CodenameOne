package com.dealtroc.gui.front.saved;


import com.codename1.components.ImageViewer;
import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.dealtroc.entities.Saved;
import com.dealtroc.gui.uikit.BaseForm;
import com.dealtroc.services.SavedService;
import com.dealtroc.utils.Statics;

import java.util.ArrayList;

public class AfficherToutSaved extends BaseForm {

    Resources theme = UIManager.initFirstTheme("/theme");

    public AfficherToutSaved(Resources res) {
        super(new BoxLayout(BoxLayout.Y_AXIS));

        Toolbar tb = new Toolbar(true);
        tb.setUIID("CustomToolbar");
        setToolbar(tb);
        getTitleArea().setUIID("Container");
        setTitle("Saved");
        getContentPane().setScrollVisible(false);

        super.addSideMenu(res);

        Image img = res.getImage("profile-background.jpg");
        if (img.getHeight() > Display.getInstance().getDisplayHeight() / 3) {
            img = img.scaledHeight(Display.getInstance().getDisplayHeight() / 3);
        }

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


        ArrayList<Saved> listSaveds = SavedService.getInstance().getAll();

        if (listSaveds.size() > 0) {
            for (Saved saved : listSaveds) {
                Component model = makeSavedModel(saved);
                this.add(model);
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {

    }

    Label imageLabel, descriptionLabel, titreLabel, categorieLabel, prixLabel;

    ImageViewer imageIV;


    private Container makeModelWithoutButtons(Saved saved) {
        Container savedModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        savedModel.setUIID("containerRounded");


        imageLabel = new Label("Image : " + saved.getImage());
        imageLabel.setUIID("labelDefault");

        descriptionLabel = new Label("Description : " + saved.getDescription());
        descriptionLabel.setUIID("labelDefault");

        titreLabel = new Label("Titre : " + saved.getTitre());
        titreLabel.setUIID("labelDefault");

        categorieLabel = new Label("Categorie : " + saved.getCategorie());
        categorieLabel.setUIID("labelDefault");

        prixLabel = new Label("Prix : " + saved.getPrix());
        prixLabel.setUIID("labelDefault");

        if (saved.getImage() != null) {
            String url = Statics.PRODUIT_IMAGE_URL + saved.getImage();
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

        savedModel.addAll(
                imageIV,
                descriptionLabel, titreLabel, categorieLabel, prixLabel
        );

        return savedModel;
    }

    Container btnsContainer ;

    private Component makeSavedModel(Saved saved) {
        Container savedModel = makeModelWithoutButtons(saved);

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        Button deleteBtn = new Button("Supprimer");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce favori ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = SavedService.getInstance().delete(saved.getId());

                if (responseCode == 200) {
                    dlg.dispose();
                    savedModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du saved. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.EAST, deleteBtn);

        savedModel.add(btnsContainer);

        return savedModel;
    }


}