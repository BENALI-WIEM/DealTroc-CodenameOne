package com.dealtroc.gui.back.ligneFacture;


import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.dealtroc.entities.LigneFacture;
import com.dealtroc.gui.uikit.BaseForm;
import com.dealtroc.services.LigneFactureService;

import java.util.ArrayList;

public class AfficherToutLigneFacture extends BaseForm {

    Form previous;

    public static LigneFacture currentLigneFacture = null;
    Button addBtn;

    TextField searchTF;
    ArrayList<Component> componentModels;


    public AfficherToutLigneFacture(Form previous) {
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


        ArrayList<LigneFacture> listLigneFactures = LigneFactureService.getInstance().getAll();
        componentModels = new ArrayList<>();

        searchTF = new TextField("", "Chercher ligneFacture par Revenu");
        searchTF.addDataChangedListener((d, t) -> {
            if (componentModels.size() > 0) {
                for (Component componentModel : componentModels) {
                    this.removeComponent(componentModel);
                }
            }
            componentModels = new ArrayList<>();
            for (LigneFacture ligneFacture : listLigneFactures) {
                if (String.valueOf(ligneFacture.getRevenu()).toLowerCase().startsWith(searchTF.getText().toLowerCase())) {
                    Component model = makeLigneFactureModel(ligneFacture);
                    this.add(model);
                    componentModels.add(model);
                }
            }
            this.revalidate();
        });
        this.add(searchTF);


        if (listLigneFactures.size() > 0) {
            for (LigneFacture ligneFacture : listLigneFactures) {
                Component model = makeLigneFactureModel(ligneFacture);
                this.add(model);
                componentModels.add(model);
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {
        addBtn.addActionListener(action -> {
            currentLigneFacture = null;
            new AjouterLigneFacture(this).show();
        });

    }

    Label factureLabel, prixInitialLabel, prixVenteLabel, prixLivraisonLabel, prixTotalLabel, revenuLabel;


    private Container makeModelWithoutButtons(LigneFacture ligneFacture) {
        Container ligneFactureModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        ligneFactureModel.setUIID("containerRounded");


        factureLabel = new Label("Facture : " + ligneFacture.getFacture());
        factureLabel.setUIID("labelDefault");

        prixInitialLabel = new Label("PrixInitial : " + ligneFacture.getPrixInitial());
        prixInitialLabel.setUIID("labelDefault");

        prixVenteLabel = new Label("PrixVente : " + ligneFacture.getPrixVente());
        prixVenteLabel.setUIID("labelDefault");

        prixLivraisonLabel = new Label("PrixLivraison : " + ligneFacture.getPrixLivraison());
        prixLivraisonLabel.setUIID("labelDefault");

        prixTotalLabel = new Label("PrixTotal : " + ligneFacture.getPrixTotal());
        prixTotalLabel.setUIID("labelDefault");

        revenuLabel = new Label("Revenu : " + ligneFacture.getRevenu());
        revenuLabel.setUIID("labelDefault");

        factureLabel = new Label("Facture : " + ligneFacture.getFacture().getCommande());
        factureLabel.setUIID("labelDefault");


        ligneFactureModel.addAll(

                factureLabel, prixInitialLabel, prixVenteLabel, prixLivraisonLabel, prixTotalLabel, revenuLabel
        );

        return ligneFactureModel;
    }

    Button editBtn, deleteBtn;
    Container btnsContainer;

    private Component makeLigneFactureModel(LigneFacture ligneFacture) {

        Container ligneFactureModel = makeModelWithoutButtons(ligneFacture);

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        editBtn = new Button("Modifier");

        editBtn.addActionListener(action -> {
            currentLigneFacture = ligneFacture;
            new ModifierLigneFacture(this).show();
        });

        deleteBtn = new Button("Supprimer");

        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce ligneFacture ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = LigneFactureService.getInstance().delete(ligneFacture.getId());

                if (responseCode == 200) {
                    currentLigneFacture = null;
                    dlg.dispose();
                    ligneFactureModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du ligneFacture. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.WEST, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);


        ligneFactureModel.add(btnsContainer);

        return ligneFactureModel;
    }

}