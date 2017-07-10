package com.app.extraslice.utils;
import java.security.Key;
import java.text.DecimalFormat;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.app.extraslice.R;
import com.app.extraslice.model.SmartSpaceModel;
import com.app.extraslice.model.UserModel;

public class Utilities {
	public static UserModel loggedInUser;
	public static final String ERROR_MESSAGE = "ERRORMESSAGE";
	public static final String STATUS_SUCCESS = "SUCCESS";
	public static final String STATUS_FAILED = "FAILED";
	public static final String STATUS_STRING = "STATUS";
	public static final String WARNING_MESAGE = "WARNING";
	public static String SCAN_FOR = "PURCHASE";
	public static final String SCAN_FOR_PURCHASE = "PURCHASE";
	public static final String SCAN_FOR_STORE = "STORE";
	public static final String SCAN_FOR_PAYPAL = "PAYPAL";
	//public static String mainUrl = "http://walknpaydev01.cloudapp.net:8080/ExtraSliceWebService/jsonws";
	public static String mainUrl ="https://extraslice.com/ExtraSliceWebService/jsonws";
	//
		//
	private static final String ALGORITHM = "AES";
	private static final byte[] keyValue = new byte[] { 'S', 'e', 'c', 'K', 'e', 'y', '@', 'S', 'l', 'i', 'c', 'e', '4', 'W', 'n', 'P' };
	public static String WARNING_FROM_SERVER=null;
	public static List<SmartSpaceModel> smartSpaceList ;
	public static String encode(String valueToEnc) throws Exception {
		Key key = generateKey();
		Cipher c = Cipher.getInstance(ALGORITHM);
		c.init(Cipher.ENCRYPT_MODE, key);
		byte[] encValue = c.doFinal(valueToEnc.getBytes());
		String encryptedValue = new BASE64Encoder().encode(encValue);
		return encryptedValue;
	}
	public static String roundto2Decimal(double amount) {
		String v = String.valueOf(new DecimalFormat("##.##").format(amount));
		if (v.indexOf(".") >= 0) {
			String v1 = "00";
			if (v.indexOf(".") > 0) {
				v1 = v.substring(0, v.indexOf("."));
			}
			String v2 = v.substring((v.indexOf(".") + 1), v.length());
			if (v1.length() < 2) {
				v1 = 0 + v1;
			}
			if (v2.length() < 2) {
				v2 = v2 + 0;
			}
			v = v1 + "." + v2;
		}
		return v;
	}
	public static String decode(String encryptedValue) throws Exception {
		Key key = generateKey();
		Cipher c = Cipher.getInstance(ALGORITHM);
		c.init(Cipher.DECRYPT_MODE, key);
		byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedValue);
		byte[] decValue = c.doFinal(decordedValue);
		String decryptedValue = new String(decValue);
		return decryptedValue;
	}

	private static Key generateKey() throws Exception {
		Key key = new SecretKeySpec(keyValue, ALGORITHM);
		return key;
	}
	
	public static void loadFragment(FragmentManager frgManager,Fragment newFrgment,int fragmentViewId,boolean isReverse){
		FragmentTransaction trxn = frgManager.beginTransaction();
		if(isReverse){
			trxn.setCustomAnimations(R.anim.frg_left_to_right_enter,R.anim.frg_left_to_right_exit);
		}else{
			trxn.setCustomAnimations(R.anim.frg_right_to_left_enter,R.anim.frg_right_to_left_exit);
		}
		trxn.addToBackStack(null);
		trxn.replace(fragmentViewId, newFrgment).addToBackStack(null).commit();	
	}
}
