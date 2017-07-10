package com.extraslice.walknpay.bl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.json.simple.parser.JSONParser;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class RunJSon extends AsyncTask<Void, JSONObject, JSONObject> {
	Context mContext;
	String urlString;
	String jsonString;

	public RunJSon(Context mContext, String urlString, String jsonString) {
		this.mContext = mContext;
		this.urlString = urlString;
		this.jsonString = jsonString;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		startProgress();
	}

	@Override
	protected JSONObject doInBackground(Void... arg0) {
		JSONObject rootObject = null;
		long start = (new Date()).getTime();
		long end = 0;
		Log.e("urlString",urlString);
		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Content-Length", Integer.toString(jsonString.length()));
			conn.setReadTimeout(15000);
			conn.setConnectTimeout(20000);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			//Log.e("url", urlString);
			Log.e("jsonString", jsonString);

			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter((conn.getOutputStream())));
			writer.write(jsonString, 0, jsonString.length());
			writer.flush();
			writer.close();

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			JSONParser parser = new JSONParser();
			Object object = parser.parse(br);
			org.json.simple.JSONObject jsonObj = (org.json.simple.JSONObject) object;
			rootObject = new JSONObject(jsonObj.toJSONString());
			//Log.e("rootObject", rootObject.toString());
			conn.disconnect();
			end = (new Date()).getTime();
		} catch (Exception e) {
			rootObject = new JSONObject();
			try {
				rootObject.put(Utilities.ERROR_MESSAGE, "Sorry, could not connect to server");
				rootObject.put(Utilities.STATUS_STRING, Utilities.STATUS_FAILED );
				
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//e.printStackTrace();
		}
		
		Log.e("time taken", "time taken   :   "+(end-start));
		Log.e("rootObject", rootObject.toString());
		return rootObject;
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		super.onPostExecute(result);
		finishProgress();
	}

	private ProgressDialog progressDialog;

	private void startProgress() {
		try {
			progressDialog = new ProgressDialog(mContext);
			if (progressDialog != null) {
				progressDialog.setCancelable(false);
				progressDialog.setMessage("One moment...");
				progressDialog.setTitle("Processing");
				progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressDialog.setProgress(0);
				progressDialog.setMax(1);
				progressDialog.show();
			}
		} catch (Exception e) {

		}
	}

	private void finishProgress() {
		try {
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		} catch (Exception e) {

		}

	}

}
