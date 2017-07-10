package com.extraslice.walknpay.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONObject;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.extraslice.R;
import com.app.extraslice.bo.CustAcctBO;
import com.app.extraslice.utils.Utilities;

import com.extraslice.walknpay.dao.ProgressClass;
import com.extraslice.walknpay.model.StripeCardModel;
import com.extraslice.walknpay.ui.AddCardFragment;
import com.extraslice.walknpay.ui.WalletFragment;
import com.stripe.Stripe;
import com.stripe.model.Card;
import com.stripe.model.Customer;
import com.stripe.model.Token;


public class WalletCardAdapter extends ArrayAdapter<StripeCardModel> {
	int layout;
	Dialog dialog;
	Context mContext;
	String cardNum = "";
	String cardId ="";
	public List<StripeCardModel> localList;
	FragmentManager fragmentManager;
	boolean subscriptionBySameUser;
	String custId;
	String apiKey;
	boolean eSlice =false;
	boolean haveSubscription,haveMemberSubscription,havePermission,isACH;
	public WalletCardAdapter(FragmentManager fragmentManager, Context context,
			int textViewResourceId, List<StripeCardModel> objects,boolean haveSubscription,boolean haveMemberSubscription,boolean havePermission,boolean subscriptionBySameUser,String custId,String apiKey,boolean isACH) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.layout = textViewResourceId;
		this.mContext = context;
		this.localList = objects;
		this.fragmentManager = fragmentManager;
		this.haveSubscription = haveSubscription;
		this.haveMemberSubscription = haveMemberSubscription;
		this.havePermission = havePermission;
		this.subscriptionBySameUser = subscriptionBySameUser;
		this.custId=custId;
		this.apiKey=apiKey;
		this.eSlice =true;
		this.isACH=isACH;
	}
	public WalletCardAdapter(FragmentManager fragmentManager, Context context,
			int textViewResourceId, List<StripeCardModel> objects,String custId,String apiKey) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.layout = textViewResourceId;
		this.mContext = context;
		this.localList = objects;
		this.fragmentManager = fragmentManager;
		this.haveSubscription = false;
		this.haveMemberSubscription = false;
		this.havePermission = false;
		this.eSlice =false;
		this.custId=custId;
		this.apiKey=apiKey;
	}
	public int getCount() {
		return localList.size();
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		final TRHolder trHolder;
		StripeCardModel model = localList.get(position);
		// if (convertView == null) {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(layout, null);
		trHolder = new TRHolder();
		trHolder.cardName = (TextView) convertView.findViewById(R.id.cardNumber);
		trHolder.cardHolder = (TextView) convertView.findViewById(R.id.cardHolderName);
		trHolder.imgDelete = (ImageView) convertView.findViewById(R.id.imgDel);
		
		trHolder.imgInfo = (ImageView) convertView.findViewById(R.id.imgInfo);
		trHolder.changeCardLyt = (RelativeLayout) convertView.findViewById(R.id.changeCardLyt);
		trHolder.changeCardText = (TextView) convertView.findViewById(R.id.changeCardText);
		
	    String redString = mContext.getResources().getString(R.string.changeDefCard);
	    if(haveMemberSubscription){
	    	redString = redString.replaceAll("XXX", "all");
	    }else{
	    	redString = redString.replaceAll("XXX", "conference");
	    }
	    trHolder.changeCardText.setText(Html.fromHtml(redString));
		
		
		trHolder.changeCardText.setTag(model);
		trHolder.imgDelete.setTag(model);
		trHolder.changeCardText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				StripeCardModel selModel = (StripeCardModel) v.getTag();
				List<StripeCardModel> othercardList = new ArrayList<StripeCardModel>();
				for(StripeCardModel mdl : localList){
					if(selModel.getCardId().equalsIgnoreCase(mdl.getCardId())){
						continue;
					}else{
						othercardList.add(mdl);
					}
					
				}
				Fragment fragment = new AddCardFragment(AddCardFragment.PURPOSE_CHANGE_DEF_CARD,othercardList,null,custId,apiKey,haveMemberSubscription,isACH);
				if (fragment != null) {
					fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
				}
				
			}
		});
		trHolder.imgInfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(trHolder.changeCardLyt.getVisibility() == View.GONE){
					trHolder.changeCardLyt.setVisibility(View.VISIBLE);
				}else{
					trHolder.changeCardLyt.setVisibility(View.GONE);
				}
				
			}
		});
		if(localList.size() == 1){
			trHolder.imgDelete.setVisibility(View.INVISIBLE);
		}/*else{
			if(!haveSubscription || !subscriptionBySameUser){
				trHolder.imgDelete.setVisibility(View.INVISIBLE);
				
			}else{
				if(!model.isDefaultCard()){
					trHolder.imgDelete.setVisibility(View.INVISIBLE);
				}
			}
		}*/
		if(this.eSlice){
			if(model.isDefaultCard()){
				trHolder.imgInfo.setVisibility(View.VISIBLE);
			}else{
				trHolder.imgInfo.setVisibility(View.INVISIBLE);
			}
		}else{
			trHolder.imgInfo.setVisibility(View.INVISIBLE);
		}
		
		
		
		try {
			cardNum = "XXXXXXXXXXXX"+model.getLast4();
			cardId =  model.getCardId();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		trHolder.cardName.setText(cardNum);
		trHolder.cardHolder.setText(model.getName());
		convertView.setTag(trHolder);

		trHolder.imgDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				dialog = new Dialog(mContext);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.wnp_custom_alert);
				final StripeCardModel selModel = (StripeCardModel) v.getTag();
				// set the custom dialog components - text, image and button
				TextView text = (TextView) dialog.findViewById(R.id.title_view);
				text.setText("Do you want to delete this ?");

				Button dialogButton = (Button) dialog.findViewById(R.id.yes_button);
				dialogButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						dialog.cancel();
						new ProcessCard(selModel.getCardId()).execute();
					}
				});
				Button negativebutton = (Button) dialog.findViewById(R.id.no_button);
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

	public class TRHolder {
;
		public TextView cardName, cardHolder,changeCardText;
		public ImageView imgDelete, imgInfo;
		public RelativeLayout changeCardLyt;
		String selecteditem = "";
		String cardId;

	}
	
	class ProcessCard extends AsyncTask<Void, Void, Void> {
		
		String cardId;
		public ProcessCard(String cardId) {
			this.cardId = cardId;
		}
		JSONObject responseJson = null;
		@Override
		protected void onPreExecute() {	
			ProgressClass.startProgress(mContext);
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... arg0) {
			try{
				Stripe.apiKey=apiKey;
				Customer cu = null;
				if(custId !=null){
					cu = Customer.retrieve(custId);
					if(cu != null && cardId != null && !cardId.trim().equals("")){
						cu.getSources().retrieve(cardId).delete();
					}
				}
			}catch(Exception e){
				Log.e("error", e.toString());
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			ProgressClass.finishProgress();
			Fragment fragment = new WalletFragment(1);
			if (fragment != null) {
				fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
			}
		}
	}
	
	
	
	
	
}
