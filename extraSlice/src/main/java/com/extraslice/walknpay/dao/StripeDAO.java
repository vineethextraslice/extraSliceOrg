package com.extraslice.walknpay.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.os.AsyncTask;

import com.extraslice.walknpay.bl.CustomException;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Card;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.ExternalAccount;
import com.stripe.model.ExternalAccountCollection;
import com.stripe.model.Token;


public class StripeDAO {
	Map<String, Object> inputParams = new HashMap<String, Object>();
	String SECRETKEY_KEY;
	String API;
	Token token = null;
	CustomException exception;
	Customer cu = null;
	String custId;
	Card card = null;
	String cardId;
	ExternalAccountCollection result;
	ExternalAccount source;
	String name;
	int expMonth, expYear;
	Charge charge = null;
	static Context mContext;
	public StripeDAO(String secKey,Context mContext){
		SECRETKEY_KEY = secKey;
		StripeDAO.mContext = mContext;
	}
	public Token createCardToken(String cardNum, int expMonth, int expYear, String cvc, String name) throws CustomException {
		inputParams = new HashMap<String, Object>();
		API = "createCardToken";
		Map<String, Object> cardParams = new HashMap<String, Object>();
		cardParams.put("number", cardNum);
		cardParams.put("exp_month", expMonth);
		cardParams.put("exp_year", expYear);
		cardParams.put("cvc", cvc);
		if (name != null && !name.trim().equals("")) {
			cardParams.put("name", name);
		}
		inputParams.put("card", cardParams);
		try {
			new StripeJSON().execute().get();
		} catch (InterruptedException e) {
			exception = new CustomException(e.getLocalizedMessage());
		} catch (ExecutionException e) {
			exception = new CustomException(e.getLocalizedMessage());
		}
		if (exception != null) {
			throw exception;
		}
		return token;
	}

	public Customer createCutomer(String email, String cardNum, int expMonth, int expYear, String cvc, String name, boolean saveCard) throws CustomException {
		inputParams = new HashMap<String, Object>();
		if (saveCard) {
			Token token = createCardToken(cardNum, expMonth, expYear, cvc, name);
			inputParams = new HashMap<String, Object>();
			inputParams.put("source", token.getId());
		}
		
		inputParams.put("description", "cutomer for " + email);
		inputParams.put("email", email);
		API = "createCutomer";
		try {
			new StripeJSON().execute().get();
		} catch (InterruptedException e) {
			exception = new CustomException(e.getLocalizedMessage());
		} catch (ExecutionException e) {
			exception = new CustomException(e.getLocalizedMessage());
		}
		if (exception != null) {
			throw exception;
		}
		return cu;
	}

	public Customer retrieveCutomer(String custId) throws CustomException {
		API = "retrieveCutomer";
		inputParams = new HashMap<String, Object>();
		this.custId = custId;
		try {
			new StripeJSON().execute().get();
		} catch (InterruptedException e) {
			exception = new CustomException(e.getLocalizedMessage());
		} catch (ExecutionException e) {
			exception = new CustomException(e.getLocalizedMessage());
		}
		if (exception != null) {
			throw exception;
		}
		return cu;
	}

	public Card addCardToCustomer(Customer cu, String cardNum, int expMonth, int expYear, String cvc, String name) throws CustomException {
		
		inputParams = new HashMap<String, Object>();
		Token token = createCardToken(cardNum, expMonth, expYear, cvc, name);
		inputParams = new HashMap<String, Object>();
		this.cu = cu;
		inputParams.put("source", token.getId());
		API = "addCardToCustomer";
		if (cu != null) {
			try {
				new StripeJSON().execute().get();
			} catch (InterruptedException e) {
				exception = new CustomException(e.getLocalizedMessage());
			} catch (ExecutionException e) {
				exception = new CustomException(e.getLocalizedMessage());
			}
			if (exception != null) {
				throw exception;
			}
		}
		return card;
	}

	
	public List<Card> retrieveAllCards(Customer cu) throws CustomException {
		this.cu = cu;
		API = "retrieveAllCards";
		inputParams = new HashMap<String, Object>();
		List<Card> cardList = new ArrayList<Card>();
		inputParams.put("object", "card");
		try {
			new StripeJSON().execute().get();
		} catch (InterruptedException e) {
			exception = new CustomException(e.getLocalizedMessage());
		} catch (ExecutionException e) {
			exception = new CustomException(e.getLocalizedMessage());
		}
		if (exception != null) {
			throw exception;
		}
		if(result !=null){
			for (ExternalAccount acct : result.getData()) {
				Card card = (Card) acct;
				cardList.add(card);
			}
		}
		return cardList;
	}

	public Card retrieveTheCard(Customer cu, String cardId) throws CustomException {
		this.cu = cu;
		inputParams = new HashMap<String, Object>();
		this.cardId = cardId;
		API = "retrieveTheCard";
		try {
			new StripeJSON().execute().get();
		} catch (InterruptedException e) {
			exception = new CustomException(e.getLocalizedMessage());
		} catch (ExecutionException e) {
			exception = new CustomException(e.getLocalizedMessage());
		}
		if (exception != null) {
			throw exception;
		}
		if (source.getObject().equals("card")) {
			card = (Card) source;
		}

		return card;
	}

	public Card updateTheCard(Customer cu, String cardId, String name, int expMonth, int expYear) throws CustomException {
		this.cu=cu;
		this.cardId=cardId;
		API = "updateTheCard";
		inputParams = new HashMap<String, Object>();
		inputParams.put("name", name);
		inputParams.put("exp_month", expMonth);
		inputParams.put("exp_year", expYear);
		try {
			new StripeJSON().execute().get();
		} catch (InterruptedException e) {
			exception = new CustomException(e.getLocalizedMessage());
		} catch (ExecutionException e) {
			exception = new CustomException(e.getLocalizedMessage());
		}
		if (exception != null) {
			throw exception;
		}
		return card;
	}

	public boolean deleteTheCard(Customer cu, String cardId) throws CustomException {
		this.cu=cu;
		this.cardId=cardId;
		API = "deleteTheCard";
		inputParams = new HashMap<String, Object>();
		try {
			new StripeJSON().execute().get();
		} catch (InterruptedException e) {
			exception = new CustomException(e.getLocalizedMessage());
		} catch (ExecutionException e) {
			exception = new CustomException(e.getLocalizedMessage());
		}
		if (exception != null) {
			throw exception;
		}
		return true;

	}

	public Charge chargeCard(Customer cu, String cardId, String sourceId, int amount, String description, boolean isExisting) throws CustomException {
		API = "chargeCard";
		this.cu=cu;
		inputParams = new HashMap<String, Object>();
			inputParams.put("amount", amount);
			inputParams.put("currency", "usd");
			if (isExisting) {
				inputParams.put("customer", cu.getId());
				inputParams.put("card", cardId);
			} else {
				inputParams.put("source", sourceId);
			}

			inputParams.put("description", "Charge for " + description);
			Map<String, String> initialMetadata = new HashMap<String, String>();
			initialMetadata.put("order_id", description);
			inputParams.put("metadata", initialMetadata);
			try {
				new StripeJSON().execute().get();
			} catch (InterruptedException e) {
				exception = new CustomException(e.getLocalizedMessage());
			} catch (ExecutionException e) {
				exception = new CustomException(e.getLocalizedMessage());
			}
			if (exception != null) {
				throw exception;
			}
		
		return charge;
	}

	public class StripeJSON extends AsyncTask<Void, Void, Void> {

		public StripeJSON() {

		}

		@Override
		protected void onPreExecute() {
			exception = null;
			super.onPreExecute();
			//ProgressClass.startProgress(StripeDAO.mContext);
		}

		@Override
		protected Void doInBackground(Void... params) {
			com.stripe.Stripe.apiKey = SECRETKEY_KEY; // test account
			try {
				if (API.equals("createCardToken")) {
					token = Token.create(inputParams);
				}else if (API.equals("createCutomer")) {
					cu = Customer.create(inputParams);
				}else if (API.equals("retrieveCutomer")) {
					cu = Customer.retrieve(custId);
				}else if (API.equals("addCardToCustomer")) {
					card = cu.createCard(inputParams);
				}else if (API.equals("retrieveAllCards")) {
					result = cu.getSources().all(inputParams);
				}else if (API.equals("retrieveTheCard")) {
					source = cu.getSources().retrieve(cardId);
				}else if (API.equals("updateTheCard")) {
					source = cu.getSources().retrieve(cardId);
					card = (Card) source.update(inputParams);
				}else if (API.equals("deleteTheCard")) {
					source = cu.getSources().retrieve(cardId);
					source.delete();
				}else if (API.equals("chargeCard")) {
					charge = Charge.create(inputParams);
				}

				
			} catch (AuthenticationException e) {
				exception = new CustomException(e.getLocalizedMessage());
			} catch (InvalidRequestException e) {
				exception = new CustomException(e.getLocalizedMessage());
			} catch (APIConnectionException e) {
				exception = new CustomException(e.getLocalizedMessage());
			} catch (CardException e) {
				exception = new CustomException(e.getLocalizedMessage());
			} catch (APIException e) {
				exception = new CustomException(e.getLocalizedMessage());
			} catch (Exception e) {
				exception = new CustomException(e.getLocalizedMessage());
			}

			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			//ProgressClass.finishProgress();
		}

	}

}
