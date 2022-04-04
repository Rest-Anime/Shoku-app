package com.example.appAnime.model;
import java.io.Serializable;
import java.util.List;

public class Usuario implements Serializable {
    private int userId;
    private String name;
    private String nick;
    private String mail;
    private String foto;
    private List<Anime> userAnimeList;
    private List<Review> userReviewList;

    public Usuario() {
    }

    public Usuario(int userId, String name, String nick, String mail, String foto) {
        this.userId = userId;
        this.name = name;
        this.nick = nick;
        this.mail = mail;
        this.foto = foto;
    }

    public Usuario(int userId, String name, String nick, String mail, String foto, List<Anime> userAnimeList, List<Review> userReviewList) {
        this.userId = userId;
        this.name = name;
        this.nick = nick;
        this.mail = mail;
        this.foto = foto;
        this.userAnimeList = userAnimeList;
        this.userReviewList = userReviewList;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public List<Anime> getUserAnimeList() {
        return userAnimeList;
    }

    public void setUserAnimeList(List<Anime> userAnimeList) {
        this.userAnimeList = userAnimeList;
    }

    public List<Review> getUserReviewList() {
        return userReviewList;
    }

    public void setUserReviewList(List<Review> userReviewList) {
        this.userReviewList = userReviewList;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", nick='" + nick + '\'' +
                ", mail='" + mail + '\'' +
                ", foto='" + foto + '\'' +
                '}';
    }
}
