package tn.example.muzika;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentHome extends Fragment {


   private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home,container,false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.featuredRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

        recyclerView.setAdapter(new homeAdapter());
        return rootView;
    }
}

class homeAdapter extends RecyclerView.Adapter<homeAdapter.ViewHolder>{
    private String[] dataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView description;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            title = (TextView) view.findViewById(R.id.adapterTextView);
            description = (TextView) view.findViewById(R.id.descriptionTextView);
        }

        public TextView getTextView() {
            return title;
        }
        public TextView getDescription() {
            return description;
        }
    }

    @NonNull
    @Override
    public homeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_adapter, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull homeAdapter.ViewHolder holder, int position) {
        holder.getTextView().setText("Lofi playlist Title");
        holder.getDescription().setText("Cool playlist");
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
