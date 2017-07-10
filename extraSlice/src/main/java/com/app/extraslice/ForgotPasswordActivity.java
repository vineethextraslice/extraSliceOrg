package com.app.extraslice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.extraslice.bo.UserBO;
import com.app.extraslice.utils.CustomException;
import com.app.extraslice.utils.ProgressClass;

public class ForgotPasswordActivity extends Activity {

	AutoCompleteTextView userNameEdtTxt;
	LinearLayout errorLyt;
	TextView errorText;
	Context mContext;
	Button cancel_button , reset_button;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		mContext = this;
		super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        //getActionBar().hide();
        userNameEdtTxt = (AutoCompleteTextView) findViewById(R.id.userName);
        String[] countries = new String[] { "anoop@extraslice.com","anumol@extraslice.com", "irshad@extraslice.com","binur@extraslcie.com" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);
		userNameEdtTxt.setAdapter(adapter);
		cancel_button = (Button)findViewById(R.id.cancel_button);
		reset_button = (Button)findViewById(R.id.reset_button);
		errorLyt = (LinearLayout)findViewById(R.id.errorLyt);
		errorText= (TextView)findViewById(R.id.errorText);
		cancel_button.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				hideVirtualKeyBoard();
				backToLoginScreen();
			}
		});
		reset_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				reset_button.setEnabled(false);
				cancel_button.setEnabled(false);
				hideVirtualKeyBoard();
				new RunInBackground(userNameEdtTxt.getText().toString()).execute();
			}
		});
		
		userNameEdtTxt.setOnFocusChangeListener(new OnFocusChangeListener() {

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
    }

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		backToLoginScreen();
	}
	private void backToLoginScreen(){
		Intent intent = new Intent(ForgotPasswordActivity.this,LoginActivity.class);
		startActivity(intent);
		finish();
	}
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

	private void hideVirtualKeyBoard() {

		try {
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	class RunInBackground extends AsyncTask<Void, Void, Void> {
		String errMsg = null;
		String userName;
		UserBO uesrBo;
		boolean status;

		public RunInBackground(String userName) {
			this.userName = userName;
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
			} else if (!userName.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")) {
				errMsg = "Invalid email!";
			}else{
				try {
					status = uesrBo.resetPassword(userName);
				} catch (CustomException e) {
					errMsg = e.getMessage();
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			reset_button.setEnabled(true);
			cancel_button.setEnabled(true);
			if (errMsg != null) {
				errorText.setText(errMsg);
				errorLyt.setVisibility(View.VISIBLE);
			} else if (status) {
				Toast toast =Toast.makeText(getApplicationContext(), "New Password send to your email", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				Intent intent = new Intent(ForgotPasswordActivity.this,LoginActivity.class);
				startActivity(intent);
				finish();
			}
			ProgressClass.finishProgress();
			super.onPostExecute(result);
		}
	}
}
