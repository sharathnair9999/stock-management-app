package com.testing.otp.mobileotp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;

public class MainActivity extends AppCompatActivity {
    private EditText name;
    private EditText password;
    private TextView info,register;
    private ImageButton login;
    private int counter=5;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private FirebaseAuth.AuthStateListener authStateListener;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name=(EditText)findViewById(R.id.etName);
        password=(EditText)findViewById(R.id.etPassword);
        info=(TextView) findViewById(R.id.tvInfo);
        login=(ImageButton) findViewById(R.id.btnLogin);
        register=(TextView)findViewById(R.id.tvRegister);

        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        progressDialog=new ProgressDialog(this);

        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null && firebaseAuth.getCurrentUser().isEmailVerified()){
                    startActivity(new Intent(MainActivity.this,NavdrawerHome.class));
                    finish();
                }
            }
        };

        /*if(user!=null){
            finish();
            startActivity(new Intent(MainActivity.this,SecondActivity.class));
        }*/
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate1()){
                    validate(name.getText().toString(),password.getText().toString());
                }

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,RegistrationActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    private void validate(String username, String userpassword){
        progressDialog.setMessage("LOADING...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(username,userpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    //Toast.makeText(MainActivity.this,"welcome!!",Toast.LENGTH_SHORT).show();
                    checkemailverification();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this,"Login failed",Toast.LENGTH_SHORT).show();
                    counter--;
                    info.setText("No.of attempts remaining:"+String.valueOf(counter));
                    if(counter<=0){
                        login.setEnabled(false);


                    }
                }
            }
        });
    }

    private void checkemailverification(){
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        Boolean emailflag=firebaseUser.isEmailVerified();

        if(emailflag){
            finish();
            startActivity(new Intent(MainActivity.this,NavdrawerHome.class));

        }else{
            Toast.makeText(this,"please check your email for verification link..!!",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
    private Boolean validate1(){
        Boolean result=false;

        String email=name.getText().toString();
        String pass=password.getText().toString();
        if(email.isEmpty() || pass.isEmpty()){
            Toast.makeText(this,"Enter all the details",Toast.LENGTH_SHORT).show();
        }
        else{
            result=true;
        }
        return result;
    }
}
