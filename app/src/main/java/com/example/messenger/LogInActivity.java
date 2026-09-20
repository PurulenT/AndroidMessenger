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

public class LogInActivity extends AppCompatActivity {

    private EditText editTextTextEmailAddress;
    private EditText editTextTextLoginPassword;
    private Button buttonLoginNext;
    private TextView textViewRegister;
    private TextView textViewForgotPassword;

    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        observeViewModel();
        setUpClickListeners();
    }

    private void initViews(){
        editTextTextEmailAddress = findViewById(R.id.editTextTextEmailAddress);
        editTextTextLoginPassword = findViewById(R.id.editTextTextLoginPassword);
        buttonLoginNext = findViewById(R.id.buttonLoginNext);
        textViewRegister = findViewById(R.id.textViewRegister);
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword);
    }

    private void observeViewModel(){
        loginViewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if(firebaseUser != null){
                    startActivity(MainActivity.newIntent(LogInActivity.this));
                    finish();
                }
            }
        });

        loginViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if (errorMessage != null) {
                    Toast.makeText(LogInActivity.this, errorMessage, LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setUpClickListeners(){
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(RegistrationActivity.newIntent(LogInActivity.this));
            }
        });

        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ForgotPasswordActivity.newIntent(
                        LogInActivity.this,
                        editTextTextEmailAddress.getText().toString().trim()));
            }
        });

        buttonLoginNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextTextEmailAddress.getText().toString().trim();
                String password = editTextTextLoginPassword.getText().toString().trim();
                loginViewModel.login(email, password);
            }
        });
    }

    public static Intent newIntent(Context context){
        return new Intent(context, LogInActivity.class);
    }
}

