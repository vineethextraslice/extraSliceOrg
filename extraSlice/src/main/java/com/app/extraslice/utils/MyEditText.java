package com.app.extraslice.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class MyEditText extends EditText {

	Context context;

	public MyEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}


	@Override
	public boolean onKeyPreIme(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.clearFocus();
			InputMethodManager mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			mgr.hideSoftInputFromWindow(this.getWindowToken(), 0);
		}
		return super.onKeyPreIme(keyCode, event);
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		return super.dispatchKeyEvent(event);
	}
	/*public void textChanged(PlanOfferModel selectedOfferModel,List<ResourceTypeModel> selectedAddOnsList,
			TextView totPrice,TextView currDetl) {
		ResourceTypeModel addonModel = (ResourceTypeModel) this.getTag();
		int cnt = Integer.parseInt(this.getText().toString());
		addonModel.setNoOfAddOns(cnt);
		double addonPrice = addonModel.getPlanSplPrice();
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
		
	}*/
}