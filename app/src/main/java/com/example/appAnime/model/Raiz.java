package com.example.appAnime.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Raiz {
    @SerializedName("anime")
    @Expose
    private List<Anime> anime = null;

    /**
     * No args constructor for use in serialization
     */
    public Raiz() {
    }

    /**
     * @param anime
     */
    public Raiz(List<Anime> anime) {
        super();
        this.anime = anime;
    }

    public List<Anime> getAnime() {
        return anime;
    }

    public void setAnime(List<Anime> anime) {
        this.anime = anime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Raiz.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("anime");
        sb.append('=');
        sb.append(((this.anime == null) ? "<null>" : this.anime));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }
}
