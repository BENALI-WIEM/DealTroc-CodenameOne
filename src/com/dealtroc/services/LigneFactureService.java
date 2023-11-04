package com.dealtroc.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.dealtroc.entities.Facture;
import com.dealtroc.entities.LigneFacture;
import com.dealtroc.utils.Statics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LigneFactureService {

    public static LigneFactureService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<LigneFacture> listLigneFactures;


    private LigneFactureService() {
        cr = new ConnectionRequest();
    }

    public static LigneFactureService getInstance() {
        if (instance == null) {
            instance = new LigneFactureService();
        }
        return instance;
    }

    public ArrayList<LigneFacture> getAll() {
        listLigneFactures = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/ligneFacture");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listLigneFactures = getList();
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

        return listLigneFactures;
    }

    private ArrayList<LigneFacture> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                LigneFacture ligneFacture = new LigneFacture(
                        (int) Float.parseFloat(obj.get("id").toString()),

                        makeFacture((Map<String, Object>) obj.get("facture")),
                        Float.parseFloat(obj.get("prixInitial").toString()),
                        Float.parseFloat(obj.get("prixVente").toString()),
                        Float.parseFloat(obj.get("prixLivraison").toString()),
                        Float.parseFloat(obj.get("prixTotal").toString()),
                        Float.parseFloat(obj.get("revenu").toString())

                );

                listLigneFactures.add(ligneFacture);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listLigneFactures;
    }

    public static Facture makeFacture(Map<String, Object> obj) {
        if (obj == null) {
            return null;
        }
        Facture facture = new Facture();
        facture.setId((int) Float.parseFloat(obj.get("id").toString()));
        facture.setCommande(FactureService.makeCommande((Map<String, Object>) obj.get("commande")));
        return facture;
    }

    public int add(LigneFacture ligneFacture) {

        cr = new ConnectionRequest();

        cr.setHttpMethod("POST");
        cr.setUrl(Statics.BASE_URL + "/ligneFacture/add");

        cr.addArgument("facture", String.valueOf(ligneFacture.getFacture().getId()));
        cr.addArgument("prixInitial", String.valueOf(ligneFacture.getPrixInitial()));
        cr.addArgument("prixVente", String.valueOf(ligneFacture.getPrixVente()));
        cr.addArgument("prixLivraison", String.valueOf(ligneFacture.getPrixLivraison()));
        cr.addArgument("prixTotal", String.valueOf(ligneFacture.getPrixTotal()));
        cr.addArgument("revenu", String.valueOf(ligneFacture.getRevenu()));


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

    public int edit(LigneFacture ligneFacture) {

        cr = new ConnectionRequest();
        cr.setHttpMethod("POST");
        cr.setUrl(Statics.BASE_URL + "/ligneFacture/edit");
        cr.addArgument("id", String.valueOf(ligneFacture.getId()));

        cr.addArgument("facture", String.valueOf(ligneFacture.getFacture().getId()));
        cr.addArgument("prixInitial", String.valueOf(ligneFacture.getPrixInitial()));
        cr.addArgument("prixVente", String.valueOf(ligneFacture.getPrixVente()));
        cr.addArgument("prixLivraison", String.valueOf(ligneFacture.getPrixLivraison()));
        cr.addArgument("prixTotal", String.valueOf(ligneFacture.getPrixTotal()));
        cr.addArgument("revenu", String.valueOf(ligneFacture.getRevenu()));


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

    public int delete(int ligneFactureId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/ligneFacture/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(ligneFactureId));

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
