package ca.cegepgim.goflight.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ca.cegepgim.goflight.R;
import ca.cegepgim.goflight.model.Airport;

public class TotalFlightsAdapter extends RecyclerView.Adapter<TotalFlightsAdapter.RecyclerViewHolder> {

    Context context;
    LayoutInflater inflater;
    List<Airport> airports;

    public TotalFlightsAdapter(Context context,List<Airport> airports) {
        super();
        this.airports=airports;
        this.context = context;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_flight, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 3;
    }


}
