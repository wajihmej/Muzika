package tn.example.muzika;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestHeaders;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import okhttp3.Headers;
import tn.example.muzika.models.Track;
import tn.example.muzika.utils.SessionManager;

public class  FragmentTracks extends Fragment {
    ProgressDialog progressDialog;
    private static final String REDIRECT_URI = "https://nameless-cliffs-25074.herokuapp.com/";
    private static final String CLIENT_ID = "fe584e15ac8847edaa874f527f1a8436";
    public static SpotifyAppRemote mSpotifyAppRemote;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        String tracksURL = bundle.getString("url");
        View rootView = inflater.inflate(R.layout.fragment_tracks, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.tracksRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.custom_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TracksAdapter adapter = new TracksAdapter(this.getContext(),progressDialog,tracksURL);
        recyclerView.setAdapter(adapter);
        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(this.getContext(), connectionParams,
                new Connector.ConnectionListener() {

                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d("MainActivity", "Connected! Yay!");

                        // Now you can start interacting with App Remote
                        mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL");

                    }

                    public void onFailure(Throwable throwable) {
                        Log.e("MyActivity", throwable.getMessage(), throwable);

                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });

    }
}

class TracksAdapter extends RecyclerView.Adapter<TracksAdapter.MyViewHolder> implements Runnable {
    private static ArrayList<Track> tracks;
    Context context;
    TracksAdapter adapter = this;

    public TracksAdapter(Context context, ProgressDialog progressDialog, String tracksURL) {
        this.context = context;
        getData(context, progressDialog, tracksURL);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.track_adapter, parent, false);

        return new TracksAdapter.MyViewHolder(view);
    }
    int playint=0;

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(tracks.get(position).getArtname());
        holder.description.setText(tracks.get(position).getName());
        Picasso.get().load(tracks.get(position).getImage()).into(holder.imageplaylist);
        holder.play.setOnClickListener(v -> {
            Log.d("TAG", "salem: ");
            if(playint==0) {
                holder.play.setImageResource(R.drawable.ic_baseline_pause_24);
                playint = 1;
            }
            else
            {
                holder.play.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                playint = 0;
            }


        });
    }

    @Override
    public int getItemCount() {
        if (tracks == null)
            return 0;
        else
            return tracks.size();
    }

    @Override
    public void run() {

    }

    void getData(Context cntx, ProgressDialog progressDialog, String Url) {
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
        private final ImageView play;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.playlistNameView);
            description = (TextView) itemView.findViewById(R.id.descriptionTextView);
            imageplaylist = (ImageView) itemView.findViewById(R.id.playlistImageView);
            play= (ImageView) itemView.findViewById(R.id.play);

        }

        public TextView getTextView() {
            return title;
        }

        public TextView getDescription() {
            return description;
        }

    }
}