package com.extraslice.walknpay.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.app.extraslice.R;
public class AboutFragment extends Fragment {
	 

	public AboutFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.about_wnp, container, false);
		TextView websiteLink = (TextView) rootView.findViewById(R.id.website);
	
		TextView privacypolicy = (TextView) rootView.findViewById(R.id.	privacy);
		final View includedLayout = rootView.findViewById(R.id.webview);
		/*websiteLink.setText(Html.fromHtml(getString(R.string.website)));*/
		/*Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "walkwayultrabold.ttf");
		websiteLink.setTypeface(font);*/
		WebView	webView = (WebView) includedLayout.findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl("file:///android_asset/privacy.html");
		websiteLink.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);
	                myWebLink.setData(Uri.parse("http://walknpay.extraslice.com"));
	                    startActivity(myWebLink);
			}
		});
		privacypolicy.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				includedLayout.setVisibility(View.VISIBLE);
			}
		});
		
		/*TextView about = (TextView) rootView.findViewById(R.id.website);
		textView.setText(Html.fromHtml(getString(R.string.website)));*/
		rootView.setFocusableInTouchMode(true);
		rootView.requestFocus();
		rootView.setFocusableInTouchMode(true);
		rootView.requestFocus();
		rootView.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (keyCode == KeyEvent.KEYCODE_BACK) {
					
					if(includedLayout.getVisibility()==View.VISIBLE)
					{
						includedLayout.setVisibility(View.GONE);
						Fragment fragment = null;
						fragment = new AboutFragment();
						if (fragment != null) {
							FragmentManager fragmentManager = getFragmentManager();
							fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
						}
						
						
						return true;
					
					}
					else {/*
						Fragment fragment = null;
						fragment = new HomeFragment();
						if (fragment != null) {
							FragmentManager fragmentManager = getFragmentManager();
							fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
						}
						return false;
					*/}
					
					
				}
				return false; 
			}
		});
		return rootView;
	}

	public void exitButton(View v)
	{
		//includedLayout.setVisibility(View.GONE);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
		}
		return true;
	}
}
