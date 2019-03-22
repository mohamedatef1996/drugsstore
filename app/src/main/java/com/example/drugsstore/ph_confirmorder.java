package com.example.drugsstore;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


/**
 * A simple {@link Fragment} subclass.
 */
public class ph_confirmorder extends Fragment {
    ImageView iv_product,Increment,decrement;
    TextView  tv_name,tv_compname,tv_describtion,tv_Total,tv_counter;
    String id,pictUrl,name,Compname,description;
    int Price;
    int counter;
    public ph_confirmorder() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        id=getArguments().getString("id");
        pictUrl=getArguments().getString("pictUrl");
        name=getArguments().getString("name");
        Compname=getArguments().getString("compname");
        description=getArguments().getString("Description");
        counter=1;
        return inflater.inflate(R.layout.fragment_ph_confirmorder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iv_product=view.findViewById(R.id.iv_product);
        tv_name=view.findViewById(R.id.tv_name);
        tv_compname=view.findViewById(R.id.tv_company);
        tv_describtion=view.findViewById(R.id.tv_describtion);
        tv_Total=view.findViewById(R.id.total);
        tv_counter=view.findViewById(R.id.tv_counter);
        Increment=view.findViewById(R.id.imageView2);
        decrement=view.findViewById(R.id.decrement);
        if (getActivity() == null) {
            return;
        }
        Glide.with(getActivity())
                .load(pictUrl)
                .into(iv_product);
        tv_name.setText(name);
        tv_compname.setText(Compname);
        tv_describtion.setText(description);
        tv_counter.setText(String.valueOf(counter));
        int total=(counter*Price);
        Increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    tv_counter.setText(String.valueOf(counter++));

            }
        });
        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter != 0) {
                    tv_counter.setText(String.valueOf(counter--));
        }}});
    }
}
