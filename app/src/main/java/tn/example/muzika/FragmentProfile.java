package tn.example.muzika;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestHeaders;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import okhttp3.Headers;
import tn.example.muzika.models.UserDetails;
import tn.example.muzika.models.user;
import tn.example.muzika.utils.SessionManager;

public class FragmentProfile extends Fragment {


    TextView profileName;
    ImageView profileImage;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        SessionManager sessionManager = new SessionManager(this.getContext());
        String token = sessionManager.getUserDetails().getSpotifyToken();
        getUserInfo(token);

        profileName = view.findViewById(R.id.profileName);
        profileImage = view.findViewById(R.id.profileImage);


        return view;
    }


    public void getUserInfo(String token)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        final user[] loggedUser = new user[1];
        RequestHeaders requestHeaders = new RequestHeaders();
        requestHeaders.put("Authorization", "Bearer "+token);
        final UserDetails[] details = new UserDetails[1];
        client.get("https://api.spotify.com/v1/me" , requestHeaders ,null
                , new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        try {
                            details[0] = UserDetails.detailsFromJson(json.jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(details[0] != null) {
                            profileName.setText(details[0].getDisplayName());
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
