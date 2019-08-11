package com.project.meds4service;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class upload extends AppCompatActivity {
    ImageView upload_image;
    TextView upload_text,upload_desc;
    Button upload_button;
    EditText upload_address;
    Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference db;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 71 && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                upload_image.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        upload_desc=(TextView)findViewById(R.id.upload_desc);
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        db= FirebaseDatabase.getInstance().getReference().child("order");

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String a=dataSnapshot.child("description").getValue().toString();
                upload_desc.setText(a);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        upload_address=(EditText) findViewById(R.id.upload_address);
        upload_text=(TextView)findViewById(R.id.upload_text);
        upload_button=(Button)findViewById(R.id.upload_button);
        upload_image=(ImageView)findViewById(R.id.upload_image);
        upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),71);
            }
        });

        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(filePath != null)
                {
                    final ProgressDialog progressDialog = new ProgressDialog(upload.this);
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();

                    StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
                    ref.putFile(filePath)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.dismiss();
                                    Toast.makeText(upload.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                    upload_text.setText("Successfully Uploaded. Your Prescription is under Review.");
                                    String a=taskSnapshot.getStorage().getDownloadUrl().toString();
                                    order_pojo pojo=new order_pojo();
                                    pojo.setAddress(upload_address.getText().toString());
                                    pojo.setImage(a);
                                    pojo.setDescription("");
                                    db.child("order").setValue(pojo);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(upload.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                            .getTotalByteCount());
                                    progressDialog.setMessage("Uploaded "+(int)progress+"%");
                                }
                            });
                }
            }
        });
    }
}
