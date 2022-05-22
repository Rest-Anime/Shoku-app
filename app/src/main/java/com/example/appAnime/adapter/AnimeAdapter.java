package com.example.appAnime.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appAnime.R;
import com.example.appAnime.model.Anime;
import com.example.appAnime.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AnimeAdapter extends RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder> {
    private ArrayList<Anime> animeList;
    private EventsInterface mOnClickListener;
    private boolean isFav;
    private String username;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public AnimeAdapter(ArrayList<Anime> animeList, EventsInterface mOnClickListener) {
        this.animeList = animeList;
        this.mOnClickListener = mOnClickListener;
    }

    public ArrayList<Anime> getAnimeList() {
        return animeList;
    }

    public void setAnimeList(ArrayList<Anime> animeList) {
        this.animeList = animeList;
    }

    @NonNull
    @Override
    public AnimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType) {
        View item =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_anime_simple,
                        parent, false);
        return new AnimeViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimeViewHolder holder, int position) {
        Anime actualAnime = animeList.get(position);
        holder.getNameText().setText(actualAnime.getTitulo());
        holder.getSeasonsText().setText("temporadas: " + String.valueOf(actualAnime.getTemporadas()));
        holder.getGenresText().setText(actualAnime.getGenero());
        holder.getStarsBar().setRating(actualAnime.getPuntuacion());
        holder.setFavIcon(actualAnime);

        //La url de la imagen
        String urlImagen = String.valueOf(actualAnime.getFoto());
        Picasso.get().load(urlImagen).fit()
                .centerCrop().into(holder.getCoverImg());
    }

    @Override
    public int getItemCount() {
        return animeList.size();
    }

    public class AnimeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameText;
        TextView genresText;
        TextView seasonsText;
        RatingBar starsBar;
        ImageView coverImg;
        ImageView fav;

        public AnimeViewHolder(@NonNull View itemView) {
            super(itemView);
            this.nameText = itemView.findViewById(R.id.userTxt);
            this.genresText = itemView.findViewById(R.id.animeReview);
            this.seasonsText = itemView.findViewById(R.id.reviewTxt);
            this.starsBar = itemView.findViewById(R.id.ratingBar);
            this.coverImg = itemView.findViewById(R.id.userPhoto);
            this.fav = itemView.findViewById(R.id.iconLike);
            username = (String) nameText.getText();
            coverImg.setOnClickListener(this);
            starsBar.setOnClickListener(this);
            seasonsText.setOnClickListener(this);
            genresText.setOnClickListener(this);
            nameText.setOnClickListener(this);
            fav.setOnClickListener(this);
        }

        public TextView getNameText() {
            return nameText;
        }

        public void setNameText(TextView nameText) {
            this.nameText = nameText;
        }

        public TextView getSeasonsText() {
            return seasonsText;
        }

        public TextView getGenresText() {
            return genresText;
        }

        public RatingBar getStarsBar() {
            return starsBar;
        }

        public ImageView getCoverImg() {
            return coverImg;
        }

        public void setCoverImg(ImageView coverImg) {
            this.coverImg = coverImg;
        }

        public void setFavIcon(Anime actualAnime){
            if(actualAnime.isFavorite() == true){
                fav.setImageResource(R.drawable.ic_baseline_favorite_24);
            }else{
                fav.setImageResource(R.drawable.ic_baseline_favorite_border_24);
            }
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mOnClickListener.clickElement(position);
        }
    }
}