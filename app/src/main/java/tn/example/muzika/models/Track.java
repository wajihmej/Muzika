package tn.example.muzika.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Track {
    String name;
    String image;
    String artname;
    public Track(String name, String image,String artname) {
        this.name = name;
        this.image = image;
        this.artname = artname;
    }

    public Track(){}
    public static ArrayList fromJson(JSONObject jsonObject) {

        ArrayList finalList = new ArrayList();
        try {
            JSONArray track  = jsonObject.getJSONArray("items") ;

            Log.d("TEST TEST ", track.toString());
            for (int i=0 ; i<track.length() ; i++) {
                JSONObject item  = track.getJSONObject(i).getJSONObject("track");

                Track tracks = new Track();

                tracks.setName(item.getString("name"));
                tracks.setImage(item.getJSONObject("album").getJSONArray("images").getJSONObject(0).getString("url"));
                tracks.setArtname(item.getJSONArray("artists").getJSONObject(0).getString("name"));
                finalList.add(tracks);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        };
        // Return new object
        return finalList;
    }

    public static ArrayList fromJsonFav(JSONObject jsonObject) {

        ArrayList finalList = new ArrayList();
        try {
            JSONArray track  = jsonObject.getJSONArray("items") ;

            Log.d("TEST TEST ", track.toString());
            for (int i=0 ; i<track.length() ; i++) {
                JSONObject item  = track.getJSONObject(i);

                Track tracks = new Track();

                tracks.setName(item.getString("name"));
                tracks.setImage(item.getJSONArray("images").getJSONObject(0).getString("url"));
                tracks.setArtname(item.getJSONObject("owner").getString("display_name"));
                finalList.add(tracks);
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
        return "Track{" +
                "name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", artname='" + artname + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getArtname() {
        return artname;
    }

    public void setArtname(String artname) {
        this.artname = artname;
    }
}
