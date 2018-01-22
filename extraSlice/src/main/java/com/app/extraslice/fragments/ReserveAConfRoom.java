package com.app.extraslice.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.app.extraslice.model.SmartSpaceModel;
import com.app.extraslice.utils.CustomPaymentGateway;
import com.app.extraslice.utils.ProgressClass;
import com.app.extraslice.utils.Utilities;
import com.stripe.model.Token;

import org.codehaus.jettison.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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



public class ReserveAConfRoom extends Fragment {
	float scale =1;
	int index = 0;
	static Resources appRes ;
	Map<Integer, List<Integer>> bookedSlotsForConfRooms = new HashMap<Integer, List<Integer>>(1);
	//Dialog dialog;
	List<Organization> apprvdOrgList = new ArrayList<Organization>(1);
	private final String ADD_RESERVATION = "ADD_RESERVATION";
	private final String DELETE_RESERVATIONS = "DELETE_RESERVATIONS";
	private final String UPDATE_RESERVATIONS_STATUS = "UPDATE_RESERVATIONS_STATUS";

	private final String LOAD_SMARTSPACE = "LOAD_SMARTSPACE";
	private final String LOAD_CURR_SCHEDULES = "LOAD_CURR_SCHEDULES";
	//RadioGroup radioPay;s
	//RadioButton payaplRB,stripeRB;
	String paymentGateWay="Stripe";
	Map<Integer, Bitmap> confRoomImageMap = new HashMap<Integer, Bitmap>();
	SmartSpaceModel selectedSmSpace;
	List<SmartSpaceModel> smartSpaceList;
	ResourceModel selectedResource;
	Organization selectedOrganization;
	
	ViewTreeObserver vto;
	int currTimeLocation = 0;
	Map<String, Map<Integer, List<ReservationModel>>> reservationMapForDays;
	Date selectedDate;
	EditText name;
	String selectedHourStr;
	int selectedCustDuration;
	Context mContext;
	Spinner orgSpinner,restypeSpinner;
	SimpleDateFormat mdyHMFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a",Locale.US);
	SimpleDateFormat mdyFormat = new SimpleDateFormat("MM/dd/yyyy");
	SimpleDateFormat MMMyyyyFormat = new SimpleDateFormat("MMM-yyyy");
	SimpleDateFormat dMMMFormat = new SimpleDateFormat("d-MMM");
	SimpleDateFormat MhFormat = new SimpleDateFormat("hh:mm a",Locale.US);
	SimpleDateFormat ymd_Format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	String selectedDayStr;
	LinearLayout errorLyt,confRoomLyt;
	TextView errorText;
	//List<ResourceTypeModel> resourceTypeList;
	
	static AdminAccountModel admAcctModel;
	double currBalance = 0;
	double resourcePrice = 0;
	
	boolean   smspaceloaded,currSchedLoaded;
	int currResPosition = 0;
	View todayseperator, tomorrowseperator, selectDayseperator;
	TextView today, tomorrow, selectDay;
	List<ReservationModel> reservatonList;
	int selectedHour;
	View rootView;
	LinearLayout orgLyt;
	List<String> minList = new ArrayList<String>();
	List<String> hourList = new ArrayList<String>(24);
	Spinner sspaceSpinner;
	TextView startDate;
	TextView startTime,endTime;
	String roomType = "Conference room";
	public ReserveAConfRoom() {

		this.selectedDate = new Date();
		this.selectedHour = 7;
		this.reservationMapForDays = null;
		this.selectedCustDuration = 30;

	}
	public ReserveAConfRoom(Date selectedDate, int selectedHour, Map<String, Map<Integer, List<ReservationModel>>> reservationMapForDays) {
		this.selectedDate = selectedDate;
		this.selectedHour = selectedHour;
		this.reservationMapForDays = reservationMapForDays;
		this.selectedCustDuration = 30;

	}
	   
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		mContext = getActivity();
		appRes = getResources();
		scale= appRes.getDisplayMetrics().density;
		//getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		rootView = inflater.inflate(R.layout.reserve_conf_room, container,false);
		String[] ampm = new String[]{"am","pm"};
		for(String ap : ampm){
			for (int ind = 0; ind < 12; ind++) {
				String str = "";
				if(ind == 0){
					str = "12:00 "+ap;
				}else if (ind < 10) {
					str = "0" + ind + ":00 "+ap;
				} else {
					str = ind + ":00 "+ap;
				}
				hourList.add(str);
			}
		}
		selectedHourStr = hourList.get(selectedHour);
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		new RunInBackground(LOAD_SMARTSPACE, -1, null, null,null,0,null).execute();
		for (Organization org : Utilities.loggedInUser.getOrgList()) {
			if (org.isApproved()) {
				apprvdOrgList.add(org);
			}
		}
		confRoomLyt = (LinearLayout) rootView.findViewById(R.id.confRoomLyt);
		startDate = (TextView) rootView.findViewById(R.id.startDate);
		startTime = (TextView) rootView.findViewById(R.id.startTime);
		todayseperator = (View) rootView.findViewById(R.id.todayseperator);
		tomorrowseperator = (View) rootView.findViewById(R.id.tomorrowseperator);
		selectDayseperator = (View) rootView.findViewById(R.id.selectDayseperator);
		today = (TextView) rootView.findViewById(R.id.today);
		tomorrow = (TextView) rootView.findViewById(R.id.tomorrow);
		selectDay = (TextView) rootView.findViewById(R.id.selectDay);
		endTime = (TextView) rootView.findViewById(R.id.endTime);
		orgLyt = (LinearLayout) rootView.findViewById(R.id.orgLyt);
		if (apprvdOrgList != null && apprvdOrgList.size() <= 1) {
			orgLyt.setVisibility(View.GONE);
		} else {
			orgLyt.setVisibility(View.VISIBLE);
		}
		orgSpinner = (Spinner) rootView.findViewById(R.id.orgSpinner);
		restypeSpinner = (Spinner) rootView.findViewById(R.id.restypeSpinner);
		sspaceSpinner = (Spinner) rootView.findViewById(R.id.sspaceSpinner);
		errorLyt = (LinearLayout) rootView.findViewById(R.id.errorLyt);
		errorText = (TextView) rootView.findViewById(R.id.errorText);
		name = (EditText) rootView.findViewById(R.id.name);
		today.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				errorText.setText("");
				errorLyt.setVisibility(View.GONE);
				setSelectedDay(today, todayseperator, new Date(),MhFormat.format(new Date()));
			}
		});
		tomorrow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				errorText.setText("");
				errorLyt.setVisibility(View.GONE);
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				cal.add(Calendar.DATE, 1);
				setSelectedDay(tomorrow, tomorrowseperator, cal.getTime(),"09:00 am");
			}
		});
		startTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				errorText.setText("");
				errorLyt.setVisibility(View.GONE);
				final Calendar cal = Calendar.getInstance();
				try {
					cal.setTime(MhFormat.parse(selectedHourStr));
				} catch (ParseException e) {
				}
				new TimePickerDialog(getActivity(),
						new TimePickerDialog.OnTimeSetListener() {
							@Override
							public void onTimeSet(TimePicker view, int h,int min) {
								errorText.setText("");
								errorLyt.setVisibility(View.GONE);
								cal.set(Calendar.HOUR_OF_DAY, h);
								cal.set(Calendar.MINUTE, min);
								changeStartTime(cal,h,min);
							}
						}, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show();
			}

		});
		endTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				errorText.setText("");
				errorLyt.setVisibility(View.GONE);
				final Calendar cal = Calendar.getInstance();
				try {
					Date meetingStartTime = null;
					meetingStartTime = mdyHMFormat.parse(selectedDayStr + " "+ selectedHourStr);
					cal.setTime(meetingStartTime);
					cal.add(Calendar.MINUTE, selectedCustDuration);
					
				} catch (ParseException e1) {
					try {
						cal.setTime(MhFormat.parse(endTime.getText().toString()));
					} catch (ParseException e) {
					}
				}
				new TimePickerDialog(getActivity(),
						new TimePickerDialog.OnTimeSetListener() {
					
							@Override
							public void onTimeSet(TimePicker view, int h,
									int min) {
								errorText.setText("");
								errorLyt.setVisibility(View.GONE);
								cal.set(Calendar.HOUR_OF_DAY, h);
								cal.set(Calendar.MINUTE, min);
								changeEndTime(cal,h,min);
								
							}
						}, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show();

			}

		});
		startDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				errorText.setText("");
				errorLyt.setVisibility(View.GONE);
				final Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				final Calendar checkCal = Calendar.getInstance();
				new DatePickerDialog(getActivity(),
						new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int y,
									int m, int d) {
								cal.set(Calendar.YEAR, y);
								cal.set(Calendar.MONTH, m);
								cal.set(Calendar.DAY_OF_MONTH, d);
								try {
									if((mdyFormat.format(cal.getTime()).equals(mdyFormat.format(new Date())))||
											(mdyFormat.parse(mdyFormat.format(cal.getTime())).after(mdyFormat.parse(mdyFormat.format(new Date()))))){
										Calendar selCal = Calendar.getInstance();
										selCal.setTime(selectedDate);

										if (selCal.get(Calendar.MONTH) != m) {
											reservationMapForDays = null;
										}
										setSelectedDay(selectDay, selectDayseperator,cal.getTime(), "09:00 am");
									}else{
										errorText.setText("Past date/time not allowed");
										errorLyt.setVisibility(View.VISIBLE);
									}
								} catch (ParseException e) {
								}
							}
						}, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		selectDay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				errorText.setText("");
				errorLyt.setVisibility(View.GONE);
				final Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				final Calendar checkCal = Calendar.getInstance();
				new DatePickerDialog(getActivity(),
						new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int y,
									int m, int d) {
								cal.set(Calendar.YEAR, y);
								cal.set(Calendar.MONTH, m);
								cal.set(Calendar.DAY_OF_MONTH, d);
								try {
									if((mdyFormat.format(cal.getTime()).equals(mdyFormat.format(new Date())))||
											(mdyFormat.parse(mdyFormat.format(cal.getTime())).after(mdyFormat.parse(mdyFormat.format(new Date()))))){
										Calendar selCal = Calendar.getInstance();
										selCal.setTime(selectedDate);

										if (selCal.get(Calendar.MONTH) != m) {
											reservationMapForDays = null;
										}
										setSelectedDay(selectDay, selectDayseperator,cal.getTime(), "09:00 am");
									}else{
										errorText.setText("Past date/time not allowed");
										errorLyt.setVisibility(View.VISIBLE);
									}
								} catch (ParseException e) {
								}
							}
						}, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		
		
		int i = 0;
		for (int ind = 0; ind <= 23; ind++) {
			String str = "";
			if (ind < 10) {
				str = "0" + ind + ":";
			} else {
				str = ind + ":";
			}
			for (int ind2 = 0; ind2 < 60; ind2++) {
				String str2 = "";
				if (ind2 < 10) {
					str2 = "0" + ind2;
				} else {
					str2 = ind2 + "";
				}
				minList.add(str + str2);
			}

		}
		if (mdyFormat.format(new Date()).equals(mdyFormat.format(selectedDate))) {
			setSelectedDay(today, todayseperator, selectedDate, selectedHourStr);
		} else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.DAY_OF_MONTH, 1);
			if (mdyFormat.format(selectedDate).equals(
					mdyFormat.format(cal.getTime()))) {
				setSelectedDay(tomorrow, tomorrowseperator, selectedDate,selectedHourStr);
			} else {
				Calendar selCal = Calendar.getInstance();
				selCal.setTime(selectedDate);
				if (selCal.get(Calendar.MONTH) != cal.get(Calendar.MONTH)) {
					reservationMapForDays = null;
				}
				setSelectedDay(selectDay, selectDayseperator, selectedDate,selectedHourStr);
			}
		}
		Button myBookings = (Button)rootView.findViewById(R.id.myBookings);
		ScrollView confRoomScrView = (ScrollView)rootView.findViewById(R.id.confRoomScrView);
		int lytHeight = (int)((appRes.getDisplayMetrics().heightPixels-(int)(333*scale)));
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,lytHeight );
		params.setMargins(5,5,10,0);
		params.addRule(RelativeLayout.BELOW,R.id.topLyt);
		//confRoomScrView.setLayoutParams(params);
		myBookings.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				errorText.setText("");
				errorLyt.setVisibility(View.GONE);
				FragmentManager frgManager = getFragmentManager();
				Fragment fragment = new MyReservations(selectedDate);
				Utilities.loadFragment(frgManager, fragment,R.id.frame_container, false);
			}
		});
		rootView.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				/*if (keyCode == KeyEvent.KEYCODE_BACK) {
					Fragment fragment = null;
					fragment = new HomeFragment();
					if (fragment != null) {
						FragmentManager fragmentManager = getFragmentManager();
						Utilities.loadFragment(fragmentManager,fragment,R.id.frame_container,true);
					}
					return true;
				} else {*/
					return true;
				//}
			}
		});
		return rootView;
	}

	private void changeStartTime(Calendar cal,int hour,int min) {
		cal.setTime(selectedDate);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, min);
		try {
			if(cal.getTime().before(new Date())){
				errorText.setText("Past date/time not allowed");
				errorLyt.setVisibility(View.VISIBLE);
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
		} catch (Exception e) {
		}
		
		loadConfRoom();
		
	}
	
	private void changeEndTime(Calendar cal,int hour,int min) {
		errorText.setText("");
		errorLyt.setVisibility(View.GONE);
		cal.setTime(selectedDate);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, min);
		try {
			if(cal.getTime().before(new Date())){
				errorText.setText("Past date/time not allowed");
				errorLyt.setVisibility(View.VISIBLE);
			}else if((MhFormat.parse(selectedHourStr).after(MhFormat.parse(MhFormat.format(cal.getTime()))))){
				errorText.setText("End time should be after start time");
				errorLyt.setVisibility(View.VISIBLE);
			}else{
				String selEndTime = MhFormat.format(cal.getTime());
				Calendar startCal = Calendar.getInstance();
				try {
					startCal.setTime(mdyHMFormat.parse(selectedDayStr + " "+ selectedHourStr));
				} catch (ParseException e1) {
				}
				selectedCustDuration = (int)((cal.getTime().getTime() - startCal.getTime().getTime())/(1000*60));
				endTime.setText(selEndTime.toLowerCase());	
				loadConfRoom();
			}
		} catch (ParseException e) {
		}		
	}
	
	
	private void setSelectedDay(TextView selTV, View selViewSperator,Date newdate, String selectedHourText) {
		selectedDate = newdate;
		todayseperator.setBackgroundResource(R.color.white);
		tomorrowseperator.setBackgroundResource(R.color.white);
		selectDayseperator.setBackgroundResource(R.color.white);
		selViewSperator.setBackgroundResource(R.color.theme_light_blue);
		selectedDayStr = mdyFormat.format(newdate);
		startDate.setText(selectedDayStr);
		startTime.setText(selectedHourText.toLowerCase());
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		if (mdyFormat.format(new Date()).equals(mdyFormat.format(selectedDate))) {
			cal.add(Calendar.MINUTE, 5);
			selectedHourText = MhFormat.format(cal.getTime());
		} else {
			selectedHourText = "09:00 am";
		}
		selectedHourStr = selectedHourText;
		startTime.setText(selectedHourText.toLowerCase());
		try {
			cal.setTime(mdyHMFormat.parse(selectedDayStr+" "+selectedHourText));
			cal.add(Calendar.MINUTE, 30);
			endTime.setText(MhFormat.format(cal.getTime()).toLowerCase());
		} catch (ParseException e) {
		}
		if (reservationMapForDays != null) {
			Map<Integer, List<ReservationModel>> map = reservationMapForDays.get(selectedDayStr);
			reservatonList = new ArrayList<ReservationModel>();
			if (map != null) {
				
				for (int key : map.keySet()) {
					reservatonList.addAll(map.get(key));
				}
			}
			currSchedLoaded = true;
			
			loadConfRoom();
		} else {
			new RunInBackground(LOAD_CURR_SCHEDULES, -1, null, null,null,0,null).execute();
		}
		
	}

	private void loadOrganization(List<Organization> apprvdOrgList) {

		final List<Map<String, Object>> orgMapList = new ArrayList<Map<String, Object>>();
		if (apprvdOrgList != null && apprvdOrgList.size() > 0) {
			Map<String, Object> map = new HashMap<String, Object>();
			if (apprvdOrgList.size() == 1) {
				selectedOrganization = apprvdOrgList.get(0);
				orgLyt.setVisibility(View.GONE);
			} else {
				orgLyt.setVisibility(View.VISIBLE);
				map = new HashMap<String, Object>();
				map.put("Select Organization", "Select Organization");
				orgMapList.add(map);
				for (Organization orgModel : apprvdOrgList) {
					map = new HashMap<String, Object>();
					map.put(orgModel.getOrgName(), orgModel);
					orgMapList.add(map);
				}
				if (orgMapList != null && orgMapList.size() > 0) {
					int selectedTimePosition = 0;

					CustomAdapter orgAdapter = new CustomAdapter(mContext, orgMapList);
					orgSpinner.setAdapter(orgAdapter);
					orgSpinner.setSelection(selectedTimePosition);
					orgSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
						public void onItemSelected(AdapterView<?> arg0,View view, int position, long id) {
							if (view.getTag() instanceof ResourceModel) {
								selectedOrganization = (Organization) view.getTag();
							} else {
								selectedOrganization = null;
							}
						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {
							// selectedCustDuration=0;
						}

					});
				}
			}

		}
	}

	private void bookNow() {

		ReservationModel resModel = new ReservationModel();
		String error = null;
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

		if (selectedSmSpace == null) {
			error = "Please select a location";
		} else if (selectedResource == null) {
			error = "Please select a resource";
		} else if (selectedHourStr == null) {
			error = "Please select a start time";
		} else if (meetingStartTime.before(new Date())) {
			error = "Past date/time not allowed";
		} else if (selectedCustDuration <= 0) {
			error = "Please select a duration";
		} else if (selectedOrganization == null) {
			error = "Please select an organization";
		}else if (name.getText().toString().trim().length() == 0) {
			error = "Please enter meeting name";
		} else {
			if (reservatonList != null) {
				for (ReservationModel resrvModel : reservatonList) {
					if(resrvModel.getResourceId() != selectedResource.getResourceId()){
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
			errorText.setText(error);
			errorLyt.setVisibility(View.VISIBLE);
		} else {
			errorText.setText("");
			errorLyt.setVisibility(View.GONE);
			resModel.setDuration(selectedCustDuration);
			resModel.setEndTime(meetingEndTime);
			resModel.setReservedByUser(Utilities.loggedInUser.getUserId());
			resModel.setResourceId(selectedResource.getResourceId());
			resModel.setResourceName(selectedResource.getResourceName());
			resModel.setResourceType(selectedResource.getResourceType());
			resModel.setSmSpaceId(selectedSmSpace.getSmSpaceId());
			resModel.setSmSpaceName(selectedSmSpace.getSmSapceName());
			resModel.setStartDate(meetingStartTime);
			resModel.setReservedByOrg(selectedOrganization.getOrgId());
			resModel.setReservedByOrgName(selectedOrganization.getOrgName());
			resModel.setReservedByUserName(Utilities.loggedInUser.getUserName());
			resModel.setReservationName(name.getText().toString());
			new RunInBackground(ADD_RESERVATION, -1, resModel, null,null,0,null).execute();
		}

	}
	/*private void selectPaymentOption(){
		int selected = radioPay.getCheckedRadioButtonId();
		RadioButton selectedButton = (RadioButton) dialog.findViewById(selected);
		if (selectedButton != null){
			if(selectedButton.getText().toString().trim().equalsIgnoreCase("Paypal")) {
				paymentGateWay = "Paypal";
			}else if(selectedButton.getText().toString().trim().equalsIgnoreCase("Stripe")) {
				paymentGateWay = "Stripe";
			}
		}
	}*/
	private void showPaymentGateways(final ReservationModel resModel,final double balance,String paymentDescription){
		
		try {
			CustomPaymentGatewayImpl.PAYABLE_AMOUNT      	   = balance;
			CustomPaymentGatewayImpl.DISPLAY_PYMNT_DESCRIPTION=paymentDescription;
			CustomPaymentGatewayImpl.STRIPE_PAYMENT_DESCRIPTION = "(Android) Payment for conf room by "+ Utilities.loggedInUser.getEmail();
			CustomPaymentGatewayImpl.USER_ID             = Utilities.loggedInUser.getUserId();
			CustomPaymentGatewayImpl.USER_NAME           = Utilities.loggedInUser.getUserName();
			CustomPaymentGatewayImpl.PURPOSE=CustomPaymentGateway.PURPOSE_NEW_RESERVATION;
			try {
				CustomPaymentGatewayImpl.STRP_API_KEY=Utilities.decode(admAcctModel.getStrpSecKey());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			CustomPaymentGatewayImpl strpGateway = new CustomPaymentGatewayImpl(mContext,resModel);
			strpGateway.makeStripePayment();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	class RunInBackground extends AsyncTask<Void, Void, Void> {

		ReservationModel reservationModel = null;
		JSONObject reservationAdded = null;
		String errorMessage;
		String purpose;
		String cardToken;
		String subscriptionId;
		int smartSpaceId;
		List<ReservationModel> reservationList;
		SmartspaceBO smBO =null;
		double amountPaid =0;
		String pymntGateway;
		String status;
		public RunInBackground(String purpose, int smartSpaceId,ReservationModel reservationModel, String cardToken,String subscriptionId,double amountPaid,String pymntGateway) {
			this.reservationModel = reservationModel;
			this.purpose = purpose;
			this.cardToken = cardToken;
			this.smartSpaceId = smartSpaceId;
			this.subscriptionId = subscriptionId;
			this.amountPaid = amountPaid;
			this.pymntGateway = pymntGateway;
		}

		@Override
		protected void onPreExecute() {
			ProgressClass.startProgress(mContext);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				smBO = new SmartspaceBO(mContext);
				if (purpose.equals(LOAD_SMARTSPACE)) {
					smartSpaceList = smBO.getAllSmartSpace(0, 0);
				} else if (purpose.equals(LOAD_CURR_SCHEDULES)) {
					Calendar nextMonth = Calendar.getInstance();
					nextMonth.setTime(selectedDate);
					nextMonth.set(Calendar.DAY_OF_MONTH, 1);
					String selectedDayStr = mdyFormat.format(nextMonth.getTime());
					String monthStartTime = "";
					String monthEndTime = "";
					ymd_Format.setTimeZone(TimeZone.getTimeZone("UTC"));
					try {
						monthStartTime = ymd_Format.format(mdyHMFormat.parse(selectedDayStr + " 00:00 am"));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					nextMonth.add(Calendar.MONTH, 1);
					selectedDayStr = mdyFormat.format(nextMonth.getTime());
					ymd_Format.setTimeZone(TimeZone.getTimeZone("UTC"));
					try {
						monthEndTime = ymd_Format.format(mdyHMFormat.parse(selectedDayStr + " 00:00 am"));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					reservationList = smBO.getCurrentSchedulesForPeriod(monthStartTime, monthEndTime);
				}else if (purpose.equals(ADD_RESERVATION)) {
					reservationAdded = smBO.addReservation(reservationModel,null,amountPaid);
				}else if (purpose.equals(DELETE_RESERVATIONS)) {
					boolean deleted = smBO.deleteReservation(reservationModel);


				}else if (purpose.equals(UPDATE_RESERVATIONS_STATUS)) {
					Long trialEndsAt = reservationModel.getStartDate().getTime();
					long currTime = new Date().getTime();
					int trialPeriods  = (int)((trialEndsAt - currTime)/(1000*60*60*24));
					status = smBO.updateReservationStatus(reservationModel, this.cardToken, trialPeriods, amountPaid, pymntGateway);
				}
				
			} catch (Exception e) {
				errorMessage = e.getMessage();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (errorMessage != null) {
				errorText.setText(errorMessage);
				errorLyt.setVisibility(View.VISIBLE);
				ProgressClass.finishProgress();
			} else {
				 if (purpose.equals(DELETE_RESERVATIONS)) {
					 ProgressClass.finishProgress();
				}else if (purpose.equals(LOAD_SMARTSPACE)) {
					loadSmartSpaces();
					admAcctModel = smBO.accountModel;
					smspaceloaded = true;
					if ( smspaceloaded) {
						loadOrganization(apprvdOrgList);
						ProgressClass.finishProgress();
					}
				}else if(purpose.equals(LOAD_CURR_SCHEDULES)) {
					if (reservationList != null && reservationList.size() > 0) {
						reservationMapForDays = new HashMap<String, Map<Integer, List<ReservationModel>>>();
						for (ReservationModel resModel : reservationList) {
							String day = mdyFormat.format(resModel.getStartDate());
							Map<Integer, List<ReservationModel>> reservationMapForHours = new HashMap<Integer, List<ReservationModel>>();
							if (reservationMapForDays.get(day) == null) {
								reservationMapForDays.put(day,reservationMapForHours);
							}
							reservationMapForHours = reservationMapForDays.get(day);
							int hour = Integer.parseInt(MhFormat.format(resModel.getStartDate()).split(":")[0]);
							List<ReservationModel> reservationForHrList = new ArrayList<ReservationModel>();
							if (reservationMapForHours.get(hour) == null) {
								reservationMapForHours.put(hour,reservationForHrList);
							}
							reservationForHrList = reservationMapForHours.get(hour);
							reservationForHrList.add(resModel);
						}
						setSelectedDay(today, todayseperator, selectedDate,selectedHourStr);
					}
					loadConfRoom();
					currSchedLoaded =true;
					if ( smspaceloaded && currSchedLoaded) {
						loadOrganization(apprvdOrgList);
						ProgressClass.finishProgress();
					}
				}else if (purpose.equals(ADD_RESERVATION)) {
					if (reservationAdded != null) {
						try {
							if (reservationAdded.get(Utilities.STATUS_STRING) != null){
								boolean makePayment = false;
								try{
									makePayment = reservationAdded.getBoolean("makePayment");
								}catch(Exception e){
									
								}
								if(reservationAdded.get(Utilities.STATUS_STRING).toString().equals(Utilities.STATUS_SUCCESS)) {
									if(makePayment){
										String paymentDescription = null;
										boolean haveAccount = false;
										try{
											paymentDescription = reservationAdded.getString("paymentDescription");
										}catch(Exception e){
											
										}
										try{
											haveAccount = reservationAdded.getBoolean("haveAccount");
										}catch(Exception e){
											
										}
										final double payableAmount = reservationAdded.getDouble("payableAmount");
										final ReservationModel resModel = new ReservationModel().jSonToObject(reservationAdded.getString("ReservationModel"));
										if(haveAccount){
											final Dialog dialog = new Dialog(mContext);
											dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
											dialog.setContentView(R.layout.payment_popup);
											TextView addPyTxt = (TextView)dialog.findViewById(R.id.addPyTxt);
											Button cancel = (Button)dialog.findViewById(R.id.cancel);
											Button submit = (Button)dialog.findViewById(R.id.submit);
											addPyTxt.setText(paymentDescription);
											cancel.setOnClickListener(new OnClickListener() {
												
												@Override
												public void onClick(View arg0) {
													new RunInBackground(DELETE_RESERVATIONS, -1, resModel, null,null,0,null).execute();
													dialog.dismiss();
												}
											});
											submit.setOnClickListener(new OnClickListener() {
												
												@Override
												public void onClick(View arg0) {
													new RunInBackground(UPDATE_RESERVATIONS_STATUS, -1, resModel, null,null,payableAmount,null).execute();
													dialog.dismiss();
												}
											});
											dialog.show();
										}else{
											showPaymentGateways(resModel,payableAmount,paymentDescription);
										}
										
									} else {
										String msgText = "Reserved successfully.";
										Toast toast = Toast.makeText(mContext,msgText, Toast.LENGTH_SHORT);
										toast.setGravity(Gravity.CENTER, 0, 0);
										toast.show();
										FragmentManager frgManager = getFragmentManager();
										Fragment fragment = new MyReservations(selectedDate);
										Utilities.loadFragment(frgManager, fragment,R.id.frame_container, false);
									}
								}else if (reservationAdded.get(Utilities.STATUS_STRING).toString().equals(Utilities.STATUS_FAILED)) {
									if(reservationAdded.get(Utilities.ERROR_MESSAGE) != null ){
										errorText.setText(reservationAdded.get(Utilities.ERROR_MESSAGE).toString());
										errorLyt.setVisibility(View.VISIBLE);
										ProgressClass.finishProgress();
									}
								}
							}
						} catch (Exception e) {
							errorText.setText(e.getLocalizedMessage());
							errorLyt.setVisibility(View.VISIBLE);
							ProgressClass.finishProgress();
						}
					} else {
						errorText.setText("Failed to add reservation");
						errorLyt.setVisibility(View.VISIBLE);
						ProgressClass.finishProgress();
					}
				}else if (purpose.equals(UPDATE_RESERVATIONS_STATUS)) {
					ProgressClass.finishProgress();
					if(status.equals(Utilities.STATUS_SUCCESS)){
						String msgText = "Reserved successfully.";
						Toast toast = Toast.makeText(mContext,msgText, Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
						FragmentManager frgManager = getFragmentManager();
						Fragment fragment = new MyReservations(selectedDate);
						Utilities.loadFragment(frgManager, fragment,R.id.frame_container, false);
					}else{
						new RunInBackground(DELETE_RESERVATIONS, -1, reservationModel, null,null,0,null).execute();
						errorText.setText(status);
						errorLyt.setVisibility(View.VISIBLE);
					}
					
				} 
			}
		}
	}

	

	private void loadSmartSpaces() {
		if (smartSpaceList != null && smartSpaceList.size() > 0) {
			if(smartSpaceList.size() == 1){
				sspaceSpinner.setVisibility(View.GONE);
				selectedSmSpace = smartSpaceList.get(0);
				fillResFilter();
				if(currSchedLoaded){
					
					loadConfRoom();
				}
			}else{
				sspaceSpinner.setVisibility(View.VISIBLE);
				List<Map<String, Object>> sspaceNameList = new ArrayList<Map<String, Object>>();
				Map<String, Object> map = new HashMap<String, Object>();
				for (SmartSpaceModel item : smartSpaceList) {
					map = new HashMap<String, Object>();
					map.put(item.getSmSapceName(), item);
					sspaceNameList.add(map);
				}
				CustomAdapter sspaceAdapter = new CustomAdapter(mContext,sspaceNameList, Gravity.CENTER,R.color.theme_color_trnsp_11);
				sspaceSpinner.setAdapter(sspaceAdapter);
				sspaceSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> arg0,View view, int position, long id) {
						if (view.getTag() instanceof SmartSpaceModel) {
							selectedSmSpace = (SmartSpaceModel) view.getTag();
							fillResFilter();
							if(currSchedLoaded){
								
								loadConfRoom();
							}
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
					}
				});
			}
		}else{
			sspaceSpinner.setVisibility(View.GONE);
		}
	}
	private void loadConfRoom(){
		confRoomLyt.removeAllViews();
		Date meetingStartTime = null;
		Date meetingEndTime = null;
		try {
			meetingStartTime = mdyHMFormat.parse(selectedDayStr + " "+ selectedHourStr);
			Calendar cal = Calendar.getInstance();
			cal.setTime(meetingStartTime);
			cal.add(Calendar.MINUTE, selectedCustDuration);
			meetingEndTime = cal.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (selectedSmSpace != null) {

			for(ResourceModel reModel : selectedSmSpace.getResourceList()){
				if(roomType == null || reModel.getResourceType().equalsIgnoreCase(roomType)){
					boolean isAvailable=true;
					if (reservatonList != null) {
						for (ReservationModel resrvModel : reservatonList) {
							if(resrvModel.getResourceId() != reModel.getResourceId()){
								continue;
							}
							if (meetingStartTime.equals(resrvModel.getStartDate()) ||(meetingStartTime.after(resrvModel.getStartDate()) && meetingStartTime
									.before(resrvModel.getEndTime()))
									|| (meetingEndTime.before(resrvModel.getEndTime()) && meetingEndTime
											.after(resrvModel.getStartDate()))
									|| (resrvModel.getStartDate().after(
											meetingStartTime) && resrvModel
											.getStartDate().before(meetingEndTime))
									|| (resrvModel.getEndTime().before(meetingEndTime) && resrvModel
											.getEndTime().after(meetingStartTime))) {
								if(resrvModel.getReservedByOrgName().equalsIgnoreCase("Individual")){
									isAvailable=false;
								}else{
									isAvailable=false;
								}
								break;
							}
						}
					}
					loadConfData(reModel,  isAvailable);
				}
			}
		}
	}
	private void loadConfData(ResourceModel reModel,boolean isAvailable){
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View convertView = inflater.inflate( R.layout.conf_room_layout, null);
		TextView confRoomName = (TextView)convertView.findViewById(R.id.confRoomName);
		confRoomName.setText(reModel.getResourceName());
		final HorizontalScrollView horzScrView = (HorizontalScrollView)convertView.findViewById(R.id.horzScrView);
		vto = horzScrView.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
		    public void onGlobalLayout() {
		    	String time = startTime.getText().toString();
		    	int startInt = Integer.parseInt(time.split(":")[0]);
		    	if(startInt == 12){
		    		if(time.endsWith("am")){
		    			startInt = 0;
			    	}
		    	}else{
		    		if(time.endsWith("pm")){
		    			startInt = startInt+12;
			    	}
		    	}
		    	horzScrView.scrollTo((int)(startInt*60*scale), 0);
		    }
		});
		vto.dispatchOnGlobalLayout();
		RelativeLayout hourLyt = (RelativeLayout)convertView.findViewById(R.id.hourLyt);
		hourLyt.removeAllViews();
		for(int i=0;i<24;i++){
			View scrView = inflater.inflate( R.layout.hour_row, null);
			TextView timeText = (TextView)scrView.findViewById(R.id.timeText);
			if(i==0 || i==24){
				timeText.setText("12a");
			}else if(i<12){
				timeText.setText(i+"a");
			}else if(i==12){
				timeText.setText(i+"p");
			}else{
				timeText.setText((i-12)+"p");
			}
			LinearLayout firstHHR = (LinearLayout)scrView.findViewById(R.id.firstHHR);
			LinearLayout secHHR = (LinearLayout)scrView.findViewById(R.id.secHHR);
			firstHHR.setTag(hourList.get(i));
			secHHR.setTag(hourList.get(i).replaceAll(":00", ":30"));
			firstHHR.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					errorText.setText("");
					errorLyt.setVisibility(View.GONE);
					final Calendar cal = Calendar.getInstance();
					
					try {
						cal.setTime(MhFormat.parse(v.getTag().toString()));
						new TimePickerDialog(getActivity(),
							new TimePickerDialog.OnTimeSetListener() {
								@Override
								public void onTimeSet(TimePicker view, int h,int min) {
									errorText.setText("");
									errorLyt.setVisibility(View.GONE);
									cal.set(Calendar.HOUR_OF_DAY, h);
									cal.set(Calendar.MINUTE, min);
									changeStartTime(cal,h,min);
								}
							}, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			secHHR.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					errorText.setText("");
					errorLyt.setVisibility(View.GONE);
					final Calendar cal = Calendar.getInstance();
					try {
						cal.setTime(MhFormat.parse(v.getTag().toString()));
							new TimePickerDialog(getActivity(),
								new TimePickerDialog.OnTimeSetListener() {
									@Override
									public void onTimeSet(TimePicker view, int h,int min) {
										errorText.setText("");
										errorLyt.setVisibility(View.GONE);
										cal.set(Calendar.HOUR_OF_DAY, h);
										cal.set(Calendar.MINUTE, min);
										changeStartTime(cal,h,min);
									}
								}, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show();
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			});
			scrView.setX(i*60*scale);
			hourLyt.addView(scrView);
			final ImageView imgfor = (ImageView)convertView.findViewById(R.id.imgfor);
			final ImageView imgback = (ImageView)convertView.findViewById(R.id.imgback);
			Animation fadeOut = new AlphaAnimation(1, 0);
			fadeOut.setInterpolator(new AccelerateInterpolator());
			fadeOut.setDuration(2000);
			fadeOut.setAnimationListener(new AnimationListener() {
				public void onAnimationEnd(Animation animation) {
					imgfor.setVisibility(View.GONE);
					imgback.setVisibility(View.GONE);
				}
				
				public void onAnimationRepeat(Animation animation) {
				}

				public void onAnimationStart(Animation animation) {
				}
			});
			imgfor.startAnimation(fadeOut);
			imgback.startAnimation(fadeOut);
		}
		if(reservatonList != null){
			for(ReservationModel res : reservatonList){
				if(reModel.getResourceId() != res.getResourceId()){
					continue;
				}
				SimpleDateFormat Mh24Format = new SimpleDateFormat("HH:mm");
				int hour = Integer.parseInt(Mh24Format.format(res.getStartDate()).split(":")[0]);
				int min = Integer.parseInt(Mh24Format.format(res.getStartDate()).split(":")[1]);
				
				int startIndex = (hour*60+min);
				RelativeLayout meetingLyt = new RelativeLayout(mContext);
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int)(res.getDuration()*scale),(int)(60*scale));
				meetingLyt.setLayoutParams(params);
				meetingLyt.setX(startIndex*scale);
				meetingLyt.setY(20*scale);
				meetingLyt.setTag(res);
				meetingLyt.setBackgroundResource(R.drawable.red_border_squre);
				meetingLyt.setPadding(1,1,1,1);
				for(int index = -5;index <= res.getDuration()+5; index++ ){
					View shading = new View(mContext);
					RelativeLayout.LayoutParams shadingParam = new RelativeLayout.LayoutParams((int)(1*scale),(int)(60*scale));
					shading.setLayoutParams(shadingParam);
					shading.setX(index*scale);
					shading.setRotation(10);
					if((index%2) == 0){
						shading.setBackgroundResource(R.color.red);
					}else{
						shading.setBackgroundResource(R.color.white);
					}
					meetingLyt.setClipChildren(true);
					meetingLyt.addView(shading);
					meetingLyt.bringChildToFront(shading);
				}
				meetingLyt.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						ReservationModel res =(ReservationModel) v.getTag();
						final Dialog dialog = new Dialog(mContext);
						dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						dialog.setContentView(R.layout.reservation_detl_popup);
						
						TextView name = (TextView)dialog.findViewById(R.id.name);
						TextView reservedBy = (TextView)dialog.findViewById(R.id.reservedBy);
						TextView startTime = (TextView)dialog.findViewById(R.id.startTime);
						TextView endTime = (TextView)dialog.findViewById(R.id.endTime);
						Button close = (Button)dialog.findViewById(R.id.close);

						String resvedByName = res.getReservedByOrgName();
						if(resvedByName.equalsIgnoreCase("Individual")){
							resvedByName = res.getReservedByUserName();
						}
						name.setText(res.getReservationName());
						reservedBy.setText(resvedByName);
						startTime.setText(MhFormat.format(res.getStartDate()).toLowerCase());
						endTime.setText(MhFormat.format(res.getEndTime()).toLowerCase());
						close.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								dialog.dismiss();
								
							}
						});
						dialog.show();
					}
				});
				hourLyt.addView(meetingLyt);
			}
		}
		
		Button bookNow = (Button)convertView.findViewById(R.id.bookNow);
		bookNow.setClickable(true);
		bookNow.setTag(reModel);
		if(isAvailable){
			bookNow.setTextColor(appRes.getColor(R.color.white));
			bookNow.setBackgroundResource(R.color.theme_dark_blue);
			bookNow.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					errorText.setText("");
					errorLyt.setVisibility(View.GONE);
					if(v.getTag() instanceof ResourceModel){
						selectedResource = (ResourceModel)v.getTag();
						bookNow();
					}
				}
			});
		}else{
			bookNow.setText("Not available");
			bookNow.setBackgroundResource(R.color.theme_dark_blue);
			bookNow.setTextColor(appRes.getColor(R.color.darker_gray));
		}
		TextView description = (TextView)convertView.findViewById(R.id.description);
		description.setText(reModel.getResourceDesc());
		confRoomLyt.addView(convertView);
	}
	
	/**
	 * 
	 * @author irshad
	 *
	 */
	class CustomPaymentGatewayImpl extends CustomPaymentGateway {
		
		public CustomPaymentGatewayImpl(Context context,ReservationModel resModel) {
			super(context, Utilities.loggedInUser, null, null, resModel);
		
		}

		@Override
		public void updateTransaction(Object responseObj, boolean forced,Dialog strpPopup) {
			if (responseObj != null) {
				if(responseObj instanceof Token){
					Token token = (Token)responseObj;
					new RunInBackground(UPDATE_RESERVATIONS_STATUS, -1, resModel, token.getId(),null,PAYABLE_AMOUNT,"Stripe").execute();
				}else{
					showError();
					ProgressClass.finishProgress();
				}
				
				
			} else if (forced) {
				new RunInBackground(DELETE_RESERVATIONS, -1, resModel, null,null,0,null).execute();
				if (strpPopup != null)
					strpPopup.dismiss();
				ProgressClass.finishProgress();
			}else{
				ProgressClass.finishProgress();
				showError();
			}
		}
	}
	private void showConfRoomDetails(ResourceModel resModel){
		final Dialog dialog = new Dialog(mContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.conf_room_details);
		TextView confRoomName = (TextView) dialog.findViewById(R.id.confRoomName);
		TextView confRoomDesc = (TextView) dialog.findViewById(R.id.confRoomDesc);
		ImageView confRomImage = (ImageView) dialog.findViewById(R.id.confRomImage);
		if(confRoomImageMap.get(resModel.getResourceId()) == null){
			new LoadImage(confRomImage, resModel.getImageUrl(),resModel.getResourceId()).execute();
		}else{
			confRomImage.setImageBitmap(confRoomImageMap.get(resModel.getResourceId()));
		}
		Button close = (Button) dialog.findViewById(R.id.close);
		confRoomName.setText(resModel.getResourceName());
		confRoomDesc.setText("4 Seats | TV | White board | Monitor");
		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	class LoadImage extends AsyncTask<Void, Void, Void> {
		ImageView itemImage;
		String url;
		Bitmap bitMap;
		int resourceId = -1;
		public LoadImage(ImageView itemImage, String url,int resourceId) {
			this.itemImage = itemImage;
			this.resourceId = resourceId;
			this.url = url;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.rotation);
			itemImage.startAnimation(animation);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				if (url != null || !url.trim().equals("")) {
					HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
					connection.connect();
					InputStream input = connection.getInputStream();
					if (input != null) {
						bitMap = BitmapFactory.decodeStream(input);
					} else {
						bitMap = null;
					}
				} else {
					bitMap = null;
				}
			} catch (Exception e) {
				bitMap = null;
				return null;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (bitMap != null) {
				itemImage.setImageBitmap(bitMap);
				confRoomImageMap.put(resourceId, bitMap);
			}
			itemImage.clearAnimation();
			super.onPostExecute(result);
		}
	}
	
	private void fillResFilter(){
		if(selectedSmSpace.getResourceList() != null){
			List<Map<String, Object>> resTypeList = new ArrayList<Map<String,Object>>();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("All", "All");
			resTypeList.add(map);
			for (ResourceModel resModel : selectedSmSpace.getResourceList()) {
				if(!map.containsKey(resModel.getResourceType().replace("Usage", ""))){
					map = new HashMap<String, Object>();
					map.put(resModel.getResourceType().replace("Usage", ""), resModel);
					resTypeList.add(map);
				}
				
			}
			if (resTypeList != null && resTypeList.size() > 0) {
				int selectedTimePosition = 0;
				CustomAdapter resAdapter = new CustomAdapter(mContext, resTypeList);
				restypeSpinner.setAdapter(resAdapter);
				restypeSpinner.setSelection(selectedTimePosition);
				restypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> arg0,View view, int position, long id) {
						if (view.getTag() instanceof ResourceModel) {
							ResourceModel res= (ResourceModel ) view.getTag();
							roomType = res.getResourceType();
							
						} else {
							roomType = null;
						}
						loadConfRoom();
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// selectedCustDuration=0;
					}

				});
			}
		}
		
	}
}
