package com.extraslice.walknpay.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.app.extraslice.R;
import com.extraslice.walknpay.adapter.WalletReceiptAdapter;
import com.extraslice.walknpay.bl.TransactionBO;
import com.extraslice.walknpay.bl.UserBO;
import com.extraslice.walknpay.bl.Utilities;
import com.extraslice.walknpay.dao.ProgressClass;
import com.extraslice.walknpay.model.ProductModel;
import com.extraslice.walknpay.model.UserModel;

public class ProfileFragment extends Fragment {

	public ProfileFragment() {
	}
	public ProfileFragment(UserModel userModel) {
		this.userModel = userModel;
	}
	Switch autoEmail;
	TextView reset;
	TextView updateProfile,pwdErrortxt,profErrortxt;
	EditText currPwd,newPwd,confNewPwd;
	EditText state,zip,addr1,addr2,addr3,lName,fName;
	UserModel userModel;
	Dialog changePwd,changeProfile;
	LinearLayout pwdErrorLyt,profErrorLyt;
	Dialog dialog ;
	FragmentManager fragmentManager;
	Context mContext;
	boolean isOnLoad;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		mContext = getActivity();
		isOnLoad =true;
		View rootView = inflater.inflate(R.layout.profile_view, container,false);
		autoEmail = (Switch)rootView.findViewById(R.id.autoEmail);
		reset = (TextView)rootView.findViewById(R.id.reset);
		updateProfile = (TextView)rootView.findViewById(R.id.updateProfile);
		
		
		SpannableString resetStr = new SpannableString("Change");
		resetStr.setSpan(new UnderlineSpan(), 0, resetStr.length(), 0);
		reset.setText(resetStr);
		
		SpannableString profileStr = new SpannableString("Update");
		profileStr.setSpan(new UnderlineSpan(), 0, profileStr.length(), 0);
		updateProfile.setText(profileStr);
		
		changePwd = new Dialog(mContext);
		changePwd.requestWindowFeature(Window.FEATURE_NO_TITLE);
		changePwd.setContentView(R.layout.change_password_popup);
		changePwd.setCanceledOnTouchOutside(false);
		pwdErrortxt = (TextView)changePwd.findViewById(R.id.errortxt);
		pwdErrorLyt = (LinearLayout)changePwd.findViewById(R.id.errorLyt);
		Button change = (Button)changePwd.findViewById(R.id.change);
		Button cancel = (Button)changePwd.findViewById(R.id.cancel);
		currPwd = (EditText)changePwd.findViewById(R.id.currPwd);
		newPwd = (EditText)changePwd.findViewById(R.id.newPwd);
		confNewPwd = (EditText)changePwd.findViewById(R.id.confNewPwd);
		
		
		changeProfile= new Dialog(mContext);
		changeProfile.requestWindowFeature(Window.FEATURE_NO_TITLE);
		changeProfile.setContentView(R.layout.edit_profile_popup);
		changeProfile.setCanceledOnTouchOutside(false);
		profErrortxt = (TextView)changeProfile.findViewById(R.id.errortxt);
		profErrorLyt = (LinearLayout)changeProfile.findViewById(R.id.errorLyt);
		Button update = (Button)changeProfile.findViewById(R.id.update);
		Button cancelProf = (Button)changeProfile.findViewById(R.id.cancel);
		fName = (EditText)changeProfile.findViewById(R.id.fName);
		lName = (EditText)changeProfile.findViewById(R.id.lName);	
		addr3 = (EditText)changeProfile.findViewById(R.id.addr3);
		addr2 = (EditText)changeProfile.findViewById(R.id.addr2);
		addr1 = (EditText)changeProfile.findViewById(R.id.addr1);
		zip = (EditText)changeProfile.findViewById(R.id.zip);
		state = (EditText)changeProfile.findViewById(R.id.state);
		if(userModel != null){
			autoEmail.setChecked(userModel.isAutoEmail());
			fName.setText(userModel.getFirstName() != null ? userModel.getFirstName() : "");
			lName.setText(userModel.getLastName() != null ? userModel.getLastName() : "");
			addr1.setText(userModel.getAddressLine1() != null ? userModel.getAddressLine1() : "");
			addr2.setText(userModel.getAddressLine2() != null ? userModel.getAddressLine2() : "");
			addr3.setText(userModel.getAddressLine3() != null ? userModel.getAddressLine3() : "");
			zip.setText(userModel.getZip() != null ? userModel.getZip() : "");
			state.setText(userModel.getState() != null ? userModel.getState() : "");
		}else{
			autoEmail.setChecked(true);
		}
		autoEmail.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				dialog = new Dialog(mContext);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.wnp_custom_alert);
				
				TextView text = (TextView) dialog.findViewById(R.id.title_view);
				if (isChecked) {
					text.setText("Do you want to enable auto email?");
				}else{
					text.setText("Do you want to disable auto email?");
				}
				Button dialogButton = (Button) dialog.findViewById(R.id.yes_button);
				dialogButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						userModel.setAutoEmail(autoEmail.isChecked());
						updatUser();
						dialog.dismiss();
						
					}
				});
				Button negativebutton = (Button) dialog.findViewById(R.id.no_button);
				// if button is clicked, close the custom dialog
				negativebutton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Fragment fragment = new ProfileFragment(Utilities.loggedInUser);
						fragmentManager = getFragmentManager();
						fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
						dialog.dismiss();
					}
				});

				dialog.show();

					
			}
		});
		updateProfile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				changeProfile.show();
				
			}
		});
		cancelProf.setOnClickListener(new OnClickListener() {
			
			
			
			@Override
			public void onClick(View arg0) {
				changeProfile.dismiss();
				
			}
		});
		update.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				try {
					if(fName.getText() != null && !fName.getText().toString().equals("") ){
						userModel.setFirstName(fName.getText().toString()) ;
					}
					if(lName.getText() != null && !lName.getText().toString().equals("") ){
						userModel.setLastName(lName.getText().toString()) ;
					}
					if(addr1.getText() != null && !addr1.getText().toString().equals("") ){
						userModel.setAddressLine1(addr1.getText().toString()) ;
					}
					if(addr2.getText() != null && !addr2.getText().toString().equals("") ){
						userModel.setAddressLine2(addr2.getText().toString()) ;
					}
					if(addr3.getText() != null && !addr3.getText().toString().equals("") ){
						userModel.setAddressLine3(addr3.getText().toString()) ;
					}
					if(zip.getText() != null && !zip.getText().toString().equals("") ){
						userModel.setZip(zip.getText().toString()) ;
					}
					if(state.getText() != null && !state.getText().toString().equals("") ){
						userModel.setState(state.getText().toString()) ;
					}
					updatUser();
					changeProfile.dismiss();
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		});
		reset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				changePwd.show();
				
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			
			
			
			@Override
			public void onClick(View arg0) {
				changePwd.dismiss();
				
			}
		});
		change.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				try {
					if(!currPwd.getText().toString().equals(Utilities.decode(userModel.getPassword()))){
						pwdErrortxt.setText("Incorrect password");
						pwdErrorLyt.setVisibility(View.VISIBLE);
					}else if(!newPwd.getText().toString().equals(confNewPwd.getText().toString())){
						pwdErrortxt.setText("New password does not match with confirm password");
						pwdErrorLyt.setVisibility(View.VISIBLE);
					}else{
						userModel.setPassword(Utilities.encode(newPwd.getText().toString()));
						updatUser();
						changePwd.dismiss();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		});
		return rootView;
	}
	private void updatUser(){
		new LoadData(userModel).execute();
		
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
		}
		return true;
	}
	
	class LoadData extends AsyncTask<Void, Void, Void> {
		UserModel model;
		public LoadData(UserModel model){
			this.model = model;
		}
		@Override
		protected void onPreExecute() {	
			ProgressClass.startProgress(mContext);
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... arg0) {
			UserBO userBO = new UserBO(mContext);
			Utilities.loggedInUser = userBO.updateUser(model);
					
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			Fragment fragment = new ProfileFragment(Utilities.loggedInUser);
			fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
			ProgressClass.finishProgress();
			
		}
	}
}
