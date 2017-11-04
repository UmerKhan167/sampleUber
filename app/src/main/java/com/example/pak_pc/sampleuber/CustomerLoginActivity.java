package com.example.pak_pc.sampleuber;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class CustomerLoginActivity extends AppCompatActivity {
    private EditText email,password;
    private Button login,register;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private static final int RC_SIGN_IN=1 ;

    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);


        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);

            firebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
            authStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user !=null){
                        String user_id = user.getUid();
                        Intent intent = new Intent(CustomerLoginActivity.this,CustomerMapActivity.class);
                        startActivity(intent);
                        mDatabaseReference.child("Users").child("Customers").child(user_id).setValue(true);
                    }else {
                        startActivityForResult(
                                AuthUI.getInstance()
                                        .createSignInIntentBuilder()
                                        .setIsSmartLockEnabled(false)
                                        .setAvailableProviders(
                                                Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                        .build(), RC_SIGN_IN);
                    }
                }
            };


//            register.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    final String Email =  email.getText().toString();
//                    final String Password= password.getText().toString();
//                    firebaseAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener
//                            (CustomerLoginActivity.this, new OnCompleteListener<AuthResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                    if(!task.isSuccessful() ){
//                                        Toast.makeText(CustomerLoginActivity.this,"Sign up error",Toast.LENGTH_LONG).show();
//                                    }else {
//                                        String user_id = firebaseAuth.getCurrentUser().getUid();
//                                        DatabaseReference user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(user_id);
//                                        user_db.setValue(true);
//                                    }
//
//                                }
//                            });
//                }
//            });
//
//            login.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    final String Email =  email.getText().toString();
//                    final String Password= password.getText().toString();
//                    firebaseAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(CustomerLoginActivity.this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if(!task.isSuccessful() ) {
//                                Toast.makeText(CustomerLoginActivity.this, "Sign in error", Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    });
//
//                }
//            });

        }
        @Override
        protected void onResume(){
            super.onResume();
            firebaseAuth.addAuthStateListener(authStateListener);
        }
        @Override
        protected void onPause(){
            super.onPause();
            if (authStateListener != null) {
                firebaseAuth.removeAuthStateListener(authStateListener);
            }
        }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent= new Intent(CustomerLoginActivity.this,MainActivity.class);
        startActivity(intent);
    }

    /**
     * onActivityResult processes the result of login requests
     **/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            // if login requset is successful show a toast,
            // otherwise tell user that the request failed and exit.
            if (resultCode == RESULT_OK) {
                Toast.makeText(CustomerLoginActivity.this, "Signed in successfully!!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(CustomerLoginActivity.this, "Sign in cancelled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}