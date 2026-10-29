package com.example.queueapplication.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.queueapplication.data.models.Service;
import com.example.queueapplication.databinding.LayoutServiceCardBinding;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {
    private List<Service> dataset;
    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onServiceClick(Service service);
    }

    public ServiceAdapter(List<Service> dataset) {
        this.dataset = dataset;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutServiceCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Service service = dataset.get(position);
        holder.bind(service, listener);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private LayoutServiceCardBinding binding;
        public ViewHolder(LayoutServiceCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Service service, OnItemClickListener listener) {
            binding.tvServiceName.setText(service.getTitle());
            binding.tvSubtitle.setText(service.getSubtitle());
            binding.imgCard.setImageResource(service.getImgResourceId());

            binding.getRoot().setOnClickListener(view -> listener.onServiceClick(service));
        }
    }
}
