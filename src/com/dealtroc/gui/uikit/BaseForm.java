package com.dealtroc.gui.uikit;

import com.codename1.components.ScaleImageLabel;
import com.codename1.ui.*;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.layouts.Layout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.util.Resources;
import com.dealtroc.gui.Login;
import com.dealtroc.gui.front.commande.AfficherToutCommande;
import com.dealtroc.gui.front.facture.AfficherToutFacture;
import com.dealtroc.gui.front.ligneFacture.AfficherToutLigneFacture;
import com.dealtroc.gui.front.livreur.AfficherToutLivreur;
import com.dealtroc.gui.front.produit.AfficherToutProduit;
import com.dealtroc.gui.front.saved.AfficherToutSaved;


/**
 * Base class for the forms with common functionality
 *
 * @author Shai Almog
 */
public class BaseForm extends Form {

    public BaseForm() {
    }

    public BaseForm(Layout contentPaneLayout) {
        super(contentPaneLayout);
    }

    public BaseForm(String title, Layout contentPaneLayout) {
        super(title, contentPaneLayout);
    }


    public Component createLineSeparator() {
        Label separator = new Label("", "WhiteSeparator");
        separator.setShowEvenIfBlank(true);
        return separator;
    }

    public Component createLineSeparator(int color) {
        Label separator = new Label("", "WhiteSeparator");
        separator.getUnselectedStyle().setBgColor(color);
        separator.getUnselectedStyle().setBgTransparency(255);
        separator.setShowEvenIfBlank(true);
        return separator;
    }

    protected void addSideMenu(Resources res) {
        Toolbar tb = getToolbar();
        tb.setUIID("CustomToolbar");

        Image img = res.getImage("profile-background.jpg");
        if (img.getHeight() > Display.getInstance().getDisplayHeight() / 3) {
            img = img.scaledHeight(Display.getInstance().getDisplayHeight() / 3);
        }
        ScaleImageLabel sl = new ScaleImageLabel(img);
        sl.setUIID("BottomPad");
        sl.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED_FILL);

        tb.addComponentToSideMenu(LayeredLayout.encloseIn(
                sl,
                FlowLayout.encloseCenterBottom(
                        new Label(res.getImage("profile-pic.jpg"), "PictureWhiteBackgrond"))
        ));

        tb.addMaterialCommandToSideMenu("Profile", FontImage.MATERIAL_SETTINGS, e -> new ProfileForm(res).show());
        tb.addMaterialCommandToSideMenu("Produit", FontImage.MATERIAL_ARCHIVE, e -> new AfficherToutProduit(res).show());
        tb.addMaterialCommandToSideMenu("Produits favoris", FontImage.MATERIAL_FAVORITE, e -> new AfficherToutSaved(res).show());
        tb.addMaterialCommandToSideMenu("Facture", FontImage.MATERIAL_BOOK, e -> new AfficherToutFacture(res).show());
        tb.addMaterialCommandToSideMenu("LigneFacture", FontImage.MATERIAL_MENU_BOOK, e -> new AfficherToutLigneFacture(res).show());
        tb.addMaterialCommandToSideMenu("Commandes", FontImage.MATERIAL_DELIVERY_DINING, e -> new AfficherToutCommande(res).show());
        tb.addMaterialCommandToSideMenu("Livreur", FontImage.MATERIAL_DIRECTIONS_BIKE, e -> new AfficherToutLivreur(res).show());


        tb.addMaterialCommandToSideMenu("Logout", FontImage.MATERIAL_EXIT_TO_APP, e -> new Login().show());
    }
}
