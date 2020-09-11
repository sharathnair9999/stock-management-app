package com.testing.otp.mobileotp;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.testing.otp.mobileotp.App.CHANNEL_1_ID;

public class PurchasingActivity extends AppCompatActivity {
    private NotificationManagerCompat notificationManager;
    TextView t1,t2,t3,t4;
    Button b1,b2,b3,b4;
    String pro,quant,id,price;
    public int pos,counter=0,init;
    userProfile p;
    String nm;
    pojo2 pq;
    private FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    FirebaseDatabase database;
    DatabaseReference myRef,myRef2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchasing);
        t1=(TextView)findViewById(R.id.textView1);
        t2=(TextView)findViewById(R.id.textView2);
        t3=(TextView)findViewById(R.id.textView3);
        t4=(TextView)findViewById(R.id.textView4);
        notificationManager = NotificationManagerCompat.from(this);

        b1=(Button)findViewById(R.id.iddecr);
        b2=(Button)findViewById(R.id.idincre);
        b3=(Button)findViewById(R.id.iddone);
        b4=(Button)findViewById(R.id.idcancel);
        Intent i=getIntent();

        pq=new pojo2("");

        pro=i.getStringExtra("PRO");
        quant=i.getStringExtra("QUA");
        id=i.getStringExtra("id");
        price=i.getStringExtra("pri");
        //pq.setP(i.getStringExtra("name"));
        //nm=pq.getP();
        nm=i.getStringExtra("name");
        t1.setText("Product:"+pro);
        t3.setText("Stock:"+quant);
        t2.setText("Price: Rs."+price);

        firebaseAuth= FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(firebaseAuth.getUid());
        myRef2=myRef.child("purchases").child(nm);
        counter=0;
        init=Integer.parseInt(quant);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("LOADING...");
        progressDialog.show();
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(counter<=0){
                    b1.setEnabled(false);

                }
                else{
                    counter--;
                    t4.setText(String.valueOf(counter));
                    changeData1();

                }
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                if(counter<=0){
                    b1.setEnabled(false);

                }
                else {
                    b1.setEnabled(true);

                    t4.setText(String.valueOf(counter));
                    changeData1();


                }

            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(counter>init){
                    Toast.makeText(PurchasingActivity.this,"reduce quantity :(:(",Toast.LENGTH_SHORT).show();
                }
                else{
                    changeData2();
                    if(init-counter<=15)
                        sendNotific(pro+" quantity:"+String.valueOf(init-counter));
                    finish();
                    Intent i=new Intent(PurchasingActivity.this,PurchaseActivity.class);
                    i.putExtra("name",nm);
                    startActivity(i);

                }
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });


        //myRef2=myRef.child("purchases").child(pq.getP()).child(id);



        myRef2.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                if(dataSnapshot.exists()){
                    p=dataSnapshot.getValue(userProfile.class);
                    counter=Integer.parseInt(p.getUserQuantity());
                    t4.setText(String.valueOf(counter));
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void changeData1(){
        //myRef2=myRef.child("purchases").child(pq.getP());

        userProfile userProfileobj=new userProfile(pro,String.valueOf(counter),id,price);


        myRef2.child(id).setValue(userProfileobj);
    }
    public void changeData2(){
        //myRef2=myRef.child("stock");
        userProfile userProfileobj=new userProfile(pro,String.valueOf(init-counter),id,price);


        myRef.child("stock").child(id).setValue(userProfileobj);
    }

    public void cancel(){
        //myRef2=myRef.child("purchases").child(pq.getP());
        myRef2.child(id).removeValue();
        finish();
        Intent i=new Intent(PurchasingActivity.this,PurchaseActivity.class);
        i.putExtra("name",nm);
        startActivity(i);

    }

    public void sendNotific(String m){
        Intent i=new Intent(this,MainActivity.class);
        PendingIntent pi=PendingIntent.getActivity(this,0,i,0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_mms)
                .setContentTitle("LOW STOCK!!!")
                .setContentText(m)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pi)
                .setColor(Color.GREEN)
                .build();

        notificationManager.notify(1, notification);
    }
}
