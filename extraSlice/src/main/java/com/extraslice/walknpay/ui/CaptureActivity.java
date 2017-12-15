package com.extraslice.walknpay.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.app.extraslice.R;
import com.extraslice.walknpay.bl.Utilities;

import jim.h.common.android.lib.zxing.camera.PlanarYUVLuminanceSource;

public class CaptureActivity extends
		jim.h.common.android.lib.zxing.CaptureActivity {
	
	ImageView customView,cameraButton;
	Context mContext;
	int manual,camera_id;
	
	String sourceClass;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		mContext = this;
		PlanarYUVLuminanceSource.toast_shown=0;
		manual = intent.getIntExtra("MANUAL", R.id.manual_button1);
		camera_id = intent.getIntExtra("CAMERA", R.id.camera_button);
		sourceClass =  intent.getStringExtra("SOURCECLASS");
		String cambuttonString = "0";
		cambuttonString = intent.getStringExtra("CAMERA_ID");
		super.cameraId =   0;
		try{
			super.cameraId = Integer.parseInt(cambuttonString);
		}catch(Exception e){
			super.cameraId =   0;
		}
		customView = (ImageView) findViewById(manual);
		customView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				close();

			}
		});
		cameraButton= (ImageView) findViewById(camera_id);
		cameraButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				switchCamera();

			}
		});
		
	}
	public void close(){
		
		if(sourceClass!= null && sourceClass.equals("CART")){
			CartFragment frg = new CartFragment();
			CartFragment.manual(mContext);
		}
		this.onBackPressed();
	}
	public void switchCamera(){
		this.onBackPressed();
		if(Utilities.CAMERA_ID == 0){
			Utilities.CAMERA_ID =1;
		}else{
			Utilities.CAMERA_ID =0;
		}
		CustomScanner.initiateScan(((Activity)mContext), R.layout.zxinglib_capture, R.id.zxinglib_viewfinder_view, R.id.zxinglib_preview_view, true,R.id.manual_button1,sourceClass,Utilities.CAMERA_ID+"");
		
	}
}
