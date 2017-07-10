package com.extraslice.walknpay.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReceiptItemModel {

	String taxAmount;
	String rewardsAmt;
	String purchaseQty;
	String name;
	String id;
	String price;
	String code;
	String taxPercentage;
	String availableQty;
	String storeItemId;

	public String getTaxAmount() {
		return taxAmount;

	}

	public void setTaxAmount(String taxAmount) {
		this.taxAmount = taxAmount;
	}

	public String getRewardsAmt() {
		return rewardsAmt;
	}

	public void setRewardsAmt(String rewardsAmt) {
		this.rewardsAmt = rewardsAmt;
	}

	public String getPurchaseQty() {
		return purchaseQty;
	}

	public void setPurchaseQty(String purchaseQty) {
		this.purchaseQty = purchaseQty;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(String taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public String getAvailableQty() {
		return availableQty;
	}

	public void setAvailableQty(String availableQty) {
		this.availableQty = availableQty;
	}

	public String getStoreItemId() {
		return storeItemId;
	}

	public void setStoreItemId(String storeItemId) {
		this.storeItemId = storeItemId;
	}

}
