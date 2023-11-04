package com.dealtroc.gui.back.facture;


import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.dealtroc.entities.Commande;
import com.dealtroc.entities.Facture;
import com.dealtroc.services.CommandeService;
import com.dealtroc.services.FactureService;
import com.dealtroc.utils.AlertUtils;

import java.util.ArrayList;

public class ModifierFacture extends Form {


    Facture currentFacture;

    TextField commissionTF;
    TextField statutTF;
    Label commissionLabel;
    Label statutLabel;
    PickerComponent dateFacturationTF;

    ArrayList<Commande> listCommandes;
    PickerComponent commandePC;
    Commande selectedCommande = null;


    Button manageButton;

    Form previous;

    public ModifierFacture(Form previous) {
        super("Modifier", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        currentFacture = AfficherToutFacture.currentFacture;

        addGUIs();
        addActions();


        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {

        String[] commandeStrings;
        int commandeIndex;
        commandePC = PickerComponent.createStrings("").label("Commande");
        listCommandes = CommandeService.getInstance().getAll();
        commandeStrings = new String[listCommandes.size()];
        commandeIndex = 0;
        for (Commande commande : listCommandes) {
            commandeStrings[commandeIndex] = commande.toString();
            commandeIndex++;
        }
        if (listCommandes.size() > 0) {
            commandePC.getPicker().setStrings(commandeStrings);
            commandePC.getPicker().addActionListener(l -> selectedCommande = listCommandes.get(commandePC.getPicker().getSelectedStringIndex()));
        } else {
            commandePC.getPicker().setStrings("");
        }


        dateFacturationTF = PickerComponent.createDate(null).label("DateFacturation");


        commissionLabel = new Label("Commission : ");
        commissionLabel.setUIID("labelDefault");
        commissionTF = new TextField();
        commissionTF.setHint("Tapez le commission");


        statutLabel = new Label("Statut : ");
        statutLabel.setUIID("labelDefault");
        statutTF = new TextField();
        statutTF.setHint("Tapez le statut");


        dateFacturationTF.getPicker().setDate(currentFacture.getDateFacturation());
        commissionTF.setText(String.valueOf(currentFacture.getCommission()));
        statutTF.setText(currentFacture.getStatut());


        commandePC.getPicker().setSelectedString(currentFacture.getCommande().toString());
        selectedCommande = currentFacture.getCommande();


        manageButton = new Button("Modifier");


        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(

                dateFacturationTF,
                commissionLabel, commissionTF,
                statutLabel, statutTF,

                commandePC,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {

        manageButton.addActionListener(action -> {
            if (controleDeSaisie()) {
                int responseCode = FactureService.getInstance().edit(
                        new Facture(
                                currentFacture.getId(),


                                dateFacturationTF.getPicker().getDate(),
                                Float.parseFloat(commissionTF.getText()),
                                statutTF.getText(),
                                selectedCommande

                        )
                );
                if (responseCode == 200) {
                    AlertUtils.makeNotification("Facture modifié avec succes");
                    showBackAndRefresh();
                } else {
                    Dialog.show("Erreur", "Erreur de modification de facture. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            }
        });
    }

    private void showBackAndRefresh() {
        ((AfficherToutFacture) previous).refresh();
        previous.showBack();
    }

    private boolean controleDeSaisie() {


        if (dateFacturationTF.getPicker().getDate() == null) {
            Dialog.show("Avertissement", "Veuillez saisir la dateFacturation", new Command("Ok"));
            return false;
        }


        if (commissionTF.getText().equals("")) {
            Dialog.show("Avertissement", "Commission vide", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(commissionTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", commissionTF.getText() + " n'est pas un nombre valide (commission)", new Command("Ok"));
            return false;
        }


        if (statutTF.getText().equals("")) {
            Dialog.show("Avertissement", "Statut vide", new Command("Ok"));
            return false;
        }


        if (selectedCommande == null) {
            Dialog.show("Avertissement", "Veuillez choisir un commande", new Command("Ok"));
            return false;
        }


        return true;
    }
}