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
public class editcomp_profile extends Fragment {
    int GALLERY=2;
    int CAMERA=3;
    Uri profilepicuri;
    Button btn_save;
    EditText phone_number,location,name,secondnumber,etdecrbtion;
    com.mikhaellopez.circularimageview.CircularImageView profile_pic;
    private DatabaseReference mDatabase;
    String ProfilePictureUrl,companyName,Location,Number1,Number2,Describtion,comp_profilePic,
    RegisteredUserID;

    public editcomp_profile() {
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
                companyName= dataSnapshot.child("Company_info").child(RegisteredUserID).child("name").getValue(String.class);
                Location=dataSnapshot.child("Company_info").child(RegisteredUserID).child("loction").getValue(String.class);
                Number1=dataSnapshot.child("Company_info").child(RegisteredUserID).child("phone").getValue(String.class);
                Number2=dataSnapshot.child("Company_info").child(RegisteredUserID).child("secondnumber").getValue(String.class);
                Describtion=dataSnapshot.child("Company_info").child(RegisteredUserID).child("descrbtion").getValue(String.class);
                comp_profilePic=dataSnapshot.child("Company_info").child(RegisteredUserID).child("profilePicUrl").getValue(String.class);
                if (getActivity() == null) {
                    return;
                }
                Glide.with(getActivity())
                        .load(comp_profilePic)
                        .into(profile_pic);
                name.setText(companyName);
                phone_number.setText(Number1);
                secondnumber.setText(Number2);
                location.setText(Location);
                etdecrbtion.setText(Describtion);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        return inflater.inflate(R.layout.fragment_editcomp_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profile_pic=view.findViewById(R.id.profile_pic);
        name=view.findViewById(R.id.ed_name);
        phone_number=view.findViewById(R.id.etPhone);
        secondnumber=view.findViewById(R.id.etPhone2);
        etdecrbtion=view.findViewById(R.id.description);
        location=view.findViewById(R.id.ed_location);
        btn_save=view.findViewById(R.id.btn_save);
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
                Name=name.getText().toString();
                Phone=phone_number.getText().toString();
                DEscrbtion=etdecrbtion.getText().toString();
                Secondnumber=secondnumber.getText().toString();
                Location=location.getText().toString();


                if(Name.isEmpty()||Name.equals(" ")){
                    name.setError("Required");
                    return;}
                if(Location.isEmpty()||Location.equals(" ")){
                    location.setError("Required");
                    return;
                }
                if(Phone.isEmpty()||Phone.equals(" ")){
                    phone_number.setError("Required");
                    return;

                }
                if(Secondnumber.isEmpty()||Secondnumber.equals(" ")){
                    secondnumber.setError("Required");
                    return;
                }
                if(DEscrbtion.isEmpty()||DEscrbtion.equals(" ")){
                    etdecrbtion.setError("Required");
                    return;
                }

                String useerEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                mDatabase = FirebaseDatabase.getInstance().getReference();
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                company_info company_info =new company_info(Name,Phone,Location,ProfilePictureUrl,Secondnumber,useerEmail,DEscrbtion,userId);
                mDatabase.child("Company_info").child(userId).setValue(company_info)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        comp_profile comp_profile=new comp_profile();
                        if (getActivity() != null) {
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.Fragment_container, comp_profile, "findThisFragment")
                                .addToBackStack(null)
                                .commit();}
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
