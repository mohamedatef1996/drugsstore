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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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
public class comp_addproduct extends Fragment {
    int GALLERY=2;
    int CAMERA=3;
    ImageView comp_productpic;
    EditText comp_Productname,comp_productprice,comp_productdesc;
    Button btncompadd;
    Uri comp_productpicuri;
    String comp_pruductpicurl,name,desription,companyname,RegisteredUserID,price;
    private DatabaseReference mDatabase;

    public comp_addproduct() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_comp_addproduct, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        comp_productpic=view.findViewById(R.id.comp_productImage);
        comp_Productname=view.findViewById(R.id.comp_productname);
        comp_productprice=view.findViewById(R.id.comp_productprice);
        comp_productdesc=view.findViewById(R.id.compProductDescrip);
        btncompadd=view.findViewById(R.id.btn_compadd);



        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        RegisteredUserID = currentUser.getUid();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                companyname=dataSnapshot.child("Company_info").child(RegisteredUserID).child("name").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        comp_productpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
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
                comp_productpicuri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), comp_productpicuri);
                    comp_productpic.setImageBitmap(bitmap);
                    uploadimage();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else if (requestCode == CAMERA) {
            comp_productpicuri= data.getData();
            comp_productpic.setImageURI(comp_productpicuri);
            uploadimage();
        }
    }
    private void uploadimage() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("uploding...");
        progressDialog.show();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference ref = storageRef.child("profilepic/" + System.currentTimeMillis() + ".jbg");
        ref.putFile(comp_productpicuri)

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
                    comp_pruductpicurl = downloadUri.toString();
                } else {
                    // Handle failures
                    // ...
                }

            }
        });
        btncompadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          name=comp_Productname.getText().toString();
          desription=comp_productdesc.getText().toString();
          price=comp_productprice.getText().toString();
          FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
          String currentuserid=firebaseUser.getUid();
          String id=mDatabase.child("company_product").push().getKey();
          comp_productinfo comp_productinfo=new comp_productinfo(companyname,name,desription,comp_pruductpicurl,price,id);
          mDatabase.child("company_product").child(currentuserid).child(id).setValue(comp_productinfo)
                  .addOnSuccessListener(new OnSuccessListener<Void>() {
                      @Override
                      public void onSuccess(Void aVoid) {
                          comp_product comp_product=new comp_product();
                          FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                          FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                          fragmentTransaction.replace(R.id.Fragment_container,comp_product);
                          fragmentTransaction.addToBackStack(null);
                          fragmentTransaction.commit();


                          }
                  });

            }
        });

    }

}
