package com.dealtroc.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.dealtroc.entities.Commande;
import com.dealtroc.entities.Facture;
import com.dealtroc.utils.Statics;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FactureService {

    public static FactureService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Facture> listFactures;


    private FactureService() {
        cr = new ConnectionRequest();
    }

    public static FactureService getInstance() {
        if (instance == null) {
            instance = new FactureService();
        }
        return instance;
    }

    public ArrayList<Facture> getAll() {
        listFactures = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/facture");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listFactures = getList();
                }

                cr.removeResponseListener(this);
            }
        });

        try {
            cr.setDisposeOnCompletion(new InfiniteProgress().showInfiniteBlocking());
            NetworkManager.getInstance().addToQueueAndWait(cr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listFactures;
    }

    private ArrayList<Facture> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Facture facture = new Facture(
                        (int) Float.parseFloat(obj.get("id").toString()),
                        new SimpleDateFormat("dd-MM-yyyy").parse((String) obj.get("dateFacturation")),
                        Float.parseFloat(obj.get("commission").toString()),
                        (String) obj.get("statut"),
                        makeCommande((Map<String, Object>) obj.get("commande"))

                );

                listFactures.add(facture);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return listFactures;
    }

    public static Commande makeCommande(Map<String, Object> obj) {
        if (obj == null) {
            return null;
        }
        Commande commande = new Commande();
        commande.setId((int) Float.parseFloat(obj.get("id").toString()));
        try {
            commande.setProduit(CommandeService.makeProduit((Map<String, Object>) obj.get("produit")));
        } catch (ClassCastException ignored) {

        }
        return commande;
    }

    public int add(Facture facture) {

        cr = new ConnectionRequest();

        cr.setHttpMethod("POST");
        cr.setUrl(Statics.BASE_URL + "/facture/add");

        cr.addArgument("dateFacturation", new SimpleDateFormat("dd-MM-yyyy").format(facture.getDateFacturation()));
        cr.addArgument("commission", String.valueOf(facture.getCommission()));
        cr.addArgument("statut", facture.getStatut());
        cr.addArgument("commande", String.valueOf(facture.getCommande().getId()));


        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultCode = cr.getResponseCode();
                cr.removeResponseListener(this);
            }
        });
        try {
            cr.setDisposeOnCompletion(new InfiniteProgress().showInfiniteBlocking());
            NetworkManager.getInstance().addToQueueAndWait(cr);
        } catch (Exception ignored) {

        }
        return resultCode;
    }

    public int edit(Facture facture) {

        cr = new ConnectionRequest();
        cr.setHttpMethod("POST");
        cr.setUrl(Statics.BASE_URL + "/facture/edit");
        cr.addArgument("id", String.valueOf(facture.getId()));

        cr.addArgument("dateFacturation", new SimpleDateFormat("dd-MM-yyyy").format(facture.getDateFacturation()));
        cr.addArgument("commission", String.valueOf(facture.getCommission()));
        cr.addArgument("statut", facture.getStatut());
        cr.addArgument("commande", String.valueOf(facture.getCommande().getId()));


        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultCode = cr.getResponseCode();
                cr.removeResponseListener(this);
            }
        });
        try {
            cr.setDisposeOnCompletion(new InfiniteProgress().showInfiniteBlocking());
            NetworkManager.getInstance().addToQueueAndWait(cr);
        } catch (Exception ignored) {

        }
        return resultCode;
    }

    public int delete(int factureId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/facture/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(factureId));

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                cr.removeResponseListener(this);
            }
        });

        try {
            cr.setDisposeOnCompletion(new InfiniteProgress().showInfiniteBlocking());
            NetworkManager.getInstance().addToQueueAndWait(cr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cr.getResponseCode();
    }
}
