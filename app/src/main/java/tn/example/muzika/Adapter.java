package tn.example.muzika;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.squareup.picasso.Picasso;

import tn.example.muzika.models.Playlist;
import tn.example.muzika.models.user;

import java.util.List;


public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    LayoutInflater inflater;
    List<Playlist> playlists;

    public Adapter(Context ctx, List<Playlist> playlists){
        this.inflater = LayoutInflater .from(ctx);
        this.playlists = playlists;

    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= inflater.inflate(R.layout.home_adapter,parent);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        //holder.Title.setText(playlists.get(position).getName());
        //holder.Description.setText(playlists.get(position).getDescription());
        //Picasso.get().load(playlists.get(position).getImagePlaylist()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView Title,Description;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            Title = itemView.findViewById(R.id.adapterTextView);
            Description = itemView.findViewById(R.id.descriptionTextView);
            imageView = itemView.findViewById(R.id.imageView2);

        }
    }
}
