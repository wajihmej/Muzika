package tn.example.muzika;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import tn.example.muzika.models.user;
import tn.example.muzika.utils.SessionManager;

public class FragmentSearch extends Fragment {
    private ArrayList<user> users;

    private SearchAdapter mAdapter;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        users = new ArrayList<>();
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.mylistresult);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.custom_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mAdapter = new SearchAdapter(getContext(),progressDialog,users);

        EditText editText = rootView.findViewById(R.id.searchview);
        recyclerView.setAdapter(mAdapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return rootView;
    }
    private void filter(String text) {
        ArrayList<user> filteredList = new ArrayList<>();
        getData(getContext(),text);
        if(users != null){
        for (user item : users) {
            if (item.getUsername().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        mAdapter.filterList(filteredList);
        }
    }

    void getData(Context cntx,String text) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("username", text);

        client.get("https://nameless-cliffs-25074.herokuapp.com/api/SearchUser/"+text
                , new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.d("Featured Fragment", json.toString());
                        users = user.fromJsonget(json.jsonArray);
                    }

                    @Override
                    public void onFailure(int statusCode, @Nullable Headers headers, String errorResponse, @Nullable Throwable throwable) {
                        Log.d("DEBUG", errorResponse);
                    }
                });
    }

}


class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> implements Runnable {
    private static ArrayList<user> users;

    Context context;
    SearchAdapter adapter = this;

    public SearchAdapter(Context context, ProgressDialog progressDialog, ArrayList<user> users) {
        this.context = context;
        getData(context, progressDialog);
        this.users = users;

    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_adapter, parent, false);
        Log.d("VIEW", "onCreateViewHolder: ");

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        SessionManager sessionManager = new SessionManager(context);
        holder.username.setText(users.get(position).getUsername());
        holder.email.setText(users.get(position).getEmail());
        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Request request = new Request.Builder().url("https://nameless-cliffs-25074.herokuapp.com/api/friendships/add/"+sessionManager.getUserDetails().getId()+"/"+users.get(position).getId())
                        .build();
                OkHttpClient client = new OkHttpClient();
                try {
                    client.newCall(request).execute();
                    Log.d("Search Fragment","User Added");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        if (users == null)
            return 0;
        else
            return users.size();
    }



    void getData(Context cntx, ProgressDialog progressDialog) {
        AsyncHttpClient client = new AsyncHttpClient();

        client.get("https://nameless-cliffs-25074.herokuapp.com/api/search/*"
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
                        progressDialog.dismiss();

                    }
                });
    }

    @Override
    public void run() {

    }

    public void filterList(ArrayList<user> filteredList) {
            users = filteredList;
            notifyDataSetChanged();

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView email;
        private final TextView username;
        private final ImageButton addButton;
        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            username = (TextView) view.findViewById(R.id.user_name);
            email = (TextView) view.findViewById(R.id.email);
            ImageView img = view.findViewById(R.id.imageView5);
            img.setImageResource(R.drawable.userprofile);
            addButton = view.findViewById(R.id.imageButton3);

        }

        public TextView getTextView() {
            return username;
        }

        public TextView getDescription() {
            return email;
        }
    }
}

