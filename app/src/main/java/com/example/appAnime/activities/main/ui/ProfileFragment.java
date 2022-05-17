package com.example.appAnime.activities.main.ui;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appAnime.R;
import com.example.appAnime.activities.detail.DetailActivity;
import com.example.appAnime.activities.main.MainActivity;
import com.example.appAnime.adapter.AnimeAdapter;
import com.example.appAnime.adapter.EventsInterface;
import com.example.appAnime.adapter.ReviewAdapter;
import com.example.appAnime.databinding.FragmentListBinding;
import com.example.appAnime.databinding.FragmentProfileBinding;
import com.example.appAnime.model.Anime;
import com.example.appAnime.model.Review;
import com.example.appAnime.model.Usuario;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {
    private static final int IMG_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    View root;
    Context context;
    Uri uri;
    ImageView picker, imgProfile, imgChangeBtn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ImageButton imageButton;
    Button modify;
    Usuario usuario;
    ReviewAdapter reviewAdapter;
    private FragmentProfileBinding binding;
    ArrayList<Review> reviewsList = new ArrayList<>();

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
        RecyclerView recyclerView = binding.reviewsRW;

        root = inflater.inflate(R.layout.fragment_profile, container, false);
        picker = (ImageView) root.findViewById(R.id.imgPickerBtn);
        imgProfile = (ImageView) root.findViewById(R.id.imgProfile);
        imageButton = (ImageButton) root.findViewById(R.id.imageButton);
        modify = (Button) root.findViewById(R.id.btnEdit2);

        //region FIRESTORE
        db.collection("reviews").whereEqualTo("usuarioID", usuario.getUID()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException error) {
                reviewsList.clear();
                for (QueryDocumentSnapshot doc : value) {
                    Review review = doc.toObject(Review.class);
                    review.setUID(doc.getId());

                    if (usuario.getReviews().containsKey(review.getUID())) {
                        reviewsList.add(review);
                    }
                Log.e("Lista reviews usuario: ", reviewsList.toString());
                reviewAdapter = new ReviewAdapter(reviewsList, function);
                recyclerView.setAdapter(reviewAdapter);
                }
            }
        });
        //endregion

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.READ_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_DENIED)) {
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else {
                        pickImageFromGallery();
                    }
                } else {
                    pickImageFromGallery();
                }
            }
        });

        // Inflate the layout for this fragment
        return root;
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent, "Select Picture"), IMG_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                } else {
                    Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && resultCode == IMG_PICK_CODE) {
            uri = data.getData();
            imgProfile.setImageURI(uri);
        }

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}

