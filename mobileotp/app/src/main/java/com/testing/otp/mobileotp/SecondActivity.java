package com.testing.otp.mobileotp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.HashSet;
import java.util.Set;

public class SecondActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    final Context context=this;
    public int counter,it=0,kp=100;
    private NotificationManagerCompat notificationManager;
    private TextView tvdigit;
    private Button btnpurch,addbtn,btndisplay;
    private EditText productEditText,quantityEditText,priceEditText;
    String product,quantity,price;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<userProfile> list;
    ArrayList<String> list2;
    boolean x;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        notificationManager = NotificationManagerCompat.from(this);


        addbtn=(Button)findViewById(R.id.addbutton);
        btndisplay=(Button)findViewById(R.id.displaybtn);
        btnpurch=(Button)findViewById(R.id.btnpurchase);
        firebaseAuth=FirebaseAuth.getInstance();
        productEditText=(EditText)findViewById(R.id.productEt);
        quantityEditText=(EditText)findViewById(R.id.quantityEt);
        priceEditText=(EditText)findViewById(R.id.priceEt);
        //firebaseDatabase=FirebaseDatabase.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(firebaseAuth.getUid()).child("stock");
        list=new ArrayList<userProfile>();
        list2=new ArrayList<String>();
        list.clear();



       /* myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userProfile userProfileobj2=dataSnapshot.getValue(userProfile.class);
                counter=Integer.parseInt(userProfileobj2.getUserQuantity());
                tvdigit.setText(String.valueOf(counter));

                product=userProfileobj2.getUserProduct();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SecondActivity.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();
            }
        });*/



        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();

            }
        });

        btndisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SecondActivity.this,DisplayActivity.class));
            }
        });

        btnpurch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //startActivity(new Intent(SecondActivity.this,PurchaseActivity.class));

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
                                        Intent i=new Intent(context,PurchaseActivity.class);
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

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logoutMenu:{
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(SecondActivity.this,MainActivity.class));
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void sendData(){
        product=productEditText.getText().toString().trim();
        quantity=quantityEditText.getText().toString().trim();
        price=priceEditText.getText().toString().trim();
        list2.clear();
        //ch(product,quantity);
        /*if(ch(product)){
            Toast.makeText(SecondActivity.this,"Already existing!!",Toast.LENGTH_SHORT).show();
        }
        else {

        */
            myRef = database.getReference(firebaseAuth.getUid()).child("stock");
            String id=myRef.push().getKey();
            userProfile userProfileobj=new userProfile(product,quantity,id,price);
            myRef.child(id).setValue(userProfileobj);
            Toast.makeText(SecondActivity.this,"Added successfully:):)",Toast.LENGTH_SHORT).show();

        //}

        //Toast.makeText(SecondActivity.this,"ans="+ch(product,quantity),Toast.LENGTH_SHORT).show();






    }

    public void findclick(View view){
        startActivity(new Intent(SecondActivity.this,FindActivity.class));
    }

    public String ch(final String ip,final String q){

        myRef.orderByChild("userProduct").equalTo(ip).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()){
                        list2.add("yes");
                        //Toast.makeText(SecondActivity.this,"Already existing!!",Toast.LENGTH_SHORT).show();
                    }







            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return String.valueOf(list2.isEmpty());

    }

}
