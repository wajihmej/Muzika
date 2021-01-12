package tn.example.muzika;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector.ConnectionListener;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import org.json.JSONException;

import tn.example.muzika.utils.SessionManager;

import static tn.example.muzika.Login.FILE_NAME;

public class HomePage extends AppCompatActivity {

    private static final int REQUEST_CODE = 1337;
        private static final String REDIRECT_URI = "https://nameless-cliffs-25074.herokuapp.com/";
    private static final String CLIENT_ID = "fe584e15ac8847edaa874f527f1a8436";
    public static SpotifyAppRemote mSpotifyAppRemote;
    private SessionManager sessionManager;
    SharedPreferences sharedPreferences;
    Dialog dialog;

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        selectedFragment = new FragmentHome();
                        break;
                    case R.id.nav_fav:
                        selectedFragment = new FragmentFeatured();
                        break;
                    case R.id.nav_profile:
                        selectedFragment = new FragmentProfile();
                        break;
                    case R.id.nav_search :
                        selectedFragment = new FragmentSearch();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();
                return true;
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        sessionManager = new SessionManager(getApplicationContext());
        getSpotifyAccessToken();
        sharedPreferences = getSharedPreferences(FILE_NAME,MODE_PRIVATE);

        Log.d("USER LOG", sessionManager.getUserDetails().toString());
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        //I added this if statement to keep the selected fragment when rotating the device




        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new FragmentHome()).commit();
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();
        if (SpotifyAppRemote.isSpotifyInstalled(this)) {
            SpotifyAppRemote.connect(this, connectionParams,
                    new ConnectionListener() {
                        @Override
                        public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                            mSpotifyAppRemote = spotifyAppRemote;
                            Log.d("HomePage", "Connected! Yay!");

                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            Log.d("SpotifyApp", "Not connected");
                            // Something went wrong when attempting to connect! Handle errors here
                        }
                    });
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_navigation, menu);
        return true;
    }

    public void getSpotifyAccessToken() {
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            SharedPreferences.Editor editor;
            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    Log.d("Token", response.getAccessToken());
                    sessionManager.setToken(response.getAccessToken());
                     editor = sharedPreferences.edit();
                        editor.putString("LOGIN",response.getAccessToken());
                        editor.apply();
                    //this.setToken(response.getAccessToken());
                    break;
                // Auth flow returned an error
                case ERROR:
                    Log.d("Token retrieval error", response.getError());
                    // Handle error response
                    dialog = new Dialog(HomePage.this);

                    OpenErreurDialog(response.getError());


                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }

    public void Logout(MenuItem item) {
        sharedPreferences.edit().clear().apply();
        Intent intent = new Intent(HomePage.this,Login.class);
        startActivity(intent);
        finish();


    }

    //dialog
    private void OpenErreurDialog(String errorResponse) {
        dialog.setContentView(R.layout.erreur_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button tryagain = dialog.findViewById(R.id.tryagainbutton);
        TextView text = dialog.findViewById(R.id.Ereurtext);
        text.setText(errorResponse);
        tryagain.setText("Reconnect");
        /*
        if(errorResponse.equals("{\"message\":\"User Not found.\"}"))
        text.setText("User Not found.");
        */
        tryagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(HomePage.this, "OUPS!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomePage.this,Login.class);
                startActivity(intent);
                finish();
            }
        });
        dialog.show();
    }
}