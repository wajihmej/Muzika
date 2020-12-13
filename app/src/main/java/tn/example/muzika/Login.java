package tn.example.muzika;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

    private Activity app = this;
    Button Register, Login, spotifyLogin;
    EditText username, password;
    SharedPreferences pref;
    SessionManager sessionManager;


    //Dialog
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final LoadingDialog loadingDialog = new LoadingDialog(Login.this);

        sessionManager = new SessionManager(getApplicationContext());
        Login = findViewById(R.id.loginbutton);
        Register = findViewById(R.id.registerbutton);
        spotifyLogin = findViewById(R.id.spotifyLogin);
        pref = getSharedPreferences("Session", 0);
        username = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);
        Log.d("Spotify App remote", "appRemote.isConnected()");

        //dialog
        dialog = new Dialog(this);

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
                loadingDialog.startLoadingDialog();

                login(loadingDialog);

            }
        });

        spotifyLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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



    void login(LoadingDialog loadingDialog) {
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
                        Log.d("USER JSON STRING", json.toString());
                        sessionManager.createLoginSession(loggedUser[0].getId(),loggedUser[0].getUsername(), loggedUser[0].getEmail(), loggedUser[0].getToken());
                        loadingDialog.startLoadingDialog();
                        success();
                    }

                    @Override
                    public void onFailure(int statusCode, @Nullable Headers headers, String errorResponse, @Nullable Throwable throwable) {
                        Log.d("DEBUG", errorResponse);
                        loadingDialog.dismissDialog();
                        OpenErreurDialog(errorResponse);

                    }
                });
    }





    void success() {
        Intent intent = new Intent(Login.this, HomePage.class);
        startActivity(intent);
    }


    //dialog
    private void OpenErreurDialog(String errorResponse){
        dialog.setContentView(R.layout.erreur_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button tryagain = dialog.findViewById(R.id.tryagainbutton);
        TextView text = dialog.findViewById(R.id.Ereurtext);
        /*
        if(errorResponse.equals("{\"message\":\"User Not found.\"}"))
        text.setText("User Not found.");
        */
        tryagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(Login.this,"OUPS!",Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }
}