package com.extraslice.walknpay.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.extraslice.R;
import com.app.extraslice.adapter.CustomAdapter;
import com.app.extraslice.model.SmartSpaceModel;
import com.extraslice.walknpay.adapter.WalletReceiptAdapter;
import com.extraslice.walknpay.bl.TransactionBO;
import com.extraslice.walknpay.bl.Utilities;
import com.extraslice.walknpay.dao.ProgressClass;
import com.extraslice.walknpay.model.TransactionModel;

public class MyReceipts extends Fragment {
	ListView listReceipts;
	WalletReceiptAdapter adapter = null;
	FragmentManager fragmentManager;
	LinearLayout headerLyt;
	TextView noReceipt;
	Context mContext;
	int  rcptPerPage=10;
	int pagePerFram=5;
	int offset=0;
	int pageSet=0;
	int pageNo =0;
	int screenHeight = 0;
	boolean wnpRcptLoaded =false;
	boolean extRcptLoaded =false;
	LinearLayout pageView, noOfrcptView;
	EditText rowsPerPage;
	Button change;
	float density=0;
	private static String RCPT_TYPE_WALKNPAY="WALKNPAY";
	private static String RCPT_TYPE_EXTRASLICE="EXTRASLICE";
	Spinner yearSpinner;
	Spinner monthSpinner;
	String selectedYear="2017";
	String selectedMonth="03";
	SimpleDateFormat ymd_Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:s");
	
	public MyReceipts(int  rcptPerPage,int pageSet,int pageNo){
		if(rcptPerPage >0){
			this.rcptPerPage = rcptPerPage;
		}
		this.pageSet = pageSet;
		this.pageNo = pageNo;
		
		Log.e("ffffffff", rcptPerPage+"\t"+pageSet+"\t"+pageNo+"\t"+offset);
	}
	static List<TransactionModel> transactionModelList = new ArrayList<TransactionModel>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		SimpleDateFormat mdyFormat = new SimpleDateFormat("MM-yyyy");
		String dt = mdyFormat.format(new Date());
		selectedYear = dt.split("-")[1];
		selectedMonth = dt.split("-")[0];
		mContext = getActivity();
		View rootView = inflater.inflate(R.layout.myreceipts_wallet, container, false);
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics ();
		display.getMetrics(outMetrics);
		yearSpinner = (Spinner) rootView.findViewById(R.id.yearSpinner);
		List<Map<String, Object>> yearList = new ArrayList<Map<String, Object>>();
		Map<String, Object> yearMap = new HashMap<String, Object>();
		int currYear = Integer.parseInt(selectedYear);
		int seleYearPos = 0;
		int yearPos = 0;
		for (int year=2015;year<=currYear;year++) {
			yearMap = new HashMap<String, Object>();
			yearMap.put(year+"", year+"");
			yearList.add(yearMap);
			if(Integer.parseInt(selectedYear) == year){
				seleYearPos=yearPos;
			}
			yearPos++;
		}
		CustomAdapter yearAdapter = new CustomAdapter(mContext,yearList);
		yearSpinner.setAdapter(yearAdapter);
		yearSpinner.setSelection(seleYearPos);
		yearSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0,View view, int position, long id) {
				selectedYear = (String) view.getTag();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		
		monthSpinner = (Spinner) rootView.findViewById(R.id.monthSpinner);
		List<Map<String, Object>> monthList = new ArrayList<Map<String, Object>>();
		Map<String, Object> monthMap = new HashMap<String, Object>();
		monthMap.put("Jan", "01");
		monthList.add(monthMap);
		monthMap = new HashMap<String, Object>();
		monthMap.put("Feb", "02");
		monthList.add(monthMap);
		monthMap = new HashMap<String, Object>();
		monthMap.put("Mar", "03");
		monthList.add(monthMap);
		monthMap = new HashMap<String, Object>();
		monthMap.put("Apr", "04");
		monthList.add(monthMap);
		monthMap = new HashMap<String, Object>();
		monthMap.put("May", "05");
		monthList.add(monthMap);
		monthMap = new HashMap<String, Object>();
		monthMap.put("Jun", "06");
		monthList.add(monthMap);
		monthMap = new HashMap<String, Object>();
		monthMap.put("Jul", "07");
		monthList.add(monthMap);
		monthMap = new HashMap<String, Object>();
		monthMap.put("Aug", "08");
		monthList.add(monthMap);
		monthMap = new HashMap<String, Object>();
		monthMap.put("Sep", "09");
		monthList.add(monthMap);
		monthMap = new HashMap<String, Object>();
		monthMap.put("Oct", "10");
		monthList.add(monthMap);
		monthMap = new HashMap<String, Object>();
		monthMap.put("Nov", "11");
		monthList.add(monthMap);
		monthMap = new HashMap<String, Object>();
		monthMap.put("Dec", "12");
		monthList.add(monthMap);
		CustomAdapter monthAdapter = new CustomAdapter(mContext,monthList);
		monthSpinner.setAdapter(monthAdapter);
		monthSpinner.setSelection(Integer.parseInt(selectedMonth)-1);
		monthSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0,View view, int position, long id) {
				selectedMonth = (String) view.getTag();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		Button search = (Button)rootView.findViewById(R.id.search);
		search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				transactionModelList.clear();
				wnpRcptLoaded =false;
				extRcptLoaded =false;
				new LoadData(RCPT_TYPE_WALKNPAY).execute();
				if(com.app.extraslice.utils.Utilities.loggedInUser.getUserType().equalsIgnoreCase("member")){
					new LoadData(RCPT_TYPE_EXTRASLICE).execute();
				}
				
			}
		});
		
		density  = getResources().getDisplayMetrics().density;
		float dpHeight = outMetrics.heightPixels ;
		float dpWidth  = outMetrics.widthPixels ;
		screenHeight = (int) dpHeight;
		 rowsPerPage =(EditText) rootView.findViewById(R.id.rowsPerPage);
		 change =(Button) rootView.findViewById(R.id.change);
		 change.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loadNextSet("#");
			}
		});
		try {
			listReceipts = (ListView) rootView.findViewById(R.id.listReceipts);
			noReceipt = (TextView) rootView.findViewById(R.id.noReceipt);
			pageView  = (LinearLayout) rootView.findViewById(R.id.pageView);
			noOfrcptView  = (LinearLayout) rootView.findViewById(R.id.noOfrcptView);
			rowsPerPage.setText(rcptPerPage+"");
			/*rowsPerPage.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if(!hasFocus){
						loadNextSet("#");
					}
					
				}
			});*/
			headerLyt = (LinearLayout) rootView.findViewById(R.id.header);
			fragmentManager = getFragmentManager();
			/*Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				public void run() {
					callReceiptTranscationAPI();
				}
			}, 0);*/
			callReceiptTranscationAPI();
			if (transactionModelList.size() != 0) {
				transactionModelList.clear();
			}
			listReceipts.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					int pos = arg2;
					Utilities.posReceiptsnew = arg2;
					TransactionModel model = transactionModelList.get(pos);

					Fragment fragment = null;
					fragment = new ReceiptFragment(model, pos - 1, pos + 1);

					if (fragment != null) {

						fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();

					}

				}
			});

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
							fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
						}
						return true;
					} else {
						return false;
					}
				}
			});
		} catch (Exception e) {

		}
		return rootView;
	}

	public void callReceiptTranscationAPI() {
		Context c= this.getActivity();
		new LoadData(RCPT_TYPE_WALKNPAY).execute();
		new LoadData(RCPT_TYPE_EXTRASLICE).execute();
	}
	
	class LoadData extends AsyncTask<Void, Void, Void> {
		boolean isSuccess;
		int totalRcpts = 0;
		String rcptType;
		public LoadData(String rcptType){
			this.rcptType=rcptType;
			
		}
		@Override
		protected void onPreExecute() {	
			ProgressClass.startProgress(mContext);
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... arg0) {
			
			TransactionBO trnBO = new TransactionBO(mContext);
			if (Utilities.loggedInUser != null) {
				try {
					JSONObject rootObject =  null;
					String startDateStr = selectedYear+"-"+selectedMonth+"-01 00:00:0";
					Date startDate = ymd_Format.parse(startDateStr);
					Calendar cal = Calendar.getInstance();
					cal.setTime(startDate);
					cal.add(Calendar.MONTH,1);
					String endDateStr = ymd_Format.format(cal.getTime());
					Log.e("dddd", startDate+"\t  "+endDateStr);
					if(rcptType.equalsIgnoreCase(RCPT_TYPE_WALKNPAY)){
						rootObject = trnBO.getAllWnPTransactionForUser(Utilities.loggedInUser.getUserId(),startDateStr,endDateStr);
						wnpRcptLoaded = true;
					}else{
						rootObject = trnBO.getAllESliceTransactionForUser(com.app.extraslice.utils.Utilities.loggedInUser.getUserId(),startDateStr,endDateStr);
						extRcptLoaded = true;
					}
					if (rootObject != null &&((String) rootObject.get(Utilities.STATUS_STRING)).equals(Utilities.STATUS_SUCCESS)) {
						String count = rootObject.get("COUNT").toString();
						totalRcpts = Integer.parseInt(count);
						if (totalRcpts > 0) {
							JSONArray transactionArray = (JSONArray) rootObject.get("TransactionList");
							if (transactionArray != null) {
								for (int i = 0; i < transactionArray.length(); i++) {
									JSONObject transactionObj = (JSONObject) transactionArray.get(i);
									TransactionModel transaction = (new TransactionModel()).jSonToObject(transactionObj.toString());		
									transactionModelList.add(transaction);
								}
							}				
						}
					} 
					totalRcpts = transactionModelList.size();
				} catch (Exception e) {
				}
						
			} 
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(!extRcptLoaded || !wnpRcptLoaded){
				return;
			}
			Collections.sort(transactionModelList);
			Utilities.posReceipts = transactionModelList.size();
			loadReceipts(totalRcpts);
			
			if (transactionModelList != null && transactionModelList.size() > 0) {
				headerLyt.setVisibility(View.VISIBLE);
				noReceipt.setVisibility(View.INVISIBLE);
				noReceipt.setText("");
			} else {
				headerLyt.setVisibility(View.INVISIBLE);
				noReceipt.setVisibility(View.VISIBLE);
				noReceipt.setText("You have not made a purchase yet!");
			}
			ProgressClass.finishProgress();
			
		}
	}
	private void loadReceipts(int totalRcpts){
		pageView.removeAllViews();
		offset=(pageSet * pagePerFram * rcptPerPage)+(pageNo*rcptPerPage);
		List<TransactionModel> currList = new ArrayList<TransactionModel>();
		for(int i=offset;i<(offset+rcptPerPage);i++){
			if(i>=transactionModelList.size()){
				break;
			}
			currList.add(transactionModelList.get(i));
		}
		
		adapter = new WalletReceiptAdapter(mContext,R.layout.wallet_list_item,currList );
		if (adapter != null) {
			listReceipts.setAdapter(adapter);
		}
		if(totalRcpts > 0){
			int tabHeight = (int)(rcptPerPage*30*density);
			if(tabHeight > (screenHeight-(250*density))  ){
				tabHeight =(int) (screenHeight-(250*density));
			}

			RelativeLayout.LayoutParams lytParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
					tabHeight);
			listReceipts.setLayoutParams(lytParams);
			int noOfPages = totalRcpts/rcptPerPage;
			if((totalRcpts%rcptPerPage) >0 ){
				noOfPages = noOfPages + 1;
			}
			if(noOfPages != 1){
				if(noOfPages<=pagePerFram){
					for(int index = 1;index<=noOfPages;index++){
						TextView tv = new TextView(mContext);
						LayoutParams tvParams = new LayoutParams((int)(32*density),(int)(32*density));
						tvParams.setMargins(2, 2, 2, 2);
						tv.setLayoutParams(tvParams);
						tv.setGravity(Gravity.CENTER);
						
						tv.setTag(index);
						tv.setText(index+"");
						if(index ==(pageNo+1)){
							//tv.setBackgroundColor(getResources().getColor(R.color.blueBaground));
							tv.setBackgroundColor(getResources().getColor(R.color.blueBaground));
							tv.setTextColor(Color.WHITE);
						}else{
							//tv.setBackgroundResource(R.drawable.circular_bordered_background);
							tv.setBackgroundColor(getResources().getColor(R.color.gray_bg));
							tv.setTextColor(Color.BLACK);
						}
						tv.setClickable(true);
						tv.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								loadNextSet(v.getTag().toString());
								
							}
						});
						pageView.addView(tv);
					}
					
				}else{
					if(pageSet!=0){
						TextView tv = new TextView(mContext);
						LayoutParams tvParams = new LayoutParams((int)(32*density),(int)(32*density));
						tvParams.setMargins(2, 2, 2, 2);
						tv.setLayoutParams(tvParams);
						tv.setGravity(Gravity.CENTER);
						//tv.setBackgroundResource(R.drawable.circular_bordered_background);
						tv.setBackgroundColor(getResources().getColor(R.color.gray_bg));
						tv.setTextColor(Color.BLACK);
						tv.setTag("<");
						tv.setText("<");
						pageView.addView(tv);
						tv.setClickable(true);
						tv.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								loadNextSet(v.getTag().toString());
								
							}
						});
					}
					boolean hasNExt = true;
					for(int index = 1;index<=pagePerFram;index++){
						if(((pageSet*pagePerFram)+index) > noOfPages){
							hasNExt =false;
							break;
						}
						TextView tv = new TextView(mContext);
						LayoutParams tvParams = new LayoutParams((int)(32*density),(int)(32*density));
						tvParams.setMargins(2, 2, 2, 2);
						tv.setLayoutParams(tvParams);
						tv.setGravity(Gravity.CENTER);
						//tv.setBackgroundResource(R.drawable.circular_bordered_background);
						tv.setBackgroundColor(getResources().getColor(R.color.gray_bg));
						tv.setTextColor(Color.BLACK);
						tv.setTag(index);
						tv.setText((pageSet*pagePerFram)+index+"");
						pageView.addView(tv);
						tv.setClickable(true);
						if(index ==(pageNo+1)){
							tv.setBackgroundColor(getResources().getColor(R.color.blueBaground));
						}
						
						tv.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								loadNextSet(v.getTag().toString());
								
							}
						});
					}
					if(hasNExt && ((pageSet+1)*pagePerFram) != noOfPages){
						TextView tv = new TextView(mContext);
						LayoutParams tvParams = new LayoutParams((int)(32*density),(int)(32*density));
						tvParams.setMargins(2, 2, 2, 2);
						tv.setLayoutParams(tvParams);
						tv.setGravity(Gravity.CENTER);
						//tv.setBackgroundResource(R.drawable.circular_bordered_background);
						tv.setBackgroundColor(getResources().getColor(R.color.gray_bg));
						tv.setTextColor(Color.BLACK);
						tv.setTag(">");
						tv.setText(">");
						pageView.addView(tv);
						tv.setClickable(true);
						tv.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								loadNextSet(v.getTag().toString());
								
							}
						});
					}
					
				}
			}
		}
	}
	private void loadNextSet(String value){
		if (value.equals("#")){
			int noOfrows =10;
			try{
				noOfrows = Integer.parseInt(rowsPerPage.getText().toString());
			}catch(Exception e){
				rowsPerPage.setText(rcptPerPage+"");
				return;
			}
			rcptPerPage = noOfrows ;
			pageSet=0;
			pageNo = 0;
		}else if(value.equals(">")){
			pageSet = pageSet+1;
			pageNo = 0;
		}else if (value.equals("<")){
			pageSet = pageSet-1;
			pageNo = 0;
		}else{
			if(Integer.parseInt(value) == (pageNo+1) ){
				return;
			}else{
				pageNo = Integer.parseInt(value)-1;
			}
		}
		loadReceipts(transactionModelList.size());
	}
}
