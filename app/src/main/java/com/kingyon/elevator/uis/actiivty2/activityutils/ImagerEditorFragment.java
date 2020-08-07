package com.kingyon.elevator.uis.actiivty2.activityutils;

import android.content.Context;
import android.os.Bundle;

import com.kingyon.elevator.view.ColorBar;
import com.leo.afbaselibrary.uis.fragments.BaseFragment;


/**
 * @Created By Admin  on 2020/7/22
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class ImagerEditorFragment extends BaseFragment implements ColorBar.ColorChangeListener {
    @Override
    public void colorChange(int color) {

    }

    @Override
    public int getContentViewId() {
        return 0;
    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    protected void dealLeackCanary() {

    }

//    Unbinder unbinder;
//    @BindView(R.id.ll_edit_filter)
//    LinearLayout llEditFilter;
//    @BindView(R.id.ll_edit_sticket)
//    LinearLayout llEditSticket;
//    @BindView(R.id.ll_edit_text)
//    LinearLayout llEditText;
//    @BindView(R.id.ll_edit_cut)
//    LinearLayout llEditCut;
//    @BindView(R.id.ll_edit_fuzzy)
//    LinearLayout llEditFuzzy;
//    @BindView(R.id.ll_root)
//    LinearLayout llRoot;
//    @BindView(R.id.rl_expression)
//    RelativeLayout rlExpression;
//    @BindView(R.id.tv_close)
//    TextView tvClose;
//    @BindView(R.id.tv_finish)
//    TextView tvFinish;
//    @BindView(R.id.et_tag)
//    EditText etTag;
//    @BindView(R.id.tv_tag)
//    TextView tvTag;
//    @BindView(R.id.tv_size)
//    TextView tvSize;
//    @BindView(R.id.text_size)
//    SeekBar textSize;
//    @BindView(R.id.colorbar)
//    ColorBar colorbar;
//    @BindView(R.id.rl_edit_text)
//    RelativeLayout rlEditText;
//    @BindView(R.id.ll_effect_container)
//    LinearLayout llEffectContainer;
//    @BindView(R.id.hsv_effect)
//    HorizontalScrollView hsvEffect;
//
//    @BindView(R.id.main_image)
//    ImageViewTouch mainImage;
//    @BindView(R.id.sticker_panel)
//    StickerView stickerPanel;
//    @BindView(R.id.crop_panel)
//    CropImageView cropPanel;
//    @BindView(R.id.rotate_panel)
//    RotateImageView rotatePanel;
//    @BindView(R.id.text_sticker_panel)
//    TextStickerView textStickerPanel;
//    @BindView(R.id.custom_paint_view)
//    CustomPaintView customPaintView;
//    @BindView(R.id.work_space)
//    FrameLayout workSpace;
//    @BindView(R.id.rl_touch_view)
//    RelativeLayout rlTouchView;
//
//    @BindView(R.id.tv_crop_qx)
//    TextView tvCropQx;
//    @BindView(R.id.tv_crop_yy)
//    TextView tvCropYy;
//    @BindView(R.id.ll_crop_panel)
//    LinearLayout llCropPanel;
//    @BindView(R.id.rl_crop_panel)
//    RelativeLayout rlCropPanel;
    private String imgPath;
//
//    /*功能*/
//    public boolean isFilter = true;
//    public boolean isSticket = true;
//    public boolean isText = true;
//
//    /**
//     * 贴图Fragment
//     */
//    public StickerFragment mStickerFragment;
//    /**
//     * 滤镜FliterListFragment
//     */
//    public FilterListFragment mFilterListFragment;
//    /**
//     * 图片剪裁Fragment
//     */
//    public CropFragment mCropFragment;
//    /**
//     * 图片旋转Fragment
//     */
//    public RotateFragment mRotateFragment;
//    /**
//     * 图片添加文字
//     */
//    public AddTextFragment mAddTextFragment;
//    /**
//     * 绘制模式Fragment
//     */
//    public PaintFragment mPaintFragment;
//    /**
//     * 美颜模式Fragment
//     */
//    public BeautyFragment mBeautyFragment;
//
//    private LoadImageTask mLoadImageTask;
//
//    /**
//     * 展示图片控件 宽 高
//     */
//    private int imageWidth, imageHeight;
//
//    public static  ImageViewTouch viewTouch;
//
//    /**
//     * 底层显示Bitmap
//     */
//    private Bitmap mainBitmap;
//
//
//    /**
//     * 滤镜处理后的bitmap
//     */
//    private Bitmap fliterBit;
//
//    /**
//     * 标记变量
//     */
//    public static Bitmap currentBitmap;
//
//    /**
//     * 撤销操作
//     */
//    private RedoUndoController mRedoUndoController;
//
//    private List<FilterModel> mVideoEffects = new ArrayList<>(); //视频滤镜效果
//    private MagicFilterType[] mMagicFilterTypes;
//    private List<String> strlist = new ArrayList<>();
//    private List<Integer> integers = new ArrayList<>();
//
//    protected int mOpTimes = 0;
//    protected boolean isBeenSaved = false;
    Context contex;
//
//    private int windowWidth;
//    private int windowHeight;
//    private int dp100;
//
//    boolean isFirstShowEditText;
//    private InputMethodManager manager;
//    private static List<RatioItem> dataList = new ArrayList<RatioItem>();
//    private CropRationClick mCropRationClick = new CropRationClick();
//
//    static {
//        // init data
//        dataList.add(new RatioItem("自由", -1f));
//        dataList.add(new RatioItem("1:1", 1f));
//        dataList.add(new RatioItem("1:2", 1 / 2f));
//        dataList.add(new RatioItem("1:3", 1 / 3f));
//        dataList.add(new RatioItem("2:3", 2 / 3f));
////        dataList.add(new RatioItem("3:4", 3 / 4f));
////        dataList.add(new RatioItem("2:1", 2f));
////        dataList.add(new RatioItem("3:1", 3f));
////        dataList.add(new RatioItem("3:2", 3 / 2f));
////        dataList.add(new RatioItem("4:3", 4 / 3f));
//    }
//
//    public TextView selctedTextView;
//
//    GetImageBitme getImageBitme;
//
    public ImagerEditorFragment setIndex(Context context, String imgPath) {
        this.imgPath = imgPath;
        this.contex = context;
        return (this);
    }
//
//    @Override
//    public int getContentViewId() {
//        return R.layout.fragment_imager_efitor;
//    }
//
//    @Override
//    public void init(Bundle savedInstanceState) {
//        LogUtils.e(imgPath);
//        loadImage(imgPath);
//        initTopView();
//        initFfects();
//        initExpression();
//        initTextSpeed();
//        initCropPanel();
//        this.viewTouch = mainImage;
//
//    }
//
//
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            if (hsvEffect != null) {
//                initView();
//            }
//        }
//    }
//
//    /**
//     * 初始化view
//     */
//    private void initTopView() {
//        DisplayMetrics metrics = contex.getResources().getDisplayMetrics();
//        imageWidth = metrics.widthPixels / 2;
//        imageHeight = metrics.heightPixels / 2;
//
//        dp100 = (int) getResources().getDimension(com.zhaoss.weixinrecorded.R.dimen.dp100);
//        windowWidth = Utils.getWindowWidth(getActivity());
//        windowHeight = Utils.getWindowHeight(getActivity());
//        LogUtils.e(imageWidth, imageHeight, windowWidth, windowHeight);
//        colorbar.setOnColorChangerListener(this);
//
//
//        mStickerFragment = StickerFragment.newInstance();
//        mFilterListFragment = FilterListFragment.newInstance();
//        mCropFragment = CropFragment.newInstance();
//        mRotateFragment = RotateFragment.newInstance();
//        mAddTextFragment = AddTextFragment.newInstance();
//        mPaintFragment = PaintFragment.newInstance();
//        mBeautyFragment = BeautyFragment.newInstance();
////        initColors();
//
//        etTag.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                tvTag.setText(s.toString());
//                LogUtils.e(tvTag.getWidth(), tvTag.getHeight());
//            }
//        });
//    }
//
//    @Override
//    protected void dealLeackCanary() {
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // TODO: inflate a fragment view
//        View rootView = super.onCreateView(inflater, container, savedInstanceState);
//        unbinder = ButterKnife.bind(this, rootView);
//        return rootView;
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        unbinder.unbind();
//    }
//
//    private void initCropPanel() {
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        params.gravity = Gravity.CENTER_VERTICAL;
//        params.leftMargin = 20;
//        params.rightMargin = 20;
//        for (int i = 0, len = dataList.size(); i < len; i++) {
//            TextView text = new TextView(getActivity());
//            text.setTextColor(UNSELECTED_COLOR);
//            text.setTextSize(20);
//            text.setText(dataList.get(i).getText());
//            llCropPanel.addView(text, params);
//            text.setTag(i);
//            if (i == 0) {
//                selctedTextView = text;
//            }
//            dataList.get(i).setIndex(i);
//            text.setTag(dataList.get(i));
//            text.setOnClickListener(mCropRationClick);
//        }
//    }
//
//    /**
//     * 选择剪裁比率
//     *
//     * @author
//     */
//    private final class CropRationClick implements View.OnClickListener {
//        @Override
//        public void onClick(View v) {
//            TextView curTextView = (TextView) v;
//            selctedTextView.setTextColor(UNSELECTED_COLOR);
//            RatioItem dataItem = (RatioItem) v.getTag();
//            selctedTextView = curTextView;
//            selctedTextView.setTextColor(SELECTED_COLOR);
//
//            cropPanel.setRatioCropRect(mainImage.getBitmapRect(),
//                    dataItem.getRatio());
////
//            // System.out.println("dataItem   " + dataItem.getText());
//        }
//    }// end inner class
//
//
//    @OnClick({R.id.ll_edit_filter, R.id.ll_edit_sticket, R.id.ll_edit_text,
//            R.id.ll_edit_cut, R.id.ll_edit_fuzzy, R.id.ll_root, R.id.rl_expression,
//            R.id.tv_close, R.id.tv_finish, R.id.tv_tag, R.id.tv_size, R.id.rl_edit_text,
//            R.id.ll_effect_container, R.id.hsv_effect, R.id.tv_crop_qx, R.id.tv_crop_yy})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.ll_edit_filter:
//                /*滤镜*/
//                if (isFilter) {
//                    isFilter = false;
//                    hsvEffect.setVisibility(View.VISIBLE);
//                } else {
//                    isFilter = true;
//                    hsvEffect.setVisibility(View.GONE);
//                }
//                break;
//            case R.id.ll_edit_sticket:
//                /*贴图*/
//                if (isSticket) {
//                    isSticket = false;
//                    rlExpression.setVisibility(View.VISIBLE);
//                } else {
//                    isSticket = true;
//                    rlExpression.setVisibility(View.GONE);
//                }
//                break;
//            case R.id.ll_edit_text:
//                /*文字*/
//                if (isText) {
//                    isText = false;
//                    rlEditText.setVisibility(View.VISIBLE);
//                } else {
//                    isText = true;
//                    rlEditText.setVisibility(View.GONE);
//                }
//                break;
//            case R.id.ll_edit_cut:
//                /*剪切*/
////                startCrop(getActivity(), Uri.fromFile(new File(imgPath)));
//                rlCropPanel.setVisibility(View.VISIBLE);
//                cropPanel.setVisibility(View.VISIBLE);
//
//                break;
//            case R.id.ll_edit_fuzzy:
//                /*马赛克*/
//
//                break;
//            case R.id.ll_root:
//
//                break;
//            case R.id.rl_expression:
//
//                break;
//            case R.id.tv_close:
//                initView();
//
//                break;
//            case R.id.tv_finish:
//                LogUtils.e("==============");
//                initView();
////                changeTextState(!(rlEditText.getVisibility() == View.VISIBLE));
//                LogUtils.e(colorbar.getCurrentColor());
//                if (etTag.getText().length() > 0) {
//                    addTextToWindow(colorbar.getCurrentColor());
//                }
//                break;
//            case R.id.tv_tag:
//
//                break;
//            case R.id.tv_size:
//
//                break;
//            case R.id.rl_edit_text:
//
//                break;
//            case R.id.ll_effect_container:
//
//                break;
//            case R.id.hsv_effect:
//
//                break;
//            case R.id.tv_crop_qx:
//                rlCropPanel.setVisibility(View.GONE);
//                cropPanel.setVisibility(View.GONE);
//                break;
//            case R.id.tv_crop_yy:
//                CropImageTask task = new CropImageTask();
//                task.execute(getMainBit());
//                rlCropPanel.setVisibility(View.GONE);
//                cropPanel.setVisibility(View.GONE);
//                break;
//        }
//    }
//    /**
//     * 图片剪裁生成 异步任务
//     *
//     * @author panyi
//     *
//     */
//    private final class CropImageTask extends AsyncTask<Bitmap, Void, Bitmap> {
//        private Dialog dialog;
//
//        @Override
//        protected void onCancelled() {
//            super.onCancelled();
//            dialog.dismiss();
//        }
//
//        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//        @Override
//        protected void onCancelled(Bitmap result) {
//            super.onCancelled(result);
//            dialog.dismiss();
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            dialog = BaseActivity.getLoadingDialog(getActivity(), com.xinlan.imageeditlibrary.R.string.saving_image,
//                    false);
//            dialog.show();
//        }
//
//        @SuppressWarnings("WrongThread")
//        @Override
//        protected Bitmap doInBackground(Bitmap... params) {
//            RectF cropRect = cropPanel.getCropRect();// 剪切区域矩形
//            Matrix touchMatrix = mainImage.getImageViewMatrix();
//            // Canvas canvas = new Canvas(resultBit);
//            float[] data = new float[9];
//            touchMatrix.getValues(data);// 底部图片变化记录矩阵原始数据
//            Matrix3 cal = new Matrix3(data);// 辅助矩阵计算类
//            Matrix3 inverseMatrix = cal.inverseMatrix();// 计算逆矩阵
//            Matrix m = new Matrix();
//            m.setValues(inverseMatrix.getValues());
//            m.mapRect(cropRect);// 变化剪切矩形
//            // Paint paint = new Paint();
//            // paint.setColor(Color.RED);
//            // paint.setStrokeWidth(10);
//            // canvas.drawRect(cropRect, paint);
//            // Bitmap resultBit = Bitmap.createBitmap(params[0]).copy(
//            // Bitmap.Config.ARGB_8888, true);
//            Bitmap resultBit = Bitmap.createBitmap(params[0],
//                    (int) cropRect.left, (int) cropRect.top,
//                    (int) cropRect.width(), (int) cropRect.height());
//            LogUtils.e(resultBit);
//            //saveBitmap(resultBit, activity.saveFilePath);
//            return resultBit;
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap result) {
//            super.onPostExecute(result);
//            dialog.dismiss();
//            if (result == null)
//                return;
//
//            changeMainBitmap(result,true);
//            cropPanel.setCropRect(mainImage.getBitmapRect());
////            backToMain();
//            currentBitmap = result;
//
//        }
//    }// end inner class
//
//    /**初始化点击view*/
//    private void initView() {
//        isFilter = true;
//        isSticket = true;
//        isText = true;
//        hsvEffect.setVisibility(View.GONE);
//        rlExpression.setVisibility(View.GONE);
//        rlEditText.setVisibility(View.GONE);
//        SoftkeyboardUtils.hideInput(getActivity());
//
//    }
//
//
//    /**初始化滤镜贴图*/
//    private void initFfects() {
//        integers.add(R.mipmap.bg_sticker_01);
//        integers.add(R.mipmap.bg_sticker_02);
//        integers.add(R.mipmap.bg_sticker_03);
//        integers.add(R.mipmap.bg_sticker_04);
//        integers.add(R.mipmap.bg_sticker_05);
//        integers.add(R.mipmap.bg_sticker_06);
//        integers.add(R.mipmap.bg_sticker_07);
//        integers.add(R.mipmap.bg_sticker_08);
//        integers.add(R.mipmap.bg_sticker_09);
//        integers.add(R.mipmap.bg_sticker_10);
//        integers.add(R.mipmap.bg_sticker_11);
//        integers.add(R.mipmap.bg_sticker_12);
//        integers.add(R.mipmap.bg_sticker_13);
//        integers.add(R.mipmap.bg_sticker_14);
//        integers.add(R.mipmap.bg_sticker_15);
//        integers.add(R.mipmap.bg_sticker_16);
//        integers.add(R.mipmap.bg_sticker_17);
//        integers.add(R.mipmap.bg_sticker_18);
//        integers.add(R.mipmap.bg_sticker_19);
//        integers.add(R.mipmap.bg_sticker_20);
//        integers.add(R.mipmap.bg_sticker_21);
//        integers.add(R.mipmap.bg_sticker_22);
//        integers.add(R.mipmap.bg_sticker_23);
//        integers.add(R.mipmap.bg_sticker_24);
//        integers.add(R.mipmap.bg_sticker_25);
//        integers.add(R.mipmap.bg_sticker_26);
//        integers.add(R.mipmap.bg_sticker_27);
//        integers.add(R.mipmap.bg_sticker_28);
//
//        strlist.add("原图");
//        strlist.add("胶片");
//        strlist.add("怀旧");
//        strlist.add("黑白");
//        strlist.add("色温");
//        strlist.add("重叠");
//        strlist.add("模糊");
//        strlist.add("噪点");
//        strlist.add("对比度");
//        strlist.add("伽马线");
//        strlist.add("色度");
//        strlist.add("交叉");
//        strlist.add("灰度");
//        strlist.add("染料");
//        //滤镜效果集合
//        mMagicFilterTypes = new MagicFilterType[]{
//                MagicFilterType.NONE, MagicFilterType.INVERT,
//                MagicFilterType.SEPIA, MagicFilterType.BLACKANDWHITE,
//                MagicFilterType.TEMPERATURE, MagicFilterType.OVERLAY,
//                MagicFilterType.BARRELBLUR, MagicFilterType.POSTERIZE,
//                MagicFilterType.CONTRAST, MagicFilterType.GAMMA,
//                MagicFilterType.HUE, MagicFilterType.CROSSPROCESS,
//                MagicFilterType.GRAYSCALE, MagicFilterType.CGACOLORSPACE,
//        };
//        ConfigUtils.getInstance().setMagicFilterType(mMagicFilterTypes[0]);
//        for (int i = 0; i < strlist.size(); i++) {
//            FilterModel model = new FilterModel();
//            model.setName(strlist.get(i));
//            mVideoEffects.add(model);
//        }
//        addEffectView();
//
//    }
//
//    /**添加滤镜*/
//    private void addEffectView() {
//        llEffectContainer.removeAllViews();
//        for (int i = 0; i < mVideoEffects.size(); i++) {
//            View itemView = LayoutInflater.from(getActivity())
//                    .inflate(R.layout.item_video_effect, llEffectContainer, false);
//            TextView tv = itemView.findViewById(R.id.tv);
//            ImageView iv = itemView.findViewById(R.id.iv);
//            FilterModel model = mVideoEffects.get(i);
//            int thumbId = MagicFilterFactory.filterType2Thumb(mMagicFilterTypes[i]);
//            iv.setImageResource(thumbId);
//            tv.setText(model.getName());
//            final int index = i;
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @SuppressLint("ResourceAsColor")
//                @Override
//                public void onClick(View view) {
//                    for (int j = 0; j < llEffectContainer.getChildCount(); j++) {
//                        View tempItemView = llEffectContainer.getChildAt(j);
//                        TextView tempTv = tempItemView.findViewById(R.id.tv);
//                        FilterModel tempModel = mVideoEffects.get(j);
//                        if (j == index) {
//                            //选中的滤镜效果
//                            if (!tempModel.isChecked()) {
//                                tempTv.setTextColor(Color.parseColor("#f00000"));
////                                openEffectAnimation(tempTv, tempModel, true);
//                                tempModel.setChecked(true);
//                                ProcessingImage task = new ProcessingImage();
//                                task.execute(j);
//                                initView();
//                            }
//                            ConfigUtils.getInstance().setMagicFilterType(mMagicFilterTypes[j]);
//                            LogUtils.e(MagicFilterFactory.getFilter().getFragmentShader(), MagicFilterFactory.getFilter());
////                            mSurfaceView.setFilter(MagicFilterFactory.getFilter());
//                        } else {
//                            //未选中的滤镜效果
//                            if (tempModel.isChecked()) {
//                                tempTv.setTextColor(Color.parseColor("#000000"));
////                                openEffectAnimation(tempTv, tempModel, false);
//                                tempModel.setChecked(false);
//                            }
//                        }
//                    }
//                }
//            });
//            llEffectContainer.addView(itemView);
//        }
//
//    }
//
//    /**
//     * 初始化表情
//     */
//    private void initExpression() {
//        int dp80 = (int) getResources().getDimension(R.dimen.dp80);
//        int dp10 = (int) getResources().getDimension(R.dimen.dp10);
//        GridView gridView = new GridView(getActivity());
//        gridView.setPadding(dp10, dp10, dp10, dp10);
//        gridView.setNumColumns(4);
//        gridView.setVerticalSpacing(15);
//        gridView.setHorizontalSpacing(15);
//        MyiconAdapter myiconAdapter = new MyiconAdapter(getActivity(), integers);
//        gridView.setAdapter(myiconAdapter);
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                rlExpression.setVisibility(View.GONE);
////                iv_icon.setImageResource(R.mipmap.icon);
//                addExpressionToWindow(integers.get(position));
//            }
//        });
//        rlExpression.addView(gridView);
//    }
//
//    /**
//     * 添加表情到界面上
//     */
//    private void addExpressionToWindow(int result) {
//        TouchView touchView = new TouchView(getActivity());
//        touchView.setBackgroundResource(result);
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(dp100, dp100);
//        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
//        touchView.setLayoutParams(layoutParams);
//
//        touchView.setLimitsX(0, windowWidth);
//        touchView.setLimitsY(0, windowHeight - dp100 / 2);
//        touchView.setOnLimitsListener(new TouchView.OnLimitsListener() {
//            @Override
//            public void OnOutLimits(float x, float y) {
////                tv_hint_delete.setTextColor(Color.RED);
//            }
//
//            @Override
//            public void OnInnerLimits(float x, float y) {
////                tv_hint_delete.setTextColor(Color.WHITE);
//            }
//        });
//        touchView.setOnTouchListener(new TouchView.OnTouchListener() {
//            @Override
//            public void onDown(TouchView view, MotionEvent event) {
////                tv_hint_delete.setVisibility(View.VISIBLE);
//                Log.e("TAG", event.toString());
//
//            }
//
//            @Override
//            public void onMove(TouchView view, MotionEvent event) {
//
//            }
//
//            @Override
//            public void onUp(TouchView view, MotionEvent event) {
////                tv_hint_delete.setVisibility(View.GONE);
//                if (view.isOutLimits()) {
//                    rlTouchView.removeView(view);
//                }
//            }
//        });
//
//        rlTouchView.addView(touchView);
//    }
//
//    public static void startCrop(Activity activity, Uri uri) {
//        //裁剪后保存到文件中
//        Uri sourceUri = uri;
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
//        Date date = new Date();
//        String imageName = simpleDateFormat.format(date);
//        Uri destinationUri = Uri.fromFile(new File(activity.getCacheDir(), imageName + ".jpeg"));
//        UCrop.Options options = new UCrop.Options();
//        options.setToolbarColor(Color.parseColor("#F53051"));
//        options.setStatusBarColor(Color.parseColor("#F53051"));
//        options.setFreeStyleCropEnabled(true);
//        UCrop.of(sourceUri, destinationUri).start(activity, UCrop.REQUEST_CROP);
//    }
//
//
//    /**
//     * 异步载入编辑图片
//     *
//     * @param filepath
//     */
//    public void loadImage(String filepath) {
//        if (mLoadImageTask != null) {
//            mLoadImageTask.cancel(true);
//        }
//        mLoadImageTask = new LoadImageTask();
//        mLoadImageTask.execute(filepath);
//    }
//
//    @Override
//    public void colorChange(int color) {
//        etTag.setTextColor(color);
//    }
//
//    /**
//     * 导入文件图片任务
//     */
//    private final class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
//        @Override
//        protected Bitmap doInBackground(String... params) {
//            return BitmapUtils.getSampledBitmap(params[0], 570,
//                    1280);
////            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap result) {
//            changeMainBitmap(result, false);
//        }
//    }// end inner class
//
//    /**
//     * @param newBit
//     * @param needPushUndoStack
//     */
//    public void changeMainBitmap(Bitmap newBit, boolean needPushUndoStack) {
//        if (newBit == null)
//            return;
//        if (mainBitmap == null || mainBitmap != newBit) {
//            if (needPushUndoStack) {
////                mRedoUndoController.switchMainBit(mainBitmap, newBit);
//                increaseOpTimes();
//            }
//            mainBitmap = newBit;
//            mainImage.setImageBitmap(mainBitmap);
//            mainImage.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
//            currentBitmap = mainBitmap;
//            bitmapList.add(currentBitmap);
//            if (getImageBitme!=null) {
//                getImageBitme.getBitme(bitmapList);
//            }
//        }
//    }
//
//
//    public void increaseOpTimes() {
//        mOpTimes++;
//        isBeenSaved = false;
//    }
//
//    public  Bitmap getMainBit() {
//        return mainBitmap;
//    }
//
//
//    /*滤镜效果*/
//
//    private final class ProcessingImage extends AsyncTask<Integer, Void, Bitmap> {
//        private Dialog dialog;
//        private Bitmap srcBitmap;
//
//        @Override
//        protected Bitmap doInBackground(Integer... params) {
//            int type = params[0];
//            if (srcBitmap != null && !srcBitmap.isRecycled()) {
//                srcBitmap.recycle();
//            }
//
//            srcBitmap = Bitmap.createBitmap(getMainBit().copy(
//                    Bitmap.Config.ARGB_8888, true));
//            return PhotoProcessing.filterPhoto(srcBitmap, type);
//        }
//
//        @Override
//        protected void onCancelled() {
//            super.onCancelled();
//            dialog.dismiss();
//        }
//
//        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//        @Override
//        protected void onCancelled(Bitmap result) {
//            super.onCancelled(result);
//            dialog.dismiss();
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap result) {
//            super.onPostExecute(result);
//            dialog.dismiss();
//            if (result == null)
//                return;
//            if (fliterBit != null && (!fliterBit.isRecycled())) {
//                fliterBit.recycle();
//            }
//            fliterBit = result;
//            mainImage.setImageBitmap(fliterBit);
//            changeMainBitmap(fliterBit,true);
//            currentBitmap = fliterBit;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            dialog = BaseActivity.getLoadingDialog(getActivity(), com.xinlan.imageeditlibrary.R.string.handing,
//                    false);
//            dialog.show();
//        }
//
//    }
//
//    /**
//     * 添加文字到界面上
//     */
//    private void addTextToWindow(int currentColor) {
//        LogUtils.e(colorbar.getCurrentColor(), etTag.getWidth(), etTag.getHeight());
//        etTag.setTextColor(currentColor);
//        etTag.setTextColor(currentColor);
//        tvTag.setTextSize(Float.parseFloat(tvSize.getText().toString()));
//        TouchView touchView = new TouchView(getActivity());
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(tvTag.getWidth(), tvTag.getHeight());
//        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
//        touchView.setLayoutParams(layoutParams);
//        Bitmap bitmap = Bitmap.createBitmap(tvTag.getWidth(), tvTag.getHeight(), Bitmap.Config.ARGB_8888);
//        tvTag.draw(new Canvas(bitmap));
////        touchView.setImageBitmap(bitmap);
//        touchView.setBackground(new BitmapDrawable(bitmap));
////        touchView.setBackgroundResource(R.mipmap.color_click);
//        touchView.setPadding(10, 10, 10, 10);
//        touchView.setLimitsX(0, windowWidth);
//        touchView.setLimitsY(0, windowHeight - dp100 / 2);
//        touchView.setOnLimitsListener(new TouchView.OnLimitsListener() {
//            @Override
//            public void OnOutLimits(float x, float y) {
////                tv_hint_delete.setTextColor(Color.RED);
//            }
//
//            @Override
//            public void OnInnerLimits(float x, float y) {
////                tv_hint_delete.setTextColor(Color.WHITE);
//            }
//        });
//        touchView.setOnTouchListener(new TouchView.OnTouchListener() {
//            @Override
//            public void onDown(TouchView view, MotionEvent event) {
////                tv_hint_delete.setVisibility(View.VISIBLE);
////                changeMode(false);
//            }
//
//            @Override
//            public void onMove(TouchView view, MotionEvent event) {
//
//            }
//
//            @Override
//            public void onUp(TouchView view, MotionEvent event) {
////                tv_hint_delete.setVisibility(View.GONE);
////                changeMode(true);
//                if (view.isOutLimits()) {
//                    rlTouchView.removeView(view);
//                }
//            }
//        });
//        rlTouchView.addView(touchView);
//        etTag.setText("");
//        tvTag.setTextColor(Color.parseColor("#FA0606"));
//        textSize.setProgress(14);
//        colorbar.setWz(60);
//        tvTag.setText("");
//    }
//
//
//    private void initTextSpeed() {
//        textSize.setMax(40);
//        textSize.setProgress(14);
//        textSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @RequiresApi(api = Build.VERSION_CODES.M)
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                tvSize.setText((progress + 10) + "");
//                etTag.setTextSize((progress + 10));
//                tvTag.setTextSize((progress + 10));
//                LogUtils.e(tvTag.getWidth(), tvTag.getHeight());
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//            }
//        });
//
//    }
//
//
//    /**
//     * 弹出键盘
//     */
//    public void popupEditText() {
//
//        isFirstShowEditText = true;
//        etTag.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                if (isFirstShowEditText) {
//                    isFirstShowEditText = false;
//                    etTag.setFocusable(true);
//                    etTag.setFocusableInTouchMode(true);
//                    etTag.requestFocus();
//                    isFirstShowEditText = !manager.showSoftInput(etTag, 0);
//                }
//            }
//        });
//    }
//
//    /**
//     * 执行文字编辑区域动画
//     */
//    private void startAnim(float start, float end, AnimatorListenerAdapter listenerAdapter) {
//
//        ValueAnimator va = ValueAnimator.ofFloat(start, end).setDuration(200);
//        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                float value = (float) animation.getAnimatedValue();
//                rlEditText.setY(value);
//            }
//        });
//        if (listenerAdapter != null) va.addListener(listenerAdapter);
//        va.start();
//    }
//
//    /**
//     * 更改文字输入状态的界面
//     */
//    private void changeTextState(boolean flag) {
//
//        if (flag) {
//            rlEditText.setY(windowHeight);
//            rlEditText.setVisibility(View.VISIBLE);
//            startAnim(rlEditText.getY(), 0, null);
//            popupEditText();
//        } else {
//            manager.hideSoftInputFromWindow(etTag.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//            startAnim(0, windowHeight, new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    rlEditText.setVisibility(View.GONE);
//                }
//            });
//        }
//    }
//
//    public  void getBitmap(GetImageBitme getImageBitme){
//            this.getImageBitme = getImageBitme;
//    }
}
