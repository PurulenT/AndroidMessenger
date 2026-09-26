package com.example.messenger;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationViewModel extends ViewModel {

    private FirebaseAuth auth;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("Users");

    private MutableLiveData<String> error = new MutableLiveData<>();
    private MutableLiveData<FirebaseUser> user = new MutableLiveData<>();

    public MutableLiveData<FirebaseUser> getUser() {
        return user;
    }

    public MutableLiveData<String> getError() {
        return error;
    }

    public RegistrationViewModel() {
        auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null){
                    user.setValue(firebaseAuth.getCurrentUser());
                }
            }
        });
    }

    public void signup(
            String name,
            String surname,
            int age,
            String email,
            String password

    ){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser firebaseUser = authResult.getUser();
                        if (firebaseUser == null){
                            return;
                        }
                        User user = new User(
                                firebaseUser.getUid(),
                                name,
                                surname,
                                email,
                                age,
                                password,
                                false
                        );
                        databaseReference.child(user.getId()).setValue(user);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        error.setValue(e.toString());
                    }
                });
    }
}
