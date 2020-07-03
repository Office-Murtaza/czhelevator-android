package com.kingyon.elevator.uis.activities.order;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.gerry.scaledelete.DeletedImageScanDialog;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.customview.MyActionBar;
import com.kingyon.elevator.date.DateUtils;
import com.kingyon.elevator.entities.ADEntity;
import com.kingyon.elevator.entities.AutoCalculationDiscountEntity;
import com.kingyon.elevator.entities.CellItemEntity;
import com.kingyon.elevator.entities.CommitOrderEntiy;
import com.kingyon.elevator.entities.CouponItemEntity;
import com.kingyon.elevator.entities.GoPlaceAnOrderEntity;
import com.kingyon.elevator.entities.ImageScan;
import com.kingyon.elevator.entities.OrderIdentityEntity;
import com.kingyon.elevator.entities.PointItemEntity;
import com.kingyon.elevator.mvpbase.MvpBaseActivity;
import com.kingyon.elevator.presenter.ConfirmOrderPresenter;
import com.kingyon.elevator.uis.activities.advertising.NetVideoPlayActivity;
import com.kingyon.elevator.uis.activities.plan.OrderCouponsActivity;
import com.kingyon.elevator.uis.activities.user.IdentityInfoActivity;
import com.kingyon.elevator.uis.dialogs.ImageDialog;
import com.kingyon.elevator.uis.dialogs.OrderIdentityDialog;
import com.kingyon.elevator.uis.dialogs.PayDialog;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.DialogUtils;
import com.kingyon.elevator.utils.FormatUtils;
import com.kingyon.elevator.utils.MyActivityUtils;
import com.kingyon.elevator.utils.RuntimeUtils;
import com.kingyon.elevator.view.ConfirmOrderView;
import com.kingyon.paylibrary.alipay.AliPayUtils;
import com.kingyon.paylibrary.wechatpay.WxPayUtils;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.leo.afbaselibrary.utils.ToastUtils;
import com.zhaoss.weixinrecorded.util.EventBusConstants;
import com.zhaoss.weixinrecorded.util.EventBusObjectEntity;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_PAY_SUCCESS;
import static com.czh.myversiontwo.utils.StringContent.STRING_PRICE2;

/**
 * 订单确认界面
 */
public class ConfirmOrderActivity extends MvpBaseActivity<ConfirmOrderPresenter> implements ConfirmOrderView {

    @BindView(R.id.tv_cat_order_detailed)
    TextView tv_cat_order_detailed;
    @BindView(R.id.et_input_ad_name)
    EditText et_input_ad_name;
    @BindView(R.id.tv_ad_type)
    TextView tv_ad_type;
    @BindView(R.id.ad_img_preview)
    ImageView ad_img_preview;
    @BindView(R.id.tv_start_date)
    TextView tv_start_date;
    @BindView(R.id.tv_start_date_desc)
    TextView tv_start_date_desc;
    @BindView(R.id.tv_end_date)
    TextView tv_end_date;
    @BindView(R.id.tv_end_date_desc)
    TextView tv_end_date_desc;
    @BindView(R.id.tv_all_screen_count)
    TextView tv_all_screen_count;
    @BindView(R.id.tv_discount_money)
    TextView tv_discount_money;
    @BindView(R.id.tv_order_money)
    TextView tv_order_money;
    @BindView(R.id.tv_go_pay)
    TextView tv_go_pay;
    @BindView(R.id.tv_discount_desc)
    TextView tv_discount_desc;
    @BindView(R.id.cuxiao_manjian)
    TextView cuxiao_manjian;
    @BindView(R.id.tv_youhuiquan)
    TextView tv_youhuiquan;


    GoPlaceAnOrderEntity goPlaceAnOrderEntity;
    SimpleDateFormat simpleDateFormat;
    SimpleDateFormat monthSimpleDateFormat;//格式化为 月日
    @BindView(R.id.my_action_bar)
    MyActionBar myActionBar;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_total_day)
    TextView tvTotalDay;
    @BindView(R.id.text_number)
    TextView textNumber;
    @BindView(R.id.el_adimg)
    RelativeLayout elAdimg;
    private String mediaPath = "";//多媒体数据的路径
    private String mediaType = "";//媒体的类型 VIDEO 还是IMAGE
    private String screenType = "";
    private int fromType;
    private ADEntity adEntity;
    private ArrayList<CouponItemEntity> coupons;
    AutoCalculationDiscountEntity autoCalculationDiscountEntity;
    String authStatus = Constants.IDENTITY_STATUS.NO_AUTH;
    private OrderIdentityDialog identityDialog;
    private double couponsAllPrice = 0;/*优惠的总金额*/
    private double couponsPrice = 0;/*使用优惠券的金额*/
    private double zheKouPrice = 0;/*使用折扣的金额*/
    private double realPayPrice = 0;/*实际付款的钱*/
    private Subscription countDownSubscribe;
    private AliPayUtils aliPayUtils;
    private WxPayUtils wxPayUtils;
    private Subscription delaySubscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        ButterKnife.bind(this);
        setStateLayout();
//        stateLayout.setErrorAction(v -> {
//            if (goPlaceAnOrderEntity != null) {
//                presenter.loadIdentityInfo(getAllMoney(),
//                        goPlaceAnOrderEntity.getPlanType(), false, "");
//            } else {
//                showShortToast("数据异常，请重试");
//                finish();
//            }
//        });
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        monthSimpleDateFormat = new SimpleDateFormat("MM月dd日");
        goPlaceAnOrderEntity = RuntimeUtils.goPlaceAnOrderEntity;
        if (goPlaceAnOrderEntity != null) {
            if (goPlaceAnOrderEntity.getCellItemEntityArrayList() != null) {
                for (CellItemEntity cell : goPlaceAnOrderEntity.getCellItemEntityArrayList()) {
                    ArrayList<PointItemEntity> cellPoints = cell.getPoints();
                    if (cellPoints != null && cellPoints.size() > cell.getChoosedScreenNum()) {
                        ArrayList<PointItemEntity> entities = new ArrayList<>();
                        entities.addAll(cellPoints.subList(0, cell.getChoosedScreenNum()));
                        cell.setPoints(entities);
                    }
                }
            }
            if (goPlaceAnOrderEntity.getPlanType().equals(Constants.PLAN_TYPE.INFORMATION)) {
                ad_img_preview.setVisibility(View.GONE);
                elAdimg.setVisibility(View.GONE);
                /*便民信息*/
                tv_ad_type.setText("便民信息");
                et_input_ad_name.setHint("请输入需投放的便民信息内容");
                et_input_ad_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(60)});
                textNumber.setText("0/60");
                et_input_ad_name.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        int textLength = s.toString().trim().length();
                        if (textLength <= 60) {
                            textNumber.setText(textLength + "/60");
                        } else {
                            ToastUtils.showToast(ConfirmOrderActivity.this,"最多输入60字",1000);
                        }
                    }
                });
            } else if (goPlaceAnOrderEntity.getPlanType().equals(Constants.PLAN_TYPE.DIY)) {
                initDiyAndBussines();
                setInformationHide();
                /*DIY广告*/
                tv_ad_type.setText("DIY广告");
                if (fromType == Constants.FROM_TYPE.MYAD) {
                    setMyAdData();
                } else {
                    mediaPath = getIntent().getStringExtra("path");
                    GlideUtils.loadRoundCornersImage(this, mediaPath, ad_img_preview, 20);
                }
                et_input_ad_name.setHint("请输入广告名称");
                et_input_ad_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
                textNumber.setText(et_input_ad_name.getText().toString().length()+"/20");
                et_input_ad_name.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        int textLength = s.toString().trim().length();
                        if (textLength <= 20) {
                            textNumber.setText(textLength + "/20");
                        } else {
                            ToastUtils.showToast(ConfirmOrderActivity.this,"最多输入20字",1000);
                        }
                    }
                });
            } else {
                /*商业广告*/
                initDiyAndBussines();
                setInformationHide();
                tv_ad_type.setText("商业广告");
                if (fromType == Constants.FROM_TYPE.MYAD) {
                    setMyAdData();
                } else {
                    mediaPath = getIntent().getStringExtra("path");
                    GlideUtils.loadRoundCornersImage(this, mediaPath, ad_img_preview, 20);
                }
                et_input_ad_name.setHint("请输入广告名称");
                et_input_ad_name.setMaxLines(20);
                textNumber.setText(et_input_ad_name.getText().toString().length()+"/20");
                et_input_ad_name.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                        int textLength = s.toString().trim().length();
                        if (textLength < 20) {
                            textNumber.setText(textLength + "/20");
                        } else {
                            ToastUtils.showToast(ConfirmOrderActivity.this,"最多输入20字",1000);
                        }
                    }
                });
            }
            tvTotalDay.setText(String.format("共%d天", FormatUtils.getInstance().getTimeDays(goPlaceAnOrderEntity.getStartTime(), goPlaceAnOrderEntity.getEndTime())));
            tv_all_screen_count.setText(getPointCount());
            try {
                Calendar startCalendar = Calendar.getInstance();
                startCalendar.setTimeInMillis(goPlaceAnOrderEntity.getStartTime());
                Calendar startCalendar1 = Calendar.getInstance();
                startCalendar1.setTimeInMillis(goPlaceAnOrderEntity.getEndTime());

                tv_start_date_desc.setText("开始");
                tv_end_date_desc.setText("结束");

                tv_start_date.setText(monthSimpleDateFormat.format(goPlaceAnOrderEntity.getStartTime()) + "(" + DateUtils.getWeekOfDate(startCalendar.getTime()) + ")");
                tv_end_date.setText(monthSimpleDateFormat.format(goPlaceAnOrderEntity.getEndTime()) + "(" + DateUtils.getWeekOfDate(startCalendar1.getTime()) + ")");
            } catch (Exception e) {
                e.printStackTrace();
            }
            presenter.loadIdentityInfo(getAllMoney(),
                    goPlaceAnOrderEntity.getPlanType(), false, "");
            LogUtils.e(getAllMoney());
        } else {
            showShortToast("参数缺失，请重试");
            finish();
        }
    }

    private void initDiyAndBussines() {
        fromType = getIntent().getIntExtra("fromType", 0);
        mediaType = getIntent().getStringExtra("resType");
        if (adEntity == null) {
            if (mediaType.equals(Constants.Materia_Type.IMAGE)) {
                screenType = Constants.AD_SCREEN_TYPE.FULL_IMAGE;
            } else if (mediaType.equals(Constants.Materia_Type.VIDEO)) {
                screenType = Constants.AD_SCREEN_TYPE.FULL_VIDEO;
            }
        }
    }

    /**
     * 跳转到优惠券选择界面
     */
    private void goSelectDiscount() {
        Bundle couponsBundle = new Bundle();
        couponsBundle.putFloat(CommonUtil.KEY_VALUE_1, (float) getAllMoney());
        couponsBundle.putString(CommonUtil.KEY_VALUE_2, goPlaceAnOrderEntity.getPlanType());
        if (coupons != null) {
            couponsBundle.putParcelableArrayList(CommonUtil.KEY_VALUE_3, coupons);
        } else {
            coupons = new ArrayList<>();
//            for (Integer id : autoCalculationDiscountEntity.getCons()) {
//                CouponItemEntity couponItemEntity = new CouponItemEntity();
//                couponItemEntity.setObjctId(id);
//                coupons.add(couponItemEntity);
//            }
            couponsBundle.putParcelableArrayList(CommonUtil.KEY_VALUE_3, coupons);
        }
        Intent intent = new Intent(this, OrderCouponsActivity.class);
        intent.putExtras(couponsBundle);
        startActivityForResult(intent, CommonUtil.REQ_CODE_2);
    }


    /**
     * 从我的广告选择跳转过来的，设置广告数据
     */
    private void setMyAdData() {
        adEntity = RuntimeUtils.adEntity;
        if (adEntity != null) {
            et_input_ad_name.setEnabled(false);
            et_input_ad_name.setFocusable(false);
            et_input_ad_name.setText(adEntity.getTitle());
            switch (adEntity.getScreenType()) {
                case Constants.AD_SCREEN_TYPE.FULL_VIDEO:
                    GlideUtils.loadVideoFrame(ConfirmOrderActivity.this, adEntity.getVideoUrl(), ad_img_preview);
                    break;
                case Constants.AD_SCREEN_TYPE.FULL_IMAGE:
                    GlideUtils.loadImage(ConfirmOrderActivity.this, adEntity.getImageUrl(), ad_img_preview);
                    break;
            }
        }
    }

    private void setInformationHide() {
        ad_img_preview.setVisibility(View.VISIBLE);
//        ad_name_container.setVisibility(View.VISIBLE);
    }

    @Override
    public ConfirmOrderPresenter initPresenter() {
        return new ConfirmOrderPresenter(this);
    }

    @OnClick({R.id.tv_cat_order_detailed, R.id.tv_youhuiquan, R.id.ad_img_preview, R.id.tv_go_pay})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cat_order_detailed:
                DialogUtils.getInstance().showOrderDetailedTipsDialog(this, goPlaceAnOrderEntity, getAllMoney(),
                        realPayPrice,
                        zheKouPrice,
                        couponsPrice);
                break;
            case R.id.tv_youhuiquan:
                goSelectDiscount();
                break;
            case R.id.ad_img_preview:
                previewAd();
                break;
            case R.id.tv_go_pay:
                //先判断有没有实名认证，再判断广告内容是否为空
                if (authStatus.equals(Constants.IDENTITY_STATUS.NO_AUTH)
                        || authStatus.equals(Constants.IDENTITY_STATUS.FAILD)
                        || authStatus.equals(Constants.IDENTITY_STATUS.AUTHING)) {

                    showOrderIdentityDialog();

                } else {
                    if (adEntity != null) {
                        if (realPayPrice > 0) {
                            if (autoCalculationDiscountEntity.isHasMore()) {
                                new AlertDialog.Builder(this)
                                        .setTitle("提示")
                                        .setMessage("您当前还有可使用的优惠券，是否继续支付？")
                                        .setPositiveButton("继续支付", (dialog, which) -> {
                                                    presenter.commitOrder(goPlaceAnOrderEntity, coupons, goPlaceAnOrderEntity.getPlanType(),
                                                            goPlaceAnOrderEntity.getStartTime(), goPlaceAnOrderEntity.getEndTime(), adEntity);
                                                    LogUtils.e("您当前还有可使用的优惠券", goPlaceAnOrderEntity, coupons, goPlaceAnOrderEntity.getPlanType(),
                                                            goPlaceAnOrderEntity.getStartTime(), goPlaceAnOrderEntity.getEndTime(), adEntity);
                                                }
                                        )
                                        .setNegativeButton("取消", null)
                                        .show();
                            } else {
                                presenter.commitOrder(goPlaceAnOrderEntity, coupons, goPlaceAnOrderEntity.getPlanType(),
                                        goPlaceAnOrderEntity.getStartTime(), goPlaceAnOrderEntity.getEndTime(), adEntity);
                                LogUtils.e("没有可以用优惠卷", goPlaceAnOrderEntity, coupons, goPlaceAnOrderEntity.getPlanType(),
                                        goPlaceAnOrderEntity.getStartTime(), goPlaceAnOrderEntity.getEndTime(), adEntity);
                            }
                        } else {
                            if (couponsPrice > getAllMoney()) {
                                //提示优惠券金额大于总价
                                new AlertDialog.Builder(this)
                                        .setTitle("提示")
                                        .setMessage("您当前使用的优惠券金额大于总金额，是否继续支付？")
                                        .setPositiveButton("继续支付", (dialog, which) -> {
                                                    presenter.commitOrder(goPlaceAnOrderEntity, coupons, goPlaceAnOrderEntity.getPlanType(),
                                                            goPlaceAnOrderEntity.getStartTime(), goPlaceAnOrderEntity.getEndTime(), adEntity);
                                                    LogUtils.e("优惠券金额大于总价", goPlaceAnOrderEntity, coupons, goPlaceAnOrderEntity.getPlanType(),
                                                            goPlaceAnOrderEntity.getStartTime(), goPlaceAnOrderEntity.getEndTime(), adEntity);
                                                }
                                        )
                                        .setNegativeButton("取消", null)
                                        .show();
                            } else {
                                presenter.commitOrder(goPlaceAnOrderEntity, coupons, goPlaceAnOrderEntity.getPlanType(),
                                        goPlaceAnOrderEntity.getStartTime(), goPlaceAnOrderEntity.getEndTime(), adEntity);
                                LogUtils.e("正常价格", goPlaceAnOrderEntity, coupons, goPlaceAnOrderEntity.getPlanType(),
                                        goPlaceAnOrderEntity.getStartTime(), goPlaceAnOrderEntity.getEndTime(), adEntity);
                            }
                        }
                    } else {
                        //没有选择已经上传过的广告
                        if (goPlaceAnOrderEntity.getPlanType().equals(Constants.PLAN_TYPE.INFORMATION)) {
                            if (et_input_ad_name.getText().toString().trim().isEmpty()) {
                                showShortToast("请输入便民信息");
                                return;
                            }
                            presenter.sendInformation(et_input_ad_name.getText().toString().trim());
                        } else {
                            if (et_input_ad_name.getText().toString().trim().isEmpty()) {
                                showShortToast("请输入广告名称");
                                return;
                            }
                            if (realPayPrice > 0) {
                                LogUtils.d("是否还有优惠券可用：" + autoCalculationDiscountEntity.isHasMore());
                                if (autoCalculationDiscountEntity.isHasMore()) {
                                    new AlertDialog.Builder(this)
                                            .setTitle("提示")
                                            .setMessage("您当前还有可使用的优惠券，是否继续支付？")
                                            .setPositiveButton("继续支付", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    presenter.uploadAdVideoOrImg(mediaPath, goPlaceAnOrderEntity.getPlanType(),
                                                            screenType, et_input_ad_name.getText().toString().trim());
                                                    LogUtils.e("是否还有优惠券可用", mediaPath, goPlaceAnOrderEntity.getPlanType(),
                                                            screenType, et_input_ad_name.getText().toString().trim());
                                                }
                                            })
                                            .setNegativeButton("取消", null)
                                            .show();
                                } else {
                                    presenter.uploadAdVideoOrImg(mediaPath, goPlaceAnOrderEntity.getPlanType(),
                                            screenType, et_input_ad_name.getText().toString().trim());
                                    LogUtils.e("是否还有优惠券可用1", mediaPath, goPlaceAnOrderEntity.getPlanType(),
                                            screenType, et_input_ad_name.getText().toString().trim());
                                }
                            } else {
                                if (couponsPrice > getAllMoney()) {
                                    //提示优惠券金额大于总价
                                    new AlertDialog.Builder(this)
                                            .setTitle("提示")
                                            .setMessage("您当前使用的优惠券金额大于总金额，是否继续支付？")
                                            .setPositiveButton("继续支付", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    presenter.uploadAdVideoOrImg(mediaPath, goPlaceAnOrderEntity.getPlanType(),
                                                            screenType, et_input_ad_name.getText().toString().trim());
                                                    LogUtils.e("您当前使用的优惠券金额大于总金额", mediaPath, goPlaceAnOrderEntity.getPlanType(),
                                                            screenType, et_input_ad_name.getText().toString().trim());
                                                }
                                            })
                                            .setNegativeButton("取消", null)
                                            .show();
                                } else {
                                    presenter.uploadAdVideoOrImg(mediaPath, goPlaceAnOrderEntity.getPlanType(),
                                            screenType, et_input_ad_name.getText().toString().trim());
                                    LogUtils.e("您当前使用的优惠券金额大于总金额111", mediaPath, goPlaceAnOrderEntity.getPlanType(),
                                            screenType, et_input_ad_name.getText().toString().trim());
                                }
                            }
                        }
                    }
                }
                break;
            default:
        }

    }

    private void previewAd() {
        if (adEntity != null) {
            if (adEntity.getScreenType().equals(Constants.AD_SCREEN_TYPE.FULL_VIDEO)) {
                Bundle videoBundle = new Bundle();
                String videoPath = adEntity.getVideoUrl();
                videoBundle.putString(CommonUtil.KEY_VALUE_1, videoPath);
                MyActivityUtils.goActivity(this, NetVideoPlayActivity.class, videoBundle);
            }
            if (adEntity.getScreenType().equals(Constants.AD_SCREEN_TYPE.FULL_IMAGE)) {
                DeletedImageScanDialog deletedImageScanDialog = new DeletedImageScanDialog(this
                        , new ImageScan(adEntity.getImageUrl()), null);
                deletedImageScanDialog.showOne();
            }
        } else {
            if (screenType.equals(Constants.AD_SCREEN_TYPE.FULL_VIDEO)) {
                Bundle videoBundle = new Bundle();
                String videoPath = mediaPath;
                videoBundle.putString(CommonUtil.KEY_VALUE_1, videoPath);
                MyActivityUtils.goActivity(this, NetVideoPlayActivity.class, videoBundle);
            }
            if (screenType.equals(Constants.AD_SCREEN_TYPE.FULL_IMAGE)) {
                ImageDialog dialog = new ImageDialog(this);
                dialog.show(mediaPath);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DialogUtils.getInstance().hideOrderDetailedTipsDialog();
    }

    public String listToString(List<AutoCalculationDiscountEntity.ConsCountBean> list, char separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i).getName() + " * " + list.get(i).getCount()).append(separator);
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }

    @Override
    public void showCouponsInfo(AutoCalculationDiscountEntity autoCalculationDiscountEntity) {
        this.autoCalculationDiscountEntity = autoCalculationDiscountEntity;
        cuxiao_manjian.setText(getLiJianPrice());
        String couponsText = "";
        if (autoCalculationDiscountEntity.getCons().size() == 0) {
            couponsText = "未选择优惠券";
        } else {
            couponsText = listToString(autoCalculationDiscountEntity.getConsCount(), ',');
        }
        realPayPrice = autoCalculationDiscountEntity.getActualAmount();
        zheKouPrice = autoCalculationDiscountEntity.getDiscountRate();
        couponsPrice = autoCalculationDiscountEntity.getConcessionalRate();
        couponsAllPrice = zheKouPrice + couponsPrice;
        tv_youhuiquan.setText(couponsText);
        tv_discount_money.setText("已优惠" + (autoCalculationDiscountEntity.getConcessionalRate() +
                autoCalculationDiscountEntity.getDiscountRate()) + "元");
//        tv_order_money.setText("¥" + autoCalculationDiscountEntity.getActualAmount() + "元");
        tv_order_money.setText(Html.fromHtml(String.format(STRING_PRICE2, autoCalculationDiscountEntity.getActualAmount())));
        LogUtils.e(String.format(STRING_PRICE2, autoCalculationDiscountEntity.getActualAmount()));
        coupons = new ArrayList<>();
        for (Integer id : autoCalculationDiscountEntity.getCons()) {
            CouponItemEntity couponItemEntity = new CouponItemEntity();
            couponItemEntity.setObjctId(id);
            coupons.add(couponItemEntity);
        }
    }

    @Override
    public void setIdentityInfo(OrderIdentityEntity orderIdentityEntity) {
        if (orderIdentityEntity != null) {
            authStatus = orderIdentityEntity.getState();
        } else {
            authStatus = Constants.IDENTITY_STATUS.NO_AUTH;
        }
    }

    @Override
    public void adUploadSuccess(ADEntity adEntity) {
        presenter.commitOrder(goPlaceAnOrderEntity, coupons, goPlaceAnOrderEntity.getPlanType(),
                goPlaceAnOrderEntity.getStartTime(), goPlaceAnOrderEntity.getEndTime(), adEntity);
    }

    @Override
    public void orderCommitSuccess(CommitOrderEntiy orderEntiy) {
        Bundle bundle = new Bundle();
//        if (orderEntiy.getPayMoney() > 0) {
//            bundle.putLong(CommonUtil.KEY_VALUE_1, orderEntiy.getOrderId());
//            MyActivityUtils.goActivity(this, OrderPayActivity.class, bundle);
//        } else {

//        }
//        EventBus.getDefault().post(new EventBusObjectEntity(EventBusConstants.AdPublishSuccess, null));
//        finish();

        LogUtils.e(orderEntiy.toString());
        if (orderEntiy.getPayMoney() > 0) {
            /*金额大于0跳支付*/
            PayDialog payDialog = new PayDialog(this, orderEntiy,orderEntiy.getPayMoney());
            payDialog.setCancelable(false);
            payDialog.show();

        } else {
            /*跳成功*/
            LogUtils.e("下单成功");
//            OrderDetailsEntity detailsEntity = new OrderDetailsEntity();
//            detailsEntity.setObjctId(orderEntiy.getOrderId());
//            detailsEntity.setCouponPrice(getAllMoney());
//            detailsEntity.setRealPrice(orderEntiy.getPayMoney());
//            detailsEntity.setPayTime(System.currentTimeMillis());
//            bundle.putParcelable(CommonUtil.KEY_VALUE_1, detailsEntity);
//            bundle.putString(CommonUtil.KEY_VALUE_2, Constants.PayType.FREE);
//            MyActivityUtils.goActivity(this, PaySuccessActivity.class, bundle);
            ActivityUtils.setActivity(ACTIVITY_PAY_SUCCESS,"orderId",orderEntiy.getOrderId(),
                    "payType",Constants.PayType.BALANCE_PAY,"priceActual", String.valueOf(orderEntiy.getPayMoney()));
            EventBus.getDefault().post(new EventBusObjectEntity(EventBusConstants.ReflashPlanList, null));
        }


    }

    private void setPayEnable() {
        if (delaySubscribe != null && !delaySubscribe.isUnsubscribed()) {
            delaySubscribe.unsubscribe();
        }
//        tvAliPay.setEnabled(true);
//        tvWxPay.setEnabled(true);
//        tvBalancePay.setEnabled(true);
    }

    @Override
    public void showManualSelectCouponsInfo(AutoCalculationDiscountEntity autoCalculationDiscountEntity) {
        this.autoCalculationDiscountEntity = autoCalculationDiscountEntity;
        cuxiao_manjian.setText(getLiJianPrice());
        String couponsText = "";
        if (autoCalculationDiscountEntity.getCons().size() == 0) {
            couponsText = "未选择优惠券";
        } else {
            couponsText = listToString(autoCalculationDiscountEntity.getConsCount(), ',');
        }
        realPayPrice = autoCalculationDiscountEntity.getActualAmount();
        zheKouPrice = autoCalculationDiscountEntity.getDiscountRate();
        couponsAllPrice = zheKouPrice + couponsPrice;
        tv_youhuiquan.setText(couponsText);
        tv_discount_money.setText("已优惠" + (autoCalculationDiscountEntity.getConcessionalRate() +
                autoCalculationDiscountEntity.getDiscountRate()) + "元");
//        tv_order_money.setText("¥" + autoCalculationDiscountEntity.getActualAmount() + "元");
        tv_order_money.setText(Html.fromHtml(String.format(STRING_PRICE2, autoCalculationDiscountEntity.getActualAmount())));

        LogUtils.e(String.format(STRING_PRICE2, autoCalculationDiscountEntity.getActualAmount()));
        coupons = new ArrayList<>();
        for (Integer id : autoCalculationDiscountEntity.getCons()) {
            CouponItemEntity couponItemEntity = new CouponItemEntity();
            couponItemEntity.setObjctId(id);
            coupons.add(couponItemEntity);
        }
        tv_go_pay.setEnabled(true);
    }

    private String getLiJianPrice() {
        double allMoney = 0;
        if (goPlaceAnOrderEntity.getPlanType().equals(Constants.PLAN_TYPE.DIY)) {
            for (int i = 0; i < goPlaceAnOrderEntity.getCellItemEntityArrayList().size(); i++) {
                CellItemEntity cellItemEntity = goPlaceAnOrderEntity.getCellItemEntityArrayList().get(i);
                allMoney += (cellItemEntity.getOriginalDiyAdPrice() - cellItemEntity.getDiyAdPrice()) * cellItemEntity.getChoosedScreenNum();
            }
            BigDecimal bg3 = new BigDecimal(allMoney);
            double f3 = bg3.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            if (f3 * goPlaceAnOrderEntity.getTotalDayCount()<=0){
                return"无促销活动";
            }else {
                return "立减" + f3 * goPlaceAnOrderEntity.getTotalDayCount() + "元";
            }
        } else if (goPlaceAnOrderEntity.getPlanType().equals(Constants.PLAN_TYPE.BUSINESS)) {
            for (int i = 0; i < goPlaceAnOrderEntity.getCellItemEntityArrayList().size(); i++) {
                CellItemEntity cellItemEntity = goPlaceAnOrderEntity.getCellItemEntityArrayList().get(i);
                allMoney += (cellItemEntity.getOriginalBusinessAdPrice() - cellItemEntity.getBusinessAdPrice()) * cellItemEntity.getChoosedScreenNum();
            }
            BigDecimal bg3 = new BigDecimal(allMoney);
            double f3 = bg3.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            if (f3 * goPlaceAnOrderEntity.getTotalDayCount()<=0){
                return"无促销活动";
            }else {
                return "立减" + f3 * goPlaceAnOrderEntity.getTotalDayCount() + "元";
            }
        } else {
            for (int i = 0; i < goPlaceAnOrderEntity.getCellItemEntityArrayList().size(); i++) {
                CellItemEntity cellItemEntity = goPlaceAnOrderEntity.getCellItemEntityArrayList().get(i);
                allMoney += (cellItemEntity.getOriginalInformationAdPrice() - cellItemEntity.getInformationAdPrice()) * cellItemEntity.getChoosedScreenNum();
            }
            BigDecimal bg3 = new BigDecimal(allMoney);
            double f3 = bg3.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            if (f3 * goPlaceAnOrderEntity.getTotalDayCount()<=0){
                return"无促销活动";
            }else {
                return "立减" + f3 * goPlaceAnOrderEntity.getTotalDayCount() + "元";
            }
//            return "便民信息无促销活动";
        }
    }

    private double getAllMoney() {
        float allMoney = 0;
        if (goPlaceAnOrderEntity.getPlanType().equals(Constants.PLAN_TYPE.DIY)) {
            for (int i = 0; i < goPlaceAnOrderEntity.getCellItemEntityArrayList().size(); i++) {
                CellItemEntity cellItemEntity = goPlaceAnOrderEntity.getCellItemEntityArrayList().get(i);
                allMoney += cellItemEntity.getDiyAdPrice() * cellItemEntity.getChoosedScreenNum();
            }
            BigDecimal bg3 = new BigDecimal(allMoney);
            double f3 = bg3.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            return f3 * goPlaceAnOrderEntity.getTotalDayCount();
        } else if (goPlaceAnOrderEntity.getPlanType().equals(Constants.PLAN_TYPE.BUSINESS)) {
            for (int i = 0; i < goPlaceAnOrderEntity.getCellItemEntityArrayList().size(); i++) {
                CellItemEntity cellItemEntity = goPlaceAnOrderEntity.getCellItemEntityArrayList().get(i);
                allMoney += cellItemEntity.getBusinessAdPrice() * cellItemEntity.getChoosedScreenNum();
                LogUtils.e(allMoney, cellItemEntity.getChoosedScreenNum(), cellItemEntity.getBusinessAdPrice(), cellItemEntity.getChoosedScreenNum() * cellItemEntity.getBusinessAdPrice());
            }
            BigDecimal bg3 = new BigDecimal(allMoney);
            double f3 = bg3.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            return f3 * goPlaceAnOrderEntity.getTotalDayCount();
        } else {
            for (int i = 0; i < goPlaceAnOrderEntity.getCellItemEntityArrayList().size(); i++) {
                CellItemEntity cellItemEntity = goPlaceAnOrderEntity.getCellItemEntityArrayList().get(i);
                allMoney += cellItemEntity.getInformationAdPrice() * cellItemEntity.getChoosedScreenNum();
                LogUtils.e(allMoney, cellItemEntity.getChoosedScreenNum(), cellItemEntity.getInformationAdPrice(), cellItemEntity.getChoosedScreenNum() * cellItemEntity.getInformationAdPrice());
            }
            BigDecimal bg3 = new BigDecimal(allMoney);
            double f3 = bg3.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            return f3 * goPlaceAnOrderEntity.getTotalDayCount();
//            return allMoney * goPlaceAnOrderEntity.getTotalDayCount();
        }
    }

    private void showOrderIdentityDialog() {
        if (identityDialog == null) {
            identityDialog = new OrderIdentityDialog(this);
            identityDialog.setOnIdentityClickListener(new OrderIdentityDialog.OnIdentityClickListener() {
                @Override
                public void onIdentityClick() {
                    jumpToIdentify();
                }
            });
        }
        identityDialog.show("根据法律规定，您当前还未成功进行认证，不能进行广告发布");
    }


    private void jumpToIdentify() {
        MyActivityUtils.goActivity(this, IdentityInfoActivity.class);
    }

    private String getPointCount() {
        int totalScreen = 0;
        for (int i = 0; i < goPlaceAnOrderEntity.getCellItemEntityArrayList().size(); i++) {
            CellItemEntity cellItemEntity = goPlaceAnOrderEntity.getCellItemEntityArrayList().get(i);
            totalScreen += cellItemEntity.getChoosedScreenNum();
        }
        return "共" + goPlaceAnOrderEntity.getCellItemEntityArrayList().size() + "个小区" + totalScreen + "面屏";
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && data != null) {
            switch (requestCode) {
                case CommonUtil.REQ_CODE_2:
                    coupons = data.getParcelableArrayListExtra(CommonUtil.KEY_VALUE_1);
                    couponsUpdate();
                    break;
            }
        }
    }

    private void couponsUpdate() {
        tv_go_pay.setEnabled(false);
        float couponsSum = 0;
        float totalPrice = (float) getAllMoney();
        if (coupons != null && coupons.size() > 0) {
            //tvCoupons.setText(String.format("已选%s张", coupons.size()));
            String choosedCouponType = coupons.get(0).getCoupontype();
            if (TextUtils.equals(Constants.CouponType.VOUCHER, choosedCouponType)) {
                for (CouponItemEntity item : coupons) {
                    couponsSum += item.getMoney();
                }
                couponsPrice = couponsSum;
            } else if (TextUtils.equals(Constants.CouponType.DISCOUNT, choosedCouponType)) {
                couponsSum = totalPrice * (1 - coupons.get(0).getDiscount() / 10);
                zheKouPrice = couponsSum;
                autoCalculationDiscountEntity.setHasMore(false);
                tv_youhuiquan.setText("折扣券 * 1");
            }
            float resultPrice = totalPrice - couponsSum;
            if (resultPrice > 0) {
                tv_discount_money.setText("已优惠" + couponsSum + "元");
//                tv_order_money.setText("¥" + resultPrice + "元");
                tv_order_money.setText(Html.fromHtml(String.format(STRING_PRICE2, resultPrice)));
                realPayPrice = resultPrice;
                couponsAllPrice = zheKouPrice + couponsPrice;
            } else {
                tv_discount_money.setText("已优惠" + totalPrice + "元");
//                tv_order_money.setText("¥ 0.0元");
                tv_order_money.setText(Html.fromHtml(String.format(STRING_PRICE2, "0.0")));
                realPayPrice = 0;
                couponsAllPrice = zheKouPrice + couponsPrice;
            }

        } else {
            //tvCoupons.setText("");
            tv_youhuiquan.setText("未选择优惠券");
            float resultPrice = totalPrice - couponsSum;
            tv_discount_money.setText("已优惠" + couponsSum + "元");
//            tv_order_money.setText("¥" + resultPrice + "元");
            tv_order_money.setText(Html.fromHtml(String.format(STRING_PRICE2, resultPrice)));
            if (autoCalculationDiscountEntity.getCons() != null && autoCalculationDiscountEntity.getCons().size() > 0) {
//            if (autoCalculationDiscountEntity.getCons().size() > 0) {
                autoCalculationDiscountEntity.setHasMore(true);
            } else {
                autoCalculationDiscountEntity.setHasMore(false);
            }
            couponsPrice = 0;
            zheKouPrice = 0;
            realPayPrice = resultPrice;
            couponsAllPrice = zheKouPrice + couponsPrice;
        }
        if (coupons == null) {
            coupons = new ArrayList<>();
        }
        presenter.loadCouponsNewInfo(getAllMoney(), goPlaceAnOrderEntity.getPlanType(), coupons);
    }
}
