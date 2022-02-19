package com.example.appAnime.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

    public class AnimeGson {

            @SerializedName("Descripcion")
            @Expose
            private String descripcion;
            @SerializedName("Duracion")
            @Expose
            private int duracion;
            @SerializedName("Estudio")
            @Expose
            private String estudio;
            @SerializedName("Foto")
            @Expose
            private String foto;
            @SerializedName("Genero")
            @Expose
            private String genero;
            @SerializedName("Lanzamiento")
            @Expose
            private String lanzamiento;
            @SerializedName("Puntuacion")
            @Expose
            private int puntuacion;
            @SerializedName("Temporadas")
            @Expose
            private int temporadas;
            @SerializedName("Titulo")
            @Expose
            private String titulo;

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

            public String getFoto() {
                return foto;
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

            public String getTitulo() {
                return titulo;
            }

            public void setTitulo(String titulo) {
                this.titulo = titulo;
            }

        }
