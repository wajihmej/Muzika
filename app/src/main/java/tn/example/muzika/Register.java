package tn.example.muzika;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import okhttp3.Headers;
import tn.example.muzika.models.user;

public class Register extends AppCompatActivity {

    Button Register;
    EditText username, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText) findViewById(R.id.usernamereg);
        email = (EditText) findViewById(R.id.emailreg);
        password = (EditText) findViewById(R.id.passwordreg);

        Register = findViewById(R.id.registerb);

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                user loggedUser = register(intent);
            }
        });

    }

    user register(Intent intent) {
        AsyncHttpClient client = new AsyncHttpClient();
        final user[] loggedUser = new user[1];
        client.post("https://nameless-cliffs-25074.herokuapp.com/api/auth/signup",
                "{\"username\" : \"" + username.getText().toString() + "\",\n" +
                        "    \"email\" : \"" + email.getText().toString() + "\",\n" +
                        "    \"password\" : \"" + password.getText().toString() + "\"}"
                , new JsonHttpResponseHandler() {


                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        //SONObject userJson = json.jsonObject;
                        //loggedUser[0] = user.fromJson(userJson);
                        //Log.d("Json" , loggedUser[0].toString());
                        startActivity(intent);
                        finish();
                    }


                    @Override
                    public void onFailure(int statusCode, @Nullable Headers headers, String errorResponse, @Nullable Throwable throwable) {
                        Log.d("DEBUG", errorResponse);
                    }
                });
        return loggedUser[0];
    }

}