package com.example.queueapplication.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.queueapplication.data.models.User;
import com.example.queueapplication.databinding.LayoutUserItemBinding;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    private List<User> dataset;
    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onSelect(User user);
    }

    public StudentAdapter(List<User> dataset) {
        this.dataset = dataset;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutUserItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = dataset.get(position);
        holder.bind(user, listener, position);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private LayoutUserItemBinding binding;
        public ViewHolder(LayoutUserItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(User user, OnItemClickListener listener, int position) {
            binding.tvUsername.setText(user.getName());
            binding.tvRole.setText(user.getRole().name());
            binding.getRoot().setOnClickListener(view -> listener.onSelect(user));
        }
    }
}
