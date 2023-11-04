package com.dealtroc.gui.back.facture;


import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.dealtroc.entities.Facture;
import com.dealtroc.gui.uikit.BaseForm;
import com.dealtroc.services.FactureService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AfficherToutFacture extends BaseForm {

    Form previous;

    public static Facture currentFacture = null;
    Button addBtn;

    TextField searchTF;
    ArrayList<Component> componentModels;


    public AfficherToutFacture(Form previous) {
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
        addBtn.addActionListener(action -> {
            currentFacture = null;
            new AjouterFacture(this).show();
        });

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

    Button editBtn, deleteBtn;
    Container btnsContainer;

    private Component makeFactureModel(Facture facture) {

        Container factureModel = makeModelWithoutButtons(facture);

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        editBtn = new Button("Modifier");

        editBtn.addActionListener(action -> {
            currentFacture = facture;
            new ModifierFacture(this).show();
        });

        deleteBtn = new Button("Supprimer");

        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce facture ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = FactureService.getInstance().delete(facture.getId());

                if (responseCode == 200) {
                    currentFacture = null;
                    dlg.dispose();
                    factureModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du facture. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.WEST, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);


        factureModel.add(btnsContainer);

        return factureModel;
    }

}