package com.extraslice.walknpay.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.extraslice.R;
import com.extraslice.walknpay.bl.UserBO;
import com.extraslice.walknpay.bl.Utilities;
import com.extraslice.walknpay.dao.ProgressClass;

public class Forgot extends Activity implements OnClickListener {

	EditText email;
	Button submit, cancel;
	ActionBar mactionBar;
	TextView actionbartitle, error;
	LinearLayout errorLyt;

	String emailValue;
	public static String newPassword;
	ConnectionDetector cd;
	Context mContext;
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
		mactionBar.setCustomView(customActionBarView, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));
		cd = new ConnectionDetector(getApplicationContext());

		mactionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		setContentView(R.layout.forgot_password_screen);
		mactionBar.show();
		errorLyt = (LinearLayout) findViewById(R.id.errorLyt);
		error = (TextView) findViewById(R.id.errorText);

		email = (EditText) findViewById(R.id.email);
		submit = (Button) findViewById(R.id.reset_button);
		cancel = (Button) findViewById(R.id.cancel_button);
		submit.setOnClickListener(this);
		cancel.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = null;
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.cancel_button:

			intent = new Intent(Forgot.this, LoginScreen.class);
			startActivity(intent);
			finish();

			break;

		case R.id.reset_button:
			if (cd.isConnectingToInternet()) {
				emailValue = email.getText().toString();
				if (Utilities.checkEmail(this, emailValue, false)) {
					new LoadData(emailValue).execute();

				} else {
					error.setText("Invalid email adress");
					errorLyt.setVisibility(View.VISIBLE);
				}

			} else {
				error.setText("No Connectivity");
				errorLyt.setVisibility(View.VISIBLE);

			}
			break;

		default:
			break;
		}

	}
	class LoadData extends AsyncTask<Void, Void, Void> {
		
		String userName;
		boolean isSuccess;
		
		public LoadData(String userName) {
			this.userName=userName;
			
		}
		@Override
		protected void onPreExecute() {	
			ProgressClass.startProgress(mContext);
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... arg0) {
			UserBO uesrBo = new UserBO(mContext);
			isSuccess = uesrBo.resetPassword(userName);
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			if (isSuccess) {
				Toast.makeText(getApplicationContext(), "New Password send to your email", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(Forgot.this, LoginScreen.class);
				startActivity(intent);
				finish();
			}else{
				error.setText(Utilities.errorMessage);
				errorLyt.setVisibility(View.VISIBLE);
			}
			super.onPostExecute(result);
			ProgressClass.finishProgress();
		}
	}
}
