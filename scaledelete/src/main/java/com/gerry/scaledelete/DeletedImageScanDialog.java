package com.gerry.scaledelete;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.gerry.scaledelete.viewpager.ViewPagerIndicator;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.leo.afbaselibrary.utils.glide.GlideApp;
import com.leo.afbaselibrary.utils.glide.VideoFrameBitmapTransformation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.load.resource.bitmap.VideoDecoder.FRAME_OPTION;

/**
 * Created by arvin on 2015/8/18
 * .
 */
public class DeletedImageScanDialog extends Dialog implements ViewPager.OnPageChangeListener, ExtendedViewPager.OnTitleShowListener {

    protected Context mContext;

    protected LinearLayout llindicator;
    protected LinearLayout root;
    protected LinearLayout head;
    protected ImageView back;
    protected ImageView deleted;
    protected TextView indicator;
    protected TouchImageAdapter mAdaptar;
    protected ExtendedViewPager mViewPager;
    protected List<ScanleImageUrl> scanImageUrls;
    protected int position = 0;
    protected OnScanleDeletedListener onScanleDeletedListener;
    protected AlertDialog removePhotoDialog;
    private boolean deletedable;
    private ValueAnimator valueAnimator;
    private boolean showTitle = true;

    public DeletedImageScanDialog(Context context, List<? extends ScanleImageUrl> scanImageUrls, int position, OnScanleDeletedListener onScanleDeletedListener) {
        super(context, R.style.normal_dialog_white);
        setContentView(R.layout.deleted_image_scan_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        initData(context, scanImageUrls);
        this.position = position;
        this.onScanleDeletedListener = onScanleDeletedListener;
    }

    public DeletedImageScanDialog(Context context, List<ScanleImageUrl> scanImageUrls, int position) {
        super(context, R.style.normal_dialog_white);
        setContentView(R.layout.deleted_image_scan_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        initData(context, scanImageUrls);
        this.position = position;
    }

    public DeletedImageScanDialog(Context context, ScanleImageUrl scanImage, OnScanleDeletedListener onScanleDeletedListener) {
        super(context, R.style.normal_dialog_white);
        setContentView(R.layout.deleted_image_scan_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        List<ScanleImageUrl> scanImageUrls = new ArrayList<>();
        scanImageUrls.add(scanImage);
        initData(context, scanImageUrls);
        this.onScanleDeletedListener = onScanleDeletedListener;
    }

    public DeletedImageScanDialog(Context context) {
        super(context, R.style.normal_dialog_white);
        setContentView(R.layout.deleted_image_scan_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        List<ScanleImageUrl> scanImages = new ArrayList<>();
        initData(context, scanImages);
    }

    public void show(int position) {
        if (mViewPager != null) {
            mViewPager.setCurrentItem(position, false);
        }
        if (!isShowing()) {
            show();
            getWindow().setWindowAnimations(R.style.BL_PopupAnimation);
        }
    }

    public void show(int position, boolean deletedable) {
        show(position, deletedable, true);
    }

    public void show(int position, boolean deletedable, boolean showTitle) {
        if (mViewPager != null) {
            mViewPager.setCurrentItem(position, false);
        }
        this.deletedable = deletedable;
        this.showTitle = showTitle;
        if (!isShowing()) {
            show();
            getWindow().setWindowAnimations(R.style.BL_PopupAnimation);
            if (mAdaptar != null) {
                mViewPager.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdaptar.notifyDataSetChanged();
                    }
                }, 200);
            }
        }
    }

    public void showNoIndicator(int position, boolean deletedable, boolean showTitle) {
        show(position, deletedable, showTitle);
        indicator.setVisibility(View.GONE);
    }

    public void showOne() {
        show(0, false, false);
//        if (mViewPager != null) {
//            mViewPager.setCurrentItem(0, false);
//        }
//        this.deletedable = false;
//        if (!isShowing()) {
//            show();
//            getWindow().setWindowAnimations(R.style.BL_PopupAnimation);
//        }
        if (llindicator != null) {
            llindicator.setVisibility(View.GONE);
        }
    }

    private void initData(Context context, List<? extends ScanleImageUrl> scanImages) {
        this.mContext = context;
        this.scanImageUrls = new ArrayList<>();
        this.scanImageUrls.addAll(scanImages);
    }

//    public void refreshShow(List<String> imgUrls, int position) {
//        this.scanImageUrls.clear();
//        this.scanImageUrls.addAll(imgUrls);
//
//        mAdaptar.notifyDataSetChanged();
//        show();
//    }

    public void setData(List<ScanleImageUrl> scanImages) {
        if (scanImages == null || scanImages.size() < 1) {
            return;
        }
        this.scanImageUrls.clear();
        this.scanImageUrls.addAll(scanImages);
        mAdaptar.notifyDataSetChanged();
    }

    public void setData(ScanleImageUrl scanImage) {
        List<ScanleImageUrl> scanImages = new ArrayList<>();
        scanImages.add(scanImage);
        setData(scanImages);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        llindicator = findViewById(R.id.indicator);
        head = findViewById(R.id.head);
        root = findViewById(R.id.root);
        back = (ImageView) findViewById(R.id.img_back);
        deleted = (ImageView) findViewById(R.id.img_delete);
        indicator = (TextView) findViewById(R.id.tv_indicator);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeletedImageScanDialog.this.dismiss();
            }
        });
        deleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewPager != null && mViewPager.getCurrentItem() > -1) {
                    showDeletedDialog();
                }
            }
        });
        deleted.setVisibility(deletedable ? View.VISIBLE : View.GONE);
        head.setVisibility(showTitle ? View.VISIBLE : View.GONE);
        llindicator.setVisibility(showTitle ? View.GONE : View.VISIBLE);
        mViewPager = (ExtendedViewPager) findViewById(R.id.evp_containar);
        mAdaptar = new TouchImageAdapter(scanImageUrls, this, getContext(), showTitle);
        mViewPager.setAdapter(mAdaptar);
        mViewPager.addOnPageChangeListener(this);
        if (showTitle) {
            mViewPager.setOnTitleShowListener(this);
        }
        new ViewPagerIndicator.Builder(getContext(), mViewPager, llindicator, mAdaptar.getCount())
                .setDotHeightByDp(8)
                .setDotWidthByDp(8)
                .setMarginByDp(8)
                .build();
        setCurrentPage(position);
        setCanceledOnTouchOutside(false);
    }

    private void showDeletedDialog() {
        if (removePhotoDialog == null) {
            removePhotoDialog = new AlertDialog.Builder(mContext)
                    .setTitle("提示")
                    .setMessage("确定要删除这张相片吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            removePhotoImage();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .create();
        }
        if (!removePhotoDialog.isShowing()) {
            removePhotoDialog.show();
            WindowManager.LayoutParams layoutParams = removePhotoDialog.getWindow().getAttributes();
            layoutParams.width = ScreenUtil.getScreenWidth(mContext) * 8 / 10;
            removePhotoDialog.getWindow().setAttributes(layoutParams);
        }
    }

    private void removePhotoImage() {
        int deletedPosition = mViewPager.getCurrentItem();
        scanImageUrls.remove(deletedPosition);
        if (onScanleDeletedListener != null) {
            onScanleDeletedListener.onScanleDeleted(deletedPosition);
        }
        if (scanImageUrls.size() > 0) {
            mAdaptar.notifyDataSetChanged();
            indicator.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setIndicatorText();
                }
            }, 500);
        } else {
            dismiss();
        }
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void setCurrentPage(int position) {
        mViewPager.setCurrentItem(position, false);
        setIndicatorText();
    }

    private void setIndicatorText() {
        indicator.setText((mViewPager.getCurrentItem() + 1) + "/" + scanImageUrls.size());
    }

    public static class TouchImageAdapter extends PagerAdapter {

        private List<ScanleImageUrl> scanImages;
        private DeletedImageScanDialog imageScanDialog;
        private Context mContext;
        private boolean showTitle;

        private TouchImageAdapter(List<ScanleImageUrl> scanImages, DeletedImageScanDialog imageScanDialog, Context context, boolean showTitle) {
            this.scanImages = scanImages;
            this.imageScanDialog = imageScanDialog;
            this.mContext = context;
            this.showTitle = showTitle;
        }

        @Override
        public int getCount() {
            return scanImages.size();
        }

        @Override
        public View instantiateItem(final ViewGroup container, int position) {
//            TouchImageView img = new TouchImageView(container.getContext());
//            img.setAdjustViewBounds(true);
//            img.setScaleType(ImageView.ScaleType.FIT_CENTER);
//            img.setBackgroundColor(Color.BLACK);
//            container.addView(img, LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT);
//            int screenWidth = ScreenUtil.getScreenWidth(mContext);
//            img.setMaxWidth(screenWidth);
//            img.setMaxHeight(screenWidth * 20);
//
//            GlideUtils.loadImage(mContext, scanImages.get(position).getImageUrl(), img);
//            img.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    imageScanDialog.dismiss();
//                }
//            });
            /*SubsamplingScaleImageView*/
            final SubsamplingScaleImageView imageView = new SubsamplingScaleImageView(mContext);
            imageView.setQuickScaleEnabled(true);
            imageView.setBackgroundColor(Color.BLACK);
            container.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            //手势回调
            final GestureDetector gestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    if (imageView.isReady()) {
                        PointF sCoord = imageView.viewToSourceCoord(e.getX(), e.getY());
                    }
                    if (!showTitle) {
                        imageScanDialog.dismiss();
                    }
                    return false;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    if (imageView.isReady()) {
                        PointF sCoord = imageView.viewToSourceCoord(e.getX(), e.getY());
                    }
                }

                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    if (imageView.isReady()) {
                        PointF sCoord = imageView.viewToSourceCoord(e.getX(), e.getY());
                    }
                    return false;
                }
            });

            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return gestureDetector.onTouchEvent(motionEvent);
                }
            });

            final int pos = position;
            imageView.setMaxScale(1.5f);
            imageView.setZoomEnabled(true);
            imageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
            if (scanImages.get(position).isVideo()) {
                GlideApp.with(mContext)
                        .asFile()
                        .load(scanImages.get(position).getImageUrl())
                        .apply(RequestOptions.frameOf(0)
                                .set(FRAME_OPTION, MediaMetadataRetriever.OPTION_CLOSEST)
                                .transform(new VideoFrameBitmapTransformation(scanImages.get(position).getImageUrl(), 0)))
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .placeholder(R.drawable.img_loading)
                        .error(R.drawable.img_loading)
                        .into(new SimpleTarget<File>() {
                            @Override
                            public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                                // 将保存的图片地址给SubsamplingScaleImageView,这里注意设置ImageViewState设置初始显示比例
                                ImageSource imageSource = ImageSource.uri(Uri.fromFile(resource));
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inJustDecodeBounds = true;//这个参数设置为true才有效，
                                BitmapFactory.decodeFile(resource.getAbsolutePath(), options);//这里的bitmap是个空
                                float sHeight = options.outHeight <= 0 ? 1 : options.outHeight;
                                float sWidth = options.outWidth <= 0 ? 1 : options.outWidth;
                                WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
//                            float width = wm.getDefaultDisplay().getWidth();
                                int height = wm.getDefaultDisplay().getHeight();
                                if (sHeight >= height && sHeight / sWidth >= 3) {
//                                float widthScale = sWidth / width;
                                    imageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
                                    imageView.setImage(imageSource, new ImageViewState(1, new PointF(0, 0), 0))
                                    ;
                                    imageView.setDoubleTapZoomStyle(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER_IMMEDIATE);
                                } else {
                                    imageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_INSIDE);
                                    imageView.setImage(imageSource);
                                    imageView.setDoubleTapZoomStyle(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER_IMMEDIATE);
                                }
                            }
                        });
            } else {
                String agateUrl = scanImages.get(position).getImageUrl();
                if (!TextUtils.isEmpty(agateUrl) && agateUrl.startsWith("http") && isVideo(agateUrl)) {
                    agateUrl = String.format("%s?vframe/jpg/offset/%s", agateUrl, 1);
                }
                GlideApp.with(mContext)
                        .asFile()
                        .load(agateUrl)
                        .placeholder(R.drawable.img_loading)
                        .error(R.drawable.img_loading)
                        .into(new SimpleTarget<File>() {
                            @Override
                            public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                                // 将保存的图片地址给SubsamplingScaleImageView,这里注意设置ImageViewState设置初始显示比例
                                ImageSource imageSource = ImageSource.uri(Uri.fromFile(resource));

                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inJustDecodeBounds = true;//这个参数设置为true才有效，
                                BitmapFactory.decodeFile(resource.getAbsolutePath(), options);//这里的bitmap是个空
                                float sHeight = options.outHeight <= 0 ? 1 : options.outHeight;
                                float sWidth = options.outWidth <= 0 ? 1 : options.outWidth;

                                WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
//                            float width = wm.getDefaultDisplay().getWidth();
                                int height = wm.getDefaultDisplay().getHeight();
                                if (sHeight >= height && sHeight / sWidth >= 3) {
//                                float widthScale = sWidth / width;
                                    imageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
                                    imageView.setImage(imageSource, new ImageViewState(1, new PointF(0, 0), 0));
                                    imageView.setDoubleTapZoomStyle(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER_IMMEDIATE);
                                } else {
                                    imageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_INSIDE);
                                    imageView.setImage(imageSource);
                                    imageView.setDoubleTapZoomStyle(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER_IMMEDIATE);
                                }
                            }
                        });
            }

            /*TouchImageView*/
//            TouchImageView img = new TouchImageView(container.getContext());
////            img.setScaleType(ImageView.ScaleType.FIT_CENTER);
//            img.setBackgroundColor(Color.BLACK);
////            container.addView(img, LinearLayout.LayoutParams.MATCH_PARENT,
////                    LinearLayout.LayoutParams.WRAP_CONTENT);
////            GlideUtils.loadImage(mContext, scanImageUrls.get(position), img);
//            container.addView(img, ScreenUtil.getScreenWidth(mContext),
//                    ScreenUtil.getScreenWidth(mContext));
//            Glide.with(mContext).load(scanImageUrls.get(position)).centerCrop().placeholder(net.arvin.afbaselibrary.R.drawable.img_loading).error(net.arvin.afbaselibrary.R.drawable.img_loading).into(img);
//            img.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    imageScanDialog.dismiss();
//                }
//            });
            return imageView;
        }

        //        @Override
//        public int getItemPosition(Object object) {
//            return POSITION_NONE;
//        }

        final String[] videoSuffix = new String[]{".avi", ".wmv", ".mpeg", ".mp4", ".mov", ".moov", ".mkv", ".flv", ".f4v", ".m4v", ".rmvb", ".rm", ".3gp", ".dat", ".ts", ".mts", ".vob"};

        public boolean isVideo(String imgLink) {
            boolean result = false;
            if (!TextUtils.isEmpty(imgLink) && imgLink.contains(".")) {
                String suffix = imgLink.substring(imgLink.lastIndexOf("."));
                if (!TextUtils.isEmpty(suffix)) {
                    suffix = suffix.toLowerCase();
                    for (int i = 0; i < videoSuffix.length; i++) {
                        if (TextUtils.equals(videoSuffix[i], suffix)) {
                            result = true;
                            break;
                        }
                    }
                }
            }
            return result;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        private int mChildCount = 0;

        @Override
        public void notifyDataSetChanged() {
            mChildCount = getCount();
            super.notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            if (mChildCount > 0) {
                mChildCount--;
                return POSITION_NONE;
            }
            return super.getItemPosition(object);

        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        if (head.getVisibility() == View.VISIBLE) {
            showHead();
        }
    }

    @Override
    public void onPageSelected(int index) {
        setIndicatorText();
    }

    private void startAnimator(float start, float end) {
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofFloat(start, end);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) head.getLayoutParams();
                    layoutParams.setMargins(0, -(int) ((1 - value) * ScreenUtil.dp2px(48)), 0, 0);
                    head.setLayoutParams(layoutParams);
                    if (value == 0 && head.getVisibility() == View.VISIBLE) {
                        head.setVisibility(View.GONE);
                    } else if (value != 0 && head.getVisibility() == View.GONE) {
                        head.setVisibility(View.VISIBLE);
                    }
                }
            });
            valueAnimator.setDuration(500);
        }
        valueAnimator.cancel();
        valueAnimator.setFloatValues(start, end);
        valueAnimator.start();
    }

    private void showHead() {
        if (head.getVisibility() == View.GONE) {
            startAnimator(0, 1);
        } else {
            startAnimator(1, 0);
        }
    }

    @Override
    public void onTitleShow(boolean needHide) {
        if (showTitle) {
            if (needHide) {
                if (head.getVisibility() == View.VISIBLE) {
                    startAnimator(1, 0);
                }
            } else {
                showHead();
            }
        }
    }
}
