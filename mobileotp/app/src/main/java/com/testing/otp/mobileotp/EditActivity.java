package com.testing.otp.mobileotp;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import static com.testing.otp.mobileotp.App.CHANNEL_1_ID;

public class EditActivity extends AppCompatActivity implements ExampleDialog.ExampleDialogListener{
    public TextView tv1,tv2,tv3;
    public int pos,counter=0;
    public String id;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String p,q,pr;
    Button b1,b2,b3,pricebutton;
    private NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        tv1=(TextView)findViewById(R.id.textView1);
        tv2=(TextView)findViewById(R.id.textView2);
        tv3=(TextView)findViewById(R.id.textView3);
        notificationManager = NotificationManagerCompat.from(this);

        b1=(Button)findViewById(R.id.iddecr);
        b2=(Button)findViewById(R.id.idincre);
        b3=(Button)findViewById(R.id.iddone);
        pricebutton=(Button)findViewById(R.id.priceBtn);
        Intent i=getIntent();

        p=i.getStringExtra("PRO");
        q=i.getStringExtra("QUA");
        pr=i.getStringExtra("pri");
        tv1.setText(p);
        tv2.setText(q);
        tv3.setText("price:"+pr);
        id=i.getStringExtra("id");


        firebaseAuth= FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(firebaseAuth.getUid()).child("stock");
        counter=Integer.parseInt(q);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(counter<=0){
                    b1.setEnabled(false);

                }
                else{
                    counter--;
                    tv2.setText(String.valueOf(counter));
                    changeData();
                    if(counter<=15){
                        sendNotific(p+" quantity:"+String.valueOf(counter));
                    }
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

                    tv2.setText(String.valueOf(counter));
                    changeData();


                }

            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(EditActivity.this,NavdDisplay.class));

            }
        });


        pricebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });


    }

    public void openDialog(){
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");


    }

    public void applyTexts(String price) {
        pr=price;
        if(pr.equals(""))
            Toast.makeText(this,"enter price again!!",Toast.LENGTH_SHORT).show();
        else
        {
            tv3.setText("price:"+pr);
            changeData();
        }



    }

    public void changeData(){

        userProfile userProfileobj=new userProfile(p,String.valueOf(counter),id,pr);


        myRef.child(id).setValue(userProfileobj);

    }

    public void ondelete(View view){
        myRef.child(id).removeValue();
        finish();
        startActivity(new Intent(EditActivity.this,NavdDisplay.class));

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
