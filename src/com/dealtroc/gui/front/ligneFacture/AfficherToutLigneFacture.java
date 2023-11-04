package com.dealtroc.gui.front.ligneFacture;


import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.util.Resources;
import com.dealtroc.entities.LigneFacture;
import com.dealtroc.gui.uikit.BaseForm;
import com.dealtroc.services.LigneFactureService;

import java.util.ArrayList;

public class AfficherToutLigneFacture extends BaseForm {

    Form previous;

    public static LigneFacture currentLigneFacture = null;

    TextField searchTF;
    ArrayList<Component> componentModels;


    public AfficherToutLigneFacture(Resources res) {
        super(new BoxLayout(BoxLayout.Y_AXIS));

        Toolbar tb = new Toolbar(true);
        tb.setUIID("CustomToolbar");
        setToolbar(tb);
        getTitleArea().setUIID("Container");
        setTitle("LigneFacture");
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

    private Component makeLigneFactureModel(LigneFacture ligneFacture) {

        return makeModelWithoutButtons(ligneFacture);
    }

}