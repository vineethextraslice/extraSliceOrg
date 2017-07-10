package com.extraslice.walknpay.adapter;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.extraslice.R;
import com.extraslice.walknpay.bl.Utilities;
import com.extraslice.walknpay.model.CouponModel;
import com.extraslice.walknpay.model.PurchasedProductModel;
import com.extraslice.walknpay.model.TransactionModel;

public class WalletReceiptAdapter extends ArrayAdapter<TransactionModel> {

	int layout;
	List<TransactionModel> listArray;
	Context c;
	
	public WalletReceiptAdapter(Context context, int textViewResourceId, List<TransactionModel> objects) {
		super(context, textViewResourceId, objects);
		this.listArray = objects;
		this.layout = textViewResourceId;
		this.c = context;
	}

	public int getCount() {
		return listArray.size();
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		final TRHolder trHolder;
		TransactionModel model = listArray.get(position);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(layout, null);
			trHolder = new TRHolder();
			trHolder.storeName = (TextView) convertView.findViewById(R.id.storeName);
			trHolder.totalAmt = (TextView) convertView.findViewById(R.id.totalAmt);
			trHolder.id = (TextView) convertView.findViewById(R.id.orderId);
			trHolder.date = (TextView) convertView.findViewById(R.id.date);
			convertView.setTag(trHolder);

		} else {
			trHolder = (TRHolder) convertView.getTag();
		}
		trHolder.storeName.setText(model.getRecieptStore().getName());
		double offerAmt =0;
		double total =0.0;
		if (model != null && model.getItemList() != null) {
			for (PurchasedProductModel prcMdl : model.getItemList()) {
				double tax = prcMdl.getTaxAmount();
				double totalForItem = prcMdl.getPrice() + tax;
				total = total + (Double.parseDouble(new DecimalFormat("00.00").format(totalForItem * prcMdl.getPurchasedQuantity())));
			}
			if (model.getCouponList() != null && model.getCouponList().size() > 0) {
				for (CouponModel cpn : model.getCouponList()) {
					if (!cpn.getCouponCode().equalsIgnoreCase("PREPAID")) {
						offerAmt = offerAmt + Double.parseDouble(new DecimalFormat("00.00").format(cpn.getApplicableAmount()));
						
					}
				}
			}
		}
		
		trHolder.totalAmt.setText(Utilities.roundto2Decimal(total- offerAmt));
		trHolder.id.setText(model.getOrderId() + "");
		SimpleDateFormat frmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat frmt2 = new SimpleDateFormat("MM/dd/yy");
		try {
			trHolder.date.setText(frmt2.format(frmt.parse(model.getOrderDate())));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}

	public class TRHolder {
		public TextView storeName, totalAmt, id,date;
	}
}
