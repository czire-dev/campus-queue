package com.example.queueapplication.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.queueapplication.data.models.User;
import com.example.queueapplication.databinding.LayoutUserItemBinding;

import java.util.List;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.ViewHolder>{
    private List<User> dataset;
    private StudentAdapter.OnItemClickListener listener;

    public StaffAdapter(List<User> dataset) {
        this.dataset = dataset;
    }

    public void setListener(StudentAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutUserItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User staff = dataset.get(position);
        holder.bind(staff, listener, position);
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

        public void bind(User staff, StudentAdapter.OnItemClickListener listener, int position) {
            binding.tvUsername.setText(staff.getName());
            binding.tvRole.setText(staff.getRole().name());
            binding.getRoot().setOnClickListener(view -> listener.onSelect(staff));
        }
    }
}
