package com.example.queueapplication;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.queueapplication.adapters.TicketHistoryAdapter;
import com.example.queueapplication.data.AppStore;
import com.example.queueapplication.databinding.ActivityHistoryBinding;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    private ActivityHistoryBinding binding;
    private TicketHistoryAdapter ticketHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ticketHistoryAdapter = new TicketHistoryAdapter(AppStore.getInstance().getCurrentUserHistory());
        binding.recyclerView.setAdapter(ticketHistoryAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ticketHistoryAdapter.updateData(AppStore.getInstance().getCurrentUserHistory());
    }
}