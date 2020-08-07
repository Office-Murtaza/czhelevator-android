package com.kingyon.elevator.uis.actiivty2.activityutils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.uis.adapters.adaptertwo.CustomFragmentPagerAdapter;
import com.kingyon.elevator.utils.StatusBarUtil;
import com.kingyon.elevator.utils.utilstwo.FileUtils;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.IMAGER_EDITOR_ACTIVITY;

/**
 * @Created By Admin  on 2020/4/30
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:图片编辑
 */
@Route(path = IMAGER_EDITOR_ACTIVITY)
public class ImagerEditorActivity extends BaseActivity {

    @Autowired
    ArrayList<String> listPath;
    @Autowired
    int position;
    @BindView(R.id.viewpagertab)
    SmartTabLayout viewpagertab;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.tv_ll)
    TextView tvLl;
    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_top_root)
    RelativeLayout rlTopRoot;
    CustomFragmentPagerAdapter adapter;
    public  static  List<Bitmap> bitmapList = new ArrayList<>();

    private int position1 = 0;
//    private SaveImageTask mSaveImageTask;
    public String saveFilePath = FileUtils.genEditFile().getAbsolutePath();// 生成的新图片路径

    @Override
    public int getContentViewId() {
        return R.layout.activity_image_editor;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        StatusBarUtil.setHeadViewPadding(this, tvLl);
        StatusBarUtil.setTransparent(this, false);
        position1 = position;
        tvTopTitle.setText((position + 1) + "/" + listPath.size());
        initAdapter();
//        new ImagerEditorFragment().getBitmap(new GetImageBitme() {
//            @Override
//            public void getBitme(List<Bitmap> bitmapList) {
//                LogUtils.e(bitmapList);
//            }
//        });
    }


    private void initAdapter() {
        adapter = new CustomFragmentPagerAdapter(getSupportFragmentManager());
        LogUtils.e(listPath.toString());
        for (int i = 0; i < listPath.size(); i++) {
            adapter.addFrag(new ImagerEditorFragment().setIndex(this, listPath.get(i)), "全部");
        }
        adapter.notifyDataSetChanged();
        vp.setAdapter(adapter);
        vp.setOffscreenPageLimit(adapter.getCount());
        viewpagertab.setViewPager(vp);
        vp.setCurrentItem(position1);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                tvTopTitle.setText((position + 1) + "/" + listPath.size());
                position1 = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }



    @OnClick({R.id.img_top_back, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_top_back:
                finish();
                break;
            case R.id.tv_right:
//                if (mOpTimes <= 0)
//                    return;

//                ImagerEditorFragment imagerEditorFragment = new ImagerEditorFragment();
//
//                LogUtils.e("==========="+imagerEditorFragment.getMainBit(),
//                        bitmapList,
//                        bitmapList.size(),
//                        bitmapList.toString(),
//                        viewTouch,viewConversionBitmap(viewTouch),ImagerEditorFragment.currentBitmap);
//
//                imagerEditorFragment.getBitmap(new GetImageBitme() {
//                    @Override
//                    public void getBitme(List<Bitmap> bitmapList) {
//                        LogUtils.e(bitmapList);
//                        for (int i= 0;i<bitmapList.size();i++){
//                            LogUtils.e(bitmapList.get(i));
//                            if (mSaveImageTask != null) {
//                                mSaveImageTask.cancel(true);
//                            }
//                            mSaveImageTask = new SaveImageTask();
//                            mSaveImageTask.execute(bitmapList.get(i));
//                        }
//                    }
//                });
                break;
        }
    }

    /**
     * 保存图像
     * 完成后退出
     */
//    private final class SaveImageTask extends AsyncTask<Bitmap, Void, Boolean> {
//        private Dialog dialog;
//
//        @Override
//        protected Boolean doInBackground(Bitmap... params) {
//            if (TextUtils.isEmpty(saveFilePath))
//                return false;
//
//            return BitmapUtils.saveBitmap(params[0],saveFilePath );
//        }
//
//        @Override
//        protected void onCancelled() {
//            super.onCancelled();
//            dialog.dismiss();
//        }
//
//        @Override
//        protected void onCancelled(Boolean result) {
//            super.onCancelled(result);
//            dialog.dismiss();
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            dialog = EditImageActivity.getLoadingDialog(ImagerEditorActivity.this, com.xinlan.imageeditlibrary.R.string.saving_image, false);
//            dialog.show();
//        }
//
//        @Override
//        protected void onPostExecute(Boolean result) {
//            super.onPostExecute(result);
//            dialog.dismiss();
//
//            if (result) {
//                onSaveTaskDone();
//            } else {
//                Toast.makeText(ImagerEditorActivity.this, com.xinlan.imageeditlibrary.R.string.save_error, Toast.LENGTH_SHORT).show();
//            }
//        }
//    }//end inner class
//
//    protected void onSaveTaskDone() {
////        Intent returnIntent = new Intent();
////        returnIntent.putExtra(FILE_PATH, filePath);
////        returnIntent.putExtra(EXTRA_OUTPUT, saveFilePath);
////        returnIntent.putExtra(IMAGE_IS_EDIT, mOpTimes > 0);
//
//        FileUtil.ablumUpdate(this, saveFilePath);
//        LogUtils.e(saveFilePath);
//
////        setResult(RESULT_OK, returnIntent);
////        finish();
//    }

    public Bitmap viewConversionBitmap(View v) {
        int w = v.getWidth();
        int h = v.getHeight();


        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        Resources res = getResources();
        Bitmap bmp1 = BitmapFactory.decodeResource(res, R.mipmap.bj);
        //        c.drawColor(Color.WHITE);
        /** 如果不设置canvas画布为白色，则生成透明 */
            LogUtils.e(bmp1);
        //以其中一张图片的大小作为画布的大小，或者也可以自己自定义
        Bitmap bitmap = Bitmap.createBitmap(bmp.getWidth(), bmp
                .getHeight(), bmp1.getConfig());
        //生成画布
        Canvas canvas = new Canvas();
        //因为我传入的secondBitmap的大小是不固定的，所以我要将传来的secondBitmap调整到和画布一样的大小
        Matrix m = new Matrix();
        //确定secondBitmap大小比例
        m.setScale(w / bmp1.getWidth(), h / bmp1.getHeight());
        Paint paint = new Paint();
        //给画笔设定透明值，想将哪个图片进行透明化，就将画笔用到那张图片上
        paint.setAlpha(150);
        canvas.drawBitmap(bmp1, 0, 0, null);
        canvas.drawBitmap(bmp, m, null);
        v.layout(0, 0, w, h);
        v.draw(canvas);
        return bitmap;
    }
}

