package com.app.extraslice.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.extraslice.R;
import com.app.extraslice.adapter.CustomAdapter;
import com.app.extraslice.bo.SmartspaceBO;
import com.app.extraslice.model.AdminAccountModel;
import com.app.extraslice.model.Organization;
import com.app.extraslice.model.ReservationModel;
import com.app.extraslice.model.ResourceModel;
import com.app.extraslice.model.ResourceTypeModel;
import com.app.extraslice.model.SmartSpaceModel;
import com.app.extraslice.utils.CustomPaymentGateway;
import com.app.extraslice.utils.ProgressClass;
import com.app.extraslice.utils.Utilities;
import com.stripe.model.Token;

import org.codehaus.jettison.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

 public class OldCurrentConfRoomReservations extends Fragment {
	private final String LOAD_RESERVATIONS = "LOAD_RESERVATIONS";
	private final String DELETE_RESERVATIONS = "DELETE_RESERVATIONS";
	private final String LOAD_SMARTSPACE = "LOAD_SMARTSPACE";
	private final String UPDATE_RESERVATIONS = "UPDATE_RESERVATIONS";
	private final String UPDATE_RESERVATIONS_STATUS = "UPDATE_RESERVATIONS_STATUS";
	
	private final int DATE_TYPE_DAILY=1;
	private final int DATE_TYPE_WEEKLY=2;
	private final int DATE_TYPE_MONTHLY=3;
	boolean  smspaceloaded;
	List<ReservationModel> reservatonList;
	Spinner confRoomSpinner;
	int selectedDayType = 1;
	String paymentGateWay="Stripe";
	double currBalance = 0;
	double resourcePrice= 0;
	int selectedCustDuration =30;
	AdminAccountModel admAcctModel;
	ResourceModel selectedResModel = null;
	LinearLayout popupErrorLyt;
	TextView popupErrorText;
	List<ResourceTypeModel> resourceTypeList;
	List<Organization> apprvdOrgList = new ArrayList<Organization>(1);
	List<SmartSpaceModel> smartSpaceList = new ArrayList<SmartSpaceModel>();
	Context mContext;
	Map<String,Map<Integer,List<ReservationModel>>> reservationMapForDays = null;
	TextView errorText,dayView,weekView,monthView,selectedPeriod;
	View dayViewseperator,weekViewseperator,monthViewseperator;
	ImageView errorImage;
	List<String> minList = new ArrayList<String>();
	CustomAdapter sspaceAdapter;
	List<Map<String, Object>> sspaceNameList;
	String selectedDayStr,selectedHourStr;
	float scale=1.0f;
	float width = 0.0f;
	boolean haveBalance = false;
	LinearLayout errorLyt;
	SimpleDateFormat mdyHMFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a",Locale.US);
	SimpleDateFormat mdyFormat = new SimpleDateFormat("MM/dd/yyyy");
	SimpleDateFormat MMMyyyyFormat  = new SimpleDateFormat("MMM-yyyy");
	SimpleDateFormat dMMMFormat = new SimpleDateFormat("d-MMM");
	SimpleDateFormat MhFormat = new SimpleDateFormat("hh:mm a",Locale.US);
	SimpleDateFormat ymd_Format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	GestureDetector gestureDetector;
	RelativeLayout containerView;
	int currTimeLocation=360;
	Dialog dialog;
	Dialog updateDialog;
	ViewTreeObserver vto;
	Calendar currCalendar;
	View rootView ;
	Date schedMonthStartDate,schedMonthEndDate;
	ScrollView dayScView;
	GridLayout monthGridView,dayGridView,hoursInday,weekGridView,days;
	RelativeLayout dayRLLyt,daysInWeek;
	Date selectedDate;
	static Resources appresources;
	public OldCurrentConfRoomReservations() {
		
		currCalendar = Calendar.getInstance();
		currCalendar.setTime(new Date());
	}
	

	public OldCurrentConfRoomReservations(Date selectedDate) {
		this.selectedDate = selectedDate;
	
		currCalendar = Calendar.getInstance();
		currCalendar.setTime(selectedDate);
	}
	
	public OldCurrentConfRoomReservations(Context ctx){
		
       gestureDetector = new GestureDetector(ctx, new GestureListener());
    }
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		
		rootView = inflater.inflate(R.layout.my_reservations,container, false);
		mContext = getActivity();
		appresources = getResources();
		Calendar startDate = (Calendar) currCalendar.clone();
		int currMonth = startDate.get(Calendar.MONTH);
		int noOFWeeks = 4;
		containerView= (RelativeLayout)rootView.findViewById(R.id.containerView);
		gestureDetector = new GestureDetector(mContext, new GestureListener());
		scale =appresources.getDisplayMetrics().density;
		containerView.setClickable(true);
		containerView.bringToFront();
		LinearLayout topLyt= (LinearLayout)rootView.findViewById(R.id.topLyt);
		dayScView= (ScrollView)rootView.findViewById(R.id.dayScView);
		
		
		vto = dayScView.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
		    public void onGlobalLayout() {
		    	/*String time = startTime.getText().toString();
		    	int startInt = Integer.parseInt(time.split(":")[0]);
		    	if(startInt == 12){
		    		if(time.endsWith("AM")){
		    			startInt = 0;
			    	}
		    	}else{
		    		if(time.endsWith("PM")){
		    			startInt = startInt+12;
			    	}
		    	}*/
		    	dayScView.scrollTo(0,(int)(9*60*scale));
		    }
		});
		vto.dispatchOnGlobalLayout();
		
		//dayScView.bringToFront();
		//topLyt.bringToFront();
		topLyt.setClickable(true);
		dayScView.setClickable(true);
		//imgLyt.bringToFront();
		topLyt.setOnTouchListener(new View.OnTouchListener() {
		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
		    	v.performClick();
		        return gestureDetector.onTouchEvent(event);
		    }
		});
		ScrollView monthScView= (ScrollView)rootView.findViewById(R.id.monthScView);
		monthScView.setOnTouchListener(new View.OnTouchListener() {
		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
		    	v.performClick();
		        return gestureDetector.onTouchEvent(event);
		    }
		});
		monthScView.setClickable(true);
		dayScView.setOnTouchListener(new View.OnTouchListener() {
		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
		    	v.performClick();
		        return gestureDetector.onTouchEvent(event);
		    }
		});
		
		
		containerView.setOnTouchListener(new View.OnTouchListener() {
		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
		    	v.performClick();
		        return gestureDetector.onTouchEvent(event);
		    }
		});
		startDate.set(Calendar.DAY_OF_MONTH, 1);
		for (Organization org : Utilities.loggedInUser.getOrgList()) {
			if (org.isApproved()) {
				apprvdOrgList.add(org);
			}
		}
		int startDay = startDate.get(Calendar.DAY_OF_WEEK);
		startDate.add(Calendar.DAY_OF_WEEK, - startDay+1);
		schedMonthStartDate = startDate.getTime();
		if(currMonth==1){
			if(((startDate.get(Calendar.YEAR)%4) == 0) ||startDay != Calendar.SUNDAY){
				noOFWeeks = 5;
			}
		}else if(currMonth == 0 || currMonth == 2 || currMonth ==4 || currMonth ==6 || currMonth ==7 || currMonth ==9 || currMonth ==11){
			if(startDay >= Calendar.FRIDAY){
				noOFWeeks = 6;
			}else{
				noOFWeeks = 5;
			}
		}else{
			if(startDay == Calendar.SATURDAY){
				noOFWeeks = 6;
			}else{
				noOFWeeks = 5;
			}
		}
		startDate.add(Calendar.DAY_OF_MONTH, ((noOFWeeks*7)-1));
		schedMonthEndDate = startDate.getTime();
		dayGridView = (GridLayout)rootView.findViewById(R.id.dayGridView);
		errorLyt = (LinearLayout)rootView.findViewById(R.id.errorLyt);
		days  =  (GridLayout)rootView.findViewById(R.id.days);
		hoursInday = (GridLayout)rootView.findViewById(R.id.hoursInday);
		dayRLLyt = (RelativeLayout)rootView.findViewById(R.id.dayRLLyt); 
		weekGridView = (GridLayout)rootView.findViewById(R.id.weekGridView);
		monthGridView = (GridLayout)rootView.findViewById(R.id.monthGridView);
		errorText =(TextView)rootView.findViewById(R.id.errorText);
		selectedPeriod =(TextView)rootView.findViewById(R.id.selectedPeriod);
		daysInWeek =(RelativeLayout)rootView.findViewById(R.id.daysInWeek);
		/*imgback =(ImageView)rootView.findViewById(R.id.imgback);;
		imgfor =(ImageView)rootView.findViewById(R.id.imgfor);*/
		dayView = (TextView) rootView.findViewById(R.id.dayView);
		weekView = (TextView) rootView.findViewById(R.id.weekView);
		monthView = (TextView) rootView.findViewById(R.id.monthView);
		dayViewseperator= (View) rootView.findViewById(R.id.dayViewseperator);
		weekViewseperator= (View) rootView.findViewById(R.id.weekViewseperator);
		monthViewseperator= (View) rootView.findViewById(R.id.monthViewseperator);
		monthGridView.setClickable(true);
		weekGridView.setClickable(true);
		monthGridView.setOnTouchListener(new View.OnTouchListener() {
		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
		    	v.performClick();
		        return gestureDetector.onTouchEvent(event);
		    }
		});
		weekGridView.setOnTouchListener(new View.OnTouchListener() {
		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
		    	v.performClick();
		        return gestureDetector.onTouchEvent(event);
		    }
		});
		dayView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectedDayType = DATE_TYPE_DAILY;
				setSelectedDay(dayViewseperator,dayView, DATE_TYPE_DAILY);
			}
		});
		
		weekView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectedDayType = DATE_TYPE_WEEKLY;
				setSelectedDay(weekViewseperator,weekView, selectedDayType);
			}
		});
		
		monthView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectedDayType = DATE_TYPE_MONTHLY;
				setSelectedDay(monthViewseperator,monthView, selectedDayType);
			}
		});
		try {
	
			new RunInBackground(LOAD_RESERVATIONS,null,null,null,-1,0,false).execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		setSelectedDay(dayViewseperator,dayView, selectedDayType);
		rootView.setFocusableInTouchMode(true);
		rootView.requestFocus();
		rootView.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				/*if (keyCode == KeyEvent.KEYCODE_BACK) {
					Fragment fragment = null;
					fragment = new ReserveAConfRoom();
					if (fragment != null) {
						FragmentManager fragmentManager = getFragmentManager();
						Utilities.loadFragment(fragmentManager,fragment,R.id.frame_container,true);
					}
					return false;
				} else {*/
				return true;
				//}
			}
		});
		
		return rootView;
	}

	private void loadNextData(int multiplier){
		if(selectedDayType == DATE_TYPE_DAILY){
			currCalendar.add(Calendar.DAY_OF_MONTH, multiplier);
			if(multiplier > 0){
				if(currCalendar.getTime().after(schedMonthEndDate)){

					new RunInBackground(LOAD_RESERVATIONS,null,null,null,-1,0,false).execute();
				}else{
					setSelectedDay(dayViewseperator,dayView, selectedDayType);
				}
			}else{
				if(currCalendar.getTime().before(schedMonthStartDate)){
					new RunInBackground(LOAD_RESERVATIONS,null,null,null,-1,0,false).execute();
				}else{
					setSelectedDay(dayViewseperator,dayView, selectedDayType);
				}
			}
		}else if(selectedDayType == DATE_TYPE_WEEKLY){
			currCalendar.add(Calendar.DAY_OF_MONTH, (7*multiplier));
			if(multiplier > 0){
				Calendar startDate = (Calendar) currCalendar.clone();
				int startDay = startDate.get(Calendar.DAY_OF_WEEK);
				startDate.add(Calendar.DAY_OF_WEEK, (7- startDay));
				if(startDate.getTime().after(schedMonthEndDate)){
					new RunInBackground(LOAD_RESERVATIONS,null,null,null,-1,0,false).execute();
				}else{
					setSelectedDay(weekViewseperator,weekView, selectedDayType);
				}
			}else{
				Calendar startDate = (Calendar) currCalendar.clone();
				int startDay = startDate.get(Calendar.DAY_OF_WEEK);
				startDate.add(Calendar.DAY_OF_WEEK, - startDay+1);
				if(startDate.getTime().before(schedMonthStartDate)){

					new RunInBackground(LOAD_RESERVATIONS,null,null,null,-1,0,false).execute();

				}else{
					setSelectedDay(weekViewseperator,weekView, selectedDayType);
				}
			}
		}else if(selectedDayType == DATE_TYPE_MONTHLY){
			currCalendar.add(Calendar.MONTH, multiplier);
			new RunInBackground(LOAD_RESERVATIONS,null,null,null,-1,0,false).execute();
		}
		
		
	}
	
	
	/**
	 * highlight selected day
	 * @param selCardView
	 * @param selTxtView
	 * @param date
	 */
	@SuppressLint("NewApi") 
	private void setSelectedDay(View selSepView,TextView selTxtView,int type){
		//errorText.setText("");
		//errorLyt.setVisibility(View.GONE);
		/*dayView.setBackgroundResource(R.color.theme_color_trnsp_55);
		weekView.setBackgroundResource(R.color.theme_color_trnsp_55);
		monthView.setBackgroundResource(R.color.theme_color_trnsp_55);
		dayViewseperator.setBackgroundResource(R.color.theme_color_trnsp_55);
		weekViewseperator.setBackgroundResource(R.color.theme_color_trnsp_55);
		monthViewseperator.setBackgroundResource(R.color.theme_color_trnsp_55);*/
		dayView.setBackgroundResource(R.color.white);
		weekView.setBackgroundResource(R.color.white);
		monthView.setBackgroundResource(R.color.white);
		dayViewseperator.setBackgroundResource(R.color.white);
		weekViewseperator.setBackgroundResource(R.color.white);
		monthViewseperator.setBackgroundResource(R.color.white);

		
		dayView.setTextColor(appresources.getColor(R.color.black));
		weekView.setTextColor(appresources.getColor(R.color.black));
		monthView.setTextColor(appresources.getColor(R.color.black));
		selSepView.setBackgroundResource(R.color.theme_dark_blue);
		selTxtView.setTextColor(appresources.getColor(R.color.black));
		selTxtView.setBackgroundResource(R.color.white);
		if(type == DATE_TYPE_DAILY){
			loadDailyData();
		}else if(type == DATE_TYPE_WEEKLY){
			loadWeeklyData();
		}else if(type == DATE_TYPE_MONTHLY){
			loadMonthlyData();
		}
		
		
	}
	
	
	class RunInBackground extends AsyncTask<Void, Void, Void> {
		ReservationModel reservationModel = null;
		String dataType;
		int selectedOrgId;
		String errorMessage;
		List<ReservationModel> reservationList;
		double amountPaid;
		JSONObject jsonObject;
		String gateWay;
		boolean agreeToPay;
		String tokenId;
		String status;
		public RunInBackground(String dataType,ReservationModel reservationModel,String tokenId,String gateWay,int selectedOrgId,double amountPaid,boolean agreeToPay){
			this.dataType=dataType;
			this.reservationModel = reservationModel;
			this.selectedOrgId= selectedOrgId;
			this.amountPaid = amountPaid;
			this.gateWay = gateWay;
			this.agreeToPay = agreeToPay;
			this.tokenId = tokenId;
		}
		@Override
		protected void onPreExecute() {
			ProgressClass.startProgress(mContext);
			super.onPreExecute();
			
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try{
				SmartspaceBO smBO = new SmartspaceBO(mContext);
				if(dataType.equals(LOAD_RESERVATIONS)){
					
					String selectedDayStr = mdyFormat.format(schedMonthStartDate.getTime());
					String monthStartTime="";
					String monthEndTime ="";
					ymd_Format.setTimeZone(TimeZone.getTimeZone("UTC"));
					try {
						monthStartTime = ymd_Format.format(mdyHMFormat.parse(selectedDayStr+" 00:00 am"));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					
					selectedDayStr = mdyFormat.format(schedMonthEndDate.getTime());
					ymd_Format.setTimeZone(TimeZone.getTimeZone("UTC"));
					try {
						monthEndTime = ymd_Format.format(mdyHMFormat.parse(selectedDayStr+" 00:00 am"));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					reservationList = smBO.getCurrentSchedulesForPeriod(monthStartTime,monthEndTime);
					
				}else if(dataType.equals(DELETE_RESERVATIONS)){
					smBO.deleteReservation(reservationModel);
				}else if(dataType.equals(LOAD_SMARTSPACE)){
					smartSpaceList = smBO.getAllSmartSpace(0, 0);
				}else if(dataType.equals(UPDATE_RESERVATIONS)){
					//ReservationModel model,String custId,String subscriptionId,String gateWay,double paidAmount,boolean agreeToPay
					long trialEndsAt = reservationModel.getStartDate().getTime();
					long currTime = new Date().getTime();
					int trialPeriods  = (int)((trialEndsAt - currTime)/(1000*60*60*24));
					jsonObject = smBO.updateReservation(reservationModel,null,trialPeriods,gateWay,amountPaid,agreeToPay);
				}else if (dataType.equals(UPDATE_RESERVATIONS_STATUS)) {
					Long trialEndsAt = reservationModel.getStartDate().getTime();
					long currTime = new Date().getTime();
					int trialPeriods  = (int)((trialEndsAt - currTime)/(1000*60*60*24));
					status = smBO.updateReservationStatus(reservationModel, null, trialPeriods, amountPaid, gateWay);
					jsonObject = smBO.updateReservation(reservationModel,null,trialPeriods,gateWay,amountPaid,agreeToPay);
				}
			}catch(Exception e){
				errorMessage = e.getMessage();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(errorMessage != null){
				if(dataType.equals(LOAD_RESERVATIONS) ){
					errorText.setText(errorMessage);
					errorLyt.setVisibility(View.VISIBLE);
				}else{
					popupErrorText.setText(errorMessage);
					popupErrorLyt.setVisibility(View.VISIBLE);
				}
				ProgressClass.finishProgress();
			} else if (dataType.equals(LOAD_SMARTSPACE)) {
				admAcctModel = SmartspaceBO.accountModel;
				List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
				int selectedPosition = 0;
				int index = 0;
				if(smartSpaceList != null && smartSpaceList.size() > 0){
					for(ResourceModel resModel : smartSpaceList.get(0).getResourceList()){
						Map<String,Object> map = new HashMap<String, Object>();
						map.put(resModel.getResourceName(), resModel);
						if(resModel.getResourceId() == reservationModel.getResourceId()){
							selectedPosition = index;
						}
						mapList.add(map);
						index++;
					}
					CustomAdapter resAdapter = new CustomAdapter(mContext, mapList);
					confRoomSpinner.setAdapter(resAdapter);
					confRoomSpinner.setSelection(selectedPosition);
					confRoomSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> parent, View view,
								int position, long id) {
							selectedResModel = (ResourceModel) view.getTag();
						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {
						}
					});
				}
				

				smspaceloaded = true;
				ProgressClass.finishProgress();
			}else if(dataType.equals(LOAD_RESERVATIONS)){
				if(reservationList != null && reservationList.size() > 0){
					reservationMapForDays = new HashMap<String, Map<Integer,List<ReservationModel>>>();
					for(ReservationModel resModel : reservationList){
						if(resModel.getReservedByUser() != Utilities.loggedInUser.getUserId()){
							continue;
						}
						
						String day = mdyFormat.format(resModel.getStartDate());
						Map<Integer,List<ReservationModel>> reservationMapForHours = new HashMap<Integer, List<ReservationModel>>();
						if(reservationMapForDays.get(day) == null){
							reservationMapForDays.put(day, reservationMapForHours);
						}
						reservationMapForHours = reservationMapForDays.get(day);
						int hour = Integer.parseInt(MhFormat.format(resModel.getStartDate()).split(":")[0]);
						List<ReservationModel> reservationForHrList =  new ArrayList<ReservationModel>();
						if(reservationMapForHours.get(hour) == null){
							reservationMapForHours.put(hour, reservationForHrList);
						}
						reservationForHrList = reservationMapForHours.get(hour);
						reservationForHrList.add(resModel);
					}
					
					TextView selView = dayView;
					View selSepView = dayViewseperator;
					if(selectedDayType == DATE_TYPE_DAILY){
						selView = dayView;
						selSepView = dayViewseperator;
					}else if(selectedDayType == DATE_TYPE_WEEKLY){
						selView = weekView ;
						selSepView = weekViewseperator;
					}else if(selectedDayType == DATE_TYPE_MONTHLY){
						selView = monthView;
						selSepView = monthViewseperator;
					}
					setSelectedDay(selSepView,selView, selectedDayType);
				}
				ProgressClass.finishProgress();
			}else if(dataType.equals(DELETE_RESERVATIONS)){
				String msgText = "Deleted successfully.";
				
				Toast toast = Toast.makeText(mContext,msgText, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				new RunInBackground(LOAD_RESERVATIONS,null,null,null,-1,0,false).execute();
				if(dialog != null){
					dialog.dismiss();
				}
			}else if(dataType.equals(UPDATE_RESERVATIONS_STATUS)){
				if(dialog != null){
					dialog.dismiss();
				}
				if(updateDialog != null){
					updateDialog.dismiss();
				}
				ProgressClass.finishProgress();
				dialog = new Dialog(mContext);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.custom_dialog);
				TextView messageText= (TextView)dialog.findViewById(R.id.messageText);
				TextView header= (TextView)dialog.findViewById(R.id.header);
				header.setText("Successful");
				messageText.setText("Updated successfully");
				messageText.setGravity(Gravity.CENTER);
				Button close = (Button)dialog.findViewById(R.id.close);
				close.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						new RunInBackground(LOAD_RESERVATIONS,null,null,null,-1,0,false).execute();
						dialog.dismiss();
					}
				});
				
				dialog.show();
			}else if(dataType.equals(UPDATE_RESERVATIONS)){
				if(updateDialog !=null){
					updateDialog.dismiss();
				}
				try {
					if (jsonObject != null &&  jsonObject.get(Utilities.STATUS_STRING) != null &&  jsonObject.get(Utilities.STATUS_STRING).toString().equals(Utilities.STATUS_SUCCESS)) {
						boolean makePayment = jsonObject.getBoolean("makePayment");
						boolean newPayment = jsonObject.getBoolean("newPayment");
						String paymentDescription = null;
						try{
							paymentDescription = jsonObject.getString("paymentDescription");
						}catch(Exception e){
							
						}
						try{
							amountPaid = jsonObject.getDouble("payableAmount");
						}catch(Exception e){
							
						}
						
						if(makePayment){
							ProgressClass.finishProgress();
							
							
							final ReservationModel resModelUpdate = new ReservationModel().jSonToObject(jsonObject.getString("ReservationModel"));
							if(newPayment){
								boolean haveAccount = jsonObject.getBoolean("haveAccount");
								
								if(haveAccount){
									updateDialog = new Dialog(mContext);
									updateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
									updateDialog.setContentView(R.layout.payment_popup);
									TextView addPyTxt = (TextView)updateDialog.findViewById(R.id.addPyTxt);
									Button cancel = (Button)updateDialog.findViewById(R.id.cancel);
									Button submit = (Button)updateDialog.findViewById(R.id.submit);
									addPyTxt.setText(paymentDescription);
									cancel.setOnClickListener(new OnClickListener() {
										
										@Override
										public void onClick(View arg0) {
											updateDialog.dismiss();
											
										}
									});
									submit.setOnClickListener(new OnClickListener() {
										
										@Override
										public void onClick(View arg0) {
											new RunInBackground(UPDATE_RESERVATIONS_STATUS, resModelUpdate, null,gateWay,-1,amountPaid,true).execute();
											dialog.dismiss();
											
										}
									});
									updateDialog.show();
								}else{
									if (admAcctModel == null) {
										errorLyt.setVisibility(View.VISIBLE);
										errorText.setText("Could not retrive account details");
										dialog.dismiss();
										ProgressClass.finishProgress();
									} else {
										try {
											CustomPaymentGatewayImpl.STRP_API_KEY=Utilities.decode(admAcctModel.getStrpSecKey());
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

										CustomPaymentGatewayImpl.PAYABLE_AMOUNT      	   = amountPaid;
										CustomPaymentGatewayImpl.DISPLAY_PYMNT_DESCRIPTION=paymentDescription;
										CustomPaymentGatewayImpl.STRIPE_PAYMENT_DESCRIPTION = "(Android) Payment for conf room by "+ Utilities.loggedInUser.getEmail();
										CustomPaymentGatewayImpl.USER_ID             = Utilities.loggedInUser.getUserId();
										CustomPaymentGatewayImpl.USER_NAME           = Utilities.loggedInUser.getUserName();
										CustomPaymentGatewayImpl.PURPOSE=CustomPaymentGateway.PURPOSE_UPDATE_RESERVATION;
										CustomPaymentGatewayImpl strpGateway = new CustomPaymentGatewayImpl(mContext,resModelUpdate);
										strpGateway.makeStripePayment();
										
									}
								}
								
							}else{
								updateDialog = new Dialog(mContext);
								updateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
								updateDialog.setContentView(R.layout.payment_popup);
								TextView addPyTxt = (TextView)updateDialog.findViewById(R.id.addPyTxt);
								Button cancel = (Button)updateDialog.findViewById(R.id.cancel);
								Button submit = (Button)updateDialog.findViewById(R.id.submit);
								addPyTxt.setText(paymentDescription);
								cancel.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View arg0) {
										updateDialog.dismiss();
										
									}
								});
								submit.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View arg0) {
										new RunInBackground(UPDATE_RESERVATIONS, resModelUpdate,null, null,-1,amountPaid,true).execute();
										
									}
								});
								updateDialog.show();
							
							}
						}else{
							if(dialog != null){
								dialog.dismiss();
							}
							dialog = new Dialog(mContext);
							dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
							dialog.setContentView(R.layout.custom_dialog);
							TextView messageText= (TextView)dialog.findViewById(R.id.messageText);
							TextView header= (TextView)dialog.findViewById(R.id.header);
							header.setText("Successful");
							messageText.setText(paymentDescription);
							messageText.setGravity(Gravity.CENTER);
							Button close = (Button)dialog.findViewById(R.id.close);
							close.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									new RunInBackground(LOAD_RESERVATIONS,null,null,null,-1,0,false).execute();
									dialog.dismiss();
								}
							});
							
							dialog.show();
	
							
							
						}
					}else{
						popupErrorText.setText( jsonObject.get(Utilities.ERROR_MESSAGE).toString());
						popupErrorLyt.setVisibility(View.VISIBLE);
						ProgressClass.finishProgress();
					}
				} catch (Exception e) {
					popupErrorText.setText(e.getMessage());
					popupErrorLyt.setVisibility(View.VISIBLE);
					ProgressClass.finishProgress();
					e.printStackTrace();
				}
				
				
				
				
				
				
				
			}
		}
	}
	
	private void setOrganization(int selectedOrgId) {
		currBalance = 0;
		resourcePrice = 0;
		if (selectedOrgId < 0) {

		} else {
			if (resourceTypeList == null || resourceTypeList.size() == 0) {
				currBalance = 0;
			} else {
				for (ResourceTypeModel mdl : resourceTypeList) {
					if (mdl.getOrgId() == selectedOrgId) {
						currBalance = (mdl.getPlanLimit() - mdl.getCurrentUsage());
						if(currBalance < 0 ){
							currBalance = 0;
						}
						resourcePrice = mdl.getPlanSplPrice();
						break;
					}
				}
			}
		}

	}
	
	private String getMeetingDetalilsAsString(ReservationModel model){
		String detl ="";
		if(model != null){
			detl = model.getReservationName();
		}
		return detl;
		
	}

	private void showMeetingDetails(final ReservationModel model){
		
		new RunInBackground(LOAD_SMARTSPACE,model,null,null,model.getReservedByOrg(),0,false).execute();
		dialog = new Dialog(mContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.reservation_popup);
		selectedHourStr = MhFormat.format(model.getStartDate());
		selectedCustDuration = model.getDuration();
		selectedDate=model.getStartDate();
		selectedDayStr = mdyFormat.format(selectedDate);
		Map<Integer, List<ReservationModel>> map = reservationMapForDays.get(selectedDayStr);
		if (map != null) {
			reservatonList = new ArrayList<ReservationModel>();
			for (int key : map.keySet()) {
				reservatonList.addAll(map.get(key));
			}
		}
		LinearLayout smartSpaceLyt = (LinearLayout)dialog.findViewById(R.id.smartSpaceLyt);
		/*if(smartSpaceList != null && smartSpaceList.size() <= 1){
			smartSpaceLyt.setVisibility(View.GONE);
		}else{
			smartSpaceLyt.setVisibility(View.VISIBLE);
		}*/
		Button	editMeeting = (Button)dialog.findViewById(R.id.editMeeting);
		Button deleteMeeting = (Button)dialog.findViewById(R.id.deleteMeeting);
		Button	saveUpdate = (Button)dialog.findViewById(R.id.saveUpdate);
		Button cancelUpdate = (Button)dialog.findViewById(R.id.cancelUpdate);
		TextView header =  (TextView)dialog.findViewById(R.id.header);
		if(model.getStartDate().before(new Date())){
			editMeeting.setVisibility(View.GONE);
			deleteMeeting.setVisibility(View.GONE);
			saveUpdate.setVisibility(View.GONE);
			cancelUpdate.setVisibility(View.GONE);
			header.setText("Reservation details");
		}else{
			editMeeting.setVisibility(View.VISIBLE);
			deleteMeeting.setVisibility(View.VISIBLE);
			saveUpdate.setVisibility(View.VISIBLE);
			cancelUpdate.setVisibility(View.VISIBLE);
			header.setText("Edit/Delete Reservation");
		}
		Spinner orgSpinner = (Spinner)dialog.findViewById(R.id.orgSpinner);
		confRoomSpinner = (Spinner)dialog.findViewById(R.id.confRoomSpinner);
		final TextView confRoomTV = (TextView)dialog.findViewById(R.id.confRoomTV);
		confRoomTV.setText(model.getResourceName());
		LinearLayout orgLyt = (LinearLayout)dialog.findViewById(R.id.orgLyt);
		final LinearLayout buttonLyt2 = (LinearLayout)dialog.findViewById(R.id.buttonLyt2);
		final LinearLayout buttonLyt = (LinearLayout)dialog.findViewById(R.id.buttonLyt);
		popupErrorText = (TextView)dialog.findViewById(R.id.errorText);
		popupErrorLyt = (LinearLayout)dialog.findViewById(R.id.errorLyt);
		final EditText name = (EditText)dialog.findViewById(R.id.name);
		final TextView endTime = (TextView)dialog.findViewById(R.id.endTime);
		
		final TextView startTime = (TextView)dialog.findViewById(R.id.startTime);
		final TextView startDate = (TextView)dialog.findViewById(R.id.startDate);
		startDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupErrorText.setText("");
				popupErrorLyt.setVisibility(View.GONE);
				final Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				new DatePickerDialog(getActivity(),
						new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int y,int m, int d) {
								cal.set(Calendar.YEAR, y);
								cal.set(Calendar.MONTH, m);
								cal.set(Calendar.DAY_OF_MONTH, d);
								Calendar selCal = Calendar.getInstance();
								selCal.setTime(selectedDate);

								if (selCal.get(Calendar.MONTH) != m) {
									reservationMapForDays = null;
								}
								changeDay(cal.getTime(), "09:00",startDate,startTime,endTime);

							}
						}, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal
								.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		startTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupErrorText.setText("");
				popupErrorLyt.setVisibility(View.GONE);
				final Calendar cal = Calendar.getInstance();
				try {
					cal.setTime(MhFormat.parse(selectedHourStr));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				new TimePickerDialog(getActivity(),
						new TimePickerDialog.OnTimeSetListener() {
							@Override
							public void onTimeSet(TimePicker view, int h,
									int min) {
								cal.set(Calendar.HOUR_OF_DAY, h);
								cal.set(Calendar.MINUTE, min);

								changeStartTime(cal,h,min,startTime,endTime);

							}
						}, cal.get(Calendar.HOUR_OF_DAY), cal
								.get(Calendar.MINUTE), false).show();

			}

		});
		endTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupErrorText.setText("");
				popupErrorLyt.setVisibility(View.GONE);
				final Calendar cal = Calendar.getInstance();
				try {
					Date meetingStartTime = null;
					meetingStartTime = mdyHMFormat.parse(selectedDayStr + " "+ selectedHourStr);
					cal.setTime(meetingStartTime);
					cal.add(Calendar.MINUTE, selectedCustDuration);
					
				} catch (ParseException e1) {
					try {
						cal.setTime(MhFormat.parse(selectedHourStr));
					} catch (ParseException e) {
					}
				}
				new TimePickerDialog(getActivity(),
						new TimePickerDialog.OnTimeSetListener() {
							@Override
							public void onTimeSet(TimePicker view, int h,
									int min) {
								cal.set(Calendar.HOUR_OF_DAY, h);
								cal.set(Calendar.MINUTE, min);

								changeEndTime(cal,h,min,endTime);

							}
						}, cal.get(Calendar.HOUR_OF_DAY), cal
								.get(Calendar.MINUTE), false).show();

			}

		});

		TextView smartSpace = (TextView)dialog.findViewById(R.id.smartSpace);
		ImageView close = (ImageView)dialog.findViewById(R.id.close);
		name.setEnabled(false);
		confRoomSpinner.setEnabled(false);
		endTime.setEnabled(false);
		startTime.setEnabled(false);
		startDate.setEnabled(false);
		endTime.setBackgroundResource(R.color.white);
		startTime.setBackgroundResource(R.color.white);
		startDate.setBackgroundResource(R.color.white);
		name.setText(model.getReservationName());
		endTime.setText(MhFormat.format(model.getEndTime()).toLowerCase());
		startTime.setText(MhFormat.format(model.getStartDate()).toLowerCase());
		startDate.setText(mdyFormat.format(model.getStartDate()));
		smartSpace.setText(model.getSmSpaceName());
		editMeeting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popupErrorText.setText("");
				popupErrorLyt.setVisibility(View.GONE);
				buttonLyt.setVisibility(View.GONE);
				buttonLyt2.setVisibility(View.VISIBLE);
				confRoomTV.setVisibility(View.GONE);
				confRoomSpinner.setVisibility(View.VISIBLE);
				endTime.setBackgroundResource(R.drawable.them_border_squre);
				startTime.setBackgroundResource(R.drawable.them_border_squre);
				startDate.setBackgroundResource(R.drawable.them_border_squre);
				name.setEnabled(true);
				endTime.setEnabled(true);
				startTime.setEnabled(true);
				startDate.setEnabled(true);
				confRoomSpinner.setEnabled(true);
			}
		});
		deleteMeeting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popupErrorText.setText("");
				popupErrorLyt.setVisibility(View.GONE);
				if(model.getEndTime().before(new Date())){
					popupErrorText.setText("This meeting is already over");
					popupErrorLyt.setVisibility(View.VISIBLE);
				}else{
					final Dialog cancelPop = new Dialog(mContext);
					cancelPop.requestWindowFeature(Window.FEATURE_NO_TITLE);
					cancelPop.setContentView(R.layout.custom_alert);
					// set the custom dialog components - text, image and button
					TextView text = (TextView) cancelPop.findViewById(R.id.title_view);
					text.setText("Do you want to delete this meeting?");
					Button okButton = (Button) cancelPop.findViewById(R.id.yes_button);
					// if button is clicked, close the custom dialog
					okButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							
							new RunInBackground(DELETE_RESERVATIONS,model,null,null,-1,0,false).execute();
							cancelPop.dismiss();
							
							
						}
					});
					Button cancelButton = (Button) cancelPop.findViewById(R.id.no_button);
					// if button is clicked, close the custom dialog
					cancelButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							cancelPop.dismiss();
						}
					});
					cancelPop.show();
				}
			}
		});
		saveUpdate.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				popupErrorText.setText("");
				popupErrorLyt.setVisibility(View.GONE);
				if(model.getEndTime().before(new Date())){
					popupErrorText.setText("This meeting is already over");
					popupErrorLyt.setVisibility(View.VISIBLE);
				}else{
					ReservationModel resModelUpdate = new ReservationModel();
					resModelUpdate.setDuration(selectedCustDuration);
					Date meetingStartTime = null;
					Date meetingEndTime = null;
					try {
						meetingStartTime = mdyHMFormat.parse(selectedDayStr + " "+ selectedHourStr);
						Calendar cal = Calendar.getInstance();
						cal.setTime(meetingStartTime);
						cal.add(Calendar.MINUTE, selectedCustDuration);
						meetingEndTime = cal.getTime();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String error=null;
					if (selectedResModel == null) {
						error = "Please select a resource";
					} else if (selectedHourStr == null) {
						error = "Please select a start time";
					} else if (meetingStartTime.before(new Date())) {
						error = "Past date/time not allowed";
					} else if (selectedCustDuration <= 0) {
						error = "Please select a duration";
					} else {
						if (reservatonList != null) {
							for (ReservationModel resrvModel : reservatonList) {
								if(resrvModel.getResourceId() != selectedResModel.getResourceId()){
									continue;
								}
								if(resrvModel.getReservationId() == model.getReservationId()){
									continue;
								}
								if ((meetingStartTime.after(resrvModel.getStartDate()) && meetingStartTime
										.before(resrvModel.getEndTime()))
										|| (meetingEndTime.before(resrvModel.getEndTime()) && meetingEndTime
												.after(resrvModel.getStartDate()))
										|| (resrvModel.getStartDate().after(
												meetingStartTime) && resrvModel
												.getStartDate().before(meetingEndTime))
										|| (resrvModel.getEndTime().before(meetingEndTime) && resrvModel
												.getEndTime().after(meetingStartTime))) {
									error = "This slot is not available";
									break;
								}

							}
						}
					}
					if (error != null) {
						popupErrorText.setText(error);
						popupErrorLyt.setVisibility(View.VISIBLE);
					} else {
						popupErrorText.setText("");
						popupErrorLyt.setVisibility(View.GONE);

						resModelUpdate.setEndTime(meetingEndTime);
						resModelUpdate.setReservationId(model.getReservationId());
						resModelUpdate.setReservationName(name.getText().toString());
						resModelUpdate.setReservedByOrg(model.getReservedByOrg());
						resModelUpdate.setReservedByOrgName(model.getReservedByOrgName());
						resModelUpdate.setReservedByUser(model.getReservedByUser());
						resModelUpdate.setReservedByUserName(model.getReservedByUserName());
						resModelUpdate.setResourceId(selectedResModel.getResourceId());
						resModelUpdate.setResourceName(selectedResModel.getResourceName());
						resModelUpdate.setResourceType(selectedResModel.getResourceType());
						resModelUpdate.setSmSpaceId(model.getSmSpaceId());
						resModelUpdate.setSmSpaceName(model.getSmSpaceName());
						resModelUpdate.setStartDate(meetingStartTime);			
						new RunInBackground(UPDATE_RESERVATIONS, resModelUpdate,null, null,-1,0,false).execute();
					}

				}
			}
		});
		cancelUpdate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				name.setEnabled(false);
				confRoomSpinner.setEnabled(false);
				endTime.setEnabled(false);
				startTime.setEnabled(false);
				startDate.setEnabled(false);
				endTime.setBackgroundResource(R.color.white);
				startTime.setBackgroundResource(R.color.white);
				startDate.setBackgroundResource(R.color.white);
				confRoomTV.setVisibility(View.VISIBLE);
				confRoomSpinner.setVisibility(View.GONE);
				name.setText(model.getReservationName());
				endTime.setText(MhFormat.format(model.getEndTime()).toLowerCase());
				startTime.setText(MhFormat.format(model.getStartDate()).toLowerCase());
				startDate.setText(mdyFormat.format(model.getStartDate()));
				int selectedPosition = 0;
				int index = 0;
				if(smartSpaceList != null && smartSpaceList.size() > 0){
					for(ResourceModel resModel : smartSpaceList.get(0).getResourceList()){
						if(resModel.getResourceId() == model.getResourceId()){
							selectedPosition = index;
							break;
						}	
						index++;
					}
				}
				
				confRoomSpinner.setSelection(selectedPosition);
				buttonLyt2.setVisibility(View.GONE);
				buttonLyt.setVisibility(View.VISIBLE);
				name.setEnabled(false);
			}
		});
		close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
		
	}
	private void changeDay(Date newdate, String selectedHourText,TextView startDate,TextView startTime,TextView endTime) {
		popupErrorText.setText("");
		popupErrorLyt.setVisibility(View.GONE);
		selectedDate = newdate;
		selectedDayStr = mdyFormat.format(newdate);
		try {
			if(mdyFormat.parse(mdyFormat.format(newdate)).after(mdyFormat.parse(mdyFormat.format(new Date())))){
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				if (mdyFormat.format(new Date()).equals(mdyFormat.format(selectedDate))) {
					cal.add(Calendar.MINUTE, 5);
					selectedHourText = MhFormat.format(cal.getTime());
				} else {
					selectedHourText = "09:00 am";
				}
				selectedHourStr = selectedHourText;
				startDate.setText(selectedDayStr);
				startTime.setText(selectedHourText.toLowerCase());
				try {
					cal.setTime(mdyHMFormat.parse(selectedDayStr+" "+selectedHourText));
					cal.add(Calendar.MINUTE, 30);
					selectedCustDuration =30;
					endTime.setText(MhFormat.format(cal.getTime()).toLowerCase());
				} catch (ParseException e) {
				}
				if (reservationMapForDays != null) {
					Map<Integer, List<ReservationModel>> map = reservationMapForDays.get(selectedDayStr);
					if (map != null) {
						reservatonList = new ArrayList<ReservationModel>();
						for (int key : map.keySet()) {
							reservatonList.addAll(map.get(key));
						}
					}
				} else {
					new RunInBackground(LOAD_RESERVATIONS,null,null,null,-1,0,false).execute();
				}
			}else{
				popupErrorText.setText("Past date/time not allowed");
				popupErrorLyt.setVisibility(View.VISIBLE);
			}
		} catch (ParseException e1) {
		}
		
		

	}

	private void reserveNewMeeting(String tag){
		
		Fragment fragment = null;
		
			fragment = new SelectRoomType();
			FragmentManager fragmentManager = getFragmentManager();
			Utilities.loadFragment(fragmentManager,fragment,R.id.frame_container,false);	
		
		 
	}
	
	private void loadDailyData() {
		dayGridView.removeAllViews();
		List<View> viewToRemove = new ArrayList<View>(1);
		for(int index =0; index <= dayRLLyt.getChildCount();index++){
			if(dayRLLyt.getChildAt(index) instanceof TextView){
				viewToRemove.add(dayRLLyt.getChildAt(index));
			}
		}
		for(View v : viewToRemove){
			dayRLLyt.removeView(v)	;
		}
		dayRLLyt.setVisibility(View.VISIBLE);
		dayGridView.setVisibility(View.VISIBLE);
		weekGridView.setVisibility(View.GONE);
		monthGridView.setVisibility(View.GONE);
		daysInWeek.setVisibility(View.GONE);

		width = appresources.getDisplayMetrics().widthPixels;
		int lytWidth = (int)(width-40*scale);
		selectedPeriod.setText(dMMMFormat.format(currCalendar.getTime()));
		Map<Integer,List<ReservationModel>> hourlyMeetingMap = null;
		if(reservationMapForDays != null && reservationMapForDays.size() > 0){
			hourlyMeetingMap = reservationMapForDays.get(mdyFormat.format(currCalendar.getTime()));
		}
		List<ReservationModel> meetings = null;
		if(hourlyMeetingMap != null && hourlyMeetingMap.size() > 0 ){
			meetings = new ArrayList<ReservationModel>();
			for(int hr : hourlyMeetingMap.keySet()){
				 meetings.addAll(hourlyMeetingMap.get(hr));
			}
			
		}
		for (int hour = 0; hour < 24; hour++) {
			if(meetings != null && meetings.size() > 0 ){
				
				Map<Integer,List<Integer>> multiMeetingMap = new HashMap<Integer, List<Integer>>();
				
				for(int firstIndex=0; firstIndex < meetings.size();firstIndex++){
					ReservationModel resModel = meetings.get(firstIndex);
					if(resModel.getReservedByUser() != Utilities.loggedInUser.getUserId()){
						continue;
					}
					List<Integer> idList = new ArrayList<Integer>();
					if(multiMeetingMap.get(resModel.getReservationId()) !=null){
						idList = multiMeetingMap.get(resModel.getReservationId());
						
					}else{
						idList.add(resModel.getReservationId());
						multiMeetingMap.put(resModel.getReservationId(), idList);
					}
					for(int nextIndex=firstIndex+1; nextIndex < meetings.size();nextIndex++){
						ReservationModel nextResModel = meetings.get(nextIndex);
						if(nextResModel.getReservedByUser() != Utilities.loggedInUser.getUserId()){
							continue;
						}
						if(resModel.getStartDate().getTime() == nextResModel.getStartDate().getTime()
								|| (nextResModel.getStartDate().after(resModel.getStartDate()) 
										&& (nextResModel.getStartDate().before(resModel.getEndTime()) ))
								|| (nextResModel.getStartDate().before(resModel.getStartDate()) 
										&& (nextResModel.getEndTime().after(resModel.getStartDate()) ))		
								){
							idList = multiMeetingMap.get(resModel.getReservationId());
							idList.add(nextResModel.getReservationId());
							multiMeetingMap.put(nextResModel.getReservationId(), idList);
						}
					}
				}
				for(ReservationModel resModel : meetings){
					if(resModel.getReservedByUser() != Utilities.loggedInUser.getUserId()){
						continue;
					}
					int startMin = 0;
					int startHour = 0;
					SimpleDateFormat Mh24Format = new SimpleDateFormat("HH:mm");
					String[] meetTime = Mh24Format.format(resModel.getStartDate()).split(":");
					try{
						startHour = Integer.parseInt(meetTime[0]);
						startMin = Integer.parseInt(meetTime[1]);
						if(startHour !=hour){
							continue;
						}
					}catch(Exception e){
						
					}
					int duration = resModel.getDuration();
					TextView textView = new TextView(mContext);
					List<Integer> idList = multiMeetingMap.get(resModel.getReservationId());
					int startPos = 0;
					for(int id : idList){
						if(id == resModel.getReservationId()){
							break;
						}
						startPos++;
					}
					int meetWidth = (int)(lytWidth/idList.size());
					LayoutParams parm = new LayoutParams(meetWidth,(int)(duration*scale));
					textView.setLayoutParams(parm);
					textView.setBackgroundResource(R.drawable.curved_border_filled);
					textView.setText(getMeetingDetalilsAsString(resModel));
					textView.setTextColor(appresources.getColor(R.color.black));
					textView.setTextSize(12);
					textView.setGravity(Gravity.CENTER);
					textView.setY((int)((startHour*60)+startMin)*scale);
					textView.setX((int)(40*scale)+(startPos*meetWidth));
					textView.bringToFront();
					dayRLLyt.addView(textView);
					textView.setTag(resModel);
					textView.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							ReservationModel model = (ReservationModel)v.getTag();
							showMeetingDetails(model);
							
						}
					});
				}
				
			}
			LinearLayout linLyt = new LinearLayout(mContext);
			linLyt.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,(int)(60*scale)));
			linLyt.setBackgroundResource(R.drawable.them_11_border_squre);
			linLyt.setTag(mdyFormat.format(currCalendar.getTime())+"-"+hour);
			/*linLyt.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String tag = v.getTag().toString();
					reserveNewMeeting(tag);
					
				}
			});*/
			dayGridView.addView(linLyt);
			
			TextView hourTV = new TextView(mContext);
			hourTV.setTextColor(appresources.getColor(R.color.black));
			hourTV.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,(int)(60*scale)));
			if(hour == 0 || hour ==24){
				hourTV.setText("12a");
			}else if(hour ==12){
				hourTV.setText("12p");
			}else if(hour <12){
				hourTV.setText(hour+"a");
			}else{
				hourTV.setText((hour-12)+"p");
			}
			
			
			//hourTV.setBackgroundResource(R.drawable.bluerectangleborderwhiteinside);
			hoursInday.addView(hourTV);
			
			
		}
		showSwipes();
		 
	}
	private void loadWeeklyData() {
		List<View> viewToRemove = new ArrayList<View>(1);
		for(int index =0; index <= dayRLLyt.getChildCount();index++){
			if(dayRLLyt.getChildAt(index) instanceof TextView){
				viewToRemove.add(dayRLLyt.getChildAt(index));
			}
		}
		for(View v : viewToRemove){
			dayRLLyt.removeView(v)	;
		}
		dayRLLyt.setVisibility(View.VISIBLE);
		dayGridView.setVisibility(View.GONE);
		weekGridView.setVisibility(View.VISIBLE);
		monthGridView.setVisibility(View.GONE);
		weekGridView.removeAllViews();
		daysInWeek.setVisibility(View.VISIBLE);
		hoursInday.removeAllViews();
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		float scale = appresources.getDisplayMetrics().density;
		int startDay = currCalendar.get(Calendar.DAY_OF_WEEK);
		int lytWidth = (int)((display.getWidth()-(30*scale))/7);
		LinearLayout empty = (LinearLayout)rootView.findViewById(R.id.empty);
		empty.setLayoutParams(new RelativeLayout.LayoutParams((int)scale*30,(int)scale*30));
		Calendar dayCal = (Calendar) currCalendar.clone();
		dayCal.add(Calendar.DAY_OF_WEEK, - startDay+1);
		String str = dMMMFormat.format(dayCal.getTime());
		days.removeAllViews();
		for(int day = 1 ; day <= 7 ; day++){
			TextView textView = new TextView(mContext);
			textView.setLayoutParams(new LayoutParams(lytWidth,(int)(30*scale)));
			
			textView.setText(dMMMFormat.format(dayCal.getTime()));
			textView.setTextSize(12);
			textView.setTextColor(appresources.getColor(R.color.black));
			textView.setGravity(Gravity.CENTER);
			textView.setBackgroundResource(R.color.white);
			days.addView(textView);

			dayCal.add(Calendar.DAY_OF_MONTH, 1);
			
		}
		dayCal.add(Calendar.DAY_OF_MONTH, -1);
		str = str + " to "+dMMMFormat.format(dayCal.getTime());
		selectedPeriod.setText(str);
		dayCal = (Calendar) currCalendar.clone();
		dayCal.add(Calendar.DAY_OF_WEEK, - startDay+1);
		int index=0;
		for(int day = 1 ; day <= 7 ; day++){
			Map<Integer,List<ReservationModel>> hourlyMeetingMap = null;
			String dayStr =mdyFormat.format(dayCal.getTime());
			if(reservationMapForDays != null && reservationMapForDays.size() > 0){
				hourlyMeetingMap = reservationMapForDays.get(dayStr);
			}
			List<ReservationModel> meetings = null;
			if(hourlyMeetingMap != null && hourlyMeetingMap.size() > 0 ){
				meetings = new ArrayList<ReservationModel>();
				for(int hr : hourlyMeetingMap.keySet()){
					 meetings.addAll(hourlyMeetingMap.get(hr));
				}
				
			}
			for (int hour = 0; hour < 24; hour++) {
				Calendar dayCal1 = (Calendar) currCalendar.clone();
				dayCal1.add(Calendar.DAY_OF_WEEK, - startDay+1);
				dayCal1.add(Calendar.DAY_OF_WEEK, (index%7));
				LinearLayout linLyt = new LinearLayout(mContext);
				linLyt.setLayoutParams(new LayoutParams(lytWidth,(int)(60*scale)));
				linLyt.setBackgroundResource(R.drawable.them_11_border_squre);
				weekGridView.addView(linLyt);
				
				linLyt.setTag(mdyFormat.format(dayCal1.getTime())+"-"+(int)(index/7));
				/*linLyt.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						String tag = v.getTag().toString();
						reserveNewMeeting(tag);
						
					}
				});*/
				if(meetings != null && meetings.size() > 0){
					Map<Integer,List<Integer>> multiMeetingMap = new HashMap<Integer, List<Integer>>();
					
					for(int firstIndex=0; firstIndex < meetings.size();firstIndex++){
						ReservationModel resModel = meetings.get(firstIndex);
						if(resModel.getReservedByUser() != Utilities.loggedInUser.getUserId()){
							continue;
						}
						List<Integer> idList = new ArrayList<Integer>();
						if(multiMeetingMap.get(resModel.getReservationId()) !=null){
							idList = multiMeetingMap.get(resModel.getReservationId());
							
						}else{
							idList.add(resModel.getReservationId());
							multiMeetingMap.put(resModel.getReservationId(), idList);
						}
						for(int nextIndex=firstIndex+1; nextIndex < meetings.size();nextIndex++){
							ReservationModel nextResModel = meetings.get(nextIndex);
							if(nextResModel.getReservedByUser() != Utilities.loggedInUser.getUserId()){
								continue;
							}
							if(resModel.getStartDate().getTime() == nextResModel.getStartDate().getTime()
									|| (nextResModel.getStartDate().after(resModel.getStartDate()) 
											&& (nextResModel.getStartDate().before(resModel.getEndTime()) ))
									|| (nextResModel.getStartDate().before(resModel.getStartDate()) 
											&& (nextResModel.getEndTime().after(resModel.getStartDate()) ))		
									){
								idList = multiMeetingMap.get(resModel.getReservationId());
								idList.add(nextResModel.getReservationId());
								multiMeetingMap.put(nextResModel.getReservationId(), idList);
							}
						}
					}
					for(ReservationModel resModel : meetings){
						if(resModel.getReservedByUser() != Utilities.loggedInUser.getUserId()){
							continue;
						}
						
						int startMin = 0;
						int startHour = 0;
						SimpleDateFormat Mh24Format = new SimpleDateFormat("HH:mm");
						String[] meetTime = Mh24Format.format(resModel.getStartDate()).split(":");
						try{
							startHour = Integer.parseInt(meetTime[0]);
							startMin = Integer.parseInt(meetTime[1]);
							if(startHour !=hour){
								continue;
							}
						}catch(Exception e){
							
						}
						List<Integer> idList = multiMeetingMap.get(resModel.getReservationId());
						int startPos = 0;
						for(int id : idList){
							if(id == resModel.getReservationId()){
								break;
							}
							startPos++;
						}
						int meetWidth = (int)(lytWidth/idList.size());
						int duration = resModel.getDuration();
						TextView textView = new TextView(mContext);
						LayoutParams parm = new LayoutParams(meetWidth,(int)(duration*scale));
						textView.setLayoutParams(parm);
						textView.setBackgroundResource(R.drawable.curved_border_filled);
						textView.setText(getMeetingDetalilsAsString(resModel));
						textView.setSingleLine(true);
						textView.setTextColor(appresources.getColor(R.color.black));
						textView.setTextSize(12);
						textView.setGravity(Gravity.CENTER);
						textView.setY((int)((startHour*60)+startMin)*scale);
						textView.setX(((day-1)*lytWidth+(startPos*meetWidth))+((int)35*scale));
						textView.bringToFront();
						dayRLLyt.addView(textView);
						textView.setTag(resModel);
						textView.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								ReservationModel model = (ReservationModel)v.getTag();
								showMeetingDetails(model);
								
							}
						});
					}
					
				}
				TextView hourTV = new TextView(mContext);
				if(hour == 0 || hour ==24){
					hourTV.setText("12a");
				}else if(hour ==12){
					hourTV.setText("12p");
				}else if(hour <12){
					hourTV.setText(hour+"a");
				}else{
					hourTV.setText((hour-12)+"p");
				}
				hourTV.setTextColor(appresources.getColor(R.color.black));
				hourTV.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,(int)(60*scale)));
				hoursInday.addView(hourTV);
				index++;
			}
			dayCal.add(Calendar.DAY_OF_MONTH, 1);
		}

		showSwipes();
	}
		
	
	private void loadMonthlyData() {
		dayGridView.setVisibility(View.GONE);
		weekGridView.setVisibility(View.GONE);
		monthGridView.setVisibility(View.VISIBLE);
		monthGridView.setClickable(true);
		daysInWeek.setVisibility(View.VISIBLE);
		dayRLLyt.setVisibility(View.GONE);
		monthGridView.removeAllViews();
		LinearLayout empty = (LinearLayout)rootView.findViewById(R.id.empty);
		empty.setLayoutParams(new RelativeLayout.LayoutParams(0,0));
		float scale = appresources.getDisplayMetrics().density;
		selectedPeriod.setText(MMMyyyyFormat.format(currCalendar.getTime()));
		int lytWidth = (int)(width/7);
		
		Calendar startDate = (Calendar) currCalendar.clone();
		int currMonth = startDate.get(Calendar.MONTH);
		int noOFWeeks = 4;
		startDate.set(Calendar.DAY_OF_MONTH, 1);
		
		int startDay = startDate.get(Calendar.DAY_OF_WEEK);
		if(currMonth==1){
			if(((startDate.get(Calendar.YEAR)%4) == 0) ||startDay != Calendar.SUNDAY){
				noOFWeeks = 5;
			}
		}else if(currMonth == 0 || currMonth == 2 || currMonth ==4 || currMonth ==6 || currMonth ==7 || currMonth ==9 || currMonth ==11){
			if(startDay >= Calendar.FRIDAY){
				noOFWeeks = 6;
			}else{
				noOFWeeks = 5;
			}
		}else{
			if(startDay == Calendar.SATURDAY){
				noOFWeeks = 6;
			}else{
				noOFWeeks = 5;
			}
		}
		int lytHeight = (int)((appresources.getDisplayMetrics().heightPixels-(int)(148*scale))/noOFWeeks);
		startDate.add(Calendar.DAY_OF_WEEK, - startDay+1);
		
		//int startDay = startDate.get(Calendar.DAY_OF_WEEK);
		
		//boolean started = false;
		Map<Integer,List<ReservationModel>> hourlyMeetingMap = null;
		days.removeAllViews();
		String[] daysArray = new String[]{"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
		for(int day = 1 ; day <= 7 ; day++){
			TextView textView = new TextView(mContext);
			textView.setLayoutParams(new LayoutParams(lytWidth,(int)(30*scale)));
			
			textView.setText(daysArray[day-1]);
			textView.setTextSize(12);
			textView.setTextColor(appresources.getColor(R.color.black));
			textView.setGravity(Gravity.CENTER);
			textView.setBackgroundResource(R.color.white);
			days.addView(textView);
			
		}
		for (int index = 0; index < noOFWeeks; index++) {
			
			for(int day = 1 ; day <= 7 ; day++){
				LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View convertView = inflater.inflate(R.layout.hour_layout, null);
				TextView timeTv = (TextView) convertView.findViewById(R.id.timeText);
				LinearLayout content =  (LinearLayout) convertView.findViewById(R.id.content);
				RelativeLayout topLyt =  (RelativeLayout) convertView.findViewById(R.id.topLyt);
				topLyt.setLayoutParams(new RelativeLayout.LayoutParams(lytWidth,lytHeight));
				//timeTv.setLayoutParams(new RelativeLayout.LayoutParams(lytWidth,(int)(60*scale)));
				//if(started){
					//if(currMonth == startDate.get(Calendar.MONTH)){
						convertView.setTag(startDate.getTime());
						timeTv.setText(startDate.get(Calendar.DAY_OF_MONTH)+"");
					/*}else{
						//break;
*/					//}
				/*}else{
					if(currMonth == startDate.get(Calendar.MONTH)){
						if(day == startDay){
							convertView.setTag(startDate.getTime());
							timeTv.setText(startDate.get(Calendar.DAY_OF_MONTH)+"");
							started = true;
						}
					}
				}*/
				//if(started ){
					if(reservationMapForDays != null && reservationMapForDays.size() > 0){
						hourlyMeetingMap = reservationMapForDays.get(mdyFormat.format(startDate.getTime()));
					}
					startDate.add(Calendar.DATE, 1);
					convertView.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Date selDate = (Date)v.getTag();
							currCalendar.setTime(selDate);
							selectedDayType=DATE_TYPE_DAILY;
							selectedDate=selDate;
							setSelectedDay(dayViewseperator,dayView, DATE_TYPE_DAILY);
							
						}
					});
					
					
						
						if(hourlyMeetingMap != null && hourlyMeetingMap.size() > 0 ){
							for(int key : hourlyMeetingMap.keySet()){
								List<ReservationModel> meetings = hourlyMeetingMap.get(key);
								for(ReservationModel resModel : meetings){
									if(resModel.getReservedByUser() != Utilities.loggedInUser.getUserId()){
										continue;
									}
									int startMin = 0;
									int startHour = 0;
									String[] meetTime = MhFormat.format(resModel.getStartDate()).split(":");
									try{
										startHour = Integer.parseInt(meetTime[0]);
										startMin = Integer.parseInt(meetTime[1]);
										
									}catch(Exception e){
										
									}
									int duration = resModel.getDuration();
									TextView textView = new TextView(mContext);
									LayoutParams parm = new LayoutParams(lytWidth-(int)(6*scale),LayoutParams.WRAP_CONTENT);
									textView.setLayoutParams(parm);
									textView.setBackgroundResource(R.drawable.curved_border_filled);
									textView.setText(getMeetingDetalilsAsString(resModel));
									textView.setSingleLine(true);
									textView.setTextColor(appresources.getColor(R.color.black));
									textView.setTextSize(12);
									textView.setGravity(Gravity.CENTER);
									textView.setTag(resModel);
									textView.setOnClickListener(new OnClickListener() {
										
										@Override
										public void onClick(View v) {
											ReservationModel model = (ReservationModel)v.getTag();
											showMeetingDetails(model);
											
										}
									});
									
									textView.bringToFront();
									content.addView(textView);
									
								}
							}	
						}		
				//}
				monthGridView.addView(convertView);
			}		
		}
	
		showSwipes();
		
	}
	private void changeStartTime(Calendar cal,int hour,int min,TextView startTime,TextView endTime) {
		popupErrorText.setText("");
		popupErrorLyt.setVisibility(View.GONE);
		cal.setTime(selectedDate);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, min);
		
		
		try {
			if(cal.getTime().before(new Date())){
				
				popupErrorText.setText("Past date/time not allowed");
				popupErrorLyt.setVisibility(View.VISIBLE);
			}else{
				String nextAvlTime = MhFormat.format(cal.getTime());
				selectedHourStr = nextAvlTime;
				selectedCustDuration = 30;
				startTime.setText(selectedHourStr.toLowerCase());
				try {
					cal.setTime(mdyHMFormat.parse(selectedDayStr+" "+selectedHourStr));
					cal.add(Calendar.MINUTE, 30);
					endTime.setText(MhFormat.format(cal.getTime()).toLowerCase());
				} catch (ParseException e) {
				}
			}
			
			
			/*if(cal.getTime().before(MhFormat.parse(MhFormat.format(new Date())))){
				popupErrorText.setText("Past date/time not allowed");
				popupErrorLyt.setVisibility(View.VISIBLE);
			}else{
				selectedHourStr = MhFormat.format(cal.getTime());;
				startTime.setText(selectedHourStr.toLowerCase());
				selectedCustDuration = 30;
				cal.setTime(mdyHMFormat.parse(selectedDayStr+" "+selectedHourStr));
				cal.add(Calendar.MINUTE, 30);
				selectedCustDuration =30;
				endTime.setText(MhFormat.format(cal.getTime()).toLowerCase());
			
			}*/
		} catch (Exception e) {
		}
		
	}
	
	private void changeEndTime(Calendar cal,int hour,int min,TextView endTime) {
		popupErrorText.setText("");
		popupErrorLyt.setVisibility(View.GONE);
		cal.setTime(selectedDate);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, min);
		
		try {
			
			if(cal.getTime().before(new Date())){
				
				popupErrorText.setText("Past date/time not allowed");
				popupErrorLyt.setVisibility(View.VISIBLE);
			}else if((MhFormat.parse(selectedHourStr).after(MhFormat.parse(MhFormat.format(cal.getTime()))))){
				popupErrorText.setText("End time should be after start time");
				popupErrorLyt.setVisibility(View.VISIBLE);
			}else{
				String selEndTime = MhFormat.format(cal.getTime());
				Calendar startCal = Calendar.getInstance();
				try {
					startCal.setTime(mdyHMFormat.parse(selectedDayStr + " "+ selectedHourStr));
				} catch (ParseException e1) {
				}
				selectedCustDuration = (int)((cal.getTime().getTime() - startCal.getTime().getTime())/(1000*60));
				endTime.setText(selEndTime.toLowerCase());	
				
			}
		
			
			
			
			
			
			/*if(MhFormat.parse(MhFormat.format(cal.getTime())).before(MhFormat.parse(MhFormat.format(new Date())))){
				
				errorText.setText("Past date/time not allowed");
				errorLyt.setVisibility(View.VISIBLE);
			}else if((MhFormat.parse(selectedHourStr).after(MhFormat.parse(MhFormat.format(cal.getTime()))))){
				errorText.setText("End time should be after start time");
				errorLyt.setVisibility(View.VISIBLE);
			}else{
				String selEndTime = MhFormat.format(cal.getTime());
				Calendar startCal = Calendar.getInstance();
				startCal.setTime(MhFormat.parse(selectedHourStr));
				selectedCustDuration = (int)((cal.getTime().getTime() - startCal.getTime().getTime())/(1000*60));
				endTime.setText(selEndTime.toLowerCase());	
			}*/
		} catch (ParseException e) {
		}
		
	}
	private void showSwipes(){
		final ImageView right = new ImageView(mContext);
		RelativeLayout.LayoutParams rightParams = new RelativeLayout.LayoutParams((int)scale*50,(int)scale*50);
		rightParams.addRule(RelativeLayout.CENTER_VERTICAL);
		rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		right.setLayoutParams(rightParams);
		right.setImageResource(R.drawable.rightarrow);
		containerView.addView(right);
		
		final ImageView left = new ImageView(mContext);
		RelativeLayout.LayoutParams leftParams = new RelativeLayout.LayoutParams((int)scale*50,(int)scale*50);
		leftParams.addRule(RelativeLayout.CENTER_VERTICAL);
		leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		left.setLayoutParams(leftParams);
		left.setImageResource(R.drawable.leftarrow);
		containerView.addView(left);
		
		Animation fadeOut = new AlphaAnimation(1, 0);
	    fadeOut.setInterpolator(new AccelerateInterpolator());
	    fadeOut.setDuration(3000);

	    fadeOut.setAnimationListener(new AnimationListener()
	    {
	            public void onAnimationEnd(Animation animation) 
	            {
	            	right.setVisibility(View.GONE);
	            	left.setVisibility(View.GONE);
	            }
	            public void onAnimationRepeat(Animation animation) {}
	            public void onAnimationStart(Animation animation) {}
	    });

	    right.startAnimation(fadeOut);
	   left.startAnimation(fadeOut);
		
	}
	class CustomPaymentGatewayImpl extends CustomPaymentGateway {
		public CustomPaymentGatewayImpl(Context context,ReservationModel resModel) {
			super(context, Utilities.loggedInUser, null, null, resModel);
		
		}


		@Override
		public void updateTransaction(Object responseObj, boolean forced,Dialog strpPopup) {
			if (responseObj != null) {
				if(responseObj instanceof Token){
					Token token = (Token)responseObj;
					new RunInBackground(UPDATE_RESERVATIONS_STATUS, resModel,token.getId(), paymentGateWay,-1,PAYABLE_AMOUNT,true).execute();
				}else{
					showError();
					ProgressClass.finishProgress();
				}
			} else if (forced) {
				if (strpPopup != null)
					strpPopup.dismiss();
				ProgressClass.finishProgress();
			}else{
				ProgressClass.finishProgress();
				showError();
			}
			
		}
	}
	private final class GestureListener extends SimpleOnGestureListener {

		private static final int SWIPE_THRESHOLD = 100;
		private static final int SWIPE_VELOCITY_THRESHOLD = 100;

		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			boolean result = false;
			try {
				float diffY = e2.getY() - e1.getY();
				float diffX = e2.getX() - e1.getX();
				if (Math.abs(diffX) > Math.abs(diffY)) {
					if (Math.abs(diffX) > SWIPE_THRESHOLD
							&& Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
						if (diffX > 0) {
							onSwipeRight();
						} else {
							onSwipeLeft();
						}
					}
					result = true;
				} else if (Math.abs(diffY) > SWIPE_THRESHOLD
						&& Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
					if (diffY > 0) {
						onSwipeBottom();
					} else {
						onSwipeTop();
					}
				}
				result = true;

			} catch (Exception exception) {
				exception.printStackTrace();
			}
			return result;
		}
	}

	public void onSwipeRight() {
		loadNextData(-1);
	}

	public void onSwipeLeft() {
		loadNextData(1);
	}

	public void onSwipeTop() {

	}

	public void onSwipeBottom() {

	}
}
