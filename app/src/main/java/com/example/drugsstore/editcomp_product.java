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
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class editcomp_product extends Fragment {

    int GALLERY=2;
    int CAMERA=3;
    ImageView comp_editproductpic;
    EditText comp_editProductname,comp_editproductprice,comp_editproductdesc;
    Button compeditsave;
    String comp_pruductpicurl,name,description,id,price,companyName;
    String pictUrl;
    Uri comp_productpicuri;
    private DatabaseReference mDatabase;

    public editcomp_product() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        id=getArguments().getString("id");
        comp_pruductpicurl=getArguments().getString("picUrl");
        name=getArguments().getString("Name");
        description=getArguments().getString("Description");
        price=getArguments().getString("Price");
        companyName=getArguments().getString("companyName");
        View root= inflater.inflate(R.layout.fragment_editcomp_product, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        comp_editproductpic=view.findViewById(R.id.comp_editproductImage);
        comp_editProductname=view.findViewById(R.id.comp_editproductname);
        comp_editproductprice=view.findViewById(R.id.comp_editproductprice);
        comp_editproductdesc=view.findViewById(R.id.comp_editProductDescrip);
        compeditsave=view.findViewById(R.id.btn_editcompsave);
        if (getActivity() == null) {
            return;
        }
        Glide.with(getActivity())
                .load(comp_pruductpicurl)
                .into(comp_editproductpic);

        comp_editProductname.setText(name);
        comp_editproductprice.setText(price);
        comp_editproductdesc.setText(description);
        comp_editproductpic.setOnClickListener(new View.OnClickListener() {
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
                    comp_editproductpic.setImageBitmap(bitmap);
                    uploadimage();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else if (requestCode == CAMERA) {
            comp_productpicuri= data.getData();
            comp_editproductpic.setImageURI(comp_productpicuri);
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
                    pictUrl = downloadUri.toString();
                } else {
                    // Handle failures
                    // ...
                }

            }
        });

        compeditsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final String Name,Description,Price;
                Name=comp_editProductname.getText().toString();
                Description=comp_editproductdesc.getText().toString();
                Price=comp_editproductprice.getText().toString();
                mDatabase = FirebaseDatabase.getInstance().getReference();
                FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
                String currentuserid=firebaseUser.getUid();
                comp_productinfo compProductinfo=new comp_productinfo(companyName,Name,Description,pictUrl,Price,id);
                mDatabase.child("company_product").child(currentuserid).child(id).setValue(compProductinfo);
                

            }
        });


    }

}
