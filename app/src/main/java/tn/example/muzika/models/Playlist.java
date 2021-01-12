package tn.example.muzika.models;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Playlist {

    String description;
    String id;
    String name;
    String tracksHref;
    String imageUrl;


    public Playlist(){
    }

    public static ArrayList fromJson(JSONObject jsonObject) {

        ArrayList finalList = new ArrayList();
        try {
            JSONObject playlists  = jsonObject.getJSONObject("playlists");
            JSONArray items = playlists.getJSONArray("items");
            for (int i=0 ; i<items.length() ; i++) {
                JSONObject item = (JSONObject) items.get(i);
                Log.d("Playlist test", String.valueOf(item.length()));
                Playlist playlist = new Playlist();

                playlist.setId(item.getString("id"));
                playlist.setName(item.getString("name"));
                playlist.setDescription(item.getString("description"));
                playlist.setImageUrl(item.getJSONArray("images").getJSONObject(0).getString("url"));
                playlist.setTracksHref(item.getJSONObject("tracks").getString("href"));
                Log.d("image", playlist.getImageUrl());
                finalList.add(playlist);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        };
        // Return new object
        return finalList;
    }

    public static ArrayList fromJsonLikes(JSONArray jsonObject) {

        Log.d("JSON", jsonObject.toString());
        ArrayList finalList = new ArrayList();
        try {

            for (int i=0 ; i<jsonObject.length() ; i++) {
                JSONObject item  = jsonObject.getJSONObject(i);

                Playlist playlist = new Playlist();
                playlist.setId(item.getString("id"));
                playlist.setName(item.getString("name"));
                playlist.setImageUrl(item.getString("image"));
                playlist.setDescription(item.getString("description"));
                finalList.add(playlist);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        };
        // Return new object
        return finalList;
    }
    @Override
    public String toString() {
        return "Playlist{" +
                "description='" + description + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", tracksHref='" + tracksHref + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTracksHref() {
        return tracksHref;
    }

    public void setTracksHref(String tracksHref) {
        this.tracksHref = tracksHref;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
