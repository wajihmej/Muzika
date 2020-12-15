package tn.example.muzika;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestHeaders;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import okhttp3.Headers;
import tn.example.muzika.models.Track;
import tn.example.muzika.utils.SessionManager;

public class tracks extends AppCompatActivity {
    ProgressDialog progressDialog;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracks);

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

class Myadapte extends RecyclerView.Adapter<Myadapte.MyViewHolder> implements Runnable{
    private static ArrayList<Track> tracks;
    Context context;
    Myadapte adapter = this;
    public Myadapte(Context context,ProgressDialog progressDialog,String tracksURL){
        this.context = context;
        getData(context,progressDialog,tracksURL);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_adapter, parent, false);

        return new Myadapte.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(tracks.get(position).getArtname());
        holder.description.setText(tracks.get(position).getName());
        Picasso.get().load(tracks.get(position).getImage()).into(holder.imageplaylist);

    }

    @Override
    public int getItemCount() {
        if(tracks == null)
            return 0;
        else
            return tracks.size();
    }

    @Override
    public void run() {

    }
    void getData(Context cntx,ProgressDialog progressDialog,String Url) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestHeaders requestHeaders = new RequestHeaders();
        SessionManager sessionManager = new SessionManager(cntx);
        requestHeaders.put("Authorization", "Bearer " + sessionManager.getUserDetails().getSpotifyToken());
        RequestParams request = new RequestParams();
        client.get(Url, requestHeaders, request
                , new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.d("Featured Fragment", json.toString());
                        tracks = Track.fromJson(json.jsonObject);
                        adapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(int statusCode, @Nullable Headers headers, String errorResponse, @Nullable Throwable throwable) {
                        Log.d("DEBUG", errorResponse);
                    }
                });
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView description;
        private final ImageView imageplaylist;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.playlistNameView);
            description = (TextView) itemView.findViewById(R.id.descriptionTextView);
            imageplaylist = (ImageView) itemView.findViewById(R.id.playlistImageView);

        }
        public TextView getTextView() {
            return title;
        }

        public TextView getDescription() {
            return description;
        }

    }
}

