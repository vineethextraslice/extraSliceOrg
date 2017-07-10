package com.extraslice.walknpay.dao;

import org.codehaus.jettison.json.JSONObject;

import android.content.Context;

import com.extraslice.walknpay.bl.RunJSon;
import com.extraslice.walknpay.bl.Utilities;
import com.extraslice.walknpay.model.ProductModel;

public class ProductDAO {
	
	public JSONObject getProductForStoreByCode(Context mContext, String productCode, int storeId, String StatusFilter) {
		String urlString = Utilities.mainUrl + "/products/getProductsForStoreByCode";
		JSONObject model = new JSONObject();

		JSONObject requestInput = new JSONObject();
		try {
			model.put("storeId", storeId);
			model.put("code", productCode);
			requestInput.put("ProductModel", model);
			requestInput.put("StatusFilter", StatusFilter);
		} catch (Exception e) {
		}

		JSONObject rootObject = null;
		/*try {
			rootObject = new RunJSon(mContext, urlString, requestInput.toString()).execute().get();
		} catch (Exception e) {
		}*/
		try {
			rootObject = WSConnnection.getResult(urlString, requestInput.toString(),mContext);
		} catch (Exception e) {
			System.out.println(e);
		}

		return rootObject;
	}

}
