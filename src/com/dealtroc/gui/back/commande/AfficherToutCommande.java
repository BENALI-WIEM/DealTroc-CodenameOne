package com.dealtroc.gui.back.commande;


import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.dealtroc.entities.Commande;
import com.dealtroc.gui.uikit.BaseForm;
import com.dealtroc.services.CommandeService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AfficherToutCommande extends BaseForm {

    Form previous;

    public static Commande currentCommande = null;

    boolean estFacturee;

    public AfficherToutCommande(Form previous, boolean estFacturee) {
        super(new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;
        this.estFacturee = estFacturee;

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


        ArrayList<Commande> listCommandes = new ArrayList<>();

        for (Commande commande : CommandeService.getInstance().getAll()) {
            if (estFacturee) {
                if (commande.getStatus() == 1) {
                    listCommandes.add(commande);
                }
            } else {
                if (commande.getStatus() == 0) {
                    listCommandes.add(commande);
                }
            }
        }

        if (listCommandes.size() > 0) {
            for (Commande commande : listCommandes) {
                Component model = makeCommandeModel(commande);
                this.add(model);
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {

    }

    Label utilisateurLabel, produitLabel, dateLabel, roleLabel, statusLabel, livreurLabel, dateLivraisonLabel, dateConfirmationLabel;


    private Container makeModelWithoutButtons(Commande commande) {
        Container commandeModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        commandeModel.setUIID("containerRounded");


        utilisateurLabel = new Label("Utilisateur : " + commande.getUtilisateur());
        utilisateurLabel.setUIID("labelDefault");

        produitLabel = new Label("Produit : " + commande.getProduit());
        produitLabel.setUIID("labelDefault");

        dateLabel = new Label("Date : " + new SimpleDateFormat("dd-MM-yyyy").format(commande.getDate()));
        dateLabel.setUIID("labelDefault");

        roleLabel = new Label("Role : " + commande.getRole());
        roleLabel.setUIID("labelDefault");

        statusLabel = new Label("Status : " + (commande.getStatus() == 1 ? "Confirmé" : "Non confirmé"));
        statusLabel.setUIID("labelDefault");

        livreurLabel = new Label("Livreur : " + commande.getLivreur());
        livreurLabel.setUIID("labelDefault");

        dateLivraisonLabel = new Label("DateLivraison : " + new SimpleDateFormat("dd-MM-yyyy").format(commande.getDateLivraison()));
        dateLivraisonLabel.setUIID("labelDefault");

        dateConfirmationLabel = new Label("DateConfirmation : " + new SimpleDateFormat("dd-MM-yyyy").format(commande.getDateConfirmation()));
        dateConfirmationLabel.setUIID("labelDefault");

        utilisateurLabel = new Label("Utilisateur : " + commande.getUtilisateur().getEmail());
        utilisateurLabel.setUIID("labelDefault");

        produitLabel = new Label("Produit : " + commande.getProduit().getDescription());
        produitLabel.setUIID("labelDefault");

        livreurLabel = new Label("Livreur : " + commande.getLivreur().getNom());
        livreurLabel.setUIID("labelDefault");


        commandeModel.addAll(

                utilisateurLabel, produitLabel, dateLabel, roleLabel, statusLabel, livreurLabel, dateLivraisonLabel, dateConfirmationLabel
        );

        return commandeModel;
    }

    Button editBtn, deleteBtn;
    Container btnsContainer;

    private Component makeCommandeModel(Commande commande) {

        Container commandeModel = makeModelWithoutButtons(commande);

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        editBtn = new Button("Modifier");

        editBtn.addActionListener(action -> {
            currentCommande = commande;
            new ModifierCommande(this).show();
        });

        deleteBtn = new Button("Supprimer");

        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce commande ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = CommandeService.getInstance().delete(commande.getId());

                if (responseCode == 200) {
                    currentCommande = null;
                    dlg.dispose();
                    commandeModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du commande. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.WEST, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);


        commandeModel.add(btnsContainer);

        return commandeModel;
    }

}