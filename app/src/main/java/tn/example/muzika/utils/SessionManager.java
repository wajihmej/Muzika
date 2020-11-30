package tn.example.muzika.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.spotify.sdk.android.authentication.LoginActivity;

import java.util.HashMap;

import tn.example.muzika.models.user;

public class SessionManager {
    /**
     * User name (make variable public to access from outside)
     */
    public static final String KEY_NAME = "name";
    /**
     * Email address (make variable public to access from outside)
     */
    public static final String KEY_EMAIL = "email";

    public static final String KEY_TOKEN = "token";

    public static final String KEY_SPOTIFY = "Spotify_Token";
    /**
     * Sharedpref file name
     */
    private static final String PREF_NAME = "Session";
    /**
     * All Shared Preferences Keys
     */
    private static final String IS_LOGIN = "IsLoggedIn";
    /**
     * Shared Preferences
     */
    SharedPreferences pref;
    /**
     * Editor for Shared preferences
     */
    SharedPreferences.Editor editor;
    /**
     * Context
     */
    Context _context;
    /**
     * Shared pref mode
     */
    int PRIVATE_MODE = 0;

    public static boolean isLoggedIn;

    /**
     * Constructor
     */
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        isLoggedIn = pref.getBoolean(IS_LOGIN,false);

    }

    /**
     * Create login session
     */
    public void createLoginSession(String name, String email,String token) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_TOKEN, token);
        // commit changes
        editor.commit();
    }

    /**
     * Get stored session data
     */
    public user getUserDetails() {
        user user = new user();
// user name
        user.setUsername(pref.getString(KEY_NAME, null));
// user email id
        user.setEmail(pref.getString(KEY_EMAIL, null));
// return user
        user.setToken(pref.getString(KEY_TOKEN, null));

        user.setSpotifyToken(pref.getString(KEY_SPOTIFY, null));
        return user;
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
// Check login status
        if (!this.isLoggedIn()) {
// user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
// Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
// Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
// Staring Login Activity
            _context.startActivity(i);
        }
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
// Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
// After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
// Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
// Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
// Staring Login Activity
        _context.startActivity(i);
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.isLoggedIn = loggedIn;
    }
}