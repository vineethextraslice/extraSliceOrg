package com.app.extraslice.model;

import com.app.extraslice.utils.CustomException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONTokener;

import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResourceTypeModel implements Comparable<ResourceTypeModel>{

	int resourceTypeId;
	String resourceTypeName;
	double planLimit;
	String planLimitUnit;
	String allowUsageBy;
	double currentUsage;
	double planSplPrice;
	int orgId;
	String resourceDesc;
	int noOfAddOns;
	
	public int getResourceTypeId() {
		return resourceTypeId;
	}

	public void setResourceTypeId(int resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
	}

	public String getResourceTypeName() {
		return resourceTypeName;
	}

	public void setResourceTypeName(String resourceTypeName) {
		this.resourceTypeName = resourceTypeName;
	}

	public double getPlanLimit() {
		return planLimit;
	}

	public void setPlanLimit(double planLimit) {
		this.planLimit = planLimit;
	}

	public String getPlanLimitUnit() {
		return planLimitUnit;
	}

	public void setPlanLimitUnit(String planLimitUnit) {
		this.planLimitUnit = planLimitUnit;
	}

	public double getCurrentUsage() {
		return currentUsage;
	}

	public void setCurrentUsage(double currentUsage) {
		this.currentUsage = currentUsage;
	}

	public double getPlanSplPrice() {
		return planSplPrice;
	}

	public void setPlanSplPrice(double planSplPrice) {
		this.planSplPrice = planSplPrice;
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public String getAllowUsageBy() {
		return allowUsageBy;
	}

	public void setAllowUsageBy(String allowUsageBy) {
		this.allowUsageBy = allowUsageBy;
	}

	public String getResourceDesc() {
		return resourceDesc;
	}

	public void setResourceDesc(String resourceDesc) {
		this.resourceDesc = resourceDesc;
	}

	public int getNoOfAddOns() {
		return noOfAddOns;
	}

	public void setNoOfAddOns(int noOfAddOns) {
		this.noOfAddOns = noOfAddOns;
	}

	public JSONObject toJSonObject() throws JsonGenerationException, JsonMappingException, IOException, JSONException {
		ObjectMapper mapper = new ObjectMapper();
		JSONTokener tokener = new JSONTokener(mapper.writeValueAsString(this));
		JSONObject obj = new JSONObject(tokener);
		return obj;
	}

	public ResourceTypeModel jSonToObject(String jsonString) throws CustomException {
		ResourceTypeModel uModel = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			uModel = mapper.readValue(jsonString, ResourceTypeModel.class);
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
		public int compareTo(ResourceTypeModel arg0) {
			// TODO Auto-generated method stub
			if(this.getAllowUsageBy()!= null && this.getAllowUsageBy().equals("ondemand") 
					&& arg0.getAllowUsageBy()!= null && !arg0.getAllowUsageBy().equals("ondemand")
			 ){
				return 1;
				
			}else if(this.getAllowUsageBy()!= null && !this.getAllowUsageBy().equals("ondemand") 
					&& arg0.getAllowUsageBy()!= null && arg0.getAllowUsageBy().equals("ondemand")
			 ){
				return -1;
				
			}else{
				return -1;
			}
			
		}
}
