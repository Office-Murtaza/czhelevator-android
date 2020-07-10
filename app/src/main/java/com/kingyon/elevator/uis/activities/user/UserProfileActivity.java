package com.kingyon.elevator.uis.activities.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.LocationEntity;
import com.kingyon.elevator.entities.UserEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.nets.NetUpload;
import com.kingyon.elevator.uis.actiivty2.activityutils.CameraViewActivit;
import com.kingyon.elevator.uis.actiivty2.user.CoverActivity;
import com.kingyon.elevator.uis.activities.homepage.CityActivity;
import com.kingyon.elevator.uis.dialogs.PictureSelectorDialog;
import com.kingyon.elevator.uis.dialogs.SexDialogs;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.PictureSelectorUtil;
import com.kingyon.elevator.utils.utilstwo.CityUtils;
import com.kingyon.elevator.utils.utilstwo.ConentUtils;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingActivity;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.leo.afbaselibrary.utils.TimeUtil;
import com.leo.afbaselibrary.widgets.StateLayout;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelector;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_USER_INTRODUCTION;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_USER_NICK;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_USER_REGION;

/**
 * Created by GongLi on 2019/1/9.
 * Email：lc824767150@163.com
 */

public class UserProfileActivity extends BaseStateRefreshingActivity {

    @BindView(R.id.img_head)
    ImageView imgHead;
    @BindView(R.id.tv_nick)
    TextView tvNick;
    @BindView(R.id.tv_pwd)
    TextView tvPwd;
    @BindView(R.id.tv_mobile)
    TextView tvMobile;

    private final int headCode = 1;
    private final int nickCode = 3;
    @BindView(R.id.pre_v_back)
    ImageView preVBack;
    @BindView(R.id.pre_tv_title)
    TextView preTvTitle;
    @BindView(R.id.head_root)
    RelativeLayout headRoot;
    @BindView(R.id.ll_head)
    LinearLayout llHead;
    @BindView(R.id.ll_nick)
    LinearLayout llNick;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.ll_sex)
    LinearLayout llSex;
    @BindView(R.id.tv_region)
    TextView tvRegion;
    @BindView(R.id.ll_region)
    LinearLayout llRegion;
    @BindView(R.id.tv_birthday)
    TextView tvBirthday;
    @BindView(R.id.ll_birthday)
    LinearLayout llBirthday;
    @BindView(R.id.tv_introduction)
    TextView tvIntroduction;
    @BindView(R.id.ll_introduction)
    LinearLayout llIntroduction;
    @BindView(R.id.ll_pwd)
    LinearLayout llPwd;
    @BindView(R.id.ll_mobile)
    LinearLayout llMobile;
    @BindView(R.id.pre_refresh)
    SwipeRefreshLayout preRefresh;
    @BindView(R.id.stateLayout)
    StateLayout stateLayout;
    String birthday;
    UserEntity user;
    @Override
    protected String getTitleText() {
        return "个人资料";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_user_profile;
    }

    @Override
    public void onRefresh() {
        NetService.getInstance().userProfile()
                .compose(this.<UserEntity>bindLifeCycle())
                .subscribe(new CustomApiCallback<UserEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(STATE_ERROR);
                    }

                    @Override
                    public void onNext(UserEntity userEntity) {
                        if (userEntity == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        DataSharedPreferences.saveUserBean(userEntity);
                        loadingComplete(STATE_CONTENT);
                        showUserInfo(userEntity);
                        user = userEntity;
                    }
                });
    }

    private void showUserInfo(UserEntity userEntity) {
        GlideUtils.loadAvatarImage(this, userEntity.getAvatar(), imgHead);
        tvNick.setText(userEntity.getNikeName() != null ? userEntity.getNikeName() : "");
        tvPwd.setText("******");
        tvMobile.setText(CommonUtil.getHideMobile(userEntity.getPhone()));
        tvIntroduction.setText(userEntity.getPersonalizedSignature());
        if (userEntity.getBirthday()<=0){
            tvBirthday.setText("未选择");
        }else {
            tvBirthday.setText(TimeUtil.getYMdTime(userEntity.getBirthday()));
        }
        CityUtils.getCityStr(this, userEntity.getCity(), new CityUtils.CityStr() {
            @Override
            public void cityCode(String cityCode) {
                tvRegion.setText(cityCode);
            }
        });
        if (userEntity.getSex().equals("M")){
            tvSex.setText("男");
        }else {
            tvSex.setText("女");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != RESULT_CANCELED||resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case headCode:
                    ArrayList<String> mSelectPath2 = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                    if (mSelectPath2 != null && mSelectPath2.size() > 0) {
                        showProgressDialog(getString(R.string.wait));
                        NetService.getInstance().uploadFile(this, new File(mSelectPath2.get(0)), new NetUpload.OnUploadCompletedListener() {
                            @Override
                            public void uploadSuccess(List<String> images, List<String> hash, JSONObject response) {
                                LogUtils.e(images, hash, response);
                                if (images != null && images.size() > 0) {
                                    ConentUtils.httpEidtProfile(UserProfileActivity.this, images.get(0),
                                            "", "", "", "", "", "", new ConentUtils.AddCollect() {
                                                @Override
                                                public void Collect(boolean is) {
                                                    if (is) {
                                                        onRefresh();
                                                    }
                                                }
                                            });
                                } else {
                                    showToast("上传头像出错");
                                    hideProgress();
                                }
                            }

                            @Override
                            public void uploadFailed(ApiException ex) {
                                showToast(ex.getDisplayMessage());
                                hideProgress();
                            }
                        });
                    }
                    break;
                case 8001:
                    LocationEntity choosed = data.getParcelableExtra(CommonUtil.KEY_VALUE_1);
                    LogUtils.e(choosed.getLatitude(), choosed.getLongitude(),
                            choosed.getCity(), choosed.describeContents(),
                            choosed.getCityCode(), choosed.getName());

                    if (choosed != null) {
                        CityUtils.getCityCode(UserProfileActivity.this, choosed.getCity(), new CityUtils.CityCode() {
                            @Override
                            public void cityCode(int cityCode1) {
                                LogUtils.e(cityCode1);
                                ConentUtils.httpEidtProfile(UserProfileActivity.this, "",
                                        "","" , String.valueOf(cityCode1), "", "", "", new ConentUtils.AddCollect() {
                                            @Override
                                            public void Collect(boolean is) {
                                                if (is) {
                                                    onRefresh();
                                                }
                                            }
                                        });
                            }
                        });
                        tvRegion.setText(choosed.getCity() + "");
                    }
                    break;
                case 101:
                    try {
                        String path =data.getStringExtra("path");
                        LogUtils.e(path);
                        showProgressDialog(getString(R.string.wait));
                        NetService.getInstance().uploadFile(this, new File(path), new NetUpload.OnUploadCompletedListener() {
                            @Override
                            public void uploadSuccess(List<String> images,List<String> hash,JSONObject response) {
                                hideProgress();
                                if (images != null && images.size() > 0) {
                                    ConentUtils.httpEidtProfile(UserProfileActivity.this, images.get(0),
                                            "", "", "", "", "","", new ConentUtils.AddCollect() {
                                                @Override
                                                public void Collect(boolean is) {
                                                    if (is) {
                                                        finish();
                                                    }
                                                }
                                            });

                                } else {
                                    hideProgress();
                                    showToast("上传失败");
                                }
                            }
                            @Override
                            public void uploadFailed(ApiException ex) {
                                hideProgress();
                                showToast("上传失败");
                            }
                        }, false);
                    }catch (Exception e){
                        LogUtils.e(e.toString());
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    @OnClick({R.id.ll_head, R.id.ll_nick, R.id.ll_sex, R.id.ll_region, R.id.ll_birthday, R.id.ll_introduction})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_head:
                /*头像*/
                PictureSelectorDialog pictureSelectorDialog = new PictureSelectorDialog(this);
                pictureSelectorDialog.show();
                pictureSelectorDialog.setOnClicked(new PictureSelectorDialog.OnClicked() {
                    @Override
                    public void onPhotoalbum() {
                        PictureSelectorUtil.showPictureSelector(UserProfileActivity.this, headCode);
                    }

                    @Override
                    public void onPicture() {
                        startActivityForResult(CameraViewActivit.class, 101);
                    }
                });

                break;
            case R.id.ll_nick:
                /*昵称*/
                ActivityUtils.setActivity(ACTIVITY_USER_NICK,"nickName",user.getNikeName());
                break;
            case R.id.ll_sex:
                /*性别*/
                SexDialogs sexDialogs = new SexDialogs(this);
                sexDialogs.show();
                sexDialogs.setOnClicked(new SexDialogs.OnClicked() {
                    @Override
                    public void onClicked(String str) {
                        tvSex.setText(str);
                        if (str.equals("男")){
                            ConentUtils.httpEidtProfile(UserProfileActivity.this, "",
                                    "", "M", "", "", "", "", new ConentUtils.AddCollect() {
                                        @Override
                                        public void Collect(boolean is) {
                                            if (is) {
                                               onRefresh();
                                            }
                                        }
                                    });
                        }else {
                            ConentUtils.httpEidtProfile(UserProfileActivity.this, "",
                                    "", "F", "", "", "", "", new ConentUtils.AddCollect() {
                                        @Override
                                        public void Collect(boolean is) {
                                            if (is) {
                                                onRefresh();
                                            }
                                        }
                                    });
                        }

                    }
                });
                break;
            case R.id.ll_region:
                /*地区*/
                startActivityForResult(CityActivity.class, 8001);
                break;
            case R.id.ll_birthday:
                /*生日*/
                initTimePicker1();
                break;
            case R.id.ll_introduction:
                /*简介*/
                ActivityUtils.setActivity(ACTIVITY_USER_INTRODUCTION,"conent",user.getPersonalizedSignature());
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
                LogUtils.e(getTime(date),date);
//                tvBirthday.setText(getTime(date));
                birthday = String.valueOf(TimeUtil.ymdToLong(getTime(date)));
                ConentUtils.httpEidtProfile(UserProfileActivity.this, "",
                        "", "", "", getTime(date), "", "", new ConentUtils.AddCollect() {
                            @Override
                            public void Collect(boolean is) {
                                if (is) {
                                    onRefresh();
                                }
                            }
                        });
            }
        }).setType(new boolean[]{true, true, true, false, false, false}) //年月日时分秒 的显示与否，不设置则默认全部显示
                .setLabel("年", "月", "日", "", "", "")//默认设置为年月日时分秒
                .isCenterLabel(false)
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
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
}
