package com.example.appAnime.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appAnime.R;
import com.example.appAnime.model.Review;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private ArrayList<Review> reviewList;
    private EventsInterface mOnClickListener;

    public ReviewAdapter(ArrayList<Review> reviewList, EventsInterface mOnClickListener) {
        this.reviewList = reviewList;
        this.mOnClickListener = mOnClickListener;
    }

    public ArrayList<Review> getReviewList() {
        return reviewList;
    }

    public void setReviewList(ArrayList<Review> animeList) {
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_review,
                parent, false);
        return new ReviewViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review actualReview = reviewList.get(position);
        holder.getReviewTitleText().setText(actualReview.getTitulo());
        holder.getRateText().setText("rate: " + String.valueOf(actualReview.getRating()));
        holder.getUserText().setText(actualReview.getComentario()); //hay que crear el atributo user
        // en la clase review
        holder.getAnimeText().setText(actualReview.getAnime().getTitulo());
        holder.getReviewDescText().setText(actualReview.getComentario());

        //La url de la imagen
        String UrlImagen = String.valueOf(actualReview.getTitulo()); //aqui habria que pillar la
        // imagen del user, aun por crear
        Picasso.get().load(UrlImagen).into(holder.getCoverImg());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleText;
        TextView rateText;
        TextView usernameText;
        TextView animetitleText;
        TextView descText;
        ImageView coverImg;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            this.titleText = itemView.findViewById(R.id.titleTxt);
            this.rateText = itemView.findViewById(R.id.followers);
            this.usernameText = itemView.findViewById(R.id.userTxt);
            this.animetitleText = itemView.findViewById(R.id.animeReview);
            this.descText = itemView.findViewById(R.id.userDescription);
            this.coverImg = itemView.findViewById(R.id.userPhoto);
            itemView.setOnClickListener(this);
        }

        public TextView getReviewTitleText() {
            return titleText;
        }

        public void setReviewTitleText(TextView titleText) {
            this.titleText = titleText;
        }

        public TextView getRateText() {
            return rateText;
        }

        public TextView getUserText() {
            return usernameText;
        }

        public TextView getAnimeText() {
            return animetitleText;
        }

        public TextView getReviewDescText() {
            return descText;
        }

        public void setReviewDescText(TextView descText) {
            this.descText = descText;
        }

        public ImageView getCoverImg() {
            return coverImg;
        }

        public void setCoverImg(ImageView coverImg) {
            this.coverImg = coverImg;
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mOnClickListener.clickElement(position);
        }
    }
}