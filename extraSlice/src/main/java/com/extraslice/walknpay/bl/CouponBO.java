package com.extraslice.walknpay.bl;

import android.content.Context;
import android.util.Log;

import com.extraslice.walknpay.dao.CouponDAO;
import com.extraslice.walknpay.model.CouponModel;
import com.extraslice.walknpay.ui.CartFragment;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CouponBO {

	static Context mContext;
	CouponDAO dao = null;

	public CouponBO(Context context) {
		mContext = context;
		dao = new CouponDAO();
	}

	
	public List<CouponModel> getAllPrepaidCoupons() throws CustomException{
		List<CouponModel> couponList = new ArrayList<CouponModel>();
		JSONObject couponListJson=null;
		Object object = null;
		try{
			
			 couponListJson = dao.getAllPrepaidCoupons(mContext,Utilities.loginUserID,1);
			
			 if (couponListJson == null ) {
					
				} else if (((String) couponListJson.get(Utilities.STATUS_STRING)).equals(Utilities.STATUS_SUCCESS)) {
					JSONArray couponArray = couponListJson.getJSONArray("CouponList");
					for(int index=0;index<couponArray.length();index++){
						couponList.add(new CouponModel().jSonToObject(couponArray.get(index).toString()));
					}
				}
		} catch (JSONException e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return couponList;
		
	}
	
	public List<CouponModel> getAllCouponsForPurchase() throws CustomException{
		List<CouponModel> couponList = new ArrayList<CouponModel>();
		try{
			JSONObject couponListJson = dao.getAllCouponsForPurchase(mContext, 
					Utilities.loggedInUser.getUserId(),Utilities.selectedStore.getStoreId(),CartFragment.productslist);
			
			if (couponListJson == null ) {
				
			} else if (((String) couponListJson.get(Utilities.STATUS_STRING)).equals(Utilities.STATUS_SUCCESS)) {
				JSONArray couponArray = couponListJson.getJSONArray("CouponList");
				for(int index=0;index<couponArray.length();index++){
					couponList.add(new CouponModel().jSonToObject(couponArray.get(index).toString()));
				}
			}
		} catch (JSONException e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return couponList;
	}
	
	public Object updatePrepaidToComplete(long transactionId) throws CustomException{
		Object object = null;
		try{
			JSONObject couponListJson = dao.updatePrepaidToComplete(mContext,transactionId, Utilities.loginUserID,Utilities.selectedStore.getStoreId());
			if (couponListJson == null ) {
			} else if (((String) couponListJson.get(Utilities.STATUS_STRING)).equals(Utilities.STATUS_SUCCESS)) {
				object= couponListJson.get("PREPAID_BALANCE");
			}
		} catch (JSONException e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return object;
		
	}
	public Object getPrepaidBalance() throws CustomException{
		Object object = null;
		int storeId = 0;
		if(Utilities.selectedStore != null){
			storeId = Utilities.selectedStore.getStoreId();
		}
		try{
			JSONObject couponListJson = dao.getPrepaidBalance(mContext, Utilities.loginUserID,storeId);
			if (couponListJson == null ) {
			} else if (((String) couponListJson.get(Utilities.STATUS_STRING)).equals(Utilities.STATUS_SUCCESS)) {
				object= couponListJson.get("PREPAID_BALANCE");
			}
		} catch (JSONException e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return object;
		
	}
	public JSONObject assignCoupon(Context context,JSONObject object) throws CustomException{
		JSONObject couponObj=	dao.addPrepaidBalance(context,object);
		return couponObj;
		
	}
	public Object reallocatePrepaid(Context context,long trxnId) throws CustomException {
		Object object=null;
		try{
			JSONObject couponObj=	dao.reallocatePrepaid(context,trxnId, Utilities.loginUserID,Utilities.selectedStore.getStoreId());
			if (couponObj == null ) {
				
			} else if (((String) couponObj.get(Utilities.STATUS_STRING)).equals(Utilities.STATUS_SUCCESS)) {
				object= couponObj.get("PREPAID_BALANCE");
			}
		} catch (JSONException e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return object;
		
	}
	public List<CouponModel> applyAllCoupons(List<CouponModel> couponList) throws CustomException{
		
		try{
			JSONArray array = new JSONArray();
			for(CouponModel model : couponList){
				try {
					array.put(model.toJSonObject());
				} catch (JsonGenerationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			JSONObject couponListJson = dao.applyAllCoupons(mContext, Utilities.loggedInUser.getUserId(), Utilities.selectedStore.getStoreId(),array);
			Log.e("applyAllCoupons", couponListJson.toString());
			if (couponListJson == null ) {
				
			} else if (((String) couponListJson.get(Utilities.STATUS_STRING)).equals(Utilities.STATUS_SUCCESS)) {
				JSONArray couponArray = couponListJson.getJSONArray("CouponList");
				for(int index=0;index<couponArray.length();index++){
					couponList.add(new CouponModel().jSonToObject(couponArray.get(index).toString()));
				}
			}
		} catch (JSONException e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return couponList;
	}
	
	
	

}
