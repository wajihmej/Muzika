package tn.example.muzika;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Headers;
import tn.example.muzika.models.UserDetails;
import tn.example.muzika.models.user;
import tn.example.muzika.utils.SessionManager;

import static android.content.Context.MODE_PRIVATE;
import static tn.example.muzika.Login.FILE_NAME;

public class FragmentProfile extends Fragment {
    //DIALOG
    ProgressDialog progressDialog;
    Dialog dialog;

    TextView profileName;
    SharedPreferences sharedPreferences;
    CircleImageView img;
    Button myplaylists,likes,friends,logoutprofil;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        SessionManager sessionManager = new SessionManager(this.getContext());
        String token = sessionManager.getUserDetails().getSpotifyToken();
        sharedPreferences = this.getActivity().getSharedPreferences(FILE_NAME,MODE_PRIVATE);
        Log.d("salem", sharedPreferences.getString("LOGIN",""));
        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.custom_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        getUserInfo(sharedPreferences.getString("LOGIN",""),progressDialog);

        profileName = view.findViewById(R.id.profileName);
        myplaylists = view.findViewById(R.id.myplaylists);
        likes = view.findViewById(R.id.liked);
        logoutprofil = view.findViewById(R.id.logoutporfile);

        img = view.findViewById(R.id.profile_image);

        logoutprofil.setOnClickListener(v -> {
            sharedPreferences.edit().clear().apply();
            Intent intent = new Intent(getContext(), Login.class);
            getContext().startActivity(intent);


        });
        myplaylists.setOnClickListener(v -> {
            FragmentFav favfrag= new FragmentFav();
            FragmentManager manager = ((AppCompatActivity)getContext()).getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.fragment_container,favfrag);
            transaction.commit();

        });

        likes.setOnClickListener(v -> {
            FragmentLike likesfrag= new FragmentLike();
            FragmentManager manager = ((AppCompatActivity)getContext()).getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.fragment_container,likesfrag);
            transaction.commit();

        });
        return view;
    }


    public void getUserInfo(String token,ProgressDialog progressDialog) {
        AsyncHttpClient client = new AsyncHttpClient();
        final user[] loggedUser = new user[1];
        Log.d("logged", loggedUser.toString());
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
                            Picasso.get().load(details[0].getImageUrl()).into(img);

                        }
                    progressDialog.dismiss();

                    }

                    @Override
                    public void onFailure(int statusCode, @Nullable Headers headers, String errorResponse, @Nullable Throwable throwable) {
                        dialog = new Dialog(getContext());

                        Log.d("DEBUG", errorResponse);
                        progressDialog.dismiss();
                        OpenErreurDialog(errorResponse);
                    }
                });
    }
    //dialog
    private void OpenErreurDialog(String errorResponse) {
        dialog.setContentView(R.layout.erreur_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button tryagain = dialog.findViewById(R.id.tryagainbutton);
        TextView text = dialog.findViewById(R.id.Ereurtext);
        text.setText("Check connection");
        /*
        if(errorResponse.equals("{\"message\":\"User Not found.\"}"))
        text.setText("User Not found.");
        */
        tryagain.setText("ok");
        tryagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(getContext(), "OUPS!", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }
}
