package com.app.extraslice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.extraslice.bo.SmartspaceBO;
import com.app.extraslice.model.PlanModel;
import com.app.extraslice.model.PlanOfferModel;
import com.app.extraslice.model.ResourceTypeModel;
import com.app.extraslice.utils.MyEditText;
import com.app.extraslice.utils.Utilities;


public class PlanResourceDetailsFragment extends Fragment {

	
	Context mContext;
	View rootView;
	PlanModel selectedPlan;
	PlanOfferModel selectedOfferModel;
	List<ResourceTypeModel> selectedAddOnsList =new ArrayList<ResourceTypeModel>(1);
	TextView planCost;
	CheckBox currentCB = null;
	double planCostVal = 0;
	int noOfdaystoSubsDate;
	long trialEndsAt ;
	long firstsubDate ;
	double noOFDaysInMoth;
	String message;
	int noOfdaystoNextMonth;
	 List<Integer> selPlanResIds = new ArrayList<Integer>(1);
	 LinearLayout addonLyt;
	public PlanResourceDetailsFragment(){
		
	}
	public PlanResourceDetailsFragment(PlanModel selectedPlan,int noOfdaystoSubsDate,long trialEndsAt,long firstsubDate,
			double noOFDaysInMoth,String message,int noOfdaystoNextMonth){
		selectedPlan.setNoOfPlans(1);
		this.selectedPlan=selectedPlan;
		this.noOfdaystoSubsDate=noOfdaystoSubsDate;
		this.trialEndsAt = trialEndsAt;
		this.firstsubDate = firstsubDate;
		this.noOFDaysInMoth = noOFDaysInMoth;
		this.message = message;
		this.noOfdaystoNextMonth = noOfdaystoNextMonth;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rootView = inflater.inflate(R.layout.signup_show_plans_detl,container, false);
		rootView.setEnabled(true);
		rootView.setClickable(true);
		rootView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				hideVirtualKeyBoard();
				
			}
		});
        mContext = getActivity();
        TextView planName=(TextView) rootView.findViewById(R.id.planName);
        TextView planShortDesc=(TextView) rootView.findViewById(R.id.planShortDesc);
        planCost=(TextView) rootView.findViewById(R.id.planCost);
        Button joinNow=(Button) rootView.findViewById(R.id.joinNow);
        ImageView back=(ImageView) rootView.findViewById(R.id.back);
        LinearLayout offerTopLyt=(LinearLayout) rootView.findViewById(R.id.offerTopLyt);
        LinearLayout offerLyt=(LinearLayout) rootView.findViewById(R.id.offerLyt);
        if(SmartspaceBO.offerList == null && SmartspaceBO.offerList.size()==0){
        	offerTopLyt.setVisibility(View.GONE);
        }else{
        	loadOffers(offerLyt);
        }
    
        LinearLayout addonTopLyt=(LinearLayout) rootView.findViewById(R.id.addonTopLyt);
        addonLyt=(LinearLayout) rootView.findViewById(R.id.addonLyt);
        
        
        LinearLayout resourceTopLyt=(LinearLayout) rootView.findViewById(R.id.resourceTopLyt);
        LinearLayout resourceLyt=(LinearLayout) rootView.findViewById(R.id.resourceLyt);
        if(selectedPlan == null ){
        	resourceTopLyt.setVisibility(View.GONE);
        }else{
        	 planName.setText(selectedPlan.getPlanName());
             planShortDesc.setText(selectedPlan.getPlanName()+"($"+selectedPlan.getPlanPrice()+")");
             planCost.setText("Plan cost : $"+selectedPlan.getPlanPrice()+"");
             loadResourcesForPlan(resourceLyt);
             planCostVal=selectedPlan.getPlanPrice();
            if(!selectedPlan.isPurchaseOnSpot()){
            	joinNow.setText("Check availability");
            }
             for(ResourceTypeModel resModel : selectedPlan.getResourceTypeList()){
            	 if(!resModel.getAllowUsageBy().equalsIgnoreCase("ondemand")){
            		 selPlanResIds.add(resModel.getResourceTypeId());
 	    		}
             }
             int addOnCount = 0;
             for(ResourceTypeModel resModel : SmartspaceBO.addonList){
            	 if(!selPlanResIds.contains(resModel.getResourceTypeId())){
            		 addOnCount++;
 	    		}
             }
             if(addOnCount<=0){
             	addonTopLyt.setVisibility(View.GONE);
             }else{
             	loadAddons(addonLyt);
             }
        }
        
        joinNow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				List<PlanModel> selectedPlans = new ArrayList<PlanModel>();
				selectedPlans.add(selectedPlan);
				Fragment newFrgment = new UserDetailsFragment(null,false,"online",selectedPlans, selectedAddOnsList,selectedOfferModel,planCostVal,noOfdaystoSubsDate,trialEndsAt,firstsubDate,noOFDaysInMoth,message,
						noOfdaystoNextMonth);
				Utilities.loadFragment(getFragmentManager(),newFrgment, R.id.frame_container, false);
				
			}
		});
        back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Fragment fragment = new ShowPlanFragment(noOfdaystoSubsDate,trialEndsAt,firstsubDate,noOFDaysInMoth,message,
						noOfdaystoNextMonth);
				Utilities.loadFragment(getFragmentManager(), fragment, R.id.frame_container, true);
			}
		});
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

		
		int screenHeight = mContext.getResources().getDisplayMetrics().heightPixels;
		float scale = mContext.getResources().getDisplayMetrics().density;
		
		//LayoutParams param = (LayoutParams)addonTopLyt.getLayoutParams();
		addonTopLyt.getLayoutParams().height=(int)((screenHeight-((int)(310*scale)))/2);
		//addonTopLyt.setLayoutParams(param);
		resourceTopLyt.getLayoutParams().height=(int)((screenHeight-((int)(310*scale)))/2);
		//resourceTopLyt.setLayoutParams(param2);
		rootView.setFocusableInTouchMode(true);
		rootView.requestFocus();
		rootView.setOnKeyListener(new View.OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				/*if (keyCode == KeyEvent.KEYCODE_BACK) {
					Fragment fragment = null;
					fragment = new ShowPlanFragment();
					if (fragment != null) {
						FragmentManager fragmentManager = getFragmentManager();
						Utilities.loadFragment(fragmentManager,fragment,R.id.frame_container,true);
					}
					return true;
				} else {
					return false;
				}*/
				return true;
			}
		});
		
       return rootView;
    }
	
	 private void loadOffers(LinearLayout offerLyt){
		 offerLyt.removeAllViews();
    	if(SmartspaceBO.offerList != null){
	    	for(PlanOfferModel offerModel:SmartspaceBO.offerList){
	    		
	    		List<String> appList = new ArrayList<String>(Arrays.asList(offerModel.getApplicableTo().split(",")));
	    		if(!appList.contains(""+selectedPlan.getPlanId())){
	    			continue;
	    		}
		    	LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View convertView = inflater.inflate( R.layout.offer_details_item, null);
				TextView offerName = (TextView) convertView.findViewById(R.id.offerName);
				TextView offeredVal = (TextView) convertView.findViewById(R.id.offeredVal);
				
				LinearLayout selectLyt = (LinearLayout) convertView.findViewById(R.id.selectLyt);
				CheckBox select= (CheckBox) convertView.findViewById(R.id.select);
				offerName.setText(offerModel.getOfferName());
				selectLyt.setVisibility(View.VISIBLE);

				select.setTag(offerModel);
				double offerPrice = selectedPlan.getPlanPrice() - (selectedPlan.getPlanPrice()*offerModel.getOfferValue()/100.00);
				offeredVal.setText("$"+offerPrice);
				
				select.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						CheckBox cb = (CheckBox)arg0;
						if(currentCB != null && currentCB !=cb){
							if(currentCB.isChecked()){
								currentCB.setChecked(false);	
							}
						}
						
						boolean isChecked = cb.isChecked();
						PlanOfferModel offrMdl = (PlanOfferModel)arg0.getTag();
						if(isChecked){
							selectedOfferModel = offrMdl;
						}else{
							selectedOfferModel = null;
						}
						calculatePlanCost();
						loadAddons(addonLyt);
						currentCB = cb;
					}
				});
				
				
		    	
				offerLyt.addView(convertView);
	    	}
	    	
    	}else{
    		
    	}
    	
    	
    }
	 private void loadAddons(LinearLayout addOnItemLyt){
    	addOnItemLyt.removeAllViews();
    	if(SmartspaceBO.addonList != null){
	    	for(ResourceTypeModel addonModel:SmartspaceBO.addonList){
	    		if(selPlanResIds.contains(addonModel.getResourceTypeId())){
	    			continue;
	    		}
		    	LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View convertView = inflater.inflate( R.layout.addon_details_item, null);
				final MyEditText resCount = (MyEditText) convertView.findViewById(R.id.resCount);
				TextView resName = (TextView) convertView.findViewById(R.id.resName);
				final TextView currDetl = (TextView) convertView.findViewById(R.id.currDetl);
				final TextView totPrice = (TextView) convertView.findViewById(R.id.totPrice);
				LinearLayout selectLyt = (LinearLayout) convertView.findViewById(R.id.selectLyt);
				CheckBox select= (CheckBox) convertView.findViewById(R.id.select);
				addonModel.setNoOfAddOns(1);
				resName.setText(addonModel.getResourceTypeName());
				resCount.setText("1");
				resCount.setTag(addonModel);
				resCount.setClickable(false);
				resCount.setEnabled(false);
				
				resCount.setOnFocusChangeListener(new OnFocusChangeListener() {
					
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						ResourceTypeModel addonModel = (ResourceTypeModel) v.getTag();
						EditText et = (EditText) v;
						Log.e("hasFocus", hasFocus+"");
						if(!hasFocus){
							int cnt = Integer.parseInt(et.getText().toString());
							addonModel.setNoOfAddOns(cnt);
							double addonPrice = addonModel.getPlanSplPrice();
							Log.e("cnt", cnt+"");
							Log.e("cnt", cnt+"");
							Log.e("cnt", cnt+"");
							double totPriceVal = (addonModel.getNoOfAddOns() ==0 ?1:addonModel.getNoOfAddOns())*addonModel.getPlanSplPrice();
							if(selectedOfferModel != null){
								addonPrice = addonModel.getPlanSplPrice() - (addonModel.getPlanSplPrice()*selectedOfferModel.getOfferValue()/100.00);
								totPriceVal = totPriceVal - (totPriceVal*selectedOfferModel.getOfferValue()/100.00);
							}
							totPrice.setText(totPriceVal+"");
							currDetl.setText("$"+addonPrice);
							for(ResourceTypeModel selAddon :selectedAddOnsList) {
								if(addonModel.getResourceTypeId() == selAddon.getResourceTypeId()){
									selAddon.setNoOfAddOns(cnt);
									calculatePlanCost();
									break;
								}
							}
						}
					}
				});
				
				selectLyt.setVisibility(View.VISIBLE);
				currDetl.setVisibility(View.VISIBLE);
				select.setTag(addonModel);
				double addonPrice = addonModel.getPlanSplPrice();
				double totPriceVal = (addonModel.getNoOfAddOns() ==0 ?1:addonModel.getNoOfAddOns())*addonModel.getPlanSplPrice();
				if(selectedOfferModel != null){
					addonPrice = addonModel.getPlanSplPrice() - (addonModel.getPlanSplPrice()*selectedOfferModel.getOfferValue()/100.00);
					totPriceVal = totPriceVal - (totPriceVal*selectedOfferModel.getOfferValue()/100.00);
				}
				totPrice.setText(totPriceVal+"");
				currDetl.setText("$"+addonPrice+"/"+addonModel.getPlanLimitUnit());
				for(ResourceTypeModel selAddon :selectedAddOnsList) {
					if(addonModel.getResourceTypeId() == selAddon.getResourceTypeId()){
						select.setChecked(true);
						if(!addonModel.getPlanLimitUnit().equalsIgnoreCase("month")){
							resCount.setBackgroundResource(R.drawable.bluerectangleborder);
							resCount.setClickable(true);
							resCount.setEnabled(true);
						}else{
							resCount.setBackgroundResource(R.drawable.grayrectangleborder);
							resCount.setClickable(false);
							resCount.setEnabled(false);
							
						}
						resCount.setText(selAddon.getNoOfAddOns()==0?"1":selAddon.getNoOfAddOns()+"");
						totPriceVal = (selAddon.getNoOfAddOns() ==0 ?1:selAddon.getNoOfAddOns())*selAddon.getPlanSplPrice();
						if(selectedOfferModel != null){
							addonPrice = selAddon.getPlanSplPrice() - (selAddon.getPlanSplPrice()*selectedOfferModel.getOfferValue()/100.00);
							totPriceVal = totPriceVal - (totPriceVal*selectedOfferModel.getOfferValue()/100.00);
						}
						totPrice.setText(totPriceVal+"");
						currDetl.setText("$"+addonPrice+"/"+addonModel.getPlanLimitUnit());
						break;
					}
				}
				
				
				select.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						ResourceTypeModel resType = (ResourceTypeModel)buttonView.getTag();
						if(isChecked){
							
							int cnt = Integer.parseInt(resCount.getText().toString());
							if(!resType.getPlanLimitUnit().equalsIgnoreCase("month")){
								resCount.setBackgroundResource(R.drawable.bluerectangleborder);
								resCount.setClickable(true);
								resCount.setEnabled(true);
							}else{
								resCount.setBackgroundResource(R.drawable.grayrectangleborder);
								resCount.setClickable(false);
								resCount.setEnabled(false);
								
							}
							resType.setNoOfAddOns(cnt);
							selectedAddOnsList.add(resType);
							double addonPrice = resType.getPlanSplPrice();
							double totPriceVal = (resType.getNoOfAddOns() ==0 ?1:resType.getNoOfAddOns())*resType.getPlanSplPrice();
							if(selectedOfferModel != null){
								addonPrice = resType.getPlanSplPrice() - (resType.getPlanSplPrice()*selectedOfferModel.getOfferValue()/100.00);
								totPriceVal = totPriceVal - (totPriceVal*selectedOfferModel.getOfferValue()/100.00);
							}
							totPrice.setText(totPriceVal+"");
							currDetl.setText("$"+addonPrice);
							
						}else{
							resCount.setBackgroundResource(R.drawable.grayrectangleborder);
							double addonPrice = resType.getPlanSplPrice();
							double totPriceVal = resType.getPlanSplPrice();
							resCount.setText("1");
							resType.setNoOfAddOns(1);
							if(selectedOfferModel != null){
								addonPrice = resType.getPlanSplPrice() - (resType.getPlanSplPrice()*selectedOfferModel.getOfferValue()/100.00);
								totPriceVal = totPriceVal - (totPriceVal*selectedOfferModel.getOfferValue()/100.00);
							}
							totPrice.setText(totPriceVal+"");
							currDetl.setText("$"+addonPrice);
							selectedAddOnsList.remove(resType);
							resCount.setClickable(false);
							resCount.setEnabled(false);
						}
						
						calculatePlanCost();
					}
				});
		    	
				addOnItemLyt.addView(convertView);
	    	}
	    	
    	}else{
    		
    	}
    	
    	
    }
	private void calculatePlanCost(){
		planCostVal = 0;
		if(selectedPlan != null){
			planCostVal = selectedPlan.getPlanPrice();
		}
		if(selectedAddOnsList != null){
			for(ResourceTypeModel resModel : selectedAddOnsList){
				planCostVal = planCostVal + ((resModel.getNoOfAddOns() ==0 ?1:resModel.getNoOfAddOns())*resModel.getPlanSplPrice());
			}
		}
		if(selectedOfferModel != null){
			planCostVal = planCostVal - (planCostVal*selectedOfferModel.getOfferValue()/100.00);
		}
		planCost.setText("Plan cost : $"+planCostVal+"");
	}
    private void loadResourcesForPlan(LinearLayout planDetl){
    	planDetl.removeAllViews();
    	if(selectedPlan != null && selectedPlan.getResourceTypeList() != null){
    		//Collections.sort(planModel.getResourceTypeList());
	    	for(ResourceTypeModel model: selectedPlan.getResourceTypeList()){
	    		if(model.getAllowUsageBy().equalsIgnoreCase("ondemand")){
	    			continue;
	    		}
				LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View convertView = inflater.inflate( R.layout.plan_details_item, null);
				TextView resLimit = (TextView) convertView.findViewById(R.id.resLimit);
				TextView resName = (TextView) convertView.findViewById(R.id.resName);
				TextView resDetl = (TextView) convertView.findViewById(R.id.resDetl);
				if(model.getResourceDesc() == null || model.getResourceDesc().trim().isEmpty()){
					resName.setVisibility(View.VISIBLE);
					resLimit.setVisibility(View.VISIBLE);
					resDetl.setVisibility(View.GONE);
					resName.setText(model.getResourceTypeName());
					if(model.getAllowUsageBy().equalsIgnoreCase("free") ){
						resLimit.setText("Unlimited");
					}else{
						if(model.getPlanLimit() < 0){
							resLimit.setText("Unlimited");
						}else if (model.getPlanLimit() == 0){
							resLimit.setText("$"+model.getPlanSplPrice()+"/"+model.getPlanLimitUnit());
						}else{
							resLimit.setText(model.getPlanLimit()+" "+model.getPlanLimitUnit());
						}
						
					}
				}else{
					resName.setVisibility(View.GONE);
					resLimit.setVisibility(View.GONE);
					resDetl.setVisibility(View.VISIBLE);
					resDetl.setText(model.getResourceDesc());
					
				}
				
				planDetl.addView(convertView);
			}
	    	
    	}
    }
	

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void hideVirtualKeyBoard() {

		try {
			InputMethodManager imm = (InputMethodManager) mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(((Activity)mContext).getCurrentFocus().getWindowToken(), 0);
			rootView.requestFocus();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
  

   
}
