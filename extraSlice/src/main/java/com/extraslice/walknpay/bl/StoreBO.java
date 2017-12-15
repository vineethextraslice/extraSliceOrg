package com.extraslice.walknpay.bl;

import android.content.Context;

import com.extraslice.walknpay.dao.StoreDAO;
import com.extraslice.walknpay.model.StoreModel;
import com.extraslice.walknpay.ui.MenuActivity;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StoreBO {
	Context mContext;
	StoreDAO dao;

	public StoreBO(Context context) {
		mContext = context;
		dao = new StoreDAO();
	}

	public List<StoreModel> getAllStore(String StatusFilter,double latitude,double longitude) {
		List<StoreModel> storeList = new ArrayList<StoreModel>();
		StoreModel model = null;
		JSONObject rootObject = null;
		try {
			dao = new StoreDAO();
			rootObject = dao.getAllStoresForDealer(mContext, 1, StatusFilter,latitude,longitude);
			if(rootObject.getString(Utilities.STATUS_STRING) != null 
					&& rootObject.getString(Utilities.STATUS_STRING).equalsIgnoreCase(Utilities.STATUS_SUCCESS)){
				JSONArray Orgs = (JSONArray) rootObject.get("StoreList");
				for (int i = 0; i < Orgs.length(); i++) {
					JSONObject storeArray = (JSONObject) Orgs.get(i);
					model = (new StoreModel()).jSonToObject(storeArray.toString());
					storeList.add(model);
				}
			}
			
			if(storeList != null && storeList.size() == 1){
				Utilities.selectedStore = storeList.get(0);
				MenuActivity.currencySymbol = Utilities.selectedStore.getCurrencySymbol();
				if(MenuActivity.currencySymbol != null && MenuActivity.currencySymbol.trim().equalsIgnoreCase("INR")){
					MenuActivity.currencySymbol= "\u20B9";
				}
				if(MenuActivity.currencySymbol == null || MenuActivity.currencySymbol.trim().isEmpty()){
					MenuActivity.currencySymbol="$";
				}
				MenuActivity.currencySymbol = MenuActivity.currencySymbol+" ";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return storeList;
	}

	public StoreModel getSelectedStore(int storeId) {
		StoreModel model = null;
		JSONObject rootObject = null;
		try {
			
			rootObject = dao.getSelectdStore(mContext, storeId);
			JSONObject store = (JSONObject) rootObject.get("Store");
			model = (new StoreModel()).jSonToObject(store.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return model;
	}
	

	

}
