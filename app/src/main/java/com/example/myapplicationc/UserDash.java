package com.example.myapplicationc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UserDash extends AppCompatActivity {
    TextView id,name,mobileno;
    FloatingActionButton call;
    Button paymoney;
    int _id;
    User_Getter_Setter user_getter_setter;
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dash);

        id=(TextView) findViewById(R.id.dashid);
        name=(TextView) findViewById(R.id.dashName);
        mobileno=(TextView) findViewById(R.id.dashmobileno);
        call=(FloatingActionButton)findViewById(R.id.call);
        paymoney=(Button)findViewById(R.id.pay_money);

        _id=getIntent().getExtras().getInt("id");
        int isActive=getIntent().getExtras().getInt("active");
        if(isActive==1){
            user_getter_setter=new db(getApplicationContext()).feachActiveUserDataById(_id);
            paymoney.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent newintent=new Intent(getApplicationContext(),PayActivity.class);
                    newintent.putExtra("id",_id);
                    startActivity(newintent);
                    finish();
                }
            });
        }
        else {
            user_getter_setter=new db(getApplicationContext()).feachUserDataById(_id);
            paymoney.setEnabled(false);
            paymoney.setVisibility(View.GONE);
        }
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+user_getter_setter.getMobileno().trim()));
                startActivity(callIntent);
            }
        });
        name.setText(""+user_getter_setter.getName());
        id.setText(""+_id);
        mobileno.setText(""+user_getter_setter.getMobileno());

        recyclerView=(RecyclerView)findViewById(R.id.userDashRecycleview);
        layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        user_dash_user_history_adapter adapter=new user_dash_user_history_adapter(new db(getApplicationContext()).fetchHistory_Id(_id));// _id
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        //startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }
}