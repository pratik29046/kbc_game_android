package com.app.mygame.userPost.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.mygame.databinding.ItemPlanBinding;
import com.app.mygame.userPost.responseVo.PlansResponse;

import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {

    private final List<PlansResponse.Data> plans;
    private final Context context;

    public PlanAdapter(List<PlansResponse.Data> plans, Context context) {
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
        PlansResponse.Data plan = plans.get(position);

        holder.binding.planName.setText(plan.name);
        holder.binding.planDescription.setText(plan.description);
        holder.binding.planDiscountedPrice.setText("₹" + plan.amount);
        holder.binding.planCoin.setText(plan.coin + " Coins");

        // Show discount badge if applicable
        if (plan.discountPercentage > 0) {
            holder.binding.planDiscount.setVisibility(View.VISIBLE);
            holder.binding.planDiscount.setText(plan.discountPercentage + "% OFF");
            holder.binding.planOriginalPrice.setVisibility(View.VISIBLE);
            holder.binding.planOriginalPrice.setText("₹" + (plan.amount / (1 - plan.discountPercentage / 100.0)));
        } else {
            holder.binding.planDiscount.setVisibility(View.GONE);
            holder.binding.planOriginalPrice.setVisibility(View.GONE);
        }

        // Set click listener to show a toast
        holder.itemView.setOnClickListener(v ->
                Toast.makeText(context, "Clicked: " + plan.name, Toast.LENGTH_SHORT).show()
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
