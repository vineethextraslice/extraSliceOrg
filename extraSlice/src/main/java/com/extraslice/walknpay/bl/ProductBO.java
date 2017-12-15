package com.extraslice.walknpay.bl;

import android.content.Context;

import com.extraslice.walknpay.dao.ProductDAO;
import com.extraslice.walknpay.model.ProductModel;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class ProductBO {
	Context mContext;
	ProductDAO dao;

	public ProductBO(Context mContext) {
		this.mContext = mContext;
		dao = new ProductDAO();
	}

	public ProductModel getProductForStoreByCode(String productCode, int storeId, String StatusFilter) {
		ProductDAO dao = new ProductDAO();
		ProductModel products = null;
		JSONObject rootObject = dao.getProductForStoreByCode(mContext, productCode, Utilities.selectedStore.getStoreId(), Utilities.STATUS_FILTER_ACTIVE);
		try {
			if (rootObject != null &&  rootObject.get(Utilities.STATUS_STRING) != null &&  rootObject.get(Utilities.STATUS_STRING).toString().equals(Utilities.STATUS_SUCCESS)) {
				try {
					products = new ProductModel().jSonToObject(rootObject.get("Product").toString());
				} catch (CustomException e1) {
				}
			}
		} catch (JSONException e2) {
			e2.printStackTrace();
		}
		return products;
	}

	
}
