package com.kingyon.elevator.uis.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.customview.NoScrollViewPager;
import com.kingyon.elevator.entities.ADEntity;
import com.kingyon.elevator.entities.FolderEntity;
import com.kingyon.elevator.mvpbase.MvpBaseActivity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.photopicker.MediaDirectory;
import com.kingyon.elevator.presenter.PhotoPickerPresenter;
import com.kingyon.elevator.uis.activities.user.MyAdActivity;
import com.kingyon.elevator.uis.adapters.FolderListAdapter;
import com.kingyon.elevator.uis.fragments.PhotoPickerFragment;
import com.kingyon.elevator.utils.DialogUtils;
import com.kingyon.elevator.utils.MyActivityUtils;
import com.kingyon.elevator.utils.MyStatusBarUtils;
import com.kingyon.elevator.utils.RuntimeUtils;
import com.kingyon.elevator.view.PhotoPickerView;
import com.leo.afbaselibrary.nets.entities.PageListEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiFilterType;
import me.nereo.multi_image_selector.bean.Folder;
import me.nereo.multi_image_selector.bean.Image;

/**
 * 图片视频选择界面
 */
public class PhotoPickerActivity extends MvpBaseActivity<PhotoPickerPresenter> implements PhotoPickerView {

    @BindView(R.id.back_close)
    ImageView back_close;
    @BindView(R.id.tv_action_title)
    TextView tv_action_title;
    @BindView(R.id.selected_status)
    ImageView selected_status;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_picker);
        ButterKnife.bind(this);
        folderListAdapter = new FolderListAdapter(this, presenter.getDirectories());
        folder_list.setAdapter(folderListAdapter);
        folder_list.setOnItemClickListener((parent, view, position, id) -> {
            folder_list.setVisibility(View.GONE);
            if (position == 0) {
                MyActivityUtils.goActivity(this, MyAdActivity.class);
            } else {
                if (presenter.getDirectories().get(position) != null) {
                    if (RuntimeUtils.currentMediaDirectory != null) {
                        if (!RuntimeUtils.currentMediaDirectory.getId().equals(presenter.getDirectories().get(position).getId())) {
                            RuntimeUtils.currentMediaDirectory = presenter.getDirectories().get(position);
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
        presenter.loadPhotoFolderList();
        presenter.loadAdInfo();
    }

    @Override
    public PhotoPickerPresenter initPresenter() {
        return new PhotoPickerPresenter(this);
    }


    @OnClick({R.id.back_close, R.id.rule_desc, R.id.tv_action_title, R.id.video_container, R.id.image_container})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.back_close:
                finish();
                break;
            case R.id.rule_desc:
                DialogUtils.getInstance().showRuleDescTipsDialog(this);
                break;
            case R.id.tv_action_title:
                if (folder_list.getVisibility() == View.GONE) {
                    folder_list.setVisibility(View.VISIBLE);
                } else {
                    folder_list.setVisibility(View.GONE);
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
        if (presenter.getDirectories().size()>2) {
            RuntimeUtils.currentMediaDirectory = presenter.getDirectories().get(1);
        }else {
            RuntimeUtils.currentMediaDirectory = presenter.getDirectories().get(0);
        }
        fragmentList.add(PhotoPickerFragment.newInstance(1));
        fragmentList.add(PhotoPickerFragment.newInstance(2));
        view_pager_container.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
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
    protected void onDestroy() {
        super.onDestroy();
        DialogUtils.getInstance().hideRuleDescTipsDialog();
    }

    private void handleCropResult(Intent result) {
        Uri resultUri = UCrop.getOutput(result);
        LogUtils.e("裁剪成功的图片链接：", resultUri.getPath());
    }
}
