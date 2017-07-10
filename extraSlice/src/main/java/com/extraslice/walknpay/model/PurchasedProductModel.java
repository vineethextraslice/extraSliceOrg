package com.extraslice.walknpay.model;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONTokener;

import com.extraslice.walknpay.bl.CustomException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchasedProductModel extends ProductModel {

	double rewardsAmount;
	double taxAmount;
	double purchasedQuantity;
	double offerAppliedQty;
	double offerAppliedAmt;
	public PurchasedProductModel() {

	}

	public PurchasedProductModel(ProductModel model) {
		this.setAvailableQty(model.getAvailableQty());
		this.setCode(model.getCode());
		this.setId(model.getId());
		this.setName(model.getName());
		this.setPrice(model.getPrice());
		this.setStoreId(model.getStoreId());
		this.setStoreItemId(model.getStoreItemId());
		this.setTaxPercentage(model.getTaxPercentage());
		this.setOnDemandItem(model.isOnDemandItem());
	}

	public double getRewardsAmount() {
		return rewardsAmount;
	}

	public void setRewardsAmount(double rewardsAmount) {
		this.rewardsAmount = rewardsAmount;
	}

	public double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public double getPurchasedQuantity() {
		return purchasedQuantity;
	}

	public void setPurchasedQuantity(double purchasedQuantity) {
		this.purchasedQuantity = purchasedQuantity;
	}

	
	public double getOfferAppliedQty() {
		return offerAppliedQty;
	}

	public void setOfferAppliedQty(double offerAppliedQty) {
		this.offerAppliedQty = offerAppliedQty;
	}

	public double getOfferAppliedAmt() {
		return offerAppliedAmt;
	}

	public void setOfferAppliedAmt(double offerAppliedAmt) {
		this.offerAppliedAmt = offerAppliedAmt;
	}

	public JSONObject toJSonObject() throws JsonGenerationException, JsonMappingException, IOException, JSONException {
		ObjectMapper mapper = new ObjectMapper();
		JSONTokener tokener = new JSONTokener(mapper.writeValueAsString(this));
		JSONObject obj = new JSONObject(tokener);
		return obj;
	}

	public PurchasedProductModel jSonToObject(String jsonString) throws CustomException {
		PurchasedProductModel uModel = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			uModel = mapper.readValue(jsonString, PurchasedProductModel.class);
		} catch (JsonParseException e) {
			throw new CustomException("Error while parsing input string : " + e.getLocalizedMessage());
		} catch (JsonMappingException e) {
			throw new CustomException("Error while  parsing input string : " + e.getLocalizedMessage());
		} catch (IOException e) {
			throw new CustomException("Error while  parsing input string : " + e.getLocalizedMessage());
		}
		return uModel;
	}

}
