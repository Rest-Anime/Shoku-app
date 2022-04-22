package com.example.appAnime.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Usuario implements Serializable {

    private String nombre;
    private String usuario;
    private String foto;
    private List<Anime> animes;
    private List<Review> reviews;
    private boolean admin;

    public Usuario() {
    }

    public Usuario(String nombre, String usuario, String foto,
                   List<Anime> animes, List<Review> reviews, boolean admin) {
        this.nombre = nombre;
        this.usuario = usuario;
        this.foto = foto;
        this.animes = animes;
        this.reviews = reviews;
        this.admin = admin;
    }

    public Usuario(String usuario) {
        this.nombre = null;
        this.usuario = usuario;
        this.foto = null;
        this.animes = new ArrayList<>();
        this.reviews = new ArrayList<>();
        this.admin = false;
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

    public List<Anime> getAnimes() {
        return animes;
    }

    public void setAnimes(List<Anime> animes) {
        this.animes = animes;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
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
