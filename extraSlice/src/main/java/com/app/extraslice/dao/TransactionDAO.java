package com.app.extraslice.dao;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.app.extraslice.connection.WSConnnection;
import com.app.extraslice.model.PlanOfferModel;
import com.app.extraslice.model.ResourceTypeModel;
import com.app.extraslice.model.UserModel;
import com.app.extraslice.model.UserRequestModel;
import com.app.extraslice.utils.CustomException;
import com.app.extraslice.utils.Utilities;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.CustomerSubscriptionCollection;

public class TransactionDAO {
	Context mContext;
	public TransactionDAO(Context mContext){
		this.mContext = mContext;
	}
	
	public JSONObject getForumLoginUrl(int userId) throws CustomException {
		String urlString = Utilities.mainUrl + "/user/getForumLoginUrl";
		JSONObject rootObject = null;
		JSONObject request = new JSONObject();
		try {
			request.put("userId",userId);
			rootObject = WSConnnection.getResult(urlString, request.toString(),mContext);
		} catch (Exception e) {
			throw new CustomException(e.getLocalizedMessage());
		}
		return rootObject;
	}
	
}
