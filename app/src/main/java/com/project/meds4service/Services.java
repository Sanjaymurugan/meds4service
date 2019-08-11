package com.project.meds4service;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Services extends AppCompatActivity {
    ImageView imageView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        imageView=(ImageView)findViewById(R.id.services_image);
        textView=(TextView)findViewById(R.id.Services_desc);

        Bundle bundle=getIntent().getExtras();
        int i=bundle.getInt("position");

        switch (i){
            case 0:
                imageView.setImageResource(R.drawable.slider1);
                textView.setText("TNMSC\nDr.Ganeshan\nService:Pediatrician");
                break;
            case 1:
                imageView.setImageResource(R.drawable.slider2);
                textView.setText("Narayana med Centre\nDr.Abhijit Chavan\nOrthopaedics");
                break;
            case 2:
                imageView.setImageResource(R.drawable.slider3);
                textView.setText("Thyrocare\nDr M.S.Seshadri\nServices:Internal Medicine and Endocrinology");
                break;
            case 3:
                imageView.setImageResource(R.drawable.slider4);
                textView.setText("KMC\nDr.Krishnan Swaminathan\nService:Cardiologist");
                break;
            case 4:
                imageView.setImageResource(R.drawable.slider5);
                textView.setText("Apollo\nDr.P.TamilSelvi\nApollo Hospitality\nService:Dietician");
        }
    }
}
