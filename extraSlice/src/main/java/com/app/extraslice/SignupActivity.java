package com.app.extraslice;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.app.extraslice.utils.CustomPaymentGateway;
import com.app.extraslice.utils.StrpePaymentSetup;
import com.app.extraslice.utils.Utilities;


public class SignupActivity extends Activity {
	
	Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen);
        mContext = this;
        //getActionBar().hide();
        Fragment newFrgment = new ShowPlanFragment();
		Utilities.loadFragment(getFragmentManager(), newFrgment, R.id.frame_container, false);
       
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
 @Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// TODO Auto-generated method stub
	super.onActivityResult(requestCode, resultCode, data);
	CustomPaymentGateway.onActivityResult(requestCode, resultCode, data);
}
}
