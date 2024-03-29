package tn.example.muzika.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class user {
    private String id;
    private String username;
    private String email;
    private String token;
    private String spotifyToken;
    private String image;

    public user() {
    }

    public user(String id, String username, String email, String token, String spotifyToken, String image) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.token = token;
        this.spotifyToken = spotifyToken;

    }

    public static user fromJson(JSONObject jsonObject) {
        user u = new user();
        // Deserialize json into object fields
        try {
            u.id = jsonObject.getString("id");
            u.username = jsonObject.getString("username");
            u.email = jsonObject.getString("email");
            u.token = jsonObject.getString("accessToken");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Return new object
        return u;
    }

    public static ArrayList fromJsonget(JSONArray jsonObject) {

        ArrayList finalList = new ArrayList();
        try {

            for (int i=0 ; i<jsonObject.length() ; i++) {
                JSONObject item  = jsonObject.getJSONObject(i);

                user User = new user();

                User.setUsername(item.getString("username"));
                User.setId(item.getString("id"));
                User.setEmail(item.getString("email"));
                finalList.add(User);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        };
        // Return new object
        return finalList;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getSpotifyToken() {
        return spotifyToken;
    }

    public void setSpotifyToken(String spotifyToken) {
        this.spotifyToken = spotifyToken;
    }

    @Override
    public String toString() {
        return "user{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", token='" + token + '\'' +
                ", spotifyToken='" + spotifyToken + '\'' +
                '}';
    }
}
