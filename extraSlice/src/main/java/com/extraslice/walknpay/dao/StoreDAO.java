package com.extraslice.walknpay.dao;

import org.codehaus.jettison.json.JSONObject;

import android.content.Context;

import com.extraslice.walknpay.bl.RunJSon;
import com.extraslice.walknpay.bl.Utilities;
import com.extraslice.walknpay.model.StoreModel;

public class StoreDAO {
	public JSONObject getAllStoresForDealer(Context mContext, int dealerId, String StatusFilter,
			double latitude,double longitude) {
		if(longitude == 0 && latitude ==0 ){
			return getAllStoresForDealer(mContext, dealerId, StatusFilter);
		}else{
			String urlString = Utilities.mainUrl + "/store/getAllStoresForDealerByLocation";
			JSONObject request = new JSONObject();
			StoreModel model = new StoreModel();
			model.setDealerId(dealerId);

			JSONObject rootObject = null;
			/*try {
				request.put("StoreModel", model.toJSonObject());
				request.put("StatusFilter", StatusFilter);

				rootObject = new RunJSon(mContext, urlString, request.toString()).execute().get();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			try {
				request.put("StoreModel", model.toJSonObject());
				request.put("StatusFilter", StatusFilter);
				request.put("longitude", longitude);
				request.put("latitude", latitude);

				rootObject = WSConnnection.getResult(urlString, request.toString(),mContext);
				String status =rootObject.get(Utilities.STATUS_STRING).toString();
				String count = rootObject.get("COUNT").toString();
				if(status.equalsIgnoreCase(Utilities.STATUS_FAILED) || Integer.parseInt(count)<=0){
					return getAllStoresForDealer(mContext, dealerId, StatusFilter);
				}
			} catch (Exception e) {
				return getAllStoresForDealer(mContext, dealerId, StatusFilter);
			}
			return rootObject;
		}
	}
	
	public JSONObject getAllStoresForDealer(Context mContext, int dealerId, String StatusFilter) {
		String urlString = Utilities.mainUrl + "/store/getAllStoresForDealer";
		JSONObject request = new JSONObject();
		StoreModel model = new StoreModel();
		model.setDealerId(dealerId);

		JSONObject rootObject = null;
		/*try {
			request.put("StoreModel", model.toJSonObject());
			request.put("StatusFilter", StatusFilter);

			rootObject = new RunJSon(mContext, urlString, request.toString()).execute().get();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		try {
			request.put("StoreModel", model.toJSonObject());
			request.put("StatusFilter", StatusFilter);

			rootObject = WSConnnection.getResult(urlString, request.toString(),mContext);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rootObject;
	}

	public JSONObject getSelectdStore(Context mContext, int storeId) {
		String urlString = Utilities.mainUrl + "/store/getStoreById";
		StoreModel model = new StoreModel();
		model.setStoreId(storeId);

		/*JSONObject rootObject = null;
		try {
			rootObject = new RunJSon(mContext, urlString, model.toJSonObject().toString()).execute().get();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		JSONObject rootObject = null;
		try {
			rootObject = WSConnnection.getResult(urlString, model.toJSonObject().toString(),mContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rootObject;
	}


}
