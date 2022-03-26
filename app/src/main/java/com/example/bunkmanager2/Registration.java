package com.example.bunkmanager2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {
    EditText mail, password, name, phone, registrationnum;
    Button register;
    TextView logintext;
    ProgressBar bar1;
    FirebaseAuth fAuth;
    FirebaseFirestore database;
    String userid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        password = findViewById(R.id.user_password);
        register = findViewById(R.id.button4);
        mail = findViewById(R.id.Mail);
        bar1 = findViewById(R.id.progressBar);
        name = findViewById(R.id.name_of_user);
        registrationnum = findViewById(R.id.user_reg_no);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        fAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),Dashboard.class));
            finish();
        }
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = mail.getText().toString();
                String Name = name.getText().toString();
                String Pass = password.getText().toString();
                String Registration = registrationnum.getText().toString();

                if (TextUtils.isEmpty(Email)) {
                    mail.setError("Mailid is Required.");
                    return;
                }
                if (TextUtils.isEmpty(Name)) {
                    name.setError("Name can not be empty.");
                    return;
                }

                if (TextUtils.isEmpty(Registration)) {
                    registrationnum.setError("Mailid is Required.");
                    return;
                }
                if (Registration.length() != 10) {
                    registrationnum.setError("Enter Correct One.");
                    return;
                }
                if (TextUtils.isEmpty(Pass)) {
                    password.setError("Password is required.");
                }
                if (Pass.length() < 8) {
                    password.setError("Password is too short");
                }
                bar1.setVisibility(View.VISIBLE);

                fAuth.createUserWithEmailAndPassword(Email, Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Registration.this, "Registration Sucessfull", Toast.LENGTH_SHORT).show();
                            DocumentReference documentReference = database.collection("Students").document(Email);
                            Map<String, Object> user = new HashMap<>();
                            user.put("Email ID", Email);
                            user.put("Name", Name);
                            user.put("Registration No", Registration);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Registration.this, "User Profile created for " + Name, Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getApplicationContext(),Dashboard.class));
                                    bar1.setVisibility(View.INVISIBLE);
                                    finish();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Registration.this, "Profile Creation Failed.", Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            Toast.makeText(Registration.this, "Registration Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            bar1.setVisibility(View.INVISIBLE);
                        }
                    }
                });


            }


        });

    }


    public void login(View view) {
        Intent i = new Intent(this,Login.class);
        startActivity(i);
        finish();
    }
}