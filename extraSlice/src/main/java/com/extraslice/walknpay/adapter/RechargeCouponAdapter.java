package com.extraslice.walknpay.adapter;

import android.app.Dialog;
import android.content.Context;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.extraslice.R;
import com.extraslice.walknpay.model.CouponModel;
import com.extraslice.walknpay.ui.MenuActivity;
import com.extraslice.walknpay.ui.MyCards;
import com.extraslice.walknpay.ui.PaymentOptionScreen;

import java.text.SimpleDateFormat;
import java.util.List;


public class RechargeCouponAdapter extends ArrayAdapter<CouponModel> {
	public List<CouponModel> couponList;
	int layout;
	Context mContext;
	public TextView couponCode,more,couponPrice;
	 CheckBox selCoupon;
	Dialog couponDetl;
	TextView couponDesc,couponEnd,couponStart,couponType,couponDetlTv,couponName,couponUsage;
	LinearLayout closepopup;		
	boolean isFromPayScreen;
	public RechargeCouponAdapter(Context context, int textViewResourceId, List<CouponModel> CouponList,boolean isFromPayScreen) {
		super(context, textViewResourceId, CouponList);
		this.isFromPayScreen=isFromPayScreen;
		this.mContext = context;
		this.couponList = CouponList;
		this.layout = textViewResourceId;
		couponDetl = new Dialog(context);
		couponDetl.requestWindowFeature(Window.FEATURE_NO_TITLE);
		couponDetl.setContentView(R.layout.coupon_details);
		couponDesc = (TextView)couponDetl.findViewById(R.id.couponDesc);
		couponEnd = (TextView)couponDetl.findViewById(R.id.couponEnd);
		couponStart = (TextView)couponDetl.findViewById(R.id.couponStart);
		couponType = (TextView)couponDetl.findViewById(R.id.couponType);
		couponDetlTv = (TextView)couponDetl.findViewById(R.id.couponDetl);
		couponUsage = (TextView)couponDetl.findViewById(R.id.couponUsage);
		couponName = (TextView)couponDetl.findViewById(R.id.couponName);
		closepopup = (LinearLayout)couponDetl.findViewById(R.id.closepopup);
		
		closepopup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				couponDetl.dismiss();
				
			}
		});
	}

	public void refresh() {

	}

	public int getCount() {
		return couponList == null ? 0 :couponList.size();
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(layout, null);
		}
		
		
		
		
		if(couponList != null){
			CouponModel selectedCoupon = couponList.get(position);
			
			couponCode = (TextView) convertView.findViewById(R.id.couponCode);
			couponCode.setText(selectedCoupon.getCouponCode());
			couponPrice = (TextView) convertView.findViewById(R.id.couponPrice);
			couponPrice.setText(""+MenuActivity.currencySymbol+selectedCoupon.getCouponPrice());
			if(selectedCoupon.getNoOfUsages() == 1){
				couponUsage.setText("Single");
			}else{
				couponUsage.setText("Multiple");
			}
			more = (TextView) convertView.findViewById(R.id.more);
			SpannableString moreText = new SpannableString("More");	
			moreText.setSpan(new UnderlineSpan(), 0, moreText.length(), 0);
			more.setText(moreText);
			selCoupon = (CheckBox) convertView.findViewById(R.id.selCoupon);
			if(selCoupon.isChecked())
			{
				selCoupon.setChecked(false);
			}
			selCoupon.setTag(selectedCoupon);
			if(isFromPayScreen){
				if(PaymentOptionScreen.couponId.contains(selectedCoupon.getCouponId()+"")){
					selCoupon.setChecked(true);
				}else{
					selCoupon.setChecked(false);
				}
			}else{
				if(MyCards.couponId.contains(selectedCoupon.getCouponId()+"")){
					selCoupon.setChecked(true);
				}else{
					selCoupon.setChecked(false);
				}
			}
			more.setTag(selectedCoupon);
			more.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					CouponModel selectedCoupon = null;
					try{
						selectedCoupon = (CouponModel)arg0.getTag();
					}catch(Exception e){
						
					}
					if(selectedCoupon != null){
						SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
						String startDat = "";
						String endDat = "";
						try{
							startDat = dateFormat.format(selectedCoupon.getStartDate());
						}catch(Exception e){
							
						}
						try{
							endDat = dateFormat.format(selectedCoupon.getEndDate());
						}catch(Exception e){
							
						}
						couponDesc.setText(selectedCoupon.getDescription());
						couponEnd.setText(endDat);
						couponStart.setText(startDat);
						couponType.setText(selectedCoupon.getCouponType());
						
						couponName.setText(selectedCoupon.getCouponCode());
						String cpnDesc="";
						if(selectedCoupon.getCouponType().equalsIgnoreCase("Prepaid")){
							cpnDesc ="Coupon price : "+MenuActivity.currencySymbol+selectedCoupon.getCouponPrice()+", Offered amount : "+MenuActivity.currencySymbol+selectedCoupon.getOfferedAmount();
						}else{
							if(selectedCoupon.getOfferOnProductId() > 0){
								cpnDesc ="Buy ";
								if(selectedCoupon.getOfferOnCount() > 0){
									int no = (int)selectedCoupon.getOfferOnCount();
									cpnDesc = cpnDesc+no+" ";
								}
								cpnDesc = cpnDesc+selectedCoupon.getOfferOnProductName()+" ";
								if(selectedCoupon.getOfferedProductId() > 0){
									cpnDesc = cpnDesc+"get ";
									if(selectedCoupon.getOfferedAmount() > 0){
										cpnDesc = cpnDesc+""+MenuActivity.currencySymbol+selectedCoupon.getOfferedAmount()+" off on "+selectedCoupon.getOfferedProductName();
									}
									if(selectedCoupon.getOfferedPerct() > 0){
										cpnDesc = cpnDesc+selectedCoupon.getOfferedPerct()+"% off on "+selectedCoupon.getOfferedProductName();
									}
									if(selectedCoupon.getOfferedCount() > 0){
										cpnDesc = cpnDesc+selectedCoupon.getOfferedCount()+" "+selectedCoupon.getOfferedProductName()+" free";
									}
								}else{
									cpnDesc = cpnDesc+"get ";
									if(selectedCoupon.getOfferedAmount() > 0){
										cpnDesc = cpnDesc+""+MenuActivity.currencySymbol+selectedCoupon.getOfferedAmount()+" off ";
									}
									if(selectedCoupon.getOfferedPerct() > 0){
										cpnDesc = cpnDesc+selectedCoupon.getOfferedPerct()+"% off ";
									}
									if(selectedCoupon.getOfferedCount() > 0){
										cpnDesc = cpnDesc+selectedCoupon.getOfferedCount()+" "+selectedCoupon.getOfferOnProductName()+" free";
									}
								}
							}else{
								
								if(selectedCoupon.getOfferAbovePrice() > 0){
									cpnDesc ="For every purchase above "+selectedCoupon.getOfferAbovePrice() ;
								}else{
									cpnDesc ="For every purchase ";
								}
								
								if(selectedCoupon.getOfferedProductId() > 0){
									cpnDesc = cpnDesc+"get ";
									if(selectedCoupon.getOfferedAmount() > 0){
										cpnDesc = cpnDesc+""+MenuActivity.currencySymbol+selectedCoupon.getOfferedAmount()+" off on "+selectedCoupon.getOfferedProductName();
									}
									if(selectedCoupon.getOfferedPerct() > 0){
										cpnDesc = cpnDesc+selectedCoupon.getOfferedPerct()+"% off on "+selectedCoupon.getOfferedProductName();
									}
									if(selectedCoupon.getOfferedCount() > 0){
										cpnDesc = cpnDesc+selectedCoupon.getOfferedCount()+" "+selectedCoupon.getOfferedProductName()+" free";
									}
								}else{
									cpnDesc = cpnDesc+"get ";
									if(selectedCoupon.getOfferedAmount() > 0){
										cpnDesc = cpnDesc+""+MenuActivity.currencySymbol+selectedCoupon.getOfferedAmount()+" off ";
									}
									if(selectedCoupon.getOfferedPerct() > 0){
										cpnDesc = cpnDesc+selectedCoupon.getOfferedPerct()+"% off ";
									}
								}
							}
						}
						
						couponDetlTv.setText(cpnDesc);
					}
					couponDetl.show();
				}
			});
			
			
			
			
			selCoupon.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					CheckBox cb = (CheckBox)v;
					

					CouponModel selectedCoupon = null;
					try{
						selectedCoupon = (CouponModel)v.getTag();
					}catch(Exception e){
						
					}
					if(selectedCoupon != null){
						if(isFromPayScreen){
							if(cb.isChecked()){
								PaymentOptionScreen.rechargePayableAmount = PaymentOptionScreen.rechargePayableAmount + selectedCoupon.getCouponPrice();
								PaymentOptionScreen.usableAmount = PaymentOptionScreen.usableAmount + selectedCoupon.getOfferedAmount();
								PaymentOptionScreen.couponId.add(selectedCoupon.getCouponId()+"");
							}else{
								PaymentOptionScreen.rechargePayableAmount = PaymentOptionScreen.rechargePayableAmount - selectedCoupon.getCouponPrice();
								PaymentOptionScreen.usableAmount = PaymentOptionScreen.usableAmount - selectedCoupon.getOfferedAmount();
								PaymentOptionScreen.couponId.remove(selectedCoupon.getCouponId()+"");
							}
						}else{
							if(cb.isChecked()){
								MyCards.payableAmount = MyCards.payableAmount + selectedCoupon.getCouponPrice();
								MyCards.usableAmount = MyCards.usableAmount + selectedCoupon.getOfferedAmount();
								MyCards.couponId.add(selectedCoupon.getCouponId()+"");
							}else{
								MyCards.payableAmount = MyCards.payableAmount - selectedCoupon.getCouponPrice();
								MyCards.usableAmount = MyCards.usableAmount - selectedCoupon.getOfferedAmount();
								MyCards.couponId.remove(selectedCoupon.getCouponId()+"");
							}
						}
						
					}
					if(isFromPayScreen){
						PaymentOptionScreen.payableAmtTV.setText("Payable amount : "+MenuActivity.currencySymbol+PaymentOptionScreen.rechargePayableAmount);
						PaymentOptionScreen.usableAmtTV.setText("Usable amount :"+MenuActivity.currencySymbol+PaymentOptionScreen.usableAmount);
						PaymentOptionScreen.deatilsLyt.setVisibility(View.VISIBLE);
					}else{
						MyCards.payableAmtTV.setText("Payable amount : "+MenuActivity.currencySymbol+MyCards.payableAmount);
						MyCards.usableAmtTV.setText("Usable amount : "+MenuActivity.currencySymbol+MyCards.usableAmount);
						MyCards.deatilsLyt.setVisibility(View.VISIBLE);
					}
					
				
				}
			});
			
		}
		
		return convertView;
	}

}