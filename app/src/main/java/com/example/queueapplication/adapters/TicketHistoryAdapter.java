package com.example.queueapplication.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.queueapplication.data.models.Ticket;
import com.example.queueapplication.databinding.LayoutTicketHistoryItemBinding;

import java.util.List;

public class TicketHistoryAdapter extends RecyclerView.Adapter<TicketHistoryAdapter.ViewHolder> {
    private List<Ticket> dataset;

    public TicketHistoryAdapter(List<Ticket> dataset) {
        this.dataset = dataset;
    }

    public void updateData(List<Ticket> updated) {
        dataset.clear();
        dataset.addAll(updated);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutTicketHistoryItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ticket ticket = dataset.get(position);
        holder.bind(ticket, position);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private LayoutTicketHistoryItemBinding binding;
        public ViewHolder(LayoutTicketHistoryItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Ticket ticket, int position) {
            binding.tvUsername.setText(ticket.getUser().getName());
            binding.tvTicketNo.setText(ticket.getTicketNo());
            binding.tvStatus.setText(ticket.getStatus().name());
            binding.tvServiceName.setText(ticket.getService().getTitle());
        }
    }
}
