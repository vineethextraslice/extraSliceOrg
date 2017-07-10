package com.extraslice.walknpay.ui;

import com.app.extraslice.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class MyAccount extends Fragment{
	Spinner spinnerRecharge;
 String[] Months = new String[] { "Select Your Recharge Option", "Flexi recharge",
	      "Top up", "Offer" };
 TextView text;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.myaccount, container, false);
		
		spinnerRecharge = (Spinner) rootView.findViewById(R.id.spinner1);
		text = (TextView) rootView.findViewById(R.id.textView2);
		spinnerRecharge.setEnabled(true);
		ArrayAdapter adapterMonth = new ArrayAdapter<String>(getActivity(), R.layout.wnp_spinner_item, Months);
	//	adapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		  
		    spinnerRecharge.setAdapter(adapterMonth);	
		    
		    text.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					spinnerRecharge.setVisibility(View.VISIBLE);
				}
			});
		    
		    return rootView;
	}

}
