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
import com.example.queueapplication.data.enums.Status;
import com.example.queueapplication.data.models.Service;
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
        keyDisplay = getIntent().getStringExtra("KEY_DISPLAY");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        boolean isAllView = "all".equalsIgnoreCase(keyDisplay);

        AppStore store = AppStore.getInstance();

        List<Ticket> initialList;

        if (isAllView) {
            initialList = store.getSortedWaitingList();
        } else {
            initialList = store.getTicketsByService(store.getCurrentService());

            if (initialList == null) {
                initialList = new ArrayList<>();
            }
        }

        ticketAdapter = new TicketAdapter(initialList, isAllView);
        binding.recyclerView.setAdapter(ticketAdapter);

        ticketToDisplay = isAllView
                ? null
                : store.getCurrentServingByService(store.getCurrentService());


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
            boolean success = AppStore.getInstance().callNext(AppStore.getInstance().getCurrentService());
            if(!success) {
                return;
            }
            Toast.makeText(this, "Called next", Toast.LENGTH_SHORT).show();
            ticketAdapter.updateData(AppStore.getInstance().getTicketsByService(AppStore.getInstance().getCurrentService()));
            ticketToDisplay = isAllView
                    ? null
                    : store.getCurrentServingByService(store.getCurrentService());
            updateUI();

        });
        binding.btnDone.setOnClickListener(view -> {
            boolean success = AppStore.getInstance().markAsDone(AppStore.getInstance().getCurrentService());
            if(!success) {
                return;
            }
            Toast.makeText(this, "Ticket mark as done", Toast.LENGTH_SHORT).show();
            ticketAdapter.updateData(AppStore.getInstance().getTicketsByService(AppStore.getInstance().getCurrentService()));
            ticketToDisplay = isAllView
                    ? null
                    : store.getCurrentServingByService(store.getCurrentService());
            updateUI();
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

        AppStore store = AppStore.getInstance();
        User currentUser = store.getCurrentUser();
        Service currentService = store.getCurrentService();

        boolean isAllView = "all".equalsIgnoreCase(keyDisplay);

        List<Ticket> waitingTickets = currentService == null
                ? new ArrayList<>()
                : store.getTicketsByService(currentService);

        if (waitingTickets == null) {
            waitingTickets = new ArrayList<>();
        }

        boolean hasWaitingTickets = !waitingTickets.isEmpty();
        boolean hasServingTicket = ticketToDisplay != null;

        binding.btnCallNext.setEnabled(false);
        binding.btnDone.setEnabled(false);

        binding.tvEmpty.setVisibility(hasWaitingTickets ? GONE : VISIBLE);

        if (currentUser.getRole() == Role.STAFF) {
            if (currentService != null
                    && currentService.getStaffs().contains(currentUser.getId())) {

                binding.btnGroupStaff.setVisibility(VISIBLE);

                binding.btnCallNext.setEnabled(
                        !hasServingTicket && hasWaitingTickets
                );

                binding.btnDone.setEnabled(hasServingTicket);
            }
        } else {
            if (!isAllView) {
                binding.btnJoin.setVisibility(VISIBLE);
            }
        }

        if (!hasServingTicket) {
            binding.tvNoServing.setVisibility(VISIBLE);
            binding.currentServing.setVisibility(GONE);
        } else {
            binding.tvNoServing.setVisibility(GONE);
            binding.currentServing.setVisibility(VISIBLE);

            binding.tvTicketNumber.setText(ticketToDisplay.getTicketNo());
            binding.tvStatus.setText(ticketToDisplay.getStatus().name());
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateTickets();
    }
}