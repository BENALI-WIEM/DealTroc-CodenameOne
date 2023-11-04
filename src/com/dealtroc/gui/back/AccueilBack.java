package com.dealtroc.gui.back;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.dealtroc.gui.Login;

public class AccueilBack extends Form {

    Resources theme = UIManager.initFirstTheme("/theme");
    Label label;
    Form previous;
    public static Form accueilForm;

    public AccueilBack(Form previous) {
        super("Menu", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;
        accueilForm = this;
        addGUIs();
    }

    private void addGUIs() {
        label = new Label("Espace administrateur"/*MainApp.getSession().getEmail()*/);
        label.setUIID("links");
        Button btnDeconnexion = new Button();
        btnDeconnexion.setMaterialIcon(FontImage.MATERIAL_ARROW_FORWARD);
        btnDeconnexion.addActionListener(action -> {
            Login.loginForm.showBack();
        });

        Container userContainer = new Container(new BorderLayout());
        userContainer.setUIID("userContainer");
        userContainer.add(BorderLayout.CENTER, label);
        userContainer.add(BorderLayout.EAST, btnDeconnexion);

        Container menuContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        menuContainer.addAll(
                userContainer,
                makeProduitsButton(),
                makeFacturesButton(),
                makeLigneFacturesButton(),
                makeCommandesNonFactButton(),
                makeCommandesFactButton(),
                makeLivreursButton()


        );

        this.add(menuContainer);
    }

    private Button makeProduitsButton() {
        Button button = new Button("Produits");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_ARCHIVE);
        button.addActionListener(action -> new com.dealtroc.gui.back.produit.AfficherToutProduit(this).show());
        return button;
    }

    private Button makeFacturesButton() {
        Button button = new Button("Factures");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_BOOKMARK);
        button.addActionListener(action -> new com.dealtroc.gui.back.facture.AfficherToutFacture(this).show());
        return button;
    }

    private Button makeLigneFacturesButton() {
        Button button = new Button("LigneFactures");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_MESSAGE);
        button.addActionListener(action -> new com.dealtroc.gui.back.ligneFacture.AfficherToutLigneFacture(this).show());
        return button;
    }

    private Button makeCommandesNonFactButton() {
        Button button = new Button("Commandes non confirmés");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_CLOSE);
        button.addActionListener(action -> new com.dealtroc.gui.back.commande.AfficherToutCommande(this, false).show());
        return button;
    }

    private Button makeCommandesFactButton() {
        Button button = new Button("Commandes confirmés");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_CHECK);
        button.addActionListener(action -> new com.dealtroc.gui.back.commande.AfficherToutCommande(this, true).show());
        return button;
    }

    private Button makeLivreursButton() {
        Button button = new Button("Livreurs");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_BIKE_SCOOTER);
        button.addActionListener(action -> new com.dealtroc.gui.back.livreur.AfficherToutLivreur(this).show());
        return button;
    }

}
