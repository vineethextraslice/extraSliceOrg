package com.app.extraslice.utils;

import com.app.extraslice.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;



public class ProgressClass {
	private static Dialog progressDialog;
	private static ImageView image;
	private static RotateAnimation anim;
	static{
		anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		anim.setInterpolator(new LinearInterpolator());
		anim.setRepeatCount(Animation.INFINITE); //Repeat animation indefinitely
		anim.setDuration(700); //Put desired d
	}
	public static void startProgress(Context mContext) {
		try {
			if (progressDialog == null) {
				progressDialog = new Dialog(mContext);
				progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				progressDialog.setContentView(R.layout.progress_dialog);
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

				image = (ImageView) progressDialog.findViewById(R.id.image);
				image.startAnimation(anim); 
				
			}
			//if(!progressDialog.isShowing()){
				progressDialog.show();
			//}
		} catch (Exception e) {
			Log.e("error progress",e.getLocalizedMessage());
		}
	}
	public static void bringToFront() {
		if (progressDialog != null) {
			
			if(progressDialog.isShowing()){
				if(image != null){
				image.bringToFront();
				}
			}
			
			progressDialog = null;
		}
	}
	public static void finishProgress() {
		try {
			if (progressDialog != null) {
				if(image != null){
					image.setAnimation(null);
				}
				if(progressDialog.isShowing()){
					progressDialog.dismiss();
				}
				
				progressDialog = null;
			}
		} catch (Exception e) {
			Log.e("error progress",e.getLocalizedMessage());
			if(image != null){
				image.setAnimation(null);
			}
			if (progressDialog != null) {
				progressDialog = null;
			}
		}
		
	}

}
