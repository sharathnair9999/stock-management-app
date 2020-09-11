package com.testing.otp.mobileotp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.UnicodeSetSpanner;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {
    Button b1,b2,b3;
    final Context context=this;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseDatabase database;
    DatabaseReference myRef,myRef3;
    private static int PICK_IMAGE=123;
    ImageView iv;
    Uri imagePath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK && data.getData()!=null){
            imagePath=data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),imagePath);
                iv.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        b1=(Button)findViewById(R.id.password);
        b2=(Button)findViewById(R.id.profilepic);
        b3=(Button)findViewById(R.id.update);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        iv=(ImageView)findViewById(R.id.imagev1);
        storageReference=firebaseStorage.getReference();
        //StorageReference myref=storageReference.child(firebaseAuth.getUid());
        final TextView navUsername = (TextView) findViewById(R.id.name1);
        final TextView navEmail=(TextView)findViewById(R.id.email1);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("LOADING...");
        progressDialog.show();

        displayProfile(new MyCallback() {
            @Override
            public void onCallback(String value1, String value2) {
                navUsername.setText(value1);
                navEmail.setText(value2);
                img();

            }
        });


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog();

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i,"Select image"),PICK_IMAGE);


                //sendImg();

            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imagePath==null)
                    Toast.makeText(ProfileActivity.this,"nothing to update",Toast.LENGTH_SHORT).show();
                else
                    sendImg();
            }
        });



    }

    @Override
    public void onBackPressed() {
          //super.onBackPressed();
        //Toast.makeText(NavdAddstk.this,"Don't use back!!:):)",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(ProfileActivity.this,NavdrawerHome.class));
        finish();

    }

    public void createDialog(){
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.change_password_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context).setTitle("Enter new password");

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.newpassword);



        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                String s=userInput.getText().toString();
                                changePass(s);

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

    public void changePass(String s){
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("LOADING...");
        progressDialog.show();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();


        firebaseUser.updatePassword(s).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if(task.isSuccessful())
                    Toast.makeText(ProfileActivity.this,"Password changed",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(ProfileActivity.this,"failed",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendImg(){
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("LOADING...");
        progressDialog.show();
        StorageReference imageRef=storageReference.child(firebaseAuth.getUid()).child("images").child("pic");
        UploadTask uploadTask=imageRef.putFile(imagePath);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(ProfileActivity.this,"Failed",Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Toast.makeText(ProfileActivity.this,"updated",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface MyCallback {
        void onCallback(String value1,String value2);
    }
    public void displayProfile(final MyCallback myCallback){

        myRef3=database.getReference(firebaseAuth.getUid()).child("profile");

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

    public void img(){
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        storageReference.child(firebaseAuth.getUid()).child("images/pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).resize(200,200).into(iv);
            }
        });
        progressDialog.dismiss();
    }
}
