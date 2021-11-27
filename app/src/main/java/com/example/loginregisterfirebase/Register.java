package com.example.loginregisterfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class Register extends AppCompatActivity {
    //Create object of database references class to get access of firebase's real time database
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://loginregisterfirebase-fcbce-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText fullname = findViewById(R.id.fullName);
        final EditText email = findViewById(R.id.email);
        final EditText phone = findViewById(R.id.phone);
        final EditText password = findViewById(R.id.password);
        final EditText repass = findViewById(R.id.repass);

        final Button registerBtn = findViewById(R.id.registerBtn);
        final TextView loginNowBtn = findViewById(R.id.loginNowBtn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get data from edit text into string variables
                final String fullnameText = fullname.getText().toString();
                final String emailText = email.getText().toString();
                final String phoneText = phone.getText().toString();
                final String passwordText = password.getText().toString();
                final String repassText = repass.getText().toString();

                //check user send all the file to firebase before entering
                if (fullnameText.isEmpty()||emailText.isEmpty()||phoneText.isEmpty()||passwordText.isEmpty()){
                    Toast.makeText(Register.this,"Please fill up all the fields", Toast.LENGTH_SHORT).show();
                }

                //checking each password
                else if (!passwordText.equals(repassText)) {
                    Toast.makeText(Register.this, "Password not matching", Toast.LENGTH_SHORT).show();
                }

                else{
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // check phone number is registered before
                            if(snapshot.hasChild(phoneText)){
                                Toast.makeText(Register.this,"Phone number is already registered",Toast.LENGTH_SHORT).show();
                            }
                            else{

                                //Sending data to firebase real time database
                                // Using phone number as primary key so that all under same phone number
                                databaseReference.child("users").child(phoneText).child("fullname").setValue(fullnameText);
                                databaseReference.child("users").child(phoneText).child("email").setValue(emailText);
                                databaseReference.child("users").child(phoneText).child("password").setValue(passwordText);

                                //Show success message
                                Toast.makeText(Register.this,"Registration Successful",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    }

            }
        });

        loginNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}