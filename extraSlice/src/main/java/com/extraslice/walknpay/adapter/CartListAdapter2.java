package com.extraslice.walknpay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.extraslice.R;
import com.extraslice.walknpay.model.ProductModel;

import java.util.List;

public class CartListAdapter2 extends ArrayAdapter<ProductModel> {
	Context localcontext;
	List<ProductModel> localList;
	int layout;
	
	int count1;


	public CartListAdapter2(Context context, int textViewResourceId,List<ProductModel> localList) {
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

		ProductModel products = localList.get(position);
		
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) localcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(layout, null);
			
		} 
		ImageView addtoCart = (ImageView)convertView.findViewById(R.id.addToCart);
		
		TextView rating = (TextView)convertView.findViewById(R.id.rating);
	//	TextView stars = (TextView)convertView.findViewById(R.id.stars);
		TextView discount = (TextView)convertView.findViewById(R.id.offer);
		TextView itemprice = (TextView)convertView.findViewById(R.id.itemprice);
		TextView itemDesc = (TextView)convertView.findViewById(R.id.itemDesc);
		ImageView itemImage = (ImageView)convertView.findViewById(R.id.itemImage);
		

		
		//stars.setText("5 Stars");
		itemprice.setText("Price : \u20B9 "+products.getPrice());
		if(position%3 == 0){
			discount.setText("Offer : Buy 1 get 1 free");
		}else{
			discount.setText("");
		}
		//if(position%2 == 0){
			int d = (int)((position +1)*0.22*100);
			rating.setText(d+" Ratings");
		//}else{
			//rating.setText("Ratings");
		//}
		itemImage.setImageResource(getContext().getResources().getIdentifier("book"+(position+1), "drawable","com.extraslice.walknpay"));
		itemDesc.setText(products.getName());
		return convertView;
	}

	
}