package com.project.meds4service;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class quickReorder extends AppCompatActivity {
    TextView tv;
    String a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_reorder);

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference();
        tv=(TextView)findViewById(R.id.qo_description);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String desc=dataSnapshot.child("order").child("description").getValue().toString();
                tv.setText(desc);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
