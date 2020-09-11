package com.testing.otp.mobileotp;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PurchaseActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference myRef,myRef2;
    RecyclerView recyclerView;
    ArrayList<userProfile> list,list2;
    ProgressDialog progressDialog;
    ImageView iv;
    EditText et;
    MyAdapter3 adapter;
    pojo2 p;
    Button b;
    String s="";
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        DividerItemDecoration itemDecoration=new DividerItemDecoration(this,DividerItemDecoration.VERTICAL );
        recyclerView=(RecyclerView)findViewById(R.id.Myrecycler2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(itemDecoration);
        list=new ArrayList<userProfile>();
        list2=new ArrayList<userProfile>();
        Intent i=getIntent();
        name=i.getStringExtra("name");

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("LOADING...");
        progressDialog.show();
        firebaseAuth=FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        b=(Button)findViewById(R.id.button);
        myRef = database.getReference(firebaseAuth.getUid()).child("stock");

        p=new pojo2("");



        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();

                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    userProfile p=dataSnapshot1.getValue(userProfile.class);

                    list.add(p);
                }
                adapter=new MyAdapter3(PurchaseActivity.this,list,name);
                recyclerView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PurchaseActivity.this,"error:((",Toast.LENGTH_SHORT).show();
            }
        });

        /*b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mod(new MyCallback() {
                    @Override
                    public void onCallback(String value) {
                        //Toast.makeText(PurchaseActivity.this,value,Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(PurchaseActivity.this,FindActivity.class);
                        i.putExtra("hi",value);
                        startActivity(i);

                    }
                });
            }
        });*/

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                Intent i=new Intent(PurchaseActivity.this,BillingActivity.class);
                i.putExtra("name",name);
                startActivity(i);


            }
        });

    }






    public interface MyCallback {
        void onCallback(String value);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        final SearchManager searchManager = (SearchManager) PurchaseActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
            final String s=searchView.getQuery().toString();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    //Toast.makeText(MainActivity.this,newText,Toast.LENGTH_SHORT).show();
                    click2(newText);
                    return false;
                }
            });

        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(PurchaseActivity.this.getComponentName()));
        }
        return super.onCreateOptionsMenu(menu);
    }


    public void click2(String s){
        list.clear();

        Query q=myRef.orderByChild("userProduct").startAt(s).endAt(s+"\uf8ff");

        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot2:dataSnapshot.getChildren())
                {
                    userProfile p=dataSnapshot2.getValue(userProfile.class);

                    list.add(p);
                }
                if(list.isEmpty()){
                    Toast.makeText(PurchaseActivity.this,"not found:(:(",Toast.LENGTH_SHORT).show();
                }

                adapter=new MyAdapter3(PurchaseActivity.this,list,name);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PurchaseActivity.this,"error:((",Toast.LENGTH_SHORT).show();
            }
        });

    }


}
