package com.example.queueapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.queueapplication.adapters.StaffAdapter;
import com.example.queueapplication.adapters.StudentAdapter;
import com.example.queueapplication.data.AppStore;
import com.example.queueapplication.data.enums.Role;
import com.example.queueapplication.data.models.User;
import com.example.queueapplication.databinding.ActivitySelectUserBinding;

import java.util.stream.Collectors;

public class SelectUserActivity extends AppCompatActivity {
    private ActivitySelectUserBinding binding;
    private StudentAdapter studentAdapter;
    private StaffAdapter staffAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        AppStore.init(this);

        studentAdapter = new StudentAdapter(AppStore.getInstance().getUsers().stream().filter(user -> user.getRole() == Role.STUDENT).collect(Collectors.toList()));
        studentAdapter.setListener(new StudentAdapter.OnItemClickListener() {
            @Override
            public void onSelect(User user) {
                AppStore.getInstance().setCurrentUser(user);
                startActivity(new Intent(SelectUserActivity.this, MainActivity.class));
            }
        });
        binding.recyclerViewStudent.setAdapter(studentAdapter);

        staffAdapter = new StaffAdapter(AppStore.getInstance().getUsers().stream().filter(user -> user.getRole() == Role.STAFF).collect(Collectors.toList()));
        staffAdapter.setListener(new StudentAdapter.OnItemClickListener() {
            @Override
            public void onSelect(User user) {
                AppStore.getInstance().setCurrentUser(user);
                startActivity(new Intent(SelectUserActivity.this, MainActivity.class));
            }
        });
        binding.recyclerViewStaff.setAdapter(staffAdapter);


    }
}