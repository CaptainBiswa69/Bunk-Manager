package com.example.bunkmanager2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Dashboard extends AppCompatActivity {

    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore data;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Subjects");
        setSupportActionBar(toolbar);

        Button b = new Button(this);
        Toolbar.LayoutParams l1=new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        l1.gravity = Gravity.END;
        b.setBackgroundColor(Color.TRANSPARENT);
        b.setLayoutParams(l1);
        b.setText("Logout");
        toolbar.addView(b);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                sendToLogin();
            }
        });

        navigationView = (NavigationView)findViewById(R.id.navmenu);
        navigationView.setItemIconTintList(null);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);

        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        firebaseAuth = FirebaseAuth.getInstance();
        data = FirebaseFirestore.getInstance();
        String email = firebaseAuth.getCurrentUser().getEmail();
        DocumentReference documentReference = data.collection("Students").document(email);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.profile_name);
        TextView navRegno = (TextView) headerView.findViewById(R.id.profile_regnum);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                navUsername.setText(documentSnapshot.getString("Name"));
                navRegno.setText(documentSnapshot.getString("Registration No"));

            }
        });
        navigationView.setCheckedItem(R.id.subjects);
        getSupportFragmentManager().beginTransaction().replace(R.id.noobc,new Subjects()).commit();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            Fragment temp;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.profile:
//                        Toast.makeText(Dashboard.this, "This Is Your Profile", Toast.LENGTH_SHORT).show();
                        temp = new Profile();
                        toolbar.setTitle("Bunk Manager");
                        break;
                    case R.id.subjects:
//                        Toast.makeText(Dashboard.this, "Subjects Activity", Toast.LENGTH_SHORT).show();
                        temp = new Subjects();
                        toolbar.setTitle("Subjects");
                        break;
                    case R.id.lab:
//                        Toast.makeText(Dashboard.this, "Lab Activity", Toast.LENGTH_SHORT).show();
                        temp = new Sessionals();
                        toolbar.setTitle("Sessionals");
                        break;

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.noobc,temp).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                return  true;
            }
        });


    }
    private void sendToLogin() {

        Intent loginIntent = new Intent(Dashboard.this, Login.class);
        startActivity(loginIntent);
        finish();

    }
    


}