package com.example.drugsstore;


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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


/**
 * A simple {@link Fragment} subclass.
 */
public class companyproduct extends Fragment {

    RecyclerView compRecycler;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;
    private FirebaseRecyclerAdapter<comp_productinfo, comp_productHolder> comp_productadapter;
    String comp_id;
    public companyproduct() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        comp_id=getArguments().getString("id");
        return inflater.inflate(R.layout.fragment_companyproduct, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        compRecycler=view.findViewById(R.id.RECYCLERVIEW);
        compRecycler.setHasFixedSize(true);
        compRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        firebaseDatabase=FirebaseDatabase.getInstance();
        ref=firebaseDatabase.getReference("company_product").child(comp_id);
        final Query comp_product = ref.orderByKey();
        FirebaseRecyclerOptions productOption = new FirebaseRecyclerOptions.Builder<comp_productinfo>().setQuery(comp_product, comp_productinfo.class).build();
        comp_productadapter=new FirebaseRecyclerAdapter<comp_productinfo, comp_productHolder>(productOption) {
            @Override
            protected void onBindViewHolder(@NonNull comp_productHolder holder, int position, @NonNull comp_productinfo model) {
                holder.setdetails(getContext(),model.getPicurl(),model.getName(),model.getDescription(),model.getCompName(),model.getPrice());
                final String id=getItem(position).getId();
                final String picturl=getItem(position).getPicurl();
                final String Price=getItem(position).getPrice();
                final String Name=getItem(position).getName();
                final String compName=getItem(position).getCompName();
                final String description=getItem(position).getDescription();
                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fragment fragment=new ph_confirmorder();
                        Bundle bundle=new Bundle();
                        bundle.putString("id",id);
                        bundle.putString("Price",Price);
                        bundle.putString("pictUrl",picturl);
                        bundle.putString("name",Name);
                        bundle.putString("compname",compName);
                        bundle.putString("Description",description);
                        fragment.setArguments(bundle);
                        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.ph_container,fragment).addToBackStack(null).commit();
                    }
                });
            }

            @NonNull
            @Override
            public comp_productHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.row, viewGroup, false);

                return new comp_productHolder(view);
            }
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
