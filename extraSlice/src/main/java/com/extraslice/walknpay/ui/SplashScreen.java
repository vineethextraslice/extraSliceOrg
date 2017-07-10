package com.extraslice.walknpay.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.app.extraslice.R;
import com.extraslice.walknpay.bl.UserBO;
import com.extraslice.walknpay.bl.Utilities;



public class SplashScreen extends Activity {
	private Thread SplashThread;
	Boolean back = false;
	String username;
	Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_splash);
		context=this;
		
		/*TextView logoText = (TextView) findViewById(R.id.logo_text);
		TextView logoTextNew = (TextView) findViewById(R.id.logo_text_new);

		Typeface font = Typeface.createFromAsset(getAssets(),
			//	"tangerinebold.ttf");
				"walkwayultrabold.ttf");
		logoText.setTypeface(font);
		logoTextNew.setTypeface(font);*/
		
		

	
		
		//font.BOLD
		SplashThread = new Thread() {
			@Override
			public void run() {
				try {
					synchronized (this) {
						wait(1000);
					}
				} catch (InterruptedException ex) {

				}

				if (back) {

					System.exit(0);

				} else {
					if (Utilities.getUsername(SplashScreen.this).length() > 0
							|| !Utilities.getUsername(SplashScreen.this)
									.equalsIgnoreCase("")) {
						 UserBO uesrBo = new UserBO(context);
						Utilities.loggedInUser=uesrBo.authenticateUser(Utilities.getUsername(context), Utilities.getPassword(context));
						// Run next activity
						if(Utilities.loggedInUser != null){
							Intent intent = new Intent(SplashScreen.this,MenuActivity.class);
							startActivity(intent);
						}else{
							Intent intent = new Intent(SplashScreen.this, LoginScreen.class);
							startActivity(intent);
						}
						
						
					} else {
						// Run next activity
						/*Intent intent = new Intent(SplashScreen.this,
								LoginScreen.class);
						startActivity(intent);*/
						
						Utilities.islogged = true;
						//Utilities.setCredentials(userName, password, LoginScreen.this);
						Intent intent = new Intent(SplashScreen.this, LoginScreen.class);
						//Intent intent = new Intent(SplashScreen.this, MenuActivity.class);
						startActivity(intent);
						finish();
					}
				}
			}
		};
		SplashThread.start();
	}

	@Override
	public boolean onTouchEvent(MotionEvent evt) {
		if (evt.getAction() == MotionEvent.ACTION_DOWN) {
			synchronized (SplashThread) {
				SplashThread.notifyAll();
			}
		}
		return true;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		finish();
		super.onPause();
	}

	// Device back key
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			back = true;
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}