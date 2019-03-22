package com.example.drugsstore;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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

public class company_profile extends AppCompatActivity {
    int GALLERY=2;
    int CAMERA=3;
    Uri First_profilepicuri;
    Button First_btn_Next;
    EditText Firstphone_number,Firstlocation,First_name,First_secondnumber,First_etdecrbtion;
    com.mikhaellopez.circularimageview.CircularImageView profile_pic;
    private DatabaseReference mDatabase;
    String ProfilePictureUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profile);
        profile_pic=findViewById(R.id.First_profile_pic);
        First_name=findViewById(R.id.First_ed_name);
        Firstphone_number=findViewById(R.id.First_etPhone);
        First_secondnumber=findViewById(R.id.First_etPhone2);
        First_etdecrbtion=findViewById(R.id.First_description);
        Firstlocation=findViewById(R.id.First_ed_location);
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


                final String Name,Phone ,Location,DEscrbtion,Secondnumber,Email;
                Name=First_name.getText().toString();
                Phone=Firstphone_number.getText().toString();
                DEscrbtion=First_etdecrbtion.getText().toString();
                Secondnumber=First_secondnumber.getText().toString();
                Location=Firstlocation.getText().toString();
                Email= FirebaseAuth.getInstance().getCurrentUser().getEmail();


                if(Name.isEmpty()||Name.equals(" ")){
                    First_name.setError("Required");
                    return;}
                if(Location.isEmpty()||Location.equals(" ")){
                    Firstlocation.setError("Required");
                    return;
                }
                if(Phone.isEmpty()||Phone.equals(" ")){
                    Firstphone_number.setError("Required");
                    return;

                }
                if(Secondnumber.isEmpty()||Secondnumber.equals(" ")){
                    First_secondnumber.setError("Required");
                    return;
                }
                if(DEscrbtion.isEmpty()||DEscrbtion.equals(" ")){
                    First_etdecrbtion.setError("Required");
                    return;
                }
                mDatabase = FirebaseDatabase.getInstance().getReference();
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                company_info company_info =new company_info(Name,Phone,Location,ProfilePictureUrl,Secondnumber,Email,DEscrbtion,userId);
                mDatabase.child("Company_info").child(userId).setValue(company_info);
                Intent intent=new Intent(company_profile.this,company.class);
                startActivity(intent);







            }
        });

    }
    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(company_profile.this);
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
        company_profile.this.startActivityForResult(intent, CAMERA);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == company_profile.this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                First_profilepicuri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(company_profile.this.getContentResolver(), First_profilepicuri);
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
