package com.extraslice.walknpay.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.extraslice.R;
import com.extraslice.walknpay.model.ProductModel;

public class CartListAdapter3 extends ArrayAdapter<ProductModel> {
	Context localcontext;
	List<ProductModel> localList;
	int layout;
	
	int count1;

public static int ccc=0;
	public CartListAdapter3(Context context, int textViewResourceId,List<ProductModel> localList) {
		super(context, textViewResourceId, localList);
		this.localList = localList;
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

		
		LayoutInflater inflater = (LayoutInflater) localcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//if (convertView == null) {
			
			convertView = inflater.inflate(layout, null);
			
		//} 
		LinearLayout cntntLyt=(LinearLayout)convertView.findViewById(R.id.cntntLyt);
		
		for(int i=0;i<=3;i++){
			View contntView = inflater.inflate(R.layout.product_rows3, null);
			ProductModel products = localList.get(++ccc);

			TextView  checkoutbuttontext = (TextView)contntView.findViewById(R.id.checkoutbuttontext);
			ImageView  checkoutbutton = (ImageView)contntView.findViewById(R.id.checkoutbutton);
			checkoutbutton.setImageResource(getContext().getResources().getIdentifier("book"+(ccc), "drawable","com.extraslice.walknpay"));
			checkoutbuttontext.setText(" \u20B9 "+products.getPrice());
			
			if(ccc == 6){
				ccc=0;
			}
			cntntLyt.addView(contntView);
		}

		return convertView;
	}

	
}