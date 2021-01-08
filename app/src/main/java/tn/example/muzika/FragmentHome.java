package tn.example.muzika;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tn.example.muzika.models.Post;
import tn.example.muzika.utils.SessionManager;

public class FragmentHome extends Fragment {


    private RecyclerView recyclerView;
    private RecyclerView CommentView;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.featuredRecycler);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.custom_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        recyclerView.setAdapter(new homeAdapter(this.getContext(),progressDialog));


       /* CommentView = (RecyclerView) rootView.findViewById(R.id.commentsRecyclerView);
        CommentView.setHasFixedSize(true);
        CommentView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        CommentView.setAdapter(new commentAdapter(this.getContext()));*/

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        return rootView;
    }
}

class homeAdapter extends RecyclerView.Adapter<homeAdapter.ViewHolder> {

    ArrayList<Post> posts = new ArrayList<>();
    homeAdapter adapter = this;
    OkHttpClient client = new OkHttpClient();
    SessionManager sessionManager;

    public homeAdapter(Context context,ProgressDialog progressDialog) {
        sessionManager = new SessionManager(context);
        getPosts(context,progressDialog);

    }

    @NonNull
    @Override
    public homeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull homeAdapter.ViewHolder holder, int position) {
        holder.postContent.setText(posts.get(position).getPostContent());
        try {
            getPlaylistImage(posts.get(position).getPlaylistId(), holder);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void getPlaylistImage(String playlist, ViewHolder holder) throws IOException, JSONException {
        Request request = new Request.Builder().url("https://api.spotify.com/v1/playlists/" + playlist)
                .header("Authorization", "Bearer " + sessionManager.getUserDetails().getSpotifyToken())
                .build();
        Response response = client.newCall(request).execute();
        JSONObject obj = new JSONObject(response.body().string());
        String name = obj.getString("name");
        String url = obj.getJSONArray("images").getJSONObject(0).getString("url");
        Picasso.get().load(url).into(holder.playlistImage);
        holder.playlistName.setText(name);
    }

    private void getUserImage(String userId) {
        Request request = new Request.Builder().url("https://api.spotify.com/v1/users/" + userId)
                .header("Authorization", "Bearer " + sessionManager.getUserDetails().getSpotifyToken())
                .build();
    }

    @Override
    public int getItemCount() {
        return posts.isEmpty() ? 0 : posts.size();
    }

    void getPosts(Context cntx,ProgressDialog progressDialog) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://nameless-cliffs-25074.herokuapp.com/api/posts/friendsPosts/" + sessionManager.getUserDetails().getId()
                , new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        try {
                            posts = Post.postFromJson(json.jsonArray);
                            adapter.notifyDataSetChanged();
                            Log.d("FragmentHome", "onSuccess: " + posts.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, @Nullable Headers headers, String errorResponse, @Nullable Throwable throwable) {
                        Log.d("DEBUG", errorResponse);
                    }
                });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView postContent;
        ImageView profileImage;
        ImageView playlistImage;
        TextView playlistName;

        public ViewHolder(View view) {
            super(view);
            postContent = view.findViewById(R.id.postContent);
            profileImage = view.findViewById(R.id.profileImageView);
            playlistImage = view.findViewById(R.id.playlistImageView);
            playlistName = view.findViewById(R.id.playlistNameView);
        }
    }
}

class commentAdapter extends RecyclerView.Adapter<commentAdapter.ViewHolder> {

    Context context;

    public commentAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_adapter, parent, false);
        return new commentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
        }
    }
}
