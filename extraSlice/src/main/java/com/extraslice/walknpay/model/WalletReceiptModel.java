package com.extraslice.walknpay.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WalletReceiptModel {

	String userName;
	String orderId;
	String storeName;
	String orderDate;

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	ArrayList<ReceiptItemModel> arrayModel;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public void setReceiptArray(ArrayList<ReceiptItemModel> arrayModel) {
		// TODO Auto-generated method stub
		this.arrayModel = arrayModel;
	}

	public ArrayList<ReceiptItemModel> getReceiptArray() {
		// TODO Auto-generated method stub
		return arrayModel;
	}
}
