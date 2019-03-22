package com.example.drugsstore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {
    EditText etEmail,etPassword;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mA;
    String RegisteredUserID;
    String userType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        etEmail=findViewById(R.id.et_loginEmail);
        etPassword=findViewById(R.id.et_Loginpassward);


    }


    public void login(View view) {
        String Email, Password;
        Email = etEmail.getText().toString();
        Password = etPassword.getText().toString();
        if (Email.isEmpty() || Email.equals(" ")) {
            etEmail.setError("You Must Enter Your Email");
            return;
        }
        if (Password.isEmpty() || Password.equals(" ")) {

            etPassword.setError("You Must Enter Your Password");
            return;
        }
        final ProgressDialog progressDialog=new ProgressDialog(login.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(Email, Password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                RegisteredUserID = currentUser.getUid();
                                mDatabase.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {


                                        userType = dataSnapshot.child("Usertype").child(RegisteredUserID).getValue().toString();
                                        if(userType.equals("company")){
                                            progressDialog.dismiss();
                                            if(currentUser!=null){
                                            Intent intentcompany = new Intent(login.this, company.class);
                                                intentcompany.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intentcompany);
                                            finish();}
                                        }else if(userType.equals("pharmacy")){
                                            progressDialog.dismiss();
                                            if(currentUser!=null){
                                                Intent intentpharmacy = new Intent(login.this, pharmacy.class);
                                                intentpharmacy.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intentpharmacy);
                                                finish();}
                                        }else if(userType.equals("customer")){
                                            progressDialog.dismiss();
                                            Toast.makeText(login.this, "customer", Toast.LENGTH_SHORT).show();

                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                            else{
                                progressDialog.dismiss();
                                Toast.makeText(login.this, "Failed Login. Please Try Again", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    });
        }



    public void register(View view) {
        Intent intent=new Intent(this, register.class);
        startActivity(intent);
    }


}
