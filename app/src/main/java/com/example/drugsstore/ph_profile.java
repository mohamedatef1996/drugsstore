package com.example.drugsstore;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class ph_profile extends Fragment {
    Button btnEditprofile, btn_signout;
    com.mikhaellopez.circularimageview.CircularImageView Pharmacy_priflePic;
    String RegisteredUserID;
    String Username,Inmage_url,Email;
    TextView tv_Username,tv_UserEmail;
    private DatabaseReference mDatabase;

    public ph_profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        RegisteredUserID = currentUser.getUid();
        Email=currentUser.getEmail();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Username= dataSnapshot.child("pharmacy").child(RegisteredUserID).child("name").getValue(String.class);
                Inmage_url=dataSnapshot.child("pharmacy").child(RegisteredUserID).child("profilePicUrl").getValue(String.class);
                if (getActivity() == null) {
                    return;
                }
                Glide.with(getActivity())
                        .load(Inmage_url)
                        .into(Pharmacy_priflePic);
                tv_Username.setText(Username);
                tv_UserEmail.setText("Email:"+Email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return inflater.inflate(R.layout.fragment_ph_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_Username=view.findViewById(R.id.tvUsername);
        tv_UserEmail=view.findViewById(R.id.ph_Email);
        btn_signout=view.findViewById(R.id.btn_signout);
        Pharmacy_priflePic=view.findViewById(R.id.iv_ph_ProfPic);
        btnEditprofile=view.findViewById(R.id.btnEditProfile);
        btnEditprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editph_profile editph_profile=new editph_profile();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.ph_container,editph_profile)
                        .addToBackStack(null)
                        .commit();

            }
        });
        btn_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(getActivity(),login.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
}
