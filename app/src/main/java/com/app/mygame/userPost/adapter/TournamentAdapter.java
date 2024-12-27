package com.app.mygame.userPost.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.mygame.userPost.responseVo.AllTournamentsResponse;
import com.app.mygame.utils.DateUtility;
import com.bumptech.glide.Glide;
import com.app.mygame.databinding.TournamentCardItemBinding;  // Import the generated binding class
import com.app.mygame.userPost.model.TournamentCard;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TournamentAdapter extends RecyclerView.Adapter<TournamentAdapter.TournamentViewHolder> {
    private Context context;
    private List<AllTournamentsResponse.Data> tournaments;
    private OnTournamentClickListener listener;

    public interface OnTournamentClickListener {
        void onTournamentClick(AllTournamentsResponse.Data tournament, int position);
    }

    public TournamentAdapter(Context context, List<AllTournamentsResponse.Data> tournaments, OnTournamentClickListener listener) {
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
        AllTournamentsResponse.Data tournament = tournaments.get(position);

        String formattedDate = DateUtility.formatDate(tournament.tournamentStartDateTime);
        // Use the view binding to access views
        holder.binding.titleText.setText(tournament.title);
        holder.binding.descriptionText.setText(tournament.description);
        holder.binding.dateText.setText(formattedDate);  // Set the formatted date as a String
        holder.binding.prizeText.setText(String.valueOf((int) tournament.winningPrice));  // Fix casting for prize text

        // Load the image using Glide
        Glide.with(context)
                .load(tournament.imageUrl)
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
