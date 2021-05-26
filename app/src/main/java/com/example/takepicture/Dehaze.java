package com.example.takepicture;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Dehaze extends AppCompatActivity {
    ImageView img1;
    ImageView img2;
    ImageView img3;
    ProgressDialog dialog;
    FirebaseDatabase mdatabase;
    DatabaseReference det;
    Uri original_uri;
    String original_uripath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dehaze);
        dialog = new ProgressDialog(Dehaze.this);
        dialog.setMessage("Calling processing model");
        dialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dehazing-app.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API api = retrofit.create(API.class);
        Call<APImodel> call = api.process();
        img1=findViewById(R.id.imageView);
        img2=findViewById(R.id.imageView2);
        img3=findViewById(R.id.imageView3);

        if (getIntent() != null) {
            original_uripath = getIntent().getStringExtra("original image");
            original_uri = Uri.parse(original_uripath);
        }

        call.enqueue(new Callback<APImodel>() {
            @Override
            public void onResponse(@NotNull Call<APImodel> call, @NotNull Response<APImodel> response) {
                Log.d("dehaze", "onResponse Called");
                Log.d("dehaze", "Code: " + response.code());
                APImodel object = response.body();
                if (Objects.requireNonNull(object).getStatus().equals("Image Analysis Completed")) {
                    dialog.setMessage("Processing image");
                    firebasecheck();
                }
            }

            @Override
            public void onFailure(@NotNull Call<APImodel> call, @NotNull Throwable t) {
                Log.d("dehaze", "onFailure Called");
                Log.d("dehaze", t.getMessage());
            }
        });
    }

    private void firebasecheck() {
        mdatabase = FirebaseDatabase.getInstance();
        det = mdatabase.getReference();
        mdatabase.getReference().child("image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("dehaze",snapshot.child("status").getValue().toString());
                Log.d("dehaze", String.valueOf(snapshot.child("status").getValue().toString().equals("Processing Completed")));
                if(snapshot.child("status").getValue().toString().equals("Processing Completed")){
                    Log.d("dehaze",snapshot.child("dehazed1url").getValue().toString());
                    Log.d("dehaze",snapshot.child("dehazed2url").getValue().toString());
                    String url1=snapshot.child("dehazed1url").getValue().toString();
                    String url2=snapshot.child("dehazed2url").getValue().toString();
                    img1.setImageURI(original_uri);
                    Picasso.with(getBaseContext()).load(url1).into(img2);
                    Picasso.with(getBaseContext()).load(url2).into(img3);
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}