package com.muzhi.camerasdk;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.muzhi.camerasdk.adapter.Filter_Effect_Adapter;
import com.muzhi.camerasdk.adapter.Filter_Sticker_Adapter;
import com.muzhi.camerasdk.adapter.FragmentViewPagerAdapter;
import com.muzhi.camerasdk.adapter.SmallThumbAdapter;
import com.muzhi.camerasdk.library.filter.GPUImageFilter;
import com.muzhi.camerasdk.library.filter.util.ImageFilterTools;
import com.muzhi.camerasdk.library.views.HorizontalListView;
import com.muzhi.camerasdk.model.CameraSdkParameterInfo;
import com.muzhi.camerasdk.model.Constants;
import com.muzhi.camerasdk.model.Filter_Effect_Info;
import com.muzhi.camerasdk.model.Filter_Sticker_Info;
import com.muzhi.camerasdk.ui.fragment.EfectFragment;
import com.muzhi.camerasdk.utils.ColorBar;
import com.muzhi.camerasdk.utils.FilterUtils;
import com.muzhi.camerasdk.view.CustomViewPager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class FilterImageActivity extends BaseActivity implements ColorBar.ColorChangeListener{
	
	private CameraSdkParameterInfo mCameraSdkParameterInfo=new CameraSdkParameterInfo();
	
	private HorizontalListView effect_listview, sticker_listview,images_listview;
	
	private TextView tab_effect, tab_sticker,txt_cropper,btn_done,txt_enhance,txt_graffiti,tv_title;
	private RelativeLayout loading_layout;// 等待框
	private SeekBar mSeekBar;		
	

	private SmallThumbAdapter iAdapter;
	private Filter_Effect_Adapter eAdapter;
	private Filter_Sticker_Adapter sAdapter;

	private ArrayList<Filter_Effect_Info> effect_list=new ArrayList<Filter_Effect_Info>(); //特效

	private ArrayList<Filter_Sticker_Info> stickerList = new ArrayList<Filter_Sticker_Info>();
	private ArrayList<String> imageList;
	
	public static Filter_Sticker_Info mSticker = null; // 从贴纸库过来的贴纸

	private FragmentViewPagerAdapter fAdapter;
	private CustomViewPager mViewPager;	
	private ArrayList<Fragment> fragments;
	
	private int current=0;
	private int num;

	/*文字*/
	private RelativeLayout rl_edit_text,content_container,layout_actionbar_root;
	private TextView tv_close,tv_finish,tv_tag,tv_size;
	private EditText et_tag;
	private SeekBar text_size;
	private ColorBar colorbar;
	private ImageView img_test;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.camerasdk_filter_image);
		showLeftIcon();

		try{
			mCameraSdkParameterInfo=(CameraSdkParameterInfo)getIntent().getSerializableExtra(CameraSdkParameterInfo.EXTRA_PARAMETER);
			num = getIntent().getIntExtra("num",0);
			imageList=mCameraSdkParameterInfo.getImage_list();
		}
		catch(Exception e){}
		
		initView();	
		
		tv_title=(TextView)findViewById(R.id.camerasdk_actionbar_title);
		if(mCameraSdkParameterInfo.isSingle_mode()){
			setActionBarTitle("编辑图片");
		}
		else{
			tv_title.setVisibility(View.VISIBLE);
			findViewById(R.id.images_layout).setVisibility(View.GONE);
		}
		
		initEvent();
		initData();
		initTextSpeed();
		
	}

	private void initView() {
		
		mViewPager= (CustomViewPager)findViewById(R.id.viewpager);
		
		tab_effect = (TextView) findViewById(R.id.txt_effect);
		tab_sticker = (TextView) findViewById(R.id.txt_sticker);
		txt_cropper = (TextView) findViewById(R.id.txt_cropper);
		txt_enhance = (TextView) findViewById(R.id.txt_enhance);
		txt_graffiti = (TextView) findViewById(R.id.txt_graffiti);
		
		btn_done = (TextView) findViewById(R.id.camerasdk_title_txv_right_text);
		btn_done.setText("完成");
		btn_done.setVisibility(View.VISIBLE);
		mSeekBar=(SeekBar)findViewById(R.id.seekBar);
		effect_listview = (HorizontalListView) findViewById(R.id.effect_listview);
		sticker_listview = (HorizontalListView) findViewById(R.id.sticker_listview);
		images_listview = (HorizontalListView) findViewById(R.id.images_listview);
		loading_layout = (RelativeLayout) findViewById(R.id.loading);

		/*文字*/
		rl_edit_text = findViewById(R.id.rl_edit_text);
		content_container = findViewById(R.id.content_container);
		layout_actionbar_root = findViewById(R.id.layout_actionbar_root);
		tv_close = findViewById(R.id.tv_close);
		tv_finish = findViewById(R.id.tv_finish);
		tv_tag = findViewById(R.id.tv_tag);
		tv_size = findViewById(R.id.tv_size);
		et_tag = findViewById(R.id.et_tag);
		text_size = findViewById(R.id.text_size);
		img_test = findViewById(R.id.img_test);
		colorbar = findViewById(R.id.colorbar);
		colorbar.setOnColorChangerListener(this);

	}
	private void initTextSpeed() {
		text_size.setMax(40);
		text_size.setProgress(14);
		text_size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@RequiresApi(api = Build.VERSION_CODES.M)
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				tv_size.setText((progress+10) + "");
				et_tag.setTextSize((progress+10));
				tv_tag.setTextSize((progress+10));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});

	}

	private void initEvent(){
		/*文字输入*/
		et_tag.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}
			@Override
			public void afterTextChanged(Editable s) {
				tv_tag.setText(s.toString());
			}
		});


		tab_effect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				effect_listview.setVisibility(View.VISIBLE);
				sticker_listview.setVisibility(View.INVISIBLE);
//				tab_effect.setTextColor(getResources().getColor(R.color.camerasdk_txt_selected));
//				tab_sticker.setTextColor(getResources().getColor(R.color.camerasdk_txt_normal));
			}
		});
		tab_sticker.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				effect_listview.setVisibility(View.INVISIBLE);
				sticker_listview.setVisibility(View.VISIBLE);
//				tab_effect.setTextColor(getResources().getColor(R.color.camerasdk_txt_normal));
//				tab_sticker.setTextColor(getResources().getColor(R.color.camerasdk_txt_selected));
			}
		});
		txt_cropper.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 裁剪图片
				Constants.bitmap=((EfectFragment)fragments.get(current)).getCurrentBitMap();
				Intent intent = new Intent();
				intent.setClassName(getApplication(), "com.muzhi.camerasdk.CutActivity");				
				startActivityForResult(intent,Constants.RequestCode_Croper);
			}
		});
		btn_done.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 贴纸Tab
				complate();
				Log.e("TAG","完成");
			}
		});
		txt_enhance.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 添加文字
//				Constants.bitmap=((EfectFragment)fragments.get(current)).getCurrentBitMap();
//				Intent intent = new Intent();
//				intent.setClassName(getApplication(), "com.muzhi.camerasdk.PhotoEnhanceActivity");
//				startActivityForResult(intent,Constants.RequestCode_Croper);
//				Toast.makeText(FilterImageActivity.this,"开发当中",Toast.LENGTH_LONG).show();

				rl_edit_text.setVisibility(View.VISIBLE);
				content_container.setVisibility(View.GONE);
				layout_actionbar_root.setVisibility(View.GONE);
			}
		});
		/**/
		tv_close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.e("TAG","89898989898989898989");
				rl_edit_text.setVisibility(View.GONE);
				content_container.setVisibility(View.VISIBLE);
				layout_actionbar_root.setVisibility(View.VISIBLE);
				et_tag.setText("");
				tv_tag.setTextColor(Color.parseColor( "#FA0606" ));
				text_size.setProgress(14);
				colorbar.setWz(60);
			}
		});
		/*文字完成*/
		tv_finish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				rl_edit_text.setVisibility(View.GONE);
				content_container.setVisibility(View.VISIBLE);
				layout_actionbar_root.setVisibility(View.VISIBLE);
				if (et_tag.getText().length() > 0) {
					addTextToWindow(colorbar.getCurrentColor());
				}
			}
		});
		txt_graffiti.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 涂鸦
				Constants.bitmap=((EfectFragment)fragments.get(current)).getCurrentBitMap();
				Intent intent = new Intent();
				intent.putExtra("path", imageList.get(0));
				intent.setClassName(getApplication(), "com.muzhi.camerasdk.GraffitiActivity");				
				startActivityForResult(intent,Constants.RequestCode_Croper);
			}
		});
		
		effect_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				eAdapter.setSelectItem(arg2);

				final int tmpint = arg2;
				final int tmpitem = arg1.getWidth();
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						effect_listview.scrollTo(tmpitem * (tmpint - 1) - tmpitem / 4);
					}
				}, 200);

				Filter_Effect_Info info= effect_list.get(arg2);
				GPUImageFilter filter = ImageFilterTools.createFilterForType(mContext,info.getFilterType());
				((EfectFragment)fragments.get(current)).addEffect(filter);
			}
		});

		sticker_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				
				Filter_Sticker_Info info = stickerList.get(arg2);
				if(info.isLib()){
					Intent intent=new Intent(mContext,StickerActivity.class);
					startActivityForResult(intent, Constants.RequestCode_Sticker);
					Log.e("TAG","11111111111111"+info);
				}
				else{
					String path=stickerList.get(arg2).getLocal_path();
					int drawableId=stickerList.get(arg2).getDrawableId();
					((EfectFragment)fragments.get(current)).addSticker(drawableId, path,null,"IMAGE");
					Log.e("TAG",path+"===="+drawableId);
				}
				
			}
		});

		images_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				mViewPager.setCurrentItem(position,true);
				fragments.get(position).onResume();
				fragments.get(current).onPause();
				current=position;
				
				iAdapter.setSelected(position);
				final int tmpint = position;
				final int tmpitem = view.getWidth();
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						images_listview.scrollTo(tmpitem * (tmpint - 1) - tmpitem / 4);
					}
				}, 200);
				
			}
		});
		mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
				// TODO Auto-generated method stub
				/*if (mFilterAdjuster != null) {
					mFilterAdjuster.adjust(progress);
				}*/
				//effect_main.requestRender();
			}
		});
		
	}

	/*文字转bitmap*/
	private void addTextToWindow(int currentColor) {
		Log.e("TAG",et_tag.getText().toString()+"===="+tv_tag.getText().toString());
		tv_tag.setTextColor(currentColor);
		et_tag.setTextColor(currentColor);
		tv_tag.setTextSize(Float.parseFloat(tv_size.getText().toString()));
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(tv_tag.getWidth(), tv_tag.getHeight());
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		tv_tag.setLayoutParams(layoutParams);
		tv_tag.setDrawingCacheEnabled(true);
		tv_tag.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
				View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		Bitmap bitmap = Bitmap.createBitmap(tv_tag.getDrawingCache());

		((EfectFragment)fragments.get(current)).addSticker(0, "",bitmap,"BITMAP");
		et_tag.setText("");
		tv_tag.setTextColor(Color.parseColor( "#FA0606" ));
		text_size.setProgress(14);
		colorbar.setWz(60);
		tv_tag.setText("");

	}
	public Bitmap getNewBitMap(String text,TextView textView,int currentColor) {
		Bitmap newBitmap = Bitmap.createBitmap(textView.getWidth(),textView.getHeight(), Bitmap.Config.ARGB_4444);
		Canvas canvas = new Canvas(newBitmap);
		canvas.drawBitmap(newBitmap, 0, 0, null);
		TextPaint textPaint = new TextPaint();
		textPaint.setAntiAlias(true);
		textPaint.setTextSize(Float.parseFloat(tv_size.getText().toString())*6);
		textPaint.setColor(currentColor);
		StaticLayout sl= new StaticLayout(text, textPaint,
				newBitmap.getWidth()-8, Layout.Alignment.ALIGN_CENTER,
				1.0f, 0.0f, false);
		canvas.translate(6, 40);
		sl.draw(canvas);
		return newBitmap;
	}
	private void initData(){		
        
		boolean flag=false;
		if(mCameraSdkParameterInfo.isSingle_mode() && mCameraSdkParameterInfo.isCroper_image()){
			flag=true;
		}
		
		fragments=new ArrayList<Fragment>();
		for(int i=0;i<imageList.size();i++){
			EfectFragment ef1=EfectFragment.newInstance(imageList.get(i),flag);
			fragments.add(ef1);
		}

		fAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), mViewPager,fragments);
		mViewPager.setAdapter(fAdapter);

		//mViewPager.setOffscreenPageLimit(imageList.size());
		mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			//position当前页面，positionOffset当前页面偏移百分比，positionOffsetPixels当前页面偏移像素
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			//当前页面跳转另一个页面完毕调用
			@Override
			public void onPageSelected(int position) {
				Log.e("TAG",position+"");
				tv_title.setText((position+1)+"/"+imageList.size());
				mViewPager.setCurrentItem(position,true);
				fragments.get(position).onResume();
				fragments.get(current).onPause();
				current=position;

				iAdapter.setSelected(position);
				final int tmpint = position;
				final int tmpitem = images_listview.getWidth();
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						images_listview.scrollTo(tmpitem * (tmpint - 1) - tmpitem / 4);
					}
				}, 200);

			}
			//state ==1的时表示正在滑动，state==2的时表示滑动完毕了，state==0的时表示什么都没做。
			@Override
			public void onPageScrollStateChanged(int state) {
				Log.e("TAG",state+"");
			}
		});

		effect_list=FilterUtils.getEffectList();
		stickerList=FilterUtils.getStickerList();
		
		iAdapter = new SmallThumbAdapter(mContext, imageList);
		eAdapter = new Filter_Effect_Adapter(this,effect_list);
		sAdapter = new Filter_Sticker_Adapter(this, stickerList);
		
		images_listview.setAdapter(iAdapter);
		iAdapter.setSelected(0);
		effect_listview.setAdapter(eAdapter);
		sticker_listview.setAdapter(sAdapter);

		tv_title.setText((num+1)+"/"+imageList.size());
		mViewPager.setCurrentItem(num);
		mViewPager.setCurrentItem(num,true);
		fragments.get(num).onResume();
		fragments.get(current).onPause();
		current=num;

		iAdapter.setSelected(num);
		final int tmpint = num;
		final int tmpitem = images_listview.getWidth();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				images_listview.scrollTo(tmpitem * (tmpint - 1) - tmpitem / 4);
			}
		}, 200);
		
	}


	private void complate(){
        
		loading_layout.setVisibility(View.VISIBLE);
		complate_runnable(3*1000);
		
	}
	
	private void complate_runnable(long delayMillis) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				ArrayList<String> list=new ArrayList<String>();
				if(mCameraSdkParameterInfo.getRet_type()==0){
					//返回一个路径
					for(int i=0;i<imageList.size();i++){
						Fragment mFragment=fragments.get(i);
						if(mFragment.isAdded()){
							String path=((EfectFragment)fragments.get(i)).getFilterImage();
							list.add(path);
						}
						else{
							list.add(imageList.get(i));
						}
					}
					Log.e("TAG=========",imageList.toString());
//					EventBus.getDefault().post(MessageWrap.getInstance(imageList));
				} else{
					//保存bitmap
					CameraSdkParameterInfo.bitmap_list.clear();
					for(int i=0;i<imageList.size();i++){
						Fragment mFragment=fragments.get(i);
						if(mFragment.isAdded()){
							Bitmap bitmap=((EfectFragment)fragments.get(i)).getFilterBitmap();
							CameraSdkParameterInfo.bitmap_list.add(bitmap);
						}
					}
				}
				
				//如果是网络图片则直接返回
				if(mCameraSdkParameterInfo.is_net_path()){

					Bundle b=new Bundle();
					b.putSerializable(CameraSdkParameterInfo.EXTRA_PARAMETER, mCameraSdkParameterInfo);

					Intent intent = new Intent();
					intent.putExtras(b);
					setResult(RESULT_OK, intent);
			        finish();
				}
				else{
					Log.e("TAG++++++++++++",list.toString());
					EventBus.getDefault().post(MessageWrap.getInstance(list));
					try {
						PhotoPickActivity.instance.getFilterComplate(list);
					}catch (Exception e){
							Log.e("TAG","123123123123123123123123123123123");
					}

				}
				finish();
				
			}
		};
		Handler handler = new Handler();
		handler.postDelayed(runnable, delayMillis);		
	}
	
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Constants.RequestCode_Croper) {
			//截图返回
			((EfectFragment)fragments.get(current)).setBitMap();
		}
		else if(resultCode == Constants.RequestCode_Sticker){
			if(data!=null){
				Filter_Sticker_Info info=(Filter_Sticker_Info)data.getSerializableExtra("info");
				((EfectFragment)fragments.get(current)).addSticker(0, info.getImage(),null,"IAMGE");
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}


	@Override
	public void colorChange(int color) {
		et_tag.setTextColor(color);
	}
}
