package com.example.authtwo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity2 extends AppCompatActivity {


    //<START> For storing unique id and email in RTDB
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    //<END>
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        String userName = getIntent().getStringExtra("userName");
        String userId = getIntent().getStringExtra("userId");
        String userEmail = getIntent().getStringExtra("userEmail");

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("User").child(userName).push();
        databaseReference.setValue(userId);

        ((TextView) findViewById(R.id.textView)).setText(getIntent()
                                                .getStringExtra("userName"));
        ((TextView) findViewById(R.id.textView2)).setText(getIntent()
                                                .getStringExtra("userId"));
        ((TextView) findViewById(R.id.textView3)).setText(getIntent()
                                                .getStringExtra("userEmail"));


        ((Button) findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                MainActivity2
                        .this
                        .startActivity(new Intent(MainActivity2.this,
                                MainActivity.class));
            }
        });
    }
}