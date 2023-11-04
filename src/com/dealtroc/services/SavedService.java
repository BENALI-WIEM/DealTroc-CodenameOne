package com.dealtroc.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.dealtroc.entities.Saved;
import com.dealtroc.entities.Utilisateur;
import com.dealtroc.utils.Statics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SavedService {

    public static SavedService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Saved> listSaveds;


    private SavedService() {
        cr = new ConnectionRequest();
    }

    public static SavedService getInstance() {
        if (instance == null) {
            instance = new SavedService();
        }
        return instance;
    }

    public ArrayList<Saved> getAll() {
        listSaveds = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/saved");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listSaveds = getList();
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

        return listSaveds;
    }

    private ArrayList<Saved> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Saved saved = new Saved(
                        (int) Float.parseFloat(obj.get("id").toString()),

                        (String) obj.get("image"),
                        (String) obj.get("description"),
                        (String) obj.get("titre"),
                        (String) obj.get("categorie"),
                        (String) obj.get("prix")

                );

                listSaveds.add(saved);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listSaveds;
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

    public int add(Saved saved) {

        MultipartRequest cr = new MultipartRequest();

        cr.setHttpMethod("POST");
        cr.setUrl(Statics.BASE_URL + "/saved/add");

        cr.addArgumentNoEncoding("image", saved.getImage());
        cr.addArgumentNoEncoding("description", saved.getDescription());
        cr.addArgumentNoEncoding("titre", saved.getTitre());
        cr.addArgumentNoEncoding("categorie", saved.getCategorie());
        cr.addArgumentNoEncoding("prix", saved.getPrix());


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

    public int edit(Saved saved, boolean imageEdited) {

        MultipartRequest cr = new MultipartRequest();
        cr.setFilename("file", "Saved.jpg");

        cr.setHttpMethod("POST");
        cr.setUrl(Statics.BASE_URL + "/saved/edit");
        cr.addArgumentNoEncoding("id", String.valueOf(saved.getId()));

        if (imageEdited) {
            try {
                cr.addData("file", saved.getImage(), "image/jpeg");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            cr.addArgumentNoEncoding("image", saved.getImage());
        }

        cr.addArgumentNoEncoding("image", saved.getImage());
        cr.addArgumentNoEncoding("description", saved.getDescription());
        cr.addArgumentNoEncoding("titre", saved.getTitre());
        cr.addArgumentNoEncoding("categorie", saved.getCategorie());
        cr.addArgumentNoEncoding("prix", saved.getPrix());


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

    public int delete(int savedId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/saved/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(savedId));

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
