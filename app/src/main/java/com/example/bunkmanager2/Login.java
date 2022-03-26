package com.example.bunkmanager2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static android.graphics.Color.*;

public class Login extends AppCompatActivity {
    EditText mail, password;
    Button b1;
    ProgressBar bar2;
    TextView forgetpassword, register_page;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mail = findViewById(R.id.Mail);
        password = findViewById(R.id.password);
        b1 = findViewById(R.id.login);
        bar2 = findViewById(R.id.progressBar2);
        register_page = findViewById(R.id.textView4);
        forgetpassword = findViewById(R.id.textView8);
        fAuth = FirebaseAuth.getInstance();
        bar2.setVisibility(View.INVISIBLE);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mail.getText().toString().trim();
                String pass = password.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    mail.setError("Mailid is Required.");
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    password.setError("Password is required.");
                }
                if (pass.length() < 8) {
                    password.setError("Password is too short");
                }
                bar2.setVisibility(View.VISIBLE);
                fAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Login Successful ", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Dashboard.class));
                            finish();
                            bar2.setVisibility(View.INVISIBLE);
                        } else {
                            Toast.makeText(Login.this, "Login Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            bar2.setVisibility(View.INVISIBLE);
                        }
                    }
                });


            }
        });

        forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                EditText usermail = new EditText(view.getContext());
//                usermail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                final AlertDialog alertDialog;
                AlertDialog.Builder reset = new AlertDialog.Builder(view.getContext());
                final View customLayout = getLayoutInflater().inflate(R.layout.update_data, null);
                alertDialog = reset.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                reset.setTitle("Reset Password")
//                        .setMessage("Enter your email:")
//                        .setView(usermail);


                TextView title = customLayout.findViewById(R.id.dialog_title);
                title.setText("Reset Password");
                TextView message = customLayout.findViewById(R.id.dialog_message);
                message.setVisibility(View.INVISIBLE);
                TextInputLayout text = customLayout.findViewById(R.id.filledTextFiel);
                text.setHint("Enter your Email Id");
                TextInputEditText usermail = customLayout.findViewById(R.id.Update_name);
                MaterialButton cancel = customLayout.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                MaterialButton update = customLayout.findViewById(R.id.update);
                update.setText("Done!");
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String mail = usermail.getText().toString().trim();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this, "Password Reset Mail sent successfully.", Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "Failed to send." + e.getMessage(), Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            }
                        });

                    }
                });
                alertDialog.setView(customLayout);
                alertDialog.show();




//               using Buttons of dialog!
//                reset.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        String mail = usermail.getText().toString().trim();
//                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(Login.this, "Password Reset Mail sent successfully.", Toast.LENGTH_SHORT).show();
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(Login.this, "Failed to send." + e.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                });
//                reset.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });

            }
        });

        register_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( getApplicationContext(), Registration.class);
                startActivity(intent);
            }
        });


    }


}