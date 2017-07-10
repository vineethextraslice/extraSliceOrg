package com.extraslice.walknpay.ui;

import android.text.InputFilter;
import android.text.Spanned;

public class DecimalDigitsInputFilter implements InputFilter {
	private final int decimalDigits;

	public DecimalDigitsInputFilter(int decimalDigits) {
		this.decimalDigits = decimalDigits;

	}

	@Override
	public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

		int dotPos = -1;
		int len = dest.length();
		for (int i = 0; i < len; i++) {
			char c = dest.charAt(i);
			if (c == '.' || c == ',') {
				dotPos = i;
				break;
			}
		}
		if (dotPos >= 0) {

			if (dend >= 9) {
				return "";
			}

			// protects against many dots
			if (source.equals(".") || source.equals(",")) {
				return "";
			}

			// if the text is entered before the dot
			if (dend <= dotPos) {
				return null;
			}
			if (len - dotPos > decimalDigits) {
				return "";
			}

		} else {

			// after integer part
			if (source.equals(".")) {
				return ".";
				// to limit decimal part
			} else if (dend >= 6) {
				return "";
			}
		}

		return null;
	}
}