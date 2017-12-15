package com.extraslice.walknpay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.extraslice.R;
import com.extraslice.walknpay.model.ProductModel;

import java.util.List;
import java.util.Map;

public class ReceiptItemAdapter extends ArrayAdapter<Map<String,String>> {
	Context localcontext;
	List<Map<String,String>> localList;
	int layout;
	ProductModel products1;

	public ReceiptItemAdapter(Context context, int textViewResourceId, List<Map<String,String>> itemMapList) {
		super(context, textViewResourceId, itemMapList);
		this.localList = itemMapList;
		this.localcontext = context;
		this.layout = textViewResourceId;
	}

	public void refresh() {

	}

	public int getCount() {
		return localList.size();
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {

		Map<String,String> products = localList.get(position);

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) localcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(layout, null);
	
			TextView code = (TextView) convertView.findViewById(R.id.code);
			TextView name = (TextView) convertView.findViewById(R.id.name);
			TextView rate = (TextView) convertView.findViewById(R.id.rate);
			TextView tax = (TextView) convertView.findViewById(R.id.tax);
			TextView qty = (TextView) convertView.findViewById(R.id.qty);
			TextView price = (TextView) convertView.findViewById(R.id.price);
			if(products.get("name") == null || products.get("name").trim().equals("")){
				code.setText(products.get("code"));
				name.setText("");
			}else if(products.get("code") == null || products.get("code").trim().equals("")){
				code.setText(products.get("name"));
				name.setText("");
			}else{
				code.setText(products.get("code"));
				name.setText(products.get("name"));
			}
			
			rate.setText(products.get("rate"));
			tax.setText(products.get("tax"));
			qty.setText(products.get("qty"));
			price.setText(products.get("price"));
		
		} 

		
		return convertView;
	}

	

	

}