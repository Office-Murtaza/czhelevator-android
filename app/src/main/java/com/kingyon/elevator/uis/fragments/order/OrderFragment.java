package com.kingyon.elevator.uis.fragments.order;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.OrderDetailsEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.fragments.main2.found.utilsf.FoundFragemtUtils;
import com.leo.afbaselibrary.nets.entities.PageListEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Created By Admin  on 2020/6/11
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class OrderFragment extends FoundFragemtUtils {
    String type,type1;
    @BindView(R.id.rcv_order_list)
    RecyclerView rcvOrderList;
    Unbinder unbinder;
    private int page = 1;

    @Override
    protected void lazyLoad() {
        httpList(type,type1,page);
    }

    private void httpList(String type,String type1,int page) {
        NetService.getInstance().orderList(type,type1,page)
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<PageListEntity<OrderDetailsEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {

                    }

                    @Override
                    public void onNext(PageListEntity<OrderDetailsEntity> orderDetailsEntityPageListEntity) {


                    }
                });


    }

    public OrderFragment setIndex(String type,String type1) {
        this.type = type;
        this.type1 = type1;
        return (this);
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_ordertwo;
    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    protected void dealLeackCanary() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
