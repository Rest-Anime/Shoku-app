package com.example.appAnime.model;
import android.graphics.drawable.Drawable;

import com.squareup.picasso.Picasso;

import java.util.Date;
public class Anime {
    private String tittle;
    private String descripcion;
    private int duracion;
    private String studio;
    private String cover;
    private Drawable coverDraw;
    private String genre;
    private Date release;
    private int rate;
    private int seasons;

    public Anime(String tittle, String descripcion, int duracion, String studio, String cover, String genre, Date release, int rate, int seasons) {
        this.tittle = tittle;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.studio = studio;
        this.cover = cover;
        this.genre = genre;
        this.release = release;
        this.rate = rate;
        this.seasons = seasons;
    }
    public Anime(String tittle, String descripcion, int duracion, String studio, Drawable coverDraw, String genre, Date release, int rate, int seasons) {
        this.tittle = tittle;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.studio = studio;
        this.cover = cover;
        this.genre = genre;
        this.release = release;
        this.rate = rate;
        this.seasons = seasons;
    }
    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
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

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }


    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Date getRelease() {
        return release;
    }

    public void setRelease(Date release) {
        this.release = release;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getSeasons() {
        return seasons;
    }

    public void setSeasons(int seasons) {
        this.seasons = seasons;
    }

    public Drawable getCoverDraw() {
        return coverDraw;
    }

    public void setCoverDraw(Drawable coverDraw) {
        this.coverDraw = coverDraw;
    }

    @Override
    public String toString() {
        return "Anime: " +
                "tittle='" + tittle + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", duracion=" + duracion +
                ", studio='" + studio + '\'' +
                ", cover='" + cover + '\'' +
                ", genre='" + genre + '\'' +
                ", release=" + release +
                ", rate=" + rate +
                ", seasons=" + seasons +
                '}';
    }

    public String getCover() {

        return cover;
    }
}
