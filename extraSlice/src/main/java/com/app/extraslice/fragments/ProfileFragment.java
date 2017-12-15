package com.app.extraslice.fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.extraslice.R;
import com.app.extraslice.adapter.CustomAdapter;
import com.app.extraslice.bo.CustAcctBO;
import com.app.extraslice.model.Organization;
import com.app.extraslice.model.PlanModel;
import com.app.extraslice.model.ResourceTypeModel;
import com.app.extraslice.model.UserOrgModel;
import com.app.extraslice.utils.CustomException;
import com.app.extraslice.utils.ProgressClass;
import com.app.extraslice.utils.Utilities;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileFragment extends Fragment {
	
	
	private final String GET_SUBCRIPTION_DETL="GET_SUBCRIPTION_DETL";
	private final String CANCEL_SUBSCRIPTION="CANCEL_SUBSCRIPTION";
	public static int count = 0;
	Context context;
	public static View rootView;
	Fragment fragment = null;
	TextView unsubscribeTxt;
	Dialog dialog;
	FragmentManager fragmentManager;
	TextView orgName,authTextView;
	TextView unsubscribeBtn;
	Spinner orgSpinner;
	Organization selectedOrg;;
	List<String> planIdList = new ArrayList<String>();
	List<String> addonIdList = new ArrayList<String>();
	List<UserOrgModel> userList = null;
	CheckBox selectMeeting;
	TextView errorText;
	ViewGroup container;
	ImageView expandSub,userExp,plnResExp;
	int seleTab = 0;
	boolean subSelected = true;
	boolean plnBenSelected = false;
	boolean userSelected = false;
	LinearLayout errorLyt,unsubscribeLyt,detlLyt,expandSubLyt,expandUserLyt,userLyt,plnResLyt,expandplnResLyt,plnbenHeader,meetingLyt;
	public ProfileFragment() {
	}
	//    unsubscribeLyt
	//confRoomScrView""plnResLyt"""
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		context 						= this.getActivity();
		rootView 						= inflater.inflate(R.layout.profile_screen, container, false);
		unsubscribeBtn 					= (TextView)rootView.findViewById(R.id.unsubscribeBtn);
		unsubscribeLyt 					= (LinearLayout)rootView.findViewById(R.id.unsubscribeLyt);
		authTextView 					= (TextView)rootView.findViewById(R.id.authTextView);
		orgName 						= (TextView)rootView.findViewById(R.id.orgName);
		orgSpinner 						= (Spinner)rootView.findViewById(R.id.orgSpinner);
		errorText 						= (TextView)rootView.findViewById(R.id.errorText);
		errorLyt  						= (LinearLayout)rootView.findViewById(R.id.errorLyt);
		detlLyt  						= (LinearLayout)rootView.findViewById(R.id.detlLyt);
		selectMeeting					= (CheckBox)rootView.findViewById(R.id.selectMeeting);
		unsubscribeTxt     				= (TextView)rootView.findViewById(R.id.unsubscribeTxt);
		expandSubLyt					= (LinearLayout)rootView.findViewById(R.id.expandSubLyt);
		expandSub						= (ImageView)rootView.findViewById(R.id.expandSub);
		expandUserLyt					= (LinearLayout)rootView.findViewById(R.id.expandUserLyt);
		expandplnResLyt					= (LinearLayout)rootView.findViewById(R.id.expandplnResLyt);
		userExp							= (ImageView)rootView.findViewById(R.id.userExp);
		plnResExp						= (ImageView)rootView.findViewById(R.id.plnResExp);
		//;"""plnResHeader" +
		plnResLyt						= (LinearLayout)rootView.findViewById(R.id.plnResLyt);
		meetingLyt						= (LinearLayout)rootView.findViewById(R.id.meetingLyt);
		userLyt							= (LinearLayout)rootView.findViewById(R.id.userLyt);
		String cancelSubTxt = getResources().getString(R.string.cancelSubscription);
		unsubscribeBtn.setText(Html.fromHtml(cancelSubTxt));
		plnResLyt.setVisibility(View.GONE);
		userLyt.setVisibility(View.GONE);
		expandSubLyt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				seleTab = 0;
				expanDCollapseViews();
				
			}
		});
		expandplnResLyt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				seleTab = 1;
				expanDCollapseViews();
				
			}
		});
		expandUserLyt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				seleTab = 2;
				expanDCollapseViews();
				
			}
		});
		
		this.container					= container;
		List<Organization> adminOrgList = new ArrayList<Organization>();
		
		if(Utilities.loggedInUser.getOrgList() != null && Utilities.loggedInUser.getOrgList().size() > 0){
			for(Organization org : Utilities.loggedInUser.getOrgList()){
				if(org.getOrgRoleId() == 1){
					adminOrgList.add(org);
				}
			}
		}
		if(adminOrgList == null || adminOrgList.size() ==0){
			RelativeLayout userHeader =  (RelativeLayout)rootView.findViewById(R.id.userHeader);
			RelativeLayout plnResHeader =  (RelativeLayout)rootView.findViewById(R.id.plnResHeader);
			RelativeLayout header =  (RelativeLayout)rootView.findViewById(R.id.header);
			
			userHeader.setVisibility(View.GONE);
			plnResHeader.setVisibility(View.GONE);
			header.setVisibility(View.GONE);
			unsubscribeLyt.setVisibility(View.GONE);
			authTextView.setVisibility(View.VISIBLE);
			authTextView.setText("You are not autherized to do this action");
		}else if(adminOrgList.size() > 1){
			unsubscribeLyt.setVisibility(View.VISIBLE);
			authTextView.setVisibility(View.GONE);
			orgName.setVisibility(View.GONE);
			orgSpinner.setVisibility(View.VISIBLE);
			List<Map<String, Object>> objMapLst = new ArrayList<Map<String,Object>>();
			Map<String, Object> objMap = new HashMap<String, Object>();
			objMapLst.add(objMap);
			objMap.put("Select Organization", "Select Organization");
			for(Organization org : adminOrgList){
				objMap = new HashMap<String, Object>();
				objMap.put(org.getOrgName(), org);
			}
			CustomAdapter adp = new CustomAdapter(context,objMapLst); 
			orgSpinner.setAdapter(adp);
			orgSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					if(arg1.getTag() instanceof Organization){
						selectedOrg = (Organization)arg1.getTag();
					}
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			new RunInBackground(GET_SUBCRIPTION_DETL,Utilities.loggedInUser.getUserId(), selectedOrg.getOrgId(),false,null,null).execute();
		}else{
			unsubscribeLyt.setVisibility(View.VISIBLE);
			authTextView.setVisibility(View.GONE);
			orgName.setVisibility(View.VISIBLE);
			orgSpinner.setVisibility(View.GONE);
			selectedOrg = adminOrgList.get(0);
			orgName.setText(selectedOrg.getOrgName().equalsIgnoreCase("Individual")?Utilities.loggedInUser.getUserName():selectedOrg.getOrgName());
			new RunInBackground(GET_SUBCRIPTION_DETL,Utilities.loggedInUser.getUserId(), selectedOrg.getOrgId(),false,null,null).execute();
		}

		unsubscribeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				errorText.setText("");
				errorLyt.setVisibility(View.GONE);
				if((planIdList == null || planIdList.size() ==0) && (addonIdList == null || addonIdList.size() ==0)){
					errorText.setText("Please select an item to unsubscribe");
					errorLyt.setVisibility(View.VISIBLE);
				}else{
					new RunInBackground(CANCEL_SUBSCRIPTION,Utilities.loggedInUser.getUserId(), selectedOrg.getOrgId(),selectMeeting.isChecked(),planIdList,addonIdList).execute();
				}
				
			}
		});
			
		
		rootView.setFocusableInTouchMode(true);
		rootView.requestFocus();
		rootView.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (keyCode == KeyEvent.KEYCODE_BACK) {

					/*Fragment fragment = null;
					fragment = new HomeFragment();
					if (fragment != null) {
						FragmentManager fragmentManager = getFragmentManager();
						//fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
						Utilities.loadFragment(context,fragmentManager,fragment,R.id.frame_container,true);
					}*/
					return true;
				} else {
					return true;
				}
			}
		});
		
		return rootView;
	}
	private void expanDCollapseViews(){
		switch (seleTab) {
		case 0:
			if(subSelected){
				subSelected =false;
				unsubscribeLyt.setVisibility(View.GONE);
				expandSub.setImageResource(R.drawable.arrow_down);
			}else{
				subSelected =true;
				plnBenSelected =false;
				userSelected =false;
				unsubscribeLyt.setVisibility(View.VISIBLE);
				expandSub.setImageResource(R.drawable.arrow_up);
			}

			plnResLyt.setVisibility(View.GONE);
			userLyt.setVisibility(View.GONE);
			plnResExp.setImageResource(R.drawable.arrow_down);
			userExp.setImageResource(R.drawable.arrow_down);
			
			break;
		case 1:
			if(plnBenSelected){
				plnBenSelected =false;
				plnResLyt.setVisibility(View.GONE);
				plnResExp.setImageResource(R.drawable.arrow_down);
			}else{
				plnBenSelected =true;
				subSelected =false;
				userSelected =false;
				plnResLyt.setVisibility(View.VISIBLE);
				plnResExp.setImageResource(R.drawable.arrow_up);
			}
			unsubscribeLyt.setVisibility(View.GONE);
			userLyt.setVisibility(View.GONE);
			expandSub.setImageResource(R.drawable.arrow_down);
			userExp.setImageResource(R.drawable.arrow_down);
			break;
		case 2:
			if(userSelected){
				userSelected =false;
				userLyt.setVisibility(View.GONE);
				userExp.setImageResource(R.drawable.arrow_down);
			}else{
				userSelected =true;
				plnBenSelected =false;
				subSelected =false;
				userLyt.setVisibility(View.VISIBLE);
				userExp.setImageResource(R.drawable.arrow_up);
			}
			
			unsubscribeLyt.setVisibility(View.GONE);
			plnResLyt.setVisibility(View.GONE);
			expandSub.setImageResource(R.drawable.arrow_down);
			plnResExp.setImageResource(R.drawable.arrow_down);
			break;
		default:
			break;
		}
		
	}
	

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		//inilize();
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

	 class RunInBackground extends AsyncTask<Void, Void, Void> {
			String errMsg = null;
			int userId;
			int orgId;
			String status;
			boolean cancelMeetingsToo;
			String purpose;
			JSONObject resultJson;
			List<String> planId;
			List<String> addonIds;
			public RunInBackground( String purpose,int userId,int orgId,boolean cancelMeetingsToo,List<String> planId,List<String> addonIds) {
				this.purpose           = purpose;
				this.userId            = userId;
				this.orgId             = orgId;
				this.cancelMeetingsToo = cancelMeetingsToo;
				this.planId = planId;
				this.addonIds = addonIds;
			}

			@Override
			protected void onPreExecute() {
				errorText.setText("");
				errorLyt.setVisibility(View.GONE);
				ProgressClass.startProgress(context);
				super.onPreExecute();
				

			}

			@Override
			protected Void doInBackground(Void... arg0) {
				CustAcctBO custBO = new CustAcctBO();
				try {
					if(purpose.equals(CANCEL_SUBSCRIPTION)){
						List<Integer> plnIdList = new ArrayList<Integer>();
						if(planId != null && planId.size() > 0){
							for(String plnId :planId){
								plnIdList.add(Integer.parseInt(plnId));
							}
						}
						List<Integer> addonIdList = new ArrayList<Integer>();
						if(addonIds != null && addonIds.size() > 0){
							for(String addId :addonIds){
								addonIdList.add(Integer.parseInt(addId));
							}
						}
						status = custBO.requestCancelSubscription(context, userId, orgId, cancelMeetingsToo,plnIdList,addonIdList);
					}else if(purpose.equals(GET_SUBCRIPTION_DETL)){
						resultJson = custBO.getProfileData(context, userId, orgId);
					}
				} catch (CustomException e) {
					errMsg = e.getLocalizedMessage();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				if (errMsg != null) {
					errorText.setText(errMsg);
					errorLyt.setVisibility(View.VISIBLE);
				} else {
					if(purpose.equals(CANCEL_SUBSCRIPTION)){
						if(status != null && status.equalsIgnoreCase(Utilities.STATUS_SUCCESS)){
							Toast tst = Toast.makeText(context, "Your request submitted successfully. Our community manager will contact you soon.", Toast.LENGTH_LONG);
							tst.setGravity(Gravity.CENTER, 0, 0);
							tst.show();
						}else{
							
							errorText.setText(status);
							errorLyt.setVisibility(View.VISIBLE);
						}
					}else if(purpose.equals(GET_SUBCRIPTION_DETL)){
						//
						if (resultJson == null ) {
							errorText.setText("Error while getting subscription details");
							errorLyt.setVisibility(View.VISIBLE);
							unsubscribeLyt.setVisibility(View.GONE);
						} else{
							try{
							if ( resultJson.getString(Utilities.STATUS_STRING).equals(Utilities.STATUS_SUCCESS)) {
								JSONArray planArray = (JSONArray)resultJson.get("PlanList");
								JSONArray userArray = (JSONArray)resultJson.get("USERLIST");
								JSONArray plnBnfts = (JSONArray)resultJson.get("ResourceTypeList");
								int maxUserCount = resultJson.getInt("USERCOUNT");
							
								boolean canUnsubscribe = resultJson.getBoolean("canUnsubscribe");
								if(!canUnsubscribe){
									unsubscribeTxt.setVisibility(View.VISIBLE);
									unsubscribeBtn.setVisibility(View.GONE);
									String unsubscribeMsg = resultJson.getString("UNSUBMESSAGE");
									unsubscribeTxt.setText(unsubscribeMsg);
								}
								if (planArray != null) {
									for (int index = 0; index < planArray.length(); index++) {
										JSONObject obj = (JSONObject) planArray.get(index);
										PlanModel plan = new PlanModel().jSonToObject(obj.toString());
										LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
										View view = inflater.inflate(R.layout.subscription_detl_layout,null );
										LinearLayout aiv = (LinearLayout)view.findViewById(R.id.loginLyt);
										aiv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
										CheckBox select =  (CheckBox) view.findViewById(R.id.select);
										TextView name =  (TextView) view.findViewById(R.id.name);
										TextView type =  (TextView) view.findViewById(R.id.type);
										type.setText("Plan");
										name.setText(plan.getPlanName()+" ($"+plan.getPlanPrice()+")");
										select.setTag("Plan-"+plan.getPlanId());
										detlLyt.addView(view);
										select.setOnClickListener(new OnClickListener() {
											
											@Override
											public void onClick(View arg0) {
												errorText.setText("");
												errorLyt.setVisibility(View.GONE);
												CheckBox cb =(CheckBox)arg0;
												String tag = arg0.getTag().toString();
												int id = Integer.parseInt(tag.split("-")[1]);
												if(cb.isChecked()){
													planIdList.add(id+"");
													meetingLyt.setVisibility(View.VISIBLE);
													unsubscribeBtn.setVisibility(View.VISIBLE);
												}else{
													planIdList.remove(id+"");
													meetingLyt.setVisibility(View.GONE);
													unsubscribeBtn.setVisibility(View.GONE);
												}
												
											}
										});
									}
								}
								if (plnBnfts != null) {
									
									for (int index = 0; index < plnBnfts.length(); index++) {
										JSONObject obj = (JSONObject) plnBnfts.get(index);
										ResourceTypeModel resModel = new ResourceTypeModel().jSonToObject(obj.toString());
										LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
										View view = inflater.inflate(R.layout.plan_benefits_item,null );

										TextView resName =  (TextView) view.findViewById(R.id.resName);
										TextView limit =  (TextView) view.findViewById(R.id.limit);
										TextView balance =  (TextView) view.findViewById(R.id.balance);
										resName.setText(resModel.getResourceTypeName());
										limit.setText(resModel.getPlanLimit()== -1?"Unlimited":resModel.getPlanLimit()+" "+resModel.getPlanLimitUnit());
										double bal = resModel.getPlanLimit()-resModel.getCurrentUsage();
										bal = bal <0?0:bal;
										balance.setText(resModel.getCurrentUsage()<0?"Unlimited":bal+" "+resModel.getPlanLimitUnit());
										plnResLyt.addView(view);
									}
								}
								if (userArray != null) {
				
									for (int index = 0; index < userArray.length(); index++) {
										JSONObject obj = (JSONObject) userArray.get(index);
										UserOrgModel user = new UserOrgModel().jSonToObject(obj.toString());
										LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
										View view = inflater.inflate(R.layout.user_details_item,null );
										LinearLayout selectLyt = (LinearLayout)view.findViewById(R.id.selectLyt);
										
										//aiv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
										CheckBox select =  (CheckBox) view.findViewById(R.id.select);
										TextView userName =  (TextView) view.findViewById(R.id.userName);
										TextView userRole =  (TextView) view.findViewById(R.id.userRole);
										TextView status =  (TextView) view.findViewById(R.id.status);
										userName.setText(user.getUserName());
										userRole.setText(user.getOrgRoleId()==1?"Admin":"Member");
										status.setText(user.getUserStatus());
										userLyt.addView(view);
										/*select.setTag("Plan-"+plan.getPlanId());
										detlLyt.addView(view);
										select.setOnClickListener(new OnClickListener() {
											
											@Override
											public void onClick(View arg0) {
												errorText.setText("");
												errorLyt.setVisibility(View.GONE);
												CheckBox cb =(CheckBox)arg0;
												String tag = arg0.getTag().toString();
												int id = Integer.parseInt(tag.split("-")[1]);
												if(cb.isChecked()){
													planIdList.add(id+"");
												}else{
													planIdList.remove(id+"");
												}
												
											}
										});*/
									}
								}
								try{
									JSONArray addonArray = (JSONArray)resultJson.get("AddonArray");
									if (addonArray != null) {
										for (int index = 0; index < addonArray.length(); index++) {
											JSONObject obj = (JSONObject) addonArray.get(index);
											ResourceTypeModel resType = new ResourceTypeModel().jSonToObject(obj.toString());
											
											LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
											View view = inflater.inflate(R.layout.subscription_detl_layout,null );
											LinearLayout aiv = (LinearLayout)view.findViewById(R.id.loginLyt);
											aiv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
											CheckBox select =  (CheckBox) view.findViewById(R.id.select);
											TextView name =  (TextView) view.findViewById( R.id.name );
											TextView type =  (TextView) view.findViewById( R.id.type );
											type.setText("Addon");
											name.setText(resType.getResourceTypeName());
											select.setTag("Addon-"+resType.getResourceTypeId());
											detlLyt.addView(view);
											select.setOnClickListener(new OnClickListener() {
												
												@Override
												public void onClick(View arg0) {
													errorText.setText("");
													errorLyt.setVisibility(View.GONE);
													CheckBox cb =(CheckBox)arg0;
													String tag = arg0.getTag().toString();
													int id = Integer.parseInt(tag.split("-")[1]);
													if(cb.isChecked()){
														addonIdList.add(id+"");
													}else{
														addonIdList.remove(id+"");
													}
													
												}
											});
											
										}
									}
								}catch(Exception eee){
									
								}
							}else{
								
								errorText.setText(resultJson.getString(Utilities.ERROR_MESSAGE));
								errorLyt.setVisibility(View.VISIBLE);
								unsubscribeLyt.setVisibility(View.GONE);
							}
							}catch(Exception ee){
								errorText.setText(ee.getMessage());
								errorLyt.setVisibility(View.VISIBLE);
								unsubscribeLyt.setVisibility(View.GONE);
							}
						}
					}
				}
				ProgressClass.finishProgress();
				super.onPostExecute(result);
			}
		}




	
}
