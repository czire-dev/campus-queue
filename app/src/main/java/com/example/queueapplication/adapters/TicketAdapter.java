package com.example.queueapplication.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.queueapplication.data.AppStore;
import com.example.queueapplication.data.models.Ticket;
import com.example.queueapplication.databinding.LayoutTicketItemBinding;

import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ViewHolder> {
    private List<Ticket> dataset;
    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onClick(Ticket ticket);
    }

    public TicketAdapter(List<Ticket> dataset) {
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
        return new ViewHolder(LayoutTicketItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ticket ticket = dataset.get(position);
        holder.bind(ticket, listener, position);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private LayoutTicketItemBinding binding;

        public ViewHolder(LayoutTicketItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Ticket ticket, OnItemClickListener listener, int position) {
            // Safe position calculation based on the specific ticket's service queue
            List<Ticket> serviceTickets = AppStore.getInstance().getTicketsByService(ticket.getService());
            int queuePosition = position + 1;

            binding.tvPosition.setText(String.valueOf(AppStore.getInstance().getPosition(ticket)));

            String ticketNumber = ticket.getTicketNo() +
                    (AppStore.getInstance().getCurrentUser().getId() == ticket.getUser().getId() ? " (YOU)" : "");
            binding.tvTicketNumber.setText(ticketNumber);
            binding.tvPriority.setText(ticket.getService().getPriorityLevel().name());

            binding.getRoot().setOnClickListener(view -> {
                if (listener != null) {
                    listener.onClick(ticket);
                }
            });
        }
    }
}
