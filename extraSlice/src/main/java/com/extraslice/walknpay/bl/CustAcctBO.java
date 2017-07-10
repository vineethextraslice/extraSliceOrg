package com.extraslice.walknpay.bl;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.extraslice.walknpay.dao.CustAcctDAO;
import com.extraslice.walknpay.dao.StripeDAO;
import com.extraslice.walknpay.model.CustAcctModel;
import com.stripe.model.Card;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Token;

public class CustAcctBO {

	static Context mContext;
	CustAcctDAO cardDAO = null;
	StripeDAO strpDAO = null;
	static String strpAcct;
	static Customer customer;
	static int custId;
	public CustAcctBO(Context context) {
		mContext = context;
		
	}
	public CustAcctBO(Context context,String strpAcct) {
		mContext = context;
		if((CustAcctBO.strpAcct != null && !CustAcctBO.strpAcct.equals(strpAcct)) || CustAcctBO.custId !=Utilities.loggedInUser.getUserId()){
			customer = null;
			CustAcctBO.strpAcct=strpAcct;
			CustAcctBO.custId=Utilities.loggedInUser.getUserId();
		}
		cardDAO = new CustAcctDAO();
		strpDAO = new StripeDAO(strpAcct,mContext);
		
	}

	public Customer getUserAccount() throws CustomException{
		JSONObject acctObj = null;
		try {
			acctObj = new GetCustomer().execute().get();
			if (acctObj == null ) {
				
			} else if (((String) acctObj.get(Utilities.STATUS_STRING)).equals(Utilities.STATUS_SUCCESS)) {
				CustAcctModel model = new CustAcctModel().jSonToObject(acctObj.get("Account").toString());
				customer = strpDAO.retrieveCutomer(Utilities.decode(model.getCustomerId()));
			} 
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return customer;
	}
	public List<Card> getOrCreateCustomerAndCard(String name, String email,String cardNum, int expMonth, int expYear, String cvv,boolean saveCard) throws CustomException {
		try {
			CustAcctModel model = null;
			JSONObject acctObj = new GetCustomer().execute().get();
			try{
				if (acctObj == null ) {
					
				} else if (((String) acctObj.get(Utilities.STATUS_STRING)).equals(Utilities.STATUS_SUCCESS)) {
					model = new CustAcctModel().jSonToObject(acctObj.get("Account").toString());
					customer = strpDAO.retrieveCutomer(Utilities.decode(model.getCustomerId()));
				} else {
					if(saveCard){
						String errorMessage = (String) acctObj.get(Utilities.ERROR_MESSAGE);
						if(errorMessage != null && errorMessage.trim().equalsIgnoreCase("No customer account found")){
							customer = strpDAO.createCutomer(email, cardNum, expMonth, expYear, cvv, name, false);
							if(customer != null){
								new AddCustomer().execute().get();
							}
						}
					}
				}
				if(customer!=null){
					if(saveCard){
						addCardToCustomer(customer, name, cardNum, expMonth, expYear, cvv);
					}
					
				}
			}catch(Exception e){
				
			}	
		} catch (Exception e) {
			return null;
		} 
		if(customer!=null){
			try {
				return getAllCards(customer);
			} catch (CustomException e) {
				return null;
			}
		}else{
			return null;
		}
	}
	
	
	public List<Card> getAllCards(Customer customer) throws CustomException {
		if(customer == null && strpAcct != null){
			customer = getUserAccount();
		}
		if(customer != null){
			return strpDAO.retrieveAllCards(customer);
		}else{
			return new ArrayList<Card>();
		}
	}

	public boolean deleteCard(Customer customer,String cardId) throws CustomException {
		return strpDAO.deleteTheCard(customer, cardId);
	}

	public Card updateCard(Customer customer,String cardId, String name, int expMonth, int expYear) throws CustomException {
		return strpDAO.updateTheCard(customer, cardId, name, expMonth, expYear);
	}

	public Charge doPayment(Customer customer,String cardId, String sourceId, int amount, String description,boolean isExisting) throws CustomException {
		return strpDAO.chargeCard(customer, cardId, sourceId, amount, description, isExisting);
	}
	public Token createCardToken(String name,String cardNum, int expMonth, int expYear, String cvv)throws CustomException {
		return strpDAO.createCardToken(cardNum, expMonth, expYear, cvv, name);
	}
	public Card addCardToCustomer(Customer customer,String name,String cardNum, int expMonth, int expYear, String cvv)throws CustomException {
		return strpDAO.addCardToCustomer(customer, cardNum, expMonth, expYear, cvv, name);
	}
	  
	class GetCustomer extends AsyncTask<Void, Void, JSONObject> {
		JSONObject acctObj;		
		CustAcctDAO custDAo = new CustAcctDAO();
		
		@Override
		protected void onPreExecute() {	
			//ProgressClass.startProgress(mContext);
			super.onPreExecute();
		}
		@Override
		protected JSONObject doInBackground(Void... arg0) {
			if(customer == null){
				acctObj = custDAo.getUserAccount(mContext, Utilities.loggedInUser.getUserId(), strpAcct);
			}
			
			return acctObj;
		}
		@Override
		protected void onPostExecute(JSONObject cst) {
			super.onPostExecute(cst);
			//ProgressClass.finishProgress();
			
		}
	}
	
	class AddCustomer extends AsyncTask<Void, Void, Void> {
		JSONObject acctObj;		
		CustAcctDAO custDAo = new CustAcctDAO();
		
		public AddCustomer(){
			
			
		}
		
		@Override
		protected void onPreExecute() {	
			//ProgressClass.startProgress(mContext);
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				custDAo.addUserAccount(mContext, Utilities.loggedInUser.getUserId(), strpAcct, Utilities.encode(customer.getId()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
		@Override
		protected void onPostExecute(Void cst) {
			super.onPostExecute(cst);
			//ProgressClass.finishProgress();
			
		}
	}
}
