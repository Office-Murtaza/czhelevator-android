package com.kingyon.elevator.uis.activities.order;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kingyon.elevator.R;

import butterknife.ButterKnife;

/**
 * 订单确认界面
 */
public class ConfirmOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        ButterKnife.bind(this);



    }
}
