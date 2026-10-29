package com.example.queueapplication;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.queueapplication.adapters.ServiceAdapter;
import com.example.queueapplication.data.AppStore;
import com.example.queueapplication.data.enums.Role;
import com.example.queueapplication.data.enums.Status;
import com.example.queueapplication.data.models.Service;
import com.example.queueapplication.data.models.Ticket;
import com.example.queueapplication.databinding.ActivityMainBinding;
import com.example.queueapplication.databinding.LayoutMyTicketDialogBinding;
import com.example.queueapplication.helpers.Helper;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private Ticket currentTicket;
    private ServiceAdapter serviceAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        serviceAdapter = new ServiceAdapter(new ArrayList<>(AppStore.getInstance().services));
        serviceAdapter.setListener(new ServiceAdapter.OnItemClickListener() {
            @Override
            public void onServiceClick(Service service) {
                AppStore.getInstance().setCurrentService(service);
                Log.d("CHANGE_SERVICE", AppStore.getInstance().getCurrentService().getTitle());
                startActivity(new Intent(getApplicationContext(), QueueActivity.class));
            }
        });
        binding.recyclerViewServices.setAdapter(serviceAdapter);

        binding.tvStudentName.setText(AppStore.getInstance().getCurrentUser().getName());

        updateUI();

        binding.btnViewTicket.setOnClickListener(view -> {
            updateUI();

            // open dialog
            LayoutMyTicketDialogBinding ticketDialogBinding = LayoutMyTicketDialogBinding.inflate(getLayoutInflater());

            ticketDialogBinding.tvServiceName.setText(currentTicket.getService().getTitle());
            ticketDialogBinding.imgCard.setImageResource(currentTicket.getService().getImgResourceId());
            ticketDialogBinding.tvTicketNo.setText(currentTicket.getTicketNo());
            ticketDialogBinding.tvPositionNum.setText(Helper.formatPosition(AppStore.getInstance().getPosition(currentTicket)));
            ticketDialogBinding.tvChipStatus.setText(currentTicket.getStatus().name());
//            ticketDialogBinding.tvEwt.setText();

            AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                    .setView(ticketDialogBinding.getRoot())
                    .create();

            ticketDialogBinding.btnCancel.setOnClickListener(view1 -> {
                Ticket ticket = AppStore.getInstance().changeStatus(currentTicket, Status.CANCELLED);
                if(ticket == null) {
                    Toast.makeText(this, "Invalid status change from " + currentTicket.getStatus() + " to CANCELLED", Toast.LENGTH_SHORT).show();
                    return;
                }
                Snackbar.make(view, "Ticket cancelled successfully", Snackbar.LENGTH_SHORT).show();
                updateUI();
                dialog.dismiss();
            });

            dialog.show();
        });
        binding.btnViewAll.setOnClickListener(view -> {
            Intent intent = new Intent(this, QueueActivity.class);
            intent.putExtra("KEY_DISPLAY", "All");
            startActivity(intent);
        });
        binding.btnProfile.setOnClickListener(view -> {
            startActivity(new Intent(this, HistoryActivity.class));
        });
    }

    public void updateUI() {
        currentTicket = AppStore.getInstance().getActiveTicket();

        if(currentTicket == null) {
            binding.btnViewTicket.setVisibility(GONE);
        } else {
            binding.btnViewTicket.setVisibility(VISIBLE);
        }



    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }
}