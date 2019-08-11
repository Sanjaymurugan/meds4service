package com.project.meds4service;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

//import me.relex.circleindicator.CircleIndicator;

public class HomeActivity extends AppCompatActivity {
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static final Integer[] XMEN= {R.drawable.slider1,R.drawable.slider2,R.drawable.slider3,R.drawable.slider4,R.drawable.slider5};
    private ArrayList<Integer> XMENArray = new ArrayList<Integer>();
    CardView upload,quickReorder,myorder;
    RecyclerView recyclerView;
    DatabaseReference dbref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        upload=(CardView)findViewById(R.id.cv_upload);
        quickReorder=(CardView)findViewById(R.id.cv_quickreorder);
        myorder=(CardView)findViewById(R.id.cv_myorders);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, com.project.meds4service.upload.class));
            }
        });

        quickReorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, com.project.meds4service.quickReorder.class));
            }
        });

        myorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,MyOrders.class));
            }
        });

        dbref=FirebaseDatabase.getInstance().getReference().child("meds").child("Home");
        for(int i=0;i<XMEN.length;i++)
            XMENArray.add(XMEN[i]);

        mPager = (ViewPager) findViewById(R.id.viewPager);
        mPager.setAdapter(new MyAdapter(HomeActivity.this,XMENArray));
//        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.cIndicator);
//        indicator.setViewPager(mPager);

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == XMEN.length) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2500, 2500);
    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerView=(RecyclerView)findViewById(R.id.meds_recyclerView);
        FirebaseRecyclerAdapter<RecyclerPojo,rv_ViewHolder> adapter=new FirebaseRecyclerAdapter<RecyclerPojo, rv_ViewHolder>(
                RecyclerPojo.class,
                R.layout.rv_child,
                rv_ViewHolder.class,
                dbref
        ) {
            @Override
            protected void populateViewHolder(rv_ViewHolder viewHolder, RecyclerPojo model, int position) {
                final String post_key=getRef(position).getKey();
                viewHolder.setTitle(model.getTitle());
                viewHolder.setAmount(model.getAmount()+"");
                viewHolder.setImage(model.getImage());
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
        recyclerView.setAdapter(adapter);
    }
        public static class rv_ViewHolder extends RecyclerView.ViewHolder{
            TextView child_title,child_amount;
            ImageView child_image;

            public rv_ViewHolder(View itemView) {
                super(itemView);
                child_title=(TextView)itemView.findViewById(R.id.rv_child_title);
                child_amount=(TextView)itemView.findViewById(R.id.rv_child_amount);
                child_image=(ImageView)itemView.findViewById(R.id.rv_child_image);
            }

            public void setTitle(String title){
                child_title.setText(title);
            }

            public void setAmount(String amount){
                child_amount.setText(amount);
            }

            public void setImage(String image){
                Glide.with(itemView).load(image).into(child_image);
            }
        }
}
