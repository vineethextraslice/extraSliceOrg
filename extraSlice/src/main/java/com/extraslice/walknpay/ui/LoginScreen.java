package com.extraslice.walknpay.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.extraslice.LoginActivity;
import com.app.extraslice.R;
import com.extraslice.walknpay.bl.UserBO;
import com.extraslice.walknpay.bl.Utilities;
import com.extraslice.walknpay.dao.ProgressClass;

import org.codehaus.jettison.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginScreen extends Activity implements OnClickListener {

	Button login, register, ok_but;
	EditText username, password, verification_code;
	String user1 = "user";
	Intent intent;
	Dialog dialog,upgradePopup;
	LayoutInflater inflater;
	View forgot_view;
	ActionBar mactionBar;
	TextView message;
	View mainLogin;
	
	TextView signup, resendEmail;
	LinearLayout verification;
	TextView actionbartitle, associate, forgot_password;
	LinearLayout errorImage;
	Context mContext;
	TextView waringMessage,waringMessage2,skipDownload;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
	
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		requestWindowFeature(Window.FEATURE_ACTION_MODE_OVERLAY);
		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		mactionBar = getActionBar();
		mactionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		View customActionBarView = getLayoutInflater().inflate(R.layout.custom_actionbar, null);
		mactionBar.setCustomView(customActionBarView,new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,ActionBar.LayoutParams.WRAP_CONTENT));
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		mactionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		setContentView(R.layout.wnp_login);
		mactionBar.show();

		
		
		/*dialog = new Dialog(LoginScreen.this,android.R.style.Theme_Translucent_NoTitleBar);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		Window window = dialog.getWindow();
		window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);*/
		
		
		
		upgradePopup = new Dialog(mContext);
		upgradePopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
		upgradePopup.setContentView(R.layout.upgrade_alert_screen);
		upgradePopup.setCancelable(false);
		Window window = upgradePopup.getWindow();
		window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		waringMessage = (TextView)upgradePopup.findViewById(R.id.warningMessage);
		waringMessage2 = (TextView)upgradePopup.findViewById(R.id.warningMessage2);
		Button downlaodBtn = (Button)upgradePopup.findViewById(R.id.download);
		skipDownload = (TextView)upgradePopup.findViewById(R.id.skip);
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
				intent = new Intent(LoginScreen.this,MenuActivity.class);
				startActivity(intent);
				finish();
				
			}
		});

		mainLogin = findViewById(R.id.mainLogin);
		verification = (LinearLayout) findViewById(R.id.verificationLayout);
		resendEmail = (TextView) findViewById(R.id.resendEmail);
		errorImage = (LinearLayout) findViewById(R.id.textLinear);
		username = (EditText) findViewById(R.id.email_edit);
		verification_code = (EditText) findViewById(R.id.user_verification);
		password = (EditText) findViewById(R.id.pass_edit);
		login = (Button) findViewById(R.id.login_button);
		signup = (TextView) findViewById(R.id.register_button);
		forgot_password = (TextView) findViewById(R.id.forgot_button);
		message = (TextView) findViewById(R.id.text);
		TextView eSliceHome = (TextView) findViewById(R.id.eSliceHome);
		eSliceHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginScreen.this,LoginActivity.class);
				startActivity(intent);
				finish();
			}
		});

		signup.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		if(verification_code.getVisibility()==View.VISIBLE){
			verification_code.setVisibility(View.GONE);
		}

		login.setOnClickListener(this);
		signup.setOnClickListener(this);
		forgot_password.setOnClickListener(this);
		mainLogin.setOnClickListener(this);
		resendEmail.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String userName = username.getText().toString();
				String passwordString = password.getText().toString();
				if (userName.length() == 0 || passwordString.length() == 0) {
					if (userName.length() == 0) {
						errorImage.setVisibility(View.VISIBLE);
						message.setText("Enter username");
					} else if (passwordString.length() == 0) {
						errorImage.setVisibility(View.VISIBLE);
						message.setText("Enter password");
					} else if (userName.length() == 0
							|| passwordString.length() == 0) {
						errorImage.setVisibility(View.VISIBLE);
						message.setText("Enter username and password");
					}
				} else {
					new ResendData(userName, passwordString,Utilities.loggedInUser.getUserId()).execute();
				}

			}
		});
		username.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.setFocusable(true);
				v.setFocusableInTouchMode(true);
				return false;
			}
		});
		password.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.setFocusable(true);
				v.setFocusableInTouchMode(true);
				return false;
			}
		});
		verification_code.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.setFocusable(true);
				v.setFocusableInTouchMode(true);
				return false;
			}
		});

		SharedPreferences sp = getSharedPreferences("LoginDetail",	Context.MODE_PRIVATE);
		if (sp.getBoolean("data", false)) {

			final String user = sp.getString("user", "");
			if (!user.equals("")) {
				username.setText(user);
				((CheckBox) findViewById(R.id.saveCred)).setChecked(true);
			}
			final String pass = sp.getString("pass", "");
			if (!pass.equals("")) {
				password.setText(pass);
				((CheckBox) findViewById(R.id.saveCred)).setChecked(true);
			}

		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		
		super.onDestroy();
	}

	
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.mainLogin:
			hideVirtualKeyBoard();
			break;
		case R.id.login_button:
			new LoadData(username.getText().toString(), password.getText().toString()).execute();
			break;
///UPTO HERE
		case R.id.register_button:

			if (errorImage.getVisibility() == View.VISIBLE) {
				errorImage.setVisibility(View.INVISIBLE);
				message.setText("");
			}

			/*username.setText("");
			password.setText("");*/
			if(verification_code.getVisibility()==View.VISIBLE)
			{
				verification_code.setVisibility(View.GONE);
				verification.setVisibility(View.GONE);
			}

			intent = new Intent(LoginScreen.this, RegisterScreen.class);
			startActivity(intent);
			hideVirtualKeyBoard();
			break;

		case R.id.forgot_button:

			intent = new Intent(LoginScreen.this, Forgot.class);
			startActivity(intent);
			hideVirtualKeyBoard();
			break;

		default:
			break;
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

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			dialog = new Dialog(this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.wnp_custom_alert);

			// set the custom dialog components - text, image and button
			TextView text = (TextView) dialog.findViewById(R.id.title_view);
			text.setText(R.string.loginalertmessage);

			// image.setImageResource(R.drawable.ic_launcher);

			Button dialogButton = (Button) dialog.findViewById(R.id.yes_button);
			// if button is clicked, close the custom dialog
			dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					System.exit(0);
				}
			});
			Button negativebutton = (Button) dialog
					.findViewById(R.id.no_button);
			// if button is clicked, close the custom dialog
			negativebutton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			dialog.show();

		}
		return super.onKeyDown(keyCode, event);
	}
	
	class LoadData extends AsyncTask<Void, Void, Void> {
		String errMsg = null;
		String userName;
		String pwd;
		UserBO uesrBo;
		public LoadData(String userName,String pwd) {
			this.userName=userName;
			this.pwd=pwd;
		}
		@Override
		protected void onPreExecute() {	
			
			message.setText("");
			errorImage.setVisibility(View.INVISIBLE);
			
			ProgressClass.startProgress(mContext);
			uesrBo = new UserBO(mContext);
			super.onPreExecute();

		}
		@Override
		protected Void doInBackground(Void... arg0) {
			if(userName.length()==0) {
				errMsg = "Enter Email!";
			}else if(pwd.length()==0)
			{
				errMsg = "Enter Password!";
			}else if(userName.length()!=0 && pwd.length()!=0) {
				if (!userName.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")) {
					errMsg = "Invalid email!";
				}else{
					int osVersion = android.os.Build.VERSION.SDK_INT;
					String appVersion = getResources().getString(R.string.version);
					Utilities.loggedInUser = uesrBo.authenticateUserWithMoreDetails(userName, pwd, osVersion+"", appVersion, "Android");
				}
			}
			

			hideVirtualKeyBoard();
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			if(errMsg != null ){
				errorImage.setVisibility(View.VISIBLE);
				message.setText(errMsg);
			}else if (Utilities.loggedInUser != null) {
				String code = Utilities.loggedInUser.getVerificationCode();
				if (code != null) {
					verification_code.setVisibility(View.VISIBLE);
					verification.setVisibility(View.VISIBLE);
					if (verification.getVisibility() == View.VISIBLE) {
						if (code.length() == 0) {
							errorImage.setVisibility(View.VISIBLE);
							message.setText("Enter verification code to continue login");
							return;
						}
						String codeServer = Utilities.loggedInUser.getVerificationCode();
						if (!(code.equals(codeServer))) {
							errorImage.setVisibility(View.VISIBLE);
							message.setText("Verification code doesn't match");
							return;
						} else {
							Utilities.loggedInUser.setVerificationCode(null);
							new UpdateUserData().execute();
						}
					}
				}				
				//isInternetPresent = cd.isConnectingToInternet();
				if (Utilities.loggedInUser != null) {
					Utilities.islogged = true;
					Utilities.setCredentials(userName, pwd,LoginScreen.this);
					String id = username.getText().toString().trim();
					id = id.replace(" ", "");
					String pass = password.getText().toString().trim();
					pass = pass.replace(" ", "");
					final CheckBox uchk = (CheckBox) findViewById(R.id.saveCred);
					SharedPreferences sp = getSharedPreferences("LoginDetail",Context.MODE_PRIVATE);
					Editor editor = sp.edit();
					if (uchk.isChecked()) {
						editor.putBoolean("data", true);
						editor.putString("user", id);
						editor.putString("pass", pass);
					} else {
						editor.putString("user", "");
						editor.putString("pass", "");
					}
					editor.putBoolean("loginvalue", true);
					editor.commit();
					if(Utilities.WARNING_FROM_SERVER == null || Utilities.WARNING_FROM_SERVER.trim().equals("")){
						intent = new Intent(LoginScreen.this,MenuActivity.class);
						startActivity(intent);
						finish();
					}else{
						SimpleDateFormat dispFormat = new SimpleDateFormat("dd-MMM-yyyy");
						SimpleDateFormat inFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm:sss");
						Date today =new Date();
						Date lastDat=null;
						String dispLastDat=null;
						String disptoday=null;
						
						try {
							lastDat = inFormat.parse(Utilities.WARNING_FROM_SERVER.trim());
							today = inFormat.parse(today.toString());
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
							dispLastDat = dispFormat.format(lastDat);
							disptoday = dispFormat.format(today);
						
						
						
						
						if(today != null && lastDat != null){
							if(today.compareTo(lastDat) >= 0){
								waringMessage.setText("This version of walkNpay became obsolete on "
										+(dispLastDat == null ? Utilities.WARNING_FROM_SERVER.trim():dispLastDat)+
										".Please upgrade to the latest version from Google Play Store.");
								waringMessage2.setVisibility(View.VISIBLE);
								skipDownload.setVisibility(View.GONE);
								waringMessage2.setText(	"Your device date is "+disptoday+", if this is incorrect," +
										" please update your device date setting and restart walkNpay");
							}else{
								waringMessage.setText("This version of walkNpay will become obsolete on "
										+(dispLastDat == null ? Utilities.WARNING_FROM_SERVER.trim():dispLastDat)+
										".Please upgrade to the latest version from Google Play Store.");
								waringMessage2.setVisibility(View.GONE);
								skipDownload.setVisibility(View.VISIBLE);
							}
						}
						
						
						upgradePopup.show();
					}
					
				} else {
					if (Utilities.errorMessage != null && Utilities.errorMessage.trim().length()>0){
						errorImage.setVisibility(View.VISIBLE);
						message.setText(Utilities.errorMessage);
					}

				}
			}else {
				if (Utilities.errorMessage != null && Utilities.errorMessage.trim().length()>0){
					errorImage.setVisibility(View.VISIBLE);
					message.setText(Utilities.errorMessage);
				}else{
					errorImage.setVisibility(View.VISIBLE);
					message.setText("Login Failed");
				}
			}
			
			super.onPostExecute(result);
			ProgressClass.finishProgress();
		}
	}

	class ResendData extends AsyncTask<Void, Void, Void> {
		String userName;
		String pwd;
		int userId;
		UserBO uesrBo;
		JSONObject resp ;
		public ResendData(String userName,String pwd,int userId) {
			this.userName=userName;
			this.pwd=pwd;
			this.userId = userId;
		}
		@Override
		protected void onPreExecute() {	
			message.setText("");
			errorImage.setVisibility(View.INVISIBLE);
			ProgressClass.startProgress(mContext);
			uesrBo = new UserBO(mContext);
			super.onPreExecute();

		}
		@Override
		protected Void doInBackground(Void... arg0) {
			resp = uesrBo.resendVerificationEmail(userName, pwd,userId);
			hideVirtualKeyBoard();
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			if (resp != null) {
				Toast.makeText(LoginScreen.this, "Verification code send to your mail", Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(LoginScreen.this, "Failed to resend verification code", Toast.LENGTH_LONG).show();
			}
			super.onPostExecute(result);
			ProgressClass.finishProgress();
		}
	}

	class UpdateUserData extends AsyncTask<Void, Void, Void> {
		
		@Override
		protected void onPreExecute() {	
			ProgressClass.startProgress(mContext);
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... arg0) {
			UserBO userBO = new UserBO(mContext);
			Utilities.loggedInUser = userBO.updateUser(Utilities.loggedInUser);					
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			ProgressClass.finishProgress();
			
		}
	}
}
