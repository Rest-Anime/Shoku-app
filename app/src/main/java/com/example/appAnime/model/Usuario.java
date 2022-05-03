package com.example.appAnime.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Usuario implements Serializable {

    private String UID;
    private String nombre;
    private String usuario;
    private String foto;
    private Map<String, Map<String, String>> animes;
    private Map<String, Integer> reviews;
    private boolean admin;

    public enum Estado {
        WATCHING, HOLD, DROPPED, TO_WATCH;
    }

    public Usuario(String usuario) {
        this.nombre = null;
        this.usuario = usuario;
        this.foto = null;
        this.animes = new HashMap<>();
        this.reviews = new HashMap<>();
        this.admin = false;
    }

    public Usuario() {
    }

    public Usuario(String nombre, String usuario, String foto,
                   Map<String, Map<String, String>> animes, Map<String, Integer> reviews,
                   boolean admin) {
        this.nombre = nombre;
        this.usuario = usuario;
        this.foto = foto;
        this.animes = animes;
        this.reviews = reviews;
        this.admin = admin;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Map<String, Map<String, String>> getAnimes() {
        return animes;
    }

    public void setAnimes(Map<String, Map<String, String>> animes) {
        this.animes = animes;
    }

    public Map<String, Integer> getReviews() {
        return reviews;
    }

    public void setReviews(Map<String, Integer> reviews) {
        this.reviews = reviews;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public void addAnimeToList(String animeID, Estado estado, Integer rate) {
        Map<String, String> contenido = new HashMap<>();
        contenido.put("estado", estado.toString());
        contenido.put("rate", rate.toString());
        this.animes.put(animeID, contenido);
    }

    public void removeAnimeFromList(String animeID) {
        this.animes.remove(animeID);
    }

    public void addReview(String reviewID, Integer like) {
        this.reviews.put(reviewID, like);
    }

    public void removeReview(String reviewID) {
        this.reviews.remove(reviewID);
    }

    public Map<String, Object> setFirestore() {
        Map<String, Object> data = new HashMap<>();
        data.put("usuario", this.usuario);
        data.put("nombre", this.nombre);
        data.put("animes", this.animes);
        data.put("reviews", this.reviews);
        data.put("admin", this.admin);
        data.put("foto", this.foto);
        return data;
    }


}
