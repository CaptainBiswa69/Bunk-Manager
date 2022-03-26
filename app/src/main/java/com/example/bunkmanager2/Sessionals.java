package com.example.bunkmanager2;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Sessionals#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Sessionals extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FloatingActionButton fab;
    FirebaseAuth mAuth;
    FirebaseDatabase data;
    String email;
    RecyclerView recyclerView;
    MyAdapter adapter;

    public Sessionals() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Sessionals.
     */
    // TODO: Rename and change types and number of parameters
    public static Sessionals newInstance(String param1, String param2) {
        Sessionals fragment = new Sessionals();
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
        View view = inflater.inflate(R.layout.fragment_sessionals, container, false);
        fab = view.findViewById(R.id.action_button_sessionals);
        mAuth = FirebaseAuth.getInstance();
        data = FirebaseDatabase.getInstance();
        email = mAuth.getCurrentUser().getEmail();
        String sub = "Sessional";
        DatabaseReference database = data.getReference("Students").child(encodeUserEmail(email)).child(sub);

        recyclerView = view.findViewById(R.id.recview2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseRecyclerOptions<DataModel> options =
                new FirebaseRecyclerOptions.Builder<DataModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Students").child(encodeUserEmail(email)).child(sub), DataModel.class)
                        .build();
        adapter = new MyAdapter(options);
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getActivity(), "Work In Progress will update soon", Toast.LENGTH_SHORT).show();

//                EditText subname = new EditText(view.getContext());

                final androidx.appcompat.app.AlertDialog alertDialog;
                androidx.appcompat.app.AlertDialog.Builder reset = new AlertDialog.Builder(view.getContext());
                final View customLayout = getLayoutInflater().inflate(R.layout.update_data, null);
                alertDialog = reset.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

//                addsub.setTitle("ADD")
//                        .setMessage("Enter name of the subject")
//                        .setView(subname);


                TextView title = customLayout.findViewById(R.id.dialog_title);
                title.setText("Add Subject");
                TextView message = customLayout.findViewById(R.id.dialog_message);
                message.setText("Enter the subject name below:");
                TextInputLayout text = customLayout.findViewById(R.id.filledTextFiel);
                text.setHint("Subject name");
                TextInputEditText subname = customLayout.findViewById(R.id.Update_name);
                MaterialButton cancel = customLayout.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                MaterialButton add = customLayout.findViewById(R.id.update);
                add.setText("Done!");
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String subject = subname.getText().toString();
                        if (TextUtils.isEmpty(subject)) {
                            subname.setError("Enter a valid subject name");
                            Toast.makeText(getActivity(), "Enter a Valid subject name.", Toast.LENGTH_SHORT).show();
                        } else {
                            database.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    if (snapshot.child(subject).exists()) {
                                        Toast.makeText(getActivity(), "Subject Already Exits", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("Category", sub);
                                        user.put("Subject", subject);
                                        user.put("Present", "0");
                                        user.put("Absent", "0");
                                        user.put("Total", "0");
                                        user.put("Percentage", "0");
                                        database.child(subject).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getActivity(), "Field Addition Successful", Toast.LENGTH_SHORT).show();
                                                    alertDialog.dismiss();
                                                } else {
                                                    Toast.makeText(getActivity(), "Field Addition Failed " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }


                    }
                });

                alertDialog.setView(customLayout);
                alertDialog.show();


//                addsub.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        String subject = subname.getText().toString();
//                        if (TextUtils.isEmpty(subject)) {
//                            subname.setError("Enter a valid subject name");
//                            Toast.makeText(getActivity(), "Enter a Valid subject name.", Toast.LENGTH_SHORT).show();
//                        } else {
//                            database.addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(DataSnapshot snapshot) {
//                                    if (snapshot.child(subject).exists()) {
//                                        Toast.makeText(getActivity(), "Subject Already Exits", Toast.LENGTH_SHORT).show();
//                                    } else {
//                                        Map<String, Object> user = new HashMap<>();
//                                        user.put("Category", sub);
//                                        user.put("Subject", subject);
//                                        user.put("Present", "0");
//                                        user.put("Absent", "0");
//                                        user.put("Total", "0");
//                                        user.put("Percentage", "0");
//                                        database.child(subject).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if (task.isSuccessful()) {
//                                                    Toast.makeText(getActivity(), "Field Addition Successful", Toast.LENGTH_SHORT).show();
//                                                } else {
//                                                    Toast.makeText(getActivity(), "Field Addition Failed " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                                                }
//                                            }
//                                        });
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//
//                                }
//                            });
//                        }
//
//                    }
//                });
//                addsub.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });
            }
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    static String encodeUserEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

    static String decodeUserEmail(String userEmail) {
        return userEmail.replace(",", ".");
    }
}