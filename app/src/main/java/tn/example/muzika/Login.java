package tn.example.muzika;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.asynchttpclient.callback.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Headers;

public class Login extends AppCompatActivity {

    Button Register,Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Login = findViewById(R.id.loginbutton);
        Register = findViewById(R.id.registerbutton);

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                login();

                Intent intent =  new Intent(Login.this,HomePage.class);
                startActivity(intent);
                finish();
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

    void login()
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.post("https://nameless-cliffs-25074.herokuapp.com/api/auth/signin",
                "{\"username\" : \"firaspop\",\n" +
                        "    \"password\" : \"123456789\"}"
                , new JsonHttpResponseHandler() {


                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        try {
                            JSONArray userJson = json.jsonObject.getJSONArray("");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("DEBUG", json.toString());
                    }



                    @Override
            public void onFailure(int statusCode, @Nullable Headers headers, String errorResponse, @Nullable Throwable throwable) {
                Log.d("DEBUG", errorResponse);
            }
        });
    }

}