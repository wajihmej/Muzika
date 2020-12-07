package tn.example.muzika;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tn.example.muzika.models.Playlist;

public class FragmentFeatured extends Fragment {
    RecyclerView recyclerView;
    List<Playlist> playlists;
    private static String JSON_URL = "https://api.spotify.com/v1/browse/featured-playlists";
    Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_featured,container,false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.Songslist);
        playlists = new ArrayList<>();
        exctracPlaylists(rootView);

        return rootView;
    }

    private void exctracPlaylists(View rootView){
        RequestQueue queue = Volley.newRequestQueue(rootView.getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject playlistObject = response.getJSONObject(i);
                        Log.d("TAG", String.valueOf(response.getJSONObject(i)));
                        Playlist playlist = new Playlist();
                        playlist.setName(playlistObject.getString("message"));
                        playlists.add(playlist);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

                adapter= new Adapter(rootView.getContext(),playlists);
                recyclerView.setAdapter(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "onErrorResponse: "+ error.getMessage());
            }
        });
        queue.add(jsonArrayRequest);
    }
}
