package com.app.extraslice.utils;

import android.content.Context;
import android.os.AsyncTask;

import com.app.extraslice.bo.SmartspaceBO;
import com.app.extraslice.bo.UserBO;
import com.app.extraslice.model.Organization;
import com.app.extraslice.model.UserModel;
import com.stripe.model.Charge;
import com.stripe.model.Customer;

public class UpdateUserSubscription   extends AsyncTask<Void, Void,Object> {
	Customer customer;
	Context mContext;
	public String errorMessage=null;
	UserModel uModel;
	int planId;
	boolean isNewUser;
	String newOrgName;
	String gateWay;
	String acctId;
	Charge ch;
	public UpdateUserSubscription(Context mContext,Customer customer,Charge ch,boolean isNewUser,UserModel uModel,int planId,String newOrgName,String gateWay,String acctId){
		this.mContext=mContext;
		this.customer = customer;
		this.planId = planId;
		this.uModel = uModel;
		this.isNewUser = isNewUser;
		this.newOrgName = newOrgName;
		this.gateWay = gateWay;
		this.acctId = acctId;
		this.ch = ch;
	}

	@Override
	protected void onPreExecute() {	
		ProgressClass.startProgress(mContext);
		super.onPreExecute();
	}
	@Override
	protected Object doInBackground(Void... arg0) {
		SmartspaceBO smBO = new SmartspaceBO(mContext);
		Object jsonReslt = null;
		try{
			int orgId = -1;
			UserBO uesrBo = new UserBO(mContext);
			for(Organization org : uModel.getOrgList()){
				if(org.getOrgName().equals(newOrgName)){
					orgId = org.getOrgId();
					break;
				}
			}
			if (customer !=null) {
				try {
					jsonReslt = uesrBo.addUserSubscription(uModel.getUserId(), orgId, planId, customer,ch,gateWay,acctId);
				} catch (Exception e2) {
					errorMessage = e2.getLocalizedMessage();
				}
				
			}else{
				if(isNewUser){
					jsonReslt = uesrBo.deleteUser(uModel.getUserId(),orgId);	
				}else{
					jsonReslt = smBO.deleteOrgPlan(uModel.getUserId(),orgId);
				}
			}		
		}catch(Exception e){
			errorMessage = e.getLocalizedMessage();
		}
		return jsonReslt;
	}
	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);	
		ProgressClass.finishProgress();
	}
}
