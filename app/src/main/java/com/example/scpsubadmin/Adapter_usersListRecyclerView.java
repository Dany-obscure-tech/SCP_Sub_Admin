package com.example.scpsubadmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter_usersListRecyclerView extends RecyclerView.Adapter<Adapter_usersListRecyclerView.ViewHolder> {
    Context context;
    List<String> users_name_List;
    List<String> users_car_no;
    List<String> users_phno;

    public Adapter_usersListRecyclerView(Context context, List<String> users_name_List, List<String> users_car_no, List<String> users_phno) {
        this.context = context;
        this.users_name_List = users_name_List;
        this.users_car_no = users_car_no;
        this.users_phno = users_phno;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_userlist_recyclerview, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name_textView.setText(users_name_List.get(position));
        holder.carNumber_textView.setText(users_car_no.get(position));
        holder.phno_textView.setText(users_phno.get(position));
    }


    @Override
    public int getItemCount() {
        return users_name_List.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name_textView;
        public TextView carNumber_textView;
        public TextView phno_textView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.name_textView = (TextView) itemView.findViewById(R.id.name_textView);
            this.carNumber_textView = (TextView) itemView.findViewById(R.id.carNumber_textView);
            this.phno_textView = (TextView) itemView.findViewById(R.id.phno_textView);
        }
    }
}