package com.dealtroc.gui.front.facture;


import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.util.Resources;
import com.dealtroc.entities.Facture;
import com.dealtroc.gui.uikit.BaseForm;
import com.dealtroc.services.FactureService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AfficherToutFacture extends BaseForm {

    Form previous;

    public static Facture currentFacture = null;

    TextField searchTF;
    ArrayList<Component> componentModels;


    public AfficherToutFacture(Resources res) {
        super(new BoxLayout(BoxLayout.Y_AXIS));

        Toolbar tb = new Toolbar(true);
        tb.setUIID("CustomToolbar");
        setToolbar(tb);
        getTitleArea().setUIID("Container");
        setTitle("Facture");
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


        ArrayList<Facture> listFactures = FactureService.getInstance().getAll();
        componentModels = new ArrayList<>();

        searchTF = new TextField("", "Chercher facture par Commission");
        searchTF.addDataChangedListener((d, t) -> {
            if (componentModels.size() > 0) {
                for (Component componentModel : componentModels) {
                    this.removeComponent(componentModel);
                }
            }
            componentModels = new ArrayList<>();
            for (Facture facture : listFactures) {
                if (String.valueOf(facture.getCommission()).toLowerCase().startsWith(searchTF.getText().toLowerCase())) {
                    Component model = makeFactureModel(facture);
                    this.add(model);
                    componentModels.add(model);
                }
            }
            this.revalidate();
        });
        this.add(searchTF);


        if (listFactures.size() > 0) {
            for (Facture facture : listFactures) {
                Component model = makeFactureModel(facture);
                this.add(model);
                componentModels.add(model);
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {
    }

    Label dateFacturationLabel, commissionLabel, statutLabel, commandeLabel;


    private Container makeModelWithoutButtons(Facture facture) {
        Container factureModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        factureModel.setUIID("containerRounded");


        dateFacturationLabel = new Label("DateFacturation : " + new SimpleDateFormat("dd-MM-yyyy").format(facture.getDateFacturation()));
        dateFacturationLabel.setUIID("labelDefault");

        commissionLabel = new Label("Commission : " + facture.getCommission());
        commissionLabel.setUIID("labelDefault");

        statutLabel = new Label("Statut : " + facture.getStatut());
        statutLabel.setUIID("labelDefault");

        commandeLabel = new Label(String.valueOf(facture.getCommande()));
        commandeLabel.setUIID("labelDefault");

        factureModel.addAll(
                dateFacturationLabel, commissionLabel, statutLabel, commandeLabel
        );

        return factureModel;
    }

    private Component makeFactureModel(Facture facture) {


        return makeModelWithoutButtons(facture);
    }

}