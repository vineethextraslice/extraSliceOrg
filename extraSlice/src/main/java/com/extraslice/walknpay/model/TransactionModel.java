package com.extraslice.walknpay.model;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONTokener;

import com.extraslice.walknpay.bl.CustomException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionModel  implements Comparable<TransactionModel>{
	long orderId;
	int userId;
	String userName;
	int storeId;
	StoreModel recieptStore;
	List<PurchasedProductModel> itemList;
	String subTotal;
	String grossTotal;
	String totalTax;
	String offerTotal;
	String payableTotal;
	String orderDate;
	String storeName;
	String payMethod;
	String deviceType;
	String receiptFor;
	List<CouponModel> couponList;

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}


	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	

	public StoreModel getRecieptStore() {
		return recieptStore;
	}

	public void setRecieptStore(StoreModel recieptStore) {
		this.recieptStore = recieptStore;
	}


	public List<PurchasedProductModel> getItemList() {
		if (itemList == null) {
			itemList = new ArrayList<PurchasedProductModel>();
		}
		return itemList;
	}

	public void setItemList(List<PurchasedProductModel> itemList) {
		this.itemList = itemList;
	}

	public String getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
	}

	public String getGrossTotal() {
		return grossTotal;
	}

	public void setGrossTotal(String grossTotal) {
		this.grossTotal = grossTotal;
	}

	public String getTotalTax() {
		return totalTax;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public void setTotalTax(String totalTax) {
		this.totalTax = totalTax;
	}

	
	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public List<CouponModel> getCouponList() {
		return couponList;
	}

	public void setCouponList(List<CouponModel> couponList) {
		this.couponList = couponList;
	}

	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}
	
	
	public String getOfferTotal() {
		return offerTotal;
	}

	public void setOfferTotal(String offerTotal) {
		this.offerTotal = offerTotal;
	}

	public String getPayableTotal() {
		return payableTotal;
	}

	public void setPayableTotal(String payableTotal) {
		this.payableTotal = payableTotal;
	}

	public String getReceiptFor() {
		return receiptFor;
	}

	public void setReceiptFor(String receiptFor) {
		this.receiptFor = receiptFor;
	}

	public JSONObject toJSonObject() throws JsonGenerationException, JsonMappingException, IOException, JSONException {
		ObjectMapper mapper = new ObjectMapper();
		JSONTokener tokener = new JSONTokener(mapper.writeValueAsString(this));
		JSONObject obj = new JSONObject(tokener);
		return obj;
	}

	public TransactionModel jSonToObject(String jsonString) throws CustomException {
		TransactionModel uModel = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			uModel = mapper.readValue(jsonString, TransactionModel.class);
		} catch (JsonParseException e) {
			throw new CustomException("Error while parsing input string : " + e.getLocalizedMessage());
		} catch (JsonMappingException e) {
			throw new CustomException("Error while  parsing input string : " + e.getLocalizedMessage());
		} catch (IOException e) {
			throw new CustomException("Error while  parsing input string : " + e.getLocalizedMessage());
		}
		return uModel;
	}
	@Override
	@JsonIgnore
	public int compareTo(TransactionModel arg0) {
		SimpleDateFormat ymd_Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0");
		try {
			if(ymd_Format.parse(arg0.getOrderDate()).after(ymd_Format.parse(this.getOrderDate()))){
				return 1;
			}else{
				return -1;
			}
		} catch (ParseException e) {
			return -1;
		}
	}
}
