package com.example.messenger;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {
    private EditText editTextRegistrationName;
    private EditText editTextRegistrationSurname;
    private EditText editTextRegistrationEmailAddress;
    private EditText editTextRegistrationPassword;
    private EditText editTextRegistrationAge;
    private Button buttonRegistrationNext;
    private RegistrationViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();
        viewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);
        observeViewModel();

        buttonRegistrationNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextRegistrationName.getText().toString().trim();
                String surname = editTextRegistrationSurname.getText().toString().trim();
                String email = editTextRegistrationEmailAddress.getText().toString().trim();
                int age = Integer.parseInt(editTextRegistrationAge.getText().toString().trim());
                String password = editTextRegistrationPassword.getText().toString().trim();
                viewModel.signup(name, age, email, password);
            }
        });
    }

    private void initViews(){
        editTextRegistrationName = findViewById(R.id.editTextRegistrationName);
        editTextRegistrationSurname = findViewById(R.id.editTextRegistrationSurname);
        editTextRegistrationEmailAddress = findViewById(R.id.editTextRegistrationEmailAddress);
        editTextRegistrationPassword = findViewById(R.id.editTextRegistrationPassword);
        buttonRegistrationNext = findViewById(R.id.buttonRegistrationNext);
        editTextRegistrationAge = findViewById(R.id.editTextRegistrationAge);
    }

    private void observeViewModel(){
        viewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if(firebaseUser != null){
                    startActivity(MainActivity.newIntent(RegistrationActivity.this));
                    finish();
                }
            }
        });

        viewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if(errorMessage != null){
                    Toast.makeText(RegistrationActivity.this, errorMessage, LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    public static Intent newIntent(Context context){
        return new Intent(context, RegistrationActivity.class);
    }
}