package com.app.extraslice;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.app.extraslice.bo.SmartspaceBO;
import com.app.extraslice.bo.UserBO;
import com.app.extraslice.model.PlanModel;
import com.app.extraslice.model.PlanOfferModel;
import com.app.extraslice.model.ResourceTypeModel;
import com.app.extraslice.model.UserModel;
import com.app.extraslice.utils.CustomException;
import com.app.extraslice.utils.ProgressClass;
import com.app.extraslice.utils.Utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends Activity {
	ImageView img1, img2;
	Context mContext;
	int[] imageArray = new int[2];
	int currImg = 0;
	int currAnim = 0;
	Animation[] animArray = new Animation[2];
	int animx1;
	Animation animation1, animation2;
	Handler handler;
	LinearLayout errorLyt;
	TextView errorText;
	Runnable animationRunner;
	SharedPreferences sharedPref;
	CheckBox saveCred;
	AutoCompleteTextView userNameEdtTxt;
	EditText passwordEdtTxt;
	Dialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mContext = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		//getActionBar().hide();
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		float scale = mContext.getResources().getDisplayMetrics().density;
		saveCred = (CheckBox) findViewById(R.id.saveCred);
		sharedPref = getSharedPreferences("LoginDetail", Context.MODE_PRIVATE);
		imageArray[0] = mContext.getResources().getIdentifier("image_1","drawable", "com.app.extraslice");
		imageArray[1] = mContext.getResources().getIdentifier("conf_room","drawable", "com.app.extraslice");
		img1 = (ImageView) findViewById(R.id.image1);
		img2 = (ImageView) findViewById(R.id.image2);
		SmartspaceBO.planList = new ArrayList<PlanModel>(1);
		SmartspaceBO.addonList = new ArrayList<ResourceTypeModel>(1);
		SmartspaceBO.offerList = new ArrayList<PlanOfferModel>(1);
		SmartspaceBO.accountModel =null;
		/*float width = display.getWidth() *scale;
		float height =width*577/730;
		height = ((display.getHeight())*scale-height)<(320 *scale)?(display.getHeight()-320)*scale : height*scale;
		RelativeLayout imgLyt = (RelativeLayout)findViewById(R.id.imgLyt);
		RelativeLayout loginLyt = (RelativeLayout)findViewById(R.id.loginLyt);
		
		RelativeLayout.LayoutParams imgLytParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,  (int)(height));
		imgLytParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		imgLytParams.addRule(RelativeLayout.ABOVE, R.id.loginLyt);
		
		RelativeLayout.LayoutParams imgParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,  (int)(height));
		imgParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		imgLyt.setLayoutParams(imgLytParams); 
		img1.setLayoutParams(imgParams); 
		img2.setLayoutParams(imgParams); 
		 
		RelativeLayout.LayoutParams lgnLytParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(int)(320*scale));
		lgnLytParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		
		loginLyt.setLayoutParams(lgnLytParams); 	
		*/
		errorLyt = (LinearLayout) findViewById(R.id.errorLyt);
		errorText = (TextView) findViewById(R.id.errorText);
		currImg = 0;
		userNameEdtTxt = (AutoCompleteTextView) findViewById(R.id.userName);
		passwordEdtTxt = (EditText) findViewById(R.id.password);
		/*String[] countries = new String[] { "anoop@extraslice.com","anumol@extraslice.com", "irshad@extraslice.com","binur@extraslcie.com" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, countries);
		userNameEdtTxt.setAdapter(adapter);
		*/String userName = sharedPref.getString("userName", "");
		String password = sharedPref.getString("password", "");
		if (userName != null && !userName.trim().equals("")) {
			saveCred.setChecked(true);
		}
		if (saveCred.isChecked()) {
			userNameEdtTxt.setText(userName);
			passwordEdtTxt.setText(password);
		}

		passwordEdtTxt.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus){
					errorLyt.setVisibility(View.GONE);
					errorText.setText("");
				}else{
					hideVirtualKeyBoard();
				}
					
			}
		});
		Button login_button = (Button) findViewById(R.id.login_button);
		login_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new RunInBackground(userNameEdtTxt.getText().toString(),passwordEdtTxt.getText().toString()).execute();
			}
		});
		TextView forgotPwd = (TextView) findViewById(R.id.forgot_button);
		Button signup = (Button) findViewById(R.id.register_button);
		signup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,SignupActivity.class);
				startActivity(intent);
				finish();
			}
		});
		Button guestSignup = (Button) findViewById(R.id.guestSignup);
		guestSignup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,GustSignupActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		forgotPwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
				startActivity(intent);
				finish();
			}
		});
		/*animation1 = new TranslateAnimation(0, 1000, 0, 0);
		animx1 = 0;
		animation1.setDuration(2000);
		// anim1.setRepeatCount(-1);
		animation1.setFillBefore(true);
		animation2 = new TranslateAnimation(-1000, 0, 0, 0);
		animation2.setDuration(2000);
		animation2.setFillBefore(true);
		// anim2.setRepeatCount(-1);
		animArray[0] = animation1;
		animArray[1] = animation2;
		img1.setVisibility(View.GONE);
		handler = new Handler();
		Runnable run = new AnimationRunner();
		handler.postDelayed(run, 9000);*/

	}

	/*private class AnimationRunner implements Runnable {
		@Override
		public void run() {

			img2.clearAnimation();
			img1.clearAnimation();
			if (animx1 == 0) {
				animx1 = -1000;

				//
				animation1 = new TranslateAnimation(-1000, 0, 0, 0);
				animation2 = new TranslateAnimation(0, 1000, 0, 0);
				animation2.setStartOffset(800);
				animation2.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						img1.setVisibility(View.VISIBLE);
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						img2.setVisibility(View.GONE);
						img1.setX(0);

					}
				});
			} else {
				animx1 = 0;

				animation1 = new TranslateAnimation(0, 1000, 0, 0);
				animation2 = new TranslateAnimation(-1000, 0, 0, 0);
				animation1.setStartOffset(800);
				animation1.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						img2.setVisibility(View.VISIBLE);
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						img1.setVisibility(View.GONE);
						img2.setX(0);
					}
				});
			}
			animation1.setDuration(4000);
			animation1.setFillBefore(true);
			animation2.setDuration(4000);

			animation2.setFillBefore(true);
			img2.startAnimation(animation2);
			img1.startAnimation(animation1);
			Runnable run = new AnimationRunner();
			handler.postDelayed(run, 9000);
		}

	}*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}



	class RunInBackground extends AsyncTask<Void, Void, Void> {
		String errMsg = null;
		String userName;
		String pwd;
		UserBO uesrBo;

		public RunInBackground(String userName, String pwd) {
			this.userName = userName;
			this.pwd = pwd;
		}

		@Override
		protected void onPreExecute() {
			errorText.setText("");
			errorLyt.setVisibility(View.GONE);
			ProgressClass.startProgress(mContext);
			uesrBo = new UserBO(mContext);
			super.onPreExecute();
			hideVirtualKeyBoard();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			if (userName.length() == 0) {
				errMsg = "Enter Email!";
			} else if (pwd.length() == 0) {
				errMsg = "Enter Password!";
			} else if (userName.length() != 0 && pwd.length() != 0) {
				Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");

				String email = userName;

				Matcher matcher = pattern.matcher(email);

				boolean matchFound = matcher.matches();
				if (!matchFound) {
					errMsg = "Invalid email!";
				} else {
					int osVersion = android.os.Build.VERSION.SDK_INT;
					String appVersion = getResources().getString(
							R.string.version);
					try {
						Utilities.loggedInUser = uesrBo.authenticateUser(userName, pwd, osVersion + "", appVersion,"Android");
					} catch (CustomException e) {
						errMsg = e.getMessage();
					}
				}
			}

			return null;
		}





		@Override
		protected void onPostExecute(Void result) {
			if (errMsg != null) {
				errorText.setText(errMsg);
				errorLyt.setVisibility(View.VISIBLE);
			} else if (Utilities.loggedInUser != null) {
				if (saveCred.isChecked()) {
					Editor editor = sharedPref.edit();
					editor.putString("userName", userName);
					editor.putString("password", pwd);
					editor.commit();
				}
				if(Utilities.loggedInUser.getVerificationCode() != null 
						&& !Utilities.loggedInUser.getVerificationCode().trim().isEmpty()){
					showVerificationPopup();
				}else if (Utilities.loggedInUser.isUsingTempPwd()){
					showResetPasswordPopup();
				}else if(Utilities.WARNING_FROM_SERVER != null && !Utilities.WARNING_FROM_SERVER.trim().isEmpty()){
					upgradeApp(Utilities.WARNING_FROM_SERVER.trim());
				}else{
					Intent intent = new Intent(LoginActivity.this,
							MenuActivity.class);
					startActivity(intent);
					finish();
				}
			}
			ProgressClass.finishProgress();
			super.onPostExecute(result);
			// ProgressClass.finishProgress();
		}
	}

	private void hideVirtualKeyBoard() {

		try {
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void showResetPasswordPopup(){

		dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.password_reset_popup);
		final EditText newPwd = (EditText)dialog.findViewById(R.id.newPassword);
		final EditText confNewPwd = (EditText)dialog.findViewById(R.id.confNewPassword);
		final TextView popupErrorText = (TextView)dialog.findViewById(R.id.errorText);
		final LinearLayout popupErrorLyt = (LinearLayout)dialog.findViewById(R.id.errorLyt);
		Button okButton = (Button) dialog.findViewById(R.id.submit);
		// if button is clicked, close the custom dialog
		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(newPwd.getText().toString().trim().length() == 0){
					popupErrorText.setText("Please enter new password");
					popupErrorLyt.setVisibility(View.VISIBLE);
				}else if(confNewPwd.getText().toString().trim().length() == 0){
					popupErrorText.setText("Please confirm new password");
					popupErrorLyt.setVisibility(View.VISIBLE);
				}else if(newPwd.getText().toString().equals(confNewPwd.getText().toString())){
					try {
						Utilities.loggedInUser.setPassword(Utilities.encode(newPwd.getText().toString()));
						Utilities.loggedInUser.setTempPassword(null);
						new UpdateUserData(popupErrorLyt,popupErrorText).execute();
					} catch (Exception e) {
						popupErrorText.setText(e.getMessage());
						popupErrorLyt.setVisibility(View.VISIBLE);
					}
					
				}else{
					popupErrorText.setText("Password and confirm password does not match");
					popupErrorLyt.setVisibility(View.VISIBLE);
				}
			}
		});
		//Button cancelButton = (Button) dialog.findViewById(R.id.no_button);
		// if button is clicked, close the custom dialog
		/*cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});*/

		dialog.show();
	
	}
	
	private void showVerificationPopup(){

		final Context context = LoginActivity.this;
		dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.user_verification_popup);
		final EditText verifyCode = (EditText)dialog.findViewById(R.id.verifyCode);
		final TextView popupErrorText = (TextView)dialog.findViewById(R.id.errorText);
		final LinearLayout popupErrorLyt = (LinearLayout)dialog.findViewById(R.id.errorLyt);
		final TextView resendVCode = (TextView)dialog.findViewById(R.id.resendVCode);
		resendVCode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				errorText.setText("");
				errorLyt.setVisibility(View.GONE);
				new ResendVCode(Utilities.loggedInUser, popupErrorText, popupErrorLyt).execute();
				
				
			}
		});
		Button okButton = (Button) dialog.findViewById(R.id.submit);
		// if button is clicked, close the custom dialog
		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				errorText.setText("");
				errorLyt.setVisibility(View.GONE);
				if(verifyCode.getText().toString().equals(Utilities.loggedInUser.getVerificationCode())){
					Utilities.loggedInUser.setVerificationCode(null);
					new UpdateUserData(popupErrorLyt,popupErrorText).execute();
				}else{
					popupErrorText.setText("invalid verification code");
					popupErrorLyt.setVisibility(View.VISIBLE);
				}
				
				
			}
		});
		//Button cancelButton = (Button) dialog.findViewById(R.id.no_button);
		// if button is clicked, close the custom dialog
		/*cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});*/

		dialog.show();
	
	}
	class UpdateUserData extends AsyncTask<Void, Void, Void> {
		String errorMessage;
		LinearLayout errorLyt;
		TextView errorText;
		public UpdateUserData(LinearLayout errorLyt,TextView errorText){
			this.errorLyt = errorLyt;
			this.errorText =errorText ;
		}
		
		@Override
		protected void onPreExecute() {	
			ProgressClass.startProgress(mContext);
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... arg0) {
			UserBO userBO = new UserBO(mContext);
			try {
				Utilities.loggedInUser = userBO.updateUser(Utilities.loggedInUser);
			} catch (CustomException e) {
				errorMessage = e.getMessage();
			}					
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			ProgressClass.finishProgress();
			if(errorMessage != null){
				errorText.setText(errorMessage);
				errorLyt.setVisibility(View.VISIBLE);
			}else{
				dialog.dismiss();
				if(Utilities.WARNING_FROM_SERVER != null && !Utilities.WARNING_FROM_SERVER.trim().isEmpty()){
					upgradeApp(Utilities.WARNING_FROM_SERVER.trim());
				}else{
					Intent intent = new Intent(mContext, MenuActivity.class);
					startActivity(intent);
					finish();
				}
				
				
			}
			super.onPostExecute(result);
			
			
			
		}
	}
	
	private void upgradeApp(String deprDate){
		
		
		SimpleDateFormat dispFormat = new SimpleDateFormat("dd-MMM-yyyy");
		SimpleDateFormat inFormat = new SimpleDateFormat("MM/dd/yyy");
		Date today =new Date();
		Date lastDat=null;
		String dispLastDat=null;
		String disptoday=null;
		
		try {
			lastDat = inFormat.parse(deprDate);
			today = inFormat.parse(today.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
			dispLastDat = dispFormat.format(lastDat);
			disptoday = dispFormat.format(today);
		
		final Dialog upgradePopup = new Dialog(mContext);
		upgradePopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
		upgradePopup.setContentView(R.layout.upgrade_alert_screen);
		upgradePopup.setCancelable(false);
		Window window = upgradePopup.getWindow();
		window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		TextView waringMessage = (TextView)upgradePopup.findViewById(R.id.warningMessage);
		TextView waringMessage2 = (TextView)upgradePopup.findViewById(R.id.warningMessage2);
		Button downlaodBtn = (Button)upgradePopup.findViewById(R.id.download);
		TextView skipDownload = (TextView)upgradePopup.findViewById(R.id.skip);
		skipDownload.setClickable(true);
		downlaodBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				upgradePopup.dismiss();
				/*Intent intent = new Intent(mContext, WebViewActivity.class);
				intent.putExtra("URL", "http://google.com");
				startActivity(intent);*/
				
				final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
				try {
				    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
				} catch (android.content.ActivityNotFoundException anfe) {
				    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
				}
			}
		});
		
		skipDownload.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				upgradePopup.dismiss();
				Intent intent = new Intent(LoginActivity.this,MenuActivity.class);
				startActivity(intent);
				finish();
				
			}
		});
		
		if(today != null && lastDat != null){
			if(today.compareTo(lastDat) >= 0){
				waringMessage.setText("This version of extraSlice app became obsolete on "
						+(dispLastDat == null ?deprDate:dispLastDat)+
						".Please upgrade to the latest version from Google Play Store.");
				waringMessage2.setVisibility(View.VISIBLE);
				skipDownload.setVisibility(View.GONE);
				waringMessage2.setText(	"Your device date is "+disptoday+", if this is incorrect," +
						" please update your device date setting and restart extraSlice app");
			}else{
				waringMessage.setText("This version of extraSlice app will become obsolete on "
						+(dispLastDat == null ? deprDate:dispLastDat)+
						".Please upgrade to the latest version from Google Play Store.");
				waringMessage2.setVisibility(View.GONE);
				skipDownload.setVisibility(View.VISIBLE);
			}
		}
		
		
		upgradePopup.show();
	}
	
	
	class ResendVCode extends AsyncTask<Void, Void, Void> {
		String errMsg = null;
		UserModel uModel;
		UserBO uesrBo;
		TextView popupErrorText;
		LinearLayout popupErrorLyt;
		public ResendVCode(UserModel uModel,TextView popupErrorText, LinearLayout popupErrorLyt) {
			this.uModel = uModel;
			this.popupErrorText = popupErrorText;
			this.popupErrorLyt = popupErrorLyt;
		}

		@Override
		protected void onPreExecute() {
			errorText.setText("");
			errorLyt.setVisibility(View.GONE);
			ProgressClass.startProgress(mContext);
			uesrBo = new UserBO(mContext);
			super.onPreExecute();
			hideVirtualKeyBoard();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				Utilities.loggedInUser = uesrBo.resendVerificationCode(uModel);
			} catch (CustomException e) {
				errMsg = e.getMessage();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (errMsg != null) {
				popupErrorText.setText(errMsg);
				popupErrorLyt.setVisibility(View.VISIBLE);
			} else if (Utilities.loggedInUser != null) {
				Toast tst = Toast.makeText(mContext, "New verification code send to your email", Toast.LENGTH_LONG);
				tst.setGravity(Gravity.CENTER, 0, 0);
				tst.show();
			}
			ProgressClass.finishProgress();
			super.onPostExecute(result);
			// ProgressClass.finishProgress();
		}
	}

}
