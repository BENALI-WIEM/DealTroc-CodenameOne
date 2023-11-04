package com.dealtroc.gui.front.commande;


import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.util.Resources;
import com.dealtroc.entities.Commande;
import com.dealtroc.gui.uikit.BaseForm;
import com.dealtroc.services.CommandeService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AfficherToutCommande extends BaseForm {

    Form previous;

    public static Commande currentCommande = null;


    public AfficherToutCommande(Resources res) {
        super(new BoxLayout(BoxLayout.Y_AXIS));

        Toolbar tb = new Toolbar(true);
        tb.setUIID("CustomToolbar");
        setToolbar(tb);
        getTitleArea().setUIID("Container");
        setTitle("Commande");
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

    ArrayList<Commande> displayListCommande;

    private void addGUIs() {
        ArrayList<Commande> listCommandes = CommandeService.getInstance().getAll();

        if (displayListCommande == null) displayListCommande = CommandeService.getInstance().getAll();

        Container container = new Container();
        container.setPreferredH(250);
        this.add(container);


        Container filtersContainer = new Container(BoxLayout.x());
        filtersContainer.setScrollableX(true);
        filtersContainer.add(new Label("Filters : "));
        filtersContainer.add(makeFilterElementByStatus(listCommandes));
        filtersContainer.add(makeFilterElementByRole(listCommandes));
        filtersContainer.add(makeFilterElementByLivreur(listCommandes));
        filtersContainer.add(makeFilterElementByProduit(listCommandes));

        this.add(filtersContainer);

        if (displayListCommande.size() > 0) {
            for (Commande commande : displayListCommande) {
                Component model = makeCommandeModel(commande);
                this.add(model);
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private PickerComponent makeFilterElementByStatus(ArrayList<Commande> list) {
        PickerComponent pickerComponent = PickerComponent.createStrings("Status");
        ArrayList<String> stringList = new ArrayList<>();

        for (Commande object : list) {
            if (!stringList.contains(String.valueOf(object.getStatus()))) {
                stringList.add(String.valueOf(object.getStatus()));
            }
        }

        String[] strings = new String[stringList.size()];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = stringList.get(i);
        }

        pickerComponent.getPicker().addActionListener(l -> {
            displayListCommande = new ArrayList<>();
            for (Commande object : list) {
                if (String.valueOf(object.getStatus()).equalsIgnoreCase(pickerComponent.getPicker().getText())) {
                    displayListCommande.add(object);
                }
            }
            this.refresh();
        });

        if (list.size() > 0) {
            pickerComponent.getPicker().setStrings(strings);
        } else {
            pickerComponent.getPicker().setStrings("");
        }

        return pickerComponent;
    }

    private PickerComponent makeFilterElementByRole(ArrayList<Commande> list) {
        PickerComponent pickerComponent = PickerComponent.createStrings("Role");
        ArrayList<String> stringList = new ArrayList<>();

        for (Commande object : list) {
            if (!stringList.contains(String.valueOf(object.getRole()))) {
                stringList.add(String.valueOf(object.getRole()));
            }
        }

        String[] strings = new String[stringList.size()];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = stringList.get(i);
        }

        pickerComponent.getPicker().addActionListener(l -> {
            displayListCommande = new ArrayList<>();
            for (Commande object : list) {
                if (String.valueOf(object.getRole()).equalsIgnoreCase(pickerComponent.getPicker().getText())) {
                    displayListCommande.add(object);
                }
            }
            this.refresh();
        });

        if (list.size() > 0) {
            pickerComponent.getPicker().setStrings(strings);
        } else {
            pickerComponent.getPicker().setStrings("");
        }

        return pickerComponent;
    }

    private PickerComponent makeFilterElementByProduit(ArrayList<Commande> list) {
        PickerComponent pickerComponent = PickerComponent.createStrings("Produit");
        ArrayList<String> stringList = new ArrayList<>();

        for (Commande object : list) {
            if (!stringList.contains(object.getProduit().toString().toLowerCase())) {
                stringList.add(String.valueOf(object.getProduit()));
            }
        }

        String[] strings = new String[stringList.size()];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = stringList.get(i);
        }

        pickerComponent.getPicker().addActionListener(l -> {
            displayListCommande = new ArrayList<>();
            for (Commande object : list) {
                if (String.valueOf(object.getProduit()).equalsIgnoreCase(pickerComponent.getPicker().getText())) {
                    displayListCommande.add(object);
                }
            }
            this.refresh();
        });

        if (list.size() > 0) {
            pickerComponent.getPicker().setStrings(strings);
        } else {
            pickerComponent.getPicker().setStrings("");
        }

        return pickerComponent;
    }

    private PickerComponent makeFilterElementByLivreur(ArrayList<Commande> list) {
        PickerComponent pickerComponent = PickerComponent.createStrings("Livreur");
        ArrayList<String> stringList = new ArrayList<>();

        for (Commande object : list) {
            if (object.getLivreur() != null) {
                if (!stringList.contains(object.getLivreur().toString().toLowerCase())) {
                    stringList.add(String.valueOf(object.getLivreur()));
                }
            }
        }

        String[] strings = new String[stringList.size()];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = stringList.get(i);
        }

        pickerComponent.getPicker().addActionListener(l -> {
            displayListCommande = new ArrayList<>();
            for (Commande object : list) {
                if (object.getLivreur() != null) {
                    if (String.valueOf(object.getLivreur()).equalsIgnoreCase(pickerComponent.getPicker().getText())) {
                        displayListCommande.add(object);
                    }
                }
            }
            this.refresh();
        });

        if (list.size() > 0) {
            pickerComponent.getPicker().setStrings(strings);
        } else {
            pickerComponent.getPicker().setStrings("");
        }

        return pickerComponent;
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

        produitLabel = new Label("Produit : " + commande.getProduit().getTitre());
        produitLabel.setUIID("labelDefault");

        commandeModel.addAll(

                utilisateurLabel, produitLabel, dateLabel, roleLabel, statusLabel, livreurLabel, dateLivraisonLabel, dateConfirmationLabel
        );

        return commandeModel;
    }

    Button deleteBtn;
    Container btnsContainer;

    private Component makeCommandeModel(Commande commande) {

        Container commandeModel = makeModelWithoutButtons(commande);

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

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

        btnsContainer.add(BorderLayout.EAST, deleteBtn);


        commandeModel.add(btnsContainer);

        return commandeModel;
    }

}