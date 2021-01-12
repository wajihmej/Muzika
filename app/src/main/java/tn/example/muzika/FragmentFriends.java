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
import tn.example.muzika.models.user;
import tn.example.muzika.utils.SessionManager;

public class FragmentFriends extends Fragment {
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

        friendsAdapter adapter = new friendsAdapter(this.getContext(), progressDialog);
        recyclerView.setAdapter(adapter);
        return rootView;
    }
}

class friendsAdapter extends RecyclerView.Adapter<friendsAdapter.MyViewHolder> implements Runnable {
    private static ArrayList<user> users;
    Context context;
    friendsAdapter adapter = this;

    public friendsAdapter(Context context, ProgressDialog progressDialog) {
        this.context = context;
        getData(context, progressDialog);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_adapter, parent, false);

        return new friendsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.username.setText(users.get(position).getUsername());
        holder.email.setText(users.get(position).getEmail());

    }

    @Override
    public int getItemCount() {
        if (users == null)
            return 0;
        else
            return users.size();
    }

    @Override
    public void run() {

    }

    void getData(Context cntx, ProgressDialog progressDialog) {
        AsyncHttpClient client = new AsyncHttpClient();

        SessionManager sessionManager = new SessionManager(cntx);
        String userId = sessionManager.getUserDetails().getId();

        client.get("https://nameless-cliffs-25074.herokuapp.com/api/findfriends/"+userId
                , new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.d("Featured Fragment", json.toString());
                        users = user.fromJsonget(json.jsonArray);
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
        private final TextView email;
        private final TextView username;
        private final ImageButton addButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.user_name);
            email = (TextView) itemView.findViewById(R.id.email);
            ImageView img = itemView.findViewById(R.id.imageView5);
            img.setImageResource(R.drawable.userprofile);
            addButton = itemView.findViewById(R.id.imageButton3);

        }

        public TextView getTextView() {
            return username;
        }

        public TextView getDescription() {
            return email;
        }
    }
}
