package com.example.myapplicationc;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ShowUserAdapter extends RecyclerView.Adapter {

    List<User_Getter_Setter> user_getter_setters;
    ShowUserAdapter(List<User_Getter_Setter> user_getter_setters){
        this.user_getter_setters=user_getter_setters;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.show_user,viewGroup,false);
        showUserHolder showuserholder=new showUserHolder(view);
        return showuserholder;
    }

    TextView u_name,u_id,u_rate_p,User_pay,u_pay_date;
    Button call;
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        User_Getter_Setter client=user_getter_setters.get(position);
        u_name=holder.itemView.findViewById(R.id.user_name);
        u_id=holder.itemView.findViewById(R.id.userId);
        u_rate_p=holder.itemView.findViewById(R.id.rateShow_p);
        u_pay_date=holder.itemView.findViewById(R.id.pay_date_);
        call=holder.itemView.findViewById(R.id.call_main_screen);
        u_name.setText(""+client.getName());
        u_id.setText(""+client.getId());
        u_rate_p.setText(""+client.getRate());
        if(client.getIsActiveUser()==1){
            u_pay_date.setText(""+client.getPayDate());
        }else{
            u_pay_date.setVisibility(View.INVISIBLE);
            call.setVisibility(View.INVISIBLE);
        }

        LinearLayout linearLayout=holder.itemView.findViewById(R.id.linear_layout1);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),UserDash.class);
                intent.putExtra("id",client.getId());
                intent.putExtra("active",client.getIsActiveUser());
                view.getContext().startActivity(intent);
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+client.getMobileno().trim()));
                holder.itemView.getContext().startActivity(callIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return user_getter_setters.size();
    }

    public static class showUserHolder extends RecyclerView.ViewHolder{
        public showUserHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
