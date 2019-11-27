package com.kingyon.elevator.uis.activities.cooperation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.mvpbase.MvpBaseActivity;
import com.kingyon.elevator.presenter.AddNewBankCardPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddNewBankCardActivity extends MvpBaseActivity<AddNewBankCardPresenter> {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_bank_card);
        ButterKnife.bind(this);

    }

    @Override
    public AddNewBankCardPresenter initPresenter() {
        return new AddNewBankCardPresenter(this);
    }



}
