package com.extraslice.walknpay.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.extraslice.R;
import com.extraslice.walknpay.bl.UserBO;
import com.extraslice.walknpay.bl.Utilities;
import com.extraslice.walknpay.dao.ProgressClass;
import com.extraslice.walknpay.model.UserModel;

public class RegisterScreen extends Activity implements OnClickListener {
	Button submit, registerCancel;
	Intent intent;
	ActionBar mactionBar;
	TextView actionbartitle;
	Dialog dialog;
	EditText email, password, conPassword;
	Context context;
	TextView message;
	WebView	webView;
	LinearLayout errorImage;
	private static final int LONG_DELAY = 5500;
	// flag for Internet connection status
	Boolean isInternetPresent = false;
	CheckBox accept;
	View mainRegister;
	 View includedLayout;

	// Connection detector class
	ConnectionDetector cd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		requestWindowFeature(Window.FEATURE_ACTION_MODE_OVERLAY);
		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		mactionBar = getActionBar();
		mactionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		View customActionBarView = getLayoutInflater().inflate(R.layout.custom_actionbar, null);
		mactionBar.setCustomView(customActionBarView, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));
		mactionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		setContentView(R.layout.register);
		mactionBar.show();
		includedLayout = findViewById(R.id.webviewterms);
		TextView termsofuse = (TextView) findViewById(R.id.termsofuseText);
		
		webView = (WebView) findViewById(R.id.webviewterms);
		webView.setWebViewClient(new MyWebViewClient());
		openURL();
		termsofuse.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				includedLayout.setVisibility(View.VISIBLE);
			}
		});
		cd = new ConnectionDetector(getApplicationContext());
		context = this;

		dialog = new Dialog(RegisterScreen.this, android.R.style.Theme_Translucent_NoTitleBar);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		Window window = dialog.getWindow();
		window.setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

		mainRegister = findViewById(R.id.mainRegister);

		submit = (Button) findViewById(R.id.submit);
		registerCancel = (Button) findViewById(R.id.registerCancel);
		email = (EditText) findViewById(R.id.user_name);
		password = (EditText) findViewById(R.id.password);
		conPassword = (EditText) findViewById(R.id.payment_password);
		message = (TextView) findViewById(R.id.text);
		errorImage = (LinearLayout) findViewById(R.id.textLinear);
		accept = (CheckBox) findViewById(R.id.acceptTNC);

		final String UserName = email.getText().toString();

		submit.setOnClickListener(this);
		registerCancel.setOnClickListener(this);
		mainRegister.setOnClickListener(this);
	}
	 private class MyWebViewClient extends WebViewClient {
         @Override
         public boolean shouldOverrideUrlLoading(WebView view, String url) {
             view.loadUrl(url);
             return true;
         }
   }
	 private void openURL() {
		 webView.loadUrl("file:///android_asset/termsofuse.html");
		 webView.requestFocus();
	    }
	@Override
	protected void onPause() {
		super.onPause();
		Utilities.classname = this.getClass().getName();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    // Check if the key event was the Back button and if there's history
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	      if(includedLayout.getVisibility()==View.VISIBLE)
	      {
	    	  includedLayout.setVisibility(View.GONE);
	    	  return true;
	      }
	        
	    }
	    // If it wasn't the Back key or there's no web page history, bubble up to the default
	    // system behavior (probably exit the activity)
	    return super.onKeyDown(keyCode, event);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.mainRegister:
			hideVirtualKeyBoard();
			break;

		case R.id.registerCancel:
			intent = new Intent(RegisterScreen.this, LoginScreen.class);
			startActivity(intent);
			finish();
			hideVirtualKeyBoard();
			break;

		case R.id.submit:

			final String userName = email.getText().toString();
			final String pwd = password.getText().toString();
			final String confPwd = conPassword.getText().toString();
			if (userName.length() == 0 || pwd.length() == 0 || confPwd.length() == 0) {
				if (userName.length() == 0 || userName.equalsIgnoreCase("") || userName.equalsIgnoreCase(null)) {
					errorImage.setVisibility(View.VISIBLE);
					message.setText("Enter Email!");
				}else if (pwd.length() == 0 || pwd.equalsIgnoreCase("") || pwd.equalsIgnoreCase(null)) {
					errorImage.setVisibility(View.VISIBLE);
					message.setText("Enter Password!");
				}
				else if (confPwd.length() == 0 || confPwd.equalsIgnoreCase("") || confPwd.equalsIgnoreCase(null)) {
					errorImage.setVisibility(View.VISIBLE);
					message.setText("Enter Confirm Password!");
				}
			}else if(accept.isChecked()==false){
				errorImage.setVisibility(View.VISIBLE);
				message.setText("Inorder to complete registration you need to accept terms and conditions");
			} else {
				if (!userName.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")) {
					errorImage.setVisibility(View.VISIBLE);
					message.setText("Invalid email!");
					return;
				}
				if (pwd.equals(confPwd)) {
					new LoadData(userName, pwd).execute();
				} else {
					errorImage.setVisibility(View.VISIBLE);
					message.setText("Password do not match!");

				}

			}

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
	
	class LoadData extends AsyncTask<Void, Void, Void> {
		String errMsg = null;
		String userName;
		String pwd;
		UserModel registerUser;
		UserBO uesrBo;
		public LoadData(String userName,String pwd) {
			this.userName=userName;
			this.pwd=pwd;
		}
		@Override
		protected void onPreExecute() {	
			
			message.setText("");
			errorImage.setVisibility(View.INVISIBLE);
			
			ProgressClass.startProgress(context);
			uesrBo = new UserBO(context);
			super.onPreExecute();

		}
		@Override
		protected Void doInBackground(Void... arg0) {
			UserBO uesrBo = new UserBO(context);
			registerUser = uesrBo.registerUser(userName, pwd);
			hideVirtualKeyBoard();
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			if (registerUser != null) {
				Toast toast = Toast.makeText(context, "Sucessfully Registered.", Toast.LENGTH_SHORT);
				toast.show();
				Toast toast2 = Toast.makeText(context, "We have send you a verification email. " +
				"Please do the verification inorder to login into the application", LONG_DELAY);
				toast2.show();
				finish();
			} else {
				if (Utilities.errorMessage.contains("Error while inserting User Details")) {
					errorImage.setVisibility(View.VISIBLE);
					message.setText("This email is already registered");
				} else {
					errorImage.setVisibility(View.VISIBLE);
					message.setText(Utilities.errorMessage);
				}
			}
			super.onPostExecute(result);
			ProgressClass.finishProgress();
		}
	}
}
