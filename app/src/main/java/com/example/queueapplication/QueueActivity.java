package com.example.queueapplication;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.queueapplication.adapters.TicketAdapter;
import com.example.queueapplication.data.AppStore;
import com.example.queueapplication.data.enums.Role;
import com.example.queueapplication.data.models.Ticket;
import com.example.queueapplication.data.models.User;
import com.example.queueapplication.databinding.ActivityQueueBinding;
import com.example.queueapplication.helpers.Helper;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class QueueActivity extends AppCompatActivity {
    private ActivityQueueBinding binding;
    private TicketAdapter ticketAdapter;
    private String keyDisplay;
    private Ticket ticketToDisplay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityQueueBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        keyDisplay = getIntent().getStringExtra("KEY_DISPLAY");
        List<Ticket> initialList = (keyDisplay != null && keyDisplay.equalsIgnoreCase("all"))
                ? AppStore.getInstance().getSortedWaitingList()
                : AppStore.getInstance().getTicketsByService(AppStore.getInstance().getCurrentService());
        ticketAdapter = new TicketAdapter(initialList);
        ticketAdapter.setListener(new TicketAdapter.OnItemClickListener() {
            @Override
            public void onClick(Ticket ticket) {
                // TODO: Open ticket detail dialog
            }
        });
        binding.recyclerView.setAdapter(ticketAdapter);

        ticketToDisplay = AppStore.getInstance().getCurrentServingByService(AppStore.getInstance().getCurrentService());

        binding.btnJoin.setOnClickListener(view -> {
            Ticket ticket = AppStore.getInstance().joinQueue();
            if(ticket == null) {
                Toast.makeText(this, "You already have an active ticket.", Toast.LENGTH_SHORT).show();
                return;
            }

            Snackbar.make(view, "Joined queue successfully!", Snackbar.LENGTH_SHORT).show();

            updateTickets();
        });
        binding.btnCallNext.setOnClickListener(view -> {

        });
        binding.btnDone.setOnClickListener(view -> {

        });

        updateUI();
    }

    private void updateTickets() {
        keyDisplay = getIntent().getStringExtra("KEY_DISPLAY");
        if(keyDisplay != null && keyDisplay.equalsIgnoreCase("all")) {
            ticketAdapter.updateData(AppStore.getInstance().getSortedWaitingList());
        } else {
            ticketAdapter.updateData(AppStore.getInstance().getTicketsByService(AppStore.getInstance().getCurrentService()));
        }
    }

    public void updateUI() {
        binding.btnGroupStaff.setVisibility(GONE);
        binding.btnJoin.setVisibility(GONE);

        binding.tvNoServing.setVisibility(GONE);
        binding.currentServing.setVisibility(GONE);

        User currentUser = AppStore.getInstance().getCurrentUser();
        boolean isAllView = keyDisplay != null && keyDisplay.equalsIgnoreCase("all");

        if(currentUser.getRole() == Role.STAFF) {
            binding.btnGroupStaff.setVisibility(VISIBLE);
        } else {
            // Students should only see "Join Queue" if they are looking at a specific service, NOT when viewing "All"
            if (!isAllView) {
                binding.btnJoin.setVisibility(VISIBLE);
            }
        }

        if(ticketToDisplay == null) {
            binding.tvNoServing.setVisibility(VISIBLE);
            binding.currentServing.setVisibility(GONE);
        } else {
            binding.tvNoServing.setVisibility(GONE);
            binding.currentServing.setVisibility(VISIBLE);

            binding.tvTicketNumber.setText(ticketToDisplay.getTicketNo());
            binding.tvStatus.setText(ticketToDisplay.getStatus().name());
//            binding.tvEwt.setText();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTickets();
//        updateUI();
    }
}