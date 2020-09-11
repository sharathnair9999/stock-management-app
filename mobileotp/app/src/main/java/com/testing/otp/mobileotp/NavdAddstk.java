package com.testing.otp.mobileotp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class NavdAddstk extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    final Context context=this;
    private Button addbtn;
    FirebaseDatabase database;
    DatabaseReference myRef,myRef3;
    private EditText productEditText,quantityEditText,priceEditText;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    ImageView iv;
    String product,quantity,price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navd_addstk);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Stock");

        productEditText=(EditText)findViewById(R.id.productEt);
        quantityEditText=(EditText)findViewById(R.id.quantityEt);
        priceEditText=(EditText)findViewById(R.id.priceEt);
        addbtn=(Button)findViewById(R.id.addbutton);
        firebaseAuth=FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(firebaseAuth.getUid()).child("stock");


        firebaseStorage= FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        storageReference.child(firebaseAuth.getUid()).child("images/pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).resize(200,200).into(iv);
            }
        });



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

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();
                productEditText.setText("");
                quantityEditText.setText("");
                priceEditText.setText("");

            }
        });

        myRef3=database.getReference(firebaseAuth.getUid()).child("profile");
        View headerView = navigationView.getHeaderView(0);
        final TextView navUsername = (TextView) headerView.findViewById(R.id.name);
        final TextView navEmail=(TextView)headerView.findViewById(R.id.email);
        iv=(ImageView)headerView.findViewById(R.id.imageView);

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
            Toast.makeText(NavdAddstk.this,"Don't use back!!:):)",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navd_addstk, menu);
        return true;
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
            startActivity(new Intent(NavdAddstk.this,ProfileActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(NavdAddstk.this,NavdrawerHome.class));
        } else if (id == R.id.nav_addstock) {

        } else if (id == R.id.nav_display) {
            startActivity(new Intent(NavdAddstk.this,NavdDisplay.class));

        } else if (id == R.id.nav_purchase) {
            //startActivity(new Intent(NavdAddstk.this,NavdPurchase.class));
            createDialog();

        }else if (id == R.id.nav_logout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(NavdAddstk.this,MainActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void createDialog(){
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.customer_name_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context).setTitle("Enter customer name");

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.customername);



        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                String s=userInput.getText().toString();
                                finish();
                                Intent i=new Intent(context,NavdPurchase.class);
                                i.putExtra("name",s);
                                startActivity(i);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }


    public void sendData(){
        product=productEditText.getText().toString().trim();
        quantity=quantityEditText.getText().toString().trim();
        price=priceEditText.getText().toString().trim();

        if(product.equals("") || quantity.equals("") || price.equals(""))
            Toast.makeText(NavdAddstk.this,"something's missing:(:(",Toast.LENGTH_SHORT).show();
        else{
            myRef = database.getReference(firebaseAuth.getUid()).child("stock");
            String id=myRef.push().getKey();
            userProfile userProfileobj=new userProfile(product,quantity,id,price);
            myRef.child(id).setValue(userProfileobj);
            Toast.makeText(NavdAddstk.this,"Added successfully:):)",Toast.LENGTH_SHORT).show();
        }

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
