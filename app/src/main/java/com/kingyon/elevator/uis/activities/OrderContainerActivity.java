package com.kingyon.elevator.uis.activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.NormalParamEntity;
import com.kingyon.elevator.uis.fragments.main.OrderFragment;
import com.kingyon.elevator.utils.MyStatusBarUtils;

public class OrderContainerActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    NormalParamEntity entity = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyStatusBarUtils.setStatusBar(this, "#ffffff");
        setContentView(R.layout.activity_order_container);
        entity = getIntent().getParcelableExtra("normalEntity");
        if (entity != null) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, OrderFragment.newInstance(entity));
            fragmentTransaction.commit();
        }
    }
}
