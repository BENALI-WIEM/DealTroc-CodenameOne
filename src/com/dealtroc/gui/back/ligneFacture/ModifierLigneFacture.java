package com.dealtroc.gui.back.ligneFacture;


import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.dealtroc.entities.Facture;
import com.dealtroc.entities.LigneFacture;
import com.dealtroc.services.FactureService;
import com.dealtroc.services.LigneFactureService;
import com.dealtroc.utils.AlertUtils;

import java.util.ArrayList;

public class ModifierLigneFacture extends Form {


    LigneFacture currentLigneFacture;

    TextField prixInitialTF;
    TextField prixVenteTF;
    TextField prixLivraisonTF;
    TextField prixTotalTF;
    TextField revenuTF;
    Label prixInitialLabel;
    Label prixVenteLabel;
    Label prixLivraisonLabel;
    Label prixTotalLabel;
    Label revenuLabel;


    ArrayList<Facture> listFactures;
    PickerComponent facturePC;
    Facture selectedFacture = null;


    Button manageButton;

    Form previous;

    public ModifierLigneFacture(Form previous) {
        super("Modifier", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        currentLigneFacture = AfficherToutLigneFacture.currentLigneFacture;

        addGUIs();
        addActions();


        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {

        String[] factureStrings;
        int factureIndex;
        facturePC = PickerComponent.createStrings("").label("Facture");
        listFactures = FactureService.getInstance().getAll();
        factureStrings = new String[listFactures.size()];
        factureIndex = 0;
        for (Facture facture : listFactures) {
            factureStrings[factureIndex] = facture.toString();
            factureIndex++;
        }
        if (listFactures.size() > 0) {
            facturePC.getPicker().setStrings(factureStrings);
            facturePC.getPicker().addActionListener(l -> selectedFacture = listFactures.get(facturePC.getPicker().getSelectedStringIndex()));
        } else {
            facturePC.getPicker().setStrings("");
        }


        prixInitialLabel = new Label("PrixInitial : ");
        prixInitialLabel.setUIID("labelDefault");
        prixInitialTF = new TextField();
        prixInitialTF.setHint("Tapez le prixInitial");


        prixVenteLabel = new Label("PrixVente : ");
        prixVenteLabel.setUIID("labelDefault");
        prixVenteTF = new TextField();
        prixVenteTF.setHint("Tapez le prixVente");


        prixLivraisonLabel = new Label("PrixLivraison : ");
        prixLivraisonLabel.setUIID("labelDefault");
        prixLivraisonTF = new TextField();
        prixLivraisonTF.setHint("Tapez le prixLivraison");


        prixTotalLabel = new Label("PrixTotal : ");
        prixTotalLabel.setUIID("labelDefault");
        prixTotalTF = new TextField();
        prixTotalTF.setHint("Tapez le prixTotal");


        revenuLabel = new Label("Revenu : ");
        revenuLabel.setUIID("labelDefault");
        revenuTF = new TextField();
        revenuTF.setHint("Tapez le revenu");


        prixInitialTF.setText(String.valueOf(currentLigneFacture.getPrixInitial()));
        prixVenteTF.setText(String.valueOf(currentLigneFacture.getPrixVente()));
        prixLivraisonTF.setText(String.valueOf(currentLigneFacture.getPrixLivraison()));
        prixTotalTF.setText(String.valueOf(currentLigneFacture.getPrixTotal()));
        revenuTF.setText(String.valueOf(currentLigneFacture.getRevenu()));

        facturePC.getPicker().setSelectedString(currentLigneFacture.getFacture().toString());
        selectedFacture = currentLigneFacture.getFacture();


        manageButton = new Button("Modifier");


        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(


                prixInitialLabel, prixInitialTF,
                prixVenteLabel, prixVenteTF,
                prixLivraisonLabel, prixLivraisonTF,
                prixTotalLabel, prixTotalTF,
                revenuLabel, revenuTF,
                facturePC,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {

        manageButton.addActionListener(action -> {
            if (controleDeSaisie()) {
                int responseCode = LigneFactureService.getInstance().edit(
                        new LigneFacture(
                                currentLigneFacture.getId(),


                                selectedFacture,
                                Float.parseFloat(prixInitialTF.getText()),
                                Float.parseFloat(prixVenteTF.getText()),
                                Float.parseFloat(prixLivraisonTF.getText()),
                                Float.parseFloat(prixTotalTF.getText()),
                                Float.parseFloat(revenuTF.getText())

                        )
                );
                if (responseCode == 200) {
                    AlertUtils.makeNotification("LigneFacture modifié avec succes");
                    showBackAndRefresh();
                } else {
                    Dialog.show("Erreur", "Erreur de modification de ligneFacture. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            }
        });
    }

    private void showBackAndRefresh() {
        ((AfficherToutLigneFacture) previous).refresh();
        previous.showBack();
    }

    private boolean controleDeSaisie() {


        if (prixInitialTF.getText().equals("")) {
            Dialog.show("Avertissement", "PrixInitial vide", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(prixInitialTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", prixInitialTF.getText() + " n'est pas un nombre valide (prixInitial)", new Command("Ok"));
            return false;
        }


        if (prixVenteTF.getText().equals("")) {
            Dialog.show("Avertissement", "PrixVente vide", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(prixVenteTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", prixVenteTF.getText() + " n'est pas un nombre valide (prixVente)", new Command("Ok"));
            return false;
        }


        if (prixLivraisonTF.getText().equals("")) {
            Dialog.show("Avertissement", "PrixLivraison vide", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(prixLivraisonTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", prixLivraisonTF.getText() + " n'est pas un nombre valide (prixLivraison)", new Command("Ok"));
            return false;
        }


        if (prixTotalTF.getText().equals("")) {
            Dialog.show("Avertissement", "PrixTotal vide", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(prixTotalTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", prixTotalTF.getText() + " n'est pas un nombre valide (prixTotal)", new Command("Ok"));
            return false;
        }


        if (revenuTF.getText().equals("")) {
            Dialog.show("Avertissement", "Revenu vide", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(revenuTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", revenuTF.getText() + " n'est pas un nombre valide (revenu)", new Command("Ok"));
            return false;
        }


        if (selectedFacture == null) {
            Dialog.show("Avertissement", "Veuillez choisir un facture", new Command("Ok"));
            return false;
        }


        return true;
    }
}