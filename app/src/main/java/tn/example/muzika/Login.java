package tn.example.muzika;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import org.json.JSONObject;

import okhttp3.Headers;
import tn.example.muzika.models.user;
import tn.example.muzika.utils.SessionManager;

public class Login extends AppCompatActivity {

    private static final int REQUEST_CODE = 1337;
    private static final String REDIRECT_URI = "https://nameless-cliffs-25074.herokuapp.com/";
    private static final String CLIENT_ID = "fe584e15ac8847edaa874f527f1a8436";
    Button Register, Login, spotifyLogin;
    EditText username, password;
    Activity app = this;
    SharedPreferences pref;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sessionManager = new SessionManager(getApplicationContext());
        Login = findViewById(R.id.loginbutton);
        Register = findViewById(R.id.registerbutton);
        spotifyLogin = findViewById(R.id.spotifyLogin);
        pref = getSharedPreferences("Session", 0);
        username = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);
        Log.d("Spotify App remote", "appRemote.isConnected()");
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        spotifyLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSpotifyAccessToken();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    void getSpotifyAccessToken()
    {
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(app, REQUEST_CODE, request);
    }

    void login() {
        AsyncHttpClient client = new AsyncHttpClient();
        final user[] loggedUser = new user[1];
        client.post("https://nameless-cliffs-25074.herokuapp.com/api/auth/signin",
                "{\"username\" : \"" + username.getText().toString() + "\",\n" +
                        "    \"password\" : \"" + password.getText().toString() + "\"}"
                , new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        JSONObject userJson = json.jsonObject;
                        loggedUser[0] = user.fromJson(userJson);
                        Log.d("Json", loggedUser[0].toString());
                        sessionManager.createLoginSession(loggedUser[0].getUsername(), loggedUser[0].getEmail(), loggedUser[0].getToken());
                        getSpotifyAccessToken();
                        success();
                    }

                    @Override
                    public void onFailure(int statusCode, @Nullable Headers headers, String errorResponse, @Nullable Throwable throwable) {
                        Log.d("DEBUG", errorResponse);
                    }
                });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    Log.d("Token", response.getAccessToken());
                    pref.edit().putString("Spotify_Token", response.getAccessToken()).commit();
                    success();
                    break;
                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }

    void success() {
        Intent intent = new Intent(Login.this, HomePage.class);
        startActivity(intent);
    }

}