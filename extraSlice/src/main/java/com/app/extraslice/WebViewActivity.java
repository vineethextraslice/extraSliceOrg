package com.app.extraslice;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;

public class WebViewActivity extends Activity {

	private WebView webView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_view);

		webView = (WebView) findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl("http://www.google.com");
		Button close = (Button)findViewById(R.id.closeWV);
		close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				webView.goBack();
			}
		});

	}

}
