package com.example.appAnime.activities.main.ui;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appAnime.R;
import com.example.appAnime.activities.login.LoginActivity;
import com.example.appAnime.activities.main.MainActivity;
import com.example.appAnime.databinding.FragmentProfileBinding;
import com.example.appAnime.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {
    private static final int REQUEST_SUCCESS = 1000;
    private FirebaseStorage storage;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private StorageReference storageReference;
    View root;
    Context context;
    Uri imageURI;
    ImageView imgProfile;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ImageButton imageButton;
    Usuario usuario;
    FragmentProfileBinding binding;
    EditText nickTxt, emailTxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(getLayoutInflater());
        usuario = ((MainActivity) getActivity()).usuario;
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        root = binding.getRoot();
        imgProfile = binding.imgProfile;
        if (usuario.getFoto() != null) {
            String urlImagen = String.valueOf(usuario.getFoto());
            Picasso.get().load(urlImagen).into(imgProfile);
        } else {
            Picasso.get().load(R.drawable.emptyuser).into(imgProfile);
        }
        nickTxt = binding.nickTxt;
        emailTxt = binding.emailTxt;
        imageButton = binding.imageButton;
        nickTxt.setText(String.valueOf(usuario.getUsuario()));
        emailTxt.setText(auth.getCurrentUser().getEmail());
        binding.modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(nickTxt.getText().toString())) {
                    usuario.setUsuario(nickTxt.getText().toString());
                    db.collection("usuarios").document(usuario.getUID()).update(usuario.setFirestore());
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(usuario.getUsuario())
                            .build();
                    auth.getCurrentUser().updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("TAG", "User display name updated.");
                            }
                        }
                    });;
                    Toast.makeText(getContext(), "Usuario modificado correctamente.",
                            Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "El campo no puede estar vacio",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.sendPasswordResetEmail(auth.getCurrentUser().getEmail())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Correo para reestablecer " +
                                                    "contraseña enviado.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageFromGallery();
            }
        });

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
        StorageReference picture = storageReference.child("images/" + usuario.getUID());
        System.out.println(picture.toString());
        picture.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                picture.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        usuario.setFoto(uri.toString());
                        db.collection("usuarios").document(usuario.getUID()).update(usuario.setFirestore());
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