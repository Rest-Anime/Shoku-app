package com.example.appAnime.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appAnime.R;
import com.example.appAnime.model.Usuario;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder> {
    private ArrayList<Usuario> usuariosList;
    private EventsInterface mOnClickListener;

    public UsuarioAdapter(ArrayList<Usuario> usuarioList, EventsInterface function) {
        this.usuariosList = usuarioList;
        this.mOnClickListener = function;
    }

    public ArrayList<Usuario> getUsuariosList() {
        return usuariosList;
    }

    public void setUsuariosList(ArrayList<Usuario> usuariosList) {
        this.usuariosList = usuariosList;
    }

    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_user,
                        parent, false);
        return new UsuarioViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
        Usuario usuario = usuariosList.get(position);
        holder.getUsuario().setText(usuario.getUsuario());
        holder.getFavoritos().setText("Favoritos: " + String.valueOf(usuario.getAnimes().size()));
        String UrlImagen = usuario.getFoto();
        if (UrlImagen != null) {
            Picasso.get().load(UrlImagen).into(holder.getFotoPerfil());
        } else {
            Picasso.get().load(R.drawable.emptyuser).into(holder.getFotoPerfil());
        }

    }

    @Override
    public int getItemCount() {
        return usuariosList.size();
    }

    public class UsuarioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView usuario;
        ImageView fotoPerfil;
        TextView favoritos;

        public UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);
            usuario = itemView.findViewById(R.id.usuario);
            fotoPerfil = itemView.findViewById(R.id.fotoPerfil);
            favoritos = itemView.findViewById(R.id.favoritos);
            itemView.setOnClickListener(this);
        }

        public TextView getUsuario() {
            return usuario;
        }

        public void setUsuario(TextView usuario) {
            this.usuario = usuario;
        }

        public ImageView getFotoPerfil() {
            return fotoPerfil;
        }

        public void setFotoPerfil(ImageView fotoPerfil) {
            this.fotoPerfil = fotoPerfil;
        }

        public TextView getFavoritos() {
            return favoritos;
        }

        public void setFavoritos(TextView favoritos) {
            this.favoritos = favoritos;
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mOnClickListener.clickElement(position);
        }
    }
}
