package com.app.extraslice.model;

import com.stripe.model.Charge;
import com.stripe.model.Customer;

public class PaymentStatusObject {
	Customer stripeCustomer;
	Charge dtripeCharge;
	public Customer getStripeCustomer() {
		return stripeCustomer;
	}
	public void setStripeCustomer(Customer stripeCustomer) {
		this.stripeCustomer = stripeCustomer;
	}
	public Charge getDtripeCharge() {
		return dtripeCharge;
	}
	public void setDtripeCharge(Charge dtripeCharge) {
		this.dtripeCharge = dtripeCharge;
	}
	

}
