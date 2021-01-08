package tn.example.muzika.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserDetails {

    String displayName;
    String spotifyId;
    String imageUrl;

    public UserDetails() {
    }

    public static UserDetails detailsFromJson(JSONObject object) throws JSONException {
        UserDetails details = new UserDetails();
        Log.d("USERRRR", object.toString());
        details.displayName = object.getString("display_name");
        details.spotifyId = object.getString("id");
        JSONArray images = object.getJSONArray("images");
        if(images.length()==0){
            details.imageUrl ="";
        }
        else
        {
            details.imageUrl = images.getJSONObject(0).getString("url");
        }


        Log.d("User details class : ", "detailsFromJson: " + details.toString());
        return details;
    }

    @Override
    public String toString() {
        return "UserDetails{" +
                "displayName='" + displayName + '\'' +
                ", spotifyId='" + spotifyId + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getSpotifyId() {
        return spotifyId;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
