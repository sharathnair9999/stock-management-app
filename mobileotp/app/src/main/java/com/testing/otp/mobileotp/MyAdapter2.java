package com.testing.otp.mobileotp;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.MyViewHolder>{

    Context context;
    ArrayList<userProfile> profiles;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String pr;

    public MyAdapter2(Context c,ArrayList<userProfile> p){
        context=c;
        profiles=p;
        firebaseAuth=FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(firebaseAuth.getUid()).child("purchases");

    }


    @Override
    public MyAdapter2.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview2,parent,false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.product.setText("PRODUCT: "+profiles.get(position).getUserProduct());

        holder.price.setText("PRICE:Rs."+profiles.get(position).getUserPrice());





        //holder.onClick(profiles.get(position).getUserProduct(),profiles.get(position).getUserQuantity(),profiles.get(position).getUserPrice(),profiles.get(position).getUserid());


    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    public void change(String product,String price,String quantity){
        String id=myRef.push().getKey();
        userProfile userProfileobj=new userProfile(product,quantity,id,price);
        myRef.child(product).setValue(userProfileobj);
    }


    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView product,price,quantity;
        Button btplus,btminus;
        public MyViewHolder(View itemView) {
            super(itemView);
            product=(TextView)itemView.findViewById(R.id.idproduct2);
            quantity=(TextView)itemView.findViewById(R.id.dispquant);
            price=(TextView)itemView.findViewById(R.id.idprice2);
            btplus=(Button)itemView.findViewById(R.id.incre);
            btminus=(Button)itemView.findViewById(R.id.decre);
            final pojo p=new pojo(Integer.parseInt(quantity.getText().toString()));
            btplus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    quantity.setText(String.valueOf(p.getQuant()+1));
                    p.setQuant(p.getQuant()+1);
                    change(product.getText().toString(),price.getText().toString(),String.valueOf(p.getQuant()));
                }
            });
            btminus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i=p.getQuant();
                    if(i>=1){
                        quantity.setText(String.valueOf(p.getQuant()-1));
                        p.setQuant(p.getQuant()-1);
                        change(product.getText().toString(),price.getText().toString(),String.valueOf(p.getQuant()));
                    }

                }
            });



        }
        public void onClick(final String product, final String quantity, String id, final String price){

        }




    }


}
