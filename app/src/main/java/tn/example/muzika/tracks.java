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
import android.view.View;
import android.view.ViewGroup;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestHeaders;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import java.util.ArrayList;

import okhttp3.Headers;
import tn.example.muzika.models.Playlist;
import tn.example.muzika.utils.SessionManager;

public class tracks extends AppCompatActivity {
    ProgressDialog progressDialog;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracks);

        recyclerView = findViewById(R.id.tracksRecycler);

    }
}

class Myadapte extends RecyclerView.Adapter<Myadapte.MyViewHolder> implements Runnable{
    private static ArrayList<Playlist> playlists;
    Context context;
    Myadapte adapter = this;
    public Myadapte(Context context,ProgressDialog progressDialog){
        this.context = context;
        getData(context,progressDialog);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if(playlists == null)
            return 0;
        else
            return playlists.size();
    }

    @Override
    public void run() {

    }
    void getData(Context cntx,ProgressDialog progressDialog) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestHeaders requestHeaders = new RequestHeaders();
        SessionManager sessionManager = new SessionManager(cntx);
        requestHeaders.put("Authorization", "Bearer " + sessionManager.getUserDetails().getSpotifyToken());
        RequestParams request = new RequestParams();
        client.get("https://api.spotify.com/v1/browse/featured-playlists", requestHeaders, request
                , new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.d("Featured Fragment", json.toString());
                        playlists = Playlist.fromJson(json.jsonObject);
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
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

