package com.testing.otp.mobileotp;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.testing.otp.mobileotp.App.CHANNEL_1_ID;

public class BillingActivity extends AppCompatActivity {

    private NotificationManagerCompat notificationManager;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference myRef,myRef2;
    ProgressDialog progressDialog;
    String pr,qu,pi,n,s;
    TextView t1,t2,t3,t4;
    Button b,b2;
    int total;
    final Context context=this;
    pojo2 p;

    @Override
    public void onBackPressed() {
        createDialog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("LOADING...");
        progressDialog.show();
        firebaseAuth=FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        notificationManager = NotificationManagerCompat.from(this);

        Intent i=getIntent();
        n=i.getStringExtra("name");
        t1=(TextView)findViewById(R.id.product);
        t2=(TextView)findViewById(R.id.quantity);
        t3=(TextView)findViewById(R.id.price);
        //t4=(TextView)findViewById(R.id.total);
        //b=(Button)findViewById(R.id.back);
        //b2=(Button)findViewById(R.id.button2);




        myRef = database.getReference(firebaseAuth.getUid()).child("purchases").child(n);
        display(new MyCallback() {
            @Override
            public void onCallback(String value1,String value2,String value3,String value4,String value5) {
                t1.setText(value1+"\n"+value5);
                t2.setText(value2);
                t3.setText(value3);
                //t4.setText(value5);
                p=new pojo2(value4+" "+value5);
                //sendNotific(value);
            }
        });
        /*b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i=new Intent(BillingActivity.this,NavdPurchase.class);
                i.putExtra("name",n);
                startActivity(i);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(BillingActivity.this,NavdrawerHome.class));
            }
        });*/



    }



    public interface MyCallback {
        void onCallback(String value1,String value2,String value3,String value4,String value5);
    }

    public void display(final MyCallback myCallback)
    {   total=0;
        s="";
        pr="";
        qu="";
        pi="";
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();

                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    userProfile p=dataSnapshot1.getValue(userProfile.class);
                    if(!p.getUserQuantity().equals("0"))
                    {
                        String pro=p.getUserProduct();
                        int q=Integer.parseInt(p.getUserQuantity());
                        int price=Integer.parseInt(p.getUserPrice());
                        pr+=pro+"\n";
                        qu+=p.getUserQuantity()+"\n";
                        pi+="Rs."+p.getUserPrice()+"\n";

                        s+=pro+" "+String.valueOf(q)+" "+"Rs."+String.valueOf(q*price)+"\n";
                        total+=q*price;
                    }

                }
                myCallback.onCallback(pr,qu,pi,s,"TOTAL: Rs."+ String.valueOf(total));
                //myCallback.onCallback(s+"TOTAL: Rs."+String.valueOf(total));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(BillingActivity.this,"error:((",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendNotific(String m){
        Intent i=new Intent(this,MainActivity.class);
        PendingIntent pi=PendingIntent.getActivity(this,0,i,0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.rupee)
                .setContentTitle("BILL :):)")
                .setStyle(new NotificationCompat.BigTextStyle()
                .bigText(m))
                //.setContentText(m)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pi)
                .setColor(Color.GREEN)
                .build();

        notificationManager.notify(1, notification);
    }

    public void createDialog(){
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.billibg_back_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context).setTitle("Transaction completed?");

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final TextView userInput = (TextView) promptsView
                .findViewById(R.id.disp);
        userInput.setText(p.getP());



        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                sendNotific(p.getP());
                                finish();
                                startActivity(new Intent(BillingActivity.this,NavdrawerHome.class));
                            }
                        })
                .setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                dialog.cancel();
                                finish();
                                Intent i=new Intent(BillingActivity.this,NavdPurchase.class);
                                i.putExtra("name",n);
                                startActivity(i);
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }
}
