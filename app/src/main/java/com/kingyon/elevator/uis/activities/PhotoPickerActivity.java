package com.kingyon.elevator.uis.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.customview.NoScrollViewPager;
import com.kingyon.elevator.mvpbase.MvpBaseActivity;
import com.kingyon.elevator.photopicker.MediaDirectory;
import com.kingyon.elevator.presenter.PhotoPickerPresenter;
import com.kingyon.elevator.uis.activities.user.MyAdActivity;
import com.kingyon.elevator.uis.adapters.adapterone.FolderListAdapter;
import com.kingyon.elevator.uis.adapters.adaptertwo.ChooseAdapter;
import com.kingyon.elevator.uis.fragments.PhotoPickerFragment;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.DialogUtils;
import com.kingyon.elevator.utils.MyActivityUtils;
import com.kingyon.elevator.utils.MyToastUtils;
import com.kingyon.elevator.utils.QuickClickUtils;
import com.kingyon.elevator.utils.RuntimeUtils;
import com.kingyon.elevator.view.PhotoPickerView;
import com.leo.afbaselibrary.utils.EasyPermissions;
import com.litao.android.lib.Utils.GridSpacingItemDecoration;
import com.litao.android.lib.entity.PhotoEntry;
import com.muzhi.camerasdk.MessageWrap;
import com.muzhi.camerasdk.model.CameraSdkParameterInfo;
import com.yalantis.ucrop.UCrop;
import com.zhaoss.weixinrecorded.util.EventBusConstants;
import com.zhaoss.weixinrecorded.util.EventBusObjectEntity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 图片视频选择界面
 */
public class PhotoPickerActivity extends MvpBaseActivity<PhotoPickerPresenter> implements PhotoPickerView, EasyPermissions.PermissionCallbacks {
    String[] permsPhoto = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    @BindView(R.id.back_close)
    ImageView back_close;
    @BindView(R.id.tv_action_title)
    TextView tv_action_title;
    @BindView(R.id.rule_desc)
    TextView rule_desc;
    @BindView(R.id.video_bottom_line)
    View video_bottom_line;
    @BindView(R.id.image_bottom_line)
    View image_bottom_line;
    @BindView(R.id.view_pager_container)
    NoScrollViewPager view_pager_container;
    @BindView(R.id.folder_list)
    ListView folder_list;
    FolderListAdapter folderListAdapter;
    private List<Fragment> fragmentList;
    protected static final int RC_PERM = 123;
    private int fromType = Constants.FROM_TYPE_TO_SELECT_MEDIA.PLAN;//来自于哪个界面
    private String planType = "";
    Drawable openFolderDrawable;
    Drawable closeFolderDrawable;
    public static PhotoPickerActivity photoPickerActivity;

    private CameraSdkParameterInfo mCameraSdkParameterInfo=new CameraSdkParameterInfo();
    ArrayList<String> listPath = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_picker);
        photoPickerActivity = this;
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        fromType = getIntent().getIntExtra("fromType", 1001);
        planType = getIntent().getStringExtra("planType");
        openFolderDrawable = getResources().getDrawable(R.mipmap.suoyoutupian_xiangshanbganniu);
        closeFolderDrawable = getResources().getDrawable(R.mipmap.suoyoushiping_xiangxiaanniu);
        openFolderDrawable.setBounds(0, 0, openFolderDrawable.getMinimumWidth(), openFolderDrawable.getMinimumHeight());
        closeFolderDrawable.setBounds(0, 0, openFolderDrawable.getMinimumWidth(), openFolderDrawable.getMinimumHeight());
        initView();
    }


    private void initView() {
        showProgressDialog(getString(R.string.wait),false);
        LogUtils.e(presenter.getDirectories());
        folderListAdapter = new FolderListAdapter(this, fromType, presenter.getDirectories());
        folder_list.setAdapter(folderListAdapter);
        folder_list.setOnItemClickListener((parent, view, position, id) -> {
            if (QuickClickUtils.isFastClick()) {
                if (fromType == Constants.FROM_TYPE_TO_SELECT_MEDIA.PLAN) {
                    if (position == 0) {
                        /*我的广告*/
                        jumpToAdvertising();
                    } else {
                        /*相册详情*/
                        folder_list.setVisibility(View.GONE);
                        setItemClick(position);
                    }
                } else {
                    folder_list.setVisibility(View.GONE);
                    setItemClick(position);
                }
            }
        });
        view_pager_container.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    video_bottom_line.setVisibility(View.VISIBLE);
                    image_bottom_line.setVisibility(View.GONE);
                } else {
                    video_bottom_line.setVisibility(View.GONE);
                    image_bottom_line.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        fragmentList = new ArrayList<>();
        if (fromType == Constants.FROM_TYPE_TO_SELECT_MEDIA.PLAN) {
            presenter.loadAdInfo(planType);
        }
        if (EasyPermissions.hasPermissions(this, permsPhoto)) {
            //有权限
            presenter.loadPhotoFolderList();
        } else {
            EasyPermissions.requestPermissions(this, "读取系统相册需要以下权限",
                    RC_PERM, permsPhoto);
        }
        if (folder_list.getVisibility() == View.VISIBLE) {
            tv_action_title.setCompoundDrawables(null, null, openFolderDrawable, null);
        } else {
            tv_action_title.setCompoundDrawables(null, null, closeFolderDrawable, null);
        }
    }

    private void setItemClick(int position) {
        LogUtils.e(RuntimeUtils.currentMediaDirectory.getId(),
                RuntimeUtils.currentMediaDirectory,
                presenter.getDirectories().get(position),
                presenter.getDirectories().get(position).getId());

        if (presenter.getDirectories().get(position) != null) {
            if (RuntimeUtils.currentMediaDirectory != null) {
                if (!RuntimeUtils.currentMediaDirectory.getId().equals(presenter.getDirectories().get(position).getId())) {
                    RuntimeUtils.currentMediaDirectory = presenter.getDirectories().get(position);
                    LogUtils.e(RuntimeUtils.currentMediaDirectory.getId(),  RuntimeUtils.currentMediaDirectory,presenter.getDirectories().get(position));
                    tv_action_title.setText(RuntimeUtils.currentMediaDirectory.getName());
                    ((PhotoPickerFragment) fragmentList.get(0)).changeData(RuntimeUtils.currentMediaDirectory);
                    ((PhotoPickerFragment) fragmentList.get(1)).changeData(RuntimeUtils.currentMediaDirectory);
                } else {
                    LogUtils.d("选择了相同的相册，不刷新数据-----------------");
                }
            } else {
                RuntimeUtils.currentMediaDirectory = presenter.getDirectories().get(position);
                tv_action_title.setText(RuntimeUtils.currentMediaDirectory.getName());
                ((PhotoPickerFragment) fragmentList.get(0)).changeData(RuntimeUtils.currentMediaDirectory);
                ((PhotoPickerFragment) fragmentList.get(1)).changeData(RuntimeUtils.currentMediaDirectory);
            }
        }
    }

    @Override
    public PhotoPickerPresenter initPresenter() {
        return new PhotoPickerPresenter(this);
    }


    private void jumpToAdvertising() {
        Intent intent = new Intent(this, MyAdActivity.class);
        Bundle bundle = new Bundle();
        switch (RuntimeUtils.goPlaceAnOrderEntity.getPlanType()) {
            case Constants.PLAN_TYPE.BUSINESS:
            case Constants.PLAN_TYPE.DIY:
                bundle.putBoolean(CommonUtil.KEY_VALUE_1, true);
                bundle.putString(CommonUtil.KEY_VALUE_2, RuntimeUtils.goPlaceAnOrderEntity.getPlanType());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void adPublishSuccess(EventBusObjectEntity eventBusObjectEntity) {
        if (eventBusObjectEntity.getEventCode() == EventBusConstants.AdPublishSuccess
                || eventBusObjectEntity.getEventCode() == EventBusConstants.VideoOrImageSelectSuccess
                || eventBusObjectEntity.getEventCode() == EventBusConstants.VideoCropSuccessResult) {
            finish();
        }
    }


    @OnClick({R.id.back_close, R.id.rule_desc, R.id.tv_action_title, R.id.video_container, R.id.image_container})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.back_close:
                finish();
                break;
            case R.id.rule_desc:
                DialogUtils.getInstance().showRuleDescTipsDialog(this,"规格说明","屏幕尺寸：","视频/图片——768(宽)  X  1220(高)");
                break;
            case R.id.tv_action_title:
                if (folder_list.getVisibility() == View.GONE) {
                    folder_list.setVisibility(View.VISIBLE);
                    tv_action_title.setCompoundDrawables(null, null, openFolderDrawable, null);
                } else {
                    folder_list.setVisibility(View.GONE);
                    tv_action_title.setCompoundDrawables(null, null, closeFolderDrawable, null);
                }
                break;
            case R.id.video_container:
                video_bottom_line.setVisibility(View.VISIBLE);
                image_bottom_line.setVisibility(View.GONE);
                view_pager_container.setCurrentItem(0, false);
                break;
            case R.id.image_container:
                video_bottom_line.setVisibility(View.GONE);
                image_bottom_line.setVisibility(View.VISIBLE);
                view_pager_container.setCurrentItem(1, false);
                break;
        }
    }

    @Override
    public void loadFolderListSuccess(List<MediaDirectory> mResultFolder) {
        LogUtils.e("多媒体数据获取成功");
        runOnUiThread(() -> {
            folderListAdapter.reflashData(mResultFolder);
            hideProgressDailog();
            initFragment();
        });
    }

    @Override
    public void loadFolderListFailed() {
        runOnUiThread(() -> {
            hideProgressDailog();
            showShortToast("多媒体数据获取失败，请重试");
            finish();
        });
    }

    @Override
    public void loadAdInfoSuccess(String coverPath, String count) {
        if (folderListAdapter != null) {
            folderListAdapter.updateMyAdInfo(coverPath, count);
        }
    }

    @Override
    public void loadAdInfoFailed() {
        if (folderListAdapter != null) {
            folderListAdapter.updateMyAdInfo("", "0");
        }
    }

    private void initFragment() {
        if (fromType == Constants.FROM_TYPE_TO_SELECT_MEDIA.PLAN) {
            if (presenter.getDirectories().size() > 2) {
                RuntimeUtils.currentMediaDirectory = presenter.getDirectories().get(1);
            } else {
                RuntimeUtils.currentMediaDirectory = presenter.getDirectories().get(0);
            }
        } else {
            RuntimeUtils.currentMediaDirectory = presenter.getDirectories().get(0);
        }
        fragmentList.add(PhotoPickerFragment.newInstance(1, fromType, planType));
        fragmentList.add(PhotoPickerFragment.newInstance(2, fromType, planType));
        view_pager_container.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
    }

    /**
     * 用户权限处理,
     * 如果全部获取, 则直接过.
     * 如果权限缺失, 则提示Dialog.
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        LogUtils.d("权限允许onPermissionsGranted：", GsonUtils.toJson(perms));
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.checkDeniedPermissionsNeverAskAgain(this,
                "当前应用缺少必要权限,请手动开启存储和相机权限。\n请点击\"设置\"-\"权限\"-打开所需权限。",
                R.string.setting, R.string.cancel, null, perms)) {
        } else {
            MyToastUtils.showShort("缺少必要权限，无法读取相册，请重新授权");
            EasyPermissions.requestPermissions(this, "读取系统相册需要以下权限",
                    1000 + new Random().nextInt(100), permsPhoto);
        }
    }


    @Override
    public void onPermissionsAllGranted() {
        presenter.loadPhotoFolderList();
    }

    class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> mfragmentList;

        public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.mfragmentList = fragmentList;
        }

        //获取集合中的某个项
        @Override
        public Fragment getItem(int position) {
            return mfragmentList.get(position);
        }

        //返回绘制项的数目
        @Override
        public int getCount() {
            return mfragmentList.size();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == UCrop.REQUEST_CROP) {
                handleCropResult(data);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        DialogUtils.getInstance().hideRuleDescTipsDialog();
    }

    private void handleCropResult(Intent result) {
        Uri resultUri = UCrop.getOutput(result);
            LogUtils.d("裁剪成功的图片链接：", resultUri.getPath(),
                    Constants.FROM_TYPE.MEDIADATA,Constants.Materia_Type.IMAGE);
            listPath.clear();
            listPath.add(resultUri.getPath());
            mCameraSdkParameterInfo.setFilter_image(true);
            mCameraSdkParameterInfo.setSingle_mode(false);
            mCameraSdkParameterInfo.setShow_camera(false);
            mCameraSdkParameterInfo.setMax_image(6);
            mCameraSdkParameterInfo.setCroper_image(false);
            mCameraSdkParameterInfo.setImage_list(listPath);
            Intent intent = new Intent();
            intent.setClassName(getApplication(), "com.muzhi.camerasdk.FilterImageActivity");
            Bundle b=new Bundle();
            b.putSerializable(CameraSdkParameterInfo.EXTRA_PARAMETER, mCameraSdkParameterInfo);
            intent.putExtras(b);
            startActivityForResult(intent, CameraSdkParameterInfo.TAKE_PICTURE_PREVIEW);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(MessageWrap message) {
        LogUtils.e(message.message.size(),message.message.get(0),message.message.toString());
//        pic_list.clear();
        if (fromType == Constants.FROM_TYPE_TO_SELECT_MEDIA.PLAN) {

            MyActivityUtils.goConfirmOrderActivity(this, Constants.FROM_TYPE.MEDIADATA, message.message.get(0), Constants.Materia_Type.IMAGE);

        }else if (fromType == Constants.FROM_TYPE_TO_SELECT_MEDIA.MYADSELECT){

            EventBus.getDefault().post(new EventBusObjectEntity(EventBusConstants.VideoOrImageSelectSuccess, message.message.get(0)));
        }
        finish();
    }

}
