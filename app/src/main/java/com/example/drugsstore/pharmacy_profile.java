package com.example.drugsstore;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class pharmacy_profile extends AppCompatActivity {
    int GALLERY=2;
    int CAMERA=3;
    Uri First_profilepicuri;
    Button First_btn_Next;
    EditText first_ph_phone,first_ph_location,first_ph_name,first_ph_phone2,first_ph_desc;
    com.mikhaellopez.circularimageview.CircularImageView profile_pic;
    private DatabaseReference mDatabase;
    String ProfilePictureUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_profile);
        profile_pic=findViewById(R.id.first_ph_profile);
        first_ph_name=findViewById(R.id.firsr_ph_name);
        first_ph_phone=findViewById(R.id.first_ph_phone);
        first_ph_phone2=findViewById(R.id.first_phphone2);
        first_ph_desc=findViewById(R.id.first_phdesc);
        first_ph_location=findViewById(R.id.first_ph_location);
        First_btn_Next=findViewById(R.id.First_btn_Next);
        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();

            }
        });
        First_btn_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Name,Location,DEscrbtion,Email,phone1,phone2;
                Name=first_ph_name.getText().toString();
                Location=first_ph_location.getText().toString();
                phone1=first_ph_phone.getText().toString();
                phone2=first_ph_phone2.getText().toString();
                DEscrbtion=first_ph_desc.getText().toString();
                Email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
                if(Name.isEmpty()||Name.equals(" ")){
                    first_ph_name.setError("Required");
                    return;}
                if(Location.isEmpty()||Location.equals(" ")){
                    first_ph_location.setError("Required");
                    return;
                }
                if(phone1.isEmpty()||phone1.equals(" ")){
                    first_ph_phone.setError("Required");
                    return;

                }
                if(phone2.isEmpty()||phone2.equals(" ")){
                    first_ph_phone2.setError("Required");
                    return;
                }
                if(DEscrbtion.isEmpty()||DEscrbtion.equals(" ")){
                    first_ph_desc.setError("Required");
                    return;
                }
                mDatabase = FirebaseDatabase.getInstance().getReference();
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    pharmacy_info pharmacy_info=new pharmacy_info(Name,phone1,Location,ProfilePictureUrl,phone2,Email,userId,DEscrbtion);
                    mDatabase.child("pharmacy").child(userId).setValue(pharmacy_info);
                    Intent intent=new Intent(pharmacy_profile.this,pharmacy.class);
                    startActivity(intent);
            }
        });

    }
    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(pharmacy_profile.this);
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
        pharmacy_profile.this.startActivityForResult(intent, CAMERA);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == pharmacy_profile.this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                First_profilepicuri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(pharmacy_profile.this.getContentResolver(), First_profilepicuri);
                    profile_pic.setImageBitmap(bitmap);
                    uploadimage();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else if (requestCode == CAMERA) {
            First_profilepicuri= data.getData();
            profile_pic.setImageURI(First_profilepicuri);
            uploadimage();
        }
    }
    private void uploadimage() {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("uploding...");
        progressDialog.show();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference spaceRef = storageRef.child("profilepic/" + System.currentTimeMillis() + ".jbg");
        spaceRef.putFile(First_profilepicuri)

                .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            progressDialog.dismiss();

                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return spaceRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Uri downloadUri = task.getResult();
                    ProfilePictureUrl = downloadUri.toString();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }
}
