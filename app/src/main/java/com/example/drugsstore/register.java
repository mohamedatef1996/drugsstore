package com.example.drugsstore;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class register extends AppCompatActivity {
    Spinner spinner;
    EditText etEmail,etPassword,etConfirm;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    ProgressBar progressBar;
    Button btn_Refister;
    String Usertype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        btn_Refister=findViewById(R.id.btn_Register);
        etPassword=findViewById(R.id.et_Rgpassword);
        etConfirm=findViewById(R.id.et_regconfpassword);
        spinner=findViewById(R.id.spinner);
        etEmail=findViewById(R.id.et_regemail);
        ArrayList<String>usertype=new ArrayList<String>();
        usertype.add("Customer");
        usertype.add("pharmacy");
        usertype.add("company");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(register.this, android.R.layout.simple_list_item_1, usertype);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                Usertype = (String)spinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });


    }

    public void register(View view) {
        final String Email,Password,ConfirmPassword;
        Email=etEmail.getText().toString();
        Password=etPassword.getText().toString();
        ConfirmPassword=etConfirm.getText().toString();

        if(Email.isEmpty()||Email.equals(" ")){

            etEmail.setError("You Must Enter Your Email");
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){

            etEmail.setError("Please Enter a valid Email");
            return;
        }


        if(Password.isEmpty()||Password.equals(" ")){

            etPassword.setError("You Must Enter Your Password");
            return;
        }
        if(Password.length()<8){

            etPassword.setError("Password must not be less than 8 numbers");
            return;
        }
        if(ConfirmPassword.isEmpty()||ConfirmPassword.equals(" ")){

            etConfirm.setError("You Must Confirm your Password");
            return;
        }
        if(!Password.equals(ConfirmPassword))
        {
            etConfirm.setError("Password does not match");
            return;
        }

        btn_Refister.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (task.isSuccessful()) {

                            switch(Usertype){

                                case "Customer":
                                    Toast.makeText(register.this,"Customer",Toast.LENGTH_SHORT).show();
                                    break;

                                case "pharmacy":
                                    Intent pharmacy = new Intent(register.this,pharmacy_profile.class);
                                    startActivity(pharmacy);
                                    finish();

                                    break;

                                case "company":
                                    Intent company = new Intent(register.this,company_profile.class);
                                    startActivity(company);
                                    finish();
                                    break;


                            }
                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            mDatabase.child("Usertype").child(userId).setValue(Usertype);
                        } else {
                            // If sign in fails, display a message to the company_info.
                            btn_Refister.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });




    }

}
