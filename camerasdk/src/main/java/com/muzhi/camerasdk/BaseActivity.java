package com.muzhi.camerasdk;



import com.muzhi.camerasdk.library.utils.MResource;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;


public abstract class BaseActivity extends FragmentActivity{
	
	protected Context mContext;
	protected TextView btn_back,mActionBarTitle;
	protected ImageView imgBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//不可横屏幕
	}
	
	/*@TargetApi(19) 
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}*/
	
	
	
	public void setActionBarTitle(String s) {
		if(mActionBarTitle==null){
			mActionBarTitle=(TextView)findViewById(MResource.getIdRes(mContext, "camerasdk_actionbar_title"));
		}
		mActionBarTitle.setText(s);
	}
	
	//显示返回按钮
	public void showLeftIcon(){
		if(btn_back==null||imgBack==null){
			btn_back=(TextView)findViewById(MResource.getIdRes(mContext, "camerasdk_btn_back"));
			imgBack=(ImageView) findViewById(MResource.getIdRes(mContext, "img_back"));
		}
		imgBack.setVisibility(View.VISIBLE);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				finish();

			}
		});
	}

	
}
