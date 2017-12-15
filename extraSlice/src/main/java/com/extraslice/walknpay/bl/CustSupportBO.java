package com.extraslice.walknpay.bl;

import android.content.Context;

import com.extraslice.walknpay.dao.CustSupportDAO;
import com.extraslice.walknpay.model.SupportOfficerModel;

import org.codehaus.jettison.json.JSONObject;

public class CustSupportBO {
	Context mContext;
	CustSupportDAO custSupportDAO;

	public CustSupportBO(Context context) {
		mContext = context;
		custSupportDAO = new CustSupportDAO();
	}

	public SupportOfficerModel assignAvailableOfficer(int userId) {
		SupportOfficerModel officerModel = null;

		try {
			JSONObject rootObject = custSupportDAO.assignAvailableOfficer(mContext, userId);
			if (rootObject == null) {
				officerModel = null;
				Utilities.statusValue = Utilities.STATUS_FAILED;
				Utilities.errorMessagenew = "null value from server";
			} else if (((String) rootObject.get(Utilities.STATUS_STRING)).equals(Utilities.STATUS_SUCCESS)) {
				officerModel = new SupportOfficerModel().jSonToObject(rootObject.get("CustomerOfficer").toString()); 
			} else {
				officerModel = null;
				Utilities.statusValue = Utilities.STATUS_FAILED;
				String errorMessage = (String) rootObject.get(Utilities.ERROR_MESSAGE);
				Utilities.errorMessagenew = errorMessage;
			}
		} catch (Exception e) {
			officerModel = null;
			Utilities.statusValue = Utilities.STATUS_FAILED;
			Utilities.errorMessagenew = e.getLocalizedMessage();
		}
		return officerModel;
	}

	public boolean updateOfficerAvailable(int officerId) {
		try {
			JSONObject rootObject = custSupportDAO.updateOfficerAvailable(mContext, officerId);
			if (rootObject == null) {
				return false;
			} else if (((String) rootObject.get(Utilities.STATUS_STRING)).equals(Utilities.STATUS_SUCCESS)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public boolean updateLastSupportTime(int officerId, int userId, String chatId) {
		try {
			JSONObject rootObject = custSupportDAO.updateLastSupportTime(mContext, officerId, userId, chatId);
			if (rootObject == null) {
				return false;
			} else if (((String) rootObject.get(Utilities.STATUS_STRING)).equals(Utilities.STATUS_SUCCESS)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}
}
