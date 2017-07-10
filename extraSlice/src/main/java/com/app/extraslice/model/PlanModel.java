package com.app.extraslice.model;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONTokener;

import com.app.extraslice.utils.CustomException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlanModel {

	String planName;
	int planId;
	String planDesc;
	double planPrice;
	String planStartDate;
	String planEndtDate;
	int planDuarationInDays;
	List<ResourceTypeModel> resourceTypeList;
	boolean purchaseOnSpot;
	String planDuaration;
	int subStartDay;
	int noOfDesks;
	int noOfMembers;
	int parentPlanId;
	boolean isSubPlan;
	boolean haveSubPlan;
	List<PlanModel> subPlanList;
	int noOfPlans =1;
	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public int getPlanId() {
		return planId;
	}

	public void setPlanId(int planId) {
		this.planId = planId;
	}

	public String getPlanDesc() {
		return planDesc;
	}

	public void setPlanDesc(String planDesc) {
		this.planDesc = planDesc;
	}

	public double getPlanPrice() {
		return planPrice;
	}

	
	public int getSubStartDay() {
		return subStartDay;
	}

	public void setSubStartDay(int subStartDay) {
		this.subStartDay = subStartDay;
	}

	public void setPlanPrice(double planPrice) {
		this.planPrice = planPrice;
	}

	

	public String getPlanStartDate() {
		return planStartDate;
	}

	public void setPlanStartDate(String planStartDate) {
		this.planStartDate = planStartDate;
	}

	public String getPlanEndtDate() {
		return planEndtDate;
	}

	public void setPlanEndtDate(String planEndtDate) {
		this.planEndtDate = planEndtDate;
	}

	public int getPlanDuarationInDays() {
		return planDuarationInDays;
	}

	public List<ResourceTypeModel> getResourceTypeList() {
		return resourceTypeList;
	}

	public void setResourceTypeList(List<ResourceTypeModel> resourceTypeList) {
		this.resourceTypeList = resourceTypeList;
	}

	public void setPlanDuarationInDays(int planDuarationInDays) {
		this.planDuarationInDays = planDuarationInDays;
	}

	
	public boolean isPurchaseOnSpot() {
		return purchaseOnSpot;
	}

	public void setPurchaseOnSpot(boolean purchaseOnSpot) {
		this.purchaseOnSpot = purchaseOnSpot;
	}

	public String getPlanDuaration() {
		return planDuaration;
	}

	public void setPlanDuaration(String planDuaration) {
		this.planDuaration = planDuaration;
	}

	public int getNoOfDesks() {
		return noOfDesks;
	}

	public void setNoOfDesks(int noOfDesks) {
		this.noOfDesks = noOfDesks;
	}

	public int getNoOfMembers() {
		return noOfMembers;
	}

	public void setNoOfMembers(int noOfMembers) {
		this.noOfMembers = noOfMembers;
	}

	public int getParentPlanId() {
		return parentPlanId;
	}

	public void setParentPlanId(int parentPlanId) {
		this.parentPlanId = parentPlanId;
	}

	public boolean isSubPlan() {
		return isSubPlan;
	}

	public void setSubPlan(boolean isSubPlan) {
		this.isSubPlan = isSubPlan;
	}

	public boolean isHaveSubPlan() {
		return haveSubPlan;
	}

	public void setHaveSubPlan(boolean haveSubPlan) {
		this.haveSubPlan = haveSubPlan;
	}

	public List<PlanModel> getSubPlanList() {
		return subPlanList;
	}

	public void setSubPlanList(List<PlanModel> subPlanList) {
		this.subPlanList = subPlanList;
	}

	public int getNoOfPlans() {
		return noOfPlans;
	}

	public void setNoOfPlans(int noOfPlans) {
		this.noOfPlans = noOfPlans;
	}

	public JSONObject toJSonObject() throws JsonGenerationException, JsonMappingException, IOException, JSONException {
		ObjectMapper mapper = new ObjectMapper();
		JSONTokener tokener = new JSONTokener(mapper.writeValueAsString(this));
		JSONObject obj = new JSONObject(tokener);
		return obj;
	}

	public PlanModel jSonToObject(String jsonString) throws CustomException {
		PlanModel uModel = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			uModel = mapper.readValue(jsonString, PlanModel.class);
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
