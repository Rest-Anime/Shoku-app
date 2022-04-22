package com.example.appAnime.model;

import java.io.Serializable;

public class Anime implements Serializable {
    private String uid;
    private String titulo;
    private String descripcion;
    private String wiki;
    private int episodios;
    private String estudio;
    private String foto;
    private String genero;
    private String lanzamiento;
    private int puntuacion;
    private int temporadas;
    private String estado;

    public Anime() {
    }

    public Anime(String titulo, String descripcion, int episodios, String estudio, String foto,
                 String genero, String lanzamiento, int puntuacion, int temporadas) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.episodios = episodios;
        this.estudio = estudio;
        this.foto = foto;
        this.genero = genero;
        this.lanzamiento = lanzamiento;
        this.puntuacion = puntuacion;
        this.temporadas = temporadas;
    }

    public Anime(String titulo, String descripcion, int episodios, String estudio, String foto,
                 String genero, String lanzamiento, int puntuacion, int temporadas, String estado) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.episodios = episodios;
        this.estudio = estudio;
        this.foto = foto;
        this.genero = genero;
        this.lanzamiento = lanzamiento;
        this.puntuacion = puntuacion;
        this.temporadas = temporadas;
        this.estado = estado;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getWiki() {
        return wiki;
    }

    public void setWiki(String wiki) {
        this.wiki = wiki;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getEpisodios() {
        return episodios;
    }

    public void setEpisodios(int episodios) {
        this.episodios = episodios;
    }

    public String getEstudio() {
        return estudio;
    }

    public void setEstudio(String estudio) {
        this.estudio = estudio;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getLanzamiento() {
        return lanzamiento;
    }

    public void setLanzamiento(String lanzamiento) {
        this.lanzamiento = lanzamiento;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public int getTemporadas() {
        return temporadas;
    }

    public void setTemporadas(int temporadas) {
        this.temporadas = temporadas;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Anime{" +
                "titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", duracion=" + episodios +
                ", estudio='" + estudio + '\'' +
                ", foto='" + foto + '\'' +
                ", genero='" + genero + '\'' +
                ", lanzamiento='" + lanzamiento + '\'' +
                ", puntuacion=" + puntuacion +
                ", temporadas=" + temporadas +
                ", status=" + estado +
                '}';
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
