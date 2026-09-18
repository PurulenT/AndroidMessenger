package com.example.messenger;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private final String TAG = "MainActivityLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = auth.getCurrentUser();
                if(user == null){
                    Log.d(TAG, "Not authorized");
                } else{
                    Log.d(TAG, "Authorized " + user.getUid());
                }
            }
        });

//        auth.createUserWithEmailAndPassword("nurihandsome@yandex.ru", "password")
//                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                    @Override
//                    public void onSuccess(AuthResult authResult) {
//                        FirebaseUser user = auth.getCurrentUser();
//                        if (user == null){
//                            Log.d(TAG, "Not authorized");
//                        } else {
//                            Log.d(TAG, "Authorized");
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d(TAG, e.toString());
//                    }
//                });

//        FirebaseUser user = auth.getCurrentUser();
////        auth.signOut();
//        user = auth.getCurrentUser();
//        if (user == null){
//            Log.d(TAG, "Not authorized");
//        }else{
//            Log.d(TAG, "Authorized " + user.getUid());
//        }


//        auth.signInWithEmailAndPassword("example@mail.ru", "password")
//                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                    @Override
//                    public void onSuccess(AuthResult authResult) {
//                        FirebaseUser user = auth.getCurrentUser();
//                        user = auth.getCurrentUser();
//                        if (user == null){
//                            Log.d(TAG, "Not authorized");
//                        }else{
//                            Log.d(TAG, "Authorized " + user.getUid());
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d(TAG, e.toString());
//                    }
//                });
//
//        auth.signOut();
//
        auth.sendPasswordResetEmail("nurihandsome@yandex.ru");


    }
}