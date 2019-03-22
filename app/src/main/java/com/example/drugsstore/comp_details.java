package com.example.drugsstore;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;


/**
 * A simple {@link Fragment} subclass.
 */
public class comp_details extends Fragment {

    com.mikhaellopez.circularimageview.CircularImageView comp_pic;
    TextView  comp_name,comp_email,location,phone1,phone2,DEscription;
    String    comp_id,comp_pictUrl,comp_Name,comp_Email,comp_location,comp_phone1,comp_phone2,
              comp_desccription;
    Button view_broduct;
    public comp_details() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        comp_id=getArguments().getString("id");
        comp_pictUrl=getArguments().getString("pictUrl");
        comp_Name=getArguments().getString("name");
        comp_Email=getArguments().getString("Email");
        comp_location=getArguments().getString("Location");
        comp_phone1=getArguments().getString("phone1");
        comp_phone2=getArguments().getString("phone2");
        comp_desccription=getArguments().getString("Description");

        return inflater.inflate(R.layout.fragment_comp_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        comp_pic=view.findViewById(R.id.comp_detailspic);
        comp_name=view.findViewById(R.id.comp_detailsname);
        comp_email=view.findViewById(R.id.comp_detailsEmail);
        location=view.findViewById(R.id.comp_detailsLocation);
        phone1=view.findViewById(R.id.comp_detailsPhone);
        phone2=view.findViewById(R.id.comp_detailsPhone2);
        DEscription=view.findViewById(R.id.comp_detailsdescribtion);
        view_broduct=view.findViewById(R.id.btn_companydproduct);
        view_broduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new companyproduct();
                Bundle bundle= new Bundle();
                bundle.putString("id",comp_id);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.ph_container,fragment).addToBackStack(null).commit();
            }
        });
        if (getActivity() == null) {
            return;
        }
        Glide.with(getActivity())
                .load(comp_pictUrl)
                .into(comp_pic);
        comp_name.setText(comp_Name);
        comp_email.setText("Email:"+comp_Email);
        location.setText(" location:"+comp_location);
        phone1.setText(" phone1:"+comp_phone1);
        phone2.setText(" phone2:"+comp_phone2);
        DEscription.setText(" Description:"+"\n"+comp_desccription);
        DEscription.setMovementMethod(new ScrollingMovementMethod());

    }
}
