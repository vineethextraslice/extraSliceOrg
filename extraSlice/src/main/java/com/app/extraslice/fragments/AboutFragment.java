package com.app.extraslice.fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.extraslice.R;
import com.app.extraslice.bo.SmartspaceBO;
import com.app.extraslice.utils.CustomException;
import com.app.extraslice.utils.ProgressClass;
import com.app.extraslice.utils.Utilities;

public class AboutFragment extends Fragment {
	Context mContext;
	View rootView;
	Fragment fragment = null;
	TextView aboutText;
	Dialog dialog;
	FragmentManager fragmentManager;
	TextView privacypolicy,webSrvcVrsn;
	RelativeLayout page;
	
	public AboutFragment() {
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContext = getActivity();
		View rootView = inflater.inflate(R.layout.about, container, false);
		TextView websiteLink = (TextView) rootView.findViewById(R.id.website);
		webSrvcVrsn =   (TextView) rootView.findViewById(R.id.webSrvcVrsn);
		page= (RelativeLayout) rootView.findViewById(R.id.page);
		privacypolicy = (TextView) rootView.findViewById(R.id.privacy);
		aboutText= (TextView) rootView.findViewById(R.id.aboutText);
		new RunInBackground().execute();
		
		/*websiteLink.setText(Html.fromHtml(getString(R.string.website)));*/
		/*Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "walkwayultrabold.ttf");
		websiteLink.setTypeface(font);*/
		
		
		websiteLink.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);
	                myWebLink.setData(Uri.parse("http://extraslice.com"));
	                    startActivity(myWebLink);
			}
		});
		
		
		/*TextView about = (TextView) rootView.findViewById(R.id.website);
		textView.setText(Html.fromHtml(getString(R.string.website)));*/
		rootView.setFocusableInTouchMode(true);
		rootView.requestFocus();
		rootView.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (keyCode == KeyEvent.KEYCODE_BACK) {

					Fragment fragment = null;
					fragment = new HomeFragment();
					if (fragment != null) {
						FragmentManager fragmentManager = getFragmentManager();
						//fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
						Utilities.loadFragment(fragmentManager,fragment,R.id.frame_container,true);
					}
					return true;
				} else {
					return false;
				}
			}
		});
		return rootView;
	}
	
	class RunInBackground extends AsyncTask<Void, Void, Void> {
		String errorMessage;

		public RunInBackground() {
			
		}

		@Override
		protected void onPreExecute() {
			
			ProgressClass.startProgress(mContext);
			

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			SmartspaceBO smBo = new SmartspaceBO(mContext);
			try {
				smBo.getAdminAccount();
			} catch (CustomException e) {
				errorMessage = e.getMessage();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (errorMessage != null) {
				aboutText.setText(errorMessage);
				aboutText.setTextColor(getResources().getColor(R.color.red));
			} else if (SmartspaceBO.accountModel != null) {
				aboutText.setText(SmartspaceBO.accountModel.getAbout());
				aboutText.setTextColor(getResources().getColor(R.color.black));
				webSrvcVrsn.setText(SmartspaceBO.accountModel.getWebserviceVersion());
				privacypolicy.setOnClickListener(new OnClickListener() {

					  @Override
					  public void onClick(View arg0) {
							LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			    			final View webViewLyt = inflater.inflate(R.layout.web_view, null, false);
			    			WebView webView = (WebView) webViewLyt.findViewById(R.id.webView);
			    			webView.getSettings().setJavaScriptEnabled(true);
			    			webView.loadUrl(SmartspaceBO.accountModel.getPrivacyPolicyUrl());
			    			webView.setWebViewClient(new WebViewClient() {
			    	             

			    	                @Override
			    	            public void onPageStarted(WebView view, String url, Bitmap favicon) {
			    	                super.onPageStarted(view, url, favicon);
			    	                ProgressClass.startProgress(mContext);

			    	            }

			    	                @Override
			    	                public boolean shouldOverrideUrlLoading(WebView view, String url)
			    	                {
			    	                    
			    	                    view.loadUrl(url);
			    	                    return true;

			    	                }

			    	               @Override
			    	               public void onPageFinished(WebView view, String url) {
			    	            	   ProgressClass.finishProgress();
			    	              }

			    	            });

			    			Button close = (Button)webViewLyt.findViewById(R.id.closeWV);
			    			page.addView(webViewLyt);
			    			close.setOnClickListener(new OnClickListener() {
			    				
			    				@Override
			    				public void onClick(View v) {
			    					page.removeView(webViewLyt);
			    				}
			    			});
					  }

					});

			}
			ProgressClass.finishProgress();
			super.onPostExecute(result);
		}
	}

	
}
