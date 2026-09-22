package com.example.messenger;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {

    private List<User> users = new ArrayList<>();

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    private OnUserClickListener onUserClickListener;

    public void setOnUserClickListener(OnUserClickListener onUserClickListener) {
        this.onUserClickListener = onUserClickListener;
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.user_item,
                parent,
                false
        );
        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        User user = users.get(position);
        String userInfo = String.format("%s %s, %s", user.getName(), user.getSurname(), user.getAge());
        holder.textViewUserInfo.setText(userInfo);

        int backgroundId;
        if(user.isOnline()){
            backgroundId = R.drawable.circle_green;
        } else{
            backgroundId = R.drawable.circle_red;
        }
        Drawable status = ContextCompat.getDrawable(holder.itemView.getContext(), backgroundId);
        holder.viewOnlineStatus.setBackground(status);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onUserClickListener != null){
                    onUserClickListener.onClick(user);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class UsersViewHolder extends RecyclerView.ViewHolder{

        TextView textViewUserInfo;
        View viewOnlineStatus;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews();
        }

        private void initViews(){
            textViewUserInfo = itemView.findViewById(R.id.textViewUserInfo);
            viewOnlineStatus = itemView.findViewById(R.id.viewOnlineStatus);
        }
    }

    interface OnUserClickListener{
        void onClick(User user);
    }
}
