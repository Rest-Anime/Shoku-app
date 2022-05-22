package com.example.appAnime.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Usuario implements Serializable {

    private String UID;
    private String usuario;
    private String foto;
    /**
     * animes
     * ID:
     * estado: string;
     * rate: string; (Int.parse(string));
     */
    private HashMap<String, String> animes;

    /**
     * reviews:
     * ID: true/false (like/dislike);
     */
    private HashMap<String, String> reviews;

    private boolean admin;

    public Usuario(String usuario) {
        this.usuario = usuario;
        this.foto = null;
        this.animes = new HashMap<>();
        this.reviews = new HashMap<>();
        this.admin = false;
    }

    public Usuario() {
    }

    public Usuario(String nombre, String usuario, String foto,
                   HashMap<String, String> animes, HashMap<String, String> reviews,
                   boolean admin) {
        this.usuario = usuario;
        this.foto = foto;
        this.animes = animes;
        this.reviews = reviews;
        this.admin = admin;

    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
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


    public HashMap<String, String> getAnimes() {
        return animes;
    }

    public void setAnimes(HashMap<String, String> animes) {
        this.animes = animes;
    }

    public HashMap<String, String> getReviews() {
        return reviews;
    }

    public void setReviews(HashMap<String, String> reviews) {
        this.reviews = reviews;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    /**
     * Añade una review a la lista y si se ha dado like o dislike.
     *
     * @param reviewID El documento al que hace referencia en la coleccion de reviews.
     * @param like     un booleano que representa un like(true) o dislike(false).
     */
    public void addReview(String reviewID, String like) {
        this.reviews.put(reviewID, like);
    }

    /**
     * Elimina del diccionario la entrada que sea igual a la dada por parametro.
     *
     * @param reviewID El documento al que hace referencia en la coleccion de reviews.
     */
    public void removeReview(String reviewID) {
        this.reviews.remove(reviewID);
    }

    /**
     * Mapeo de Objeto para insertar/actualizar BBDD.
     *
     * @return Diccionario con los datos para crear/actualizar Entrada en Firestore.
     */
    public Map<String, Object> setFirestore() {
        Map<String, Object> data = new HashMap<>();
        data.put("usuario", this.usuario);
        data.put("animes", this.animes);
        data.put("reviews", this.reviews);
        data.put("admin", this.admin);
        data.put("foto", this.foto);
        return data;
    }


    @Override
    public String toString() {
        return "Usuario{" +
                "UID='" + UID + '\'' +
                ", usuario='" + usuario + '\'' +
                ", foto='" + foto + '\'' +
                ", animes=" + animes +
                ", reviews=" + reviews +
                ", admin=" + admin +
                '}';
    }
}
