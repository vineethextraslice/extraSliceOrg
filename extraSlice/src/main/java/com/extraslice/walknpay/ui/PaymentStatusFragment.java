package com.extraslice.walknpay.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.extraslice.R;
import com.extraslice.walknpay.bl.Utilities;
import com.extraslice.walknpay.model.TransactionModel;

public class PaymentStatusFragment extends Fragment {

	Context context;

	// backCart,backReceipt;
	TextView success_message, errorMessageView, emailMessageView, cartValue;

	LinearLayout cart, receipt, success_tab, fail_tab;
	boolean status;
	String errorMessage;
	TransactionModel model = new TransactionModel();
	String email = null;
	boolean emailStatus = false;
	public PaymentStatusFragment(){
		
	}
	public PaymentStatusFragment(boolean status, String message, TransactionModel model, String email, boolean emailStatus) {
		this.status = status;
		this.errorMessage = message;
		this.model = model;
		this.email = email;
		this.emailStatus = emailStatus;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.payment_status_screen, container, false);
		context = this.getActivity();

		this.cart = (LinearLayout) rootView.findViewById(R.id.cart);
		this.receipt = (LinearLayout) rootView.findViewById(R.id.receipt);
		this.success_tab = (LinearLayout) rootView.findViewById(R.id.success_tab);
		this.fail_tab = (LinearLayout) rootView.findViewById(R.id.fail_tab);
		this.cartValue = (TextView) rootView.findViewById(R.id.backCart);
		success_message = (TextView) rootView.findViewById(R.id.success_txn_id);
		errorMessageView = (TextView) rootView.findViewById(R.id.fail_error_message);
		emailMessageView = (TextView) rootView.findViewById(R.id.emailmsg);

		if (this.email != null && !this.email.trim().equals("")) {
			if (emailStatus) {
				emailMessageView.setText("The reciept has been send to your email " + email);
			} else {
				emailMessageView.setText("We are sorry, the reciept could not sent to your email " + email + " due to some technical problem");
			}
			emailMessageView.setVisibility(View.VISIBLE);
		} else {
			emailMessageView.setVisibility(View.GONE);
		}

		receipt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Fragment fragment = new ReceiptFragment(model, -1, -1);
				if (fragment != null) {
					FragmentManager fragmentManager = getFragmentManager();

					fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
					// update selected item and title, then close the drawer

				}

			}
		});

		cart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Fragment fragment = new CartFragment();
				if (fragment != null) {
					FragmentManager fragmentManager = getFragmentManager();

					fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();

					// update selected item and title, then close the drawer

				}

			}
		});

		if (status) {
			String message = success_message.getText().toString();
			message = message + " " + errorMessage;
			success_message.setText(message);
			cart.setVisibility(View.GONE);
			receipt.setVisibility(View.VISIBLE);
			if (Utilities.subtotal_amt != null) {
				Utilities.subtotal_amt = "00.00";
			}
			if (Utilities.Tax1 != null) {
				Utilities.Tax1 = "00.00";
			}
			if (Utilities.total1 != null) {
				Utilities.total1 = "00.00";
			}
			Utilities.pay_finish = true;
			Utilities.total = 0.00;
			// sendEmail(model);
			if (CartFragment.productslist != null && CartFragment.productslist.size() > 0) {
				CartFragment.productslist.clear();

			}
			Utilities.setTotalCount();
			success_tab.setVisibility(View.VISIBLE);
			fail_tab.setVisibility(View.GONE);
		} else {
			errorMessageView.setText(errorMessage);
			errorMessageView.setTextColor(Color.RED);
			cart.setVisibility(View.VISIBLE);
			receipt.setVisibility(View.GONE);
			errorMessageView.setVisibility(View.VISIBLE);
			success_tab.setVisibility(View.GONE);
			fail_tab.setVisibility(View.VISIBLE);

			if (CartFragment.productslist != null && CartFragment.productslist.size() > 0) {
				int test = 0;

				for (int i = 0; i < CartFragment.productslist.size(); i++) {

					test += (int) CartFragment.productslist.get(i).getPurchasedQuantity();

				}

				String itemNos = String.valueOf(test);
				this.cartValue.setText(itemNos);

			} else {

				this.cartValue.setText("0");
			}

		}
		rootView.setFocusableInTouchMode(true);
		rootView.requestFocus();
		rootView.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (keyCode == KeyEvent.KEYCODE_BACK) {

					//if (status) {
						final Context context = getActivity();
						Fragment fragment = null;
						fragment = new CartFragment();
						if (fragment != null) {
							FragmentManager fragmentManager = getFragmentManager();
							fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
						}
/*						

					} else {
						Fragment fragment = null;
						fragment = new HomeFragment();
						if (fragment != null) {
							FragmentManager fragmentManager = getFragmentManager();
							fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
						}
					}*/

					return true;
				} else {
					return false;
				}
			}

		});

		return rootView;

	}

	public void onBackPressed() {
		if (getFragmentManager().getBackStackEntryCount() == 0) {

			Fragment fragment = null;
			fragment = new WalletFragment(0,0,0,0);
			if (fragment != null) {
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();

			} else {
				getFragmentManager().popBackStack();
			}

		}
	}

}
