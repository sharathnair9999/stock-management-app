package com.testing.otp.mobileotp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.function.ToLongBiFunction;


public class FindActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    public static boolean x;
    public static int x2;
    Button b;
    String p,quant;
    SearchView sv2;
    TextView t;
    public static String k,l;
    public static ArrayList<userProfile> ans;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        b=(Button)findViewById(R.id.idfind);
        sv2=(SearchView)findViewById(R.id.sv);
        firebaseAuth= FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();



        t=(TextView)findViewById(R.id.test);
        Intent i=getIntent();
        t.setText(i.getStringExtra("hi"));

        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FindActivity.this,SecondActivity.class));
            }
        });

        ans=new ArrayList<userProfile>();
        /*b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindActivity.k="notfound";
                l=sv2.getQuery().toString().trim();
                readData(new MyCallback() {
                    @Override
                    public void onCallback(String value) {
                        FindActivity.k=value;
                        Log.d("TAG1", value);
                        Log.d("TAG2",FindActivity.k);
                    }
                });
                Log.d("TAG3", k);
                if(FindActivity.k.equals("found")){
                    Toast.makeText(FindActivity.this,"yes",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(FindActivity.this,"no",Toast.LENGTH_SHORT).show();
                }
            }
        });*/



    }


    /*public void onclick(View view){

        readData(new MyCallback() {
            @Override
            public void onCallback(String value) {
                k=value;
            }
        });

        if(x){
            Toast.makeText(FindActivity.this,"yes",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(FindActivity.this,"no",Toast.LENGTH_SHORT).show();
        }
    }*/

    public String check(String s){
        x=false;
        Query q=myRef.orderByChild("userProduct").equalTo(s);
        p="";
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot2:dataSnapshot.getChildren()){
                    p=dataSnapshot2.child("userProduct").getValue().toString();
                    quant=dataSnapshot2.child("userQuantity").getValue().toString();

                    //Toast.makeText(FindActivity.this,p+"\n"+quant,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Toast.makeText(FindActivity.this,"not found",Toast.LENGTH_SHORT).show();
            }
        });

        return p;

    }

   public void check2(final String s){
        FindActivity.x=false;
       myRef.child(s).orderByChild("userProduct").equalTo(s).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if(dataSnapshot.exists())
                   FindActivity.x=true;
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });



   }

    /*public interface MyCallback {
        void onCallback(String value);
    }

    private void readData(final MyCallback myCallback) {


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    userProfile pq=dataSnapshot1.getValue(userProfile.class);
                    if(pq.getUserProduct().equals(FindActivity.l))
                        FindActivity.k="found";
                }
                myCallback.onCallback(FindActivity.k);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }*/


}
