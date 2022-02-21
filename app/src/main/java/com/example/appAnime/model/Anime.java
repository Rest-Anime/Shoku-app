package com.example.appAnime.model;

import android.graphics.drawable.Drawable;

import java.util.Date;

public class Anime {
    private String titulo;
    private String descripcion;
    private int duracion;
    private String estudio;
    private String foto;
    private String genero;
    private Date lanzamiento;
    private int puntuacion;
    private int temporadas;

    public Anime(String titulo, String descripcion, int duracion, String estudio, String foto,
                 String genero, Date lanzamiento, int puntuacion, int temporadas) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.estudio = estudio;
        this.foto = foto;
        this.genero = genero;
        this.lanzamiento = lanzamiento;
        this.puntuacion = puntuacion;
        this.temporadas = temporadas;
    }

    public Anime(String titulo, String descripcion, int duracion, String estudio,
                 Drawable coverDraw, String genero, Date lanzamiento, int puntuacion, int temporadas) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.estudio = estudio;
        this.foto = foto;
        this.genero = genero;
        this.lanzamiento = lanzamiento;
        this.puntuacion = puntuacion;
        this.temporadas = temporadas;
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

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public String getEstudio() {
        return estudio;
    }

    public void setEstudio(String estudio) {
        this.estudio = estudio;
    }


    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Date getLanzamiento() {
        return lanzamiento;
    }

    public void setLanzamiento(Date lanzamiento) {
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

    @Override
    public String toString() {
        return "Anime: " +
                "tittle='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", duracion=" + duracion +
                ", studio='" + estudio + '\'' +
                ", cover='" + foto + '\'' +
                ", genre='" + genero + '\'' +
                ", release=" + lanzamiento +
                ", rate=" + puntuacion +
                ", seasons=" + temporadas +
                '}';
    }

    public String getFoto() {
        return foto;
    }
}
