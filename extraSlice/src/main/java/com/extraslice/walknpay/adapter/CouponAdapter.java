package com.extraslice.walknpay.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.app.extraslice.R;
import com.extraslice.walknpay.bl.Utilities;
import com.extraslice.walknpay.model.CouponModel;
import com.extraslice.walknpay.ui.MenuActivity;
import com.extraslice.walknpay.ui.PaymentOptionScreen;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SuppressLint("NewApi")
public class CouponAdapter extends ArrayAdapter<CouponModel> {
	int layout;
	Context mContext;
	
	public List<CouponModel> localList;
	FragmentManager fragmentManager;
	//int totalCount;
//	double totalAmount;
	public CouponAdapter(FragmentManager fragmentManager, Context context,int textViewResourceId, List<CouponModel> objects) {
		super(context, textViewResourceId, objects);
		this.layout = textViewResourceId;
		this.mContext = context;
		this.localList = objects;
		this.fragmentManager = fragmentManager;
		//this.totalCount =totalCount;
		//this.totalAmount=totalAmount;
	}

	public int getCount() {
		return localList.size();
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		CouponModel model = localList.get(position);
		final TRHolder trHolder;
		
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(layout, null);
		trHolder = new TRHolder();
		trHolder.couponCode = (TextView) convertView.findViewById(R.id.couponCode);

		trHolder.select = (CheckBox) convertView.findViewById(R.id.select);
		trHolder.status = (TextView) convertView.findViewById(R.id.status);
		trHolder.detailsCoupon = (TextView) convertView.findViewById(R.id.details);
		final View popup = ((Activity) mContext).findViewById(R.id.cardDetails);
		trHolder.detailsCoupon .setTag(model);
		
		
		trHolder.select.setTag(R.id.select,model.getRecalculatedOfferAmount()+"");
		trHolder.select.setTag(R.id.couponCode,model);
		
		trHolder.detailsCoupon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CouponModel selectedCoupon = null;
				try{
					selectedCoupon = (CouponModel)trHolder.detailsCoupon .getTag();
				}catch(Exception e){
					
				}
				if(selectedCoupon!=null){
					TextView couponName = (TextView) popup.findViewById(R.id.couponName);
					couponName.setText(selectedCoupon.getCouponCode());
					TextView couponDetl = (TextView) popup.findViewById(R.id.couponDetl);
					
					TextView couponType = (TextView) popup.findViewById(R.id.couponType);
					couponType.setText(selectedCoupon.getCouponType());
					TextView couponStart = (TextView) popup.findViewById(R.id.couponStart);
					TextView couponEnd = (TextView) popup.findViewById(R.id.couponEnd);
					TextView couponUsage = (TextView)popup.findViewById(R.id.couponUsage);
					if(selectedCoupon.getNoOfUsages() == 1){
						couponUsage.setText("Single");
					}else{
						couponUsage.setText("Multiple");
					}
					Timestamp startDate =selectedCoupon.getStartDate();
					Timestamp endDate = selectedCoupon.getEndDate();
					
					
					couponStart.setText(getDate(startDate.getTime()));
					couponEnd.setText(getDate(endDate.getTime()));
					TextView coupondesc = (TextView) popup.findViewById(R.id.couponDesc);
					coupondesc.setText(selectedCoupon.getDescription());
					String cpnDesc="";
					if(selectedCoupon.getCouponType().equalsIgnoreCase("Prepaid")){
						cpnDesc ="Coupon price : "+MenuActivity.currencySymbol+selectedCoupon.getCouponPrice()+", Offered amount : "+MenuActivity.currencySymbol+selectedCoupon.getOfferedAmount();
					}else{
						if(selectedCoupon.getOfferOnProductId() > 0){
							cpnDesc ="Buy ";
							if(selectedCoupon.getOfferOnCount() > 0){
								cpnDesc = cpnDesc+selectedCoupon.getOfferOnCount()+" ";
							}
							cpnDesc = cpnDesc+selectedCoupon.getOfferOnProductName()+" ";
							if(selectedCoupon.getOfferedProductId() > 0){
								cpnDesc = cpnDesc+"get ";
								if(selectedCoupon.getOfferedAmount() > 0){
									cpnDesc = cpnDesc+MenuActivity.currencySymbol+selectedCoupon.getOfferedAmount()+" off on "+selectedCoupon.getOfferedProductName();
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
									cpnDesc = cpnDesc+MenuActivity.currencySymbol+selectedCoupon.getOfferedAmount()+" off ";
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
									cpnDesc = cpnDesc+MenuActivity.currencySymbol+selectedCoupon.getOfferedAmount()+" off on "+selectedCoupon.getOfferedProductName();
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
									cpnDesc = cpnDesc+MenuActivity.currencySymbol+selectedCoupon.getOfferedAmount()+" off ";
								}
								if(selectedCoupon.getOfferedPerct() > 0){
									cpnDesc = cpnDesc+selectedCoupon.getOfferedPerct()+"% off ";
								}
							}
						}
					}
					
					couponDetl.setText(cpnDesc);
					popup.setVisibility(View.VISIBLE);
				}
			}
		});
		
		if(model.getApplyBy().equalsIgnoreCase("DEFAULT")){
			trHolder.select.setChecked(true);
			trHolder.select.setClickable(false);
			String text = model.getCouponCode()+"<font color='red'><sup>*</sup></font>";
			trHolder.couponCode.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
			PaymentOptionScreen.footNote.setVisibility(View.VISIBLE);
		}else{
			trHolder.couponCode.setText(model.getCouponCode());
			if(Utilities.selectedCoupons != null && Utilities.selectedCoupons.contains(model.getCouponCode())){
				trHolder.select.setChecked(true);
			}else{
				trHolder.select.setChecked(false);
			}
			
			//PaymentOptionScreen.footNote.setVisibility(View.GONE);
		}
		if(model.getRecalculatedOfferAmount() <= 0 ){
			trHolder.select.setClickable(false);
		}
		trHolder.status.setText(MenuActivity.currencySymbol+Utilities.roundto2Decimaldouble(model.getRecalculatedOfferAmount()));
		trHolder.select.setTag(model.getCouponCode());

		
	
		
		trHolder.select.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CheckBox cb = (CheckBox) v;
				double totalAmount = Double.parseDouble(Utilities.total1);
				CouponModel model = (CouponModel)v.getTag(R.id.couponCode);
				if(cb.isChecked()){
					model.calcualteOfferAmount(true,false);
					PaymentOptionScreen.defCouponList.add(model);
					PaymentOptionScreen.offCouponList.remove(model);
					Utilities.selectedCoupons.add(v.getTag().toString());
					Utilities.offerAmount = Utilities.offerAmount + model.getRecalculatedOfferAmount();
					PaymentOptionScreen.totalAmountForOffer  = PaymentOptionScreen.totalAmountForOffer  - model.getRecalculatedOfferAmount();
					for(CouponModel cpn : PaymentOptionScreen.offCouponList){
						if(!cpn.getCouponType().equalsIgnoreCase("Prepaid")){
							cpn.calcualteOfferAmount(false,false);
						}
					}
					PaymentOptionScreen.offerAmount.setText(MenuActivity.currencySymbol+Utilities.roundto2Decimaldouble((Utilities.offerAmount-PaymentOptionScreen.prepaidAmount)));
				}else{
					Utilities.offerAmount = Utilities.offerAmount  -  model.getRecalculatedOfferAmount();
					PaymentOptionScreen.totalAmountForOffer  = PaymentOptionScreen.totalAmountForOffer  + model.getRecalculatedOfferAmount();
					model.calcualteOfferAmount(false,true);
					PaymentOptionScreen.defCouponList.remove(model);
					PaymentOptionScreen.offCouponList.add(model);
					Utilities.selectedCoupons.remove(v.getTag().toString());
					
					for(CouponModel cpn : PaymentOptionScreen.defCouponList){
						if(!cpn.getCouponType().equalsIgnoreCase("Prepaid")){
							cpn.calcualteOfferAmount(false,true);
						}
						
						
						
					}
					Utilities.offerAmount = 0;
					PaymentOptionScreen.totalAmountForOffer=Double.parseDouble(Utilities.total1);
					for(CouponModel cpn : PaymentOptionScreen.defCouponList){
						if(!cpn.getCouponType().equalsIgnoreCase("Prepaid")){
							cpn.calcualteOfferAmount(true,false);
						}
						
						Utilities.offerAmount = Utilities.offerAmount +  cpn.getRecalculatedOfferAmount();
						PaymentOptionScreen.totalAmountForOffer = PaymentOptionScreen.totalAmountForOffer -  cpn.getRecalculatedOfferAmount();
					}
					for(CouponModel cpn : PaymentOptionScreen.offCouponList){
						if(!cpn.getCouponType().equalsIgnoreCase("Prepaid")){
							cpn.calcualteOfferAmount(false,false);
						}
					}
					PaymentOptionScreen.offerAmount.setText(MenuActivity.currencySymbol+Utilities.roundto2Decimaldouble((Utilities.offerAmount)));
					Utilities.offerAmount = Utilities.offerAmount+PaymentOptionScreen.prepaidAmount;
				}
				
				
				
				
				
				
				
				if(PaymentOptionScreen.prepaidChk.isChecked()){
					Utilities.offerAmount=Utilities.offerAmount - PaymentOptionScreen.prepaidAmount ;
					
					PaymentOptionScreen.prepaidTxt.setTextColor(Color.GRAY);
					if(PaymentOptionScreen.prepaidAvailableAmount > (totalAmount -Utilities.offerAmount)){
						PaymentOptionScreen.prepaidAmount =(totalAmount -Utilities.offerAmount);
					}else{
						PaymentOptionScreen.prepaidAmount = PaymentOptionScreen.prepaidAvailableAmount;
					}
					if(PaymentOptionScreen.prepaidAmount <=0){
						PaymentOptionScreen.prepaidChk.setChecked(false);
						Utilities.selectedCoupons.remove(PaymentOptionScreen.prepaidChk.getTag().toString());
					}
					Utilities.offerAmount=Utilities.offerAmount +PaymentOptionScreen.prepaidAmount ;
					PaymentOptionScreen.prepaidTxt.setText(" Prepaid (Remaining bal "+MenuActivity.currencySymbol + new DecimalFormat("00.00").format
							(PaymentOptionScreen.prepaidAvailableAmount-PaymentOptionScreen.prepaidAmount) + ")");
					PaymentOptionScreen.prepaidAppldLyt.setVisibility(View.VISIBLE);
					PaymentOptionScreen.prepaidAppld.setText(MenuActivity.currencySymbol+ Utilities.roundto2Decimaldouble(PaymentOptionScreen.prepaidAmount));
					if(totalAmount -Utilities.offerAmount < 0){
						PaymentOptionScreen.payableAmountTv.setText(MenuActivity.currencySymbol+0);
					}else{
						PaymentOptionScreen.payableAmountTv.setText(MenuActivity.currencySymbol+Utilities.roundto2Decimaldouble((totalAmount -Utilities.offerAmount)));
					}
					
				}
				int noOfCouppon = 0;
				if(PaymentOptionScreen.offCouponList != null){
					noOfCouppon =noOfCouppon + PaymentOptionScreen.offCouponList.size();
				}
				if(PaymentOptionScreen.defCouponList != null ){
					noOfCouppon = noOfCouppon + PaymentOptionScreen.defCouponList.size();
				}
				
				
				if(noOfCouppon <= 0){
					PaymentOptionScreen.offerAmountLyt.setVisibility(View.GONE);
				}else{
					PaymentOptionScreen.offerAmountLyt.setVisibility(View.VISIBLE);
				}
				if(totalAmount -Utilities.offerAmount <= 0){
					PaymentOptionScreen.payableAmountTv.setText(MenuActivity.currencySymbol+0);
						if(PaymentOptionScreen.prepaidAmount <= 0){
							PaymentOptionScreen.title_view.setVisibility(View.GONE);
							PaymentOptionScreen.seperator.setVisibility(View.GONE);
							PaymentOptionScreen.lyt2.setVisibility(View.GONE);
						}
						PaymentOptionScreen.lyt1.setVisibility(View.GONE);							
						
				}else{
					PaymentOptionScreen.payableAmountTv.setText(MenuActivity.currencySymbol+Utilities.roundto2Decimaldouble((totalAmount -Utilities.offerAmount)));
					PaymentOptionScreen.title_view.setText("Pay balance "+MenuActivity.currencySymbol+Utilities.roundto2Decimaldouble(totalAmount -Utilities.offerAmount)+" using");
					PaymentOptionScreen.title_view.setVisibility(View.VISIBLE);
					PaymentOptionScreen.seperator.setVisibility(View.VISIBLE);
					PaymentOptionScreen.lyt1.setVisibility(View.VISIBLE);	
					PaymentOptionScreen.lyt2.setVisibility(View.VISIBLE);
				}
				PaymentOptionScreen.payableAmount = totalAmount - Utilities.offerAmount;
				if(PaymentOptionScreen.payableAmount <= 0){
					PaymentOptionScreen.payableAmount =0;
				}
				
				PaymentOptionScreen.couponAdapter.notifyDataSetChanged();
				PaymentOptionScreen.couponAdapter = new CouponAdapter(fragmentManager, mContext, R.layout.coupon_item_list,localList);
				PaymentOptionScreen.couponListView.setAdapter(PaymentOptionScreen.couponAdapter);	
				if(PaymentOptionScreen.payableAmount <= 0){
					PaymentOptionScreen.title_view.setText("");
				}else{
					PaymentOptionScreen.title_view.setText("Pay balance "+MenuActivity.currencySymbol+Utilities.roundto2Decimaldouble((totalAmount - Utilities.offerAmount))+" using");
				}
			
				
			}
		});
		
		return convertView;
	}
	private String getDate(long timeStamp){

	    try{
	        DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
	        Date netDate = (new Date(timeStamp));
	        return sdf.format(netDate);
	    }
	    catch(Exception ex){
	        return "xx";
	    }
	} 
	public class TRHolder {
		public TextView couponCode, status,detailsCoupon;
		public CheckBox select;
	}
	
}
