package com.extraslice.walknpay.dao;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.app.extraslice.R;
import com.extraslice.walknpay.bl.CustomException;
import com.extraslice.walknpay.bl.Utilities;
import com.extraslice.walknpay.model.CouponModel;
import com.extraslice.walknpay.model.PurchasedProductModel;
import com.extraslice.walknpay.model.TransactionModel;
import com.extraslice.walknpay.ui.CartFragment;
import com.extraslice.walknpay.ui.PaymentOptionScreen;

public class TransactionDAO {
	@SuppressWarnings("unchecked")
	public TransactionModel addTrasaction(Context mContext,String payMethod) throws CustomException {
		TransactionModel transaction = new TransactionModel();
		TransactionModel addedTrxnModel= new TransactionModel();
		try {
			transaction.setUserId(Utilities.loggedInUser.getUserId());
			transaction.setStoreId(Utilities.selectedStore.getStoreId());
			transaction.setDeviceType("Android");
			transaction.setPayMethod(payMethod);
			if(transaction.getItemList() == null){
				List<PurchasedProductModel> itemList = new ArrayList<PurchasedProductModel>();
				transaction.setItemList(itemList);
			}
			double subTotal=0;
			double grossTotal=0;
			double totalTax=0;
			double offerTotal=0;
			double payableTotal=0;
			for (PurchasedProductModel pm : CartFragment.productslist) {
				PurchasedProductModel obj = new PurchasedProductModel();
				double taxAmt = pm.getPrice() * pm.getTaxPercentage() / 100;
				obj.setTaxAmount(taxAmt);
				obj.setPurchasedQuantity(pm.getPurchasedQuantity());
				obj.setId(pm.getId());
				obj.setCode(pm.getCode());
				obj.setName(pm.getName());
				obj.setStoreId(Utilities.selectedStore.getStoreId());
				obj.setPrice(pm.getPrice());
				obj.setTaxPercentage( pm.getTaxPercentage());
				obj.setStoreItemId(pm.getStoreItemId());
				obj.setOnDemandItem(pm.isOnDemandItem());
				totalTax = totalTax + (taxAmt * pm.getPurchasedQuantity());
				subTotal =subTotal + (pm.getPrice() * pm.getPurchasedQuantity());
				transaction.getItemList().add(obj);
				
			}
			grossTotal = Utilities.roundto2Decimaldouble(subTotal) + Utilities.roundto2Decimaldouble(totalTax);
			transaction.setTotalTax(Utilities.roundto2Decimal(totalTax));
			transaction.setSubTotal(Utilities.roundto2Decimal(subTotal));
			transaction.setGrossTotal(Utilities.roundto2Decimal(grossTotal));
			if(transaction.getCouponList() == null){
				List<CouponModel> itemList = new ArrayList<CouponModel>();
				transaction.setCouponList(itemList);
			}
			for (CouponModel cpn : PaymentOptionScreen.appliedCouponList) {
				if(cpn.isCouponApplied()){
					transaction.getCouponList().add(cpn);
					if(!cpn.getCouponCode().equalsIgnoreCase("PREPAID")){
						offerTotal = offerTotal+cpn.getApplicableAmount();
					}
				}
				
			}
			transaction.setOfferTotal(Utilities.roundto2Decimal(offerTotal));
			payableTotal = Utilities.roundto2Decimaldouble(grossTotal) - Utilities.roundto2Decimaldouble(offerTotal);
			transaction.setPayableTotal(payableTotal+"");
			PaymentOptionScreen.appliedCouponList.clear();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (Utilities.checkInternetConnection(mContext)) {
			String urlString = null;
			try {

				urlString = Utilities.mainUrl + "/transaction/addTransaction";

				JSONObject rootObjectNew;
				try {
					Log.e("rew", transaction.toJSonObject().toString());
					//rootObjectNew = new RunJSon(mContext, urlString, transaction.toJSonObject().toString()).execute().get();
					rootObjectNew = WSConnnection.getResult(urlString, transaction.toJSonObject().toString(),mContext);
					String status = (String) rootObjectNew.get(Utilities.STATUS_STRING);
					if (status.equals(Utilities.STATUS_FAILED)) {
						Fragment fragment = new CartFragment();
						if (fragment != null) {
							FragmentManager fragmentManager = ((Activity) mContext).getFragmentManager();
							fragmentManager.beginTransaction().replace(R.id.frame_container, fragment)
							// .addToBackStack(null)
									.commit();
						}
						final Dialog dialog = new Dialog(mContext);
						dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						dialog.setContentView(R.layout.alert);
						dialog.setTitle("Error");
						dialog.show();

						// set the custom dialog components - text, image and
						// button
						TextView text = (TextView) dialog.findViewById(R.id.content);
						text.setText(rootObjectNew.get(Utilities.ERROR_MESSAGE).toString());

						Button dialogButton = (Button) dialog.findViewById(R.id.alertpositivebutton);
						// if button is clicked, close the custom dialog
						dialogButton.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {

								dialog.dismiss();
							}
						});
					} else {
						JSONObject transactionObj = (JSONObject) rootObjectNew.get("Transaction");
						addedTrxnModel = new TransactionModel().jSonToObject(transactionObj.toString());
						Utilities.billNo = addedTrxnModel.getOrderId();
					}
				} catch (Exception e) {
					transaction = null;
					e.printStackTrace();
					throw new CustomException (e.getLocalizedMessage());
				} 
			} catch (Exception e1) {
				transaction = null;
				e1.printStackTrace();
				throw new CustomException (e1.getLocalizedMessage());
			}
		} else {
			Utilities.showInternetStatus(mContext, false);
		}

		return addedTrxnModel;
	}

	public void deleteTransaction(Context mContext) {
		String urlString = Utilities.mainUrl + "/transaction/deleteTransactionById";
		JSONObject input = new JSONObject();
		try {
			input.put("userId", Utilities.loggedInUser.getUserId());
			input.put("orderId", Utilities.billNo);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		JSONObject rootObject = null;
		/*try {
			rootObject = new RunJSon(mContext, urlString, input.toString()).execute().get();
		} catch (Exception e) {
			e.printStackTrace();
		} */
		try {
			rootObject =WSConnnection.getResult(urlString, input.toString(),mContext);
			Log.e("root",rootObject.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} 

	}

	public void updateTransactionToPurchase(Context mContext) {
		JSONObject rootObject = null;
		String urlString = Utilities.mainUrl + "/transaction/updateTransactionToPurchase";
		JSONObject input = new JSONObject();
		try {
			input.put("userId", Utilities.loggedInUser.getUserId());
			input.put("orderId", Utilities.billNo);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		/*try {
			rootObject = new RunJSon(mContext, urlString, input.toString()).execute().get();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		try {
			rootObject = WSConnnection.getResult(urlString, input.toString(),mContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public JSONObject emailWnPReceipt(Context mContext, int userId,long orderId) {
		JSONObject rootObject = null;
		String urlString = Utilities.mainUrl + "/transaction/sendReceiptByMail";
		JSONObject input = new JSONObject();
		try {
			input.put("userId", userId);
			input.put("orderId", orderId);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		/*try {
			rootObject = new RunJSon(mContext, urlString, input.toString()).execute().get();
		} catch (Exception e) {
			e.printStackTrace();
		}*/ 
		try {
			rootObject = WSConnnection.getResult(urlString, input.toString(),mContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rootObject;
	}
	public JSONObject emailEsliceReceipt(Context mContext, int userId,long orderId) {
		JSONObject rootObject = null;
		String urlString = com.app.extraslice.utils.Utilities.mainUrl + "/transaction/sendReceiptByMail";
		JSONObject input = new JSONObject();
		try {
			input.put("userId", userId);
			input.put("orderId", orderId);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		/*try {
			rootObject = new RunJSon(mContext, urlString, input.toString()).execute().get();
		} catch (Exception e) {
			e.printStackTrace();
		}*/ 
		try {
			rootObject = WSConnnection.getResult(urlString, input.toString(),mContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rootObject;
	}
	
	public JSONObject sendMail(Context mContext, int userId,long orderId) {
		JSONObject rootObject = null;
		String urlString = Utilities.mainUrl + "/transaction/sendReceiptByMail";
		JSONObject input = new JSONObject();
		try {
			input.put("userId", userId);
			input.put("orderId", orderId);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		/*try {
			rootObject = new RunJSon(mContext, urlString, input.toString()).execute().get();
		} catch (Exception e) {
			e.printStackTrace();
		}*/ 
		try {
			rootObject = WSConnnection.getResult(urlString, input.toString(),mContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rootObject;
	}
	public JSONObject getAllESliceTransactionForUser(Context mContext, int userId, String startDate,String endDate) {

		String urlString = com.app.extraslice.utils.Utilities.mainUrl + "/transaction/getAllReceipts";
		JSONObject rootObject = null;
		JSONObject request = new JSONObject();
		try {
			request.put("userId", userId);
			request.put("startDate", startDate);
			request.put("endDate", endDate);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		try {
			Log.e("dd", request.toString());
			rootObject = WSConnnection.getResult(urlString, request.toString(),mContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rootObject;
	}
	
	public JSONObject getAllWnPTransactionForUser(Context mContext, int userId, String startDate,String endDate) {
		String urlString = Utilities.mainUrl + "/transaction/getAllTransactionForUserForPeriod";
		JSONObject rootObject = null;
		JSONObject request = new JSONObject();
		try {
			request.put("userId", userId);
			request.put("startDate", startDate);
			request.put("endDate", endDate);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		try {
			Log.e("dd", request.toString());
			rootObject = WSConnnection.getResult(urlString, request.toString(),mContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rootObject;
	}
	public JSONObject getAllTransactionForUser(Context mContext, int userId, int storeId,int noOfReceipts,int offset) {
		String urlString = Utilities.mainUrl + "/transaction/getAllTransactionForUserWithOffset";
		JSONObject rootObject = null;
		JSONObject trxnModel = new JSONObject();
		JSONObject request = new JSONObject();
		try {
			trxnModel.put("userId", userId);
			trxnModel.put("storeId", storeId);
			request.put("TransactionModel", trxnModel);
			request.put("noOfReceipts", noOfReceipts);
			request.put("offset", offset);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		/*try {
			rootObject = new RunJSon(mContext, urlString, request.toString()).execute().get();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		try {
			rootObject = WSConnnection.getResult(urlString, request.toString(),mContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rootObject;
	}
}
