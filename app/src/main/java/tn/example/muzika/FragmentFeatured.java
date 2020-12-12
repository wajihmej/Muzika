package tn.example.muzika;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import okhttp3.Headers;
import tn.example.muzika.models.Playlist;
import tn.example.muzika.utils.SessionManager;

public class FragmentFeatured extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_featured, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.featuredRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        featuredAdapter adapter = new featuredAdapter(this.getContext());
        recyclerView.setAdapter(adapter);
        return rootView;
    }

}

class featuredAdapter extends RecyclerView.Adapter<featuredAdapter.ViewHolder> implements Runnable {
    private ArrayList<Playlist> playlists;
    Context context;
    featuredAdapter adapter = this;
    featuredAdapter(Context context) {
        this.context = context;
        getData(context);
    }

    @NonNull
    @Override
    public featuredAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_adapter, parent, false);
        Log.d("VIEW", "onCreateViewHolder: ");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull featuredAdapter.ViewHolder holder, int position) {
        holder.title.setText(playlists.get(position).getName());
    }


    @Override
    public int getItemCount() {
        if(playlists == null)
            return 0;
        else
            return playlists.size();
    }

    void getData(Context cntx) {
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
                    }

                    @Override
                    public void onFailure(int statusCode, @Nullable Headers headers, String errorResponse, @Nullable Throwable throwable) {
                        Log.d("DEBUG", errorResponse);
                    }
                });
    }

    @Override
    public void run() {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView description;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            title = (TextView) view.findViewById(R.id.adapterTextView);
            description = (TextView) view.findViewById(R.id.descriptionTextView);
        }

        public TextView getTextView() {
            return title;
        }

        public TextView getDescription() {
            return description;
        }
    }
}

