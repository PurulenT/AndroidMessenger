package com.example.messenger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText editTextForgotEmail;
    private Button buttonForgotNext;

    private static final String EXTRA_EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();
        String email = getIntent().getStringExtra(EXTRA_EMAIL);
        editTextForgotEmail.setText(email);
        buttonForgotNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextForgotEmail.getText().toString().trim();
            }
        });

    }

    private void initViews(){
        editTextForgotEmail = findViewById(R.id.editTextForgotEmail);
        buttonForgotNext = findViewById(R.id.buttonForgotNext);
    }

    public static Intent newIntent(Context context, String email){
        Intent intent = new Intent(context, ForgotPasswordActivity.class);
        intent.putExtra(EXTRA_EMAIL, email);
        return intent;
    }
}