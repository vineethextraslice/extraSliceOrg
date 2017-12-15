package com.app.extraslice;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.extraslice.adapter.CustomAdapter;
import com.app.extraslice.bo.SmartspaceBO;
import com.app.extraslice.model.PlanModel;
import com.app.extraslice.model.PlanOfferModel;
import com.app.extraslice.model.ResourceTypeModel;
import com.app.extraslice.model.UserModel;
import com.app.extraslice.utils.CustomException;
import com.app.extraslice.utils.ProgressClass;
import com.app.extraslice.utils.Utilities;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ShowPlanFragment extends Fragment {
	CustomAdapter planAdapter ;
	Context mContext;
	LinearLayout errorLyt,planLyt,registeredUserLyt,popupErrorLyt;
	TextView errorText,popupErrorText;
	private static final String LOAD_SIGNUP_DATA="LOAD_SIGNUP_DATA"; 
	private static final String LOAD_EXISTING_USER="LOAD_EXISTING_USER";
	View rootView ;
	Activity act;
	CheckBox registeredUser;
	int noOfdaystoSubsDate;
	long trialEndsAt ;
	long firstsubDate ;
	double noOFDaysInMoth;
	String message;
	int noOfdaystoNextMonth;
	Dialog dialog;
	public ShowPlanFragment(){
		
	}

	public ShowPlanFragment(int noOfdaystoSubsDate,long trialEndsAt,long firstsubDate,double noOFDaysInMoth,String message,int noOfdaystoNextMonth){
		
		this.noOfdaystoSubsDate=noOfdaystoSubsDate;
		this.trialEndsAt = trialEndsAt;
		this.firstsubDate = firstsubDate;
		this.noOFDaysInMoth = noOFDaysInMoth;
		this.message = message;
		this.noOfdaystoNextMonth = noOfdaystoNextMonth;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rootView = inflater.inflate(R.layout.signup_show_plans, container,false);
		mContext = getActivity();
		act = getActivity();
		errorText = (TextView) rootView.findViewById(R.id.errorText);
		errorLyt = (LinearLayout) rootView.findViewById(R.id.errorLyt);
		planLyt = (LinearLayout) rootView.findViewById(R.id.planLyt);
		registeredUserLyt = (LinearLayout) rootView.findViewById(R.id.registeredUserLyt);
		ImageView backImage = (ImageView) rootView.findViewById(R.id.back);
		backImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(Utilities.loggedInUser ==null){
					Intent intent = new Intent(act, LoginActivity.class);
					startActivity(intent);
				}else{
					Intent intent = new Intent(act, MenuActivity.class);
					startActivity(intent);
				}
				
				act.finish();
			}
		});
		registeredUser = (CheckBox) rootView.findViewById(R.id.registeredUser);
		if (SmartspaceBO.planList == null || SmartspaceBO.planList.size()==0) {
			new RunInBackground(LOAD_SIGNUP_DATA,null).execute();
		} else {
			loadPlans();
		}

		registeredUser.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				if(isChecked){
					dialog = new Dialog(mContext);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.existing_user_email_popup);
					final EditText extEmail  =(EditText)dialog.findViewById(R.id.extEmail);
					popupErrorLyt  =(LinearLayout)dialog.findViewById(R.id.errorLyt);
					popupErrorText  =(TextView)dialog.findViewById(R.id.errorText);
					
					Button submit  =(Button)dialog.findViewById(R.id.submit);
					Button cancel  =(Button)dialog.findViewById(R.id.cancel);
					cancel.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							dialog.dismiss();
							
						}
					});
					submit.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							String email = extEmail.getText().toString();
							if(email.trim().isEmpty()){
								popupErrorText.setText("Please enter email");
								popupErrorLyt.setVisibility(View.VISIBLE);
					    	}else if(!email.toString().matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")){
					    		popupErrorText.setText("Invalid email");
					    		popupErrorLyt.setVisibility(View.VISIBLE);
					    	}else{
					    		popupErrorLyt.setVisibility(View.GONE);
					    		new RunInBackground(LOAD_EXISTING_USER,email).execute();
					    	}
							
							
						}
					});
					dialog.show();
				}
			}
		});

		rootView.setFocusableInTouchMode(true);
		rootView.requestFocus();
		rootView.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				return true;
			}
		});
		return rootView;
	}
	
    
    class RunInBackground extends AsyncTask<Void, Void, Void> {
		String errMsg = null;
		SmartspaceBO smartSpaceBo;
		String purpose;
		String email;
		JSONObject resultJson;
		public RunInBackground(String purpose,String email){
			this.purpose = purpose;
			this.email=email;
			
		}
		@Override
		protected void onPreExecute() {
			/*errorText.setText("");
			errorLyt.setVisibility(View.GONE);*/
			ProgressClass.startProgress(mContext);
			smartSpaceBo = new SmartspaceBO(mContext);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			if(this.purpose.equalsIgnoreCase(LOAD_SIGNUP_DATA)){
				try {
					smartSpaceBo.getSignupData();
				} catch (CustomException e) {
					errMsg = e.getLocalizedMessage();
				}
			}else if(this.purpose.equalsIgnoreCase(LOAD_EXISTING_USER)){
				try {
					resultJson = smartSpaceBo.getExistingUser(email);
				} catch (CustomException e) {
					errMsg = e.getLocalizedMessage();
				}
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if(this.purpose.equalsIgnoreCase(LOAD_SIGNUP_DATA)){
				if (errMsg != null) {
					errorText.setText(errMsg);
					errorLyt.setVisibility(View.VISIBLE);
				} else {
					
					noOfdaystoSubsDate = smartSpaceBo.noOfdaystoSubsDate;
					trialEndsAt = smartSpaceBo.trialEndsAt;
					firstsubDate = smartSpaceBo.firstsubDate;
					noOFDaysInMoth = smartSpaceBo.noOFDaysInMoth;
					message=smartSpaceBo.message;
					noOfdaystoNextMonth = smartSpaceBo.noOfdaystoNextMonth;
					loadPlans();
				}
				if(SmartspaceBO.accountModel == null){
		    		registeredUserLyt.setVisibility(View.GONE);
		    		planLyt.setVisibility(View.GONE);
		    	}
			}else if(this.purpose.equalsIgnoreCase(LOAD_EXISTING_USER)){
				if (errMsg != null) {
					popupErrorText.setText(errMsg);
					popupErrorLyt.setVisibility(View.VISIBLE);
				} else {
					Log.e("resultJson : ",resultJson.toString());
					try{
						JSONArray planArray = (JSONArray)resultJson.get("PlanList");
						JSONArray addonArray = null;
						JSONObject OfferObj = null;
						try{
							OfferObj =(JSONObject)resultJson.get("Offer");
						}catch(Exception e){
							Log.e("error : ",e.getMessage());
							
						}
						try{
							addonArray = (JSONArray)resultJson.get("AddonList");
						}catch(Exception e){
							Log.e("error : ",e.getMessage());
							
						}
						List<PlanModel> selePlanList = new ArrayList<PlanModel>();
						List<ResourceTypeModel> selAddonList = new ArrayList<ResourceTypeModel>();
						PlanOfferModel selOfferModel = null;
						if(OfferObj != null){
							selOfferModel = new PlanOfferModel().jSonToObject(OfferObj.toString());
						}
						if (planArray != null) {
							for (int index = 0; index < planArray.length(); index++) {
								JSONObject obj = (JSONObject) planArray.get(index);
								PlanModel plan = new PlanModel().jSonToObject(obj.toString());
								selePlanList.add(plan);
							}
						}
						if (addonArray != null) {
							for (int index = 0; index < addonArray.length(); index++) {
								JSONObject obj = (JSONObject) addonArray.get(index);
								ResourceTypeModel resType = new ResourceTypeModel().jSonToObject(obj.toString());
								selAddonList.add(resType);
							}
						}
						int noOfdaystoSubsDate=resultJson.getInt("noOfdaystoSubsDate");
						long trialEndsAt = resultJson.getLong("trialEndsAt");
						long firstsubDate = resultJson.getLong("firstsubDate");
						double noOFDaysInMoth = resultJson.getInt("noOFDaysInMoth");
						String message=resultJson.getString("message");
						String paymentType=resultJson.getString("paymentType");
						int noOfdaystoNextMonth =  resultJson.getInt("noOfdaystoNextMonth");
						double planCostVal = 0;
						if(paymentType == null || !paymentType.equalsIgnoreCase("offline")){
							for(PlanModel pln : selePlanList){
								planCostVal = (planCostVal+pln.getPlanPrice()*(pln.getNoOfPlans()<=0 ? 1 : pln.getNoOfPlans()));
							}
							
							if(selAddonList != null){
								for(ResourceTypeModel resModel : selAddonList){
									planCostVal = planCostVal + (resModel.getPlanSplPrice()*(resModel.getNoOfAddOns()<=0 ? 1 : resModel.getNoOfAddOns()));
								}
							}
							if(selOfferModel != null){
								if(selOfferModel.getOfferType().equalsIgnoreCase("percent")){
									planCostVal = planCostVal - (planCostVal*selOfferModel.getOfferValue()/100.00);
								}else if(selOfferModel.getOfferType().equalsIgnoreCase("amount")){
									planCostVal = planCostVal - selOfferModel.getOfferValue();
								}
								
							}
						}
						if(dialog != null && dialog.isShowing()){
							dialog.dismiss();
						}
						UserModel userModel = new UserModel().jSonToObject(resultJson.get("User").toString());
						Fragment newFrgment = new UserDetailsFragment(userModel,true,paymentType,selePlanList,	selAddonList,selOfferModel,planCostVal,noOfdaystoSubsDate,trialEndsAt,
								firstsubDate,noOFDaysInMoth,message,noOfdaystoNextMonth);
						Utilities.loadFragment(getFragmentManager(),newFrgment, R.id.frame_container, false);
					}catch(Exception e){
						Log.e("error : ",e.getMessage());
						
					}
				}
				
			}
			ProgressClass.finishProgress();
			super.onPostExecute(result);
		}
	}
	

    /**
     * 
     */
    private void loadPlans(){
    	if(SmartspaceBO.accountModel == null){
    		registeredUserLyt.setVisibility(View.GONE);
    		planLyt.setVisibility(View.GONE);
    	}
    	if (SmartspaceBO.planList != null && SmartspaceBO.planList.size() > 0) {
    	
			int index = 0;;
			for (PlanModel model : SmartspaceBO.planList) {
				LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View convertView = inflater.inflate( R.layout.view_plans, null);
				if(index%2 == 0){
					convertView.setBackgroundResource(R.color.theme_light_blue);
				}else{
					convertView.setBackgroundResource(R.color.darker_gray);
				}
				TextView planPrice = (TextView)convertView.findViewById(R.id.planPrice);
				TextView planName  = (TextView)convertView.findViewById(R.id.planName);
				ImageView showPlan= (ImageView)convertView.findViewById(R.id.showPlan);
			   
				planName.setText(model.getPlanName());
				
				double minAmount = model.getPlanPrice();
				for(PlanOfferModel offerModel : SmartspaceBO.offerList){
		    		String[] plnIdArray = offerModel.getApplicableTo().split(",");
		    		List<Integer> planIdList = new ArrayList<Integer>();
		    		for(String str : plnIdArray){
		    			planIdList.add(Integer.parseInt(str));
		    		}
		    		if(planIdList.contains(model.getPlanId())){
		    			double currAmount = model.getPlanPrice()-(model.getPlanPrice()*offerModel.getOfferValue()/100.00);
		    			if(currAmount < minAmount){
		    				minAmount = currAmount;
		    			}
		    		}
		    	}
				planPrice.setText("Starts from $"+minAmount);


				convertView.setTag(model);
				convertView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						PlanModel model = (PlanModel)v.getTag();
						Fragment newFrgment = new PlanResourceDetailsFragment( model,noOfdaystoSubsDate,trialEndsAt,firstsubDate,noOFDaysInMoth,message,
								noOfdaystoNextMonth);
						Utilities.loadFragment(getFragmentManager(),newFrgment, R.id.frame_container, false);
					}
				});
			    
				
				index++;
				planLyt.addView(convertView);
			}
    	}

    }

}
