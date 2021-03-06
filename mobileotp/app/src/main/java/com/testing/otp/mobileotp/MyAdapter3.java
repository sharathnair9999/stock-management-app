package com.testing.otp.mobileotp;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyAdapter3 extends RecyclerView.Adapter<MyAdapter3.MyViewHolder>{

    Context context;
    ArrayList<userProfile> profiles;
    String name;


    public MyAdapter3(Context c,ArrayList<userProfile> p,String n){
        context=c;
        profiles=p;
        name=n;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview3,parent,false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.product.setText("PRODUCT: "+profiles.get(position).getUserProduct());

        holder.price.setText("PRICE:Rs."+profiles.get(position).getUserPrice());
        holder.onClick(profiles.get(position).getUserProduct(),profiles.get(position).getUserQuantity(),profiles.get(position).getUserid(),profiles.get(position).getUserPrice(),name);


    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView product,quantity,price;
        Button bt;
        public MyViewHolder(View itemView) {
            super(itemView);
            product=(TextView)itemView.findViewById(R.id.idproduct3);

            price=(TextView)itemView.findViewById(R.id.idprice3);
            bt=(Button)itemView.findViewById(R.id.addbtn);


        }
        public void onClick(final String product,final String quantity,final String id,final String price,final String n){
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i=new Intent(context,PurchasingActivity.class);
                    i.putExtra("PRO",product);
                    i.putExtra("QUA",quantity);
                    i.putExtra("id",id);
                    i.putExtra("pri",price);
                    i.putExtra("name",n);
                    context.startActivity(i);

                }
            });
        }
    }

}
