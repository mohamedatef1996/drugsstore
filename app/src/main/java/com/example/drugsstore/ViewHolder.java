package com.example.drugsstore;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ViewHolder extends RecyclerView.ViewHolder {
    View mview;
    TextView name,desc,companyname,Price;
    ImageView productpicurl,delete,edit;


    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        mview=itemView;
        delete=mview.findViewById(R.id.deleteProduct);
        edit=mview.findViewById(R.id.compProdectedit);
    }

    public void serdetails(Context context,String picurl,String Name,String description,String companyName,String price){


        name=mview.findViewById(R.id.ProductName);
        desc=mview.findViewById(R.id.Description);
        companyname=mview.findViewById(R.id.companyName);
        Price=mview.findViewById(R.id.price);
        productpicurl=mview.findViewById(R.id.productPic);

        name.setText("drugs name:"+Name);
        desc.setText("Description:"+"\n"+description);
        companyname.setText("company Name"+companyName);
        Price.setText(price+"L.E");
        Glide.with(context)
                .load(picurl)
                .into(productpicurl);


    }






}
