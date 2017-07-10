package com.app.extraslice.fragments;

import org.apache.http.util.EncodingUtils;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.app.extraslice.R;
import com.app.extraslice.bo.UserBO;
import com.app.extraslice.model.UserModel;
import com.app.extraslice.utils.ProgressClass;
import com.app.extraslice.utils.Utilities;

public class ForumFragment extends Fragment {
	Context mContext;
	WebView webview ;
	RelativeLayout page;
	public ForumFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		View rootView = inflater.inflate(R.layout.forun_screen, container, false);
		page = (RelativeLayout) rootView.findViewById(R.id.loginLyt);
		final View webViewLyt = inflater.inflate(R.layout.web_view_only, null, false);
		WebView webView = (WebView) webViewLyt.findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);
		try {
			UserModel model = Utilities.loggedInUser;
			String username = model.getEmail().substring(0,model.getEmail().lastIndexOf("."));
			 username =  username.replaceAll("@", "_");
			if(username.length() > 19){
				username = username.substring(0, 19);
			}
			String password = Utilities.decode(model.getPassword());
			String postData = "username="+username+"&password="+password;
			webView.postUrl("http://extraslicedev04.cloudapp.net/phpbb/autologin.php",  EncodingUtils.getBytes(postData, "utf-8"));
			webView.setWebViewClient(new WebViewClient() {

				@Override
				public void onPageStarted(WebView view, String url, Bitmap favicon) {
					super.onPageStarted(view, url, favicon);
					ProgressClass.startProgress(mContext);

				}

				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {

					view.loadUrl(url);
					return true;

				}

				@Override
				public void onPageFinished(WebView view, String url) {
					ProgressClass.finishProgress();
				}

			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		page.addView(webViewLyt);
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	
}
