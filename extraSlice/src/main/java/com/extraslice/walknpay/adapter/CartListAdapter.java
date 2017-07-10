package com.extraslice.walknpay.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.extraslice.R;
import com.extraslice.walknpay.bl.ProductBO;
import com.extraslice.walknpay.bl.Utilities;
import com.extraslice.walknpay.dao.ProgressClass;
import com.extraslice.walknpay.model.ProductModel;
import com.extraslice.walknpay.model.PurchasedProductModel;
import com.extraslice.walknpay.ui.CartFragment;
import com.extraslice.walknpay.ui.MenuActivity;

public class CartListAdapter extends ArrayAdapter<PurchasedProductModel>{
	static Context localcontext;
	public static ArrayList<PurchasedProductModel> localList;
	int layout;

	int count1;


	public CartListAdapter(Context context, int textViewResourceId, ArrayList<PurchasedProductModel> theTRList, int count) {
		super(context, textViewResourceId, theTRList);
		CartListAdapter.localList = theTRList;
		this.localcontext = context;
		this.layout = textViewResourceId;
		this.count1 = count;
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

		PurchasedProductModel products = localList.get(position);
		final TRHolder trHolder;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) localcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(layout, null);
			trHolder = new TRHolder();
			trHolder.tvPDescription = (TextView) convertView.findViewById(R.id.lbltvproductname);
			trHolder.tvPCount = (MyEditText) convertView.findViewById(R.id.lbltvproductcount);
			trHolder.tvPPrice = (TextView) convertView.findViewById(R.id.lbltvrowprice);
			trHolder.imgclear = (ImageView) convertView.findViewById(R.id.imgclear);
			convertView.setTag(trHolder);
			trHolder.tvPCount.setTag(localList.get(position));
			trHolder.tvPCount.setTag(R.id.lbltvproductcount, trHolder);
		} else {
			trHolder = (TRHolder) convertView.getTag();
		}
		
		trHolder.tvPDescription.setText(products.getName());
		trHolder.tvPDescription.setTag(products);
		trHolder.tvPPrice.setText(MenuActivity.currencySymbol+"" + String.valueOf(new DecimalFormat("##.##").format(products.getPrice() * products.getPurchasedQuantity())));
		trHolder.tvPCount.setText(String.valueOf((int) products.getPurchasedQuantity()));
		trHolder.tvPCount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				
				if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT
						||  actionId == EditorInfo.IME_FLAG_NAVIGATE_NEXT || actionId == EditorInfo.IME_ACTION_GO) {
					TRHolder trHolder = (TRHolder) v.getTag(R.id.lbltvproductcount);
					Utilities.hideVirtualKeyBoard(localcontext, trHolder.tvPCount);
					verifyAvaialableAndUpdateQty(localcontext,trHolder);
					refresh();
					return true;
				}
				return false;

			}
		});
		
		refresh();
		trHolder.imgclear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				final Dialog dialog = new Dialog(localcontext);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.wnp_custom_alert);

				// set the custom dialog components - text, image and button
				TextView text = (TextView) dialog.findViewById(R.id.title_view);
				text.setText("Do you want to Delete?");

				Button dialogButton = (Button) dialog.findViewById(R.id.yes_button);
				// if button is clicked, close the custom dialog
				dialogButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (localList != null && localList.size() > position) {
							localList.remove(position);
							refresh();
						}
						dialog.dismiss();
						Utilities.setTotalCount();
					}
				});
				Button negativebutton = (Button) dialog.findViewById(R.id.no_button);
				// if button is clicked, close the custom dialog
				negativebutton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});

				dialog.show();

			}
		});
		return convertView;
	}

	public static void verifyAvaialableAndUpdateQty(Context localcontext,TRHolder trHolder) {
		
		String qtyUserupdate = trHolder.tvPCount.getText().toString();
		PurchasedProductModel product = (PurchasedProductModel) trHolder.tvPDescription.getTag();
		String productCode = product.getCode();
		if (qtyUserupdate == null || qtyUserupdate.trim().equals("") || Integer.parseInt(qtyUserupdate.trim())==0) {
			qtyUserupdate="1";
			trHolder.tvPCount.setText("1");
		}
			if(product.isOnDemandItem()){
				trHolder.tvPCount.setText(qtyUserupdate);
				product.setPurchasedQuantity(Integer.parseInt(qtyUserupdate));
				double ProdPrice = product.getPrice() * (Integer.parseInt(qtyUserupdate));
				trHolder.tvPPrice.setText(""+MenuActivity.currencySymbol+" " + String.valueOf(Utilities.roundto2Decimal(ProdPrice)));
				trHolder.tvPCount.setTag(product);
				product.setAvailableQty(Integer.parseInt(qtyUserupdate));
				CartFragment.calculate();
			}else{
				new ProductBG(productCode,qtyUserupdate,product,trHolder).execute();
			}
			
			Utilities.setTotalCount();
			
		//}
	}

	public class TRHolder {
		public TextView tvPDescription, tvPPrice;
		public MyEditText tvPCount;
		public ImageView imgplus, imgclear, imgminus;
	}
	static class ProductBG extends AsyncTask<Void, Void, Void> {
		String contents;
		ProductModel products = null;
		String qtyUserupdate;
		TRHolder trHolder;
		PurchasedProductModel purchasedModel;
		public ProductBG(String contents,String qtyUserupdate,PurchasedProductModel purchasedModel,TRHolder trHolder) {
			this.contents=contents;
			this.qtyUserupdate = qtyUserupdate;
			this.purchasedModel = purchasedModel;
			this.trHolder = trHolder;
		}
		@Override
		protected void onPreExecute() {	
			ProgressClass.startProgress(localcontext);
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... arg0) {

			ProductBO bo = new ProductBO(localcontext);
			products = bo.getProductForStoreByCode(contents, Utilities.selectedStore.getStoreId(), Utilities.STATUS_FILTER_ACTIVE);
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (products == null) {
				Toast.makeText(localcontext, "Not recently updated qty", Toast.LENGTH_SHORT).show();

			}else{
				int storeAvailableQtyint = 0, qtyUserupdateInt = 0;
				double storeAvailableQty = products.getAvailableQty();
				try {
					storeAvailableQtyint = (int) storeAvailableQty;
					qtyUserupdateInt = Integer.valueOf(qtyUserupdate);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				if (qtyUserupdateInt > storeAvailableQtyint) {
					trHolder.tvPCount.setText(storeAvailableQtyint+"");
					Toast.makeText(localcontext, "We're sorry! We are able to accommodate only " + storeAvailableQtyint + " units", Toast.LENGTH_LONG).show();
					LayoutInflater inflater2 = (LayoutInflater) localcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					trHolder.tvPCount.setText(String.valueOf(storeAvailableQtyint));
					purchasedModel = (PurchasedProductModel) trHolder.tvPDescription.getTag();
					purchasedModel.setPurchasedQuantity(storeAvailableQtyint);
					double ProdPrice = purchasedModel.getPrice() * (storeAvailableQtyint);
					trHolder.tvPPrice.setText(""+MenuActivity.currencySymbol+" " + String.valueOf(Utilities.roundto2Decimal(ProdPrice)));
					trHolder.tvPCount.setTag(purchasedModel);

				} else if (qtyUserupdateInt <= storeAvailableQtyint) {
					trHolder.tvPCount.setText(qtyUserupdateInt+"");
					if (qtyUserupdateInt != 0) {
						purchasedModel = (PurchasedProductModel) trHolder.tvPDescription.getTag();
						purchasedModel.setPurchasedQuantity(qtyUserupdateInt);
						double ProdPrice = purchasedModel.getPrice() * (purchasedModel.getPurchasedQuantity());
						trHolder.tvPPrice.setText(""+MenuActivity.currencySymbol+" " + String.valueOf(Utilities.roundto2Decimal(ProdPrice)));
						trHolder.tvPCount.setTag(purchasedModel);

					}
				}

				CartFragment.calculate();
			}
			Utilities.setTotalCount();
			ProgressClass.finishProgress();
		}
	}
	
}