package com.app.mygame.userPost.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.mygame.databinding.ItemPlanBinding;
import com.app.mygame.userPost.model.Plan;

import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {

    private final List<Plan> plans;
    private final Context context;

    public PlanAdapter(List<Plan> plans, Context context) {
        this.plans = plans;
        this.context = context;
    }

    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPlanBinding binding = ItemPlanBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PlanViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        Plan plan = plans.get(position);

        holder.binding.planName.setText(plan.getName());
        holder.binding.planDescription.setText(plan.getDescription());
        holder.binding.planDiscountedPrice.setText("₹" + plan.getAmount());
        holder.binding.planCoin.setText(plan.getCoin() + " Coins");

        // Show discount badge if applicable
        if (plan.getDiscountPercentage() > 0) {
            holder.binding.planDiscount.setVisibility(View.VISIBLE);
            holder.binding.planDiscount.setText(plan.getDiscountPercentage() + "% OFF");
            holder.binding.planOriginalPrice.setVisibility(View.VISIBLE);
            holder.binding.planOriginalPrice.setText("₹" + (plan.getAmount() / (1 - plan.getDiscountPercentage() / 100.0)));
        } else {
            holder.binding.planDiscount.setVisibility(View.GONE);
            holder.binding.planOriginalPrice.setVisibility(View.GONE);
        }

        // Set click listener to show a toast
        holder.itemView.setOnClickListener(v ->
                Toast.makeText(context, "Clicked: " + plan.getName(), Toast.LENGTH_SHORT).show()
        );
    }


    @Override
    public int getItemCount() {
        return plans.size();
    }

    public static class PlanViewHolder extends RecyclerView.ViewHolder {
        private final ItemPlanBinding binding;

        public PlanViewHolder(@NonNull ItemPlanBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
