package com.app.extraslice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.codehaus.jettison.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.extraslice.bo.SmartspaceBO;
import com.app.extraslice.bo.UserBO;
import com.app.extraslice.model.Organization;
import com.app.extraslice.model.PlanModel;
import com.app.extraslice.model.PlanOfferModel;
import com.app.extraslice.model.ResourceTypeModel;
import com.app.extraslice.model.UserModel;
import com.app.extraslice.model.UserRequestModel;
import com.app.extraslice.utils.CustomException;
import com.app.extraslice.utils.CustomPaymentGateway;
import com.app.extraslice.utils.ProgressClass;
import com.app.extraslice.utils.Utilities;
import com.extraslice.walknpay.ui.CartFragment;
import com.google.gson.JsonObject;
import com.stripe.model.Charge;
import com.stripe.model.Token;


public class UserDetailsFragment extends Fragment {

	private final String ADD_USER_REQUEST ="ADD_USER_REQUEST";
	private final String ADD_USER ="ADD_USER";
	private final String GET_INDIVIDUAL ="GET_INDIVIDUAL";
	private final String GET_ALL_ORGS ="GET_ALL_ORGS";
	private final String UPDATE_ONTIME_PAYMENT ="UPDATE_ONTIME_PAYMENT";
	//private final String GET_STRIPE_PLN_MODEL ="GET_STRIPE_PLN_MODEL";
	boolean userNameAvl = true;
	Context mContext;
	RelativeLayout page;
	boolean tcAccepted =false;
	EditText phone,password,confPassword,noOfSeats;
	LinearLayout errorLyt,paymentLyt;
	TextView errorText,recPayInfo;
	AutoCompleteTextView name,userName;
	RadioGroup memberTypeRG,payRG;
	RadioButton stripeRB,paypalRB,orgRB,individualRB;
	String paymentGateWay="Stripe";
	boolean individual = true;
	boolean existingMember = false;
	boolean makePayment = true;
	boolean newOrg = false;
	Dialog dialog;
	List<String> orgNameList;
	List<ResourceTypeModel> selectedAddOnsList;
	Organization selectedOrg=null;
	List<PlanModel> selectedPlans  =null;
	View rootView ;
	double payableAmount;
	//StripePlanModel strpPlnModel; 
	String userRegCode= null;
	boolean orgNameVerified = false;
	
	PlanOfferModel offerModel;
	LinearLayout acceptTnCLyt;
	boolean isDedicated = false;
	int noOfdaystoSubsDate;
	long trialEndsAt ;
	long firstsubDate ;
	double noOFDaysInMoth;
	String message;
	int noOfdaystoNextMonth;
	double onetimeAmount=0;
	UserModel existingUserModel =null;
	String otMessage;
	boolean sendVerCode =true;
	public UserDetailsFragment(){
		
	}
	public UserDetailsFragment(UserModel userModel,boolean exitingUser,String paymentType,List<PlanModel> selectedPlans,
			List<ResourceTypeModel> selectedAddOnsList,PlanOfferModel offerModel,double payableAmount,int noOfdaystoSubsDate,long trialEndsAt,long firstsubDate,double noOFDaysInMoth,String message,
			int noOfdaystoNextMonth){
		this.existingUserModel = userModel;
		this.payableAmount = payableAmount;
		this.selectedPlans = selectedPlans;
		this.offerModel = offerModel;
		this.selectedAddOnsList = selectedAddOnsList;
		this.noOfdaystoSubsDate=noOfdaystoSubsDate;
		this.trialEndsAt = trialEndsAt;
		this.firstsubDate = firstsubDate;
		this.noOFDaysInMoth = noOFDaysInMoth;
		this.message = message;
		this.noOfdaystoNextMonth = noOfdaystoNextMonth;
		onetimeAmount =(payableAmount)*((noOfdaystoNextMonth)/(noOFDaysInMoth*1.00));
		existingMember = exitingUser;
		if(exitingUser){
			userRegCode = "Existing";
			
		}else{
			userRegCode = null;
		}
		if(paymentType != null && (paymentType.equalsIgnoreCase("offline") || paymentType.equalsIgnoreCase("alreadypaid"))){
			makePayment = false;
		}
		
			
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rootView = inflater.inflate(R.layout.signup_user_details,container, false);
        mContext = getActivity();
        page = (RelativeLayout)rootView.findViewById(R.id.loginLyt);
        Button signup_button = (Button)rootView.findViewById(R.id.signup_button);
        paymentLyt =  (LinearLayout)rootView.findViewById(R.id.paymentLyt);
        errorText = (TextView)rootView.findViewById(R.id.errorText);
        recPayInfo = (TextView)rootView.findViewById(R.id.recPayInfo);
        errorLyt = (LinearLayout)rootView.findViewById(R.id.errorLyt);
        phone= (EditText)rootView.findViewById(R.id.phone);
        noOfSeats= (EditText)rootView.findViewById(R.id.noOfSeats);
        password= (EditText)rootView.findViewById(R.id.password);
        confPassword= (EditText)rootView.findViewById(R.id.confPassword);
        name= (AutoCompleteTextView)rootView.findViewById(R.id.name);
        userName = (AutoCompleteTextView)rootView.findViewById(R.id.userName);
        memberTypeRG = (RadioGroup)rootView.findViewById(R.id.memberTypeRG);
        payRG = (RadioGroup)rootView.findViewById(R.id.payRG);
        acceptTnCLyt = (LinearLayout)rootView.findViewById(R.id.acceptTnCLyt); 
        TextView termsNCond = (TextView)rootView.findViewById(R.id.acceptTnCTxt);
        String redString = getResources().getString(R.string.termsncondition);
        termsNCond.setText(Html.fromHtml(redString));
        new RunInBackground(GET_INDIVIDUAL,null,null,null,null,null,null,null).execute();
        new RunInBackground(GET_ALL_ORGS,null,null,null,null,null,null,null).execute();
        userName.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				Log.e("hasFocus", hasFocus+"");
				if(!hasFocus){
					if(com.extraslice.walknpay.bl.Utilities.loggedInUser ==null){
						new CheckUserName(userName.getText().toString()).execute();
					}
				}
				
			}
		});
        if(selectedPlans != null){
        	onetimeAmount = (payableAmount*noOfdaystoNextMonth/noOFDaysInMoth);
        	otMessage = message.replaceAll("replace_amount", Utilities.roundto2Decimal(onetimeAmount));
    		recPayInfo.setText(otMessage);
    	}
        try {
	        if(com.extraslice.walknpay.bl.Utilities.loggedInUser !=null){
	        	this.sendVerCode =false;
	        	userName.setText(Utilities.loggedInUser.getEmail());
	        	password.setText(Utilities.decode(Utilities.loggedInUser.getPassword()));
	        	confPassword.setText(Utilities.decode(Utilities.loggedInUser.getPassword()));
	        }
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		termsNCond.setOnClickListener(new OnClickListener() {

			  @Override
			  public void onClick(View arg0) {
				  showTermsAndConditions();
			  }

			});
        stripeRB = (RadioButton)rootView.findViewById(R.id.stripeRB);
        paypalRB = (RadioButton)rootView.findViewById(R.id.paypalRB);
        orgRB = (RadioButton)rootView.findViewById(R.id.orgRB);
        individualRB = (RadioButton)rootView.findViewById(R.id.individualRB);
        ImageView backImage = (ImageView)rootView.findViewById(R.id.back);
        
        if(existingMember){
        	phone.setVisibility(View.GONE);
        }else{
        	phone.setVisibility(View.VISIBLE);
        }
        
        backImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Fragment fragment = new ShowPlanFragment(noOfdaystoSubsDate,trialEndsAt,firstsubDate,noOFDaysInMoth,message,noOfdaystoNextMonth);
				Utilities.loadFragment(getFragmentManager(), fragment, R.id.frame_container, true);
			}
		});
        
        
        memberTypeRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				selectMembershipOption();
				
			}
		});
        payRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				selectPaymentOption();
				
			}
		});
        
     
        
		
		signup_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(userNameAvl){
					errorText.setText("");
					errorLyt.setVisibility(View.GONE);
					hideVirtualKeyBoard();
					if(existingMember){
						selectedOrg = existingUserModel.getOrgList().get(0);
						if(selectedOrg.getOrgName().equalsIgnoreCase("Individual")){
							individual =true;
						}else{
							individual = false;
						}
					}
						if(individual){
							selectedOrg = SmartspaceBO.individualOrg;
							selectedOrg.setOrgRoleId(1);
						}
					
					
					
					String errorStr = validatUserDetails();
					if(errorStr != null){
						errorText.setText(errorStr);
						errorLyt.setVisibility(View.VISIBLE);
					}else{
						if(selectedPlans != null && isDedicated){
							UserRequestModel model = new UserRequestModel();
							model.setContactNo(phone.getText().toString());
							
							
							model.setEmail(userName.getText().toString());
							model.setName(name.getText().toString());
							if(noOfSeats.getText().toString().isEmpty()){
								model.setNoOfSeats(0);
							}else{
								model.setNoOfSeats(Integer.parseInt(noOfSeats.getText().toString()));
							}
							model.setPlanName(selectedPlans.get(0).getPlanName());
							new RunInBackground(ADD_USER_REQUEST,null,model,null,null,null,null,null).execute();
						} else {
							UserModel userModel = null;
							if(existingMember){
								userModel = existingUserModel;
							}else{
								userModel = new UserModel();
								List<Organization> userOrgList = new ArrayList<Organization>();
								userOrgList.add(selectedOrg);
								userModel.setOrgList(userOrgList);
								userModel.setFirstName(name.getText().toString());
								userModel.setContactNo(phone.getText().toString());
							}
							try {
								userModel.setPassword(Utilities.encode(password.getText().toString()));
							} catch (Exception e) {
							}
							userModel.setUserName(userName.getText().toString());
							userModel.setEmail(userName.getText().toString());
							if(makePayment){
								makePayment(userModel);
							}else{
								new RunInBackground(ADD_USER,userModel,null,selectedAddOnsList,offerModel,null,null,null).execute();
							}
						}
					}
				}else{
					errorText.setText("Email already registered !");
					errorLyt.setVisibility(View.VISIBLE);
				}
				
				
			}
		});
		
		if(existingMember && !makePayment){
			
			showViewExistingMemeber();
		}else if(existingMember && makePayment){
			
			showViewExistingMemeberWithPayment();
		}else{
			
			for(PlanModel mdl :selectedPlans){
				if(!mdl.isPurchaseOnSpot()){
					isDedicated = true;
					break;
				}
			}
			if(isDedicated){
				acceptTnCLyt.setVisibility(View.GONE);
				signup_button.setText("Check availability");
				showViewDedicatedPlan();
			}else{
				showViewSharedPlan();
			}
		}
		
		
		rootView.setFocusableInTouchMode(true);
		rootView.requestFocus();
		rootView.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				/*if (keyCode == KeyEvent.KEYCODE_BACK) {
					Fragment fragment = null;
					fragment = new ShowPlanFragment();
					if (fragment != null) {
						FragmentManager fragmentManager = getFragmentManager();
						Utilities.loadFragment(fragmentManager,fragment,R.id.frame_container,true);
					}
					return true;
				} else {
					return false;
				}*/
				return true;
			}
		});
		CheckBox tcCheckBox = (CheckBox)rootView.findViewById(R.id.acceptTnCCB);
		tcCheckBox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CheckBox cb = (CheckBox)v;
				hideVirtualKeyBoard();
				if(cb.isChecked()){
					tcAccepted = true;
				}else{
					tcAccepted = false;
				}
			}
		});
       return rootView;
    }
	private void showTermsAndConditions(){

		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View webViewLyt = inflater.inflate(R.layout.web_view, null, false);
		WebView webView = (WebView) webViewLyt.findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(SmartspaceBO.accountModel.getTermsNCondUrl());
		webView.setWebViewClient(new WebViewClient() {
             

                @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                ProgressClass.startProgress(mContext);

            }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url)
                {
                    
                    view.loadUrl(url);
                    return true;

                }

               @Override
               public void onPageFinished(WebView view, String url) {
            	   ProgressClass.finishProgress();
              }

            });

		Button close = (Button)webViewLyt.findViewById(R.id.closeWV);
		page.addView(webViewLyt);
		close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				page.removeView(webViewLyt);
			}
		});
  
	}
	private void showUserInfoAddedPopup(){
		
		dialog = new Dialog(mContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_dialog);
		TextView messageText= (TextView)dialog.findViewById(R.id.messageText);
		TextView header= (TextView)dialog.findViewById(R.id.header);
		header.setText("Thank you");
		messageText.setGravity(Gravity.CENTER);
		messageText.setText("Your request processed successfully. Our community manager will contact you soon.");
		Button close = (Button)dialog.findViewById(R.id.close);
		close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				resetWalkNPay();
				Intent intent = new Intent(mContext, LoginActivity.class);
				mContext.startActivity(intent);
				((Activity)mContext).finish();
				dialog.dismiss();
			}
		});
		
		dialog.show();
	}
	
	
	private void showCreatOrgPopup(){
		
		dialog = new Dialog(mContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.create_organization);
		final AutoCompleteTextView description = (AutoCompleteTextView)dialog.findViewById(R.id.description);
		final EditText phone = (EditText)dialog.findViewById(R.id.phone);
		final AutoCompleteTextView adress = (AutoCompleteTextView)dialog.findViewById(R.id.adress);
		final AutoCompleteTextView orgName = (AutoCompleteTextView)dialog.findViewById(R.id.orgName);
		final TextView popupErrorText= (TextView)dialog.findViewById(R.id.errorText);
		final LinearLayout popupErrorLyt= (LinearLayout)dialog.findViewById(R.id.errorLyt);
		Button cancel = (Button)dialog.findViewById(R.id.cancel);
		Button create = (Button)dialog.findViewById(R.id.create);
		create.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popupErrorText.setText("");
				popupErrorLyt.setVisibility(View.GONE);
				if(orgName.getText().toString().trim().isEmpty()){
					popupErrorText.setText("Please enter organization name");
					popupErrorLyt.setVisibility(View.VISIBLE);
				}else if(orgNameList != null && orgNameList.size() > 0){
					if(orgNameList.contains(orgName.getText().toString().trim().toUpperCase())){
						popupErrorText.setText("Organization name already exists");
						popupErrorLyt.setVisibility(View.VISIBLE);
					}else{
						orgNameVerified = true;
						selectedOrg = new Organization();
						selectedOrg.setAddress(adress.getText().toString());
						selectedOrg.setContactNo(phone.getText().toString());
						selectedOrg.setKeyWords(description.getText().toString());
						selectedOrg.setOrgName(orgName.getText().toString());
						selectedOrg.setOrgRoleId(1);
						dialog.dismiss();
					}
				}else{
					orgNameVerified = false;
					selectedOrg = new Organization();
					selectedOrg.setAddress(adress.getText().toString());
					selectedOrg.setContactNo(phone.getText().toString());
					selectedOrg.setKeyWords(description.getText().toString());
					selectedOrg.setOrgName(orgName.getText().toString());
					selectedOrg.setOrgRoleId(1);
					dialog.dismiss();
				}
				
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	private void selectPaymentOption(){
		int selected = payRG.getCheckedRadioButtonId();
		RadioButton selectedButton = (RadioButton) rootView.findViewById(selected);
		if (selectedButton != null){
			if(selectedButton.getText().toString().trim().equalsIgnoreCase("Paypal")) {
				paymentGateWay = "Paypal";
			}else if(selectedButton.getText().toString().trim().equalsIgnoreCase("Stripe")) {
				paymentGateWay = "Stripe";
			}
		}
	}
	
	private void selectMembershipOption(){
		int selected = memberTypeRG.getCheckedRadioButtonId();
		RadioButton selectedButton = (RadioButton) rootView.findViewById(selected);
		if (selectedButton != null){
			if(selectedButton.getText().toString().trim().equalsIgnoreCase("Individual")) {
				individual = true;
				showViewIndividual();
			}else if(selectedButton.getText().toString().trim().equalsIgnoreCase("Organization")) {
				individual = false;
				showCreatOrgPopup();
			}
		}
	}
	

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
   
    private void hideVirtualKeyBoard() {

		/*try {
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mContext.getCurrentFocus().getWindowToken(), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
    
    class CheckUserName extends AsyncTask<Void, Void, Void> {
    	JSONObject resultJSon;
    	String emailId;
    	String status;
    	String errorMsg;
    	public CheckUserName(String emailId){
    		this.emailId=emailId;
    	}
		@Override
		protected Void doInBackground(Void... params) {
			UserBO bo = new UserBO(mContext);
			try {
				resultJSon = bo.checkUserName(emailId);
			} catch (CustomException e) {
				status = "WARNING";
				errorMsg =e.getLocalizedMessage();
			}
			return null;
		}
    	
		@Override
		protected void onPostExecute(Void result) {
			if (resultJSon != null) {
				try {
					String status = (String) resultJSon.get(Utilities.STATUS_STRING);
					if (status != null && status.equals(Utilities.STATUS_SUCCESS)) {
						userNameAvl=true;
						errorText.setText("");
						errorText.setTextColor(Color.RED);
						errorLyt.setVisibility(View.GONE);
					}else if (status.equals("WARNING")) {
						userNameAvl=true;
						errorText.setText("Failed to check availability !");
						errorText.setTextColor(Color.BLUE);
						errorLyt.setVisibility(View.VISIBLE);
					}else {
						userNameAvl=false;
						errorText.setText("Email already registered !");
						errorText.setTextColor(Color.RED);
						errorLyt.setVisibility(View.VISIBLE);
					}
				} catch (Exception e) {
					errorText.setText("Failed to check availability !");
					errorText.setTextColor(Color.BLUE);
					errorLyt.setVisibility(View.VISIBLE);
				}
			}else{
				errorText.setText("Failed to check availability !");
				errorText.setTextColor(Color.BLUE);
				errorLyt.setVisibility(View.VISIBLE);
			}
			
			super.onPostExecute(result);
		}
    }
    class RunInBackground extends AsyncTask<Void, Void, Void> {
		String errMsg = null;
		SmartspaceBO smartSpaceBo;
		List<Integer> planIdList;
		UserModel userModel;
		String purpose;
		UserRequestModel userReqModel;
		boolean addedUserReq;
		String cardToken;
		CustomPaymentGatewayImpl cst;
		String refId;
		String status;
		String custId =null;
		List<ResourceTypeModel> selectedAddOnsList;
		PlanOfferModel offerModel;
		public RunInBackground(String purpose, UserModel userModel,UserRequestModel userReqModel,List<ResourceTypeModel> selectedAddOnsList,
				PlanOfferModel offerModel,String cardToken,CustomPaymentGatewayImpl cst,String refId ) {
			this.userModel = userModel;
			this.userReqModel = userReqModel;
			this.purpose = purpose;
			this.cardToken = cardToken;
			planIdList = new ArrayList<Integer>();
			this.selectedAddOnsList=selectedAddOnsList;
			this.offerModel = offerModel;
			this.refId = refId;
			if(selectedPlans != null){
				for(PlanModel pln : selectedPlans){
					planIdList.add(pln.getPlanId());
		    	}
			}
			this.cst = cst;
		}

		@Override
		protected void onPreExecute() {
			if(!purpose.equals(GET_ALL_ORGS)){
				ProgressClass.startProgress(mContext);
			}
			smartSpaceBo = new SmartspaceBO(mContext);
			
			super.onPreExecute();
			hideVirtualKeyBoard();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				UserBO userBO = new UserBO(mContext);
				if(purpose.equals(ADD_USER_REQUEST)){
					addedUserReq = userBO.addDedicatedMembershipRequest(userReqModel,selectedAddOnsList);
				}else if(purpose.equals(ADD_USER)){
					UserBO bo = new UserBO(mContext);
					userModel =bo.registerUser(userModel, selectedAddOnsList, userRegCode, cardToken, planIdList, 
							trialEndsAt, noOfdaystoSubsDate, offerModel,"STRIPE");
					custId = bo.custId;
				}else if(purpose.equals(GET_INDIVIDUAL)){
					SmartspaceBO bo = new SmartspaceBO(mContext);
					bo.getIndividualOrg();
				}else if(purpose.equals(GET_ALL_ORGS)){
					SmartspaceBO bo = new SmartspaceBO(mContext);
					orgNameList =bo.getAllOrganizationNames(0, 0);
				}else if(purpose.equals(UPDATE_ONTIME_PAYMENT)){
					SmartspaceBO bo = new SmartspaceBO(mContext);
					double amount=onetimeAmount;
					
					 
					status = bo.updatePlanForOrg(userModel.getUserId(), userModel.getOrgList().get(0).getOrgId(), planIdList, 
							"One timepayment", refId, (new Date()).getTime(), firstsubDate,"STRIPE",refId,amount);
				}
				
				
				
			} catch (CustomException e) {
				errMsg = e.getLocalizedMessage();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (errMsg != null) {
				errorText.setText(errMsg);
				errorLyt.setVisibility(View.VISIBLE);
				ProgressClass.finishProgress();
				
			} else {
				if(purpose.equals(ADD_USER_REQUEST)){
					if(addedUserReq){
						showUserInfoAddedPopup();
					}else{
						Toast.makeText(mContext, "Failed to add", Toast.LENGTH_SHORT).show();
					}
				}/*else if(purpose.equals(GET_STRIPE_PLN_MODEL)){
					Log.e("Log", strpPlnModel.getStripePlanName());
				}*/
				else if(purpose.equals(ADD_USER)){
					if(userModel != null){
						if(onetimeAmount >0 && cst != null){
							cst.userModel = userModel;
							cst.custId=custId;
							cst.PAYABLE_AMOUNT=onetimeAmount;
							cst.PURPOSE=CustomPaymentGateway.PURPOSE_MAKE_INI_PAYMENT;
							cst.USER_ID=userModel.getUserId();
							cst.submitClicked();
						}else{
							ProgressClass.finishProgress();
							Toast toast = Toast.makeText(mContext, "Sucessfully Registered.", Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
							if(sendVerCode){
								Toast toast2 = Toast.makeText(mContext, "We have send you a verification email. " +
										"Please do the verification inorder to login into the application", Toast.LENGTH_SHORT);
										toast2.setGravity(Gravity.CENTER, 0, 0);
										toast2.show();
							}
							
							resetWalkNPay();
							Intent intent = new Intent(mContext, LoginActivity.class);
							mContext.startActivity(intent);
							((Activity)mContext).finish();
						}
						//new UpdateESliceUser(userModel).execute();
					}else{
						Toast.makeText(mContext, "Failed to add", Toast.LENGTH_SHORT).show();
					}
				}else if(purpose.equals(UPDATE_ONTIME_PAYMENT)){
					if(status != null && status.equalsIgnoreCase(Utilities.STATUS_SUCCESS)){
						Toast toast = Toast.makeText(mContext, "Sucessfully Registered.", Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
						if(sendVerCode){
							Toast toast2 = Toast.makeText(mContext, "We have send you a verification email. " +
									"Please do the verification inorder to login into the application", Toast.LENGTH_SHORT);
									toast2.setGravity(Gravity.CENTER, 0, 0);
									toast2.show();
						}
						
						resetWalkNPay();
						Intent intent = new Intent(mContext, LoginActivity.class);
						mContext.startActivity(intent);
						((Activity)mContext).finish();
					}else{
						errorText.setText(status);
						errorLyt.setVisibility(View.VISIBLE);
					}
					
				}
			}
			if(purpose.equals(ADD_USER_REQUEST) || purpose.equals(GET_INDIVIDUAL)){
				ProgressClass.finishProgress();
			}
			super.onPostExecute(result);
			// ProgressClass.finishProgress();
		}
	}
    
    
    
    private void makePayment(UserModel userModel){
    	String planIds = "";
    	String planNames = "";
    	boolean isFirst = true;
    	for(PlanModel pln : selectedPlans){
    		if(isFirst){
    			planIds = pln.getPlanId()+"";
    			planNames= pln.getPlanName();
    			isFirst=false;
    		}else{
    			planIds=planIds+","+pln.getPlanId();
    			planNames= planNames+","+pln.getPlanName();
    		}
    	}
    	
    	if(paymentGateWay.equals("Stripe")){
			
			
    		CustomPaymentGatewayImpl.PLAN_NAMES        =planNames;
    		CustomPaymentGatewayImpl.PAYABLE_AMOUNT      	   = payableAmount;
    		CustomPaymentGatewayImpl.STRIPE_PAYMENT_DESCRIPTION = "(Android) One time payment for plan purchase for "+userModel.getEmail()+" for "+this.noOfdaystoNextMonth+" days";
    		CustomPaymentGatewayImpl.USER_ID             = userModel.getUserId();
    		CustomPaymentGatewayImpl.USER_NAME           = userModel.getUserName();
    		CustomPaymentGatewayImpl.PURPOSE=CustomPaymentGateway.PURPOSE_ADD_USER;
    		CustomPaymentGatewayImpl.DISPLAY_PYMNT_DESCRIPTION= otMessage;
			
			try {
				CustomPaymentGatewayImpl.STRP_API_KEY=Utilities.decode(SmartspaceBO.accountModel.getStrpSecKey());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			CustomPaymentGatewayImpl strpGateway =new CustomPaymentGatewayImpl(mContext,userModel,selectedAddOnsList,offerModel);
			strpGateway.makeStripePayment();
			
		}else{}
    }
    
    class CustomPaymentGatewayImpl extends CustomPaymentGateway{
		public CustomPaymentGatewayImpl(Context mContext,UserModel userModel,List<ResourceTypeModel> selectedAddOnsList,PlanOfferModel offerModel) {
			super(mContext,userModel,selectedAddOnsList,offerModel,null);
		}

		@Override
		public void updateTransaction(Object responseObj, boolean forced,Dialog strpPopup) {
			if(responseObj != null){
				if(responseObj instanceof Token){
					Token tkn = (Token)responseObj;
					new RunInBackground(ADD_USER, userModel, null,addOnsList,offerModel, tkn.getId(), this,null).execute();
				}else if(responseObj instanceof Charge){
					Charge ch = (Charge)responseObj;
					new RunInBackground(UPDATE_ONTIME_PAYMENT, userModel, null,addOnsList,offerModel, null, this,ch.getId()).execute();
					
				}else{
					if(responseObj instanceof String){
						String tkn = (String)responseObj;
					}else{
					}
				}
				/*String stts = (String)responseObj;
				if(stts.equals(Utilities.STATUS_SUCCESS)){
						Toast toast = Toast.makeText(mContext, "Sucessfully Registered.", Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
						Toast toast2 = Toast.makeText(mContext, "We have send you a verification email. " +
						"Please do the verification inorder to login into the application", Toast.LENGTH_SHORT);
						toast2.setGravity(Gravity.CENTER, 0, 0);
						toast2.show();
						Intent intent = new Intent(mContext, LoginActivity.class);
						mContext.startActivity(intent);
						((Activity)mContext).finish();
				}else{
					StrpePaymentSetup.errorMessage = stts;
					showError();
				}*/
			}else if(forced){
				ProgressClass.finishProgress();
				if(strpPopup != null)
					strpPopup.dismiss();
			}else{
				showError();
				ProgressClass.finishProgress();
			}
		}
	}
    
   
    private String validatUserDetails(){
    	String errorMessage = null;
    	if(!isDedicated){
    		if(!tcAccepted){
        		return "Please accept terms and conditions";
        	}
    	}
    	
    	if(userName.getText().toString().trim().isEmpty()){
    		return "Please enter email";
    	}
    	if(!userName.getText().toString().matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")){
    		return "Invalid email";
    	}
    	if(selectedPlans == null){
    		if(password.getText().toString().trim().isEmpty()){
        		return "Please enter password";
        	}
    		if(confPassword.getText().toString().trim().isEmpty()){
        		return "Please confirm password";
        	}
    		if(!password.getText().toString().equals(confPassword.getText().toString())){
        		return "Password and confirm password does not match";
        	}
    		
    	}
    	if(selectedPlans != null){
    		if(isDedicated){
    			if(phone.getText().toString().trim().isEmpty()){
        			return "Please enter contact no";
        		}
    			if(noOfSeats.getText().toString().trim().isEmpty()){
        			return "Please enter no of seats required";
        		}
    		}else{
    			if(password.getText().toString().trim().isEmpty()){
            		return "Please enter password";
            	}
    			if(confPassword.getText().toString().trim().isEmpty()){
            		return "Please confirm password";
            	}
    			if(!password.getText().toString().equals(confPassword.getText().toString())){
            		return "Password and confirm password does not match";
            	}
    		}
    	}
    	if(!individual){
    		if(selectedOrg == null){
    			return "Please select/add an organization";
    		}else if(!orgNameVerified && orgNameList != null && orgNameList.size() > 0){
				if(!existingMember && orgNameList.contains(selectedOrg.getOrgName().toUpperCase())){
					return "Organization name already exists" ;
				}
    		}
    	}
    	return errorMessage;
    }
   
    private void showViewIndividual(){
    	password.setVisibility(View.VISIBLE);
    	confPassword.setVisibility(View.VISIBLE);
    	memberTypeRG.setVisibility(View.VISIBLE);
    	if(!existingMember){
    		paymentLyt.setVisibility(View.GONE);
    		recPayInfo.setVisibility(View.VISIBLE);
    	}else{
    		paymentLyt.setVisibility(View.GONE);
    		recPayInfo.setVisibility(View.GONE);
    	}
    }
    private void showViewSelectOrg(){
    	password.setVisibility(View.VISIBLE);
    	confPassword.setVisibility(View.VISIBLE);
    	memberTypeRG.setVisibility(View.VISIBLE);
    	if(existingMember){
    		paymentLyt.setVisibility(View.GONE);
    		recPayInfo.setVisibility(View.GONE);
    	}else{
    		paymentLyt.setVisibility(View.GONE);
    		recPayInfo.setVisibility(View.VISIBLE);
    	}
    	
    }
    private void showViewExistingMemeberWithPayment(){
    	password.setVisibility(View.VISIBLE);
    	confPassword.setVisibility(View.VISIBLE);
		paymentLyt.setVisibility(View.VISIBLE);
		recPayInfo.setVisibility(View.VISIBLE);
    	password.setVisibility(View.VISIBLE);
    	confPassword.setVisibility(View.VISIBLE);
    	memberTypeRG.setVisibility(View.GONE);
    	noOfSeats.setVisibility(View.GONE);
    	name.setVisibility(View.GONE);
    }
    private void showViewExistingMemeber(){
    	password.setVisibility(View.VISIBLE);
    	confPassword.setVisibility(View.VISIBLE);
		paymentLyt.setVisibility(View.GONE);
		recPayInfo.setVisibility(View.GONE);
    	password.setVisibility(View.VISIBLE);
    	confPassword.setVisibility(View.VISIBLE);
    	memberTypeRG.setVisibility(View.GONE);
    	noOfSeats.setVisibility(View.GONE);
    	name.setVisibility(View.GONE);
    }
    private void showViewSharedPlan(){
		paymentLyt.setVisibility(View.GONE);
		recPayInfo.setVisibility(View.VISIBLE);
    	password.setVisibility(View.VISIBLE);
    	confPassword.setVisibility(View.VISIBLE);
    	memberTypeRG.setVisibility(View.VISIBLE);
    	noOfSeats.setVisibility(View.GONE);
    }
    private void showViewDedicatedPlan(){
		paymentLyt.setVisibility(View.GONE);
		recPayInfo.setVisibility(View.GONE);
    	password.setVisibility(View.GONE);
    	confPassword.setVisibility(View.GONE);
    	memberTypeRG.setVisibility(View.GONE);
    	noOfSeats.setVisibility(View.VISIBLE);
    }
    private void resetWalkNPay(){
		SmartspaceBO.accountModel=null;
		SmartspaceBO.individualOrg=null;
		Utilities.loggedInUser =null;
		com.extraslice.walknpay.bl.Utilities.selectedStore=null;
		com.extraslice.walknpay.bl.Utilities.storeList = null;
		com.extraslice.walknpay.bl.Utilities.pay_finish = false;
		com.extraslice.walknpay.bl.Utilities.loggedInUser=null;
		com.extraslice.walknpay.bl.Utilities.loginUserID=1;
		com.extraslice.walknpay.bl.Utilities.confirm_store = false;
		if (CartFragment.tvtax_amt != null) {
			CartFragment.tvtax_amt.setText("00.00");
		}
		if (CartFragment.tv_subtotal != null) {
			CartFragment.tv_subtotal.setText("00.00");
		}
		if (CartFragment.tvtotal_amt != null) {
			CartFragment.tvtotal_amt.setText("00.00");
		}
		if (CartFragment.cartlistadapter != null) {
			CartFragment.cartlistadapter.clear();
		}
	}
	
}
