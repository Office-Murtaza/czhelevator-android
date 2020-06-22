package com.kingyon.elevator.uis.actiivty2.property;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.PointItemEntity;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.entities.entities.EquipmentDetailsRevenueEntiy;
import com.kingyon.elevator.entities.entities.QueryRecommendEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.activities.cooperation.CooperationDeviceReportActivity;
import com.kingyon.elevator.uis.adapters.adaptertwo.AttentionAdapter;
import com.kingyon.elevator.uis.adapters.adaptertwo.property.EqdetailsAdapter;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.FormatUtils;
import com.kingyon.elevator.utils.TimeUtil;
import com.kingyon.elevator.utils.utilstwo.OrdinaryActivity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Created By Admin  on 2020/6/19
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:设备详情带收益
 */
public class EquipmentDetailsRevenueActivity extends BaseActivity {
    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_device_id)
    TextView tvDeviceId;
    @BindView(R.id.tv_device_no)
    TextView tvDeviceNo;
    @BindView(R.id.tv_lift_no)
    TextView tvLiftNo;
    @BindView(R.id.ll_lift_no)
    LinearLayout llLiftNo;
    @BindView(R.id.tv_cell_address)
    TextView tvCellAddress;
    @BindView(R.id.tv_device_location)
    TextView tvDeviceLocation;
    @BindView(R.id.tv_device_time)
    TextView tvDeviceTime;
    @BindView(R.id.tv_device_status)
    TextView tvDeviceStatus;
    @BindView(R.id.tv_ad_status)
    TextView tvAdStatus;
    @BindView(R.id.ll_choose_time)
    LinearLayout llChooseTime;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.tv_choose_time)
    TextView tvChooseTime;
    long deviceId;
    String month = "";
    int page = 1;
    @BindView(R.id.rl_error)
    RelativeLayout rlError;
    @BindView(R.id.rl_null)
    RelativeLayout rlNull;
    EquipmentDetailsRevenueActivity activity;
    List<EquipmentDetailsRevenueEntiy> list = new ArrayList<>();
    private String role;
    EqdetailsAdapter adapter;
    @Override
    public int getContentViewId() {
        return R.layout.activity_equipmentdetailsrevenue;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        activity = this;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");//设置日期格式
        month = df.format(new Date());
        deviceId = getIntent().getLongExtra(CommonUtil.KEY_VALUE_1, 0);
        role = getIntent().getStringExtra(CommonUtil.KEY_VALUE_2);

        NetService.getInstance().deviceDetails(deviceId)
                .compose(this.<PointItemEntity>bindLifeCycle())
                .subscribe(new CustomApiCallback<PointItemEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                    }

                    @Override
                    public void onNext(PointItemEntity pointItemEntity) {
                        tvDeviceId.setText(CommonUtil.getDeviceId(pointItemEntity.getObjectId()));
                        tvDeviceNo.setText(pointItemEntity.getDeviceNo());
                        tvDeviceTime.setText(TimeUtil.getAllTimeNoSecond(pointItemEntity.getNetTime()));
                        tvCellAddress.setText(pointItemEntity.getCellAddress());
                        tvLiftNo.setText(pointItemEntity.getLiftNo());
                        StringBuilder stringBuilder = new StringBuilder();
                        if (!TextUtils.isEmpty(pointItemEntity.getCellName())) {
                            stringBuilder.append(pointItemEntity.getCellName()).append("·");
                        }
                        String buildUnit = String.format("%s%s", pointItemEntity.getBuild() != null ? pointItemEntity.getBuild() : "", pointItemEntity.getUnit() != null ? pointItemEntity.getUnit() : "");
                        if (!TextUtils.isEmpty(buildUnit)) {
                            stringBuilder.append(buildUnit).append("·");
                        }
                        if (!TextUtils.isEmpty(pointItemEntity.getLift())) {
                            stringBuilder.append(pointItemEntity.getLift()).append("·");
                        }
                        String deviceOritation = FormatUtils.getInstance().getDeviceOritation(pointItemEntity.getDevice());
                        if (!TextUtils.isEmpty(deviceOritation)) {
                            stringBuilder.append(deviceOritation).append("·");
                        }
                        String deviceLocatoin = stringBuilder.length() > 1 ? stringBuilder.substring(0, stringBuilder.length() - 1) : "";
                        tvDeviceLocation.setText(deviceLocatoin);
                        tvDeviceStatus.setText((TextUtils.equals(Constants.DEVICE_STATUS.NORMAL, pointItemEntity.getStatus()) || TextUtils.equals(Constants.DEVICE_STATUS.ONLINE, pointItemEntity.getStatus())) ? "正常" : "维修中");
                        tvAdStatus.setText(TextUtils.equals(Constants.Device_AD_STATUS.PROCESSING, pointItemEntity.getAdStatus()) ? "投放中" : "未投放");
                    }
                });
        if (role.equals(Constants.RoleType.PARTNER)){
            httpData1(page, month, deviceId);
        }else {
            httpData(page, month, deviceId);
        }


    }

    private void httpData1(int page, String month, long deviceId) {
        NetService.getInstance().getIncomeList(page, month, deviceId)
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<ConentEntity<EquipmentDetailsRevenueEntiy>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e(ex.getDisplayMessage(), ex.getCode());
                        OrdinaryActivity.closeRefresh(smartRefreshLayout);
                        if (ex.getCode() == -102) {
                            if (page > 1) {
                                ToastUtils.showToast(activity, ex.getDisplayMessage(), 1000);
                            } else {
                                rvList.setVisibility(View.GONE);
                                rlError.setVisibility(View.GONE);
                                rlNull.setVisibility(View.VISIBLE);
                            }

                        } else if (ex.getCode()==100200){
                            rvList.setVisibility(View.GONE);
                            rlError.setVisibility(View.GONE);
                            rlNull.setVisibility(View.GONE);
                        }else {
                            rvList.setVisibility(View.GONE);
                            rlError.setVisibility(View.VISIBLE);
                            rlNull.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onNext(ConentEntity<EquipmentDetailsRevenueEntiy> list) {
                        OrdinaryActivity.closeRefresh(smartRefreshLayout);
                        rvList.setVisibility(View.VISIBLE);
                        rlError.setVisibility(View.GONE);
                        rlNull.setVisibility(View.GONE);
                        addData(list);
                        if (list.getContent().size()>0||page>1) {
                            rvList.setVisibility(View.VISIBLE);
                            rlError.setVisibility(View.GONE);
                            rlNull.setVisibility(View.GONE);
                        }else {
                            rvList.setVisibility(View.GONE);
                            rlError.setVisibility(View.GONE);
                            rlNull.setVisibility(View.VISIBLE);
                        }
                    }
                });

    }

    private void httpData(int page, String month, long deviceId) {
        NetService.getInstance().getEquipmentDetailsRevenue(page, month, deviceId)
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<ConentEntity<EquipmentDetailsRevenueEntiy>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e(ex.getDisplayMessage(), ex.getCode());
                        OrdinaryActivity.closeRefresh(smartRefreshLayout);
                        if (ex.getCode() == -102) {
                            if (page > 1) {
                                ToastUtils.showToast(activity, ex.getDisplayMessage(), 1000);
                            } else {
                                rvList.setVisibility(View.GONE);
                                rlError.setVisibility(View.GONE);
                                rlNull.setVisibility(View.VISIBLE);
                            }

                        } else if (ex.getCode()==100200){
                            rvList.setVisibility(View.GONE);
                            rlError.setVisibility(View.GONE);
                            rlNull.setVisibility(View.GONE);
                        }else {
                            rvList.setVisibility(View.GONE);
                            rlError.setVisibility(View.VISIBLE);
                            rlNull.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onNext(ConentEntity<EquipmentDetailsRevenueEntiy> list) {
                        OrdinaryActivity.closeRefresh(smartRefreshLayout);
                        rvList.setVisibility(View.VISIBLE);
                        rlError.setVisibility(View.GONE);
                        rlNull.setVisibility(View.GONE);
                        addData(list);
                        if (list.getContent().size()>0||page>1) {
                            rvList.setVisibility(View.VISIBLE);
                            rlError.setVisibility(View.GONE);
                            rlNull.setVisibility(View.GONE);
                        }else {
                            rvList.setVisibility(View.GONE);
                            rlError.setVisibility(View.GONE);
                            rlNull.setVisibility(View.VISIBLE);
                        }
                    }
                });

    }

    private void addData(ConentEntity<EquipmentDetailsRevenueEntiy> listq) {
        for (int i = 0; i < listq.getContent().size(); i++) {
            EquipmentDetailsRevenueEntiy queryRecommendEntity = new EquipmentDetailsRevenueEntiy();
            queryRecommendEntity = listq.getContent().get(i);
            list.add(queryRecommendEntity);
        }
        if (adapter == null || page == 1) {
            adapter = new EqdetailsAdapter(activity);
            adapter.addData(list);
            rvList.setAdapter(adapter);
            rvList.setLayoutManager(new GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false));
        } else {
            adapter.addData(list);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                if (role.equals(Constants.RoleType.PARTNER)){
                    httpData1(page, month, deviceId);
                }else {
                    httpData(page, month, deviceId);
                }
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                LogUtils.e("onLoadMore");
                page++;
                if (role.equals(Constants.RoleType.PARTNER)){
                    httpData1(page, month, deviceId);
                }else {
                    httpData(page, month, deviceId);
                }
            }
        });
    }

    @OnClick({R.id.img_top_back, R.id.ll_choose_time,R.id.rl_error,R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_top_back:
                finish();
                break;
            case R.id.ll_choose_time:
                /*选择时间*/
                initTimePicker1();
                break;
            case R.id.rl_error:
                page =1;
                httpData(page, month, deviceId);

                break;
            case R.id.tv_right:
                Bundle bundle = new Bundle();
                bundle.putLong(CommonUtil.KEY_VALUE_1, deviceId);
                startActivityForResult(CooperationDeviceReportActivity.class, CommonUtil.REQ_CODE_1, bundle);

                break;
        }
    }

    private void initTimePicker1() {
        //选择出生年月日
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        SimpleDateFormat formatter_year = new SimpleDateFormat("yyyy ");
        String year_str = formatter_year.format(curDate);
        int year_int = (int) Double.parseDouble(year_str);

        SimpleDateFormat formatter_mouth = new SimpleDateFormat("MM ");
        String mouth_str = formatter_mouth.format(curDate);
        int mouth_int = (int) Double.parseDouble(mouth_str);

        SimpleDateFormat formatter_day = new SimpleDateFormat("dd ");
        String day_str = formatter_day.format(curDate);
        int day_int = (int) Double.parseDouble(day_str);


        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(1900, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(year_int, mouth_int - 1, day_int);

        //时间选择器
        TimePickerView pvTime1 = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                /*btn_Time.setText(getTime(date));*/
                LogUtils.e(getTime(date), date);
                tvChooseTime.setText(getTime(date));
//                month = com.leo.afbaselibrary.utils.TimeUtil.ymdToLong(getTime(date));
                month = getTime(date);
                page = 1;
                if (role.equals(Constants.RoleType.PARTNER)){
                    httpData1(page, month, deviceId);
                }else {
                    httpData(page, month, deviceId);
                }
                LogUtils.e(month);
            }
        }).setType(new boolean[]{true, true, false, false, false, false}) //年月日时分秒 的显示与否，不设置则默认全部显示
                .setLabel("年", "月", "", "", "", "")//默认设置为年月日时分秒
                .isCenterLabel(false)
                .setTitleText("")//标题
                .setDividerColor(0xFFFFFFFF)
                .setSubmitColor(0xFF2D6EF2)//确定按钮文字颜色
                .setCancelColor(0xFF999999)//取消按钮文字颜色
                .setTextColorCenter(0xFF333333)//设置选中项的颜色
                .setTextColorOut(0xFF666666)//设置没有被选中项的颜色
                .setTextColorCenter(0xFF000000)//
                .setTitleColor(0xFF000000)//标题文字颜色
                .setSubCalSize(16)//确定和取消文字大小
                .setTitleSize(16)//标题文字大小
                .setContentSize(21)
                .setDate(selectedDate)
                .setLineSpacingMultiplier(1.2f)
//                .setRangDate(startDate, endDate)
//                .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                .setDecorView(null)
                .build();
        pvTime1.show();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        return format.format(date);
    }

}
