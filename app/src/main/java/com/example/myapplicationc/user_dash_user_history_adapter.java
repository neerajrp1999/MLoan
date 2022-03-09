package com.example.myapplicationc;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class user_dash_user_history_adapter extends RecyclerView.Adapter {
    List<String[]> history;
    TextView text_text,text_date,text_amount,text_link;
    Button show_attachment;
    public user_dash_user_history_adapter(List<String[]> history) {
        this.history=history;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_dash_user_history,parent,false);
        user_dash_user_history_adapter.user_dash_user_history_Holder holder=new user_dash_user_history_adapter.user_dash_user_history_Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String a[]=history.get(position);
        text_text=(TextView) holder.itemView.findViewById(R.id.text_text);
        text_date=(TextView) holder.itemView.findViewById(R.id.text_date);
        text_amount=(TextView) holder.itemView.findViewById(R.id.text_amount);
        text_link=(TextView) holder.itemView.findViewById(R.id.text_link);
        show_attachment=(Button) holder.itemView.findViewById(R.id.show_attachment);
        text_text.setText(a[4]);
        text_date.setText(a[3]);
        text_amount.setText(a[1]);
        text_link.setText(a[2]);
        show_attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ImageOL(holder.itemView.getContext()).openImage(a[2]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return history.size();
    }

    public static class user_dash_user_history_Holder extends RecyclerView.ViewHolder {
        public user_dash_user_history_Holder(@NonNull View itemView) {
            super(itemView);
        }
    }
}