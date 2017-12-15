package com.extraslice.walknpay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.extraslice.R;
import com.extraslice.walknpay.model.ReceiptItemModel;

import java.util.ArrayList;

public class ReceiptWalletAdapter extends ArrayAdapter<ReceiptItemModel> {
	int layout;
	ArrayList<ReceiptItemModel> listArray;
	Context c;
	public ReceiptWalletAdapter(Context context, int textViewResourceId, ArrayList<ReceiptItemModel> objects) {
		super(context, textViewResourceId, objects);
		this.layout = textViewResourceId;
		this.c = context;
		this.listArray = objects;
	}

	public int getCount() {
		return listArray.size();
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		final TRHolder trHolder;
		ReceiptItemModel model = listArray.get(position);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(layout, null);
			trHolder = new TRHolder();
			trHolder.proCode = (TextView) convertView.findViewById(R.id.itemcode);
			trHolder.proName = (TextView) convertView.findViewById(R.id.itemName);
			trHolder.proPrice = (TextView) convertView.findViewById(R.id.total);

		} else {
			trHolder = (TRHolder) convertView.getTag();
		}
		trHolder.proCode.setText(model.getCode());
		trHolder.proName.setText(model.getName());
		trHolder.proPrice.setText(model.getPrice());
		return convertView;
	}

	public class TRHolder {
		public TextView proCode, proName, proPrice;
	}
}
