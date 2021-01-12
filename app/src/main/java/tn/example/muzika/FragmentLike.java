package tn.example.muzika;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import tn.example.muzika.models.Playlist;
import tn.example.muzika.utils.SessionManager;

public class FragmentLike extends Fragment {
    ProgressDialog progressDialog;
    private ArrayList<Playlist> playlists;

    private likedAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        playlists = new ArrayList<>();
        getData(getContext());

        View rootView = inflater.inflate(R.layout.fragment_featured, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.featuredRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.custom_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mAdapter = new likedAdapter(getContext(),progressDialog,playlists);
        EditText editText = rootView.findViewById(R.id.searchviewfeatured);

        recyclerView.setAdapter(mAdapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());

            }
        });
        return rootView;
    }
    private void filter(String text) {
        ArrayList<Playlist> filteredList = new ArrayList<>();
        for (Playlist item : playlists) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        mAdapter.filterList(filteredList);
    }
    void getData(Context cntx) {
        AsyncHttpClient client = new AsyncHttpClient();
        SessionManager sessionManager = new SessionManager(cntx);
        String userId = sessionManager.getUserDetails().getId();
        client.get("https://nameless-cliffs-25074.herokuapp.com/api/playlists/Playlistbyuser/"+userId
                , new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.d("Featured Fragment", json.toString());
                        playlists = Playlist.fromJsonLikes(json.jsonArray);
                    }

                    @Override
                    public void onFailure(int statusCode, @Nullable Headers headers, String errorResponse, @Nullable Throwable throwable) {
                        Log.d("DEBUG", errorResponse);
                    }
                });
    }
}
class likedAdapter extends RecyclerView.Adapter<likedAdapter.MyViewHolder> implements Runnable {
    private static ArrayList<Playlist> playlists;
    Context context;
    likedAdapter adapter = this;

    public likedAdapter(Context context, ProgressDialog progressDialog, ArrayList<Playlist> playlists) {
        this.context = context;
        getData(context, progressDialog);
        this.playlists = playlists;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_adapter, parent, false);

        return new likedAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(playlists.get(position).getName());
        holder.description.setText(playlists.get(position).getDescription());
        Picasso.get().load(playlists.get(position).getImageUrl()).into(holder.imageplaylist);

        holder.play.setOnClickListener(v -> {
            Log.d("TAG", "salem: ");

        });
        holder.like.setOnClickListener(v -> {
            deletedata(context,playlists.get(position).getId());

        });
    }

    @Override
    public int getItemCount() {
        if (playlists == null)
            return 0;
        else
            return playlists.size();
    }

    @Override
    public void run() {

    }
    public void filterList(ArrayList<Playlist> filteredList) {
        playlists = filteredList;
        notifyDataSetChanged();

    }
    void deletedata(Context cntx,String str) {
        AsyncHttpClient client = new AsyncHttpClient();
        SessionManager sessionManager = new SessionManager(cntx);
        String userId = sessionManager.getUserDetails().getId();
        RequestHeaders requestHeaders = new RequestHeaders();
        requestHeaders.put("id",str);


        client.delete("https://nameless-cliffs-25074.herokuapp.com/api/playlists/delete/", requestHeaders, null
                , new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.d("Featured Fragment", json.toString());

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(int statusCode, @Nullable Headers headers, String errorResponse, @Nullable Throwable throwable) {
                        Log.d("DEBUG", errorResponse);
                    }
                });
    }
    void getData(Context cntx, ProgressDialog progressDialog) {
        AsyncHttpClient client = new AsyncHttpClient();
        SessionManager sessionManager = new SessionManager(cntx);
        String userId = sessionManager.getUserDetails().getId();
        client.get("https://nameless-cliffs-25074.herokuapp.com/api/playlists/Playlistbyuser/"+userId
                , new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.d("Featured Fragment", json.toString());
                        playlists = Playlist.fromJsonLikes(json.jsonArray);
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
        private final ImageButton like;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.playlistNameView);
            description = (TextView) itemView.findViewById(R.id.descriptionTextView);
            imageplaylist = (ImageView) itemView.findViewById(R.id.playlistImageView);
            play= (ImageButton) itemView.findViewById(R.id.play);
            like= (ImageButton) itemView.findViewById(R.id.imageButton);
            like.setColorFilter(Color.argb(255, 255, 0, 0));

        }

        public TextView getTextView() {
            return title;
        }

        public TextView getDescription() {
            return description;
        }

    }
}
