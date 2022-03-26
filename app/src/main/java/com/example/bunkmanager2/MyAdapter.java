package com.example.bunkmanager2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyAdapter extends FirebaseRecyclerAdapter<DataModel, MyAdapter.myviewholder> {


    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String email = mAuth.getCurrentUser().getEmail();
    FirebaseDatabase data = FirebaseDatabase.getInstance();
    DatabaseReference database = data.getReference("Students").child(encodeUserEmail(email));

    public MyAdapter(@NonNull FirebaseRecyclerOptions<DataModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull DataModel model) {


        holder.absent_count.setText(model.getAbsent());
        holder.percentage_count.setText(model.getPercentage());
        holder.present_count.setText(model.getPresent());
        holder.header.setText(model.getSubject());
        holder.category.setText(model.getCategory());
        holder.total_count.setText(model.getTotal());


        holder.edit_present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final androidx.appcompat.app.AlertDialog alertDialog;
                androidx.appcompat.app.AlertDialog.Builder reset = new androidx.appcompat.app.AlertDialog.Builder(view.getContext());
                final View customLayout = LayoutInflater.from(view.getContext()).inflate(R.layout.update_data, null);
                alertDialog = reset.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                TextView title = customLayout.findViewById(R.id.dialog_title);
                title.setText("Update Present Count");
                TextView message = customLayout.findViewById(R.id.dialog_message);
                message.setText("Current Value is: " + holder.present_count.getText().toString());
                TextInputLayout text = customLayout.findViewById(R.id.filledTextFiel);
                text.setHint("Enter Value");
                TextInputEditText getvalue = customLayout.findViewById(R.id.Update_name);
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
                        String value = getvalue.getText().toString();
                        if (TextUtils.isEmpty(value)) {
                            alertDialog.dismiss();
                        } else {

                            float present = Float.parseFloat(value);
                            String total = String.valueOf((int) present + Integer.parseInt(holder.absent_count.getText().toString()));
                            float tot = Float.parseFloat(total);
                            float percen = (present / tot) * 100;


                            String percentage = String.valueOf(percen);
                            database.child(holder.category.getText().toString()).child(holder.header.getText().toString()).child("Present").setValue(value);
                            database.child(holder.category.getText().toString()).child(holder.header.getText().toString()).child("Total").setValue(total);
                            database.child(holder.category.getText().toString()).child(holder.header.getText().toString()).child("Percentage").setValue(percentage);

                            alertDialog.dismiss();
                        }

                    }
                });

                alertDialog.setView(customLayout);
                alertDialog.show();





//                AlertDialog.Builder updation = new AlertDialog.Builder(view.getContext());
//                EditText getvalue = new EditText(view.getContext());
//                updation.setTitle("Update Present count")
//                        .setMessage("Current Value is: " + holder.present_count.getText().toString())
//                        .setView(getvalue);
//                updation.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        String value = getvalue.getText().toString();
//                        if (TextUtils.isEmpty(value)) {
//                            dialogInterface.cancel();
//                        } else {
//
//                            float present = Float.parseFloat(value);
//                            String total = String.valueOf((int) present + Integer.parseInt(holder.absent_count.getText().toString()));
//                            float tot = Float.parseFloat(total);
//                            float percen = (present / tot) * 100;
//
//
//                            String percentage = String.valueOf(percen);
//                            database.child(holder.category.getText().toString()).child(holder.header.getText().toString()).child("Present").setValue(value);
//                            database.child(holder.category.getText().toString()).child(holder.header.getText().toString()).child("Total").setValue(total);
//                            database.child(holder.category.getText().toString()).child(holder.header.getText().toString()).child("Percentage").setValue(percentage);
//                        }
//                    }
//                });
//                updation.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.cancel();
//                    }
//                });
//                updation.show();

            }
        });
        holder.edit_absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final androidx.appcompat.app.AlertDialog alertDialog;
                androidx.appcompat.app.AlertDialog.Builder reset = new androidx.appcompat.app.AlertDialog.Builder(view.getContext());
                final View customLayout = LayoutInflater.from(view.getContext()).inflate(R.layout.update_data, null);
                alertDialog = reset.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                TextView title = customLayout.findViewById(R.id.dialog_title);
                title.setText("Update Absent Count");
                TextView message = customLayout.findViewById(R.id.dialog_message);
                message.setText("Current Value is: " + holder.absent_count.getText().toString());
                TextInputLayout text = customLayout.findViewById(R.id.filledTextFiel);
                text.setHint("Enter Value");
                TextInputEditText getvalue = customLayout.findViewById(R.id.Update_name);
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
                        String value = getvalue.getText().toString();
                        if (TextUtils.isEmpty(value)) {
                            alertDialog.dismiss();
                        } else {

                            float absent = Float.parseFloat(value);
                            String total = String.valueOf((int) absent + Integer.parseInt(holder.present_count.getText().toString()));
                            float tot = Float.parseFloat(total);
                            float percen = (Float.parseFloat(holder.present_count.getText().toString()) / tot) * 100;


                            String percentage = String.valueOf(percen);
                            database.child(holder.category.getText().toString()).child(holder.header.getText().toString()).child("Absent").setValue(value);
                            database.child(holder.category.getText().toString()).child(holder.header.getText().toString()).child("Total").setValue(total);
                            database.child(holder.category.getText().toString()).child(holder.header.getText().toString()).child("Percentage").setValue(percentage);

                            alertDialog.dismiss();
                        }
                    }
                });

                alertDialog.setView(customLayout);
                alertDialog.show();


//                EditText getvalue = new EditText(view.getContext());
//                updation.setTitle("Update Absent count")
//                        .setMessage("Current Value is: " + holder.absent_count.getText().toString())
//                        .setView(custom);
//                updation.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        String value = getvalue.getText().toString();
//                        if (TextUtils.isEmpty(value)) {
//                            dialogInterface.cancel();
//                        } else {
//
//                            float absent = Float.parseFloat(value);
//                            String total = String.valueOf((int) absent + Integer.parseInt(holder.present_count.getText().toString()));
//                            float tot = Float.parseFloat(total);
//                            float percen = (Float.parseFloat(holder.present_count.getText().toString()) / tot) * 100;
//
//
//                            String percentage = String.valueOf(percen);
//                            database.child(holder.category.getText().toString()).child(holder.header.getText().toString()).child("Absent").setValue(value);
//                            database.child(holder.category.getText().toString()).child(holder.header.getText().toString()).child("Total").setValue(total);
//                            database.child(holder.category.getText().toString()).child(holder.header.getText().toString()).child("Percentage").setValue(percentage);
//                        }
//                    }
//                });
//                updation.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.cancel();
//                    }
//                });
//                updation.show();

            }
        });
        holder.increase_present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                float present = (Float.parseFloat(holder.present_count.getText().toString())) + 1;
                String total = String.valueOf((int) present + Integer.parseInt(holder.absent_count.getText().toString()));
                float tot = Float.parseFloat(total);
                float percen = (present / tot) * 100;


                String percentage = String.valueOf(percen);
                database.child(holder.category.getText().toString()).child(holder.header.getText().toString()).child("Present").setValue(String.valueOf((int) present));
                database.child(holder.category.getText().toString()).child(holder.header.getText().toString()).child("Total").setValue(total);
                database.child(holder.category.getText().toString()).child(holder.header.getText().toString()).child("Percentage").setValue(percentage);


            }
        });
        holder.increase_absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                float absent = (Float.parseFloat(holder.absent_count.getText().toString())) + 1;
                String total = String.valueOf((int) absent + Integer.parseInt(holder.present_count.getText().toString()));
                float tot = Float.parseFloat(total);
                float percen = (Float.parseFloat(holder.present_count.getText().toString()) / tot) * 100;


                String percentage = String.valueOf(percen);
                database.child(holder.category.getText().toString()).child(holder.header.getText().toString()).child("Absent").setValue(String.valueOf((int) absent));
                database.child(holder.category.getText().toString()).child(holder.header.getText().toString()).child("Total").setValue(total);
                database.child(holder.category.getText().toString()).child(holder.header.getText().toString()).child("Percentage").setValue(percentage);


            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder del = new AlertDialog.Builder(view.getContext());
                del.setTitle("Attention")
                        .setMessage("Are you sure you want to delete " + holder.header.getText().toString() + "?" + " field");
                del.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        database.child(holder.category.getText().toString()).child(holder.header.getText().toString()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(view.getContext(), "Field Deletion Successful", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(view.getContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        Toast.makeText(view.getContext(), "Noob", Toast.LENGTH_SHORT).show();
                    }
                });
                del.show();
            }
        });


    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow, parent, false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder {

        TextView present_count, absent_count, total_count, percentage_count, header, category;
        Button edit_present, edit_absent, delete, increase_present, increase_absent;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.Catagory);
            header = itemView.findViewById(R.id.subject_name);
            present_count = itemView.findViewById(R.id.present_count);
            absent_count = itemView.findViewById(R.id.absent_count);
            total_count = itemView.findViewById(R.id.total_count);
            percentage_count = itemView.findViewById(R.id.percentage_count);
            edit_present = itemView.findViewById(R.id.edit_present_count);
            edit_absent = itemView.findViewById(R.id.edit_absent_count);
            delete = itemView.findViewById(R.id.delete_row);
            increase_present = itemView.findViewById(R.id.present_button);
            increase_absent = itemView.findViewById(R.id.absent_button);

        }


    }


    static String encodeUserEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

    static String decodeUserEmail(String userEmail) {
        return userEmail.replace(",", ".");
    }

}
