package com.testing.otp.mobileotp;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NavdPurchase extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference myRef,myRef3;
    RecyclerView recyclerView;
    ArrayList<userProfile> list,list2;
    ProgressDialog progressDialog;
    MyAdapter3 adapter;
    pojo2 p;
    Button b;
    String s="";
    String name;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navd_purchase);

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



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Purchase");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();

                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    userProfile p=dataSnapshot1.getValue(userProfile.class);

                    list.add(p);
                }
                adapter=new MyAdapter3(NavdPurchase.this,list,name);
                recyclerView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(NavdPurchase.this,"error:((",Toast.LENGTH_SHORT).show();
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                Intent i=new Intent(NavdPurchase.this,BillingActivity.class);
                i.putExtra("name",name);
                startActivity(i);


            }
        });
        myRef3=database.getReference(firebaseAuth.getUid()).child("profile");
        View headerView = navigationView.getHeaderView(0);
        final TextView navUsername = (TextView) headerView.findViewById(R.id.name);
        final TextView navEmail=(TextView)headerView.findViewById(R.id.email);
        final ImageView iv=(ImageView)headerView.findViewById(R.id.imageView);
        displayProfile(new MyCallback() {
            @Override
            public void onCallback(String value1, String value2) {
                navUsername.setText(value1);
                navEmail.setText(value2);
                Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/mobile-otp-2a474.appspot.com/o/default%2Fman.png?alt=media&token=cfd6ad80-b97c-4040-bc5b-b94573306a5b")
                        .placeholder(R.drawable.man)
                        .into(iv);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            Toast.makeText(NavdPurchase.this,"Don't use back!!:):)",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navd_purchase, menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        final SearchManager searchManager = (SearchManager) NavdPurchase.this.getSystemService(Context.SEARCH_SERVICE);

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
            searchView.setSearchableInfo(searchManager.getSearchableInfo(NavdPurchase.this.getComponentName()));
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            finish();
            startActivity(new Intent(NavdPurchase.this,ProfileActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Toast.makeText(NavdPurchase.this,"please continue the transaction",Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_addstock) {
            Toast.makeText(NavdPurchase.this,"please continue the transaction",Toast.LENGTH_SHORT).show();


        } else if (id == R.id.nav_display) {
            Toast.makeText(NavdPurchase.this,"please continue the transaction",Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_purchase) {

        }else if (id == R.id.nav_logout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(NavdPurchase.this,MainActivity.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                    Toast.makeText(NavdPurchase.this,"not found:(:(",Toast.LENGTH_SHORT).show();
                }

                adapter=new MyAdapter3(NavdPurchase.this,list,name);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(NavdPurchase.this,"error:((",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public interface MyCallback {
        void onCallback(String value1,String value2);
    }

    public void displayProfile(final MyCallback myCallback){



        myRef3.addListenerForSingleValueEvent(new ValueEventListener() {
            String us,ue;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    userProfile2 p=dataSnapshot1.getValue(userProfile2.class);

                    us=p.getUserName();
                    ue=p.getUserEmail();
                }

                myCallback.onCallback(us,ue);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
