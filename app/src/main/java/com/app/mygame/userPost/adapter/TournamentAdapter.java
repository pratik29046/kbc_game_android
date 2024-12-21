package com.app.mygame.userPost.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.app.mygame.databinding.TournamentCardItemBinding;  // Import the generated binding class
import com.app.mygame.userPost.model.TournamentCard;
import java.util.List;

public class TournamentAdapter extends RecyclerView.Adapter<TournamentAdapter.TournamentViewHolder> {
    private Context context;
    private List<TournamentCard> tournaments;
    private OnTournamentClickListener listener;

    public interface OnTournamentClickListener {
        void onTournamentClick(TournamentCard tournament, int position);
    }

    public TournamentAdapter(Context context, List<TournamentCard> tournaments, OnTournamentClickListener listener) {
        this.context = context;
        this.tournaments = tournaments;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TournamentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout using view binding
        TournamentCardItemBinding binding = TournamentCardItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new TournamentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TournamentViewHolder holder, int position) {
        TournamentCard tournament = tournaments.get(position);

        // Use the view binding to access views
        holder.binding.titleText.setText(tournament.getTitle());
        holder.binding.descriptionText.setText(tournament.getDescription());
        holder.binding.dateText.setText(tournament.getDate());
        holder.binding.prizeText.setText(tournament.getPrize());

        // Load the image using Glide
        Glide.with(context)
                .load(tournament.getImageUrl())
                .centerCrop()
                .into(holder.binding.tournamentImage);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTournamentClick(tournament, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tournaments.size();
    }

    static class TournamentViewHolder extends RecyclerView.ViewHolder {
        TournamentCardItemBinding binding;  // View binding instance

        public TournamentViewHolder(@NonNull TournamentCardItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
