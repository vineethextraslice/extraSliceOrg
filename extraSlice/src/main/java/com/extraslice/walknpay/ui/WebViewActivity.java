package com.extraslice.walknpay.ui;


import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.app.extraslice.R;

public class WebViewActivity extends Activity {

	private WebView webView;
	String url;
	public WebViewActivity(){
		
	}
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.termsofuse);

		webView = (WebView) findViewById(R.id.webviewterms);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(getIntent().getExtras().getString("URL"));

	}

}