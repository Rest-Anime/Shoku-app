package com.example.appAnime.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Review implements Serializable {
    private int UID;
    private String titulo;
    private String comentario;
    private int rating;
    private Anime anime;
    private String animeID;
    private Usuario usuario;
    private String usuarioID;
    private Integer likes;
    private Integer dislikes;

    public Review() {
    }

    public Review(String titulo, String comentario, int rating, String animeID, String usuarioID,
                  Integer likes, Integer dislikes) {
        this.titulo = titulo;
        this.comentario = comentario;
        this.rating = rating;
        this.animeID = animeID;
        this.usuarioID = usuarioID;
        this.likes = likes;
        this.dislikes = dislikes;
    }

    public Review(int UID, String titulo, String comentario, int rating, Anime anime,
                  String animeID, Usuario usuario, String usuarioID, Integer likes,
                  Integer dislikes) {
        this.UID = UID;
        this.titulo = titulo;
        this.comentario = comentario;
        this.rating = rating;
        this.anime = anime;
        this.animeID = animeID;
        this.usuario = usuario;
        this.usuarioID = usuarioID;
        this.likes = likes;
        this.dislikes = dislikes;
    }

    public int getUID() {
        return UID;
    }

    public void setUID(int UID) {
        this.UID = UID;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Anime getAnime() {
        return anime;
    }

    public void setAnime(Anime anime) {
        this.anime = anime;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getAnimeID() {
        return animeID;
    }

    public void setAnimeID(String animeID) {
        this.animeID = animeID;
    }

    public String getUsuarioID() {
        return usuarioID;
    }

    public void setUsuarioID(String usuarioID) {
        this.usuarioID = usuarioID;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getDislikes() {
        return dislikes;
    }

    public void setDislikes(Integer dislikes) {
        this.dislikes = dislikes;
    }


    /**
     * Mapeo de Objeto para insertar/actualizar BBDD.
     *
     * @return Diccionario con los datos para crear/actualizar Entrada en Firestore.
     */
    public Map<String, Object> setFirestore() {
        Map<String, Object> data = new HashMap<>();
        data.put("animeID", this.anime.getUID());
        data.put("usuarioID", this.usuario.getUID());
        data.put("likes", this.likes);
        data.put("dislikes", this.dislikes);
        data.put("comentario", this.comentario);
        data.put("titulo", this.titulo);
        return data;
    }

    @Override
    public String toString() {
        return "Review{" +
                "reviewId=" + UID +
                ", title='" + titulo + '\'' +
                ", review='" + comentario + '\'' +
                ", rating=" + rating +
                ", anime=" + anime +
                '}';
    }
}
