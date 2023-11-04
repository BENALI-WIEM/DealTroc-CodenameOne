package com.dealtroc.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.dealtroc.entities.Commande;
import com.dealtroc.entities.Livreur;
import com.dealtroc.entities.Produit;
import com.dealtroc.entities.Utilisateur;
import com.dealtroc.utils.Statics;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommandeService {

    public static CommandeService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Commande> listCommandes;


    private CommandeService() {
        cr = new ConnectionRequest();
    }

    public static CommandeService getInstance() {
        if (instance == null) {
            instance = new CommandeService();
        }
        return instance;
    }

    public ArrayList<Commande> getAll() {
        listCommandes = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/commande");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listCommandes = getList();
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

        return listCommandes;
    }

    private ArrayList<Commande> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Commande commande = new Commande(
                        (int) Float.parseFloat(obj.get("id").toString()),

                        makeUtilisateur((Map<String, Object>) obj.get("utilisateur")),
                        makeProduit((Map<String, Object>) obj.get("produit")),
                        new SimpleDateFormat("dd-MM-yyyy").parse((String) obj.get("date")),
                        (String) obj.get("role"),
                        (int) Float.parseFloat(obj.get("status").toString()),
                        makeLivreur((Map<String, Object>) obj.get("livreur")),
                        new SimpleDateFormat("dd-MM-yyyy").parse((String) obj.get("dateLivraison")),
                        new SimpleDateFormat("dd-MM-yyyy").parse((String) obj.get("dateConfirmation"))

                );

                listCommandes.add(commande);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return listCommandes;
    }

    public Utilisateur makeUtilisateur(Map<String, Object> obj) {
        if (obj == null) {
            return null;
        }
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId((int) Float.parseFloat(obj.get("id").toString()));
        utilisateur.setEmail((String) obj.get("email"));
        return utilisateur;
    }

    public static Produit makeProduit(Map<String, Object> obj) {
        if (obj == null) {
            return null;
        }
        Produit produit = new Produit();
        produit.setId((int) Float.parseFloat(obj.get("id").toString()));
        produit.setDescription((String) obj.get("description"));
        produit.setTitre((String) obj.get("titre"));
        return produit;
    }

    public Livreur makeLivreur(Map<String, Object> obj) {
        if (obj == null) {
            return null;
        }
        Livreur livreur = new Livreur();
        livreur.setId((int) Float.parseFloat(obj.get("id").toString()));
        livreur.setNom((String) obj.get("nom"));
        return livreur;
    }

    public int add(Commande commande) {

        cr = new ConnectionRequest();

        cr.setHttpMethod("POST");
        cr.setUrl(Statics.BASE_URL + "/commande/add");

        cr.addArgument("utilisateur", String.valueOf(commande.getUtilisateur().getId()));
        cr.addArgument("produit", String.valueOf(commande.getProduit().getId()));
        cr.addArgument("date", new SimpleDateFormat("dd-MM-yyyy").format(commande.getDate()));
        cr.addArgument("role", commande.getRole());
        cr.addArgument("status", String.valueOf(commande.getStatus()));
        cr.addArgument("livreur", commande.getLivreur() != null ? String.valueOf(commande.getLivreur().getId()) : "1");
        cr.addArgument("dateLivraison", new SimpleDateFormat("dd-MM-yyyy").format(commande.getDateLivraison()));
        cr.addArgument("dateConfirmation", new SimpleDateFormat("dd-MM-yyyy").format(commande.getDateConfirmation()));


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

    public int edit(Commande commande) {

        cr = new ConnectionRequest();
        cr.setHttpMethod("POST");
        cr.setUrl(Statics.BASE_URL + "/commande/edit");
        cr.addArgument("id", String.valueOf(commande.getId()));

        cr.addArgument("utilisateur", String.valueOf(commande.getUtilisateur().getId()));
        cr.addArgument("produit", String.valueOf(commande.getProduit().getId()));
        cr.addArgument("date", new SimpleDateFormat("dd-MM-yyyy").format(commande.getDate()));
        cr.addArgument("role", commande.getRole());
        cr.addArgument("status", String.valueOf(commande.getStatus()));
        cr.addArgument("livreur", String.valueOf(commande.getLivreur().getId()));
        cr.addArgument("dateLivraison", new SimpleDateFormat("dd-MM-yyyy").format(commande.getDateLivraison()));
        cr.addArgument("dateConfirmation", new SimpleDateFormat("dd-MM-yyyy").format(commande.getDateConfirmation()));


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

    public int delete(int commandeId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/commande/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(commandeId));

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
