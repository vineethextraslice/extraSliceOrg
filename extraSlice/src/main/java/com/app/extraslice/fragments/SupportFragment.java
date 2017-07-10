package com.app.extraslice.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.app.extraslice.R;
import com.app.extraslice.bo.SmartspaceBO;
import com.app.extraslice.fragments.AboutFragment.RunInBackground;
import com.app.extraslice.utils.CustomException;
import com.app.extraslice.utils.ProgressClass;

public class SupportFragment extends Fragment {
	Context mContext;
	WebView webview ;
	public SupportFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		View rootView = inflater.inflate(R.layout.support, container, false);
		webview = (WebView) rootView.findViewById(R.id.webview);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.loadUrl("https://docs.google.com/forms/d/e/1FAIpQLSdyWUIshrIGHrexvkZkG_bohxwagIt6biX0ZySBTy9EhyaNSg/viewform?embedded=true");
		//webview.loadUrl("http://extraslicedev03.cloudapp.net:8080/jforum-2.1.9");
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// inilize();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("test", 1);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
}
