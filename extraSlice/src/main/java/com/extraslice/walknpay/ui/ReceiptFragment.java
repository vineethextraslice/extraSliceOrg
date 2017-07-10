package com.extraslice.walknpay.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONException;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.app.extraslice.R;
import com.extraslice.walknpay.adapter.ReceiptItemAdapter;
import com.extraslice.walknpay.bl.TransactionBO;
import com.extraslice.walknpay.bl.Utilities;
import com.extraslice.walknpay.dao.ProgressClass;
import com.extraslice.walknpay.model.CouponModel;
import com.extraslice.walknpay.model.PurchasedProductModel;
import com.extraslice.walknpay.model.TransactionModel;

public class ReceiptFragment extends Fragment {

	TextView storeName;
	TextView address1;
	TextView address2;
	TextView city;
	TextView state;
	TextView zip;
	TextView recieptId;
	TextView date;
	TextView subTotal, billTotal, totaltax,discount;
	TextView receiptId, offerSummary, totalItem, pay1Amt, pay1text, pay2Amt, pay2text, summarytab;
	//ListView headerView;
	ListView contentView,couponContentView;
	// ScrollView scrlView;
	Fragment fragment;
	int noOfItems = 0;
	TransactionModel recieptModel;
	ImageView imgback, imgfor;
	int prevId, nextId;
	View mainReceipts;
	Context context;
	LinearLayout couponLyt,discountLyt;
	ReceiptItemAdapter contentAdapter ;
	String currencySymbol="";
	public ReceiptFragment() {

	}

	public ReceiptFragment(TransactionModel recieptModel, int prevId, int nextId) {
		this.recieptModel = recieptModel;
		this.prevId = prevId;
		this.nextId = nextId;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.myreceipts, container, false);
		context = getActivity();
		storeName = (TextView) rootView.findViewById(R.id.storeName);
		storeName.setText(recieptModel.getRecieptStore().getName());
		address1 = (TextView) rootView.findViewById(R.id.addressline1);
		totalItem = (TextView) rootView.findViewById(R.id.noOfItems);
		date = (TextView) rootView.findViewById(R.id.receiptdate);
		billTotal = (TextView) rootView.findViewById(R.id.billTotal);
		subTotal = (TextView) rootView.findViewById(R.id.subTotal);
		totaltax = (TextView) rootView.findViewById(R.id.totaltax);
		offerSummary = (TextView) rootView.findViewById(R.id.offerSummary);
		//headerView = (ListView) rootView.findViewById(R.id.header);
		receiptId = (TextView) rootView.findViewById(R.id.receiptId);
		pay1Amt = (TextView) rootView.findViewById(R.id.pay1Amt);
		pay1text = (TextView) rootView.findViewById(R.id.pay1text);
		pay2text = (TextView) rootView.findViewById(R.id.pay2text);
		pay2Amt = (TextView) rootView.findViewById(R.id.pay2Amt);
		discount= (TextView) rootView.findViewById(R.id.discount);
		couponLyt = (LinearLayout) rootView.findViewById(R.id.couponLyt);
		discountLyt = (LinearLayout) rootView.findViewById(R.id.discountLyt);
		contentView = (ListView) rootView.findViewById(R.id.content);
		couponContentView = (ListView) rootView.findViewById(R.id.couponcontent);
		ImageView sendEmail = (ImageView) rootView.findViewById(R.id.sendMail);
		sendEmail.setClickable(true);
		currencySymbol = this.recieptModel.getRecieptStore().getCurrencySymbol()+" ";
		if(currencySymbol != null && currencySymbol.trim().equalsIgnoreCase("INR")){
			currencySymbol= "\u20B9"+" ";
		}
		sendEmail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new SendEmail().execute();
			}
		});
		// scrlView=(ScrollView)rootView.findViewById(R.id.table_scroll);
		receiptId.setText("Receipt Id : " + recieptModel.getOrderId() + "");
		if (recieptModel.getRecieptStore().getAddressLine1() != null && recieptModel.getRecieptStore().getAddressLine2() != null) {
			if (recieptModel.getRecieptStore().getAddressLine1().length() > 0) {
				address1.setText(recieptModel.getRecieptStore().getAddressLine1());
			}
			if (recieptModel.getRecieptStore().getAddressLine1().length() > 0 && recieptModel.getRecieptStore().getAddressLine2().length() > 0) {
				address1.setText(recieptModel.getRecieptStore().getAddressLine1() + "," + recieptModel.getRecieptStore().getAddressLine2());
			}

		}/*
		 * else if(recieptModel.getRecieptStore().getAddressLine1() != null ){
		 * address1.setText(recieptModel.getRecieptStore().getAddressLine1());
		 * }else if(recieptModel.getRecieptStore().getAddressLine2() != null){
		 * address1.setText(recieptModel.getRecieptStore().getAddressLine2()); }
		 */

		imgback = (ImageView) rootView.findViewById(R.id.imgback);
		imgfor = (ImageView) rootView.findViewById(R.id.imgfor);

		mainReceipts = rootView.findViewById(R.id.mainReceipts);

		

		

		String dateBig = recieptModel.getOrderDate();
		String dateSub = dateBig.substring(0, dateBig.indexOf(" "));
		date.setText("Date : " + dateSub);

		if (prevId == -1 && nextId == -1) {
			imgback.setVisibility(View.INVISIBLE);
			imgfor.setVisibility(View.INVISIBLE);
		} else if (MyReceipts.transactionModelList.size() == 1) {
			imgback.setVisibility(View.INVISIBLE);
			imgfor.setVisibility(View.INVISIBLE);
		} else if (prevId == -1 && nextId == 1) {

			imgback.setVisibility(View.INVISIBLE);
			// imgback.setImageResource(R.drawable.backwardarrowdis);
			imgback.setClickable(false);

		} else if (prevId == (MyReceipts.transactionModelList.size() - 2) && nextId == MyReceipts.transactionModelList.size()) {
			imgfor.setVisibility(View.INVISIBLE);
			// imgfor.setImageResource(R.drawable.forwardarrowdis);
			imgfor.setClickable(false);
		} else {
			imgback.setVisibility(View.VISIBLE);
			imgfor.setVisibility(View.VISIBLE);
		}
		
		String[] beanFields = { "code","name", "rate", "tax","qty", "price" };
		int[] to = { R.id.code,R.id.name, R.id.rate, R.id.tax,R.id.qty, R.id.price };
		Map<String, String> headerMap = new HashMap<String, String>(1);
		headerMap.put("code", "Code/Item");
		headerMap.put("rate", "Rate");
		headerMap.put("tax", "Tax");
		headerMap.put("qty", "Qty");
		headerMap.put("price", "Amount");
		
		/*List<Map<String, String>> headerList = new ArrayList<Map<String, String>>();
		headerList.add(headerMap);
		SimpleAdapter headerAdapter = new SimpleAdapter(rootView.getContext(), headerList, R.layout.reciept_items_header_config, beanFields, to);*/

		//headerView.setAdapter(headerAdapter);

		List<Map<String, String>> contentList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> couponContentList = new ArrayList<Map<String, String>>();
		if (recieptModel != null && recieptModel.getItemList() != null) {
			double tot = 0.0;
			double taxTotalAmt = 0;
			double subTotalAmt = 0;
			for (PurchasedProductModel model : recieptModel.getItemList()) {
				double tax = model.getTaxAmount();
				double totalForItem = model.getPrice() + tax;
				Map<String, String> contentMap = new HashMap<String, String>(1);
				contentMap.put("code", model.getCode());
				contentMap.put("name", model.getName());
				contentMap.put("rate",  new DecimalFormat("00.00").format(model.getPrice()));
				contentMap.put("tax", new DecimalFormat("00.000").format( model.getTaxAmount()));
				Double d = model.getPurchasedQuantity();
				int qty = d.intValue();
				contentMap.put("qty", String.valueOf(qty));
				noOfItems = noOfItems + (int) model.getPurchasedQuantity();
				contentMap.put("price", this.currencySymbol + new DecimalFormat("00.00").format(totalForItem * model.getPurchasedQuantity()));
				tot = tot + (Double.parseDouble(new DecimalFormat("00.00").format(totalForItem * model.getPurchasedQuantity())));
				taxTotalAmt = taxTotalAmt + (tax * model.getPurchasedQuantity());
				subTotalAmt = subTotalAmt + (model.getPrice() * model.getPurchasedQuantity());
				contentList.add(contentMap);
			}
			boolean hasPrepaid = false;
			double prepaidAmount = 0;
			int noOfPayMethods = 0;
			double offerAmount = 0;
			boolean hasCoupon = false;
			if (recieptModel.getCouponList() != null && recieptModel.getCouponList().size() > 0) {
				
				String[] couponFields = { "couponCode","description", "offeredAmount" };
				int[] couponFieldsTo = { R.id.couponCode,R.id.description, R.id.offeredAmount};
				for (CouponModel model : recieptModel.getCouponList()) {
					if (model.getCouponCode().equalsIgnoreCase("PREPAID")) {
						hasPrepaid = true;
						noOfPayMethods++;
						prepaidAmount = Double.parseDouble(new DecimalFormat("00.00").format(model.getApplicableAmount()));

					} else {
						hasCoupon = true;
						Map<String, String> contentMap = new HashMap<String, String>(1);
						contentMap.put("couponCode", model.getCouponCode());
						contentMap.put("description", model.getDescription());
						contentMap.put("offeredAmount", "-"+this.currencySymbol+ new DecimalFormat("00.00").format(model.getApplicableAmount()));
						tot = tot - (Double.parseDouble(new DecimalFormat("00.00").format(model.getApplicableAmount())));
						//subTotalAmt = subTotalAmt - (Double.parseDouble(new DecimalFormat("00.00").format(model.getApplicableAmount())));
						offerAmount = offerAmount + (Double.parseDouble(new DecimalFormat("00.00").format(model.getApplicableAmount())));
						couponContentList.add(contentMap);
					}

				}
				SimpleAdapter couponAdapter = new SimpleAdapter(rootView.getContext(), couponContentList, R.layout.coupon_items_config, couponFields, couponFieldsTo);
				couponContentView.setAdapter(couponAdapter);
			}
			if(hasCoupon){
				discountLyt.setVisibility(View.VISIBLE);
				couponLyt.setVisibility(View.VISIBLE);
			}else{
				discountLyt.setVisibility(View.GONE);
				couponLyt.setVisibility(View.GONE);
			}
			if (recieptModel.getPayMethod() != null && !recieptModel.getPayMethod().equalsIgnoreCase("PREPAID")) {
				if ((tot - prepaidAmount) > 0) {
					noOfPayMethods++;
				}
			}
			if (noOfPayMethods == 1) {
				pay2text.setVisibility(View.GONE);
				pay2Amt.setVisibility(View.GONE);
				if (hasPrepaid) {
					pay1text.setText("Prepaid");
					pay1Amt.setText(this.currencySymbol+new DecimalFormat("00.00").format(prepaidAmount));
				} else {
					pay1text.setText( recieptModel.getPayMethod());
					pay1Amt.setText(this.currencySymbol+new DecimalFormat("00.00").format(tot));
				}
			} else if (noOfPayMethods == 2) {
				if (hasPrepaid) {
					pay1text.setText("Prepaid");
					pay1Amt.setText(this.currencySymbol+new DecimalFormat("00.00").format(prepaidAmount));
				}
				if (recieptModel.getPayMethod() != null && recieptModel.getPayMethod().trim().length() > 0) {
					pay2text.setText(recieptModel.getPayMethod());
					pay2Amt.setText(this.currencySymbol+new DecimalFormat("00.00").format(tot - prepaidAmount));
				} else {
					if (!hasPrepaid) {
						noOfPayMethods = 0;
					}
				}

			}
			if (noOfPayMethods == 0) {
				pay1text.setVisibility(View.GONE);
				pay1Amt.setVisibility(View.GONE);
				pay2text.setVisibility(View.GONE);
				pay2Amt.setVisibility(View.GONE);
			}
			if(tot ==0){
				billTotal.setText(this.currencySymbol+"00.00" );
			}else{
				billTotal.setText(this.currencySymbol + new DecimalFormat("00.00").format(tot));
			}
			
			subTotal.setText(this.currencySymbol + new DecimalFormat("00.00").format(subTotalAmt));
			totaltax.setText(this.currencySymbol + new DecimalFormat("00.00").format(taxTotalAmt));
			if (offerAmount > 0) {
				offerSummary.setText("You have saved "+this.currencySymbol + new DecimalFormat("00.00").format(offerAmount));
				offerSummary.setVisibility(View.VISIBLE);
				discount.setText("-"+this.currencySymbol+new DecimalFormat("00.00").format(offerAmount));
			} else {
				discount.setText(this.currencySymbol+"00.00");
				offerSummary.setVisibility(View.GONE);
			}

			totalItem.setText(Integer.toString(noOfItems));

			/*
			 * totalamountdue.setText("$" + String.valueOf(new
			 * DecimalFormat("00.00").format(total)));
			 */
		}
		contentAdapter = new ReceiptItemAdapter(rootView.getContext(),  R.layout.reciept_items_config, contentList);
		
		contentView.setAdapter(contentAdapter);
		scrollMyListViewToBottom() ;
		
		imgback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (prevId >= 0) {

					imgback.setVisibility(View.VISIBLE);
					// imgback.setImageResource(R.drawable.backwardarrow);
					imgback.setClickable(true);

					TransactionModel model = MyReceipts.transactionModelList.get(prevId);

					int prvRcptPos = -1;
					if (prevId > 0) {
						prvRcptPos = prevId - 1;
					}

					Fragment fragment = null;
					fragment = new ReceiptFragment(model, prvRcptPos, prevId + 1);

					if (fragment != null) {
						FragmentManager fragmentManager = getFragmentManager();
						fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();

					}

				} else {

					imgback.setVisibility(View.INVISIBLE);
					// imgback.setImageResource(R.drawable.backwardarrowdis);
					imgback.setClickable(false);

				}

			}
		});

		imgfor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (nextId < Utilities.posReceipts && nextId >= 0) {

					imgfor.setVisibility(View.VISIBLE);
					imgfor.setClickable(true);
					TransactionModel model = MyReceipts.transactionModelList.get(nextId);
					int nextRcptPos = -1;

					if (nextId <= MyReceipts.transactionModelList.size() - 1) {
						nextRcptPos = nextId + 1;
					}

					Fragment fragment = null;
					fragment = new ReceiptFragment(model, nextId - 1, nextRcptPos);

					if (fragment != null) {
						FragmentManager fragmentManager = getFragmentManager();
						fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();

					}
				} else {
					imgfor.setVisibility(View.INVISIBLE);
					// imgfor.setImageResource(R.drawable.forwardarrowdis);
					imgfor.setClickable(false);
				}

			}
		});

		/*
		 * rootView.setFocusableInTouchMode(true); rootView.requestFocus();
		 * rootView.setOnKeyListener(new View.OnKeyListener() {
		 * 
		 * @Override public boolean onKey(View v, int keyCode, KeyEvent event) {
		 * 
		 * if( keyCode == KeyEvent.KEYCODE_BACK ) {
		 * 
		 * 
		 * 
		 * return true; } else { return false; } }
		 * 
		 * });
		 */

		mainReceipts.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
			public void onSwipeTop() {
				// Toast.makeText(getActivity(), "top",
				// Toast.LENGTH_SHORT).show();
			}

			public void onSwipeLeft() {
				if (nextId < Utilities.posReceipts && nextId >= 0) {

				///	imgfor.setVisibility(View.VISIBLE);
					//imgfor.setClickable(true);
					TransactionModel model = MyReceipts.transactionModelList.get(nextId);
					int nextRcptPos = -1;

					if (nextId <= MyReceipts.transactionModelList.size() - 1) {
						nextRcptPos = nextId + 1;
					}

					Fragment fragment = null;
					fragment = new ReceiptFragment(model, nextId - 1, nextRcptPos);

					if (fragment != null) {
						FragmentManager fragmentManager = getFragmentManager();
						fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();

					}
				} else {
				//	imgfor.setVisibility(View.INVISIBLE);
					// imgfor.setImageResource(R.drawable.forwardarrowdis);
				//	imgfor.setClickable(false);
				}

			}

			public void onSwipeRight() {
				// Toast.makeText(getActivity(), "left",
				// Toast.LENGTH_SHORT).show();

				// TODO Auto-generated method stub

				if (prevId >= 0) {

					imgback.setVisibility(View.VISIBLE);
				//	// imgback.setImageResource(R.drawable.backwardarrow);
					imgback.setClickable(true);

					TransactionModel model = MyReceipts.transactionModelList.get(prevId);

					int prvRcptPos = -1;
					if (prevId > 0) {
						prvRcptPos = prevId - 1;
					}

					Fragment fragment = null;
					fragment = new ReceiptFragment(model, prvRcptPos, prevId + 1);

					if (fragment != null) {
						FragmentManager fragmentManager = getFragmentManager();
						fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();

					}

				} else {

					imgback.setVisibility(View.INVISIBLE);
					// imgback.setImageResource(R.drawable.backwardarrowdis);
					imgback.setClickable(false);

				}

			}

			public void onSwipeBottom() {
				// Toast.makeText(getActivity(), "bottom",
				// Toast.LENGTH_SHORT).show();
			}

			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		});

		return rootView;

	}
	private void scrollMyListViewToBottom() {
		contentView.post(new Runnable() {
			@Override
			public void run() {
				// Select the last row so it will scroll into view...
				contentView.setSelection(contentAdapter.getCount() - 1);
			}
		});
	}
	class SendEmail extends AsyncTask<Void, Void, Void> {
		String status;
		@Override
		protected void onPreExecute() {	
			ProgressClass.startProgress(context);
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... arg0) {
			TransactionBO bo = new TransactionBO(context);
			try {
				if(recieptModel.getReceiptFor().equalsIgnoreCase("WALKNPAY")){
					status = bo.emailWnPReceipt(Utilities.loggedInUser.getUserId(), recieptModel.getOrderId());
				}else if(recieptModel.getReceiptFor().equalsIgnoreCase("EXTRASLICE")){
					status = bo.emailEsliceReceipt(com.app.extraslice.utils.Utilities.loggedInUser.getUserId(), recieptModel.getOrderId());
				}
				
				
			} catch (JSONException e) {
				status = null;
			}	
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (status != null && status.equals("Success")) {
				Toast.makeText(context, "Reciept sent to " + Utilities.loggedInUser.getEmail(), Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(context, "Failed to send the reciept", Toast.LENGTH_LONG).show();
			}
			ProgressClass.finishProgress();
			
		}
	}

}
