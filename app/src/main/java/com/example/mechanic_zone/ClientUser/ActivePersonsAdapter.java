package com.example.mechanic_zone.ClientUser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mechanic_zone.R;
import com.example.mechanic_zone.Model.ClientUsersModel.nameAndisActive;

import java.util.List;

public class ActivePersonsAdapter extends RecyclerView.Adapter<ActivePersonsAdapter.ViewHolder> {
     List<nameAndisActive> activeUsersList;
    Context context;

    public ActivePersonsAdapter(Context context,List<nameAndisActive> activeUsersList) {
        this.activeUsersList = activeUsersList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_active_person, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        nameAndisActive user = activeUsersList.get(position);
        holder.nameTextView.setText(user.getName());
    }

    @Override
    public int getItemCount() {
        return activeUsersList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView,number;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.hyy);
        }
    }
}

