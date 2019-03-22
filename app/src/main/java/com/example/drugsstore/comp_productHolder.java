package com.example.drugsstore;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class comp_productHolder extends RecyclerView.ViewHolder {
    View mview;
    TextView name,desc,companyname,Price;
    ImageView productpicurl;
    LinearLayout linearLayout;
    public comp_productHolder(@NonNull View itemView) {
        super(itemView);
        mview=itemView;
        linearLayout=mview.findViewById(R.id.row);
    }
    public void setdetails(Context context, String picurl, String Name, String description, String companyName, String price){


        name=mview.findViewById(R.id.Tv_ProductName);
        desc=mview.findViewById(R.id.tv_Description);
        companyname=mview.findViewById(R.id.tv_companyName);
        Price=mview.findViewById(R.id.tv_price);
        productpicurl=mview.findViewById(R.id.iv_productPic);

        name.setText("drugs name:"+Name);
        desc.setText("Description:"+"\n"+description);
        companyname.setText("company Name"+companyName);
        Price.setText(price+"L.E");
        Glide.with(context)
                .load(picurl)
                .into(productpicurl);


    }

}
