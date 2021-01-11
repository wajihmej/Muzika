package tn.example.muzika;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestHeaders;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import okhttp3.Headers;
import tn.example.muzika.models.Playlist;
import tn.example.muzika.utils.SessionManager;

public class FragmentSearch extends Fragment {
    private ArrayList<Playlist> playlists;
    private SearchAdapter mAdapter;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        playlists = new ArrayList<>();
        getData(getContext());
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.mylistresult);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.custom_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mAdapter = new SearchAdapter(getContext(),progressDialog,playlists);

        EditText editText = rootView.findViewById(R.id.searchview);
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
                    }

                    @Override
                    public void onFailure(int statusCode, @Nullable Headers headers, String errorResponse, @Nullable Throwable throwable) {
                        Log.d("DEBUG", errorResponse);
                    }
                });
    }
}


class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> implements Runnable {
    private static ArrayList<Playlist> playlists;

    Context context;
    SearchAdapter adapter = this;

    public SearchAdapter(Context context, ProgressDialog progressDialog, ArrayList<Playlist> playlists) {
        this.context = context;
        getData(context, progressDialog);
        this.playlists = playlists;

    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_adapter, parent, false);
        Log.d("VIEW", "onCreateViewHolder: ");

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        holder.title.setText(playlists.get(position).getName());
        holder.description.setText(playlists.get(position).getDescription());
        Picasso.get().load(playlists.get(position).getImageUrl()).into(holder.imageplaylist);
        ImageButton shareButton = (ImageButton) holder.itemView.findViewById(R.id.shareButton);

        //GO TO MUSIC LIST
        holder.itemView.setOnClickListener(v -> {

            Bundle bundle = new Bundle();
            bundle.putString("url", playlists.get(position).getTracksHref());

            FragmentTracks tracksfrag= new FragmentTracks();
            tracksfrag.setArguments(bundle);
            FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.fragment_container,tracksfrag);
            transaction.commit();


            /*
            Intent intent = new Intent(this.context, tracks.class);
            intent.putExtra("url", playlists.get(position).getTracksHref());
            context.startActivity(intent);
            */

        });
        shareButton.setOnClickListener(v -> {
            View popupView = LayoutInflater.from(holder.itemView.getContext()).inflate(R.layout.share_popup, null);
            int width = LinearLayout.LayoutParams.MATCH_PARENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true; // lets taps outside the popup also dismiss it
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
            popupView.findViewById(R.id.Share).setOnClickListener(v1 -> {
                EditText postEdit = (EditText) popupView.findViewById(R.id.postText);
                Log.d("Post Content", "onBindViewHolder: " + postEdit.getText().toString());
                sharePost(holder.itemView.getContext(), position, postEdit.getText().toString(), popupWindow);

            });
            popupView.findViewById(R.id.Cancel).setOnClickListener(v12 -> {
                popupWindow.dismiss();
            });
            popupWindow.showAtLocation(holder.itemView, Gravity.CENTER, 0, 0);
        });

    }


    @Override
    public int getItemCount() {
        if (playlists == null)
            return 0;
        else
            return playlists.size();
    }


    void sharePost(Context cntx, int position, String postContent, PopupWindow window) {
        AsyncHttpClient client = new AsyncHttpClient();
        SessionManager sessionManager = new SessionManager(cntx);
        String userId = sessionManager.getUserDetails().getId();
        String playlistId = playlists.get(position).getId();
        String body = "{\n" +
                "    \"postContent\" : \"" + postContent + "\",\n" +
                "    \"playlistId\" : \"" + playlistId + "\"\n}";
        client.post("https://nameless-cliffs-25074.herokuapp.com/api/posts/add/" + userId, body, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d("Post response", "onSuccess: " + json.toString());
                window.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d("Post response", "onFailure: " + response);
            }
        });
    }

    void getData(Context cntx, ProgressDialog progressDialog) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestHeaders requestHeaders = new RequestHeaders();
        SessionManager sessionManager = new SessionManager(cntx);
        Log.d("eeeeeeeeeeeeee", sessionManager.getUserDetails().getSpotifyToken());
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

    @Override
    public void run() {

    }

    public void filterList(ArrayList<Playlist> filteredList) {
            playlists = filteredList;
            notifyDataSetChanged();

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView description;
        private final ImageView imageplaylist;
        private final ImageButton play;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            title = (TextView) view.findViewById(R.id.playlistNameView);
            description = (TextView) view.findViewById(R.id.descriptionTextView);
            imageplaylist = (ImageView) view.findViewById(R.id.playlistImageView);
            play = (ImageButton) view.findViewById(R.id.play);
        }

        public TextView getTextView() {
            return title;
        }

        public TextView getDescription() {
            return description;
        }
    }
}
