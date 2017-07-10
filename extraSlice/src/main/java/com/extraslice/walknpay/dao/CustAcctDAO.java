package com.extraslice.walknpay.dao;

import org.codehaus.jettison.json.JSONObject;

import android.content.Context;

import com.extraslice.walknpay.bl.RunJSon;
import com.extraslice.walknpay.bl.Utilities;
import com.extraslice.walknpay.model.CustAcctModel;

public class CustAcctDAO {
	public JSONObject getUserAccount(Context mContext, int userId,String strpAcct) {
		String urlString = Utilities.mainUrl + "/custacct/getCutomerAccount";
		CustAcctModel model = new CustAcctModel();
		model.setUserId(userId);
		model.setStrpAcct(strpAcct);
		JSONObject rootObject = null;
		/*try {
			rootObject = new RunJSon(mContext, urlString, model.toJSonObject().toString()).execute().get();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		try {
			rootObject = WSConnnection.getResult(urlString, model.toJSonObject().toString(),mContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rootObject;
	}

	public JSONObject addUserAccount(Context mContext, int userId,String strpAcct,String custId) {
		String urlString = Utilities.mainUrl + "/custacct/addCutomerAccount";
		CustAcctModel model = new CustAcctModel();
		try {
			model.setUserId(userId);
			model.setCustomerId(custId);
			model.setStrpAcct(strpAcct);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		JSONObject rootObject = null;
		/*try {
			rootObject = new RunJSon(mContext, urlString, model.toJSonObject().toString()).execute().get();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		try {
			rootObject =WSConnnection.getResult(urlString, model.toJSonObject().toString(),mContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rootObject;
	}

}
