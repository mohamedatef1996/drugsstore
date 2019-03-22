package com.example.drugsstore;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


/**
 * A simple {@link Fragment} subclass.
 */
public class comp_product extends Fragment {

    RecyclerView compRecycler;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;
    private FirebaseRecyclerAdapter<comp_productinfo, ViewHolder> comp_productadapter;

    public comp_product() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_comp_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        compRecycler=view.findViewById(R.id.comp_Recyclerview);
        compRecycler.setHasFixedSize(true);
        compRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        firebaseDatabase=FirebaseDatabase.getInstance();
        FirebaseUser currentuser= FirebaseAuth.getInstance().getCurrentUser();
        final String userid=currentuser.getUid();
        ref=firebaseDatabase.getReference("company_product").child(userid);
        final Query comp_product = ref.orderByKey();
        FirebaseRecyclerOptions productOption = new FirebaseRecyclerOptions.Builder<comp_productinfo>().setQuery(comp_product, comp_productinfo.class).build();
        comp_productadapter=new FirebaseRecyclerAdapter<comp_productinfo, ViewHolder>(productOption) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, final int position, @NonNull final comp_productinfo model) {
                holder.serdetails(getActivity(),model.getPicurl(),model.getName(),model.getDescription(),model.getCompName(),model.getPrice());
                final String id=getItem(position).getId();
                final String picUrl=getItem(position).getPicurl();
                final String Name=getItem(position).getName();
                final String Description=getItem(position).getDescription();
                final String Price=getItem(position).getPrice();
                final String companyName=getItem(position).getCompName();
                holder.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    Fragment fragment=new editcomp_product();
                    Bundle b=new Bundle();
                    b.putString("id",id);
                    b.putString("picUrl",picUrl);
                    b.putString("Name",Name);
                    b.putString("Description",Description);
                    b.putString("Price",Price);
                    b.putString("companyName",companyName);
                    fragment.setArguments(b);
                        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.Fragment_container,fragment).addToBackStack(null).commit();
                    }
                });



                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                        alertDialogBuilder.setTitle("delete");
                        alertDialogBuilder.setMessage("are you sure to delete this item");
                        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                comp_productadapter.getRef(position).removeValue();
                                Toast.makeText(getActivity(),"item deleted successfuly...",Toast.LENGTH_SHORT).show();
                            }
                        });
                        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();


                    }
                });
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.comp_product_row, viewGroup, false);

                return new ViewHolder(view);            }
        };
        compRecycler.setAdapter(comp_productadapter);




    }


    @Override
    public void onStart() {
        super.onStart();
        comp_productadapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        comp_productadapter.stopListening();
    }
}
