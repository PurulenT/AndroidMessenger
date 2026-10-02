package com.example.messenger;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatViewModel extends ViewModel {
    private MutableLiveData<List<Message>> messages = new MutableLiveData<>();
    private MutableLiveData<User> otherUser = new MutableLiveData<>();
    private MutableLiveData<Boolean> messageSent = new MutableLiveData<>();
    private MutableLiveData<String> errors = new MutableLiveData<>();

    private String currentUserId;
    private String otherUserId;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseUsers = firebaseDatabase.getReference("Users");
    private DatabaseReference databaseMessages = firebaseDatabase.getReference("Messages");

    public ChatViewModel(String currentUserId, String otherUserId) {
        this.currentUserId = currentUserId;
        this.otherUserId = otherUserId;
        databaseUsers.child(otherUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                otherUser.setValue(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseMessages.child(currentUserId).child(otherUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Message> messagesList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Message message = dataSnapshot.getValue(Message.class);
                    messagesList.add(message);
                }
                messages.setValue(messagesList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public LiveData<List<Message>> getMessages() {
        return messages;
    }

    public LiveData<User> getOtherUser() {
        return otherUser;
    }

    public LiveData<Boolean> getMessageSent() {
        return messageSent;
    }

    public LiveData<String> getErrors() {
        return errors;
    }

    public void sendMessage(Message message){
        databaseMessages
                .child(message.getSenderId())
                .child(message.getReceiverId())
                .push()
                .setValue(message)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        databaseMessages
                                .child(message.getReceiverId())
                                .child(message.getReceiverId())
                                .push()
                                .setValue(message)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        messageSent.setValue(true);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        errors.setValue(e.getMessage());
                                    }
                                });;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        errors.setValue(e.getMessage());
                    }
                });
    }
}
