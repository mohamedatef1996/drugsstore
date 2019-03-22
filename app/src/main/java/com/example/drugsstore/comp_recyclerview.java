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
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


/**
 * A simple {@link Fragment} subclass.
 */
public class comp_recyclerview extends Fragment {
    RecyclerView companyREcycler;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;
    FirebaseRecyclerAdapter<company_info,comp_holder>compadapter;
    public comp_recyclerview() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comp_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        companyREcycler=view.findViewById(R.id.comp_recyclerView);
        companyREcycler.setHasFixedSize(true);
        companyREcycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        firebaseDatabase=FirebaseDatabase.getInstance();
        ref=firebaseDatabase.getReference("Company_info");
        final Query company = ref.orderByKey();
        FirebaseRecyclerOptions productOption = new FirebaseRecyclerOptions.Builder<company_info>().setQuery(company, company_info.class).build();
        compadapter=new FirebaseRecyclerAdapter<company_info, comp_holder>(productOption) {
            @Override
            protected void onBindViewHolder(@NonNull comp_holder holder, int position, @NonNull company_info model) {
                holder.comp_details(getActivity(),model.getProfilePicUrl(),model.getName(),model.getLoction(),model.getEmail());
                final String id=getItem(position).getId();
                final String Email=getItem(position).getEmail();
                final String pictUrl=getItem(position).getProfilePicUrl();
                final String Name=getItem(position).getName();
                final String Location=getItem(position).getLoction();
                final String phone1=getItem(position).getPhone();
                final String phone2=getItem(position).getSecondnumber();
                final String Decription=getItem(position).getDescrbtion();

                holder.complist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fragment fragment=new comp_details();
                        Bundle bundle=new Bundle();
                        bundle.putString("id",id);
                        bundle.putString("pictUrl",pictUrl);
                        bundle.putString("name",Name);
                        bundle.putString("Email",Email);
                        bundle.putString("Location",Location);
                        bundle.putString("phone1",phone1);
                        bundle.putString("phone2",phone2);
                        bundle.putString("Description",Decription);
                        fragment.setArguments(bundle);
                        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.ph_container,fragment).addToBackStack(null).commit();
                    }
                });
            }

            @NonNull
            @Override
            public comp_holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view1 =LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.comp_row,viewGroup,false);
               return new comp_holder(view1);
            }
        };
        companyREcycler.setAdapter(compadapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        compadapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        compadapter.stopListening();
    }
}
