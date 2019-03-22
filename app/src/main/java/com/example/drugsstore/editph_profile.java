package com.example.drugsstore;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class editph_profile extends Fragment {
    int GALLERY=2;
    int CAMERA=3;
    Uri profilepicuri;
    Button btn_save;
    EditText ph_phone1,ph_location,ph_name,ph_phone2,ph_description;
    com.mikhaellopez.circularimageview.CircularImageView profile_pic;
    private DatabaseReference mDatabase;
    String ProfilePictureUrl,companyName,Location,Number1,Number2,Description,comp_profilePic,
            RegisteredUserID;

    public editph_profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        RegisteredUserID = currentUser.getUid();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                companyName= dataSnapshot.child("pharmacy").child(RegisteredUserID).child("name").getValue(String.class);
                Location=dataSnapshot.child("pharmacy").child(RegisteredUserID).child("loction").getValue(String.class);
                Number1=dataSnapshot.child("pharmacy").child(RegisteredUserID).child("phone").getValue(String.class);
                Number2=dataSnapshot.child("pharmacy").child(RegisteredUserID).child("secondnumber").getValue(String.class);
                Description=dataSnapshot.child("pharmacy").child(RegisteredUserID).child("description").getValue(String.class);
                comp_profilePic=dataSnapshot.child("pharmacy").child(RegisteredUserID).child("profilePicUrl").getValue(String.class);
                if (getActivity() == null) {
                    return;
                }
                Glide.with(getActivity())
                        .load(comp_profilePic)
                        .into(profile_pic);
                ph_name.setText(companyName);
                ph_phone1.setText(Number1);
                ph_phone2.setText(Number2);
                ph_location.setText(Location);
                ph_description.setText(Description);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return inflater.inflate(R.layout.fragment_editph_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profile_pic=view.findViewById(R.id.ph_profilepic);
        ph_name=view.findViewById(R.id.Ph_name);
        ph_phone1=view.findViewById(R.id.ph_phone1);
        ph_phone2=view.findViewById(R.id.ph_phone2);
        ph_description=view.findViewById(R.id.ph_description);
        ph_location=view.findViewById(R.id.ph_location);
        btn_save=view.findViewById(R.id.btn_phsave);
        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Name,Phone ,Location,DEscrbtion,Secondnumber;
                Name=ph_name.getText().toString();
                Phone=ph_phone1.getText().toString();
                DEscrbtion=ph_description.getText().toString();
                Secondnumber=ph_phone2.getText().toString();
                Location=ph_location.getText().toString();


                if(Name.isEmpty()||Name.equals(" ")){
                    ph_name.setError("Required");
                    return;}
                if(Location.isEmpty()||Location.equals(" ")){
                    ph_location.setError("Required");
                    return;
                }
                if(Phone.isEmpty()||Phone.equals(" ")){
                    ph_phone1.setError("Required");
                    return;

                }
                if(Secondnumber.isEmpty()||Secondnumber.equals(" ")){
                    ph_phone2.setError("Required");
                    return;
                }
                if(DEscrbtion.isEmpty()||DEscrbtion.equals(" ")){
                    ph_description.setError("Required");
                    return;
                }
            String UserEmail=FirebaseAuth.getInstance().getCurrentUser().getEmail();
            String Userid=FirebaseAuth.getInstance().getCurrentUser().getUid();
            pharmacy_info pharmacy_info=new pharmacy_info(Name,Phone,Location,ProfilePictureUrl,Secondnumber,UserEmail,Userid,DEscrbtion);
            mDatabase.child("pharmacy").child(Userid).setValue(pharmacy_info)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            ph_profile ph_profile=new ph_profile();
                            if (getActivity() != null) {
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.ph_container,ph_profile)
                                        .addToBackStack(null)
                                        .commit();
                            }
                        }
                    });


            }
        });
    }
    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }
    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        getActivity().startActivityForResult(intent, CAMERA);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                profilepicuri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), profilepicuri);
                    profile_pic.setImageBitmap(bitmap);
                    uploadimage();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else if (requestCode == CAMERA) {
            profilepicuri= data.getData();
            profile_pic.setImageURI(profilepicuri);
            uploadimage();
        }
    }
    private void uploadimage() {
        final ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("uploding...");
        progressDialog.show();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference ref = storageRef.child("profilepic/"+System.currentTimeMillis()+".jbg");
        ref.putFile(profilepicuri)

                .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            progressDialog.dismiss();
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return ref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Uri downloadUri = task.getResult();
                    ProfilePictureUrl=downloadUri.toString();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
}}
