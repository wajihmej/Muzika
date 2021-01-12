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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import okhttp3.Headers;
import tn.example.muzika.models.Track;
import tn.example.muzika.utils.SessionManager;

public class FragmentFav extends Fragment {
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fav, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.featuredRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.custom_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        favdAdapter adapter = new favdAdapter(this.getContext(), progressDialog);
        recyclerView.setAdapter(adapter);
        return rootView;
    }
}

class favdAdapter extends RecyclerView.Adapter<favdAdapter.MyViewHolder> implements Runnable {
    private static ArrayList<Track> tracks;
    Context context;
    favdAdapter adapter = this;

    public favdAdapter(Context context, ProgressDialog progressDialog) {
        this.context = context;
        getData(context, progressDialog);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_adapter, parent, false);

        return new favdAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(tracks.get(position).getArtname());
        holder.description.setText(tracks.get(position).getName());
        Picasso.get().load(tracks.get(position).getImage()).into(holder.imageplaylist);
        holder.play.setOnClickListener(v -> {
            Log.d("TAG", "salem: ");

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

    void getData(Context cntx, ProgressDialog progressDialog) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestHeaders requestHeaders = new RequestHeaders();
        SessionManager sessionManager = new SessionManager(cntx);
        requestHeaders.put("Authorization", "Bearer " + sessionManager.getUserDetails().getSpotifyToken());
        RequestParams request = new RequestParams();
        client.get("https://api.spotify.com/v1/me/playlists", requestHeaders, request
                , new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.d("Featured Fragment", json.toString());
                        tracks = Track.fromJsonFav(json.jsonObject);
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
        private final ImageButton play;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.playlistNameView);
            description = (TextView) itemView.findViewById(R.id.descriptionTextView);
            imageplaylist = (ImageView) itemView.findViewById(R.id.playlistImageView);
            play= (ImageButton) itemView.findViewById(R.id.play);

        }

        public TextView getTextView() {
            return title;
        }

        public TextView getDescription() {
            return description;
        }

    }
}
