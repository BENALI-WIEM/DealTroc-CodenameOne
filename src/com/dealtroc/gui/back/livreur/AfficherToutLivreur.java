package com.dealtroc.gui.back.livreur;


import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.dealtroc.entities.Livreur;
import com.dealtroc.gui.uikit.BaseForm;
import com.dealtroc.services.LivreurService;

import java.util.ArrayList;

public class AfficherToutLivreur extends BaseForm {

    Form previous;

    public static Livreur currentLivreur = null;
    Button addBtn;


    public AfficherToutLivreur(Form previous) {
        super(new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        addGUIs();
        addActions();

        super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    public void refresh() {
        this.removeAll();
        addGUIs();
        addActions();
        this.refreshTheme();
    }

    private void addGUIs() {


        addBtn = new Button("Ajouter");

        this.add(addBtn);


        ArrayList<Livreur> listLivreurs = LivreurService.getInstance().getAll();


        if (listLivreurs.size() > 0) {
            for (Livreur livreur : listLivreurs) {
                Component model = makeLivreurModel(livreur);
                this.add(model);
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {
        addBtn.addActionListener(action -> {
            currentLivreur = null;
            new AjouterLivreur(this).show();
        });

    }

    Label nomLabel, numLabel;


    private Container makeModelWithoutButtons(Livreur livreur) {
        Container livreurModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        livreurModel.setUIID("containerRounded");


        nomLabel = new Label("Nom : " + livreur.getNom());
        nomLabel.setUIID("labelDefault");

        numLabel = new Label("Num : " + livreur.getNum());
        numLabel.setUIID("labelDefault");


        livreurModel.addAll(

                nomLabel, numLabel
        );

        return livreurModel;
    }

    Button editBtn, deleteBtn;
    Container btnsContainer;

    private Component makeLivreurModel(Livreur livreur) {

        Container livreurModel = makeModelWithoutButtons(livreur);

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        editBtn = new Button("Modifier");

        editBtn.addActionListener(action -> {
            currentLivreur = livreur;
            new ModifierLivreur(this).show();
        });

        deleteBtn = new Button("Supprimer");

        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce livreur ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = LivreurService.getInstance().delete(livreur.getId());

                if (responseCode == 200) {
                    currentLivreur = null;
                    dlg.dispose();
                    livreurModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du livreur. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.WEST, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);


        livreurModel.add(btnsContainer);

        return livreurModel;
    }

}