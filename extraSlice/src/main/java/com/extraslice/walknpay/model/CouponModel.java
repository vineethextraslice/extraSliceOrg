package com.extraslice.walknpay.model;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

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
import com.extraslice.walknpay.ui.CartFragment;
import com.extraslice.walknpay.ui.PaymentOptionScreen;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CouponModel  implements Comparable<CouponModel>{

	int userId;
	int couponId;
	String couponCode;
	String couponType;
	int storeId;
	String payBy;
	int categoryId;
	String reasonForFailure;
	boolean couponApplied;
	String description;
	int offerOnProductId;
	Timestamp startDate;
	Timestamp endDate;
	int noOfUsages;
	String applyBy;
	double couponPrice;
	double offeredAmount;
	double offeredPerct;
	int offeredCount; 
	int offeredProductId;
	double offerAbovePrice;
	double offerOnCount;
	boolean includeTaxInOffer;
	String notApplicableWith;
	double applicableAmount;
		String offeredProductName;
	String offerOnProductName;
	
	@JsonIgnore
	double recalculatedOfferAmount;
	@JsonIgnore
	int offeredItemCount;
	@JsonIgnore
	double offeredItemAmount;
	@JsonIgnore
	double offeredOthItemAmount;
	@JsonIgnore
	int offeredOthItemCount;
	 
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getCouponId() {
		return couponId;
	}

	public void setCouponId(int couponId) {
		this.couponId = couponId;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public String getCouponType() {
		return couponType;
	}

	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public int getOfferOnProductId() {
		return offerOnProductId;
	}

	public void setOfferOnProductId(int offerOnProductId) {
		this.offerOnProductId = offerOnProductId;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public int getNoOfUsages() {
		return noOfUsages;
	}

	public void setNoOfUsages(int noOfUsages) {
		this.noOfUsages = noOfUsages;
	}

	public String getApplyBy() {
		return applyBy;
	}

	public void setApplyBy(String applyBy) {
		this.applyBy = applyBy;
	}

	public double getCouponPrice() {
		return couponPrice;
	}

	public void setCouponPrice(double couponPrice) {
		this.couponPrice = couponPrice;
	}

	public double getOfferedAmount() {
		return offeredAmount;
	}

	public void setOfferedAmount(double offeredAmount) {
		this.offeredAmount = offeredAmount;
	}

	public double getOfferedPerct() {
		return offeredPerct;
	}

	public void setOfferedPerct(double offeredPerct) {
		this.offeredPerct = offeredPerct;
	}

	public double getOfferAbovePrice() {
		return offerAbovePrice;
	}

	public void setOfferAbovePrice(double offerAbovePrice) {
		this.offerAbovePrice = offerAbovePrice;
	}

	public double getOfferOnCount() {
		return offerOnCount;
	}

	public void setOfferOnCount(double offerOnCount) {
		this.offerOnCount = offerOnCount;
	}

	public String getPayBy() {
		return payBy;
	}

	public void setPayBy(String payBy) {
		this.payBy = payBy;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

		public boolean isIncludeTaxInOffer() {
		return includeTaxInOffer;
	}

	public void setIncludeTaxInOffer(boolean includeTaxInOffer) {
		this.includeTaxInOffer = includeTaxInOffer;
	}

	public String getNotApplicableWith() {
		return notApplicableWith;
	}

	public void setNotApplicableWith(String notApplicableWith) {
		this.notApplicableWith = notApplicableWith;
	}

	public double getApplicableAmount() {
		return applicableAmount;
	}

	public void setApplicableAmount(double applicableAmount) {
		this.applicableAmount = applicableAmount;
	}

	public String getReasonForFailure() {
		return reasonForFailure;
	}

	public void setReasonForFailure(String reasonForFailure) {
		this.reasonForFailure = reasonForFailure;
	}

	public boolean isCouponApplied() {
		return couponApplied;
	}

	public void setCouponApplied(boolean couponApplied) {
		this.couponApplied = couponApplied;
	}

	public int getOfferedCount() {
		return offeredCount;
	}

	public void setOfferedCount(int offeredCount) {
		this.offeredCount = offeredCount;
	}

	public int getOfferedProductId() {
		return offeredProductId;
	}

	public void setOfferedProductId(int offerProductId) {
		this.offeredProductId = offerProductId;
	}

	

	

	public double getOfferedItemAmount() {
		return offeredItemAmount;
	}

	public void setOfferedItemAmount(double offeredItemAmount) {
		this.offeredItemAmount = offeredItemAmount;
	}

	public double getOfferedOthItemAmount() {
		return offeredOthItemAmount;
	}

	public void setOfferedOthItemAmount(double offeredOthItemAmount) {
		this.offeredOthItemAmount = offeredOthItemAmount;
	}

	public int getOfferedOthItemCount() {
		return offeredOthItemCount;
	}

	public void setOfferedOthItemCount(int offeredOthItemCount) {
		this.offeredOthItemCount = offeredOthItemCount;
	}

	public int getOfferedItemCount() {
		return offeredItemCount;
	}

	public void setOfferedItemCount(int offeredItemCount) {
		this.offeredItemCount = offeredItemCount;
	}

	public String getOfferedProductName() {
		return offeredProductName;
	}

	public void setOfferedProductName(String offeredProductName) {
		this.offeredProductName = offeredProductName;
	}

	public String getOfferOnProductName() {
		return offerOnProductName;
	}

	public void setOfferOnProductName(String offerOnProductName) {
		this.offerOnProductName = offerOnProductName;
	}

	public JSONObject toJSonObject() throws JsonGenerationException, JsonMappingException, IOException, JSONException {
		ObjectMapper mapper = new ObjectMapper();
		JSONTokener tokener = new JSONTokener(mapper.writeValueAsString(this));
		JSONObject obj = new JSONObject(tokener);
		return obj;
	}

	

	public double getRecalculatedOfferAmount() {
		return recalculatedOfferAmount;
	}

	public void setRecalculatedOfferAmount(double offerAppliedAmount) {
		this.recalculatedOfferAmount = offerAppliedAmount;
	}

	public CouponModel jSonToObject(String jsonString) throws CustomException {
		CouponModel uModel = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			uModel = mapper.readValue(jsonString, CouponModel.class);
		} catch (JsonParseException e) {
			throw new CustomException("Error while parsing input string : " + e.getLocalizedMessage());
		} catch (JsonMappingException e) {
			throw new CustomException("Error while  parsing input string : " + e.getLocalizedMessage());
		} catch (IOException e) {
			throw new CustomException("Error while  parsing input string : " + e.getLocalizedMessage());
		}
		return uModel;
	}
	@JsonIgnore
	public void calcualteOfferAmount(boolean selected,boolean reallocate) {
		int totalCount = 0;
		double totalAmount = 0;
		boolean offerApplicable = false;

		Map<Integer, PurchasedProductModel> itemIdList = new HashMap<Integer, PurchasedProductModel>();
		for (PurchasedProductModel item : CartFragment.productslist) {
			totalCount = totalCount + (int) item.getPurchasedQuantity();
			totalAmount = totalAmount + (item.getPurchasedQuantity() * item.getPrice());
			if (item.getId() == this.getOfferOnProductId() || this.getOfferedProductId() == item.getId()) {
				itemIdList.put(item.getId(), item);
			}
		}
		if (this.getOfferOnProductId() > 0) {
			int offerApplicableQty = 0;

			PurchasedProductModel pm = itemIdList.get(this.getOfferOnProductId());
			if (pm != null) {
				if(reallocate){
					this.setOfferedItemAmount(0);
					pm.setOfferAppliedQty(pm.getOfferAppliedQty()-this.getOfferedItemCount());
					this.setOfferedItemCount(0);
					pm.setOfferAppliedAmt(pm.getOfferAppliedAmt()-this.getOfferedItemAmount());
					this.setOfferedItemCount(0);
					if (this.getOfferedProductId() > 0){
						PurchasedProductModel offerModel = itemIdList.get(this.getOfferedProductId());
						if(offerModel != null){
							offerModel.setOfferAppliedQty(offerModel.getOfferAppliedQty()-this.getOfferedOthItemCount());
							this.setOfferedOthItemCount(0);
							offerModel.setOfferAppliedAmt(offerModel.getOfferAppliedAmt()-this.getOfferedOthItemAmount());
							this.setOfferedOthItemAmount(0);
						}
					}
				}
				offerApplicableQty = (int) (pm.getPurchasedQuantity() - pm.getOfferAppliedQty());
				recalculatedOfferAmount = (offerApplicableQty * pm.getPrice()) - pm.getOfferAppliedAmt();
				if (PaymentOptionScreen.totalAmountForOffer < recalculatedOfferAmount) {
					recalculatedOfferAmount = PaymentOptionScreen.totalAmountForOffer;
				}
				if (recalculatedOfferAmount < 0) {
					recalculatedOfferAmount = 0;
					return;
				}
				if (this.getOfferOnCount() > 0) {
					if (offerApplicableQty >= this.getOfferOnCount()) {
						int actualApplicableQty = (int) (offerApplicableQty / this.getOfferOnCount());
						if(actualApplicableQty > 0){
							if (this.getOfferedProductId() > 0) {
								PurchasedProductModel offeredModel = itemIdList.get(this.getOfferedProductId());
								if(offeredModel != null && offeredModel.getPurchasedQuantity() > 0){
									offerApplicable = calculateOfferOnOtherProduct(selected,actualApplicableQty,offeredModel);
								}else{
									recalculatedOfferAmount = 0;
									offerApplicable = false;
								}
							} else {
								if (this.getOfferedAmount() > 0) {
									offerApplicable = true;
									if (this.getOfferedAmount() <= recalculatedOfferAmount) {
										recalculatedOfferAmount = this.getOfferedAmount();
									}
								} else if (this.getOfferedPerct() > 0) {
									recalculatedOfferAmount = recalculatedOfferAmount * this.getOfferedPerct() / 100;
									offerApplicable = true;
								} else if (this.getOfferedCount() > 0) {
									actualApplicableQty = (int) (offerApplicableQty / (this.getOfferOnCount()+this.getOfferedCount()));
									if(actualApplicableQty > 0){
										recalculatedOfferAmount = (actualApplicableQty * this.getOfferedCount()) * pm.getPrice();
										offerApplicable = true;
									}else{
										recalculatedOfferAmount = 0;
										offerApplicable = false;
									}
								}
							}
						}else{
							recalculatedOfferAmount = 0;
							offerApplicable = false;
						}
						if (offerApplicable && selected) {
							this.setOfferedItemCount((actualApplicableQty * ( (int)this.getOfferOnCount()+this.getOfferedCount())));
							pm.setOfferAppliedQty(pm.getOfferAppliedQty() + this.getOfferedItemCount());
						}
					}
				} else if (this.getOfferAbovePrice() > 0) {
					if (recalculatedOfferAmount >= this.getOfferAbovePrice()) {
						int actualApplicableQty = ((int) (recalculatedOfferAmount / this.getOfferAbovePrice()));
						if(actualApplicableQty > 0){
							if (this.getOfferedProductId() > 0) {
								PurchasedProductModel offeredModel = itemIdList.get(this.getOfferedProductId());
								if(offeredModel != null && offeredModel.getPurchasedQuantity() > 0){
									offerApplicable = calculateOfferOnOtherProduct(selected,actualApplicableQty,offeredModel);
								}
							} else {
								if (this.getOfferedAmount() > 0) {
									offerApplicable = true;
									if (this.getOfferedAmount() <= recalculatedOfferAmount) {
										recalculatedOfferAmount = this.getOfferedAmount();
									}
								} else if (this.getOfferedPerct() > 0) {
									offerApplicable = true;
									recalculatedOfferAmount = recalculatedOfferAmount * this.getOfferedPerct() / 100;
								} else if (this.getOfferedCount() > 0) {
									recalculatedOfferAmount = (actualApplicableQty * this.getOfferedCount()) * pm.getPrice();
									offerApplicable = true;
									
								}
							}
						}else{
							recalculatedOfferAmount = 0;
							offerApplicable = false;
						}
						if (offerApplicable && selected) {
							this.setOfferedItemAmount(recalculatedOfferAmount);
							pm.setOfferAppliedAmt(pm.getOfferAppliedAmt() + this.getOfferedItemAmount());
						}
					}
				}

			}

		} else {
			if (this.getOfferOnCount() > 0) {
				if (totalCount >= this.getOfferOnCount()) {
					double usablePrice = PaymentOptionScreen.totalAmountForOffer;
					if (this.getOfferedAmount() > 0) {
						offerApplicable = true;
						if (this.getOfferedAmount() <= usablePrice) {
							recalculatedOfferAmount = this.getOfferedAmount();
						} else {
							recalculatedOfferAmount = PaymentOptionScreen.totalAmountForOffer;
						}
					} else if (this.getOfferedPerct() > 0) {
						offerApplicable = true;
						recalculatedOfferAmount = PaymentOptionScreen.totalAmountForOffer * this.getOfferedPerct() / 100;
					}else{
						offerApplicable = false;
						recalculatedOfferAmount = 0;
					}
				}
			} else if (this.getOfferAbovePrice() > 0) {
				if (totalAmount >= this.getOfferAbovePrice()) {
					double usablePrice = PaymentOptionScreen.totalAmountForOffer;
					
					if (this.getOfferedProductId() > 0) {
						if(reallocate){
							PurchasedProductModel offerModel = itemIdList.get(this.getOfferedProductId());
							if(offerModel != null){
								offerModel.setOfferAppliedQty(offerModel.getOfferAppliedQty()-this.getOfferedOthItemCount());
								this.setOfferedOthItemCount(0);
								offerModel.setOfferAppliedAmt(offerModel.getOfferAppliedAmt()-this.getOfferedOthItemAmount());
								this.setOfferedOthItemAmount(0);
							}
						}
						int actualApplicableQty = ((int) (usablePrice / this.getOfferAbovePrice()));
						if(actualApplicableQty > 0){
							PurchasedProductModel offeredModel = itemIdList.get(this.getOfferedProductId());
							if(offeredModel != null && offeredModel.getPurchasedQuantity() > 0){
								offerApplicable = calculateOfferOnOtherProduct(selected,actualApplicableQty,offeredModel);
							}else{
								offerApplicable = false;
								recalculatedOfferAmount = 0;
							}
						}else{
							offerApplicable = false;
							recalculatedOfferAmount = 0;
						}
					}else{
						if (this.getOfferedAmount() > 0) {
							if (this.getOfferedAmount() <= usablePrice) {
								offerApplicable = true;
								recalculatedOfferAmount = this.getOfferedAmount();
							} else {
								offerApplicable = true;
								recalculatedOfferAmount = PaymentOptionScreen.totalAmountForOffer;
							}
						} else if (this.getOfferedPerct() > 0) {
							offerApplicable = true;
							recalculatedOfferAmount = PaymentOptionScreen.totalAmountForOffer * this.getOfferedPerct() / 100;
						}
					}
				}
			} else {
				double usablePrice = PaymentOptionScreen.totalAmountForOffer;
				if (this.getOfferedProductId() > 0) {
					int actualApplicableQty = ((int) (usablePrice / this.getOfferAbovePrice()));
					if(actualApplicableQty > 0){
						PurchasedProductModel offeredModel = itemIdList.get(this.getOfferedProductId());
						if(offeredModel != null && offeredModel.getPurchasedQuantity() > 0){
							offerApplicable = calculateOfferOnOtherProduct(selected,actualApplicableQty,offeredModel);
						}else{
							offerApplicable = false;
							recalculatedOfferAmount = 0;
						}
					}else{
						offerApplicable = false;
						recalculatedOfferAmount = 0;
					}
				}else{
					if (this.getOfferedAmount() > 0) {
						if (this.getOfferedAmount() <= usablePrice) {
							offerApplicable = true;
							recalculatedOfferAmount = this.getOfferedAmount();
						} else {
							offerApplicable = true;
							recalculatedOfferAmount = PaymentOptionScreen.totalAmountForOffer;
						}
					} else if (this.getOfferedPerct() > 0) {
						offerApplicable = true;
						recalculatedOfferAmount = PaymentOptionScreen.totalAmountForOffer * this.getOfferedPerct() / 100;
					} 
				}

			}

		}
		if (!offerApplicable) {
			recalculatedOfferAmount = 0;
		}else{
			if (PaymentOptionScreen.totalAmountForOffer < recalculatedOfferAmount) {
				recalculatedOfferAmount = PaymentOptionScreen.totalAmountForOffer;
			}
		}
	}

	@Override
	@JsonIgnore
	public int compareTo(CouponModel arg0) {
		// TODO Auto-generated method stub
		if(arg0.getRecalculatedOfferAmount()== this.getRecalculatedOfferAmount()){
			if(arg0.getNoOfUsages() ==1 && this.getNoOfUsages() ==1){
				if(this.getOfferedAmount() -arg0.getOfferedAmount()  >= 0){
					return 1;
				}else{
					return -1;
				}
			}else if (arg0.getNoOfUsages() ==1 ){
				if(arg0.getOfferedAmount() > arg0.getOfferedAmount()){
					return -1;
				}else{
					return 1;
				}
			}else if (this.getNoOfUsages() ==1 ){
				if(this.getOfferedAmount() > this.getOfferedAmount()){
					return 1;
				}else{
					return -1;
				}
			}else{
				return 1;
			}
			
			
		}else{
		
			if(this.getOfferedAmount() -arg0.getOfferedAmount()  >= 0){
				return -1;
			}else{
				return 1;
			}
		}
		
	}
	private boolean calculateOfferOnOtherProduct(boolean selected, int actualApplicableQty, PurchasedProductModel offerModel){
		boolean offerApplicable =false;
		int actualQuantity = actualApplicableQty;
		double actualPrice = offerModel.getPrice();
		if (this.getOfferedCount() > 0) {
			actualQuantity = actualApplicableQty * this.getOfferedCount();
			if ((offerModel.getPurchasedQuantity() - offerModel.getOfferAppliedQty()) < (actualApplicableQty * this.getOfferedCount())) {
				actualQuantity = (int)(offerModel.getPurchasedQuantity() - offerModel.getOfferAppliedQty());
				if(actualQuantity <= 0){
					actualQuantity = 0;
					offerApplicable = false;
				}else{
					offerApplicable = true;
				}
			}
			recalculatedOfferAmount = actualQuantity * actualPrice;
			if(recalculatedOfferAmount > 0){
				offerApplicable = true;
				if (selected) {
					offerModel.setOfferAppliedQty(offerModel.getOfferAppliedQty() + actualQuantity);
					this.setOfferedOthItemCount((int) actualQuantity);
				}
			}else{
				recalculatedOfferAmount = 0;
				offerApplicable = false;
			}
			
		} else if (this.getOfferedAmount() > 0) {
			recalculatedOfferAmount = this.getOfferedAmount();
			if (this.getOfferedAmount() < ((offerModel.getPurchasedQuantity() - offerModel.getOfferAppliedQty()) * actualPrice)) {
				recalculatedOfferAmount = (offerModel.getPurchasedQuantity() - offerModel.getOfferAppliedQty()) * actualPrice;
				
			} 
			if(recalculatedOfferAmount > 0){
				offerApplicable = true;
				if (selected) {
					offerModel.setOfferAppliedAmt(offerModel.getOfferAppliedAmt() + recalculatedOfferAmount);
					this.setOfferedOthItemAmount(recalculatedOfferAmount);
				}
			}
		} else if (this.getOfferedPerct() > 0) {
			recalculatedOfferAmount = (offerModel.getPurchasedQuantity() * offerModel.getPrice()) * this.getOfferedPerct() / 100;
			offerApplicable = true;
			
		}else{
			recalculatedOfferAmount = 0;
			offerApplicable = false;
		}

		return offerApplicable;
	}
	
}
