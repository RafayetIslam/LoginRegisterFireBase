package com.example.loginregisterfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

 //   DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://loginregisterfirebase-fcbce-default-rtdb.firebaseio.com/");
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://loginregisterfirebase-fcbce-default-rtdb.firebaseio.com/");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText phone=findViewById(R.id.phone);
        final EditText password=findViewById(R.id.password);
        final Button loginBtn=findViewById(R.id.loginBtn);
        final TextView registerNowBtn=findViewById(R.id.registerNowBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phoneText = phone.getText().toString();
                final String passwordText = password.getText().toString();

                if(phoneText.isEmpty()||passwordText.isEmpty()){
                    Toast.makeText(Login.this,"Please enter your mobile or password",Toast.LENGTH_SHORT).show();
                }
                else{
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            //Check if mobile number is exist in firebase database
                            if (snapshot.hasChild(phoneText)) {
                                //mobile exist in firebase database
                                final String getPassword = snapshot.child(phoneText).child("password").getValue(String.class);

                                //Open main activity
                                if (getPassword.equals(passwordText)) {
                                    Toast.makeText(Login.this, "Successfully log in", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Login.this, MainActivity.class));
                                    finish();

                                } else {
                                    Toast.makeText(Login.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Login.this, "Invalid Mobile Number", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        registerNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Register.class));
            }
        });

    }
}