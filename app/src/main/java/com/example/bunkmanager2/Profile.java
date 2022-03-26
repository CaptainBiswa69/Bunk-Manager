package com.example.bunkmanager2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore data = FirebaseFirestore.getInstance();
    TextView email, name, regno;
    Button update;
    ImageView edit_name , edit_regno;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Profile() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        LayoutInflater lf = getActivity().getLayoutInflater();
        View view = lf.inflate(R.layout.fragment_profile, container, false); //pass the correct layout name for the fragment
        name = view.findViewById(R.id.profile_frag_name);
        email = view.findViewById(R.id.profile_frag_email);
        regno = view.findViewById(R.id.profile_frag_regnum);
        update = view.findViewById(R.id.button);
        edit_name = view.findViewById(R.id.name_edit);
        edit_regno = view.findViewById(R.id.reg_edit);
        String mail = firebaseAuth.getCurrentUser().getEmail();
        DocumentReference documentReference = data.collection("Students").document(mail);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                name.setText("Name: " + documentSnapshot.getString("Name"));
                email.setText(documentSnapshot.getString("Email ID"));
                regno.setText("Reg No.:" + documentSnapshot.getString("Registration No"));
            }
        });


        edit_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final androidx.appcompat.app.AlertDialog alertDialog;
                androidx.appcompat.app.AlertDialog.Builder reset = new androidx.appcompat.app.AlertDialog.Builder(view.getContext());
                final View customLayout = getLayoutInflater().inflate(R.layout.update_data, null);
                alertDialog = reset.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                TextView title = customLayout.findViewById(R.id.dialog_title);
                title.setText("Update");
                TextView message = customLayout.findViewById(R.id.dialog_message);
                message.setVisibility(View.INVISIBLE);
                TextInputLayout text = customLayout.findViewById(R.id.filledTextFiel);
                text.setHint("Name");
                TextInputEditText u_name = customLayout.findViewById(R.id.Update_name);
                MaterialButton cancel = customLayout.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                MaterialButton update = customLayout.findViewById(R.id.update);
                update.setText("Update");
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String nName = u_name.getText().toString();
                        documentReference.update("Name", nName).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Your name updated successfully", Toast.LENGTH_LONG).show();
                                alertDialog.dismiss();
                                ref();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Name Update failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            }
                        });
                    }
                });

                alertDialog.setView(customLayout);
                alertDialog.show();
            }
        });


        edit_regno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final androidx.appcompat.app.AlertDialog alertDialog;
                androidx.appcompat.app.AlertDialog.Builder reset = new androidx.appcompat.app.AlertDialog.Builder(view.getContext());
                final View customLayout = getLayoutInflater().inflate(R.layout.update_data, null);
                alertDialog = reset.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                TextView title = customLayout.findViewById(R.id.dialog_title);
                title.setText("Update");
                TextView message = customLayout.findViewById(R.id.dialog_message);
                message.setVisibility(View.INVISIBLE);
                TextInputLayout text = customLayout.findViewById(R.id.filledTextFiel);
                text.setHint("Registration No");
                TextInputEditText u_reg = customLayout.findViewById(R.id.Update_name);
                MaterialButton cancel = customLayout.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                MaterialButton update = customLayout.findViewById(R.id.update);
                update.setText("Update");
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String nreg = u_reg.getText().toString();
                        documentReference.update("Registration No", nreg).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Your Registration No updated successfully", Toast.LENGTH_LONG).show();
                                alertDialog.dismiss();
                                ref();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Registration No Update failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            }
                        });
                    }
                });

                alertDialog.setView(customLayout);
                alertDialog.show();
            }
        });







//        update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
//                alert.setTitle("Alert")
//                        .setMessage("Are you sure you want to update your data?");
//
//                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        String nName = name.getText().toString();
//                        String nreg = regno.getText().toString();
//                        documentReference.update("Name", nName, "Registration No", nreg).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(getActivity(), "Your profile updated successfully", Toast.LENGTH_LONG).show();
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(getActivity(), "Profile Update failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                });
//                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });
//                alert.show();
//
//
//            }
//        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final androidx.appcompat.app.AlertDialog alertDialog;
                androidx.appcompat.app.AlertDialog.Builder reset = new androidx.appcompat.app.AlertDialog.Builder(view.getContext());
                final View customLayout = getLayoutInflater().inflate(R.layout.do_not_click, null);
                alertDialog = reset.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                alertDialog.setView(customLayout);
                alertDialog.show();
            }
        });



        return view;
    }
    public void ref(){
        getActivity().getSupportFragmentManager().beginTransaction().replace(Profile.this.getId(), new Profile()).commit();
    }
}