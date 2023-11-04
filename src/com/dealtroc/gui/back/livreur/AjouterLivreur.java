package com.dealtroc.gui.back.livreur;


import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.dealtroc.entities.Livreur;
import com.dealtroc.services.LivreurService;

public class AjouterLivreur extends Form {


    TextField nomTF;
    TextField numTF;
    Label nomLabel;
    Label numLabel;


    Button manageButton;

    Form previous;

    public AjouterLivreur(Form previous) {
        super("Ajouter", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        addGUIs();
        addActions();


        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {


        nomLabel = new Label("Nom : ");
        nomLabel.setUIID("labelDefault");
        nomTF = new TextField();
        nomTF.setHint("Tapez le nom");


        numLabel = new Label("Num : ");
        numLabel.setUIID("labelDefault");
        numTF = new TextField();
        numTF.setHint("Tapez le num");


        manageButton = new Button("Ajouter");


        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(

                nomLabel, nomTF,
                numLabel, numTF,

                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {

        manageButton.addActionListener(action -> {
            if (controleDeSaisie()) {
                int responseCode = LivreurService.getInstance().add(
                        new Livreur(


                                nomTF.getText(),
                                numTF.getText()
                        )
                );
                if (responseCode == 200) {
                    Dialog.show("Succés", "Livreur ajouté avec succes", new Command("Ok"));
                    showBackAndRefresh();
                } else {
                    Dialog.show("Erreur", "Erreur d'ajout de livreur. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            }
        });
    }

    private void showBackAndRefresh() {
        ((AfficherToutLivreur) previous).refresh();
        previous.showBack();
    }

    private boolean controleDeSaisie() {


        if (nomTF.getText().equals("")) {
            Dialog.show("Avertissement", "Nom vide", new Command("Ok"));
            return false;
        }


        if (numTF.getText().equals("")) {
            Dialog.show("Avertissement", "Num vide", new Command("Ok"));
            return false;
        }


        return true;
    }
}