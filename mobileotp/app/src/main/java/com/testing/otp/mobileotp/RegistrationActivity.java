package com.testing.otp.mobileotp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class RegistrationActivity extends AppCompatActivity {
    private EditText rusername,ruserpassword,remail;
    private TextView back2login;
    private ImageButton reg;
    private FirebaseAuth firebaseAuth;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    FirebaseDatabase database;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setupviews();
        firebaseAuth=FirebaseAuth.getInstance();

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    //upload
                    String user_email=remail.getText().toString().trim();
                    String user_password=ruserpassword.getText().toString().trim();
                    firebaseAuth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                sendEmailVerification();
                                createProfile();
                                sendImg();
                            }
                            else{
                                Toast.makeText(RegistrationActivity.this,"Registration failed",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        back2login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(RegistrationActivity.this,MainActivity.class);
                startActivity(i);
            }
        });
    }

    private void setupviews(){
        rusername=(EditText)findViewById(R.id.etUsername);
        ruserpassword=(EditText)findViewById(R.id.etUserPassword);
        remail=(EditText)findViewById(R.id.etUserEmail);
        back2login=(TextView) findViewById(R.id.tvbacktologin);
        reg=(ImageButton) findViewById(R.id.btnRegister);
    }

    private Boolean validate(){
        Boolean result=false;
        String name=rusername.getText().toString();
        String email=remail.getText().toString();
        String pass=ruserpassword.getText().toString();
        if(name.isEmpty() || email.isEmpty() || pass.isEmpty()){
            Toast.makeText(this,"Enter all the details",Toast.LENGTH_SHORT).show();
        }
        else{
            result=true;
        }
        return result;
    }


    private void sendEmailVerification(){
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegistrationActivity.this,"verification mail sent!!",Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(RegistrationActivity.this,MainActivity.class));
                    }else{
                        Toast.makeText(RegistrationActivity.this," server error:(",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void createProfile(){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(firebaseAuth.getUid()).child("profile");
        String n=rusername.getText().toString();
        String e=remail.getText().toString();
        String id=myRef.push().getKey();

        userProfile2 userProfileobj2=new userProfile2(n,e,id);
        myRef.child(id).setValue(userProfileobj2);

    }
    public void sendImg(){
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("LOADING...");
        progressDialog.show();
        firebaseStorage= FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();

        storageReference.child("default/man").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                StorageReference imageRef=storageReference.child(firebaseAuth.getUid()).child("images").child("pic");
                UploadTask uploadTask=imageRef.putFile(uri);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(RegistrationActivity.this,"Failed",Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Toast.makeText(RegistrationActivity.this,"updated",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });








    }

}
