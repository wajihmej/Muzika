package tn.example.muzika;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

public class tracks extends AppCompatActivity {
    ProgressDialog progressDialog;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tracks);

        String tracksURL = getIntent().getStringExtra("url");
        Log.d("trackssssss", tracksURL);
        recyclerView = findViewById(R.id.tracksRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.custom_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Myadapte adapter = new Myadapte(this,progressDialog,tracksURL);
        recyclerView.setAdapter(adapter);
    }
}

