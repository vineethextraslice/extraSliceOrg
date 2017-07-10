package com.extraslice.walknpay.adapter;

import android.content.Context;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.app.extraslice.R;
import com.extraslice.walknpay.adapter.CartListAdapter.TRHolder;
import com.extraslice.walknpay.ui.CartFragment;

public class MyEditText extends EditText {

	Context context;

	public MyEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	@Override
	public boolean onKeyPreIme(int keyCode, KeyEvent event) {
		Log.e("lllll","oooooooooooooooo");
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			TRHolder trHolder = (TRHolder) this.getTag(R.id.lbltvproductcount);
			CartListAdapter.verifyAvaialableAndUpdateQty(context,trHolder);
			InputMethodManager mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			mgr.hideSoftInputFromWindow(this.getWindowToken(), 0);
		}
		return super.onKeyPreIme(keyCode, event);
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		Log.e("lllll","vvvvvvvvvvvvvvvvvvvv");
		return super.dispatchKeyEvent(event);
	}
	
}