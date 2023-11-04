package com.dealtroc.gui.front.livreur;


import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.util.Resources;
import com.dealtroc.entities.Livreur;
import com.dealtroc.gui.uikit.BaseForm;
import com.dealtroc.services.LivreurService;

import java.util.ArrayList;

public class AfficherToutLivreur extends BaseForm {

    Form previous;

    public static Livreur currentLivreur = null;


    public AfficherToutLivreur(Resources res) {
        super(new BoxLayout(BoxLayout.Y_AXIS));

        Toolbar tb = new Toolbar(true);
        tb.setUIID("CustomToolbar");
        setToolbar(tb);
        getTitleArea().setUIID("Container");
        setTitle("Livreur");
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

    private Component makeLivreurModel(Livreur livreur) {


        return makeModelWithoutButtons(livreur);
    }

}