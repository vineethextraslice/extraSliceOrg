package com.extraslice.walknpay.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.extraslice.R;

import java.util.List;
import java.util.Map;

public class CustomAdapter extends ArrayAdapter<Map<String, Integer>> {

	private Context context;
	List<Map<String, Integer>> storeNameIdMap;

	public CustomAdapter(Context context, int resource, List<Map<String, Integer>> deer) {
		super(context, resource, deer);
		this.context = context;
		this.storeNameIdMap = deer;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = super.getView(position, convertView, parent);
		Map<String, Integer> current = storeNameIdMap.get(position);
		String key = current.keySet().iterator().next();
		TextView name = (TextView) v;
		name.setText(key);
		name.setTag(current.get(key));
		name.setTextColor(Color.BLACK);
		name.setBackgroundColor(Color.parseColor("#BBFFFFFF"));
		return v;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.wnp_spinner_row, parent, false);
		}
		Map<String, Integer> current = storeNameIdMap.get(position);
		String key = current.keySet().iterator().next();
		TextView name = (TextView) row.findViewById(R.id.spinnerText);
		name.setText(key);
		name.setTag(current.get(key));
		name.setTextColor(Color.BLACK);
		name.setBackgroundColor(Color.parseColor("#BBFFFFFF"));
		return row;
	}
}