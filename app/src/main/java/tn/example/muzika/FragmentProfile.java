package tn.example.muzika;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestHeaders;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import okhttp3.Headers;
import tn.example.muzika.models.UserDetails;
import tn.example.muzika.models.user;
import tn.example.muzika.utils.SessionManager;

import static android.content.Context.MODE_PRIVATE;
import static tn.example.muzika.Login.FILE_NAME;

public class FragmentProfile extends Fragment {

    TextView profileName;
    ImageView profileImage;
    SharedPreferences sharedPreferences;
    Button myplaylists,likes,friends,saved;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        SessionManager sessionManager = new SessionManager(this.getContext());
        String token = sessionManager.getUserDetails().getSpotifyToken();
        sharedPreferences = this.getActivity().getSharedPreferences(FILE_NAME,MODE_PRIVATE);
        Log.d("salem", sharedPreferences.getString("LOGIN",""));
        getUserInfo(sharedPreferences.getString("LOGIN",""));

        profileName = view.findViewById(R.id.profileName);
        profileImage = view.findViewById(R.id.profileImage);
        myplaylists = view.findViewById(R.id.myplaylists);

        myplaylists.setOnClickListener(v -> {
            FragmentFav tracksfrag= new FragmentFav();
            FragmentManager manager = ((AppCompatActivity)getContext()).getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.fragment_container,tracksfrag);
            transaction.commit();

        });
        return view;
    }


    public void getUserInfo(String token) {
        AsyncHttpClient client = new AsyncHttpClient();
        final user[] loggedUser = new user[1];
        RequestHeaders requestHeaders = new RequestHeaders();
        requestHeaders.put("Authorization", "Bearer " + token);
        final UserDetails[] details = new UserDetails[1];
        client.get("https://api.spotify.com/v1/me", requestHeaders, null
                , new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        try {
                            details[0] = UserDetails.detailsFromJson(json.jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (details[0] != null) {
                            profileName.setText(details[0].getDisplayName());
                        }
                        if(details[0].getImageUrl()==""){

                        }
                        else {
                            Picasso.get().load(details[0].getImageUrl()).into(profileImage);
                        }


                    }

                    @Override
                    public void onFailure(int statusCode, @Nullable Headers headers, String errorResponse, @Nullable Throwable throwable) {
                        Log.d("DEBUG", errorResponse);
                    }
                });
    }
}
