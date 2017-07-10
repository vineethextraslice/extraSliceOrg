package com.app.extraslice;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.extraslice.bo.SmartspaceBO;
import com.app.extraslice.bo.UserBO;
import com.app.extraslice.model.AdminAccountModel;
import com.app.extraslice.model.UserModel;
import com.app.extraslice.utils.CustomException;
import com.app.extraslice.utils.ProgressClass;
import com.app.extraslice.utils.Utilities;





public class GustSignupActivity extends Activity implements OnClickListener {
	Button submit, registerCancel;
	Intent intent;
	Dialog dialog;
	EditText email, password, conPassword;
	Context context;
	TextView errorText;
	WebView	webView;
	LinearLayout errorLyt;
	private static final int LONG_DELAY = 5500;
	// flag for Internet connection status
	Boolean isInternetPresent = false;
	CheckBox accept;
	View mainRegister;
	ImageView back;
	RelativeLayout page;
	AdminAccountModel adminAcctModel;
	// Connection detector class


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guest_signup);
		errorText = (TextView) findViewById(R.id.errorText);
		errorLyt = (LinearLayout) findViewById(R.id.errorLyt);
		getActionBar().hide();
		
		TextView termsofuse = (TextView) findViewById(R.id.termsofuseText);
		String redString = getResources().getString(R.string.termsncondition);
		termsofuse.setText(Html.fromHtml(redString));
		page = (RelativeLayout)findViewById(R.id.loginLyt);
		webView = (WebView) findViewById(R.id.webviewterms);
		back = (ImageView) findViewById(R.id.back);
		webView.setWebViewClient(new MyWebViewClient());
		openURL();
		termsofuse.setOnClickListener(new OnClickListener() {

			  @Override
			  public void onClick(View arg0) {
					LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    			final View webViewLyt = inflater.inflate(R.layout.web_view, null, false);
	    			WebView webView = (WebView) webViewLyt.findViewById(R.id.webView);
	    			webView.getSettings().setJavaScriptEnabled(true);
	    			webView.loadUrl(adminAcctModel.getTermsNCondUrl());
	    			webView.setWebViewClient(new WebViewClient() {
	    	             

	    	                @Override
	    	            public void onPageStarted(WebView view, String url, Bitmap favicon) {
	    	                super.onPageStarted(view, url, favicon);
	    	                ProgressClass.startProgress(context);

	    	            }

	    	                @Override
	    	                public boolean shouldOverrideUrlLoading(WebView view, String url)
	    	                {
	    	                    
	    	                    view.loadUrl(url);
	    	                    return true;

	    	                }

	    	               @Override
	    	               public void onPageFinished(WebView view, String url) {
	    	            	   ProgressClass.finishProgress();
	    	              }

	    	            });

	    			Button close = (Button)webViewLyt.findViewById(R.id.closeWV);
	    			page.addView(webViewLyt);
	    			close.setOnClickListener(new OnClickListener() {
	    				
	    				@Override
	    				public void onClick(View v) {
	    					page.removeView(webViewLyt);
	    				}
	    			});
			  }

			});

		context = this;
		new RunInBackground().execute();
		dialog = new Dialog(GustSignupActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		Window window = dialog.getWindow();
		window.setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

		mainRegister = findViewById(R.id.mainRegister);

		submit = (Button) findViewById(R.id.submit);
		registerCancel = (Button) findViewById(R.id.registerCancel);
		email = (EditText) findViewById(R.id.user_name);
		password = (EditText) findViewById(R.id.password);
		conPassword = (EditText) findViewById(R.id.payment_password);
		
		accept = (CheckBox) findViewById(R.id.acceptTNC);
		submit.setOnClickListener(this);
		registerCancel.setOnClickListener(this);
		mainRegister.setOnClickListener(this);
		mainRegister.setOnClickListener(this);
		back.setOnClickListener(this);
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
		com.extraslice.walknpay.bl.Utilities.classname = this.getClass().getName();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    // Check if the key event was the Back button and if there's history
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	      if(webView.getVisibility()==View.VISIBLE)
	      {
	    	  webView.setVisibility(View.GONE);
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
			intent = new Intent(GustSignupActivity.this, LoginActivity.class);
			startActivity(intent);
			finish();
			hideVirtualKeyBoard();
			break;
		case R.id.back:
			intent = new Intent(GustSignupActivity.this, LoginActivity.class);
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
					errorLyt.setVisibility(View.VISIBLE);
					errorText.setText("Enter Email!");
				}else if (pwd.length() == 0 || pwd.equalsIgnoreCase("") || pwd.equalsIgnoreCase(null)) {
					errorLyt.setVisibility(View.VISIBLE);
					errorText.setText("Enter Password!");
				}
				else if (confPwd.length() == 0 || confPwd.equalsIgnoreCase("") || confPwd.equalsIgnoreCase(null)) {
					errorLyt.setVisibility(View.VISIBLE);
					errorText.setText("Enter Confirm Password!");
				}
			}else if(accept.isChecked()==false){
				errorLyt.setVisibility(View.VISIBLE);
				errorText.setText("Inorder to complete registration you need to accept terms and conditions");
			} else {
				if (!userName.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")) {
					errorLyt.setVisibility(View.VISIBLE);
					errorText.setText("Invalid email!");
					return;
				}
				if (pwd.equals(confPwd)) {
					new LoadData(userName, pwd).execute();
				} else {
					errorLyt.setVisibility(View.VISIBLE);
					errorText.setText("Password do not match!");

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
			
			errorText.setText("");
			errorLyt.setVisibility(View.INVISIBLE);
			
			ProgressClass.startProgress(context);
			uesrBo = new UserBO(context);
			super.onPreExecute();

		}
		@Override
		protected Void doInBackground(Void... arg0) {
			UserBO uesrBo = new UserBO(context);
			UserModel model = new UserModel();
			model.setUserName(userName);
			model.setEmail(userName);
			try {
				model.setPassword(Utilities.encode(pwd));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String appVersion = getResources().getString(R.string.version);
			try {
				registerUser = uesrBo.addGuestUser(model,"Android",appVersion);
			} catch (CustomException e) {
				errMsg = e.getMessage();
			}
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
				intent = new Intent(GustSignupActivity.this, LoginActivity.class);
				startActivity(intent);
				finish();
				hideVirtualKeyBoard();
			} else {
				if (errMsg.contains("Error while inserting User Details")) {
					errorLyt.setVisibility(View.VISIBLE);
					errorText.setText("This email is already registered");
				} else {
					errorLyt.setVisibility(View.VISIBLE);
					errorText.setText(errMsg);
				}
			}
			super.onPostExecute(result);
			ProgressClass.finishProgress();
		}
	}
    class RunInBackground extends AsyncTask<Void, Void, Void> {
		String errMsg = null;
		SmartspaceBO smartSpaceBo;
		
		@Override
		protected void onPreExecute() {
			errorText.setText("");
			errorLyt.setVisibility(View.GONE);
			ProgressClass.startProgress(context);
			smartSpaceBo = new SmartspaceBO(context);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				smartSpaceBo.getSignupData();
			} catch (CustomException e) {
				errMsg = e.getLocalizedMessage();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (errMsg != null) {
				errorText.setText(errMsg);
				errorLyt.setVisibility(View.VISIBLE);
			} else {
				
				adminAcctModel = smartSpaceBo.accountModel;
				
			}
			
			ProgressClass.finishProgress();
			super.onPostExecute(result);
		}
	}
    
}
