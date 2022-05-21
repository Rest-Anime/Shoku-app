package com.example.appAnime.activities.main.ui;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appAnime.R;
import com.example.appAnime.activities.main.MainActivity;
import com.example.appAnime.adapter.EventsInterface;
import com.example.appAnime.adapter.ReviewAdapter;
import com.example.appAnime.databinding.FragmentProfileBinding;
import com.example.appAnime.model.Anime;
import com.example.appAnime.model.Review;
import com.example.appAnime.model.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.UUID;

public class ProfileFragment extends Fragment {
    private static final int REQUEST_SUCCESS = 1000;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    View root;
    Context context;
    Uri imageURI;
    ImageView picker, imgProfile;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ImageButton imageButton;
    Button modify;
    Usuario usuario;
    ReviewAdapter reviewAdapter;
    FragmentProfileBinding binding;
    Anime anime;
    Review review;
    ArrayList<Review> reviewsList = new ArrayList<>();
    String newName, newNick, newMail;
    EditText nameTxt, nickTxt, mailTxt;

    private EventsInterface function = (pos) -> {
        Intent launchInfo = new Intent(context, ProfileFragment.class);
        launchInfo.putExtra("anime", reviewsList.get(pos));
        launchInfo.putExtra("usuario", usuario);
        launchInfo.putExtra("pos", pos);
        startActivity(launchInfo);
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(getLayoutInflater());
        usuario = ((MainActivity) getActivity()).usuario;
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        RecyclerView recyclerView = binding.reviewsRW;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL));

        root = binding.getRoot();
        imgProfile = binding.imgProfile;
        if (usuario.getFoto() != null) {
            String urlImagen = String.valueOf(usuario.getFoto());
            Picasso.get().load(urlImagen).into(imgProfile);
        } else {
            Picasso.get().load(R.drawable.emptyuser).into(imgProfile);
        }
        nameTxt = binding.nametxt;
        nickTxt = binding.nicktxt;
        mailTxt = binding.mailtxt;;

        imageButton = binding.imageButton;
        modify = binding.btnEdit2;

        nameTxt.setText(String.valueOf(usuario.getUsuario()));
        if(usuario.getNombre() != null){
            newNick = String.valueOf(usuario.getNombre());
        }else{
            newNick = "Usuario no contiene este apartado";
        }
        nickTxt.setText(newNick);
        mailTxt.setText(String.valueOf(usuario.getCorreo()));


        //region FIRESTORE
        db.collection("reviews").whereEqualTo("usuarioID", usuario.getUID()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot,
                                @Nullable FirebaseFirestoreException error) {
                reviewsList.clear();
                for (QueryDocumentSnapshot doc : querySnapshot) {
                    review = doc.toObject(Review.class);
                    review.setUID(doc.getId());

                    db.collection("animes").document(review.getAnimeID()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value,
                                            @Nullable FirebaseFirestoreException error) {
                            anime = value.toObject(Anime.class);
                            anime.setUID(value.getId());
                            review.setAnime(anime);
                        }
                    });
                    review.setUsuario(usuario);
                    reviewsList.add(review);
                }
                Log.e("Lista reviews usuario: ", reviewsList.toString());
                reviewAdapter = new ReviewAdapter(reviewsList, function);
                recyclerView.setAdapter(reviewAdapter);
            }
        });
        //endregion

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newName = String.valueOf(nameTxt.getText());
                newNick = String.valueOf(nickTxt.getText());
                newMail = String.valueOf(mailTxt.getText());

                usuario.setNombre(String.valueOf(newNick));
                usuario.setUsuario(String.valueOf(newName));
                usuario.setCorreo(String.valueOf(newMail));
                db.collection("usuarios").document(usuario.getUID()).update(usuario.setFirestore());
                Toast.makeText(getContext(),  "Usuario modificado correctamente.", Toast.LENGTH_SHORT).show();
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageFromGallery();
            }
        });


        // Inflate the layout for this fragment
        return root;
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_SUCCESS);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SUCCESS && resultCode == RESULT_OK && data != null) {
            imageURI = data.getData();
            imgProfile.setImageURI(imageURI);
            uploadPicture();
        }

    }

    private void uploadPicture() {
        final ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle("Uploading image...");
        pd.show();
        final String randomKey = UUID.randomUUID().toString();
        StorageReference picture = storageReference.child("images/" + randomKey);
        picture.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                picture.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        updateUser(uri.toString());
                        Snackbar.make(root, "Image Uploaded.", Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Snackbar.make(root, "There was an Error: " + e.getLocalizedMessage(),
                        Snackbar.LENGTH_SHORT).show();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progressPercent =
                        (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                pd.setMessage("Progress: " + (int) progressPercent + "%");
            }
        });

    }

    private void updateUser(String foto) {
        usuario.setFoto(foto);
        db.collection("usuarios").document(usuario.getUID()).update(usuario.setFirestore());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

}

