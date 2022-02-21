package com.example.appAnime.adapter;

import com.example.appAnime.model.Raiz;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiAnime {
    @GET("/api/anime/")
    Call<Raiz> obtenerAnimes();

    @GET("/api/anime/{anime}")
    Call<Raiz> obtenerAnimesNombre(@Path("anime") String animeTitulo);

}
