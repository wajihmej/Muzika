package tn.example.muzika;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;
import org.w3c.dom.Text;

import okhttp3.Headers;
import tn.example.muzika.models.user;

public class Login extends AppCompatActivity {

    Button Register,Login;
    EditText username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Login = findViewById(R.id.loginbutton);
        Register = findViewById(R.id.registerbutton);

        username = (EditText)findViewById(R.id.userName);
        password = (EditText)findViewById(R.id.password);

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
                Intent intent = new Intent(Login.this, HomePage.class);
                user loggedUser = login(intent);
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

    user login(Intent intent)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        final user[] loggedUser = new user[1];
        client.post("https://nameless-cliffs-25074.herokuapp.com/api/auth/signin",
                "{\"username\" : \""+username.getText().toString()+"\",\n" +
                        "    \"password\" : \""+password.getText().toString()+"\"}"
                , new JsonHttpResponseHandler() {


                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        JSONObject userJson = json.jsonObject;
                        loggedUser[0] = user.fromJson(userJson);
                        Log.d("Json" , loggedUser[0].toString());
                        startActivity(intent);
                    }



                    @Override
            public void onFailure(int statusCode, @Nullable Headers headers, String errorResponse, @Nullable Throwable throwable) {
                Log.d("DEBUG", errorResponse);
            }
        });
        return loggedUser[0];
    }

}