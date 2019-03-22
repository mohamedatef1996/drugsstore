package com.example.drugsstore;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class comp_holder extends RecyclerView.ViewHolder {
    View mview;
    com.mikhaellopez.circularimageview.CircularImageView comp_pic;
    TextView name,location,email;
    LinearLayout complist;
    public comp_holder(@NonNull View itemView) {
        super(itemView);
        mview=itemView;
        complist=mview.findViewById(R.id.comp_list);
    }
    public void comp_details(Context context,String comp_url,String Name,String Location,String Email){

        comp_pic=mview.findViewById(R.id.comp_pic);
        name=mview.findViewById(R.id.comp_name);
        location=mview.findViewById(R.id.comp_location);
        email=mview.findViewById(R.id.comp_Email);
        Glide.with(context)
                .load(comp_url)
                .into(comp_pic);
        name.setText(Name);
        location.setText(Location);
        email.setText(Email);

    }
}
