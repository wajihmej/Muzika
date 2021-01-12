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
        getData(getContext(),"w");
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

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());

            }
        });

        return rootView;
    }
    private void filter(String text) {
        ArrayList<user> filteredList = new ArrayList<>();
        getData(getContext(),text);
        for (user item : users) {
            if (item.getUsername().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        mAdapter.filterList(filteredList);
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
                .inflate(R.layout.home_adapter, parent, false);
        Log.d("VIEW", "onCreateViewHolder: ");

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        holder.title.setText(users.get(position).getUsername());
        holder.description.setText(users.get(position).getEmail());

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
        RequestParams params = new RequestParams();

        params.put("username", "w");

        client.get("https://nameless-cliffs-25074.herokuapp.com/api/SearchUser/"
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

