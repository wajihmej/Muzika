package tn.example.muzika;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.appcompat.app.AppCompatActivity;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import tn.example.muzika.utils.SessionManager;


public class MainActivity extends AppCompatActivity {


    private static final String CLIENT_ID = "fe584e15ac8847edaa874f527f1a8436";
    private static final String REDIRECT_URI = "https://nameless-cliffs-25074.herokuapp.com/";
    public static SpotifyAppRemote mSpotifyAppRemote;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();
        if(SpotifyAppRemote.isSpotifyInstalled(this)) {
            SpotifyAppRemote.connect(this, connectionParams,
                    new Connector.ConnectionListener() {

                        @Override
                        public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                            mSpotifyAppRemote = spotifyAppRemote;
                            Log.d("MainActivity", "Connected! Yay!");
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            Log.e("MainActivity", throwable.getMessage(), throwable);
                            Intent intent = new Intent(MainActivity.this, Login.class);
                            startActivity(intent);
                            // Something went wrong when attempting to connect! Handle errors here
                        }
                    });
        }
        SessionManager sessionManager = new SessionManager(context);
        if (sessionManager.isLoggedIn()) {
            Intent intent = new Intent(MainActivity.this, HomePage.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        }
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
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}