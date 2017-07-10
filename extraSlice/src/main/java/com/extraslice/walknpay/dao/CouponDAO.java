package com.extraslice.walknpay.dao;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import android.content.Context;

import com.extraslice.walknpay.adapter.CartListAdapter;
import com.extraslice.walknpay.bl.CustomException;
import com.extraslice.walknpay.bl.RunJSon;
import com.extraslice.walknpay.bl.Utilities;
import com.extraslice.walknpay.model.CouponModel;
import com.extraslice.walknpay.model.CustAcctModel;
import com.extraslice.walknpay.model.PurchasedProductModel;
import com.extraslice.walknpay.ui.CartFragment;

public class CouponDAO {
	//applyDefaultCoupons applyCoupon assignCoupon updateCoupon addCoupon
	public JSONObject getAllCouponsForPurchase(Context mContext, int userId,int storeId,List<PurchasedProductModel> productList) {
		String urlString = Utilities.mainUrl + "/coupon/getAllCouponsForPurchase";
		JSONObject request = new JSONObject();
		JSONObject rootObject = null;
		try {
			request.put("storeId", storeId);
			request.put("userId", userId);
			JSONArray jsonArray = new JSONArray();
			for(PurchasedProductModel model : productList){
				try {
					jsonArray.put(model.toJSonObject());
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
			request.put("itemList", jsonArray);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		/*try {
			rootObject = new RunJSon(mContext, urlString, request.toString()).execute().get();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		try {
			rootObject = WSConnnection.getResult(urlString, request.toString(),mContext);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rootObject;
	}
	
	
	//Manacy created for getting prepaid balance . function written seperate bcos the above is under processing
	
public JSONObject addPrepaidBalance(Context mContext,JSONObject request) throws CustomException{
		
		String urlString = Utilities.mainUrl + "/coupon/addPrepaidBalance";
		JSONObject rootObject = null;
		/*try {
			rootObject = new RunJSon(mContext, urlString, request.toString()).execute().get();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new CustomException(e.getLocalizedMessage());
		}*/
		try {
			rootObject = WSConnnection.getResult(urlString, request.toString(),mContext);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new CustomException(e.getLocalizedMessage());
		}
		return rootObject;
		
		
	}
public JSONObject getAllPrepaidCoupons(Context mContext,int userID,int id){
		JSONObject request = new JSONObject();
		String urlString = Utilities.mainUrl + "/coupon/getAllPrepaidCoupons";
		JSONObject rootObject = null;
		/*try {
			request.put("userID", 1);
			rootObject = new RunJSon(mContext, urlString, request.toString()).execute().get();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		try {
			request.put("userID", 1);
			rootObject = WSConnnection.getResult(urlString, request.toString(),mContext);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rootObject;
		
		
	}
	public JSONObject reallocatePrepaid(Context mContext,long trxnId,int userId,int storeId) {
		String urlString = Utilities.mainUrl + "/coupon/reallocatePrepaid";
		JSONObject rootObject = null;
		/*try {
			rootObject = new RunJSon(mContext, urlString, request.toString()).execute().get();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		JSONObject request = new JSONObject();
		try {
			request.put("storeId", storeId);
			request.put("userId", userId);
			request.put("TransactionId", trxnId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			rootObject = WSConnnection.getResult(urlString, request.toString(),mContext);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rootObject;
	}
	//Manacy created for getting prepaid balance . function written seperate bcos the above is under processing
	
	public JSONObject getPrepaidBalance(Context mContext, int userId,int storeId) {
		String urlString = Utilities.mainUrl + "/coupon/getPrepaidBalanceOfUser";
		JSONObject request = new JSONObject();
		JSONObject rootObject = null;
		try {
			request.put("storeId", storeId);
			request.put("userId", userId);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		/*try {
			rootObject = new RunJSon(mContext, urlString, request.toString()).execute().get();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		try {
			rootObject = WSConnnection.getResult(urlString, request.toString(),mContext);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rootObject;
	}
	
	public JSONObject updatePrepaidToComplete(Context mContext,long transactionId, int userId,int storeId) {
		String urlString = Utilities.mainUrl + "/coupon/updatePrepaidToComplete";
		JSONObject request = new JSONObject();
		JSONObject rootObject = null;
		try {
			request.put("storeId", storeId);
			request.put("userId", userId);
			request.put("TransactionId", transactionId);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		/*try {
			rootObject = new RunJSon(mContext, urlString, request.toString()).execute().get();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		try {
			rootObject = WSConnnection.getResult(urlString, request.toString(),mContext);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rootObject;
	}
	
	
	public JSONObject applyAllCoupons(Context mContext, int userId, int storeId,JSONArray couponList) {
		String urlString = Utilities.mainUrl + "/coupon/applyAllCoupons";
		JSONObject request = new JSONObject();
		try {
			request.put("userId", userId);
			request.put("storeId", storeId);
			JSONArray ayyayProducts = new JSONArray();
			JSONObject trxn = new JSONObject();

			trxn.put("userId", userId);
			trxn.put("storeId", storeId);
			int totalCount = 0;
			double totalAmount=0;

			trxn.put("itemList", ayyayProducts);
			request.put("TransactionModel", trxn);
			request.put("totalAmount", totalAmount);
			request.put("totalCount", totalCount);
			request.put("couponList", couponList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JSONObject rootObject = null;
		/*try {
			rootObject = new RunJSon(mContext, urlString, request.toString()).execute().get();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		try {
			rootObject = WSConnnection.getResult( urlString, request.toString(),mContext);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rootObject;
	}

}
