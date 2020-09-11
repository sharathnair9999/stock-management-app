package com.testing.otp.mobileotp;

import android.app.LauncherActivity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.testing.otp.mobileotp.App.CHANNEL_1_ID;
public class DisplayActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private int counter;
    private NotificationManagerCompat notificationManager;
    private TextView tvdigit,tv1,tv2;
    private Button bdecre;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String product,quantity;
    RecyclerView recyclerView;
    ArrayList<userProfile> list;
    ProgressDialog progressDialog;
    String pro,quant,id;

    ImageView iv;
    EditText et;
    MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        DividerItemDecoration itemDecoration=new DividerItemDecoration(this,DividerItemDecoration.VERTICAL );
        recyclerView=(RecyclerView)findViewById(R.id.Myrecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(itemDecoration);
        list=new ArrayList<userProfile>();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("LOADING...");
        progressDialog.show();
        firebaseAuth=FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(firebaseAuth.getUid()).child("stock");


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();

                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    userProfile p=dataSnapshot1.getValue(userProfile.class);
                    
                    list.add(p);
                }
                adapter=new MyAdapter(DisplayActivity.this,list);
                recyclerView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DisplayActivity.this,"error:((",Toast.LENGTH_SHORT).show();
            }
        });




    }


    @Override
    /*public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logoutMenu:{
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(DisplayActivity.this,MainActivity.class));
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }*/

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        final SearchManager searchManager = (SearchManager) DisplayActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
            final String s=searchView.getQuery().toString();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    //click1(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    //Toast.makeText(MainActivity.this,newText,Toast.LENGTH_SHORT).show();
                    click1(newText);
                    return false;
                }
            });

        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(DisplayActivity.this.getComponentName()));
        }
        return super.onCreateOptionsMenu(menu);
    }

    public void click1(String s){
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
                    Toast.makeText(DisplayActivity.this,"not found:(:(",Toast.LENGTH_SHORT).show();
                }

                adapter=new MyAdapter(DisplayActivity.this,list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DisplayActivity.this,"error:((",Toast.LENGTH_SHORT).show();
            }
        });

    }

}
