package com.app.extraslice.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.app.extraslice.R;
import com.stripe.model.Customer;



public class CustomAdapter extends ArrayAdapter<Map<String, Object>> {
	private ArrayList<Map<String, Object>> suggestions;
	private Context context;
	List<Map<String, Object>> objectMpList;
	List<Map<String, Object>> objectMpListAll;
	int textViewGravity;
	int textViewBGColor =-1;;
	public CustomAdapter(Context context,  List<Map<String, Object>> objectMpList) {
		super(context, R.layout.spinner_row, objectMpList);
		this.context = context;
		this.objectMpList =objectMpList;
		this.objectMpListAll =  new ArrayList<Map<String,Object>>();
		this.objectMpListAll.addAll(objectMpList);
		textViewGravity = Gravity.LEFT|Gravity.CENTER_VERTICAL;
		suggestions = new ArrayList<Map<String,Object>>();
		
	}
	public CustomAdapter(Context context, List<Map<String, Object>> objectMpList,int textViewGravity,int textViewBGColor) {
		super(context, R.layout.spinner_row, objectMpList);
		this.context = context;
		this.textViewGravity = textViewGravity;
		this.textViewBGColor = textViewBGColor;
		this.objectMpList =objectMpList;
		this.objectMpListAll =  new ArrayList<Map<String,Object>>();
		this.objectMpListAll.addAll(objectMpList);
		suggestions = new ArrayList<Map<String,Object>>();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		//View row = super.getView(position, convertView, parent);
		
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.spinner_row, parent, false);
		}
		Map<String, Object> current = objectMpList.get(position);
		String key = current.keySet().iterator().next();
		//CardView crdView = (CardView) row.findViewById(R.id.spinnerCard);
		/*if(position == 0){
			row.setBackgroundResource(R.drawable.spinner_border);
		}else{
			row.setBackgroundResource(R.color.theme_second_color);
		}*/
		TextView name = (TextView) row.findViewById(R.id.spinnerText);
		name.setText(key);
		row.setTag(current.get(key));
		name.setTextColor(Color.BLACK);
		if(textViewBGColor != -1){
			name.setBackgroundResource(textViewBGColor);
		}
		
		name.setGravity(textViewGravity);
		return row;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.spinner_row, parent, false);
		}
		Map<String, Object> current = objectMpList.get(position);
		String key = current.keySet().iterator().next();
		TextView name = (TextView) row.findViewById(R.id.spinnerText);
		name.setText(key);
		row.setTag(current.get(key));
		name.setTextColor(Color.BLACK);
		if(textViewBGColor != -1){
			name.setBackgroundResource(textViewBGColor);
		}
		name.setGravity(textViewGravity);
		return row;
	}
	@Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
        	
        	Map<String, Object> current = (Map<String, Object>)resultValue;
    		String key = current.keySet().iterator().next();     
            return key;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
        	suggestions.clear();
            
            FilterResults filterResults = new FilterResults();
            if(constraint != null) {
                for (Map<String, Object> current : objectMpListAll) {
                	String key = current.keySet().iterator().next(); 
                    if(key.toLowerCase().startsWith(constraint.toString().toLowerCase())){
                        suggestions.add(current);
                    }
                }
            } else{
            	suggestions.addAll(objectMpListAll);
            }
            filterResults.values = suggestions;
            filterResults.count = suggestions.size();
            return filterResults;
          
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<Map<String, Object>> filteredList = (ArrayList<Map<String, Object>>) results.values;
            if(results != null && results.count > 0) {
                clear();
                for (Map<String, Object> c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };
}